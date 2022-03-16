import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText, Modal, ModalBody, FormGroup, Form, Label, Input } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IContact } from 'app/shared/model/contact.model';
import { getEntities as getContacts } from 'app/entities/contact/contact.reducer';
import { IAddress } from 'app/shared/model/address.model';
import { getEntities as getAddresses } from 'app/entities/address/address.reducer';
import { getEntities as getGivens } from 'app/entities/givens/givens.reducer';
import { getEntity, updateEntity, createEntity, reset } from './patient.reducer';
import { IPatient } from 'app/shared/model/patient.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { initialGivenFormData, initialHumanNameFormData } from 'app/shared/util/formdata-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

const MyModal = ({ children, isOpen, toggle }) => (
  <div>
    <Modal isOpen={isOpen} toggle={toggle} style={{ maxWidth: '1600px', width: '70%', margin: '10px auto' }}>
      <ModalBody>{children}</ModalBody>
    </Modal>
  </div>
);

export const PatientUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const givens = useAppSelector(state => state.givens.entities);
  const contacts = useAppSelector(state => state.contact.entities);
  const addresses = useAppSelector(state => state.address.entities);
  const patientEntity = useAppSelector(state => state.patient.entity);
  const loading = useAppSelector(state => state.patient.loading);
  const updating = useAppSelector(state => state.patient.updating);
  const updateSuccess = useAppSelector(state => state.patient.updateSuccess);
  const [givenFormData, updateGivenFormData] = React.useState(initialGivenFormData);
  const [givenList, setGivenList] = useState([]);
  const [humanNameFormData, updateHumanNameFormData] = React.useState(initialHumanNameFormData);
  const [humanNameList, setHumanNameList] = useState([]);
  const [isRequired, setRequired] = useState([]);
  const [isOpen, setIsOpen] = useState(false);
  const toggle = () => setIsOpen(!isOpen);

  useEffect(() => {
    if (givens.length > 0 && patientEntity.names && humanNameList.length === 0 && patientEntity.id.toString() === props.match.params.id) {
      const list = [];
      patientEntity.names.forEach(element => {
        list.push({ ...element, givens: givens.filter(e => e.human?.id?.toString() === element.id?.toString()) });
      });
      setHumanNameList(list);
    }
  }, [patientEntity.names, givens]);

  const handleNameChange = e => {
    updateHumanNameFormData({
      ...humanNameFormData,
      [e.target.name]: e.target.value.trim(),
    });
  };

  const handleNameSubmit = e => {
    e.preventDefault();
    const required = [];
    if (humanNameFormData.family === '') {
      required.push('family');
    }
    if (givenList.length === 0) {
      required.push('givens');
    }
    if (required.length > 0) {
      setRequired(required);
    } else {
      setRequired([]);
      addName();
      toggle();
    }
  };

  const addName = () => {
    const newNameList = [...humanNameList];
    const humanNameEntity = { ...humanNameFormData, givens: givenList };
    newNameList.push(humanNameEntity);
    setGivenList([]);
    setHumanNameList(newNameList);
  };

  const removeName = index => {
    const newNameList = [...humanNameList];
    newNameList.splice(index, 1);
    setHumanNameList(newNameList);
  };

  const handleGivenChange = e => {
    updateGivenFormData({
      ...givenFormData,
      [e.target.name]: e.target.value.trim(),
    });
  };

  const handleGivenSubmit = e => {
    e.preventDefault();
    if (givenFormData.given === '') {
      setRequired(['given']);
    } else {
      setRequired([]);
      addGiven();
    }
  };

  const addGiven = () => {
    const newGivenList = [...givenList];
    const givenEntity = {
      ...givenFormData,
      textName: ((givenFormData.prefix ?? '') + ' ' + givenFormData.given + ' ' + (givenFormData.suffix ?? '')).trim(),
    };
    newGivenList.push(givenEntity);
    setGivenList(newGivenList);
  };

  const removeGiven = index => {
    const newGivenList = [...givenList];
    newGivenList.splice(index, 1);
    setGivenList(newGivenList);
  };

  const handleClose = () => {
    props.history.push('/patient');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getGivens({}));
    dispatch(getContacts({}));
    dispatch(getAddresses({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.birthDate = convertDateTimeToServer(values.birthDate);
    values.deceasedDate = convertDateTimeToServer(values.deceasedDate);
    if (humanNameList.length === 0) {
      setRequired(['name']);
    } else {
      const entity = {
        ...patientEntity,
        ...values,
        names: humanNameList,
      };
      if (isNew) {
        dispatch(createEntity(entity));
      } else {
        dispatch(updateEntity(entity));
      }
      setHumanNameList([]);
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          birthDate: displayDefaultDateTime(),
          deceasedDate: displayDefaultDateTime(),
        }
      : {
          ...patientEntity,
          religion: patientEntity.religion ?? 'N0',
          gender: patientEntity.gender ?? 'Male',
          birthDate: convertDateTimeFromServer(patientEntity.birthDate),
          deceasedDate: convertDateTimeFromServer(patientEntity.deceasedDate),
          maritalStatus: patientEntity.maritalStatus,
          contactsId: patientEntity?.contacts?.id,
          addressId: patientEntity?.address?.id,
        };

  return (
    <div>
      <MyModal isOpen={isOpen} toggle={toggle}>
        <Form>
          <FormGroup>
            <Label id="familyLabel" for="human-name-family">
              <Translate contentKey="hcpNphiesPortalApp.humanName.family">Family</Translate>
            </Label>
            <Input id="human-name-family" data-cy="family" type="text" name="family" onChange={handleNameChange} />
            {isRequired.indexOf('family') > -1 ? (
              <p style={{ color: 'red' }}>
                <Translate contentKey="entity.validation.required">Required</Translate>
              </p>
            ) : null}
          </FormGroup>
          <Translate contentKey="hcpNphiesPortalApp.givens.home.title">Givens</Translate>
          <ul>
            {givenList.map(e => (
              <li style={{ padding: 10 }} key={e.given}>
                {e.textName}{' '}
                <Button style={{ float: 'right' }} color="danger" onClick={() => removeGiven(givenList.indexOf(e))}>
                  Delete
                </Button>
              </li>
            ))}
          </ul>
          {isRequired.indexOf('givens') > -1 ? (
            <p style={{ color: 'red' }}>
              <Translate contentKey="entity.validation.arrayrequired">Required</Translate>
            </p>
          ) : null}
          {givenList.length === 0 ? (
            <p style={{ fontStyle: 'italic' }}>
              <Translate contentKey="entity.fieldarray">Fill</Translate>
            </p>
          ) : null}
          <FormGroup>
            <Row>
              <Col xs="12" sm="2">
                <Label id="prefixLabel" for="givens-prefix">
                  <Translate contentKey="hcpNphiesPortalApp.givens.prefix">Prefix</Translate>
                </Label>
                <Input id="givens-prefix" data-cy="prefix" type="text" name="prefix" onChange={handleGivenChange} />
              </Col>
              <Col xs="12" sm="6">
                <Label id="givenLabel" for="givens-given">
                  <Translate contentKey="hcpNphiesPortalApp.givens.given">Given</Translate>
                </Label>
                <Input id="givens-given" data-cy="given" type="text" name="given" onChange={handleGivenChange} />
                {isRequired.indexOf('given') > -1 ? (
                  <p style={{ color: 'red' }}>
                    <Translate contentKey="entity.validation.required">Required</Translate>
                  </p>
                ) : null}
              </Col>
              <Col xs="12" sm="2">
                <Label id="suffixLabel" for="givens-suffix">
                  <Translate contentKey="hcpNphiesPortalApp.givens.suffix">Suffix</Translate>
                </Label>
                <Input id="givens-suffix" data-cy="suffix" type="text" name="suffix" onChange={handleGivenChange} />
              </Col>
              <Col xs="12" sm="2">
                <Button
                  color="primary"
                  id="add-given"
                  data-cy="entityCreateSaveButton"
                  style={{ ...{ float: 'right', marginTop: 30, marginRight: 10 } }}
                  onClick={handleGivenSubmit}
                >
                  <Translate contentKey="entity.action.addgiven">Add given</Translate>
                </Button>
              </Col>
            </Row>
          </FormGroup>
          <FormGroup>
            <Button color="primary" id="add-name" data-cy="entityCreateSaveButton" onClick={handleNameSubmit}>
              <Translate contentKey="entity.action.save">Save</Translate>
            </Button>
          </FormGroup>
        </Form>
      </MyModal>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hcpNphiesPortalApp.patient.home.createOrEditLabel" data-cy="PatientCreateUpdateHeading">
            <Translate contentKey="hcpNphiesPortalApp.patient.home.createOrEditLabel">Create or edit a Patient</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="patient-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hcpNphiesPortalApp.patient.guid')}
                id="patient-guid"
                name="guid"
                data-cy="guid"
                type="text"
              />
              <Translate contentKey="hcpNphiesPortalApp.humanName.home.title">Names</Translate>
              {humanNameList.length > 0 ? (
                <ul>
                  {humanNameList.map(e => (
                    <li style={{ padding: 10 }} key={e.family}>
                      {e.family.toUpperCase()} {e.givens.map(x => x.textName).join(', ')}{' '}
                      <Button style={{ float: 'right' }} color="danger" onClick={() => removeName(humanNameList.indexOf(e))}>
                        Delete name
                      </Button>
                    </li>
                  ))}
                </ul>
              ) : null}
              {isRequired.indexOf('name') > -1 ? (
                <p style={{ color: 'red' }}>
                  <Translate contentKey="entity.validation.arrayrequired">Required</Translate>
                </p>
              ) : null}
              <FormGroup>
                <Button color="primary" onClick={toggle}>
                  <Translate contentKey="entity.action.addname">Add name</Translate>
                </Button>
              </FormGroup>
              <ValidatedField
                label={translate('hcpNphiesPortalApp.patient.forceId')}
                id="patient-forceId"
                name="forceId"
                data-cy="forceId"
                type="text"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.patient.residentNumber')}
                id="patient-residentNumber"
                name="residentNumber"
                data-cy="residentNumber"
                type="text"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.patient.passportNumber')}
                id="patient-passportNumber"
                name="passportNumber"
                data-cy="passportNumber"
                type="text"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.patient.nationalHealthId')}
                id="patient-nationalHealthId"
                name="nationalHealthId"
                data-cy="nationalHealthId"
                type="text"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.patient.iqama')}
                id="patient-iqama"
                name="iqama"
                data-cy="iqama"
                type="text"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.patient.religion')}
                id="patient-religion"
                name="religion"
                data-cy="religion"
                type="select"
              >
                <option value=""></option>
                <option value="N0">{translate('hcpNphiesPortalApp.ReligionEnum.N0')}</option>
                <option value="N1">{translate('hcpNphiesPortalApp.ReligionEnum.N1')}</option>
                <option value="N2">{translate('hcpNphiesPortalApp.ReligionEnum.N2')}</option>
                <option value="N3">{translate('hcpNphiesPortalApp.ReligionEnum.N3')}</option>
                <option value="N4">{translate('hcpNphiesPortalApp.ReligionEnum.N4')}</option>
                <option value="N5">{translate('hcpNphiesPortalApp.ReligionEnum.N5')}</option>
                <option value="N7">{translate('hcpNphiesPortalApp.ReligionEnum.N7')}</option>
                <option value="N8">{translate('hcpNphiesPortalApp.ReligionEnum.N8')}</option>
                <option value="N9">{translate('hcpNphiesPortalApp.ReligionEnum.N9')}</option>
                <option value="N98">{translate('hcpNphiesPortalApp.ReligionEnum.N98')}</option>
                <option value="N99">{translate('hcpNphiesPortalApp.ReligionEnum.N99')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('hcpNphiesPortalApp.patient.gender')}
                id="patient-gender"
                name="gender"
                data-cy="gender"
                type="select"
              >
                <option value="Male">{translate('hcpNphiesPortalApp.AdministrativeGenderEnum.Male')}</option>
                <option value="Female">{translate('hcpNphiesPortalApp.AdministrativeGenderEnum.Female')}</option>
                <option value="Unknown">{translate('hcpNphiesPortalApp.AdministrativeGenderEnum.Unknown')}</option>
                <option value="U">{translate('hcpNphiesPortalApp.AdministrativeGenderEnum.U')}</option>
                <option value="N">{translate('hcpNphiesPortalApp.AdministrativeGenderEnum.N')}</option>
                <option value="A">{translate('hcpNphiesPortalApp.AdministrativeGenderEnum.A')}</option>
                <option value="B">{translate('hcpNphiesPortalApp.AdministrativeGenderEnum.B')}</option>
                <option value="C">{translate('hcpNphiesPortalApp.AdministrativeGenderEnum.C')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('hcpNphiesPortalApp.patient.birthDate')}
                id="patient-birthDate"
                name="birthDate"
                data-cy="birthDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.patient.deceasedDate')}
                id="patient-deceasedDate"
                name="deceasedDate"
                data-cy="deceasedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.patient.maritalStatus')}
                id="patient-maritalStatus"
                name="maritalStatus"
                data-cy="maritalStatus"
                type="select"
              >
                <option value=""></option>
                <option value="L">{translate('hcpNphiesPortalApp.MaritalStatusEnum.L')}</option>
                <option value="D">{translate('hcpNphiesPortalApp.MaritalStatusEnum.D')}</option>
                <option value="M">{translate('hcpNphiesPortalApp.MaritalStatusEnum.M')}</option>
                <option value="U">{translate('hcpNphiesPortalApp.MaritalStatusEnum.U')}</option>
                <option value="W">{translate('hcpNphiesPortalApp.MaritalStatusEnum.W')}</option>
                <option value="UNK">{translate('hcpNphiesPortalApp.MaritalStatusEnum.UNK')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('hcpNphiesPortalApp.contact.phone')}
                id="contact-phone"
                data-cy="contacts.phone"
                type="text"
                name="contacts.phone"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.contact.email')}
                id="contact-email"
                data-cy="contacts.email"
                type="text"
                name="contacts.email"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.contact.mobile')}
                id="contact-mobile"
                data-cy="contacts.mobile"
                type="text"
                name="contacts.mobile"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.contact.url')}
                id="contact-url"
                data-cy="contacts.url"
                type="text"
                name="contacts.url"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.address.addressLine')}
                id="address-addressLine"
                data-cy="address.addressLine"
                type="text"
                name="address.addressLine"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.address.city')}
                id="address-city"
                data-cy="address.city"
                type="text"
                name="address.city"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.address.district')}
                id="address-district"
                data-cy="address.district"
                type="text"
                name="address.district"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.address.state')}
                id="address-state"
                data-cy="address.state"
                type="text"
                name="address.state"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.address.postalCode')}
                id="address-postalCode"
                data-cy="address.postalCode"
                type="text"
                name="address.postalCode"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.address.country')}
                id="address-country"
                data-cy="address.country"
                type="text"
                name="address.country"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/patient" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PatientUpdate;

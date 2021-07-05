import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label, Modal, ModalBody, FormGroup, Form, Input } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IPatient } from 'app/shared/model/patient.model';
import { getEntities as getPatients } from 'app/entities/patient/patient.reducer';
import { IOrganization } from 'app/shared/model/organization.model';
import { getEntities as getOrganizations } from 'app/entities/organization/organization.reducer';
import { ICoverageEligibilityRequest } from 'app/shared/model/coverage-eligibility-request.model';
import { getEntities as getCoverageEligibilityRequests } from 'app/entities/coverage-eligibility-request/coverage-eligibility-request.reducer';
import { getEntity, updateEntity, createEntity, reset } from './coverage.reducer';
import { ICoverage } from 'app/shared/model/coverage.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { getEntities as getExemptions } from 'app/entities/exemption-component/exemption-component.reducer';
import {
  getClassTerm,
  getCostTerm,
  getExempTerm,
  initialClassComponentFormData,
  initialCostToBeneficiaryComponentFormData,
  initialExemptionFormData,
} from 'app/shared/util/formdata-utils';
import Autocomplete from 'react-autocomplete';
import { getOrgTerm, getPatTerm, matchOrgToTerm, matchPatToTerm } from 'app/shared/util/autocomplete-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

const MyModal = ({ children, isOpen, toggle }) => (
  <div>
    <Modal isOpen={isOpen} toggle={toggle} style={{ maxWidth: '1600px', width: '70%', margin: '10px auto' }}>
      <ModalBody>{children}</ModalBody>
    </Modal>
  </div>
);

export const CoverageUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const exemptions = useAppSelector(state => state.exemptionComponent.entities);
  const patients = useAppSelector(state => state.patient.entities);
  const organizations = useAppSelector(state => state.organization.entities);
  const coverageEligibilityRequests = useAppSelector(state => state.coverageEligibilityRequest.entities);
  const coverageEntity = useAppSelector(state => state.coverage.entity);
  const loading = useAppSelector(state => state.coverage.loading);
  const updating = useAppSelector(state => state.coverage.updating);
  const updateSuccess = useAppSelector(state => state.coverage.updateSuccess);

  const [isRequired, setRequired] = useState([]);
  const [exemptionFormData, updateExemptionFormData] = React.useState(initialExemptionFormData);
  const [exemptionList, setExemptionList] = useState([]);
  const [costToBeneficiaryComponentFormData, updateCostToBeneficiaryComponentFormData] = React.useState(
    initialCostToBeneficiaryComponentFormData
  );
  const [costToBeneficiaryComponentList, setCostToBeneficiaryComponentList] = useState([]);
  const [isOpenCost, setIsOpenCost] = useState(false);
  const toggleCost = () => setIsOpenCost(!isOpenCost);
  const [classComponentFormData, updateClassComponentFormData] = React.useState(initialClassComponentFormData);
  const [classComponentList, setClassComponentList] = useState([]);
  const [isOpenClass, setIsOpenClass] = useState(false);
  const toggleClass = () => setIsOpenClass(!isOpenClass);

  const [subscriberPatientId, setSubscriberPatientId] = useState('');
  const [subscriberPatient, setSubscriberPatient] = useState({});

  useEffect(() => {
    if (coverageEntity.subscriberPatient && subscriberPatientId === '') {
      setSubscriberPatient(coverageEntity.subscriberPatient);
      setSubscriberPatientId(getPatTerm(coverageEntity.subscriberPatient));
    }
  }, [coverageEntity.subscriberPatient]);

  const changeSubscriberPatient = e => {
    setSubscriberPatientId(e.target.value);
  };

  const selectSubscriberPatient = (val, item) => {
    setSubscriberPatient(item);
    setSubscriberPatientId(val);
  };

  const [beneficiaryPatientId, setBeneficiaryPatientId] = useState('');
  const [beneficiary, setBeneficiaryPatient] = useState({});

  useEffect(() => {
    if (coverageEntity.beneficiary && beneficiaryPatientId === '') {
      setBeneficiaryPatient(coverageEntity.beneficiary);
      setBeneficiaryPatientId(getPatTerm(coverageEntity.beneficiary));
    }
  }, [coverageEntity.beneficiary]);

  const changeBeneficiaryPatient = e => {
    setBeneficiaryPatientId(e.target.value);
  };

  const selectBeneficiaryPatient = (val, item) => {
    setBeneficiaryPatient(item);
    setBeneficiaryPatientId(val);
  };

  const [insId, setInsId] = useState('');
  const [payor, setIns] = useState({});

  useEffect(() => {
    if (coverageEntity.payor && insId === '') {
      setIns(coverageEntity.payor);
      setInsId(getOrgTerm(coverageEntity.payor));
    }
  }, [coverageEntity.payor]);

  const changeIns = e => {
    setInsId(e.target.value);
  };

  const selectIns = (val, item) => {
    setIns(item);
    setInsId(val);
  };

  useEffect(() => {
    if (coverageEntity.classComponents && classComponentList.length === 0 && coverageEntity.id?.toString() === props.match.params.id) {
      const list = [];
      coverageEntity.classComponents.forEach(element => {
        if (element != null) {
          list.push({ ...element });
        }
      });
      setClassComponentList(list);
    }
  }, [coverageEntity.classComponents]);

  const handleClassChange = e => {
    updateClassComponentFormData({
      ...classComponentFormData,
      [e.target.name]: e.target.value.trim(),
    });
  };

  const handleClassSubmit = e => {
    e.preventDefault();
    const required = [];
    if (classComponentFormData.type === null || classComponentFormData.value === '') {
      required.push('class');
    } else {
      setRequired([]);
      addClass();
      toggleClass();
    }
  };

  const addClass = () => {
    const newClassList = [...classComponentList];
    const classComponentEntity = { ...classComponentFormData };
    newClassList.push(classComponentEntity);
    setClassComponentList(newClassList);
  };

  const removeClass = index => {
    const newClassList = [...classComponentList];
    newClassList.splice(index, 1);
    setClassComponentList(newClassList);
  };

  useEffect(() => {
    if (
      exemptions.length > 0 &&
      coverageEntity.costToBeneficiaryComponents &&
      costToBeneficiaryComponentList.length === 0 &&
      coverageEntity.id?.toString() === props.match.params.id
    ) {
      const list = [];
      coverageEntity.costToBeneficiaryComponents.forEach(element => {
        if (element != null) {
          list.push({
            ...element,
            exceptions: [...exemptions].filter(
              e => e.costToBeneficiary?.id !== null && e.costToBeneficiary?.id?.toString() === element.id?.toString()
            ),
          });
        }
      });
      setCostToBeneficiaryComponentList(list);
    }
  }, [coverageEntity.costToBeneficiaryComponents, exemptions]);

  const handleCostChange = e => {
    updateCostToBeneficiaryComponentFormData({
      ...costToBeneficiaryComponentFormData,
      [e.target.name]: e.target.name === 'isMoney' ? e.target.checked : e.target.value.trim(),
    });
  };

  const handleCostSubmit = e => {
    e.preventDefault();
    const required = [];
    if (costToBeneficiaryComponentFormData.type === null || costToBeneficiaryComponentFormData.value <= 0) {
      required.push('cost');
    } else {
      setRequired([]);
      addCost();
      toggleCost();
    }
  };

  const addCost = () => {
    const newCostList = [...costToBeneficiaryComponentList];
    const costToBeneficiaryComponentEntity = { ...costToBeneficiaryComponentFormData, exceptions: exemptionList };
    newCostList.push(costToBeneficiaryComponentEntity);
    setExemptionList([]);
    setCostToBeneficiaryComponentList(newCostList);
  };

  const removeCost = index => {
    const newCostList = [...costToBeneficiaryComponentList];
    newCostList.splice(index, 1);
    setCostToBeneficiaryComponentList(newCostList);
  };

  const handleExemptionChange = e => {
    updateExemptionFormData({
      ...exemptionFormData,
      [e.target.name]: e.target.value.trim(),
    });
  };

  const handleExemptionSubmit = e => {
    e.preventDefault();
    addExemption();
  };

  const addExemption = () => {
    const newExemptionList = [...exemptionList];
    const exemptionEntity = { ...exemptionFormData };
    newExemptionList.push(exemptionEntity);
    setExemptionList(newExemptionList);
  };

  const removeExemption = index => {
    const newExemptionList = [...exemptionList];
    newExemptionList.splice(index, 1);
    setExemptionList(newExemptionList);
  };

  const handleClose = () => {
    props.history.push('/coverage');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getExemptions({}));
    dispatch(getPatients({}));
    dispatch(getOrganizations({}));
    dispatch(getCoverageEligibilityRequests({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    costToBeneficiaryComponentList.forEach(c => {
      c.exceptions
        ? (c.exceptions = [...c.exceptions].map(element => {
            return {
              ...element,
              start: convertDateTimeToServer(element.start),
              end: convertDateTimeToServer(element.end),
            };
          }))
        : null;
    });
    let entity = {
      ...coverageEntity,
      ...values,
      costToBeneficiaryComponents: costToBeneficiaryComponentList,
      classComponents: classComponentList,
    };

    if (getPatTerm(subscriberPatient)) {
      entity = { ...entity, subscriberPatient };
    }

    if (getPatTerm(beneficiary)) {
      entity = { ...entity, beneficiary };
    }

    if (getOrgTerm(payor)) {
      entity = { ...entity, payor };
    }

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...coverageEntity,
          coverageType: 'EHCPOL',
          relationShip: 'Child',
          subscriberPatientId: coverageEntity?.subscriberPatient?.id,
          beneficiaryId: coverageEntity?.beneficiary?.id,
          payorId: coverageEntity?.payor?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hcpNphiesPortalApp.coverage.home.createOrEditLabel" data-cy="CoverageCreateUpdateHeading">
            <Translate contentKey="hcpNphiesPortalApp.coverage.home.createOrEditLabel">Create or edit a Coverage</Translate>
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
                  id="coverage-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hcpNphiesPortalApp.coverage.guid')}
                id="coverage-guid"
                name="guid"
                data-cy="guid"
                type="text"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.coverage.forceId')}
                id="coverage-forceId"
                name="forceId"
                data-cy="forceId"
                type="text"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.coverage.coverageType')}
                id="coverage-coverageType"
                name="coverageType"
                data-cy="coverageType"
                type="select"
              >
                <option value="EHCPOL">{translate('hcpNphiesPortalApp.CoverageTypeEnum.EHCPOL')}</option>
                <option value="PUBLICPOL">{translate('hcpNphiesPortalApp.CoverageTypeEnum.PUBLICPOL')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('hcpNphiesPortalApp.coverage.subscriberId')}
                id="coverage-subscriberId"
                name="subscriberId"
                data-cy="subscriberId"
                type="text"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.coverage.dependent')}
                id="coverage-dependent"
                name="dependent"
                data-cy="dependent"
                type="text"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.coverage.relationShip')}
                id="coverage-relationShip"
                name="relationShip"
                data-cy="relationShip"
                type="select"
              >
                <option value="Child">{translate('hcpNphiesPortalApp.RelationShipEnum.Child')}</option>
                <option value="Parent">{translate('hcpNphiesPortalApp.RelationShipEnum.Parent')}</option>
                <option value="Spouse">{translate('hcpNphiesPortalApp.RelationShipEnum.Spouse')}</option>
                <option value="Common">{translate('hcpNphiesPortalApp.RelationShipEnum.Common')}</option>
                <option value="Other">{translate('hcpNphiesPortalApp.RelationShipEnum.Other')}</option>
                <option value="Self">{translate('hcpNphiesPortalApp.RelationShipEnum.Self')}</option>
                <option value="Injured">{translate('hcpNphiesPortalApp.RelationShipEnum.Injured')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('hcpNphiesPortalApp.coverage.network')}
                id="coverage-network"
                name="network"
                data-cy="network"
                type="text"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.coverage.subrogation')}
                id="coverage-subrogation"
                name="subrogation"
                data-cy="subrogation"
                check
                type="checkbox"
              />
              <FormGroup>
                <Label for="coverage-subscriberPatient">
                  <Translate contentKey="hcpNphiesPortalApp.coverage.subscriberPatient">Subscriber Patient</Translate>
                </Label>
                <FormGroup>
                  <Autocomplete
                    shouldItemRender={matchPatToTerm}
                    getItemValue={item => getPatTerm(item)}
                    items={[...patients]}
                    renderItem={(item, isHighlighted) => (
                      <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getPatTerm(item)}</div>
                    )}
                    value={subscriberPatientId}
                    onChange={changeSubscriberPatient}
                    onSelect={selectSubscriberPatient}
                  />
                </FormGroup>
              </FormGroup>
              <FormGroup>
                <Label for="coverage-beneficiary">
                  <Translate contentKey="hcpNphiesPortalApp.coverage.beneficiary">Beneficiary</Translate>
                </Label>
                <FormGroup>
                  <Autocomplete
                    shouldItemRender={matchPatToTerm}
                    getItemValue={item => getPatTerm(item)}
                    items={[...patients]}
                    renderItem={(item, isHighlighted) => (
                      <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getPatTerm(item)}</div>
                    )}
                    value={beneficiaryPatientId}
                    onChange={changeBeneficiaryPatient}
                    onSelect={selectBeneficiaryPatient}
                  />
                </FormGroup>
              </FormGroup>
              <FormGroup>
                <Label for="coverage-payor">
                  <Translate contentKey="hcpNphiesPortalApp.coverage.payor">Payor</Translate>
                </Label>
                <FormGroup>
                  <Autocomplete
                    shouldItemRender={matchOrgToTerm}
                    getItemValue={item => getOrgTerm(item)}
                    items={[...organizations]}
                    renderItem={(item, isHighlighted) => (
                      <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getOrgTerm(item)}</div>
                    )}
                    value={insId}
                    onChange={changeIns}
                    onSelect={selectIns}
                  />
                </FormGroup>
              </FormGroup>
              <Translate contentKey="hcpNphiesPortalApp.classComponent.home.title">Class</Translate>
              {classComponentList.length > 0 ? (
                <ul>
                  {classComponentList.map(e => (
                    <li style={{ padding: 10 }} key={e.name}>
                      {getClassTerm(e)}{' '}
                      <Button style={{ float: 'right' }} color="danger" onClick={() => removeClass(classComponentList.indexOf(e))}>
                        <Translate contentKey="entity.action.deleteclass">Delete class</Translate>
                      </Button>
                    </li>
                  ))}
                </ul>
              ) : null}
              <FormGroup>
                <Button color="primary" onClick={toggleClass}>
                  <Translate contentKey="entity.action.addclass">Add class</Translate>
                </Button>
              </FormGroup>
              <MyModal isOpen={isOpenClass} toggle={toggleClass}>
                <Form>
                  <FormGroup>
                    <Label id="typeLabel" for="class-component-type">
                      <Translate contentKey="hcpNphiesPortalApp.classComponent.type">Type</Translate>
                    </Label>
                    <Input
                      id="class-component-type"
                      data-cy="type"
                      type="select"
                      className="form-control"
                      name="type"
                      value={classComponentFormData.type}
                      onChange={handleClassChange}
                    >
                      <option value="null"></option>
                      <option value="Group">{translate('hcpNphiesPortalApp.ClassTypeEnum.Group')}</option>
                      <option value="Subgroup">{translate('hcpNphiesPortalApp.ClassTypeEnum.Subgroup')}</option>
                      <option value="Plan">{translate('hcpNphiesPortalApp.ClassTypeEnum.Plan')}</option>
                      <option value="Subplan">{translate('hcpNphiesPortalApp.ClassTypeEnum.Subplan')}</option>
                      <option value="Class">{translate('hcpNphiesPortalApp.ClassTypeEnum.Class')}</option>
                      <option value="Subclass">{translate('hcpNphiesPortalApp.ClassTypeEnum.Subclass')}</option>
                      <option value="Sequence">{translate('hcpNphiesPortalApp.ClassTypeEnum.Sequence')}</option>
                      <option value="Rxbin">{translate('hcpNphiesPortalApp.ClassTypeEnum.Rxbin')}</option>
                      <option value="Rxpcn">{translate('hcpNphiesPortalApp.ClassTypeEnum.Rxpcn')}</option>
                      <option value="Rxid">{translate('hcpNphiesPortalApp.ClassTypeEnum.Rxid')}</option>
                      <option value="Rxgroup">{translate('hcpNphiesPortalApp.ClassTypeEnum.Rxgroup')}</option>
                    </Input>
                  </FormGroup>
                  <FormGroup>
                    <Label id="valueLabel" for="class-component-value">
                      <Translate contentKey="hcpNphiesPortalApp.classComponent.value">Value</Translate>
                    </Label>
                    <Input
                      id="class-component-value"
                      data-cy="value"
                      type="text"
                      name="value"
                      validate={{
                        required: { value: true, errorMessage: translate('entity.validation.required') },
                      }}
                      onChange={handleClassChange}
                    />
                  </FormGroup>
                  <FormGroup>
                    <Label id="nameLabel" for="class-component-name">
                      <Translate contentKey="hcpNphiesPortalApp.classComponent.name">Name</Translate>
                    </Label>
                    <Input id="class-component-name" data-cy="name" type="text" name="name" onChange={handleClassChange} />
                  </FormGroup>
                  <FormGroup>
                    <Button color="info" onClick={toggleClass}>
                      <Translate contentKey="entity.action.back">Back</Translate>
                    </Button>
                    &nbsp;
                    <Button color="primary" id="add-name" data-cy="entityCreateSaveButton" onClick={handleClassSubmit}>
                      <Translate contentKey="entity.action.save">Save</Translate>
                    </Button>
                  </FormGroup>
                </Form>
              </MyModal>
              <Translate contentKey="hcpNphiesPortalApp.costToBeneficiaryComponent.home.title">Costs</Translate>
              {costToBeneficiaryComponentList.length > 0 ? (
                <ul>
                  {costToBeneficiaryComponentList.map(e => (
                    <li style={{ padding: 10 }} key={e.family}>
                      {getCostTerm(e)}{' '}
                      <Button
                        style={{ float: 'right' }}
                        color="danger"
                        onClick={() => removeCost(costToBeneficiaryComponentList.indexOf(e))}
                      >
                        Delete name
                      </Button>
                    </li>
                  ))}
                </ul>
              ) : null}
              <FormGroup>
                <Button color="primary" onClick={toggleCost}>
                  <Translate contentKey="entity.action.addcost">Add cost</Translate>
                </Button>
              </FormGroup>
              <MyModal isOpen={isOpenCost} toggle={toggleCost}>
                <Form>
                  <FormGroup>
                    <Label id="typeLabel" for="cost-to-beneficiary-component-type">
                      <Translate contentKey="hcpNphiesPortalApp.costToBeneficiaryComponent.type">Type</Translate>
                    </Label>
                    <Input
                      id="cost-to-beneficiary-component-type"
                      data-cy="type"
                      type="select"
                      className="form-control"
                      name="type"
                      value={costToBeneficiaryComponentFormData.type}
                      onChange={handleCostChange}
                    >
                      <option value="null"></option>
                      <option value="Gpvisit">{translate('hcpNphiesPortalApp.CostToBeneficiaryTypeEnum.Gpvisit')}</option>
                      <option value="Spvisit">{translate('hcpNphiesPortalApp.CostToBeneficiaryTypeEnum.Spvisit')}</option>
                      <option value="Copaypct">{translate('hcpNphiesPortalApp.CostToBeneficiaryTypeEnum.Copaypct')}</option>
                      <option value="Copay">{translate('hcpNphiesPortalApp.CostToBeneficiaryTypeEnum.Copay')}</option>
                      <option value="Deductible">{translate('hcpNphiesPortalApp.CostToBeneficiaryTypeEnum.Deductible')}</option>
                      <option value="Maxoutofpocket">{translate('hcpNphiesPortalApp.CostToBeneficiaryTypeEnum.Maxoutofpocket')}</option>
                    </Input>
                  </FormGroup>
                  <FormGroup check>
                    <Label id="isMoneyLabel">
                      <Input
                        id="cost-to-beneficiary-component-isMoney"
                        data-cy="isMoney"
                        type="checkbox"
                        className="form-check-input"
                        name="isMoney"
                        onChange={handleCostChange}
                      />
                      <Translate contentKey="hcpNphiesPortalApp.costToBeneficiaryComponent.isMoney">Is Money</Translate>
                    </Label>
                  </FormGroup>
                  <FormGroup>
                    <Label id="valueLabel" for="cost-to-beneficiary-component-value">
                      <Translate contentKey="hcpNphiesPortalApp.costToBeneficiaryComponent.value">Value</Translate>
                    </Label>
                    <Input id="cost-to-beneficiary-component-value" data-cy="value" type="text" name="value" onChange={handleCostChange} />
                  </FormGroup>
                  <Translate contentKey="hcpNphiesPortalApp.exemptionComponent.home.title">Exceptions</Translate>
                  <ul>
                    {exemptionList.map(e => (
                      <li style={{ padding: 10 }} key={e.exemption}>
                        {getExempTerm(e)}{' '}
                        <Button style={{ float: 'right' }} color="danger" onClick={() => removeExemption(exemptionList.indexOf(e))}>
                          Delete
                        </Button>
                      </li>
                    ))}
                  </ul>
                  {exemptionList.length === 0 ? (
                    <p style={{ fontStyle: 'italic' }}>
                      <Translate contentKey="entity.fieldarray">Fill</Translate>
                    </p>
                  ) : null}
                  <FormGroup>
                    <Row>
                      <Col xs="12" sm="3">
                        <FormGroup>
                          <Label id="typeLabel" for="exemption-component-type">
                            <Translate contentKey="hcpNphiesPortalApp.exemptionComponent.type">Type</Translate>
                          </Label>
                          <Input
                            id="exemption-component-type"
                            data-cy="type"
                            type="select"
                            className="form-control"
                            name="type"
                            value={exemptionFormData?.type}
                            onChange={handleExemptionChange}
                          >
                            <option value="null"></option>
                            <option value="Retired">{translate('hcpNphiesPortalApp.ExemptionTypeEnum.Retired')}</option>
                            <option value="Foster">{translate('hcpNphiesPortalApp.ExemptionTypeEnum.Foster')}</option>
                          </Input>
                        </FormGroup>
                      </Col>
                      <Col xs="12" sm="3">
                        <FormGroup>
                          <Label id="startLabel" for="exemption-component-start">
                            <Translate contentKey="hcpNphiesPortalApp.exemptionComponent.start">Start</Translate>
                          </Label>
                          <Input
                            id="exemption-component-start"
                            data-cy="start"
                            type="datetime-local"
                            className="form-control"
                            name="start"
                            placeholder={'YYYY-MM-DD HH:mm'}
                            value={exemptionFormData?.start}
                            onChange={handleExemptionChange}
                          />
                        </FormGroup>
                      </Col>
                      <Col xs="12" sm="3">
                        <FormGroup>
                          <Label id="endLabel" for="exemption-component-end">
                            <Translate contentKey="hcpNphiesPortalApp.exemptionComponent.end">End</Translate>
                          </Label>
                          <Input
                            id="exemption-component-end"
                            data-cy="end"
                            type="datetime-local"
                            className="form-control"
                            name="end"
                            placeholder={'YYYY-MM-DD HH:mm'}
                            value={exemptionFormData?.end}
                            onChange={handleExemptionChange}
                          />
                        </FormGroup>
                      </Col>
                      <Col xs="12" sm="2">
                        <Button
                          color="primary"
                          id="add-exemption"
                          data-cy="entityCreateSaveButton"
                          style={{ ...{ float: 'right', marginTop: 30, marginRight: 10 } }}
                          onClick={handleExemptionSubmit}
                        >
                          <Translate contentKey="entity.action.addexemption">Add exemption</Translate>
                        </Button>
                      </Col>
                    </Row>
                  </FormGroup>
                  <FormGroup>
                    <Button color="info" onClick={toggleCost}>
                      <Translate contentKey="entity.action.back">Back</Translate>
                    </Button>
                    &nbsp;
                    <Button color="primary" id="add-cost" data-cy="entityCreateSaveButton" onClick={handleCostSubmit}>
                      <Translate contentKey="entity.action.save">Save</Translate>
                    </Button>
                  </FormGroup>
                </Form>
              </MyModal>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/coverage" replace color="info">
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

export default CoverageUpdate;

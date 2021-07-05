import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText, Label, FormGroup, Input, Form } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IPatient } from 'app/shared/model/patient.model';
import { getEntities as getPatients } from 'app/entities/patient/patient.reducer';
import { IOrganization } from 'app/shared/model/organization.model';
import { getEntities as getOrganizations } from 'app/entities/organization/organization.reducer';
import { ILocation } from 'app/shared/model/location.model';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { ICoverage } from 'app/shared/model/coverage.model';
import { getEntities as getCoverages } from 'app/entities/coverage/coverage.reducer';
import { getEntity, updateEntity, createEntity, reset } from './coverage-eligibility-request.reducer';
import { ICoverageEligibilityRequest } from 'app/shared/model/coverage-eligibility-request.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import Autocomplete from 'react-autocomplete';
import {
  getCovTerm,
  getLocTerm,
  getOrgTerm,
  getPatTerm,
  matchCovToTerm,
  matchLocToTerm,
  matchOrgToTerm,
  matchPatToTerm,
} from 'app/shared/util/autocomplete-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CoverageEligibilityRequestUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const patients = useAppSelector(state => state.patient.entities);
  const organizations = useAppSelector(state => state.organization.entities);
  const locations = useAppSelector(state => state.location.entities);
  const coverages = useAppSelector(state => state.coverage.entities);
  const coverageEligibilityRequestEntity = useAppSelector(state => state.coverageEligibilityRequest.entity);
  const loading = useAppSelector(state => state.coverageEligibilityRequest.loading);
  const updating = useAppSelector(state => state.coverageEligibilityRequest.updating);
  const updateSuccess = useAppSelector(state => state.coverageEligibilityRequest.updateSuccess);

  const [purpose, setPurpose] = useState([]);

  useEffect(() => {
    if (coverageEligibilityRequestEntity.purposes && purpose.length === 0) {
      const list = [];
      coverageEligibilityRequestEntity.purposes.forEach(element => {
        list.push(element.erp);
      });
      setPurpose(list);
    }
  }, [coverageEligibilityRequestEntity.purposes]);

  const changePurpose = e => {
    const list = [...purpose];
    if (purpose.indexOf(e.target.value) > -1 && coverageEligibilityRequestEntity.id?.toString() === props.match.params.id) {
      list.splice(purpose.indexOf(e.target.value), 1);
    } else {
      list.push(e.target.value);
    }
    setPurpose(list);
  };

  const [patientId, setPatientId] = useState('');
  const [patient, setPatient] = useState({});

  useEffect(() => {
    if (coverageEligibilityRequestEntity.patient && patientId === '') {
      setPatient(coverageEligibilityRequestEntity.patient);
      setPatientId(getPatTerm(coverageEligibilityRequestEntity.patient));
    }
  }, [coverageEligibilityRequestEntity.patient]);

  const changePatient = e => {
    setPatientId(e.target.value);
  };

  const selectPatient = (val, item) => {
    setPatient(item);
    setPatientId(val);
  };

  const [insId, setInsId] = useState('');
  const [insurer, setIns] = useState({});

  useEffect(() => {
    if (coverageEligibilityRequestEntity.insurer && insId === '') {
      setIns(coverageEligibilityRequestEntity.insurer);
      setInsId(getOrgTerm(coverageEligibilityRequestEntity.insurer));
    }
  }, [coverageEligibilityRequestEntity.insurer]);

  const changeIns = e => {
    setInsId(e.target.value);
  };

  const selectIns = (val, item) => {
    setIns(item);
    setInsId(val);
  };

  const [locId, setLocId] = useState('');
  const [facility, setLoc] = useState({});

  useEffect(() => {
    if (coverageEligibilityRequestEntity.facility && locId === '') {
      setLoc(coverageEligibilityRequestEntity.facility);
      setLocId(getLocTerm(coverageEligibilityRequestEntity.facility));
    }
  }, [coverageEligibilityRequestEntity.facility]);

  const changeLoc = e => {
    setLocId(e.target.value);
  };

  const selectLoc = (val, item) => {
    setLoc(item);
    setLocId(val);
  };

  const handleClose = () => {
    props.history.push('/coverage-eligibility-request');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getPatients({}));
    dispatch(getOrganizations({}));
    dispatch(getLocations({}));
    dispatch(getCoverages({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.servicedDate = convertDateTimeToServer(values.servicedDate);
    values.servicedDateEnd = convertDateTimeToServer(values.servicedDateEnd);

    let entity = {
      ...coverageEligibilityRequestEntity,
      ...values,
      purposes: [...purpose.map(x => ({ erp: x }))],
      coverages: mapIdList(values.coverages),
    };

    if (getPatTerm(patient)) {
      entity = { ...entity, patient };
    }

    if (getOrgTerm(insurer)) {
      entity = { ...entity, insurer };
    }

    if (getLocTerm(facility)) {
      entity = { ...entity, facility };
    }

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
    setPurpose([]);
  };

  const defaultValues = () =>
    isNew
      ? {
          servicedDate: displayDefaultDateTime(),
          servicedDateEnd: displayDefaultDateTime(),
        }
      : {
          ...coverageEligibilityRequestEntity,
          priority: 'Stat',
          servicedDate: convertDateTimeFromServer(coverageEligibilityRequestEntity.servicedDate),
          servicedDateEnd: convertDateTimeFromServer(coverageEligibilityRequestEntity.servicedDateEnd),
          patientId: coverageEligibilityRequestEntity?.patient?.id,
          providerId: coverageEligibilityRequestEntity?.provider?.id,
          insurerId: coverageEligibilityRequestEntity?.insurer?.id,
          facilityId: coverageEligibilityRequestEntity?.facility?.id,
          coverages: coverageEligibilityRequestEntity?.coverages?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2
            id="hcpNphiesPortalApp.coverageEligibilityRequest.home.createOrEditLabel"
            data-cy="CoverageEligibilityRequestCreateUpdateHeading"
          >
            <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.home.createOrEditLabel">
              Create or edit a CoverageEligibilityRequest
            </Translate>
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
                  id="coverage-eligibility-request-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hcpNphiesPortalApp.coverageEligibilityRequest.guid')}
                id="coverage-eligibility-request-guid"
                name="guid"
                data-cy="guid"
                type="text"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.coverageEligibilityRequest.parsed')}
                id="coverage-eligibility-request-parsed"
                name="parsed"
                data-cy="parsed"
                type="text"
              />
              <Form>
                <FormGroup>
                  <Label check>
                    <Input
                      type="checkbox"
                      checked={purpose.indexOf('Benefits') > -1}
                      onChange={changePurpose}
                      label={translate('hcpNphiesPortalApp.EligibilityPurposeEnum.Benefits')}
                      value="Benefits"
                    />{' '}
                    Benefits{' '}
                  </Label>
                </FormGroup>
                <FormGroup>
                  <Label check>
                    <Input
                      type="checkbox"
                      checked={purpose.indexOf('Discovery') > -1}
                      onChange={changePurpose}
                      label={translate('hcpNphiesPortalApp.EligibilityPurposeEnum.Discovery')}
                      value="Discovery"
                    />{' '}
                    Discovery{' '}
                  </Label>
                </FormGroup>
                <FormGroup>
                  <Label check>
                    <Input
                      type="checkbox"
                      checked={purpose.indexOf('Validation') > -1}
                      onChange={changePurpose}
                      label={translate('hcpNphiesPortalApp.EligibilityPurposeEnum.Validation')}
                      value="Validation"
                    />{' '}
                    Validation{' '}
                  </Label>
                </FormGroup>
              </Form>
              <ValidatedField
                label={translate('hcpNphiesPortalApp.coverageEligibilityRequest.priority')}
                id="coverage-eligibility-request-priority"
                name="priority"
                data-cy="priority"
                type="select"
              >
                <option value="Stat">{translate('hcpNphiesPortalApp.PriorityEnum.Stat')}</option>
                <option value="Normal">{translate('hcpNphiesPortalApp.PriorityEnum.Normal')}</option>
                <option value="Deferred">{translate('hcpNphiesPortalApp.PriorityEnum.Deferred')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('hcpNphiesPortalApp.coverageEligibilityRequest.identifier')}
                id="coverage-eligibility-request-identifier"
                name="identifier"
                data-cy="identifier"
                type="text"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.coverageEligibilityRequest.servicedDate')}
                id="coverage-eligibility-request-servicedDate"
                name="servicedDate"
                data-cy="servicedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.coverageEligibilityRequest.servicedDateEnd')}
                id="coverage-eligibility-request-servicedDateEnd"
                name="servicedDateEnd"
                data-cy="servicedDateEnd"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <FormGroup>
                <Label for="coverage-eligibility-request-patient">
                  <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.patient">Patient</Translate>
                </Label>
                <FormGroup>
                  <Autocomplete
                    shouldItemRender={matchPatToTerm}
                    getItemValue={item => getPatTerm(item)}
                    items={[...patients]}
                    renderItem={(item, isHighlighted) => (
                      <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getPatTerm(item)}</div>
                    )}
                    value={patientId}
                    onChange={changePatient}
                    onSelect={selectPatient}
                  />
                </FormGroup>
              </FormGroup>
              {/* <FormGroup>
                <Label for="coverage-eligibility-request-provider">
                  <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.provider">Provider</Translate>
                </Label>
                <FormGroup>
                  <Autocomplete
                    shouldItemRender={matchOrgToTerm}
                    getItemValue={(item) => getOrgTerm(item)}
                    items={[...organizations]}
                    renderItem={(item, isHighlighted) =>
                      <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>
                        {getOrgTerm(item)}
                      </div>
                    }
                    value={provId}
                    onChange={changeProv}
                    onSelect={selectProv}
                  />
                </FormGroup>
              </FormGroup> */}
              <FormGroup>
                <Label for="coverage-eligibility-request-insurer">
                  <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.insurer">Insurer</Translate>
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
              <FormGroup>
                <Label for="coverage-eligibility-request-facility">
                  <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.facility">Facility</Translate>
                </Label>
                <FormGroup>
                  <Autocomplete
                    shouldItemRender={matchLocToTerm}
                    getItemValue={item => getLocTerm(item)}
                    items={[...locations]}
                    renderItem={(item, isHighlighted) => (
                      <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getLocTerm(item)}</div>
                    )}
                    value={locId}
                    onChange={changeLoc}
                    onSelect={selectLoc}
                  />
                </FormGroup>
              </FormGroup>
              <ValidatedField
                label={translate('hcpNphiesPortalApp.coverageEligibilityRequest.coverages')}
                id="coverage-eligibility-request-coverages"
                data-cy="coverages"
                type="select"
                multiple
                name="coverages"
              >
                <option value="" key="0" />
                {coverages
                  ? coverages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {getCovTerm(otherEntity)}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button
                tag={Link}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                to="/coverage-eligibility-request"
                replace
                color="info"
              >
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

export default CoverageEligibilityRequestUpdate;

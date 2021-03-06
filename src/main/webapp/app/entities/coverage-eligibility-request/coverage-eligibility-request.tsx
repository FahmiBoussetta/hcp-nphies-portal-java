import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities, sendEntity } from './coverage-eligibility-request.reducer';
import { ICoverageEligibilityRequest } from 'app/shared/model/coverage-eligibility-request.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getCovTerm, getLocTerm, getOrgTerm, getPatTerm } from 'app/shared/util/autocomplete-utils';

export const CoverageEligibilityRequest = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const coverageEligibilityRequestList = useAppSelector(state => state.coverageEligibilityRequest.entities);
  const loading = useAppSelector(state => state.coverageEligibilityRequest.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const send = entity => {
    dispatch(sendEntity(entity));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="coverage-eligibility-request-heading" data-cy="CoverageEligibilityRequestHeading">
        <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.home.title">Coverage Eligibility Requests</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.home.createLabel">
              Create new Coverage Eligibility Request
            </Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {coverageEligibilityRequestList && coverageEligibilityRequestList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.guid">Guid</Translate>
                </th>
                <th>
                  <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.parsed">Parsed</Translate>
                </th>
                <th>
                  <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.purposes">Purposes</Translate>
                </th>
                <th>
                  <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.priority">Priority</Translate>
                </th>
                <th>
                  <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.identifier">Identifier</Translate>
                </th>
                <th>
                  <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.servicedDate">Serviced Date</Translate>
                </th>
                <th>
                  <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.servicedDateEnd">Serviced Date End</Translate>
                </th>
                <th>
                  <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.patient">Patient</Translate>
                </th>
                <th>
                  <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.insurer">Insurer</Translate>
                </th>
                <th>
                  <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.facility">Facility</Translate>
                </th>
                <th>
                  <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.coverages">Coverages</Translate>
                </th>
                <th>
                  <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.coverageEligibilityResponse">Response</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {coverageEligibilityRequestList.map((coverageEligibilityRequest, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${coverageEligibilityRequest.id}`} color="link" size="sm">
                      {coverageEligibilityRequest.id}
                    </Button>
                  </td>
                  <td>{coverageEligibilityRequest.guid}</td>
                  <td>{coverageEligibilityRequest.parsed?.substring(0, 80)}...</td>
                  <td>
                    {coverageEligibilityRequest.purposes
                      ? coverageEligibilityRequest.purposes.map((val, j) => (
                          <span key={j}>
                            {val.erp}
                            {j === coverageEligibilityRequest.purposes.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>
                    <Translate contentKey={`hcpNphiesPortalApp.PriorityEnum.${coverageEligibilityRequest.priority}`} />
                  </td>
                  <td>{coverageEligibilityRequest.identifier}</td>
                  <td>
                    {coverageEligibilityRequest.servicedDate ? (
                      <TextFormat type="date" value={coverageEligibilityRequest.servicedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {coverageEligibilityRequest.servicedDateEnd ? (
                      <TextFormat type="date" value={coverageEligibilityRequest.servicedDateEnd} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {coverageEligibilityRequest.patient ? (
                      <Link to={`patient/${coverageEligibilityRequest.patient.id}`}>{getPatTerm(coverageEligibilityRequest.patient)}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {coverageEligibilityRequest.insurer ? (
                      <Link to={`organization/${coverageEligibilityRequest.insurer.id}`}>
                        {getOrgTerm(coverageEligibilityRequest.insurer)}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {coverageEligibilityRequest.facility ? (
                      <Link to={`location/${coverageEligibilityRequest.facility.id}`}>
                        {getLocTerm(coverageEligibilityRequest.facility)}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {coverageEligibilityRequest.coverages
                      ? coverageEligibilityRequest.coverages.map((val, j) => (
                          <span key={j}>
                            <Link to={`coverage/${val.id}`}>{getCovTerm(val)}</Link>
                            {j === coverageEligibilityRequest.coverages.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>
                    {coverageEligibilityRequest.coverageEligibilityResponse ? (
                      <Link to={`coverage-eligibility-response/${coverageEligibilityRequest.coverageEligibilityResponse.id}`}>
                        {coverageEligibilityRequest.coverageEligibilityResponse.value}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`${match.url}/${coverageEligibilityRequest.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button onClick={() => send(coverageEligibilityRequest)} color="warning" size="sm" data-cy="entitySendButton">
                        <FontAwesomeIcon icon="paper-plane" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.send">Send</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${coverageEligibilityRequest.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${coverageEligibilityRequest.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="hcpNphiesPortalApp.coverageEligibilityRequest.home.notFound">
                No Coverage Eligibility Requests found
              </Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default CoverageEligibilityRequest;

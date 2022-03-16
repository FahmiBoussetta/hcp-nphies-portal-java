import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './care-team-sequence.reducer';
import { ICareTeamSequence } from 'app/shared/model/care-team-sequence.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CareTeamSequence = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const careTeamSequenceList = useAppSelector(state => state.careTeamSequence.entities);
  const loading = useAppSelector(state => state.careTeamSequence.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="care-team-sequence-heading" data-cy="CareTeamSequenceHeading">
        <Translate contentKey="hcpNphiesPortalApp.careTeamSequence.home.title">Care Team Sequences</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="hcpNphiesPortalApp.careTeamSequence.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="hcpNphiesPortalApp.careTeamSequence.home.createLabel">Create new Care Team Sequence</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {careTeamSequenceList && careTeamSequenceList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="hcpNphiesPortalApp.careTeamSequence.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="hcpNphiesPortalApp.careTeamSequence.careSeq">Care Seq</Translate>
                </th>
                <th>
                  <Translate contentKey="hcpNphiesPortalApp.careTeamSequence.item">Item</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {careTeamSequenceList.map((careTeamSequence, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${careTeamSequence.id}`} color="link" size="sm">
                      {careTeamSequence.id}
                    </Button>
                  </td>
                  <td>{careTeamSequence.careSeq}</td>
                  <td>{careTeamSequence.item ? <Link to={`item/${careTeamSequence.item.id}`}>{careTeamSequence.item.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${careTeamSequence.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${careTeamSequence.id}/edit`}
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
                        to={`${match.url}/${careTeamSequence.id}/delete`}
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
              <Translate contentKey="hcpNphiesPortalApp.careTeamSequence.home.notFound">No Care Team Sequences found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default CareTeamSequence;

import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './care-team-sequence.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CareTeamSequenceDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const careTeamSequenceEntity = useAppSelector(state => state.careTeamSequence.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="careTeamSequenceDetailsHeading">
          <Translate contentKey="hcpNphiesPortalApp.careTeamSequence.detail.title">CareTeamSequence</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{careTeamSequenceEntity.id}</dd>
          <dt>
            <span id="careSeq">
              <Translate contentKey="hcpNphiesPortalApp.careTeamSequence.careSeq">Care Seq</Translate>
            </span>
          </dt>
          <dd>{careTeamSequenceEntity.careSeq}</dd>
          <dt>
            <Translate contentKey="hcpNphiesPortalApp.careTeamSequence.item">Item</Translate>
          </dt>
          <dd>{careTeamSequenceEntity.item ? careTeamSequenceEntity.item.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/care-team-sequence" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/care-team-sequence/${careTeamSequenceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CareTeamSequenceDetail;

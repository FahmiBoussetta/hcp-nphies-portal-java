import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CareTeamSequence from './care-team-sequence';
import CareTeamSequenceDetail from './care-team-sequence-detail';
import CareTeamSequenceUpdate from './care-team-sequence-update';
import CareTeamSequenceDeleteDialog from './care-team-sequence-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CareTeamSequenceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CareTeamSequenceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CareTeamSequenceDetail} />
      <ErrorBoundaryRoute path={match.url} component={CareTeamSequence} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CareTeamSequenceDeleteDialog} />
  </>
);

export default Routes;

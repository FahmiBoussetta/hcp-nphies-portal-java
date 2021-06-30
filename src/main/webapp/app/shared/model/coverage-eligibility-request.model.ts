import dayjs from 'dayjs';
import { ICovEliErrorMessages } from 'app/shared/model/cov-eli-error-messages.model';
import { IListEligibilityPurposeEnum } from 'app/shared/model/list-eligibility-purpose-enum.model';
import { IPatient } from 'app/shared/model/patient.model';
import { IOrganization } from 'app/shared/model/organization.model';
import { ILocation } from 'app/shared/model/location.model';
import { ICoverage } from 'app/shared/model/coverage.model';
import { PriorityEnum } from 'app/shared/model/enumerations/priority-enum.model';

export interface ICoverageEligibilityRequest {
  id?: number;
  guid?: string | null;
  parsed?: string | null;
  priority?: PriorityEnum;
  identifier?: string | null;
  servicedDate?: string;
  servicedDateEnd?: string | null;
  errors?: ICovEliErrorMessages[] | null;
  purposes?: IListEligibilityPurposeEnum[] | null;
  patient?: IPatient | null;
  provider?: IOrganization | null;
  insurer?: IOrganization | null;
  facility?: ILocation | null;
  coverages?: ICoverage[] | null;
}

export const defaultValue: Readonly<ICoverageEligibilityRequest> = {};

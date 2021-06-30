import dayjs from 'dayjs';
import { IResponseInsuranceItem } from 'app/shared/model/response-insurance-item.model';
import { ICoverage } from 'app/shared/model/coverage.model';
import { ICoverageEligibilityResponse } from 'app/shared/model/coverage-eligibility-response.model';

export interface IResponseInsurance {
  id?: number;
  notInforceReason?: string | null;
  inforce?: boolean | null;
  benefitStart?: string | null;
  benefitEnd?: string | null;
  items?: IResponseInsuranceItem[] | null;
  coverage?: ICoverage | null;
  coverageEligibilityResponse?: ICoverageEligibilityResponse | null;
}

export const defaultValue: Readonly<IResponseInsurance> = {
  inforce: false,
};

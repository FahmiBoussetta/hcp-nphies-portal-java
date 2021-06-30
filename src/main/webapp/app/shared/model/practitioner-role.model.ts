import dayjs from 'dayjs';
import { IListRoleCodeEnum } from 'app/shared/model/list-role-code-enum.model';
import { IListSpecialtyEnum } from 'app/shared/model/list-specialty-enum.model';
import { IPractitioner } from 'app/shared/model/practitioner.model';
import { IOrganization } from 'app/shared/model/organization.model';

export interface IPractitionerRole {
  id?: number;
  guid?: string | null;
  forceId?: string | null;
  start?: string | null;
  end?: string | null;
  codes?: IListRoleCodeEnum[] | null;
  specialties?: IListSpecialtyEnum[] | null;
  practitioner?: IPractitioner | null;
  organization?: IOrganization | null;
}

export const defaultValue: Readonly<IPractitionerRole> = {};

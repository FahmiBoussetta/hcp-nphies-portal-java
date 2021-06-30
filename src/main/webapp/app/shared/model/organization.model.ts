import { IContact } from 'app/shared/model/contact.model';
import { IAddress } from 'app/shared/model/address.model';
import { OrganizationTypeEnum } from 'app/shared/model/enumerations/organization-type-enum.model';

export interface IOrganization {
  id?: number;
  guid?: string | null;
  forceId?: string | null;
  organizationLicense?: string | null;
  baseUrl?: string | null;
  organizationType?: OrganizationTypeEnum | null;
  name?: string | null;
  contacts?: IContact[] | null;
  address?: IAddress | null;
}

export const defaultValue: Readonly<IOrganization> = {};

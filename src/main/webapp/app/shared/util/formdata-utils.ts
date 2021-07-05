import { IExemptionComponent } from 'app/shared/model/exemption-component.model';
import { ICostToBeneficiaryComponent } from 'app/shared/model/cost-to-beneficiary-component.model';
import { IClassComponent } from 'app/shared/model/class-component.model';
import { IGivens } from 'app/shared/model/givens.model';
import { IHumanName } from 'app/shared/model/human-name.model';
import { convertDateTimeToServer } from './date-utils';

export const initialGivenFormData: IGivens = Object.freeze({});
export function getGivenTerm(c: IGivens) {
  return c.textName ?? '';
}

export const initialHumanNameFormData: IHumanName = Object.freeze({});
export function getHumanNameTerm(c: IHumanName) {
  return (c.givens.map(x => x.textName).join(', ') + ' ' + c.family).trim();
}

export const initialClassComponentFormData: IClassComponent = Object.freeze({});
export function getClassTerm(c: IClassComponent) {
  return ((c.type ?? '') + ' ' + (c.value ?? '') + ' ' + (c.name ?? '')).trim();
}

export const initialCostToBeneficiaryComponentFormData: ICostToBeneficiaryComponent = Object.freeze({});
export function getCostTerm(c: ICostToBeneficiaryComponent) {
  return (
    (c.type ?? '') +
    ' ' +
    (c.value ?? '') +
    (c.isMoney ? ' SAR' : '%') +
    (c.exceptions?.length > 0 ? ' ( ' + c.exceptions?.map(x => getExempTerm(x)).join(', ') + ' )' : '')
  ).trim();
}

export const initialExemptionFormData: IExemptionComponent = Object.freeze({});
export function getExempTerm(e) {
  return (
    'except ' +
    e.type?.toString() +
    (e.start ? ' from ' + convertDateTimeToServer(e.start).toDateString() : '') +
    (e.end ? ' to ' + convertDateTimeToServer(e.end).toDateString() : '')
  ).trim();
}

import { ICareTeam } from 'app/shared/model/care-team.model';
import { IClassComponent } from 'app/shared/model/class-component.model';
import { ICostToBeneficiaryComponent } from 'app/shared/model/cost-to-beneficiary-component.model';
import { IDiagnosis } from 'app/shared/model/diagnosis.model';
import { IExemptionComponent } from 'app/shared/model/exemption-component.model';
import { IGivens } from 'app/shared/model/givens.model';
import { IHumanName } from 'app/shared/model/human-name.model';
import { IInsurance } from 'app/shared/model/insurance.model';
import { ISupportingInfo } from 'app/shared/model/supporting-info.model';
import { IDetailItem } from '../model/detail-item.model';
import { IItem } from '../model/item.model';
import { IRelated } from '../model/related.model';
import { ISubDetailItem } from '../model/sub-detail-item.model';
import { IReferenceIdentifier } from './../model/reference-identifier.model';
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

export const initialRelatedFormData: IRelated = Object.freeze({});
export function getRelatedTerm(r: IRelated) {
  return ((r.claim ? r.claim.id : r.claimReference ? r.claimReference.identifier : '') + ' - ' + (r.relationShip?.toString() ?? '')).trim();
}

export const initialCareTeamFormData: ICareTeam = Object.freeze({});
export function getCareTeamTerm(c: ICareTeam) {
  return c.sequence ?? '';
}

export const initialInsurancesFormData: IInsurance = Object.freeze({});
export function getInsurancesTerm(c: IInsurance) {
  return c.sequence ?? '';
}

export const initialDiagnosisFormData: IDiagnosis = Object.freeze({});
export function getDiagnosisTerm(c: IDiagnosis) {
  return c.sequence ?? '';
}

export const initialSupportingInfoFormData: ISupportingInfo = Object.freeze({});
export function getSupportingInfoTerm(c: ISupportingInfo) {
  return c.sequence ?? '';
}

export const initialItemFormData: IItem = Object.freeze({});
export const initialDetailItemFormData: IDetailItem = Object.freeze({});
export const initialSubDetailItemFormData: ISubDetailItem = Object.freeze({});
export function getItemTerm(i) {
  return i.sequence ?? '';
}

export const initialUdiFormData: IReferenceIdentifier = Object.freeze({});
export const initialDetailUdiFormData: IReferenceIdentifier = Object.freeze({});
export const initialSubDetailUdiFormData: IReferenceIdentifier = Object.freeze({});
export function getUdiTerm(i) {
  return i.ref ? i.ref + '/' + i.id : i.identifier;
}

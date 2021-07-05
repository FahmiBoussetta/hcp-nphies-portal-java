import { Organization } from './../../entities/organization/organization';
export function matchEncToTerm(text, value) {
  return text.label.toLowerCase().indexOf(value.toLowerCase()) !== -1;
}

export function matchLocToTerm(loc, value) {
  return loc.identifier.toLowerCase().indexOf(value.toLowerCase()) !== -1;
}

export function getLocTerm(loc) {
  return loc.identifier;
}

export function matchPatToTerm(pat, value) {
  return (
    pat.residentNumber?.toLowerCase().indexOf(value.toLowerCase()) !== -1 ||
    pat.nationalHealthId?.toLowerCase().indexOf(value.toLowerCase()) !== -1 ||
    pat.iqama?.toLowerCase().indexOf(value.toLowerCase()) !== -1 ||
    pat.passportNumber?.toLowerCase().indexOf(value.toLowerCase()) !== -1
  );
}

export function getPatTerm(pat) {
  return pat?.iqama;
}

export function matchPracToTerm(text, value) {
  return text.label.toLowerCase().indexOf(value.toLowerCase()) !== -1;
}

export function matchPracRoleToTerm(text, value) {
  return text.label.toLowerCase().indexOf(value.toLowerCase()) !== -1;
}

export function matchCovToTerm(text, value) {
  return text.subscriberId?.toLowerCase().indexOf(value.toLowerCase()) !== -1;
}

export function getCovTerm(cov) {
  return cov.subscriberId + (cov.beneficiary ? ' - ' + getPatTerm(cov.beneficiary) ?? '' : '');
}

export function matchOrgToTerm(org, value) {
  return org.organizationLicense.toLowerCase().indexOf(value.toLowerCase()) !== -1;
}

export function getOrgTerm(org) {
  return org.organizationLicense;
}

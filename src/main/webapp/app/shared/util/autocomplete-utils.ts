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
  return pat?.residentNumber && pat?.residentNumber !== ''
    ? pat?.residentNumber
    : pat?.nationalHealthId && pat?.nationalHealthId !== ''
    ? pat?.nationalHealthId
    : pat?.iqama && pat?.iqama !== ''
    ? pat?.iqama
    : pat?.passportNumber;
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
  return cov.id + (cov.subscriberId ? ' - ' + cov.subscriberId : '') + (cov.beneficiary ? ' - ' + getPatTerm(cov.beneficiary) ?? '' : '');
}

export function matchOrgToTerm(org, value) {
  return org.organizationLicense != null && org.organizationLicense.toLowerCase().indexOf(value.toLowerCase()) !== -1;
}

export function getOrgTerm(org) {
  return org.organizationLicense;
}

export function matchClaimToTerm(claim, value) {
  return claim.identifier.indexOf(value) !== -1;
}

export function getClaimTerm(claim) {
  return claim.identifier;
}

export function matchProdToTerm(prod, value) {
  return prod.toLowerCase().indexOf(value.toLowerCase()) !== -1;
}

export function getProdTerm(p) {
  const s = p.substring(0, p.length - 1);
  return s.substring(s.lastIndexOf('|') + 1);
}

export function matchPractitionerToTerm(p, value) {
  return p.id.toString().indexOf(value) !== -1;
}

export function getPractitionerTerm(p) {
  return p.id.toString();
}

export function matchPractitionerRoleToTerm(p, value) {
  return p.id.toString().indexOf(value) !== -1;
}

export function getPractitionerRoleTerm(p) {
  return p.id.toString();
}

export function matchCrToTerm(c, value) {
  return c.id.toString().indexOf(value) !== -1;
}

export function getCrTerm(c) {
  return c.id.toString();
}

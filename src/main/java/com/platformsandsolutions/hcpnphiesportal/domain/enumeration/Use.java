package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

/**
 * The Use enumeration.
 */
public enum Use {
    Claim("claim"),
    PreAuthorization("preauthorization"),
    Predetermination("predetermination");

    private final String value;

    Use(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public org.hl7.fhir.r4.model.Claim.Use convert() {
        switch (this.getValue()) {
            case "predetermination":
                return org.hl7.fhir.r4.model.Claim.Use.PREDETERMINATION;
            case "preauthorization":
                return org.hl7.fhir.r4.model.Claim.Use.PREAUTHORIZATION;
            case "claim":
                return org.hl7.fhir.r4.model.Claim.Use.CLAIM;
        }
        return null;
    }
}

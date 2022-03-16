package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The ClaimRelationshipEnum enumeration.
 */
public enum ClaimRelationshipEnum {
    Prior("Prior Claim"),
    Associated("Associated Claim"),
    Extend("Authorization to extend");

    private final String value;

    ClaimRelationshipEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.ClaimRelationshipEnum convert() {
        for (Enums.ClaimRelationshipEnum e : Enums.ClaimRelationshipEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

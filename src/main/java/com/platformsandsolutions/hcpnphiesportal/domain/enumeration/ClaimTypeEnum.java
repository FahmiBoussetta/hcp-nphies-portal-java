package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The ClaimTypeEnum enumeration.
 */
public enum ClaimTypeEnum {
    Institutional("InPatient"),
    Oral("Dental"),
    Pharmacy("Pharmacy"),
    Professional("OutPatient"),
    Vision("Optical");

    private final String value;

    ClaimTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.ClaimTypeEnum convert() {
        for (Enums.ClaimTypeEnum e : Enums.ClaimTypeEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

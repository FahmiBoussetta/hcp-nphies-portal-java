package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The ClaimSubTypeEnum enumeration.
 */
public enum ClaimSubTypeEnum {
    Ip("InPatient"),
    Op("OutPatient"),
    Emr("Emergency");

    private final String value;

    ClaimSubTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.ClaimSubTypeEnum convert() {
        for (Enums.ClaimSubTypeEnum e : Enums.ClaimSubTypeEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

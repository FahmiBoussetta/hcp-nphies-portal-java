package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The ReAdmissionEnum enumeration.
 */
public enum ReAdmissionEnum {
    R("Re-admission");

    private final String value;

    ReAdmissionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.ReAdmissionEnum convert() {
        for (Enums.ReAdmissionEnum e : Enums.ReAdmissionEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

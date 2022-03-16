package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The AccidentTypeEnum enumeration.
 */
public enum AccidentTypeEnum {
    MVA("Motor vehicle accident"),
    SCHOOL("School Accident"),
    SPT("Sporting Accident"),
    WPA("Workplace accident");

    private final String value;

    AccidentTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.AccidentTypeEnum convert() {
        for (Enums.AccidentTypeEnum e : Enums.AccidentTypeEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

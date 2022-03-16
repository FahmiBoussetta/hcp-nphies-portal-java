package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The ActPriorityEnum enumeration.
 */
public enum ActPriorityEnum {
    EM("Emergency"),
    EL("Elective");

    private final String value;

    ActPriorityEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.ActPriorityEnum convert() {
        for (Enums.ActPriorityEnum e : Enums.ActPriorityEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

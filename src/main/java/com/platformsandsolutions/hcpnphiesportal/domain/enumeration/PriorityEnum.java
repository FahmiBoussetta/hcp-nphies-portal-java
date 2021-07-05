package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.*;

/**
 * The PriorityEnum enumeration.
 */
public enum PriorityEnum {
    Stat("Immediate"),
    Normal("Normal"),
    Deferred("Deferred");

    private final String value;

    PriorityEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.PriorityEnum convert() {
        for (Enums.PriorityEnum e : Enums.PriorityEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

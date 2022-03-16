package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The DrgEnum enumeration.
 */
public enum DrgEnum {
    none("None");

    private final String value;

    DrgEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.DrgEnum convert() {
        for (Enums.DrgEnum e : Enums.DrgEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

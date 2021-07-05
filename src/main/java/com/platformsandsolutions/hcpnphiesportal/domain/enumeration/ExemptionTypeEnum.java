package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The ExemptionTypeEnum enumeration.
 */
public enum ExemptionTypeEnum {
    Retired("Retired"),
    Foster("Foster child");

    private final String value;

    ExemptionTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.ExemptionTypeEnum convert() {
        for (Enums.ExemptionTypeEnum e : Enums.ExemptionTypeEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

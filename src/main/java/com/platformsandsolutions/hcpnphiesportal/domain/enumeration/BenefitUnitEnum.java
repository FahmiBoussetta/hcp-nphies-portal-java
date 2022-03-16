package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The BenefitUnitEnum enumeration.
 */
public enum BenefitUnitEnum {
    individual("Individual"),
    family("Family");

    private final String value;

    BenefitUnitEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.BenefitUnitEnum convert() {
        for (Enums.BenefitUnitEnum e : Enums.BenefitUnitEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

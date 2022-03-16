package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The BenefitTermEnum enumeration.
 */
public enum BenefitTermEnum {
    annual("Annual"),
    day("Day"),
    lifetime("Lifetime");

    private final String value;

    BenefitTermEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.BenefitTermEnum convert() {
        for (Enums.BenefitTermEnum e : Enums.BenefitTermEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

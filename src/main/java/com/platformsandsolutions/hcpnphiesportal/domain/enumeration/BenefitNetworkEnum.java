package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The BenefitNetworkEnum enumeration.
 */
public enum BenefitNetworkEnum {
    in("In Network"),
    out("Out of Network");

    private final String value;

    BenefitNetworkEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.BenefitNetworkEnum convert() {
        for (Enums.BenefitNetworkEnum e : Enums.BenefitNetworkEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The PayeeTypeEnum enumeration.
 */
public enum PayeeTypeEnum {
    Subscriber("Subscriber"),
    Provider("Provider"),
    Other("Other");

    private final String value;

    PayeeTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.PayeeTypeEnum convert() {
        for (Enums.PayeeTypeEnum e : Enums.PayeeTypeEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

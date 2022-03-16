package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The FundsReserveEnum enumeration.
 */
public enum FundsReserveEnum {
    Patient("Patient"),
    Provider("Provider"),
    None("None");

    private final String value;

    FundsReserveEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.FundsReserveEnum convert() {
        for (Enums.FundsReserveEnum e : Enums.FundsReserveEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

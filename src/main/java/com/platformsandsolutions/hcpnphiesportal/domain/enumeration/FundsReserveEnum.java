package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

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
}

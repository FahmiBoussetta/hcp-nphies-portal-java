package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

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
}

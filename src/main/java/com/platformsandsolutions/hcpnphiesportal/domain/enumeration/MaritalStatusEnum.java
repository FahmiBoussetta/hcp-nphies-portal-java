package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.*;

/**
 * The MaritalStatusEnum enumeration.
 */
public enum MaritalStatusEnum {
    L("Legally separated"),
    D("Divorced"),
    M("Married"),
    U("Unmarried"),
    W("Widowed"),
    UNK("Unknown");

    private final String value;

    MaritalStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.MaritalStatusEnum convert() {
        return Enums.MaritalStatusEnum.valueOf(this.name());
    }
}

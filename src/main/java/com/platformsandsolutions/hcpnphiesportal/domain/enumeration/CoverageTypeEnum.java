package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.*;

/**
 * The CoverageTypeEnum enumeration.
 */
public enum CoverageTypeEnum {
    EHCPOL("Extended healthcare"),
    PUBLICPOL("Public healthcare");

    private final String value;

    CoverageTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.CoverageTypeEnum convert() {
        return Enums.CoverageTypeEnum.valueOf(this.name());
    }
}

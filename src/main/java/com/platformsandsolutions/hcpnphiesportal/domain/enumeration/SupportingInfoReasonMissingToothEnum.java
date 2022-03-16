package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The SupportingInfoReasonMissingToothEnum enumeration.
 */
public enum SupportingInfoReasonMissingToothEnum {
    E("Extraction"),
    C("Congenital"),
    U("Unknown"),
    O("Other");

    private final String value;

    SupportingInfoReasonMissingToothEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.SupportingInfoReasonMissingToothEnum convert() {
        for (Enums.SupportingInfoReasonMissingToothEnum e : Enums.SupportingInfoReasonMissingToothEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

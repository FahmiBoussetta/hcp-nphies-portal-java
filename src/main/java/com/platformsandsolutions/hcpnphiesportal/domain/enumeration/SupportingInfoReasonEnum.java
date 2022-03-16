package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The SupportingInfoReasonEnum enumeration.
 */
public enum SupportingInfoReasonEnum {
    Missing_info("Missing information"),
    Missing_attach("Missing attachement"),
    Info_Correct("Information correction");

    private final String value;

    SupportingInfoReasonEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.SupportingInfoReasonEnum convert() {
        for (Enums.SupportingInfoReasonEnum e : Enums.SupportingInfoReasonEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The SupportingInfoCodeVisitEnum enumeration.
 */
public enum SupportingInfoCodeVisitEnum {
    New_visit("New Visit"),
    Follow_up("Follow Up"),
    Refill("Refill"),
    Walk_in("Walk in"),
    Referral("Referral");

    private final String value;

    SupportingInfoCodeVisitEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.SupportingInfoCodeVisitEnum convert() {
        for (Enums.SupportingInfoCodeVisitEnum e : Enums.SupportingInfoCodeVisitEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

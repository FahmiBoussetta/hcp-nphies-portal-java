package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The RemittanceOutcomeEnum enumeration.
 */
public enum RemittanceOutcomeEnum {
    queued("QUEUED"),
    complete("COMPLETE"),
    error("ERROR"),
    partial("PARTIAL");

    private final String value;

    RemittanceOutcomeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.RemittanceOutcomeEnum convert() {
        for (Enums.RemittanceOutcomeEnum e : Enums.RemittanceOutcomeEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

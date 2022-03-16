package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The AdjudicationOutcomeEnum enumeration.
 */
public enum AdjudicationOutcomeEnum {
    approved("Approved"),
    rejected("Rejected"),
    partial("Partially Approved"),
    not_required("Not Required"),
    pended("Pended");

    private final String value;

    AdjudicationOutcomeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.AdjudicationOutcomeEnum convert() {
        for (Enums.AdjudicationOutcomeEnum e : Enums.AdjudicationOutcomeEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

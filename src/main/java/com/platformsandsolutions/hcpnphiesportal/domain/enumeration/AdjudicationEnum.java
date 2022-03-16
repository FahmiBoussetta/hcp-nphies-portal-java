package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The AdjudicationEnum enumeration.
 */
public enum AdjudicationEnum {
    submitted("Submitted Amount"),
    copay("Patient Co-Payment"),
    eligible("Eligible Amount"),
    deductible("Deductible"),
    unallocDeduct("Unallocated Deductible"),
    tax("Tax"),
    eligpercent("Eligible %"),
    benefit("Benefit Amount");

    private final String value;

    AdjudicationEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.AdjudicationEnum convert() {
        for (Enums.AdjudicationEnum e : Enums.AdjudicationEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

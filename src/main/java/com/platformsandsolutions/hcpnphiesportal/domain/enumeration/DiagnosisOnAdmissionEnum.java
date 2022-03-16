package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The DiagnosisOnAdmissionEnum enumeration.
 */
public enum DiagnosisOnAdmissionEnum {
    Y("Yes"),
    N("No"),
    U("Unknown");

    private final String value;

    DiagnosisOnAdmissionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.DiagnosisOnAdmissionEnum convert() {
        for (Enums.DiagnosisOnAdmissionEnum e : Enums.DiagnosisOnAdmissionEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

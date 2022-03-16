package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The ReissueReasonEnum enumeration.
 */
public enum ReissueReasonEnum {
    correction("Error correction"),
    adjudication("Additional adjudication"),
    miscalculation("Miscalculation correction"),
    benefit("Benefit processing correction"),
    audit("Audit"),
    other("Other");

    private final String value;

    ReissueReasonEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.ReissueReasonEnum convert() {
        for (Enums.ReissueReasonEnum e : Enums.ReissueReasonEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

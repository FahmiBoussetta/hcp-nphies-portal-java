package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The EncounterClassEnum enumeration.
 */
public enum EncounterClassEnum {
    AMB("ambulatory"),
    EMER("emergency"),
    HH("home health"),
    IMP("inpatient encounter"),
    SS("short stay");

    private final String value;

    EncounterClassEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.EncounterClassEnum convert() {
        for (Enums.EncounterClassEnum e : Enums.EncounterClassEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

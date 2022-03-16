package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The RoleCodeEnum enumeration.
 */
public enum RoleCodeEnum {
    Doctor("Doctor"),
    Nurse("Nurse"),
    Pharmacist("Pharmacist"),
    Researcher("Researcher"),
    Teacher("Teacher or educator"),
    Dentist("Dentist"),
    Physio("Physiotherapist"),
    Speech("Speechtherapist"),
    Ict("ICT professional");

    private final String value;

    RoleCodeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.RoleCodeEnum convert() {
        for (Enums.RoleCodeEnum e : Enums.RoleCodeEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

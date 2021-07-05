package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

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
}

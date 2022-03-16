package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.*;

/**
 * The AdministrativeGenderEnum enumeration.
 */
public enum AdministrativeGenderEnum {
    male("Male"),
    female("Female"),
    unknown("Unknown"),
    U("Undetermined"),
    N("Undifferentiated"),
    A("Sex changed to Male"),
    B("Sex changed to female "),
    C("Not Completed");

    private final String value;

    AdministrativeGenderEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.AdministrativeGenderEnum convert() {
        for (Enums.AdministrativeGenderEnum e : Enums.AdministrativeGenderEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

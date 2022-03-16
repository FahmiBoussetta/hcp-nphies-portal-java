package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The LanguageEnum enumeration.
 */
public enum LanguageEnum {
    AR("Arabic"),
    EN("English");

    private final String value;

    LanguageEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.LanguageEnum convert() {
        for (Enums.LanguageEnum e : Enums.LanguageEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

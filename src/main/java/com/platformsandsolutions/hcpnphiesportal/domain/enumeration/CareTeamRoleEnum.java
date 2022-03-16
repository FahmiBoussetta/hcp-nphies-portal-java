package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The CareTeamRoleEnum enumeration.
 */
public enum CareTeamRoleEnum {
    Primary("Primary provider"),
    Assist("Assisting Provider"),
    Supervisor("Supervising Provider"),
    Other("Other");

    private final String value;

    CareTeamRoleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.CareTeamRoleEnum convert() {
        for (Enums.CareTeamRoleEnum e : Enums.CareTeamRoleEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

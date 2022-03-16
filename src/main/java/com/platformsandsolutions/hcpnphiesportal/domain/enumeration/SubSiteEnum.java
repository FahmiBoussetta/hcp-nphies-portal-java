package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The SubSiteEnum enumeration.
 */
public enum SubSiteEnum {
    R("Right"),
    L("Left"),
    U("Upper"),
    D("Down"),
    A("Anterior"),
    P("Posterior"),
    I("interior"),
    E("Exterior");

    private final String value;

    SubSiteEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.SubSiteEnum convert() {
        for (Enums.SubSiteEnum e : Enums.SubSiteEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

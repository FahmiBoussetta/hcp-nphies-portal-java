package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.*;

/**
 * The RelationShipEnum enumeration.
 */
public enum RelationShipEnum {
    Child("Child"),
    Parent("Parent"),
    Spouse("Spouse"),
    Common("Common Law Spouse"),
    Other("Other"),
    Self("Self"),
    Injured("Injured");

    private final String value;

    RelationShipEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.RelationShipEnum convert() {
        for (Enums.RelationShipEnum e : Enums.RelationShipEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

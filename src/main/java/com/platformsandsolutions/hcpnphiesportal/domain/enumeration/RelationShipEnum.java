package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.*;

/**
 * The RelationShipEnum enumeration.
 */
public enum RelationShipEnum {
    child("Child"),
    parent("Parent"),
    spouse("Spouse"),
    common("Common Law Spouse"),
    other("Other"),
    self("Self"),
    injured("Injured");

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

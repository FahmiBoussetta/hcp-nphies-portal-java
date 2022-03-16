package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The OrganizationTypeEnum enumeration.
 */
public enum OrganizationTypeEnum {
    prov("Healthcare Provider"),
    dept("Hospital Department"),
    ins("Insurance Company"),
    pay("Payer"),
    other("Other");

    private final String value;

    OrganizationTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.OrganizationTypeEnum convert() {
        for (Enums.OrganizationTypeEnum e : Enums.OrganizationTypeEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

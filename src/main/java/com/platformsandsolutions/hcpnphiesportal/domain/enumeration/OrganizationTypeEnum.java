package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The OrganizationTypeEnum enumeration.
 */
public enum OrganizationTypeEnum {
    Prov("Healthcare Provider"),
    Dept("Hospital Department"),
    Ins("Insurance Company"),
    Pay("Payer"),
    Other("Other");

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

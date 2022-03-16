package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The ServiceTypeEnum enumeration.
 */
public enum ServiceTypeEnum {
    N237("Acute Inpatient Serv"),
    N576("Rehabilitation"),
    N356("General Maintenance"),
    N621("Complex Maintenance"),
    N179("Palliative medicine");

    private final String value;

    ServiceTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.ServiceTypeEnum convert() {
        for (Enums.ServiceTypeEnum e : Enums.ServiceTypeEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

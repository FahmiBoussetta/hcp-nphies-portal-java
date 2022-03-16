package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

public enum NotInForceReasonEnum {
    NC("No coverage inforce"),
    ALC("Annual limit consumed"),
    ONW("Provider outside member Network");

    private String value;

    private NotInForceReasonEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.NotInForceReasonEnum convert() {
        for (Enums.NotInForceReasonEnum e : Enums.NotInForceReasonEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

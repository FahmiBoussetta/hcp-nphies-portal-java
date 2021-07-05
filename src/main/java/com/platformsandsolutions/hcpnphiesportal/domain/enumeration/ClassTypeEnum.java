package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.*;

/**
 * The ClassTypeEnum enumeration.
 */
public enum ClassTypeEnum {
    Group("Group"),
    Subgroup("SubGroup"),
    Plan("Plan"),
    Subplan("SubPlan"),
    Class("Class"),
    Subclass("SubClass"),
    Sequence("Sequence"),
    Rxbin("RX BIN"),
    Rxpcn("RX PCN"),
    Rxid("RX Id"),
    Rxgroup("RX Group");

    private final String value;

    ClassTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.ClassTypeEnum convert() {
        for (Enums.ClassTypeEnum e : Enums.ClassTypeEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

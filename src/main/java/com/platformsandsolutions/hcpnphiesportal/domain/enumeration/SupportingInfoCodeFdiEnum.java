package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The SupportingInfoCodeFdiEnum enumeration.
 */
public enum SupportingInfoCodeFdiEnum {
    N11("UPPER RIGHT; PERMANENT TEETH # 1"),
    N12("UPPER RIGHT; PERMANENT TEETH # 2"),
    N13("UPPER RIGHT; PERMANENT TEETH # 3"),
    N14("UPPER RIGHT; PERMANENT TEETH # 4"),
    N15("UPPER RIGHT; PERMANENT TEETH # 5"),
    N16("UPPER RIGHT; PERMANENT TEETH # 6"),
    N17("UPPER RIGHT; PERMANENT TEETH # 7"),
    N18("UPPER RIGHT; PERMANENT TEETH # 8"),
    N21("UPPER LEFT; PERMANENT TEETH # 1"),
    N22("UPPER LEFT; PERMANENT TEETH # 2"),
    N23("UPPER LEFT; PERMANENT TEETH # 3"),
    N24("UPPER LEFT; PERMANENT TEETH # 4"),
    N25("UPPER LEFT; PERMANENT TEETH # 5"),
    N26("UPPER LEFT; PERMANENT TEETH # 6"),
    N27("UPPER LEFT; PERMANENT TEETH # 7"),
    N28("UPPER LEFT; PERMANENT TEETH # 8"),
    N31("LOWER LEFT; PERMANENT TEETH # 1"),
    N32("LOWER LEFT; PERMANENT TEETH # 2"),
    N33("LOWER LEFT; PERMANENT TEETH # 3"),
    N34("LOWER LEFT; PERMANENT TEETH # 4"),
    N35("LOWER LEFT; PERMANENT TEETH # 5"),
    N36("LOWER LEFT; PERMANENT TEETH # 6"),
    N37("LOWER LEFT; PERMANENT TEETH # 7"),
    N38("LOWER LEFT; PERMANENT TEETH # 8"),
    N41("LOWER RIGHT; PERMANENT TEETH # 1"),
    N42("LOWER RIGHT; PERMANENT TEETH # 2"),
    N43("LOWER RIGHT; PERMANENT TEETH # 3"),
    N44("LOWER RIGHT; PERMANENT TEETH # 4"),
    N45("LOWER RIGHT; PERMANENT TEETH # 5"),
    N46("LOWER RIGHT; PERMANENT TEETH # 6"),
    N47("LOWER RIGHT; PERMANENT TEETH # 7"),
    N48("LOWER RIGHT; PERMANENT TEETH # 8"),
    N51("UPPER RIGHT; DECIDUOUS TEETH # 1"),
    N52("UPPER RIGHT; DECIDUOUS TEETH # 2"),
    N53("UPPER RIGHT; DECIDUOUS TEETH # 3"),
    N54("UPPER RIGHT; DECIDUOUS TEETH # 4"),
    N55("UPPER RIGHT; DECIDUOUS TEETH # 5"),
    N61("UPPER LEFT; DECIDUOUS TEETH # 1"),
    N62("UPPER LEFT; DECIDUOUS TEETH # 2"),
    N63("UPPER LEFT; DECIDUOUS TEETH # 3"),
    N64("UPPER LEFT; DECIDUOUS TEETH # 4"),
    N65("UPPER LEFT; DECIDUOUS TEETH # 5"),
    N71("LOWER LEFT; DECIDUOUS TEETH # 1"),
    N72("LOWER LEFT; DECIDUOUS TEETH # 2"),
    N73("LOWER LEFT; DECIDUOUS TEETH # 3"),
    N74("LOWER LEFT; DECIDUOUS TEETH # 4"),
    N75("LOWER LEFT; DECIDUOUS TEETH # 5"),
    N81("LOWER RIGHT; DECIDUOUS TEETH # 1"),
    N82("LOWER RIGHT; DECIDUOUS TEETH # 2"),
    N83("LOWER RIGHT; DECIDUOUS TEETH # 3"),
    N84("LOWER RIGHT; DECIDUOUS TEETH # 4"),
    N85("LOWER RIGHT; DECIDUOUS TEETH # 5");

    private final String value;

    SupportingInfoCodeFdiEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.SupportingInfoCodeFdiEnum convert() {
        for (Enums.SupportingInfoCodeFdiEnum e : Enums.SupportingInfoCodeFdiEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

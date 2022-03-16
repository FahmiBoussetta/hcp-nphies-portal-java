package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The BenefitCategoryEnum enumeration.
 */
public enum BenefitCategoryEnum {
    _1("Medical Care"),
    _2("Surgical"),
    _3("Consultation"),
    _4("Diagnostic XRay"),
    _5("Diagnostic Lab"),
    _6("Renal Supplies"),
    _7("Diagnostic Dental"),
    _8("Periodontics"),
    _9("Restorative"),
    _10("Endodontics"),
    _11("Maxillofacial Prosthetics"),
    _12("Adjunctive Dental Services"),
    _13("Health Benefit Plan Coverage"),
    _14("Dental Care"),
    _15("Dental Crowns"),
    _16("Dental Accident"),
    _17("Hospital Room and Board"),
    _18("Major Medical"),
    _19("Medically Related Transportation"),
    _20("In-vitro Fertilization"),
    _21("MRI Scan"),
    _22("Donor Procedures"),
    _23("Maternity"),
    _24("Renal Dialysis"),
    _25("Medical Coverage"),
    _26("Dental Coverage"),
    _27("Hearing Coverage"),
    _28("Vision Coverage"),
    _29("Mental Health"),
    _30("OP Medical"),
    _31("Max Copay");

    private final String value;

    BenefitCategoryEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.BenefitCategoryEnum convert() {
        for (Enums.BenefitCategoryEnum e : Enums.BenefitCategoryEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

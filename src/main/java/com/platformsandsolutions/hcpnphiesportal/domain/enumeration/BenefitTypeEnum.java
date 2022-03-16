package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The BenefitTypeEnum enumeration.
 */
public enum BenefitTypeEnum {
    benefit("Benefit"),
    deductible("Deductible"),
    visit("Visit"),
    room("Room"),
    copay("Copayment per service"),
    copay_percent("Copayment Percent per service"),
    copay_maximum("Copayment maximum per service"),
    vision_exam("Vision Exam"),
    vision_glasses("Vision Glasses"),
    vision_contacts("Vision Contacts Coverage"),
    medical_primarycare("Medical Primary Health Coverage"),
    pharmacy_dispense("Pharmacy Dispense Coverage");

    private final String value;

    BenefitTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.BenefitTypeEnum convert() {
        for (Enums.BenefitTypeEnum e : Enums.BenefitTypeEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

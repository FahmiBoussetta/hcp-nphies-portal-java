package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

/**
 * The SupportingInfoCategoryEnum enumeration.
 */
public enum SupportingInfoCategoryEnum {
    Info("Information"),
    Discharge("Discharge"),
    Onset("Onset"),
    Related("Related Services"),
    Exception("Exception"),
    Material("Materials Forwarded"),
    Attachment("Attachment"),
    Missingtooth("Missing Tooth"),
    Prosthesis("Prosthesis"),
    Other("Other"),
    Hospitalized("Hospitalized"),
    EmploymentImpacted("Employment Impacted"),
    External_Cause("External Caause"),
    Patient_Reason_for_Visit("Patient Reason for Visit"),
    Lab_test("lab-test"),
    Reason_for_Visit("Reason for visit"),
    Days_Supply("Days supply"),
    Vital_Sign_Weight("Vital Sign Weight"),
    Vital_Sign_Systolic("Vital Sign Systolic"),
    Vital_Sign_Diastolic("Vital Sign Diastolic"),
    Icu_hours("ICU Hours"),
    Ventilation_hours("Ventilation Hours"),
    Vital_Sign_Height("Vital Sign Height"),
    Chief_complaint("Chief complaint");

    private final String value;

    SupportingInfoCategoryEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.SupportingInfoCategoryEnum convert() {
        for (Enums.SupportingInfoCategoryEnum e : Enums.SupportingInfoCategoryEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

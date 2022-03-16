package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

import platform.fhir_client.utils.Enums;

public enum AdjudicationReasonEnum {
    NDC001("Prior approval is required and was not obtained"),
    NDC002("Diagnosis(es) is (are) not covered"),
    NDC003("Service(s) is (are) not covered"),
    NDC004("Experimental or investigational services, treatments, devices and medications are not covered"),
    NDC005("Acupuncture, acupressure, hypnotism, and other forms of alternative treatment are not covered"),
    NDC006("Air ambulance transportation services are not covered"),
    NDC007("Allergy services and treatments are not covered"),
    NDC008("Ambulance transportation for non-emergency situations are not covered"),
    NDC009("Behavioral and mental health services, treatments and medications are not covered"),
    NDC010("Dental treatments are not covered"),
    NDC011("Elective procedure is not covered"),
    NDC012("Pre-existing conditions are not covered"),
    NDC013("Alternative service should have been utilized"),
    NDC014("Service(s) is (are) performed outside authorization validity date"),
    NDC015("Claim is a work-related injury/illness and thus the liability of the employer "),
    NDC016("Deductible/co-pay not collected from member"),
    NDC017("Annual limit/sublimit amount exceeded "),
    NDC018("Eye exams and treatments are not covered"),
    NDC019(
        "Gender manipulation services and associated expenses including voluntary sterilizations or reversal of sterilizations are not covered"
    ),
    NDC020("Hearing services are not covered"),
    NDC021("Home Health (Private Nurse) services are not covered"),
    NDC022("Home Health (Respite) services are not covered"),
    NDC023("Home Health services are not covered"),
    NDC024("Immunizations are not covered"),
    NDC025("Preventative care is not covered"),
    NDC026("Services resulting from attempted suicide or self-infliction are not covered"),
    NDC027("Vaccinations are not covered"),
    NDC028("Service(s) is (are) not performed"),
    NDC029("Service(s) is (are) performed outside period of hospitalization/admission"),
    NDC030("Chronic Condition Service(s) is (are) not covered"),
    NDC031("Waiting period on pre-existing / specific conditions"),
    NDC032("Patient is not a covered member"),
    NDC033("Services performed after the last date of coverage"),
    NDC034("Services performed prior to the effective date of coverage"),
    NDC035("Services performed by a non-network provider"),
    NDC036("Benefit maximum for this time period or occurrence has been reached"),
    NDC037("Calculation discrepancy "),
    NDC038("Service has no contract price"),
    NDC039("Multiple procedure payment rules incorrectly applied"),
    NDC040("Charges inconsistent with clinician specialty"),
    NDC041("Payment is included in the allowance for another service"),
    NDC042("Recovery of Payment"),
    NDC043("Consultation within free follow up period"),
    NDC044("Service is not clinically indicated based on good clinical practice"),
    NDC045("Service is not clinically indicated based on good clinical practice, without additional supporting diagnoses/activities"),
    NDC046("Activity/diagnosis inconsistent with clinician specialty"),
    NDC047("Incorrect DRG calculated"),
    NDC048("Service/supply may be appropriate, but too frequent"),
    NDC049("Inapropriate Drug Dose"),
    NDC050("High alert Drug to drug interaction / Drug combination is contra-indicated"),
    NDC051("Authorization quantity exceeds prescription quantity"),
    NDC052("Diagnosis is inconsistent with the patient's age"),
    NDC053("Diagnosis is inconsistent with the patient's gender"),
    NDC054("Service code is inconsistent with the patient's age"),
    NDC055("Service code is inconsistent with the patient's gender"),
    NDC056("Max duration exceeded"),
    NDC057("Max daily dose exceeded"),
    NDC058("Service / Medication is above SAR threshold"),
    NDC059("Encounter type inconsistent with service(s) / diagnosis"),
    NDC060("Diagnosis is inconsistent with the procedure"),
    NDC061("Diagnosis is inconsistent with the provider type"),
    NDC062("Service(s) is (are) performed outside authorization validity date"),
    NDC063("Missing Service Codes in a DRG claim"),
    NDC064("Services not available on direct biling"),
    NDC065("Prescription out of date."),
    NDC066("Drug not listed in formulary"),
    NDC067("Refill too soon"),
    NDC068("Claim information is inconsistent with pre-certified/authorized services"),
    NDC069("Claim is a duplicate based on service codes and dates"),
    NDC070("Payment already made for same/similar service within set time frame"),
    NDC071("Fraud"),
    NDC072("Time limit for submission has expired"),
    NDC073("Audit"),
    NDC074("Service(s) is (are) not performed (used after audit)"),
    NDC075("Claim overlaps inpatient stay. Resubmit only those services rendered outside the inpatient stay"),
    NDC076("Date of birth follows the date of service"),
    NDC077("Date of death precedes the date of service"),
    NDC078("Inpatient admission spans multiple rate periods.  Resubmit separate claims"),
    NDC079("Submission not compliant with contractual agreement between provider & payer"),
    NDC080("Authorization request overlaps or is within the period of another paid claim or approved authorization."),
    NDC081("Authorization request/Claim is a duplicate based on service codes and dates"),
    NDC082("Incorrect billing regime"),
    NDC083("Requested service code/charges inconsistent with clinician specialty"),
    NDC084("Use bundled code"),
    NDC085("Requested additional information was not received or was not received within time limit"),
    NDC086("Appeal procedures not followed or time limits not met");

    private String value;

    private AdjudicationReasonEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Enums.AdjudicationReasonEnum convert() {
        for (Enums.AdjudicationReasonEnum e : Enums.AdjudicationReasonEnum.values()) {
            if (e.getDescription() == this.getValue()) {
                return e;
            }
        }
        return null;
    }
}

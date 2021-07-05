package com.platformsandsolutions.hcpnphiesportal.domain.enumeration;

/**
 * The ResourceTypeEnum enumeration.
 */
public enum ResourceTypeEnum {
    Claim("Claim"),
    ClaimResponse("ClaimResponse"),
    Communication("Communication"),
    CommunicationRequest("CommunicationRequest"),
    CoverageEligibilityRequest("CoverageEligibilityRequest"),
    CoverageEligibilityResponse("CoverageEligibilityResponse"),
    OperationOutcome("OperationOutcome"),
    PaymentNotice("PaymentNotice"),
    PaymentReconciliation("PaymentReconciliation");

    private final String value;

    ResourceTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

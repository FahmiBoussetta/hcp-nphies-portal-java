package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReconciliationDetailItem.
 */
@Entity
@Table(name = "reconciliation_detail_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReconciliationDetailItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "predecessor")
    private String predecessor;

    @Column(name = "type")
    private String type;

    @Column(name = "date")
    private Instant date;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "errors",
            "relateds",
            "careTeams",
            "supportingInfos",
            "diagnoses",
            "insurances",
            "items",
            "encounter",
            "eligibilityResponse",
            "patient",
            "provider",
            "insurer",
            "prescription",
            "originalPrescription",
            "payee",
            "referral",
            "facility",
            "accident",
        },
        allowSetters = true
    )
    private Claim request;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contacts", "address" }, allowSetters = true)
    private Organization submitter;

    @ManyToOne
    @JsonIgnoreProperties(value = { "errors", "items", "totals" }, allowSetters = true)
    private ClaimResponse response;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contacts", "address" }, allowSetters = true)
    private Organization payee;

    @ManyToOne
    @JsonIgnoreProperties(value = { "details", "paymentIssuer", "paymentNotices" }, allowSetters = true)
    private PaymentReconciliation paymentReconciliation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReconciliationDetailItem id(Long id) {
        this.id = id;
        return this;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public ReconciliationDetailItem identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPredecessor() {
        return this.predecessor;
    }

    public ReconciliationDetailItem predecessor(String predecessor) {
        this.predecessor = predecessor;
        return this;
    }

    public void setPredecessor(String predecessor) {
        this.predecessor = predecessor;
    }

    public String getType() {
        return this.type;
    }

    public ReconciliationDetailItem type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getDate() {
        return this.date;
    }

    public ReconciliationDetailItem date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public ReconciliationDetailItem amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Claim getRequest() {
        return this.request;
    }

    public ReconciliationDetailItem request(Claim claim) {
        this.setRequest(claim);
        return this;
    }

    public void setRequest(Claim claim) {
        this.request = claim;
    }

    public Organization getSubmitter() {
        return this.submitter;
    }

    public ReconciliationDetailItem submitter(Organization organization) {
        this.setSubmitter(organization);
        return this;
    }

    public void setSubmitter(Organization organization) {
        this.submitter = organization;
    }

    public ClaimResponse getResponse() {
        return this.response;
    }

    public ReconciliationDetailItem response(ClaimResponse claimResponse) {
        this.setResponse(claimResponse);
        return this;
    }

    public void setResponse(ClaimResponse claimResponse) {
        this.response = claimResponse;
    }

    public Organization getPayee() {
        return this.payee;
    }

    public ReconciliationDetailItem payee(Organization organization) {
        this.setPayee(organization);
        return this;
    }

    public void setPayee(Organization organization) {
        this.payee = organization;
    }

    public PaymentReconciliation getPaymentReconciliation() {
        return this.paymentReconciliation;
    }

    public ReconciliationDetailItem paymentReconciliation(PaymentReconciliation paymentReconciliation) {
        this.setPaymentReconciliation(paymentReconciliation);
        return this;
    }

    public void setPaymentReconciliation(PaymentReconciliation paymentReconciliation) {
        this.paymentReconciliation = paymentReconciliation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReconciliationDetailItem)) {
            return false;
        }
        return id != null && id.equals(((ReconciliationDetailItem) o).id);
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReconciliationDetailItem{" + "id=" + getId() + ", identifier='" + getIdentifier() + "'"
                + ", predecessor='" + getPredecessor() + "'" + ", type='" + getType() + "'" + ", date='" + getDate()
                + "'" + ", amount=" + getAmount() + "}";
    }
}

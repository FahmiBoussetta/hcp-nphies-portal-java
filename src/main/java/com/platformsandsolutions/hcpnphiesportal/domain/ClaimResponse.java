package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.AdjudicationOutcomeEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.ClaimSubTypeEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.ClaimTypeEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.DrgEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.PayeeTypeEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.ReissueReasonEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.RemittanceOutcomeEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.Use;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.ClaimResponseModel;
import platform.fhir_client.models.IdentifierModel;

/**
 * A ClaimResponse.
 */
@Entity
@Table(name = "claim_response")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ClaimResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private String value;

    @Column(name = "jhi_system")
    private String system;

    @Column(name = "parsed")
    private String parsed;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "outcome", nullable = false)
    private RemittanceOutcomeEnum outcome;

    @Column(name = "batch_identifier")
    ReferenceIdentifier batchIdentifier;

    @Column(name = "batch_number")
    Integer batchNumber;

    @Column(name = "batch_start_date")
    Date batchStartDate;

    @Column(name = "batch_end_date")
    Date batchEndDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "drg")
    DrgEnum drg;

    @Enumerated(EnumType.STRING)
    @Column(name = "reissue_reason")
    ReissueReasonEnum reissueReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "adjudication_outcome")
    AdjudicationOutcomeEnum adjudicationOutcome;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_use", nullable = false)
    private Use use;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ClaimTypeEnum type;

    @Enumerated(EnumType.STRING)
    @Column(name = "sub_type")
    private ClaimSubTypeEnum subType;

    @Column(name = "disposition")
    private String disposition;

    @Column(name = "preauth_ref")
    private String preauthRef;

    @Column(name = "created")
    Date created;

    @Column(name = "preauth_period_start")
    Date preauthPeriodStart;

    @Column(name = "preauth_period_end")
    Date preauthPeriodEnd;

    @Enumerated(EnumType.STRING)
    @Column(name = "payee_type")
    PayeeTypeEnum PayeeType;

    @ManyToOne
    private Claim request;

    @ManyToOne
    @JsonIgnoreProperties(value = { "names", "contacts", "address" }, allowSetters = true)
    private Patient patient;

    @ManyToOne
    @JsonIgnoreProperties(value = { "names", "contacts", "address" }, allowSetters = true)
    private Organization insurer;

    @ManyToOne
    @JsonIgnoreProperties(value = { "names", "contacts", "address" }, allowSetters = true)
    private Organization requestor;

    @ManyToOne
    @JsonIgnoreProperties(value = { "names", "contacts", "address" }, allowSetters = true)
    private CommunicationRequest communicationRequest;

    @OneToMany(mappedBy = "claimResponse", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "claimResponse" }, allowSetters = true)
    private Set<CRErrorMessages> errors = new HashSet<>();

    @OneToMany(mappedBy = "claimResponse", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "notes", "adjudications", "details", "claimResponse" }, allowSetters = true)
    private Set<AdjudicationItem> items = new HashSet<>();

    @OneToMany(mappedBy = "claimResponse", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "claimResponse" }, allowSetters = true)
    private Set<Total> totals = new HashSet<>();

    @OneToMany(mappedBy = "claimResponse", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "claimResponse" }, allowSetters = true)
    private Set<Note> notes = new HashSet<>();

    @OneToMany(mappedBy = "claimResponse", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "claimResponse" }, allowSetters = true)
    private Set<Insurance> insurances;

    @ManyToMany(mappedBy = "crIdentifiers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ReferenceIdentifier> identifiers = new HashSet<>();

    public Set<ReferenceIdentifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Set<ReferenceIdentifier> identifiers) {
        this.identifiers = identifiers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClaimResponse id(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return this.value;
    }

    public ClaimResponse value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSystem() {
        return this.system;
    }

    public ClaimResponse system(String system) {
        this.system = system;
        return this;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getParsed() {
        return this.parsed;
    }

    public ClaimResponse parsed(String parsed) {
        this.parsed = parsed;
        return this;
    }

    public void setParsed(String parsed) {
        this.parsed = parsed;
    }

    public RemittanceOutcomeEnum getOutcome() {
        return this.outcome;
    }

    public ClaimResponse outcome(RemittanceOutcomeEnum outcome) {
        this.outcome = outcome;
        return this;
    }

    public void setOutcome(RemittanceOutcomeEnum outcome) {
        this.outcome = outcome;
    }

    public Set<CRErrorMessages> getErrors() {
        return this.errors;
    }

    public Use getUse() {
        return use;
    }

    public ReferenceIdentifier getBatchIdentifier() {
        return batchIdentifier;
    }

    public void setBatchIdentifier(ReferenceIdentifier batchIdentifier) {
        this.batchIdentifier = batchIdentifier;
    }

    public Integer getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(Integer batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Date getBatchStartDate() {
        return batchStartDate;
    }

    public void setBatchStartDate(Date batchStartDate) {
        this.batchStartDate = batchStartDate;
    }

    public Date getBatchEndDate() {
        return batchEndDate;
    }

    public void setBatchEndDate(Date batchEndDate) {
        this.batchEndDate = batchEndDate;
    }

    public DrgEnum getDrg() {
        return drg;
    }

    public void setDrg(DrgEnum drg) {
        this.drg = drg;
    }

    public ReissueReasonEnum getReissueReason() {
        return reissueReason;
    }

    public void setReissueReason(ReissueReasonEnum reissueReason) {
        this.reissueReason = reissueReason;
    }

    public AdjudicationOutcomeEnum getAdjudicationOutcome() {
        return adjudicationOutcome;
    }

    public void setAdjudicationOutcome(AdjudicationOutcomeEnum adjudicationOutcome) {
        this.adjudicationOutcome = adjudicationOutcome;
    }

    public void setUse(Use use) {
        this.use = use;
    }

    public ClaimTypeEnum getType() {
        return type;
    }

    public void setType(ClaimTypeEnum type) {
        this.type = type;
    }

    public ClaimSubTypeEnum getSubType() {
        return subType;
    }

    public void setSubType(ClaimSubTypeEnum subType) {
        this.subType = subType;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public String getPreauthRef() {
        return preauthRef;
    }

    public void setPreauthRef(String preauthRef) {
        this.preauthRef = preauthRef;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getPreauthPeriodStart() {
        return preauthPeriodStart;
    }

    public void setPreauthPeriodStart(Date preauthPeriodStart) {
        this.preauthPeriodStart = preauthPeriodStart;
    }

    public Date getPreauthPeriodEnd() {
        return preauthPeriodEnd;
    }

    public void setPreauthPeriodEnd(Date preauthPeriodEnd) {
        this.preauthPeriodEnd = preauthPeriodEnd;
    }

    public PayeeTypeEnum getPayeeType() {
        return PayeeType;
    }

    public void setPayeeType(PayeeTypeEnum payeeType) {
        PayeeType = payeeType;
    }

    public Claim getRequest() {
        return request;
    }

    public void setRequest(Claim request) {
        this.request = request;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Organization getInsurer() {
        return insurer;
    }

    public void setInsurer(Organization insurer) {
        this.insurer = insurer;
    }

    public Organization getRequestor() {
        return requestor;
    }

    public void setRequestor(Organization requestor) {
        this.requestor = requestor;
    }

    public CommunicationRequest getCommunicationRequest() {
        return communicationRequest;
    }

    public void setCommunicationRequest(CommunicationRequest communicationRequest) {
        this.communicationRequest = communicationRequest;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    public Set<Insurance> getInsurances() {
        return insurances;
    }

    public void setInsurances(Set<Insurance> insurances) {
        this.insurances = insurances;
    }

    public ClaimResponse errors(Set<CRErrorMessages> cRErrorMessages) {
        this.setErrors(cRErrorMessages);
        return this;
    }

    public ClaimResponse addErrors(CRErrorMessages cRErrorMessages) {
        this.errors.add(cRErrorMessages);
        cRErrorMessages.setClaimResponse(this);
        return this;
    }

    public ClaimResponse removeErrors(CRErrorMessages cRErrorMessages) {
        this.errors.remove(cRErrorMessages);
        cRErrorMessages.setClaimResponse(null);
        return this;
    }

    public void setErrors(Set<CRErrorMessages> cRErrorMessages) {
        if (this.errors != null) {
            this.errors.forEach(i -> i.setClaimResponse(null));
        }
        if (cRErrorMessages != null) {
            cRErrorMessages.forEach(i -> i.setClaimResponse(this));
        }
        this.errors = cRErrorMessages;
    }

    public Set<AdjudicationItem> getItems() {
        return this.items;
    }

    public ClaimResponse items(Set<AdjudicationItem> adjudicationItems) {
        this.setItems(adjudicationItems);
        return this;
    }

    public ClaimResponse addItems(AdjudicationItem adjudicationItem) {
        this.items.add(adjudicationItem);
        adjudicationItem.setClaimResponse(this);
        return this;
    }

    public ClaimResponse removeItems(AdjudicationItem adjudicationItem) {
        this.items.remove(adjudicationItem);
        adjudicationItem.setClaimResponse(null);
        return this;
    }

    public void setItems(Set<AdjudicationItem> adjudicationItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setClaimResponse(null));
        }
        if (adjudicationItems != null) {
            adjudicationItems.forEach(i -> i.setClaimResponse(this));
        }
        this.items = adjudicationItems;
    }

    public Set<Total> getTotals() {
        return this.totals;
    }

    public ClaimResponse totals(Set<Total> totals) {
        this.setTotals(totals);
        return this;
    }

    public ClaimResponse addTotal(Total total) {
        this.totals.add(total);
        total.setClaimResponse(this);
        return this;
    }

    public ClaimResponse removeTotal(Total total) {
        this.totals.remove(total);
        total.setClaimResponse(null);
        return this;
    }

    public void setTotals(Set<Total> totals) {
        if (this.totals != null) {
            this.totals.forEach(i -> i.setClaimResponse(null));
        }
        if (totals != null) {
            totals.forEach(i -> i.setClaimResponse(this));
        }
        this.totals = totals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClaimResponse)) {
            return false;
        }
        return id != null && id.equals(((ClaimResponse) o).id);
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
        return "ClaimResponse{" + "id=" + getId() + ", value='" + getValue() + "'" + ", system='" + getSystem() + "'"
                + ", parsed='" + getParsed() + "'" + ", outcome='" + getOutcome() + "'" + "}";
    }

    public static ClaimResponse convertFrom(ClaimResponseModel model) {
        ClaimResponse resp = new ClaimResponse();
        if (model.getIdentifiers() != null) {
            resp.setIdentifiers(model.getIdentifiers().stream().map(i -> ReferenceIdentifier.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getErrors() != null) {
            resp.setErrors(model.getErrors().stream().map(x -> CRErrorMessages.convertFrom(x)).collect(Collectors.toSet()));
        }
        if (model.getAdjudicationItems() != null) {
            resp.setItems(model.getAdjudicationItems().stream().map(x -> AdjudicationItem.convertFrom(x)).collect(Collectors.toSet()));
        }
        if (model.getTotal() != null) {
            resp.setTotals(model.getTotal().stream().map(x -> Total.convertFrom(x)).collect(Collectors.toSet()));
        }
        if (model.getOutcome() != null) {
            resp.setOutcome(RemittanceOutcomeEnum.valueOf(model.getOutcome().toString()));
        }
        if (model.getParsedRequest() != null) {
            resp.setParsed(model.getParsedRequest());
        }
        if (model.getIdentifiers() != null) {
            resp.setSystem(model.getIdentifiers().get(0).getSystem());
            resp.setValue(model.getIdentifiers().get(0).getValue());
        }
        if (model.getRequest() != null) {
            resp.setRequest(Claim.convertFrom(model.getRequest()));
        }
        return resp;
    }

    public ClaimResponseModel convertIdentifier() {
        ClaimResponseModel cr = new ClaimResponseModel();
        IdentifierModel i = new IdentifierModel();
        i.setValue(this.getValue());
        i.setSystem(this.getSystem());
        cr.addIdentifier(i);
        cr.setIsContained(false);
        return cr;
    }
}

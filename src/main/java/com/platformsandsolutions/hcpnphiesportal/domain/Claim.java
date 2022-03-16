package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.ClaimSubTypeEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.ClaimTypeEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.FundsReserveEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.PriorityEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.Use;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.ClaimModel;
import platform.fhir_client.models.CoreResourceModel;
import platform.fhir_client.models.IdentifierModel;

/**
 * A Claim.
 */
@Entity
@Table(name = "claim")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Claim implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid")
    private String guid;

    @Column(name = "is_queued")
    private Boolean isQueued;

    @Column(name = "parsed")
    private String parsed;

    @Column(name = "identifier")
    private String identifier;

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

    @Column(name = "eligibility_offline")
    private String eligibilityOffline;

    @Column(name = "eligibility_offline_date")
    private Instant eligibilityOfflineDate;

    @Column(name = "authorization_offline_date")
    private Instant authorizationOfflineDate;

    @Column(name = "billable_start")
    private Instant billableStart;

    @Column(name = "billable_end")
    private Instant billableEnd;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private PriorityEnum priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "funds_reserve")
    private FundsReserveEnum fundsReserve;

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "claim" }, allowSetters = true)
    private Set<ClaimErrorMessages> errors = new HashSet<>();

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "claimReference", "claim" }, allowSetters = true)
    private Set<Related> relateds = new HashSet<>();

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "provider", "providerRole", "claim" }, allowSetters = true)
    private Set<CareTeam> careTeams = new HashSet<>();

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "valueQuantity", "valueAttachment", "valueReference", "claim" }, allowSetters = true)
    private Set<SupportingInfo> supportingInfos = new HashSet<>();

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "claim" }, allowSetters = true)
    private Set<Diagnosis> diagnoses = new HashSet<>();

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "coverage", "claimResponse", "claim" }, allowSetters = true)
    private Set<Insurance> insurances = new HashSet<>();

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "careTeamSequences", "diagnosisSequences", "informationSequences", "udis", "details", "claim" },
        allowSetters = true
    )
    private Set<Item> items = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "subject", "hospitalization", "serviceProvider" }, allowSetters = true)
    private Encounter encounter;

    @ManyToOne
    @JsonIgnoreProperties(value = { "errors", "insurances", "patient", "insurer" }, allowSetters = true)
    private CoverageEligibilityResponse eligibilityResponse;

    @ManyToOne
    @JsonIgnoreProperties(value = { "names", "contacts", "address" }, allowSetters = true)
    private Patient patient;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contacts", "address" }, allowSetters = true)
    private Organization provider;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contacts", "address" }, allowSetters = true)
    private Organization insurer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "item", "detailItem", "subDetailItem" }, allowSetters = true)
    private ReferenceIdentifier prescription;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "item", "detailItem", "subDetailItem" }, allowSetters = true)
    private ReferenceIdentifier originalPrescription;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "partyPatient", "partyOrganization" }, allowSetters = true)
    private Payee payee;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "item", "detailItem", "subDetailItem" }, allowSetters = true)
    private ReferenceIdentifier referral;

    @ManyToOne
    @JsonIgnoreProperties(value = { "managingOrganization" }, allowSetters = true)
    private Location facility;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "location" }, allowSetters = true)
    private Accident accident;

    @ManyToMany(mappedBy = "claimIdentifiers")
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

    public Claim id(Long id) {
        this.id = id;
        return this;
    }

    public String getGuid() {
        return this.guid;
    }

    public Claim guid(String guid) {
        this.guid = guid;
        return this;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Boolean getIsQueued() {
        return this.isQueued;
    }

    public Claim isQueued(Boolean isQueued) {
        this.isQueued = isQueued;
        return this;
    }

    public void setIsQueued(Boolean isQueued) {
        this.isQueued = isQueued;
    }

    public String getParsed() {
        return this.parsed;
    }

    public Claim parsed(String parsed) {
        this.parsed = parsed;
        return this;
    }

    public void setParsed(String parsed) {
        this.parsed = parsed;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public Claim identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Use getUse() {
        return this.use;
    }

    public Claim use(Use use) {
        this.use = use;
        return this;
    }

    public void setUse(Use use) {
        this.use = use;
    }

    public ClaimTypeEnum getType() {
        return this.type;
    }

    public Claim type(ClaimTypeEnum type) {
        this.type = type;
        return this;
    }

    public void setType(ClaimTypeEnum type) {
        this.type = type;
    }

    public ClaimSubTypeEnum getSubType() {
        return this.subType;
    }

    public Claim subType(ClaimSubTypeEnum subType) {
        this.subType = subType;
        return this;
    }

    public void setSubType(ClaimSubTypeEnum subType) {
        this.subType = subType;
    }

    public String getEligibilityOffline() {
        return this.eligibilityOffline;
    }

    public Claim eligibilityOffline(String eligibilityOffline) {
        this.eligibilityOffline = eligibilityOffline;
        return this;
    }

    public void setEligibilityOffline(String eligibilityOffline) {
        this.eligibilityOffline = eligibilityOffline;
    }

    public Instant getEligibilityOfflineDate() {
        return this.eligibilityOfflineDate;
    }

    public Claim eligibilityOfflineDate(Instant eligibilityOfflineDate) {
        this.eligibilityOfflineDate = eligibilityOfflineDate;
        return this;
    }

    public void setEligibilityOfflineDate(Instant eligibilityOfflineDate) {
        this.eligibilityOfflineDate = eligibilityOfflineDate;
    }

    public Instant getAuthorizationOfflineDate() {
        return this.authorizationOfflineDate;
    }

    public Claim authorizationOfflineDate(Instant authorizationOfflineDate) {
        this.authorizationOfflineDate = authorizationOfflineDate;
        return this;
    }

    public void setAuthorizationOfflineDate(Instant authorizationOfflineDate) {
        this.authorizationOfflineDate = authorizationOfflineDate;
    }

    public Instant getBillableStart() {
        return this.billableStart;
    }

    public Claim billableStart(Instant billableStart) {
        this.billableStart = billableStart;
        return this;
    }

    public void setBillableStart(Instant billableStart) {
        this.billableStart = billableStart;
    }

    public Instant getBillableEnd() {
        return this.billableEnd;
    }

    public Claim billableEnd(Instant billableEnd) {
        this.billableEnd = billableEnd;
        return this;
    }

    public void setBillableEnd(Instant billableEnd) {
        this.billableEnd = billableEnd;
    }

    public PriorityEnum getPriority() {
        return this.priority;
    }

    public Claim priority(PriorityEnum priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(PriorityEnum priority) {
        this.priority = priority;
    }

    public FundsReserveEnum getFundsReserve() {
        return this.fundsReserve;
    }

    public Claim fundsReserve(FundsReserveEnum fundsReserve) {
        this.fundsReserve = fundsReserve;
        return this;
    }

    public void setFundsReserve(FundsReserveEnum fundsReserve) {
        this.fundsReserve = fundsReserve;
    }

    public Set<ClaimErrorMessages> getErrors() {
        return this.errors;
    }

    public Claim errors(Set<ClaimErrorMessages> claimErrorMessages) {
        this.setErrors(claimErrorMessages);
        return this;
    }

    public Claim addErrors(ClaimErrorMessages claimErrorMessages) {
        this.errors.add(claimErrorMessages);
        claimErrorMessages.setClaim(this);
        return this;
    }

    public Claim removeErrors(ClaimErrorMessages claimErrorMessages) {
        this.errors.remove(claimErrorMessages);
        claimErrorMessages.setClaim(null);
        return this;
    }

    public void setErrors(Set<ClaimErrorMessages> claimErrorMessages) {
        if (this.errors != null) {
            this.errors.forEach(i -> i.setClaim(null));
        }
        if (claimErrorMessages != null) {
            claimErrorMessages.forEach(i -> i.setClaim(this));
        }
        this.errors = claimErrorMessages;
    }

    public Set<Related> getRelateds() {
        return this.relateds;
    }

    public Claim relateds(Set<Related> relateds) {
        this.setRelateds(relateds);
        return this;
    }

    public Claim addRelateds(Related related) {
        this.relateds.add(related);
        related.setClaim(this);
        return this;
    }

    public Claim removeRelateds(Related related) {
        this.relateds.remove(related);
        related.setClaim(null);
        return this;
    }

    public void setRelateds(Set<Related> relateds) {
        if (this.relateds != null) {
            this.relateds.forEach(i -> i.setClaim(null));
        }
        if (relateds != null) {
            relateds.forEach(i -> i.setClaim(this));
        }
        this.relateds = relateds;
    }

    public Set<CareTeam> getCareTeams() {
        return this.careTeams;
    }

    public Claim careTeams(Set<CareTeam> careTeams) {
        this.setCareTeams(careTeams);
        return this;
    }

    public Claim addCareTeam(CareTeam careTeam) {
        this.careTeams.add(careTeam);
        careTeam.setClaim(this);
        return this;
    }

    public Claim removeCareTeam(CareTeam careTeam) {
        this.careTeams.remove(careTeam);
        careTeam.setClaim(null);
        return this;
    }

    public void setCareTeams(Set<CareTeam> careTeams) {
        if (this.careTeams != null) {
            this.careTeams.forEach(i -> i.setClaim(null));
        }
        if (careTeams != null) {
            careTeams.forEach(i -> i.setClaim(this));
        }
        this.careTeams = careTeams;
    }

    public Set<SupportingInfo> getSupportingInfos() {
        return this.supportingInfos;
    }

    public Claim supportingInfos(Set<SupportingInfo> supportingInfos) {
        this.setSupportingInfos(supportingInfos);
        return this;
    }

    public Claim addSupportingInfos(SupportingInfo supportingInfo) {
        this.supportingInfos.add(supportingInfo);
        supportingInfo.setClaim(this);
        return this;
    }

    public Claim removeSupportingInfos(SupportingInfo supportingInfo) {
        this.supportingInfos.remove(supportingInfo);
        supportingInfo.setClaim(null);
        return this;
    }

    public void setSupportingInfos(Set<SupportingInfo> supportingInfos) {
        if (this.supportingInfos != null) {
            this.supportingInfos.forEach(i -> i.setClaim(null));
        }
        if (supportingInfos != null) {
            supportingInfos.forEach(i -> i.setClaim(this));
        }
        this.supportingInfos = supportingInfos;
    }

    public Set<Diagnosis> getDiagnoses() {
        return this.diagnoses;
    }

    public Claim diagnoses(Set<Diagnosis> diagnoses) {
        this.setDiagnoses(diagnoses);
        return this;
    }

    public Claim addDiagnoses(Diagnosis diagnosis) {
        this.diagnoses.add(diagnosis);
        diagnosis.setClaim(this);
        return this;
    }

    public Claim removeDiagnoses(Diagnosis diagnosis) {
        this.diagnoses.remove(diagnosis);
        diagnosis.setClaim(null);
        return this;
    }

    public void setDiagnoses(Set<Diagnosis> diagnoses) {
        if (this.diagnoses != null) {
            this.diagnoses.forEach(i -> i.setClaim(null));
        }
        if (diagnoses != null) {
            diagnoses.forEach(i -> i.setClaim(this));
        }
        this.diagnoses = diagnoses;
    }

    public Set<Insurance> getInsurances() {
        return this.insurances;
    }

    public Claim insurances(Set<Insurance> insurances) {
        this.setInsurances(insurances);
        return this;
    }

    public Claim addInsurances(Insurance insurance) {
        this.insurances.add(insurance);
        insurance.setClaim(this);
        return this;
    }

    public Claim removeInsurances(Insurance insurance) {
        this.insurances.remove(insurance);
        insurance.setClaim(null);
        return this;
    }

    public void setInsurances(Set<Insurance> insurances) {
        if (this.insurances != null) {
            this.insurances.forEach(i -> i.setClaim(null));
        }
        if (insurances != null) {
            insurances.forEach(i -> i.setClaim(this));
        }
        this.insurances = insurances;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public Claim items(Set<Item> items) {
        this.setItems(items);
        return this;
    }

    public Claim addItems(Item item) {
        this.items.add(item);
        item.setClaim(this);
        return this;
    }

    public Claim removeItems(Item item) {
        this.items.remove(item);
        item.setClaim(null);
        return this;
    }

    public void setItems(Set<Item> items) {
        if (this.items != null) {
            this.items.forEach(i -> i.setClaim(null));
        }
        if (items != null) {
            items.forEach(i -> i.setClaim(this));
        }
        this.items = items;
    }

    public Encounter getEncounter() {
        return this.encounter;
    }

    public Claim encounter(Encounter encounter) {
        this.setEncounter(encounter);
        return this;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }

    public CoverageEligibilityResponse getEligibilityResponse() {
        return this.eligibilityResponse;
    }

    public Claim eligibilityResponse(CoverageEligibilityResponse coverageEligibilityResponse) {
        this.setEligibilityResponse(coverageEligibilityResponse);
        return this;
    }

    public void setEligibilityResponse(CoverageEligibilityResponse coverageEligibilityResponse) {
        this.eligibilityResponse = coverageEligibilityResponse;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public Claim patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Organization getProvider() {
        return this.provider;
    }

    public Claim provider(Organization organization) {
        this.setProvider(organization);
        return this;
    }

    public void setProvider(Organization organization) {
        this.provider = organization;
    }

    public Organization getInsurer() {
        return this.insurer;
    }

    public Claim insurer(Organization organization) {
        this.setInsurer(organization);
        return this;
    }

    public void setInsurer(Organization organization) {
        this.insurer = organization;
    }

    public ReferenceIdentifier getPrescription() {
        return this.prescription;
    }

    public Claim prescription(ReferenceIdentifier referenceIdentifier) {
        this.setPrescription(referenceIdentifier);
        return this;
    }

    public void setPrescription(ReferenceIdentifier referenceIdentifier) {
        this.prescription = referenceIdentifier;
    }

    public ReferenceIdentifier getOriginalPrescription() {
        return this.originalPrescription;
    }

    public Claim originalPrescription(ReferenceIdentifier referenceIdentifier) {
        this.setOriginalPrescription(referenceIdentifier);
        return this;
    }

    public void setOriginalPrescription(ReferenceIdentifier referenceIdentifier) {
        this.originalPrescription = referenceIdentifier;
    }

    public Payee getPayee() {
        return this.payee;
    }

    public Claim payee(Payee payee) {
        this.setPayee(payee);
        return this;
    }

    public void setPayee(Payee payee) {
        this.payee = payee;
    }

    public ReferenceIdentifier getReferral() {
        return this.referral;
    }

    public Claim referral(ReferenceIdentifier referenceIdentifier) {
        this.setReferral(referenceIdentifier);
        return this;
    }

    public void setReferral(ReferenceIdentifier referenceIdentifier) {
        this.referral = referenceIdentifier;
    }

    public Location getFacility() {
        return this.facility;
    }

    public Claim facility(Location location) {
        this.setFacility(location);
        return this;
    }

    public void setFacility(Location location) {
        this.facility = location;
    }

    public Accident getAccident() {
        return this.accident;
    }

    public Claim accident(Accident accident) {
        this.setAccident(accident);
        return this;
    }

    public void setAccident(Accident accident) {
        this.accident = accident;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Claim)) {
            return false;
        }
        return id != null && id.equals(((Claim) o).id);
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
        return "Claim{" + "id=" + getId() + ", guid='" + getGuid() + "'" + ", isQueued='" + getIsQueued() + "'"
                + ", parsed='" + getParsed() + "'" + ", identifier='" + getIdentifier() + "'" + ", use='" + getUse()
                + "'" + ", type='" + getType() + "'" + ", subType='" + getSubType() + "'" + ", eligibilityOffline='"
                + getEligibilityOffline() + "'" + ", eligibilityOfflineDate='" + getEligibilityOfflineDate() + "'"
                + ", authorizationOfflineDate='" + getAuthorizationOfflineDate() + "'" + ", billableStart='"
                + getBillableStart() + "'" + ", billableEnd='" + getBillableEnd() + "'" + ", priority='" + getPriority()
                + "'" + ", fundsReserve='" + getFundsReserve() + "'" + "}";
    }

    public ClaimModel convert(ArrayList<CoreResourceModel> coreResources) {
        ClaimModel model = new ClaimModel();
        if (this.getAccident() != null) {
            model.setAccident(this.getAccident().convert(coreResources));
        }
        if (this.getAuthorizationOfflineDate() != null) {
            model.setAuthorizationOfflineDate(Date.from(this.getAuthorizationOfflineDate()));
        }
        if (this.getBillableEnd() != null) {
            model.setBillableEnd(Date.from(this.getBillableEnd()));
        }
        if (this.getBillableStart() != null) {
            model.setBillableStart(Date.from(this.getBillableStart()));
        }
        if (this.getCareTeams() != null) {
            model.setCareTeam(this.getCareTeams().stream().map(i -> i.convert(coreResources)).collect(Collectors.toList()));
        }
        if (this.getDiagnoses() != null) {
            model.setDiagnoses(this.getDiagnoses().stream().map(i -> i.convert(coreResources)).collect(Collectors.toList()));
        }
        if (this.getEligibilityOffline() != null) {
            model.setEligibilityOffline(this.getEligibilityOffline());
        }
        if (this.getEligibilityOfflineDate() != null) {
            model.setEligibilityOfflineDate(Date.from(this.getEligibilityOfflineDate()));
        }
        if (this.getEligibilityResponse() != null) {
            model.setEligibilityResponse(this.getEligibilityResponse().convertIdentifier());
        }
        if (this.getEncounter() != null) {
            model.setEncounter(this.getEncounter().convert(coreResources));
        }
        if (this.getFacility() != null) {
            model.setFacility(this.getFacility().convert(coreResources));
        }
        if (this.getFundsReserve() != null) {
            model.setFundsReserve(this.getFundsReserve().convert());
        }
        if (this.getGuid() != null) {
            model.setId(UUID.fromString(this.getGuid()));
        }
        if (this.getIdentifier() != null) {
            IdentifierModel identifier = new IdentifierModel();
            identifier.setValue(
                this.getIdentifier() + "_" + this.getId() + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
            );
            model.addIdentifier(identifier);
        }
        if (this.getInsurances() != null) {
            model.setInsurances(this.getInsurances().stream().map(i -> i.convert(coreResources)).collect(Collectors.toList()));
        }
        if (this.getInsurer() != null) {
            model.setInsurer(this.getInsurer().convert(coreResources));
        }
        if (this.getItems() != null) {
            model.setItems(this.getItems().stream().map(i -> i.convert(coreResources)).collect(Collectors.toList()));
        }
        if (this.getOriginalPrescription() != null) {
            model.setOriginalPrescription(this.getOriginalPrescription().convert());
        }
        if (this.getPatient() != null) {
            model.setPatient(this.getPatient().convert(coreResources));
        }
        if (this.getPayee() != null) {
            model.setPayee(this.getPayee().convert(coreResources));
        }
        if (this.getPrescription() != null) {
            model.setPrescription(this.getPrescription().convert());
        }
        if (this.getPriority() != null) {
            model.setPriority(this.getPriority().convert());
        }
        if (this.getReferral() != null) {
            model.setReferral(this.getReferral().convert());
        }
        if (this.getRelateds() != null) {
            model.setRelateds(
                this.getRelateds().stream().map(i -> i.convert(coreResources)).collect(Collectors.toCollection(ArrayList::new))
            );
        }
        if (this.getSubType() != null) {
            model.setSubType(this.getSubType().convert());
        }
        if (this.getSupportingInfos() != null) {
            model.setSupportingInfos(this.getSupportingInfos().stream().map(i -> i.convert(coreResources)).collect(Collectors.toList()));
        }
        if (this.getType() != null) {
            model.setType(this.getType().convert());
        }
        if (this.getUse() != null) {
            model.setUse(this.getUse().convert());
        }

        return model;
    }

    public static Claim convertFrom(ClaimModel model) {
        Claim c = new Claim();
        if (model.getIdentifiers() != null) {
            c.setIdentifiers(model.getIdentifiers().stream().map(i -> ReferenceIdentifier.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getAccident() != null) {
            c.setAccident(Accident.convertFrom(model.getAccident()));
        }
        if (model.getAuthorizationOfflineDate() != null) {
            c.setAuthorizationOfflineDate(model.getAuthorizationOfflineDate().toInstant());
        }
        if (model.getBillableEnd() != null) {
            c.setBillableEnd(model.getBillableEnd().toInstant());
        }
        if (model.getBillableStart() != null) {
            c.setBillableStart(model.getBillableStart().toInstant());
        }
        if (model.getCareTeam() != null) {
            c.setCareTeams(model.getCareTeam().stream().map(i -> CareTeam.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getDiagnoses() != null) {
            c.setDiagnoses(model.getDiagnoses().stream().map(i -> Diagnosis.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getEligibilityOffline() != null) {
            c.setEligibilityOffline(model.getEligibilityOffline());
        }
        if (model.getEligibilityOfflineDate() != null) {
            c.setEligibilityOfflineDate(model.getEligibilityOfflineDate().toInstant());
        }
        if (model.getEligibilityResponse() != null) {
            c.setEligibilityResponse(CoverageEligibilityResponse.convertFrom(model.getEligibilityResponse()));
        }
        if (model.getEncounter() != null) {
            c.setEncounter(Encounter.convertFrom(model.getEncounter()));
        }
        if (model.getFacility() != null) {
            c.setFacility(Location.convertFrom(model.getFacility()));
        }
        if (model.getFundsReserve() != null) {
            c.setFundsReserve(FundsReserveEnum.valueOf(model.getFundsReserve().name()));
        }
        if (model.getId() != null) {
            c.setGuid(model.getId().toString());
        }
        if (model.getIdentifiers() != null) {
            c.setIdentifier(model.getIdentifiers().get(0).getValue());
        }
        if (model.getInsurances() != null) {
            c.setInsurances(model.getInsurances().stream().map(i -> Insurance.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getInsurer() != null) {
            c.setInsurer(Organization.convertFrom(model.getInsurer()));
        }
        if (model.getItems() != null) {
            c.setItems(model.getItems().stream().map(i -> Item.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getOriginalPrescription() != null) {
            c.setOriginalPrescription(ReferenceIdentifier.convertFrom(model.getOriginalPrescription()));
        }
        if (model.getPatient() != null) {
            c.setPatient(Patient.convertFrom(model.getPatient()));
        }
        if (model.getPayee() != null) {
            c.setPayee(Payee.convertFrom(model.getPayee()));
        }
        if (model.getPrescription() != null) {
            c.setPrescription(ReferenceIdentifier.convertFrom(model.getPrescription()));
        }
        if (model.getPriority() != null) {
            c.setPriority(PriorityEnum.valueOf(model.getPriority().name()));
        }
        if (model.getReferral() != null) {
            c.setReferral(ReferenceIdentifier.convertFrom(model.getReferral()));
        }
        if (model.getRelateds() != null) {
            c.setRelateds(model.getRelateds().stream().map(i -> Related.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getSubType() != null) {
            c.setSubType(ClaimSubTypeEnum.valueOf(model.getSubType().name()));
        }
        if (model.getSupportingInfos() != null) {
            c.setSupportingInfos(model.getSupportingInfos().stream().map(i -> SupportingInfo.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getType() != null) {
            c.setType(ClaimTypeEnum.valueOf(model.getType().name()));
        }
        if (model.getUse() != null) {
            c.setUse(Use.valueOf(model.getUse().name()));
        }

        return c;
    }
}

package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.IdentifierModel;
import platform.fhir_client.models.ReferenceModel;

/**
 * A ReferenceIdentifier.
 */
@Entity
@Table(name = "reference_identifier")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReferenceIdentifier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "reference")
    private String reference;

    @Column(name = "system")
    private String system;

    @Column(name = "value")
    private String value;

    @Column(name = "display")
    private String display;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(
        value = { "careTeamSequences", "diagnosisSequences", "informationSequences", "udis", "details", "claim" },
        allowSetters = true
    )
    private Item item;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "udis", "subDetails", "item" }, allowSetters = true)
    private DetailItem detailItem;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "udis", "detailItem" }, allowSetters = true)
    private SubDetailItem subDetailItem;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_claim__identifiers",
        joinColumns = @JoinColumn(name = "claim_id"),
        inverseJoinColumns = @JoinColumn(name = "reference_identifier_id")
    )
    private Set<Claim> claimIdentifiers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_cr__identifiers",
        joinColumns = @JoinColumn(name = "claim_response_id"),
        inverseJoinColumns = @JoinColumn(name = "reference_identifier_id")
    )
    private Set<ClaimResponse> crIdentifiers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_communication__identifiers",
        joinColumns = @JoinColumn(name = "communication_id"),
        inverseJoinColumns = @JoinColumn(name = "reference_identifier_id")
    )
    private Set<Communication> communicationIdentifiers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_com_req__identifiers",
        joinColumns = @JoinColumn(name = "communication_request_id"),
        inverseJoinColumns = @JoinColumn(name = "reference_identifier_id")
    )
    private Set<CommunicationRequest> comReqIdentifiers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_coverage__identifiers",
        joinColumns = @JoinColumn(name = "coverage_id"),
        inverseJoinColumns = @JoinColumn(name = "reference_identifier_id")
    )
    private Set<Coverage> coverageIdentifiers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_cov_eli_req__identifiers",
        joinColumns = @JoinColumn(name = "coverage_eligibility_request_id"),
        inverseJoinColumns = @JoinColumn(name = "reference_identifier_id")
    )
    private Set<CoverageEligibilityRequest> covEliReqIdentifiers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_cov_eli_resp__identifiers",
        joinColumns = @JoinColumn(name = "coverage_eligibility_response_id"),
        inverseJoinColumns = @JoinColumn(name = "reference_identifier_id")
    )
    private Set<CoverageEligibilityResponse> covEliRespIdentifiers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_encounter__identifiers",
        joinColumns = @JoinColumn(name = "encounter_id"),
        inverseJoinColumns = @JoinColumn(name = "reference_identifier_id")
    )
    private Set<Encounter> encounterIdentifiers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_location__identifiers",
        joinColumns = @JoinColumn(name = "location_id"),
        inverseJoinColumns = @JoinColumn(name = "reference_identifier_id")
    )
    private Set<Location> locationIdentifiers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_organization__identifiers",
        joinColumns = @JoinColumn(name = "organization_id"),
        inverseJoinColumns = @JoinColumn(name = "reference_identifier_id")
    )
    private Set<Organization> organizationIdentifiers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_patient__identifiers",
        joinColumns = @JoinColumn(name = "patient_id"),
        inverseJoinColumns = @JoinColumn(name = "reference_identifier_id")
    )
    private Set<Patient> patientIdentifiers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_pay_not__identifiers",
        joinColumns = @JoinColumn(name = "payment_notice_id"),
        inverseJoinColumns = @JoinColumn(name = "reference_identifier_id")
    )
    private Set<PaymentNotice> payNotIdentifiers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_pay_rec__identifiers",
        joinColumns = @JoinColumn(name = "payment_reconciliation_id"),
        inverseJoinColumns = @JoinColumn(name = "reference_identifier_id")
    )
    private Set<PaymentReconciliation> payRecIdentifiers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_practitioner__identifiers",
        joinColumns = @JoinColumn(name = "practitioner_id"),
        inverseJoinColumns = @JoinColumn(name = "reference_identifier_id")
    )
    private Set<Practitioner> practitionerIdentifiers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_practitioner_role__identifiers",
        joinColumns = @JoinColumn(name = "practitioner_role_id"),
        inverseJoinColumns = @JoinColumn(name = "reference_identifier_id")
    )
    private Set<PractitionerRole> practitionerRoleIdentifiers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_task__identifiers",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "reference_identifier_id")
    )
    private Set<Task> taskIdentifiers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_task_resp__identifiers",
        joinColumns = @JoinColumn(name = "task_response_id"),
        inverseJoinColumns = @JoinColumn(name = "reference_identifier_id")
    )
    private Set<TaskResponse> taskRespIdentifiers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReferenceIdentifier id(Long id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return type;
    }

    public ReferenceIdentifier type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReference() {
        return reference;
    }

    public ReferenceIdentifier reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSystem() {
        return system;
    }

    public ReferenceIdentifier system(String system) {
        this.system = system;
        return this;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getValue() {
        return value;
    }

    public ReferenceIdentifier value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDisplay() {
        return this.display;
    }

    public ReferenceIdentifier display(String display) {
        this.display = display;
        return this;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public Item getItem() {
        return this.item;
    }

    public ReferenceIdentifier item(Item item) {
        this.setItem(item);
        return this;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public DetailItem getDetailItem() {
        return this.detailItem;
    }

    public ReferenceIdentifier detailItem(DetailItem detailItem) {
        this.setDetailItem(detailItem);
        return this;
    }

    public void setDetailItem(DetailItem detailItem) {
        this.detailItem = detailItem;
    }

    public SubDetailItem getSubDetailItem() {
        return this.subDetailItem;
    }

    public ReferenceIdentifier subDetailItem(SubDetailItem subDetailItem) {
        this.setSubDetailItem(subDetailItem);
        return this;
    }

    public void setSubDetailItem(SubDetailItem subDetailItem) {
        this.subDetailItem = subDetailItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReferenceIdentifier)) {
            return false;
        }
        return id != null && id.equals(((ReferenceIdentifier) o).id);
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
        return "ReferenceIdentifier{" + "id=" + getId() + ", reference='" + getReference() + "'" + ", display='"
                + getDisplay() + "'" + "}";
    }

    public ReferenceModel convert() {
        ReferenceModel r = new ReferenceModel();
        r.setDisplay(this.getDisplay());
        r.setReference(this.getReference());
        r.setSystem(this.getSystem());
        r.setValue(this.getValue());
        r.setType(this.getType());
        return r;
    }

    public static ReferenceIdentifier convertFrom(ReferenceModel model) {
        ReferenceIdentifier r = new ReferenceIdentifier();
        r.setDisplay(model.getDisplay());
        r.setReference(model.getReference());
        r.setSystem(model.getSystem());
        r.setValue(model.getValue());
        r.setType(model.getType());
        return r;
    }

    public static ReferenceIdentifier convertFrom(IdentifierModel model) {
        ReferenceIdentifier r = new ReferenceIdentifier();
        r.setSystem(model.getSystem());
        r.setValue(model.getValue());
        r.setType(model.getType());
        return r;
    }
}

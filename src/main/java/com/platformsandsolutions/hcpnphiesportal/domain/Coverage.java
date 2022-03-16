package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.CoverageTypeEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.RelationShipEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.CoreResourceModel;
import platform.fhir_client.models.CoverageModel;
import platform.fhir_client.models.IdentifierModel;

/**
 * A Coverage.
 */
@Entity
@Table(name = "coverage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Coverage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid")
    private String guid;

    @Column(name = "force_id")
    private String forceId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "coverage_type", nullable = false)
    private CoverageTypeEnum coverageType;

    @Column(name = "subscriber_id")
    private String subscriberId;

    @Column(name = "dependent")
    private String dependent;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "relation_ship", nullable = false)
    private RelationShipEnum relationShip;

    @Column(name = "network")
    private String network;

    @Column(name = "subrogation")
    private Boolean subrogation;

    @OneToMany(mappedBy = "coverage", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "coverage" }, allowSetters = true)
    private Set<ClassComponent> classComponents = new HashSet<>();

    @OneToMany(mappedBy = "coverage", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "exceptions", "coverage" }, allowSetters = true)
    private Set<CostToBeneficiaryComponent> costToBeneficiaryComponents = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "names", "contacts", "address" }, allowSetters = true)
    private Patient subscriberPatient;

    @ManyToOne
    @JsonIgnoreProperties(value = { "names", "contacts", "address" }, allowSetters = true)
    private Patient beneficiary;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contacts", "address" }, allowSetters = true)
    private Organization payor;

    @ManyToMany(mappedBy = "coverages")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "errors", "purposes", "patient", "provider", "insurer", "facility", "coverages" }, allowSetters = true)
    private Set<CoverageEligibilityRequest> coverageEligibilityRequests = new HashSet<>();

    @ManyToMany(mappedBy = "coverageIdentifiers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ReferenceIdentifier> identifiers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Coverage id(Long id) {
        this.id = id;
        return this;
    }

    public Set<ReferenceIdentifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Set<ReferenceIdentifier> identifiers) {
        this.identifiers = identifiers;
    }

    public String getGuid() {
        return this.guid;
    }

    public Coverage guid(String guid) {
        this.guid = guid;
        return this;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getForceId() {
        return this.forceId;
    }

    public Coverage forceId(String forceId) {
        this.forceId = forceId;
        return this;
    }

    public void setForceId(String forceId) {
        this.forceId = forceId;
    }

    public CoverageTypeEnum getCoverageType() {
        return this.coverageType;
    }

    public Coverage coverageType(CoverageTypeEnum coverageType) {
        this.coverageType = coverageType;
        return this;
    }

    public void setCoverageType(CoverageTypeEnum coverageType) {
        this.coverageType = coverageType;
    }

    public String getSubscriberId() {
        return this.subscriberId;
    }

    public Coverage subscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
        return this;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getDependent() {
        return this.dependent;
    }

    public Coverage dependent(String dependent) {
        this.dependent = dependent;
        return this;
    }

    public void setDependent(String dependent) {
        this.dependent = dependent;
    }

    public RelationShipEnum getRelationShip() {
        return this.relationShip;
    }

    public Coverage relationShip(RelationShipEnum relationShip) {
        this.relationShip = relationShip;
        return this;
    }

    public void setRelationShip(RelationShipEnum relationShip) {
        this.relationShip = relationShip;
    }

    public String getNetwork() {
        return this.network;
    }

    public Coverage network(String network) {
        this.network = network;
        return this;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public Boolean getSubrogation() {
        return this.subrogation;
    }

    public Coverage subrogation(Boolean subrogation) {
        this.subrogation = subrogation;
        return this;
    }

    public void setSubrogation(Boolean subrogation) {
        this.subrogation = subrogation;
    }

    public Set<ClassComponent> getClassComponents() {
        return this.classComponents;
    }

    public Coverage classComponents(Set<ClassComponent> classComponents) {
        this.setClassComponents(classComponents);
        return this;
    }

    public Coverage addClassComponents(ClassComponent classComponent) {
        this.classComponents.add(classComponent);
        classComponent.setCoverage(this);
        return this;
    }

    public Coverage removeClassComponents(ClassComponent classComponent) {
        this.classComponents.remove(classComponent);
        classComponent.setCoverage(null);
        return this;
    }

    public void setClassComponents(Set<ClassComponent> classComponents) {
        if (this.classComponents != null) {
            this.classComponents.forEach(i -> i.setCoverage(null));
        }
        if (classComponents != null) {
            classComponents.forEach(i -> i.setCoverage(this));
        }
        this.classComponents = classComponents;
    }

    public Set<CostToBeneficiaryComponent> getCostToBeneficiaryComponents() {
        return this.costToBeneficiaryComponents;
    }

    public Coverage costToBeneficiaryComponents(Set<CostToBeneficiaryComponent> costToBeneficiaryComponents) {
        this.setCostToBeneficiaryComponents(costToBeneficiaryComponents);
        return this;
    }

    public Coverage addCostToBeneficiaryComponents(CostToBeneficiaryComponent costToBeneficiaryComponent) {
        this.costToBeneficiaryComponents.add(costToBeneficiaryComponent);
        costToBeneficiaryComponent.setCoverage(this);
        return this;
    }

    public Coverage removeCostToBeneficiaryComponents(CostToBeneficiaryComponent costToBeneficiaryComponent) {
        this.costToBeneficiaryComponents.remove(costToBeneficiaryComponent);
        costToBeneficiaryComponent.setCoverage(null);
        return this;
    }

    public void setCostToBeneficiaryComponents(Set<CostToBeneficiaryComponent> costToBeneficiaryComponents) {
        if (this.costToBeneficiaryComponents != null) {
            this.costToBeneficiaryComponents.forEach(i -> i.setCoverage(null));
        }
        if (costToBeneficiaryComponents != null) {
            costToBeneficiaryComponents.forEach(i -> i.setCoverage(this));
        }
        this.costToBeneficiaryComponents = costToBeneficiaryComponents;
    }

    public Patient getSubscriberPatient() {
        return this.subscriberPatient;
    }

    public Coverage subscriberPatient(Patient patient) {
        this.setSubscriberPatient(patient);
        return this;
    }

    public void setSubscriberPatient(Patient patient) {
        this.subscriberPatient = patient;
    }

    public Patient getBeneficiary() {
        return this.beneficiary;
    }

    public Coverage beneficiary(Patient patient) {
        this.setBeneficiary(patient);
        return this;
    }

    public void setBeneficiary(Patient patient) {
        this.beneficiary = patient;
    }

    public Organization getPayor() {
        return this.payor;
    }

    public Coverage payor(Organization organization) {
        this.setPayor(organization);
        return this;
    }

    public void setPayor(Organization organization) {
        this.payor = organization;
    }

    public Set<CoverageEligibilityRequest> getCoverageEligibilityRequests() {
        return this.coverageEligibilityRequests;
    }

    public Coverage coverageEligibilityRequests(Set<CoverageEligibilityRequest> coverageEligibilityRequests) {
        this.setCoverageEligibilityRequests(coverageEligibilityRequests);
        return this;
    }

    public Coverage addCoverageEligibilityRequests(CoverageEligibilityRequest coverageEligibilityRequest) {
        this.coverageEligibilityRequests.add(coverageEligibilityRequest);
        coverageEligibilityRequest.getCoverages().add(this);
        return this;
    }

    public Coverage removeCoverageEligibilityRequests(CoverageEligibilityRequest coverageEligibilityRequest) {
        this.coverageEligibilityRequests.remove(coverageEligibilityRequest);
        coverageEligibilityRequest.getCoverages().remove(this);
        return this;
    }

    public void setCoverageEligibilityRequests(Set<CoverageEligibilityRequest> coverageEligibilityRequests) {
        if (this.coverageEligibilityRequests != null) {
            this.coverageEligibilityRequests.forEach(i -> i.removeCoverages(this));
        }
        if (coverageEligibilityRequests != null) {
            coverageEligibilityRequests.forEach(i -> i.addCoverages(this));
        }
        this.coverageEligibilityRequests = coverageEligibilityRequests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coverage)) {
            return false;
        }
        return id != null && id.equals(((Coverage) o).id);
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
        return "Coverage{" + "id=" + getId() + ", guid='" + getGuid() + "'" + ", forceId='" + getForceId() + "'"
                + ", coverageType='" + getCoverageType() + "'" + ", subscriberId='" + getSubscriberId() + "'"
                + ", dependent='" + getDependent() + "'" + ", relationShip='" + getRelationShip() + "'" + ", network='"
                + getNetwork() + "'" + ", subrogation='" + getSubrogation() + "'" + "}";
    }

    public CoverageModel convert(ArrayList<CoreResourceModel> coreResources) {
        if (coreResources.stream().anyMatch(x -> x.getId() == UUID.fromString(this.getGuid()))) {
            return (CoverageModel) coreResources.stream().filter(x -> x.getId() == UUID.fromString(this.getGuid())).findFirst().get();
        }
        CoverageModel cov = new CoverageModel();
        if (this.getGuid() != null) {
            cov.setId(UUID.fromString(this.getGuid()));
        }
        if (this.getId() != null) {
            IdentifierModel i = new IdentifierModel();
            i.setValue("cov-" + this.getId());
            cov.addIdentifier(i);
        }
        if (this.getCoverageType() != null) {
            cov.setCoverageType(this.getCoverageType().convert());
        }
        if (this.getSubscriberId() != null) {
            cov.setSubscriberId(this.getSubscriberId());
        }
        if (this.getDependent() != null) {
            cov.setDependent(this.getDependent());
        }
        if (this.getRelationShip() != null) {
            cov.setRelationship(this.getRelationShip().convert());
        }
        if (this.getNetwork() != null) {
            cov.setNetwork(this.getNetwork());
        }
        if (this.getSubrogation() != null) {
            cov.setSubrogation(this.getSubrogation());
        }
        if (this.getBeneficiary() != null) {
            cov.setBeneficiary(this.getBeneficiary().convert(coreResources));
        }
        if (this.getClassComponents() != null) {
            cov.setClassComponents(
                this.getClassComponents().stream().map(i -> i.convert()).collect(Collectors.toCollection(ArrayList::new))
            );
        }
        if (this.getCostToBeneficiaryComponents() != null) {
            cov.setCostToBeneficiaryComponents(
                this.getCostToBeneficiaryComponents().stream().map(i -> i.convert()).collect(Collectors.toCollection(ArrayList::new))
            );
        }
        if (this.getPayor() != null) {
            cov.setPayor(this.getPayor().convert(coreResources));
        }
        if (this.getSubscriberPatient() != null) {
            cov.setSubscriber(this.getSubscriberPatient().convert(coreResources));
        }

        coreResources.add(cov);
        return cov;
    }

    public static Coverage convertFrom(CoverageModel model) {
        Coverage cov = new Coverage();
        if (model.getId() != null) {
            cov.setGuid(model.getId().toString());
        }
        if (model.getIdentifiers() != null) {
            cov.setIdentifiers(model.getIdentifiers().stream().map(i -> ReferenceIdentifier.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getCoverageType() != null) {
            cov.setCoverageType(CoverageTypeEnum.valueOf(model.getCoverageType().name()));
        }
        if (model.getSubscriberId() != null) {
            cov.setSubscriberId(model.getSubscriberId());
        }
        if (model.getDependent() != null) {
            cov.setDependent(model.getDependent());
        }
        if (model.getRelationship() != null) {
            cov.setRelationShip(RelationShipEnum.valueOf(model.getRelationship().name()));
        }
        if (model.getNetwork() != null) {
            cov.setNetwork(model.getNetwork());
        }
        if (model.getSubrogation() != null) {
            cov.setSubrogation(model.getSubrogation());
        }
        if (model.getBeneficiary() != null) {
            cov.setBeneficiary(Patient.convertFrom(model.getBeneficiary()));
        }
        if (model.getClassComponents() != null) {
            cov.setClassComponents(model.getClassComponents().stream().map(i -> ClassComponent.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getCostToBeneficiaryComponents() != null) {
            cov.setCostToBeneficiaryComponents(
                model
                    .getCostToBeneficiaryComponents()
                    .stream()
                    .map(i -> CostToBeneficiaryComponent.convertFrom(i))
                    .collect(Collectors.toSet())
            );
        }
        if (model.getPayor() != null) {
            cov.setPayor(Organization.convertFrom(model.getPayor()));
        }
        if (model.getSubscriber() != null) {
            cov.setSubscriberPatient(Patient.convertFrom(model.getSubscriber()));
        }

        return cov;
    }
}

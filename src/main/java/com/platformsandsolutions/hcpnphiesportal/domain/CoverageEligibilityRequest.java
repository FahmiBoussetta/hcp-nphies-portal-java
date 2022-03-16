package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.PriorityEnum;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.*;

/**
 * A CoverageEligibilityRequest.
 */
@Entity
@Table(name = "coverage_eligibility_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CoverageEligibilityRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid")
    private String guid;

    @Column(name = "parsed")
    private String parsed;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private PriorityEnum priority;

    @Column(name = "identifier")
    private String identifier;

    @NotNull
    @Column(name = "serviced_date", nullable = false)
    private Instant servicedDate;

    @Column(name = "serviced_date_end")
    private Instant servicedDateEnd;

    @OneToMany(mappedBy = "coverageEligibilityRequest", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "coverageEligibilityRequest" }, allowSetters = true)
    private Set<CovEliErrorMessages> errors = new HashSet<>();

    @OneToMany(mappedBy = "coverageEligibilityRequest", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "coverageEligibilityRequest" }, allowSetters = true)
    private Set<ListEligibilityPurposeEnum> purposes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "names", "contacts", "address" }, allowSetters = true)
    private Patient patient;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contacts", "address" }, allowSetters = true)
    private Organization provider;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contacts", "address" }, allowSetters = true)
    private Organization insurer;

    @ManyToOne
    @JsonIgnoreProperties(value = { "managingOrganization" }, allowSetters = true)
    private Location facility;

    @JsonIgnoreProperties(value = { "errors", "insurances", "patient", "insurer" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private CoverageEligibilityResponse coverageEligibilityResponse;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_coverage_eligibility_request__coverages",
        joinColumns = @JoinColumn(name = "coverage_eligibility_request_id"),
        inverseJoinColumns = @JoinColumn(name = "coverages_id")
    )
    @JsonIgnoreProperties(
        value = {
            "classComponents", "costToBeneficiaryComponents", "subscriberPatient", "beneficiary", "payor", "coverageEligibilityRequests",
        },
        allowSetters = true
    )
    private Set<Coverage> coverages = new HashSet<>();

    @ManyToMany(mappedBy = "covEliReqIdentifiers")
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

    public CoverageEligibilityRequest id(Long id) {
        this.id = id;
        return this;
    }

    public String getGuid() {
        return this.guid;
    }

    public CoverageEligibilityRequest guid(String guid) {
        this.guid = guid;
        return this;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getParsed() {
        return this.parsed;
    }

    public CoverageEligibilityRequest parsed(String parsed) {
        this.parsed = parsed;
        return this;
    }

    public void setParsed(String parsed) {
        this.parsed = parsed;
    }

    public PriorityEnum getPriority() {
        return this.priority;
    }

    public CoverageEligibilityRequest priority(PriorityEnum priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(PriorityEnum priority) {
        this.priority = priority;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public CoverageEligibilityRequest identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Instant getServicedDate() {
        return this.servicedDate;
    }

    public CoverageEligibilityRequest servicedDate(Instant servicedDate) {
        this.servicedDate = servicedDate;
        return this;
    }

    public void setServicedDate(Instant servicedDate) {
        this.servicedDate = servicedDate;
    }

    public Instant getServicedDateEnd() {
        return this.servicedDateEnd;
    }

    public CoverageEligibilityRequest servicedDateEnd(Instant servicedDateEnd) {
        this.servicedDateEnd = servicedDateEnd;
        return this;
    }

    public void setServicedDateEnd(Instant servicedDateEnd) {
        this.servicedDateEnd = servicedDateEnd;
    }

    public Set<CovEliErrorMessages> getErrors() {
        return this.errors;
    }

    public CoverageEligibilityRequest errors(Set<CovEliErrorMessages> covEliErrorMessages) {
        this.setErrors(covEliErrorMessages);
        return this;
    }

    public CoverageEligibilityRequest addErrors(CovEliErrorMessages covEliErrorMessages) {
        this.errors.add(covEliErrorMessages);
        covEliErrorMessages.setCoverageEligibilityRequest(this);
        return this;
    }

    public CoverageEligibilityRequest removeErrors(CovEliErrorMessages covEliErrorMessages) {
        this.errors.remove(covEliErrorMessages);
        covEliErrorMessages.setCoverageEligibilityRequest(null);
        return this;
    }

    public void setErrors(Set<CovEliErrorMessages> covEliErrorMessages) {
        if (this.errors != null) {
            this.errors.forEach(i -> i.setCoverageEligibilityRequest(null));
        }
        if (covEliErrorMessages != null) {
            covEliErrorMessages.forEach(i -> i.setCoverageEligibilityRequest(this));
        }
        this.errors = covEliErrorMessages;
    }

    public Set<ListEligibilityPurposeEnum> getPurposes() {
        return this.purposes;
    }

    public CoverageEligibilityRequest purposes(Set<ListEligibilityPurposeEnum> listEligibilityPurposeEnums) {
        this.setPurposes(listEligibilityPurposeEnums);
        return this;
    }

    public CoverageEligibilityRequest addPurposes(ListEligibilityPurposeEnum listEligibilityPurposeEnum) {
        this.purposes.add(listEligibilityPurposeEnum);
        listEligibilityPurposeEnum.setCoverageEligibilityRequest(this);
        return this;
    }

    public CoverageEligibilityRequest removePurposes(ListEligibilityPurposeEnum listEligibilityPurposeEnum) {
        this.purposes.remove(listEligibilityPurposeEnum);
        listEligibilityPurposeEnum.setCoverageEligibilityRequest(null);
        return this;
    }

    public void setPurposes(Set<ListEligibilityPurposeEnum> listEligibilityPurposeEnums) {
        if (this.purposes != null) {
            this.purposes.forEach(i -> i.setCoverageEligibilityRequest(null));
        }
        if (listEligibilityPurposeEnums != null) {
            listEligibilityPurposeEnums.forEach(i -> i.setCoverageEligibilityRequest(this));
        }
        this.purposes = listEligibilityPurposeEnums;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public CoverageEligibilityRequest patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Organization getProvider() {
        return this.provider;
    }

    public CoverageEligibilityRequest provider(Organization organization) {
        this.setProvider(organization);
        return this;
    }

    public void setProvider(Organization organization) {
        this.provider = organization;
    }

    public Organization getInsurer() {
        return this.insurer;
    }

    public CoverageEligibilityRequest insurer(Organization organization) {
        this.setInsurer(organization);
        return this;
    }

    public void setInsurer(Organization organization) {
        this.insurer = organization;
    }

    public Location getFacility() {
        return this.facility;
    }

    public CoverageEligibilityRequest facility(Location location) {
        this.setFacility(location);
        return this;
    }

    public void setFacility(Location location) {
        this.facility = location;
    }

    public CoverageEligibilityResponse getCoverageEligibilityResponse() {
        return this.coverageEligibilityResponse;
    }

    public CoverageEligibilityRequest coverageEligibilityResponse(CoverageEligibilityResponse coverageEligibilityResponse) {
        this.setCoverageEligibilityResponse(coverageEligibilityResponse);
        return this;
    }

    public void setCoverageEligibilityResponse(CoverageEligibilityResponse coverageEligibilityResponse) {
        this.coverageEligibilityResponse = coverageEligibilityResponse;
    }

    public Set<Coverage> getCoverages() {
        return this.coverages;
    }

    public CoverageEligibilityRequest coverages(Set<Coverage> coverages) {
        this.setCoverages(coverages);
        return this;
    }

    public CoverageEligibilityRequest addCoverages(Coverage coverage) {
        this.coverages.add(coverage);
        coverage.getCoverageEligibilityRequests().add(this);
        return this;
    }

    public CoverageEligibilityRequest removeCoverages(Coverage coverage) {
        this.coverages.remove(coverage);
        coverage.getCoverageEligibilityRequests().remove(this);
        return this;
    }

    public void setCoverages(Set<Coverage> coverages) {
        this.coverages = coverages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoverageEligibilityRequest)) {
            return false;
        }
        return id != null && id.equals(((CoverageEligibilityRequest) o).id);
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
		return "CoverageEligibilityRequest{" + "id=" + getId() + ", guid='" + getGuid() + "'" + ", parsed='"
				+ getParsed() + "'" + ", priority='" + getPriority() + "'" + ", identifier='" + getIdentifier() + "'"
				+ ", servicedDate='" + getServicedDate() + "'" + ", servicedDateEnd='" + getServicedDateEnd() + "'"
				+ "}";
	}

    public CoverageEligibilityRequestModel convert(ArrayList<CoreResourceModel> coreResources) {
        CoverageEligibilityRequestModel model = new CoverageEligibilityRequestModel();
        model.setCoverages(
            this.getCoverages().stream().map(i -> i.convert(coreResources)).collect(Collectors.toCollection(ArrayList::new))
        );
        if (this.getFacility() != null) {
            model.setFacility(this.getFacility().convert(coreResources));
        }
        model.setPatient(this.getPatient().convert(coreResources));
        model.setInsurer(this.getInsurer().convert(coreResources));
        IdentifierModel id = new IdentifierModel();
        id.setValue(this.getIdentifier() + "_" + this.getId() + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        model.addIdentifier(id);
        model.setPurposes(this.getPurposes().stream().map(i -> i.convert()).collect(Collectors.toCollection(ArrayList::new)));
        model.setServicedDate(Date.from(this.getServicedDate()));
        model.setServicedDateEnd(Date.from(this.getServicedDate()));
        model.setPriority(this.getPriority().convert());
        return model;
    }

    public CoverageEligibilityRequest convertFrom(CoverageEligibilityRequestModel model) {
        CoverageEligibilityRequest c = new CoverageEligibilityRequest();
        if (model.getIdentifiers() != null) {
            c.setIdentifiers(model.getIdentifiers().stream().map(i -> ReferenceIdentifier.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getCoverages() != null) {
            c.setCoverages(model.getCoverages().stream().map(i -> Coverage.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getFacility() != null) {
            c.setFacility(Location.convertFrom(model.getFacility()));
        }
        c.setPatient(Patient.convertFrom(model.getPatient()));
        c.setInsurer(Organization.convertFrom(model.getInsurer()));
        if (model.getPurposes() != null) {
            c.setPurposes(model.getPurposes().stream().map(i -> ListEligibilityPurposeEnum.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getServicedDate() != null) {
            c.setServicedDate(model.getServicedDate().toInstant());
        }
        if (model.getServicedDate() != null) {
            c.setServicedDateEnd(model.getServicedDate().toInstant());
        }
        if (model.getPriority() != null) {
            c.setPriority(PriorityEnum.valueOf(model.getPriority().name()));
        }
        return c;
    }
}

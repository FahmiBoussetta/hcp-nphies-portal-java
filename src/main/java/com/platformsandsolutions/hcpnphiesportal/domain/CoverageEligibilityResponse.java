package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.NotInForceReasonEnum;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.CoverageEligibilityResponseModel;
import platform.fhir_client.models.IdentifierModel;

/**
 * A CoverageEligibilityResponse.
 */
@Entity
@Table(name = "coverage_eligibility_response")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CoverageEligibilityResponse implements Serializable {

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

    @Column(name = "outcome")
    private String outcome;

    @Column(name = "serviced")
    private Instant serviced;

    @Column(name = "serviced_end")
    private Instant servicedEnd;

    @Column(name = "disposition")
    private String disposition;

    @Column(name = "not_inforce_reason")
    private NotInForceReasonEnum notInforceReason;

    @OneToMany(mappedBy = "coverageEligibilityResponse", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "coverageEligibilityResponse" }, allowSetters = true)
    private Set<CovEliRespErrorMessages> errors = new HashSet<>();

    @OneToMany(mappedBy = "coverageEligibilityResponse", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "items", "coverage", "coverageEligibilityResponse" }, allowSetters = true)
    private Set<ResponseInsurance> insurances = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "names", "contacts", "address" }, allowSetters = true)
    private Patient patient;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contacts", "address" }, allowSetters = true)
    private Organization insurer;

    @ManyToMany(mappedBy = "covEliRespIdentifiers")
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

    public CoverageEligibilityResponse id(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return this.value;
    }

    public CoverageEligibilityResponse value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSystem() {
        return this.system;
    }

    public CoverageEligibilityResponse system(String system) {
        this.system = system;
        return this;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getParsed() {
        return this.parsed;
    }

    public CoverageEligibilityResponse parsed(String parsed) {
        this.parsed = parsed;
        return this;
    }

    public void setParsed(String parsed) {
        this.parsed = parsed;
    }

    public String getOutcome() {
        return this.outcome;
    }

    public CoverageEligibilityResponse outcome(String outcome) {
        this.outcome = outcome;
        return this;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public Instant getServiced() {
        return this.serviced;
    }

    public CoverageEligibilityResponse serviced(Instant serviced) {
        this.serviced = serviced;
        return this;
    }

    public void setServiced(Instant serviced) {
        this.serviced = serviced;
    }

    public Instant getServicedEnd() {
        return this.servicedEnd;
    }

    public CoverageEligibilityResponse servicedEnd(Instant servicedEnd) {
        this.servicedEnd = servicedEnd;
        return this;
    }

    public void setServicedEnd(Instant servicedEnd) {
        this.servicedEnd = servicedEnd;
    }

    public String getDisposition() {
        return this.disposition;
    }

    public CoverageEligibilityResponse disposition(String disposition) {
        this.disposition = disposition;
        return this;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public NotInForceReasonEnum getNotInforceReason() {
        return this.notInforceReason;
    }

    public CoverageEligibilityResponse notInforceReason(NotInForceReasonEnum notInforceReason) {
        this.notInforceReason = notInforceReason;
        return this;
    }

    public void setNotInforceReason(NotInForceReasonEnum notInforceReason) {
        this.notInforceReason = notInforceReason;
    }

    public Set<CovEliRespErrorMessages> getErrors() {
        return this.errors;
    }

    public CoverageEligibilityResponse errors(Set<CovEliRespErrorMessages> covEliRespErrorMessages) {
        this.setErrors(covEliRespErrorMessages);
        return this;
    }

    public CoverageEligibilityResponse addErrors(CovEliRespErrorMessages covEliRespErrorMessages) {
        this.errors.add(covEliRespErrorMessages);
        covEliRespErrorMessages.setCoverageEligibilityResponse(this);
        return this;
    }

    public CoverageEligibilityResponse removeErrors(CovEliRespErrorMessages covEliRespErrorMessages) {
        this.errors.remove(covEliRespErrorMessages);
        covEliRespErrorMessages.setCoverageEligibilityResponse(null);
        return this;
    }

    public void setErrors(Set<CovEliRespErrorMessages> covEliRespErrorMessages) {
        if (this.errors != null) {
            this.errors.forEach(i -> i.setCoverageEligibilityResponse(null));
        }
        if (covEliRespErrorMessages != null) {
            covEliRespErrorMessages.forEach(i -> i.setCoverageEligibilityResponse(this));
        }
        this.errors = covEliRespErrorMessages;
    }

    public Set<ResponseInsurance> getInsurances() {
        return this.insurances;
    }

    public CoverageEligibilityResponse insurances(Set<ResponseInsurance> responseInsurances) {
        this.setInsurances(responseInsurances);
        return this;
    }

    public CoverageEligibilityResponse addInsurance(ResponseInsurance responseInsurance) {
        this.insurances.add(responseInsurance);
        responseInsurance.setCoverageEligibilityResponse(this);
        return this;
    }

    public CoverageEligibilityResponse removeInsurance(ResponseInsurance responseInsurance) {
        this.insurances.remove(responseInsurance);
        responseInsurance.setCoverageEligibilityResponse(null);
        return this;
    }

    public void setInsurances(Set<ResponseInsurance> responseInsurances) {
        if (this.insurances != null) {
            this.insurances.forEach(i -> i.setCoverageEligibilityResponse(null));
        }
        if (responseInsurances != null) {
            responseInsurances.forEach(i -> i.setCoverageEligibilityResponse(this));
        }
        this.insurances = responseInsurances;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public CoverageEligibilityResponse patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Organization getInsurer() {
        return this.insurer;
    }

    public CoverageEligibilityResponse insurer(Organization organization) {
        this.setInsurer(organization);
        return this;
    }

    public void setInsurer(Organization organization) {
        this.insurer = organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoverageEligibilityResponse)) {
            return false;
        }
        return id != null && id.equals(((CoverageEligibilityResponse) o).id);
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
        return "CoverageEligibilityResponse{" + "id=" + getId() + ", value='" + getValue() + "'" + ", system='"
                + getSystem() + "'" + ", parsed='" + getParsed() + "'" + ", outcome='" + getOutcome() + "'"
                + ", serviced='" + getServiced() + "'" + ", servicedEnd='" + getServicedEnd() + "'" + ", disposition='"
                + getDisposition() + "'" + ", notInforceReason='" + getNotInforceReason() + "'" + "}";
    }

    public static CoverageEligibilityResponse convertFrom(CoverageEligibilityResponseModel model) {
        CoverageEligibilityResponse resp = new CoverageEligibilityResponse();
        if (model.getIdentifiers() != null) {
            resp.setIdentifiers(model.getIdentifiers().stream().map(i -> ReferenceIdentifier.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getErrors() != null) {
            resp.setErrors(model.getErrors().stream().map(x -> CovEliRespErrorMessages.convertFrom(x)).collect(Collectors.toSet()));
        }
        resp.setDisposition(model.getDisposition());
        if (model.getInsurance() != null) {
            resp.setInsurances(model.getInsurance().stream().map(x -> ResponseInsurance.convertFrom(x)).collect(Collectors.toSet()));
        }
        resp.setInsurer(Organization.convertFrom(model.getInsurer()));
        if (model.getNotInforceReason() != null) {
            resp.setNotInforceReason(NotInForceReasonEnum.valueOf(model.getNotInforceReason().name()));
        }
        if (model.getOutcome() != null) {
            resp.setOutcome(model.getOutcome().toString());
        }
        if (model.getParsedRequest() != null) {
            resp.setParsed(model.getParsedRequest());
        }
        if (model.getPatient() != null) {
            resp.setPatient(Patient.convertFrom(model.getPatient()));
        }
        if (model.getServicedDate() != null) {
            resp.setServiced(model.getServicedDate().toInstant());
        }
        if (model.getServicedDateEnd() != null) {
            resp.setServicedEnd(model.getServicedDateEnd().toInstant());
        }
        if (model.getIdentifiers() != null) {
            resp.setSystem(model.getIdentifiers().get(0).getSystem());
            resp.setValue(model.getIdentifiers().get(0).getValue());
        }
        return resp;
    }

    public CoverageEligibilityResponseModel convertIdentifier() {
        CoverageEligibilityResponseModel cr = new CoverageEligibilityResponseModel();
        IdentifierModel i = new IdentifierModel();
        i.setValue(this.getValue());
        i.setSystem(this.getSystem());
        cr.addIdentifier(i);
        return cr;
    }
}

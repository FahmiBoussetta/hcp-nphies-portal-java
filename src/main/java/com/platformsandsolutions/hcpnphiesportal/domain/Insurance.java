package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.CoreResourceModel;
import platform.fhir_client.models.InsuranceModel;

/**
 * A Insurance.
 */
@Entity
@Table(name = "insurance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Insurance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    @NotNull
    @Column(name = "focal", nullable = false)
    private Boolean focal;

    @Column(name = "pre_auth_ref")
    private String preAuthRef;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "classComponents", "costToBeneficiaryComponents", "subscriberPatient", "beneficiary", "payor", "coverageEligibilityRequests",
        },
        allowSetters = true
    )
    private Coverage coverage;

    @ManyToOne
    @JsonIgnoreProperties(value = { "errors", "items", "totals" }, allowSetters = true)
    private ClaimResponse claimResponse;

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
    private Claim claim;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Insurance id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public Insurance sequence(Integer sequence) {
        this.sequence = sequence;
        return this;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Boolean getFocal() {
        return this.focal;
    }

    public Insurance focal(Boolean focal) {
        this.focal = focal;
        return this;
    }

    public void setFocal(Boolean focal) {
        this.focal = focal;
    }

    public String getPreAuthRef() {
        return this.preAuthRef;
    }

    public Insurance preAuthRef(String preAuthRef) {
        this.preAuthRef = preAuthRef;
        return this;
    }

    public void setPreAuthRef(String preAuthRef) {
        this.preAuthRef = preAuthRef;
    }

    public Coverage getCoverage() {
        return this.coverage;
    }

    public Insurance coverage(Coverage coverage) {
        this.setCoverage(coverage);
        return this;
    }

    public void setCoverage(Coverage coverage) {
        this.coverage = coverage;
    }

    public ClaimResponse getClaimResponse() {
        return this.claimResponse;
    }

    public Insurance claimResponse(ClaimResponse claimResponse) {
        this.setClaimResponse(claimResponse);
        return this;
    }

    public void setClaimResponse(ClaimResponse claimResponse) {
        this.claimResponse = claimResponse;
    }

    public Claim getClaim() {
        return this.claim;
    }

    public Insurance claim(Claim claim) {
        this.setClaim(claim);
        return this;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Insurance)) {
            return false;
        }
        return id != null && id.equals(((Insurance) o).id);
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
        return "Insurance{" + "id=" + getId() + ", sequence=" + getSequence() + ", focal='" + getFocal() + "'"
                + ", preAuthRef='" + getPreAuthRef() + "'" + "}";
    }

    public InsuranceModel convert(ArrayList<CoreResourceModel> coreResources) {
        InsuranceModel ins = new InsuranceModel();
        if (this.getClaimResponse() != null) {
            ins.setClaimResponse(this.getClaimResponse().convertIdentifier());
        }
        if (this.getCoverage() != null) {
            ins.setCoverage(this.getCoverage().convert(coreResources));
        }
        if (this.getFocal() != null) {
            ins.setFocal(this.getFocal());
        }
        if (this.getPreAuthRef() != null) {
            ins.setPreAuthRef(Arrays.asList(this.getPreAuthRef().split(";")));
        }
        if (this.getSequence() != null) {
            ins.setSequence(this.getSequence());
        }
        return ins;
    }

    public static Insurance convertFrom(InsuranceModel model) {
        Insurance ins = new Insurance();
        if (model.getClaimResponse() != null) {
            ins.setClaimResponse(ClaimResponse.convertFrom(model.getClaimResponse()));
        }
        if (model.getCoverage() != null) {
            ins.setCoverage(Coverage.convertFrom(model.getCoverage()));
        }
        if (model.getFocal() != null) {
            ins.setFocal(model.getFocal());
        }
        if (model.getPreAuthRef() != null) {
            ins.setPreAuthRef(String.join(";", model.getPreAuthRef()));
        }
        if (model.getSequence() > -1) {
            ins.setSequence(model.getSequence());
        }
        return ins;
    }
}

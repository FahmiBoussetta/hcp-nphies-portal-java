package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.CareTeamRoleEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.SpecialtyEnum;
import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.CareTeamModel;
import platform.fhir_client.models.CoreResourceModel;

/**
 * A CareTeam.
 */
@Entity
@Table(name = "care_team")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CareTeam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private CareTeamRoleEnum role;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "qualification", nullable = false)
    private SpecialtyEnum qualification;

    @ManyToOne
    @JsonIgnoreProperties(value = { "names" }, allowSetters = true)
    private Practitioner provider;

    @ManyToOne
    @JsonIgnoreProperties(value = { "codes", "specialties", "practitioner", "organization" }, allowSetters = true)
    private PractitionerRole providerRole;

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

    public CareTeam id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public CareTeam sequence(Integer sequence) {
        this.sequence = sequence;
        return this;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public CareTeamRoleEnum getRole() {
        return this.role;
    }

    public CareTeam role(CareTeamRoleEnum role) {
        this.role = role;
        return this;
    }

    public void setRole(CareTeamRoleEnum role) {
        this.role = role;
    }

    public SpecialtyEnum getQualification() {
        return qualification;
    }

    public CareTeam qualification(SpecialtyEnum qualification) {
        this.setQualification(qualification);
        return this;
    }

    public void setQualification(SpecialtyEnum qualification) {
        this.qualification = qualification;
    }

    public Practitioner getProvider() {
        return this.provider;
    }

    public CareTeam provider(Practitioner practitioner) {
        this.setProvider(practitioner);
        return this;
    }

    public void setProvider(Practitioner practitioner) {
        this.provider = practitioner;
    }

    public PractitionerRole getProviderRole() {
        return this.providerRole;
    }

    public CareTeam providerRole(PractitionerRole practitionerRole) {
        this.setProviderRole(practitionerRole);
        return this;
    }

    public void setProviderRole(PractitionerRole practitionerRole) {
        this.providerRole = practitionerRole;
    }

    public Claim getClaim() {
        return this.claim;
    }

    public CareTeam claim(Claim claim) {
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
        if (!(o instanceof CareTeam)) {
            return false;
        }
        return id != null && id.equals(((CareTeam) o).id);
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
        return "CareTeam{" + "id=" + getId() + ", sequence=" + getSequence() + ", role='" + getRole() + "'"
                + ", qualification='" + getQualification() + "'" + ", provider='" + getProvider().getId() + "'"
                + ", providerRole='" + getProviderRole().getId() + "'" + "}";
    }

    public CareTeamModel convert(ArrayList<CoreResourceModel> coreResources) {
        CareTeamModel ct = new CareTeamModel();
        if (this.getProvider() != null) {
            ct.setProvider(this.getProvider().convert(coreResources));
        }
        if (this.getProviderRole() != null) {
            ct.setProviderRole(this.getProviderRole().convert(coreResources));
        }
        if (this.getRole() != null) {
            ct.setRole(this.getRole().convert());
        }
        if (this.getSequence() != null) {
            ct.setSequence(this.getSequence());
        }
        if (this.getQualification() != null) {
            ct.setQualification(this.getQualification().convert());
        }
        return ct;
    }

    public static CareTeam convertFrom(CareTeamModel model) {
        CareTeam ct = new CareTeam();
        if (model.getProvider() != null) {
            ct.setProvider(Practitioner.convertFrom(model.getProvider()));
        }
        if (model.getProviderRole() != null) {
            ct.setProviderRole(PractitionerRole.convertFrom(model.getProviderRole()));
        }
        if (model.getRole() != null) {
            ct.setRole(CareTeamRoleEnum.valueOf(model.getRole().name()));
        }
        if (model.getSequence() > -1) {
            ct.setSequence(model.getSequence());
        }
        if (model.getQualification() != null) {
            ct.setQualification(SpecialtyEnum.valueOf(model.getQualification().name()));
        }
        return ct;
    }
}

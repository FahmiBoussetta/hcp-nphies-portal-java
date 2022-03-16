package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.DiagnosisOnAdmissionEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.DiagnosisTypeEnum;
import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.CoreResourceModel;
import platform.fhir_client.models.DiagnosisModel;

/**
 * A Diagnosis.
 */
@Entity
@Table(name = "diagnosis")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Diagnosis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    @NotNull
    @Column(name = "diagnosis", nullable = false)
    private String diagnosis;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DiagnosisTypeEnum type;

    @Enumerated(EnumType.STRING)
    @Column(name = "on_admission")
    private DiagnosisOnAdmissionEnum onAdmission;

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

    public Diagnosis id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public Diagnosis sequence(Integer sequence) {
        this.sequence = sequence;
        return this;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getDiagnosis() {
        return this.diagnosis;
    }

    public Diagnosis diagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
        return this;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public DiagnosisTypeEnum getType() {
        return this.type;
    }

    public Diagnosis type(DiagnosisTypeEnum type) {
        this.type = type;
        return this;
    }

    public void setType(DiagnosisTypeEnum type) {
        this.type = type;
    }

    public DiagnosisOnAdmissionEnum getOnAdmission() {
        return this.onAdmission;
    }

    public Diagnosis onAdmission(DiagnosisOnAdmissionEnum onAdmission) {
        this.onAdmission = onAdmission;
        return this;
    }

    public void setOnAdmission(DiagnosisOnAdmissionEnum onAdmission) {
        this.onAdmission = onAdmission;
    }

    public Claim getClaim() {
        return this.claim;
    }

    public Diagnosis claim(Claim claim) {
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
        if (!(o instanceof Diagnosis)) {
            return false;
        }
        return id != null && id.equals(((Diagnosis) o).id);
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
        return "Diagnosis{" + "id=" + getId() + ", sequence=" + getSequence() + ", diagnosis='" + getDiagnosis() + "'"
                + ", type='" + getType() + "'" + ", onAdmission='" + getOnAdmission() + "'" + "}";
    }

    public DiagnosisModel convert(ArrayList<CoreResourceModel> coreResources) {
        DiagnosisModel d = new DiagnosisModel();
        if (this.getDiagnosis() != null) {
            d.setDiagnosis(this.getDiagnosis());
        }
        if (this.getOnAdmission() != null) {
            d.setOnAdmission(this.getOnAdmission().convert());
        }
        if (this.getSequence() != null) {
            d.setSequence(this.getSequence());
        }
        if (this.getType() != null) {
            d.setType(this.getType().convert());
        }
        return d;
    }

    public static Diagnosis convertFrom(DiagnosisModel model) {
        Diagnosis d = new Diagnosis();
        if (model.getDiagnosis() != null) {
            d.setDiagnosis(model.getDiagnosis());
        }
        if (model.getOnAdmission() != null) {
            d.setOnAdmission(DiagnosisOnAdmissionEnum.valueOf(model.getOnAdmission().name()));
        }
        if (model.getSequence() > -1) {
            d.setSequence(model.getSequence());
        }
        if (model.getType() != null) {
            d.setType(DiagnosisTypeEnum.valueOf(model.getType().name()));
        }
        return d;
    }
}

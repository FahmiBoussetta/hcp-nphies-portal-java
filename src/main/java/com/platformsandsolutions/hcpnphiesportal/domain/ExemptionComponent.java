package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.ExemptionTypeEnum;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.ExemptionComponentModel;

/**
 * A ExemptionComponent.
 */
@Entity
@Table(name = "exemption_component")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExemptionComponent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ExemptionTypeEnum type;

    @Column(name = "start")
    private Instant start;

    @Column(name = "end")
    private Instant end;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "exceptions", "coverage" }, allowSetters = true)
    private CostToBeneficiaryComponent costToBeneficiary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExemptionComponent id(Long id) {
        this.id = id;
        return this;
    }

    public ExemptionTypeEnum getType() {
        return this.type;
    }

    public ExemptionComponent type(ExemptionTypeEnum type) {
        this.type = type;
        return this;
    }

    public void setType(ExemptionTypeEnum type) {
        this.type = type;
    }

    public Instant getStart() {
        return this.start;
    }

    public ExemptionComponent start(Instant start) {
        this.start = start;
        return this;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return this.end;
    }

    public ExemptionComponent end(Instant end) {
        this.end = end;
        return this;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public CostToBeneficiaryComponent getCostToBeneficiary() {
        return this.costToBeneficiary;
    }

    public ExemptionComponent costToBeneficiary(CostToBeneficiaryComponent costToBeneficiaryComponent) {
        this.setCostToBeneficiary(costToBeneficiaryComponent);
        return this;
    }

    public void setCostToBeneficiary(CostToBeneficiaryComponent costToBeneficiaryComponent) {
        this.costToBeneficiary = costToBeneficiaryComponent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExemptionComponent)) {
            return false;
        }
        return id != null && id.equals(((ExemptionComponent) o).id);
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
        return "ExemptionComponent{" + "id=" + getId() + ", type='" + getType() + "'" + ", start='" + getStart() + "'"
                + ", end='" + getEnd() + "'" + "}";
    }

    public ExemptionComponentModel convert() {
        ExemptionComponentModel model = new ExemptionComponentModel();
        if (this.getEnd() != null) {
            model.setEnd(Date.from(this.getEnd()));
        }
        if (this.getType() != null) {
            model.setType(this.getType().convert());
        }
        if (this.getStart() != null) {
            model.setStart(Date.from(this.getStart()));
        }
        return model;
    }

    public static ExemptionComponent convertFrom(ExemptionComponentModel model) {
        ExemptionComponent e = new ExemptionComponent();
        if (model.getEnd() != null) {
            e.setEnd(model.getEnd().toInstant());
        }
        if (model.getType() != null) {
            e.setType(ExemptionTypeEnum.valueOf(model.getType().name()));
        }
        if (model.getStart() != null) {
            e.setStart(model.getStart().toInstant());
        }
        return e;
    }
}

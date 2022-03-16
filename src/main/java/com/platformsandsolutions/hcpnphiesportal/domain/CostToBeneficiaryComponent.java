package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.CostToBeneficiaryTypeEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.CostToBeneficiaryComponentModel;

/**
 * A CostToBeneficiaryComponent.
 */
@Entity
@Table(name = "cost_to_beneficiary_component")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CostToBeneficiaryComponent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CostToBeneficiaryTypeEnum type;

    @Column(name = "is_money")
    private Boolean isMoney;

    @NotNull
    @Column(name = "value", precision = 21, scale = 2, nullable = false)
    private BigDecimal value;

    @OneToMany(mappedBy = "costToBeneficiary", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "costToBeneficiary" }, allowSetters = true)
    private Set<ExemptionComponent> exceptions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "classComponents", "costToBeneficiaryComponents", "subscriberPatient", "beneficiary", "payor", "coverageEligibilityRequests",
        },
        allowSetters = true
    )
    private Coverage coverage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CostToBeneficiaryComponent id(Long id) {
        this.id = id;
        return this;
    }

    public CostToBeneficiaryTypeEnum getType() {
        return this.type;
    }

    public CostToBeneficiaryComponent type(CostToBeneficiaryTypeEnum type) {
        this.type = type;
        return this;
    }

    public void setType(CostToBeneficiaryTypeEnum type) {
        this.type = type;
    }

    public Boolean getIsMoney() {
        return this.isMoney;
    }

    public CostToBeneficiaryComponent isMoney(Boolean isMoney) {
        this.isMoney = isMoney;
        return this;
    }

    public void setIsMoney(Boolean isMoney) {
        this.isMoney = isMoney;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public CostToBeneficiaryComponent value(BigDecimal value) {
        this.value = value;
        return this;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Set<ExemptionComponent> getExceptions() {
        return this.exceptions;
    }

    public CostToBeneficiaryComponent exceptions(Set<ExemptionComponent> exemptionComponents) {
        this.setExceptions(exemptionComponents);
        return this;
    }

    public CostToBeneficiaryComponent addExceptions(ExemptionComponent exemptionComponent) {
        this.exceptions.add(exemptionComponent);
        exemptionComponent.setCostToBeneficiary(this);
        return this;
    }

    public CostToBeneficiaryComponent removeExceptions(ExemptionComponent exemptionComponent) {
        this.exceptions.remove(exemptionComponent);
        exemptionComponent.setCostToBeneficiary(null);
        return this;
    }

    public void setExceptions(Set<ExemptionComponent> exemptionComponents) {
        if (this.exceptions != null) {
            this.exceptions.forEach(i -> i.setCostToBeneficiary(null));
        }
        if (exemptionComponents != null) {
            exemptionComponents.forEach(i -> i.setCostToBeneficiary(this));
        }
        this.exceptions = exemptionComponents;
    }

    public Coverage getCoverage() {
        return this.coverage;
    }

    public CostToBeneficiaryComponent coverage(Coverage coverage) {
        this.setCoverage(coverage);
        return this;
    }

    public void setCoverage(Coverage coverage) {
        this.coverage = coverage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CostToBeneficiaryComponent)) {
            return false;
        }
        return id != null && id.equals(((CostToBeneficiaryComponent) o).id);
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
        return "CostToBeneficiaryComponent{" + "id=" + getId() + ", type='" + getType() + "'" + ", isMoney='"
                + getIsMoney() + "'" + ", value=" + getValue() + "}";
    }

    public CostToBeneficiaryComponentModel convert() {
        CostToBeneficiaryComponentModel model = new CostToBeneficiaryComponentModel();
        if (this.getExceptions() != null) {
            model.setExceptions(this.getExceptions().stream().map(i -> i.convert()).collect(Collectors.toCollection(ArrayList::new)));
        }
        if (this.getIsMoney() != null) {
            model.setIsMoney(this.getIsMoney());
        }
        if (this.getType() != null) {
            model.setType(this.getType().convert());
        }
        if (this.getValue() != null) {
            model.setValue(this.getValue());
        }
        return model;
    }

    public static CostToBeneficiaryComponent convertFrom(CostToBeneficiaryComponentModel model) {
        CostToBeneficiaryComponent c = new CostToBeneficiaryComponent();
        if (model.getExceptions() != null) {
            c.setExceptions(model.getExceptions().stream().map(i -> ExemptionComponent.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getIsMoney() != null) {
            c.setIsMoney(model.getIsMoney());
        }
        if (model.getType() != null) {
            c.setType(CostToBeneficiaryTypeEnum.valueOf(model.getType().name()));
        }
        if (model.getValue() != null) {
            c.setValue(model.getValue());
        }
        return c;
    }
}

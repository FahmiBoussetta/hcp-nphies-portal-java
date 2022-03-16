package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.BenefitCategoryEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.BenefitNetworkEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.BenefitTermEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.BenefitUnitEnum;
import java.io.Serializable;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.EligibilityResponseInsuranceItemModel;

/**
 * A ResponseInsuranceItem.
 */
@Entity
@Table(name = "response_insurance_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ResponseInsuranceItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category")
    private BenefitCategoryEnum category;

    @Column(name = "excluded")
    private Boolean excluded;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "network")
    private BenefitNetworkEnum network;

    @Column(name = "unit")
    private BenefitUnitEnum unit;

    @Column(name = "term")
    private BenefitTermEnum term;

    @OneToMany(mappedBy = "responseInsuranceItem", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "responseInsuranceItem" }, allowSetters = true)
    private Set<InsuranceBenefit> benefits = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "items", "coverage", "coverageEligibilityResponse" }, allowSetters = true)
    private ResponseInsurance responseInsurance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ResponseInsuranceItem id(Long id) {
        this.id = id;
        return this;
    }

    public BenefitCategoryEnum getCategory() {
        return this.category;
    }

    public ResponseInsuranceItem category(BenefitCategoryEnum category) {
        this.category = category;
        return this;
    }

    public void setCategory(BenefitCategoryEnum benefitCategoryEnum) {
        this.category = benefitCategoryEnum;
    }

    public Boolean getExcluded() {
        return this.excluded;
    }

    public ResponseInsuranceItem excluded(Boolean excluded) {
        this.excluded = excluded;
        return this;
    }

    public void setExcluded(Boolean excluded) {
        this.excluded = excluded;
    }

    public String getName() {
        return this.name;
    }

    public ResponseInsuranceItem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ResponseInsuranceItem description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BenefitNetworkEnum getNetwork() {
        return this.network;
    }

    public ResponseInsuranceItem network(BenefitNetworkEnum network) {
        this.network = network;
        return this;
    }

    public void setNetwork(BenefitNetworkEnum network) {
        this.network = network;
    }

    public BenefitUnitEnum getUnit() {
        return this.unit;
    }

    public ResponseInsuranceItem unit(BenefitUnitEnum unit) {
        this.unit = unit;
        return this;
    }

    public void setUnit(BenefitUnitEnum unit) {
        this.unit = unit;
    }

    public BenefitTermEnum getTerm() {
        return this.term;
    }

    public ResponseInsuranceItem term(BenefitTermEnum term) {
        this.term = term;
        return this;
    }

    public void setTerm(BenefitTermEnum term) {
        this.term = term;
    }

    public Set<InsuranceBenefit> getBenefits() {
        return this.benefits;
    }

    public ResponseInsuranceItem benefits(Set<InsuranceBenefit> insuranceBenefits) {
        this.setBenefits(insuranceBenefits);
        return this;
    }

    public ResponseInsuranceItem addBenefit(InsuranceBenefit insuranceBenefit) {
        this.benefits.add(insuranceBenefit);
        insuranceBenefit.setResponseInsuranceItem(this);
        return this;
    }

    public ResponseInsuranceItem removeBenefit(InsuranceBenefit insuranceBenefit) {
        this.benefits.remove(insuranceBenefit);
        insuranceBenefit.setResponseInsuranceItem(null);
        return this;
    }

    public void setBenefits(Set<InsuranceBenefit> insuranceBenefits) {
        if (this.benefits != null) {
            this.benefits.forEach(i -> i.setResponseInsuranceItem(null));
        }
        if (insuranceBenefits != null) {
            insuranceBenefits.forEach(i -> i.setResponseInsuranceItem(this));
        }
        this.benefits = insuranceBenefits;
    }

    public ResponseInsurance getResponseInsurance() {
        return this.responseInsurance;
    }

    public ResponseInsuranceItem responseInsurance(ResponseInsurance responseInsurance) {
        this.setResponseInsurance(responseInsurance);
        return this;
    }

    public void setResponseInsurance(ResponseInsurance responseInsurance) {
        this.responseInsurance = responseInsurance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResponseInsuranceItem)) {
            return false;
        }
        return id != null && id.equals(((ResponseInsuranceItem) o).id);
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
        return "ResponseInsuranceItem{" + "id=" + getId() + ", category='" + getCategory() + "'" + ", excluded='"
                + getExcluded() + "'" + ", name='" + getName() + "'" + ", description='" + getDescription() + "'"
                + ", network='" + getNetwork() + "'" + ", unit='" + getUnit() + "'" + ", term='" + getTerm() + "'"
                + "}";
    }

    public static ResponseInsuranceItem convertFrom(EligibilityResponseInsuranceItemModel y2) {
        ResponseInsuranceItem item = new ResponseInsuranceItem();
        if (y2.getBenefits() != null) {
            item.setBenefits(y2.getBenefits().stream().map(y -> InsuranceBenefit.convertFrom(y)).collect(Collectors.toSet()));
        }
        if (y2.getCategory() != null) {
            item.setCategory(BenefitCategoryEnum.valueOf(y2.getCategory().name()));
        }
        if (y2.getDescription() != null) {
            item.setDescription(y2.getDescription());
        }
        if (y2.getExcluded() != null) {
            item.setExcluded(y2.getExcluded());
        }
        if (y2.getName() != null) {
            item.setName(y2.getName());
        }
        if (y2.getNetwork() != null) {
            item.setNetwork(BenefitNetworkEnum.valueOf(y2.getNetwork().name()));
        }
        if (y2.getTerm() != null) {
            item.setTerm(BenefitTermEnum.valueOf(y2.getTerm().name()));
        }
        if (y2.getUnit() != null) {
            item.setUnit(BenefitUnitEnum.valueOf(y2.getUnit().name()));
        }
        return item;
    }
}

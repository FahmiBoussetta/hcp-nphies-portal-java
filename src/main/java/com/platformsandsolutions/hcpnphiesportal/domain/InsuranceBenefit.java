package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.BenefitTypeEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.EligibilityResponseBenefitModel;

/**
 * A InsuranceBenefit.
 */
@Entity
@Table(name = "insurance_benefit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InsuranceBenefit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private BenefitTypeEnum type;

    @Column(name = "allowed")
    private String allowedString;

    @Column(name = "allowed_unsigned_int")
    private Integer allowedUnsignedInt;

    @Column(name = "allowed_money")
    private BigDecimal allowedMoney;

    @Column(name = "used")
    private String usedString;

    @Column(name = "used_unsigned_int")
    private Integer usedUnsignedInt;

    @Column(name = "used_money")
    private BigDecimal usedMoney;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "benefits", "responseInsurance" }, allowSetters = true)
    private ResponseInsuranceItem responseInsuranceItem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InsuranceBenefit id(Long id) {
        this.id = id;
        return this;
    }

    public ResponseInsuranceItem getResponseInsuranceItem() {
        return this.responseInsuranceItem;
    }

    public BenefitTypeEnum getType() {
        return type;
    }

    public InsuranceBenefit type(BenefitTypeEnum type) {
        this.type = type;
        return this;
    }

    public void setType(BenefitTypeEnum type) {
        this.type = type;
    }

    public String getAllowedString() {
        return allowedString;
    }

    public InsuranceBenefit allowedString(String allowedString) {
        this.allowedString = allowedString;
        return this;
    }

    public void setAllowedString(String allowedString) {
        this.allowedString = allowedString;
    }

    public Integer getAllowedUnsignedInt() {
        return allowedUnsignedInt;
    }

    public InsuranceBenefit allowedUnsignedInt(Integer allowedUnsignedInt) {
        this.allowedUnsignedInt = allowedUnsignedInt;
        return this;
    }

    public void setAllowedUnsignedInt(Integer allowedUnsignedInt) {
        this.allowedUnsignedInt = allowedUnsignedInt;
    }

    public BigDecimal getAllowedMoney() {
        return allowedMoney;
    }

    public InsuranceBenefit allowedMoney(BigDecimal allowedMoney) {
        this.allowedMoney = allowedMoney;
        return this;
    }

    public void setAllowedMoney(BigDecimal allowedMoney) {
        this.allowedMoney = allowedMoney;
    }

    public String getUsedString() {
        return usedString;
    }

    public InsuranceBenefit usedString(String usedString) {
        this.usedString = usedString;
        return this;
    }

    public void setUsedString(String usedString) {
        this.usedString = usedString;
    }

    public Integer getUsedUnsignedInt() {
        return usedUnsignedInt;
    }

    public InsuranceBenefit usedUnsignedInt(Integer usedUnsignedInt) {
        this.usedUnsignedInt = usedUnsignedInt;
        return this;
    }

    public void setUsedUnsignedInt(Integer usedUnsignedInt) {
        this.usedUnsignedInt = usedUnsignedInt;
    }

    public BigDecimal getUsedMoney() {
        return usedMoney;
    }

    public InsuranceBenefit usedMoney(BigDecimal usedMoney) {
        this.usedMoney = usedMoney;
        return this;
    }

    public void setUsedMoney(BigDecimal usedMoney) {
        this.usedMoney = usedMoney;
    }

    public InsuranceBenefit responseInsuranceItem(ResponseInsuranceItem responseInsuranceItem) {
        this.setResponseInsuranceItem(responseInsuranceItem);
        return this;
    }

    public void setResponseInsuranceItem(ResponseInsuranceItem responseInsuranceItem) {
        this.responseInsuranceItem = responseInsuranceItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsuranceBenefit)) {
            return false;
        }
        return id != null && id.equals(((InsuranceBenefit) o).id);
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
        return "InsuranceBenefit{" + "id=" + getId() + ", allowed='" + getAllowedString() + "'" + ", used='"
                + getUsedString() + "'"
                + "}";
    }

    public static InsuranceBenefit convertFrom(EligibilityResponseBenefitModel y) {
        InsuranceBenefit ben = new InsuranceBenefit();
        if (y.getAllowedString() != null) {
            ben.setAllowedString(y.getAllowedString());
        }
        if (y.getAllowedUnsignedInt() != null) {
            ben.setAllowedUnsignedInt(y.getAllowedUnsignedInt());
        }
        if (y.getAllowedMoney() != null) {
            ben.setAllowedMoney(y.getAllowedMoney());
        }
        if (y.getUsedString() != null) {
            ben.setUsedString(y.getUsedString());
        }
        if (y.getUsedUnsignedInt() != null) {
            ben.setUsedUnsignedInt(y.getUsedUnsignedInt());
        }
        if (y.getUsedMoney() != null) {
            ben.setUsedMoney(y.getUsedMoney());
        }
        if (y.getType() != null) {
            ben.setType(BenefitTypeEnum.valueOf(y.getType().name()));
        }
        return ben;
    }
}

package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.AdjudicationEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.TotalModel;

/**
 * A Total.
 */
@Entity
@Table(name = "total")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Total implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category")
    private AdjudicationEnum category;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "errors", "items", "totals" }, allowSetters = true)
    private ClaimResponse claimResponse;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Total id(Long id) {
        this.id = id;
        return this;
    }

    public AdjudicationEnum getCategory() {
        return this.category;
    }

    public Total category(AdjudicationEnum category) {
        this.category = category;
        return this;
    }

    public void setCategory(AdjudicationEnum category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Total amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ClaimResponse getClaimResponse() {
        return this.claimResponse;
    }

    public Total claimResponse(ClaimResponse claimResponse) {
        this.setClaimResponse(claimResponse);
        return this;
    }

    public void setClaimResponse(ClaimResponse claimResponse) {
        this.claimResponse = claimResponse;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Total)) {
            return false;
        }
        return id != null && id.equals(((Total) o).id);
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
        return "Total{" +
                "id=" + getId() +
                ", category='" + getCategory() + "'" +
                ", amount=" + getAmount() +
                "}";
    }

    public static Total convertFrom(TotalModel x) {
        Total t = new Total();
        if (x.getCategory() != null) {
            t.setCategory(AdjudicationEnum.valueOf(x.getCategory().name()));
        }
        t.setAmount(x.getAmount());
        return t;
    }
}

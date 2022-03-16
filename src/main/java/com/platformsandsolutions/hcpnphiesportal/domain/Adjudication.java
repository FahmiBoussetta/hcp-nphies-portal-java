package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.AdjudicationEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.AdjudicationReasonEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.AdjudicationModel;

/**
 * A Adjudication.
 */
@Entity
@Table(name = "adjudication")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Adjudication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "category", nullable = false)
    private AdjudicationEnum category;

    @Column(name = "reason")
    private AdjudicationReasonEnum reason;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "value", precision = 21, scale = 2)
    private BigDecimal value;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "notes", "adjudications", "details", "claimResponse" }, allowSetters = true)
    private AdjudicationItem adjudicationItem;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "notes", "adjudications", "subDetails", "adjudicationItem" }, allowSetters = true)
    private AdjudicationDetailItem adjudicationDetailItem;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "notes", "adjudications", "adjudicationDetailItem" }, allowSetters = true)
    private AdjudicationSubDetailItem adjudicationSubDetailItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Adjudication id(Long id) {
        this.id = id;
        return this;
    }

    public AdjudicationEnum getCategory() {
        return this.category;
    }

    public Adjudication category(AdjudicationEnum updatedCategory) {
        this.category = updatedCategory;
        return this;
    }

    public void setCategory(AdjudicationEnum category) {
        this.category = category;
    }

    public AdjudicationReasonEnum getReason() {
        return this.reason;
    }

    public Adjudication reason(AdjudicationReasonEnum reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(AdjudicationReasonEnum reason) {
        this.reason = reason;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Adjudication amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public Adjudication value(BigDecimal value) {
        this.value = value;
        return this;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public AdjudicationItem getAdjudicationItem() {
        return this.adjudicationItem;
    }

    public Adjudication adjudicationItem(AdjudicationItem adjudicationItem) {
        this.setAdjudicationItem(adjudicationItem);
        return this;
    }

    public void setAdjudicationItem(AdjudicationItem adjudicationItem) {
        this.adjudicationItem = adjudicationItem;
    }

    public AdjudicationDetailItem getAdjudicationDetailItem() {
        return this.adjudicationDetailItem;
    }

    public Adjudication adjudicationDetailItem(AdjudicationDetailItem adjudicationDetailItem) {
        this.setAdjudicationDetailItem(adjudicationDetailItem);
        return this;
    }

    public void setAdjudicationDetailItem(AdjudicationDetailItem adjudicationDetailItem) {
        this.adjudicationDetailItem = adjudicationDetailItem;
    }

    public AdjudicationSubDetailItem getAdjudicationSubDetailItem() {
        return this.adjudicationSubDetailItem;
    }

    public Adjudication adjudicationSubDetailItem(AdjudicationSubDetailItem adjudicationSubDetailItem) {
        this.setAdjudicationSubDetailItem(adjudicationSubDetailItem);
        return this;
    }

    public void setAdjudicationSubDetailItem(AdjudicationSubDetailItem adjudicationSubDetailItem) {
        this.adjudicationSubDetailItem = adjudicationSubDetailItem;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Adjudication)) {
            return false;
        }
        return id != null && id.equals(((Adjudication) o).id);
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
        return "Adjudication{" +
                "id=" + getId() +
                ", category='" + getCategory() + "'" +
                ", reason='" + getReason() + "'" +
                ", amount=" + getAmount() +
                ", value=" + getValue() +
                "}";
    }

    public static Adjudication convertFrom(AdjudicationModel y) {
        Adjudication adj = new Adjudication();
        if (y.getCategory() != null) {
            adj.setCategory(AdjudicationEnum.valueOf(y.getCategory().name()));
        }
        if (y.getReason() != null) {
            adj.setReason(AdjudicationReasonEnum.valueOf(y.getReason().name()));
        }
        adj.setAmount(y.getAmount());
        adj.setValue(y.getValue());
        return adj;
    }
}

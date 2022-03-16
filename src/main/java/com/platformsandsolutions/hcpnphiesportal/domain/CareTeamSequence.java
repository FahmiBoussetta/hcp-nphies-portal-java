package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CareTeamSequence.
 */
@Entity
@Table(name = "care_team_sequence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CareTeamSequence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "care_seq")
    private Integer careSeq;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(
        value = { "diagnosisSequences", "careTeamSequences", "informationSequences", "udis", "details", "claim" },
        allowSetters = true
    )
    private Item item;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CareTeamSequence id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getCareSeq() {
        return this.careSeq;
    }

    public CareTeamSequence careSeq(Integer careSeq) {
        this.careSeq = careSeq;
        return this;
    }

    public void setCareSeq(Integer careSeq) {
        this.careSeq = careSeq;
    }

    public Item getItem() {
        return this.item;
    }

    public CareTeamSequence item(Item item) {
        this.setItem(item);
        return this;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CareTeamSequence)) {
            return false;
        }
        return id != null && id.equals(((CareTeamSequence) o).id);
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
        return "CareTeamSequence{" +
                "id=" + getId() +
                ", careSeq=" + getCareSeq() +
                "}";
    }

    public static CareTeamSequence convertFrom(Integer model) {
        CareTeamSequence c = new CareTeamSequence();
        c.setCareSeq(model);
        return c;
    }
}

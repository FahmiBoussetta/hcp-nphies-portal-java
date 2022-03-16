package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.ClaimRelationshipEnum;
import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.CoreResourceModel;
import platform.fhir_client.models.RelatedModel;

/**
 * A Related.
 */
@Entity
@Table(name = "related")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Related implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation_ship")
    private ClaimRelationshipEnum relationShip;

    @ManyToOne(cascade = CascadeType.ALL)
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

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Related id(Long id) {
        this.id = id;
        return this;
    }

    public ClaimRelationshipEnum getRelationShip() {
        return this.relationShip;
    }

    public Related relationShip(ClaimRelationshipEnum relationShip) {
        this.relationShip = relationShip;
        return this;
    }

    public void setRelationShip(ClaimRelationshipEnum relationShip) {
        this.relationShip = relationShip;
    }

    public Claim getClaim() {
        return this.claim;
    }

    public Related claim(Claim claim) {
        this.setClaim(claim);
        return this;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Related)) {
            return false;
        }
        return id != null && id.equals(((Related) o).id);
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
        return "Related{" + "id=" + getId() + ", relationShip='" + getRelationShip() + "'" + "}";
    }

    public RelatedModel convert(ArrayList<CoreResourceModel> coreResources) {
        RelatedModel r = new RelatedModel();
        if (this.getClaim() != null) {
            r.setClaim(this.getClaim().convert(coreResources));
        }
        if (this.getRelationShip() != null) {
            r.setRelationship(this.getRelationShip().convert());
        }
        return r;
    }

    public static Related convertFrom(RelatedModel model) {
        Related r = new Related();
        if (model.getClaim() != null) {
            r.setClaim(Claim.convertFrom(model.getClaim()));
        }
        if (model.getRelationship() != null) {
            r.setRelationShip(ClaimRelationshipEnum.valueOf(model.getRelationship().name()));
        }
        return r;
    }
}

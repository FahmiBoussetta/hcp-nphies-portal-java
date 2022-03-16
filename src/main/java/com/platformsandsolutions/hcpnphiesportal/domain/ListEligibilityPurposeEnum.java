package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.EligibilityPurposeEnum;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.utils.*;

/**
 * A ListEligibilityPurposeEnum.
 */
@Entity
@Table(name = "list_eligibility_purpose_enum")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ListEligibilityPurposeEnum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "erp")
    private EligibilityPurposeEnum erp;

    @ManyToOne
    @JsonIgnoreProperties(value = { "errors", "purposes", "patient", "provider", "insurer", "facility", "coverages" }, allowSetters = true)
    private CoverageEligibilityRequest coverageEligibilityRequest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ListEligibilityPurposeEnum id(Long id) {
        this.id = id;
        return this;
    }

    public EligibilityPurposeEnum getErp() {
        return this.erp;
    }

    public ListEligibilityPurposeEnum erp(EligibilityPurposeEnum erp) {
        this.erp = erp;
        return this;
    }

    public void setErp(EligibilityPurposeEnum erp) {
        this.erp = erp;
    }

    public CoverageEligibilityRequest getCoverageEligibilityRequest() {
        return this.coverageEligibilityRequest;
    }

    public ListEligibilityPurposeEnum coverageEligibilityRequest(CoverageEligibilityRequest coverageEligibilityRequest) {
        this.setCoverageEligibilityRequest(coverageEligibilityRequest);
        return this;
    }

    public void setCoverageEligibilityRequest(CoverageEligibilityRequest coverageEligibilityRequest) {
        this.coverageEligibilityRequest = coverageEligibilityRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ListEligibilityPurposeEnum)) {
            return false;
        }
        return id != null && id.equals(((ListEligibilityPurposeEnum) o).id);
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
        return "ListEligibilityPurposeEnum{" + "id=" + getId() + ", erp='" + getErp() + "'" + "}";
    }

    public Enums.EligibilityPurposeEnum convert() {
        for (Enums.EligibilityPurposeEnum e : Enums.EligibilityPurposeEnum.values()) {
            if (e.getDescription() == this.erp.getValue()) {
                return e;
            }
        }
        return null;
    }

    public static ListEligibilityPurposeEnum convertFrom(Enums.EligibilityPurposeEnum i) {
        ListEligibilityPurposeEnum l = new ListEligibilityPurposeEnum();
        l.setErp(EligibilityPurposeEnum.valueOf(i.name()));
        return l;
    }
}

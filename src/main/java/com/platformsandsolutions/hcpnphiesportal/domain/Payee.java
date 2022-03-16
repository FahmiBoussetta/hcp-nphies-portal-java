package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.PayeeTypeEnum;
import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.CoreResourceModel;
import platform.fhir_client.models.PayeeModel;

/**
 * A Payee.
 */
@Entity
@Table(name = "payee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Payee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PayeeTypeEnum type;

    @ManyToOne
    @JsonIgnoreProperties(value = { "names", "contacts", "address" }, allowSetters = true)
    private Patient partyPatient;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contacts", "address" }, allowSetters = true)
    private Organization partyOrganization;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Payee id(Long id) {
        this.id = id;
        return this;
    }

    public PayeeTypeEnum getType() {
        return this.type;
    }

    public Payee type(PayeeTypeEnum type) {
        this.type = type;
        return this;
    }

    public void setType(PayeeTypeEnum type) {
        this.type = type;
    }

    public Patient getPartyPatient() {
        return this.partyPatient;
    }

    public Payee partyPatient(Patient patient) {
        this.setPartyPatient(patient);
        return this;
    }

    public void setPartyPatient(Patient patient) {
        this.partyPatient = patient;
    }

    public Organization getPartyOrganization() {
        return this.partyOrganization;
    }

    public Payee partyOrganization(Organization organization) {
        this.setPartyOrganization(organization);
        return this;
    }

    public void setPartyOrganization(Organization organization) {
        this.partyOrganization = organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payee)) {
            return false;
        }
        return id != null && id.equals(((Payee) o).id);
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
        return "Payee{" + "id=" + getId() + ", type='" + getType() + "'" + "}";
    }

    public PayeeModel convert(ArrayList<CoreResourceModel> coreResources) {
        PayeeModel p = new PayeeModel();
        if (this.getPartyOrganization() != null) {
            p.setPartyOrganization(this.getPartyOrganization().convert(coreResources));
        }
        if (this.getPartyPatient() != null) {
            p.setPartyPatient(this.getPartyPatient().convert(coreResources));
        }
        if (this.getType() != null) {
            p.setType(this.getType().convert());
        }
        return p;
    }

    public static Payee convertFrom(PayeeModel model) {
        Payee p = new Payee();
        if (model.getPartyOrganization() != null) {
            p.setPartyOrganization(Organization.convertFrom(model.getPartyOrganization()));
        }
        if (model.getPartyPatient() != null) {
            p.setPartyPatient(Patient.convertFrom(model.getPartyPatient()));
        }
        if (model.getType() != null) {
            p.setType(PayeeTypeEnum.valueOf(model.getType().name()));
        }
        return p;
    }
}

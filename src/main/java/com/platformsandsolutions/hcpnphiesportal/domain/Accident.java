package com.platformsandsolutions.hcpnphiesportal.domain;

import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.AccidentTypeEnum;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
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
import platform.fhir_client.models.AccidentModel;
import platform.fhir_client.models.CoreResourceModel;

/**
 * A Accident.
 */
@Entity
@Table(name = "accident")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Accident implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AccidentTypeEnum type;

    @ManyToOne(cascade = CascadeType.ALL)
    private Address location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Accident id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getDate() {
        return this.date;
    }

    public Accident date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public AccidentTypeEnum getType() {
        return this.type;
    }

    public Accident type(AccidentTypeEnum type) {
        this.type = type;
        return this;
    }

    public void setType(AccidentTypeEnum type) {
        this.type = type;
    }

    public Address getLocation() {
        return this.location;
    }

    public Accident location(Address address) {
        this.setLocation(address);
        return this;
    }

    public void setLocation(Address address) {
        this.location = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Accident)) {
            return false;
        }
        return id != null && id.equals(((Accident) o).id);
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
        return "Accident{" + "id=" + getId() + ", date='" + getDate() + "'" + ", type='" + getType() + "'" + "}";
    }

    public AccidentModel convert(ArrayList<CoreResourceModel> coreResources) {
        AccidentModel acc = new AccidentModel();
        if (this.getDate() != null) {
            acc.setDate(Date.from(this.getDate()));
        }
        if (this.getLocation() != null) {
            acc.setLocation(this.getLocation().convert());
        }
        if (this.getType() != null) {
            acc.setType(this.getType().convert());
        }
        return acc;
    }

    public static Accident convertFrom(AccidentModel model) {
        Accident acc = new Accident();
        if (model.getDate() != null) {
            acc.setDate(model.getDate().toInstant());
        }
        if (model.getLocation() != null) {
            acc.setLocation(Address.convertFrom(model.getLocation()));
        }
        if (model.getType() != null) {
            acc.setType(AccidentTypeEnum.valueOf(model.getType().name()));
        }
        return acc;
    }
}

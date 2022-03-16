package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.LocationTypeEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.CoreResourceModel;
import platform.fhir_client.models.IdentifierModel;
import platform.fhir_client.models.LocationModel;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid")
    private String guid;

    @Column(name = "identifier")
    private String identifier;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LocationTypeEnum type;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contacts", "address" }, allowSetters = true)
    private Organization managingOrganization;

    @ManyToMany(mappedBy = "locationIdentifiers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ReferenceIdentifier> identifiers = new HashSet<>();

    public Set<ReferenceIdentifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Set<ReferenceIdentifier> identifiers) {
        this.identifiers = identifiers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location id(Long id) {
        this.id = id;
        return this;
    }

    public String getGuid() {
        return this.guid;
    }

    public Location guid(String guid) {
        this.guid = guid;
        return this;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public Location identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public LocationTypeEnum getType() {
        return this.type;
    }

    public Location type(LocationTypeEnum type) {
        this.type = type;
        return this;
    }

    public void setType(LocationTypeEnum type) {
        this.type = type;
    }

    public Organization getManagingOrganization() {
        return this.managingOrganization;
    }

    public Location managingOrganization(Organization organization) {
        this.setManagingOrganization(organization);
        return this;
    }

    public void setManagingOrganization(Organization organization) {
        this.managingOrganization = organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
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
        return "Location{" + "id=" + getId() + ", guid='" + getGuid() + "'" + ", identifier='" + getIdentifier() + "'"
                + ", type='" + getType() + "'" + "}";
    }

    public LocationModel convert(ArrayList<CoreResourceModel> coreResources) {
        if (coreResources.stream().anyMatch(x -> x.getId() == UUID.fromString(this.getGuid()))) {
            return (LocationModel) coreResources.stream().filter(x -> x.getId() == UUID.fromString(this.getGuid())).findFirst().get();
        }
        LocationModel loc = new LocationModel();
        if (this.getGuid() != null) {
            loc.setId(UUID.fromString(this.getGuid()));
        }
        if (this.getIdentifier() != null) {
            IdentifierModel i = new IdentifierModel();
            i.setValue("loc-" + this.getIdentifier());
            loc.addIdentifier(i);
        }
        if (this.getManagingOrganization() != null) {
            loc.setManagingOrganization(this.getManagingOrganization().convert(coreResources));
        }
        if (this.getType() != null) {
            loc.setType(this.getType().convert());
        }
        coreResources.add(loc);
        return loc;
    }

    public static Location convertFrom(LocationModel model) {
        Location loc = new Location();
        if (model.getIdentifiers() != null) {
            loc.setIdentifiers(model.getIdentifiers().stream().map(i -> ReferenceIdentifier.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getId() != null) {
            loc.setGuid(model.getId().toString());
        }
        if (model.getIdentifiers() != null) {
            loc.setIdentifier(model.getIdentifiers().get(0).getValue());
        }
        if (model.getManagingOrganization() != null) {
            loc.setManagingOrganization(Organization.convertFrom(model.getManagingOrganization()));
        }
        if (model.getType() != null) {
            loc.setType(LocationTypeEnum.valueOf(model.getType().name()));
        }
        return loc;
    }
}

package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.OrganizationTypeEnum;
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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.CoreResourceModel;
import platform.fhir_client.models.OrganizationModel;

/**
 * A Organization.
 */
@Entity
@Table(name = "organization")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid")
    private String guid;

    @Column(name = "force_id")
    private String forceId;

    @Column(name = "organization_license")
    private String organizationLicense;

    @Column(name = "base_url")
    private String baseUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "organization_type")
    private OrganizationTypeEnum organizationType;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "name", "organization" }, allowSetters = true)
    private Set<Contact> contacts = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;

    @ManyToMany(mappedBy = "organizationIdentifiers")
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

    public Organization id(Long id) {
        this.id = id;
        return this;
    }

    public String getGuid() {
        return this.guid;
    }

    public Organization guid(String guid) {
        this.guid = guid;
        return this;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getForceId() {
        return this.forceId;
    }

    public Organization forceId(String forceId) {
        this.forceId = forceId;
        return this;
    }

    public void setForceId(String forceId) {
        this.forceId = forceId;
    }

    public String getOrganizationLicense() {
        return this.organizationLicense;
    }

    public Organization organizationLicense(String organizationLicense) {
        this.organizationLicense = organizationLicense;
        return this;
    }

    public void setOrganizationLicense(String organizationLicense) {
        this.organizationLicense = organizationLicense;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public Organization baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public OrganizationTypeEnum getOrganizationType() {
        return this.organizationType;
    }

    public Organization organizationType(OrganizationTypeEnum organizationType) {
        this.organizationType = organizationType;
        return this;
    }

    public void setOrganizationType(OrganizationTypeEnum organizationType) {
        this.organizationType = organizationType;
    }

    public String getName() {
        return this.name;
    }

    public Organization name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Contact> getContacts() {
        return this.contacts;
    }

    public Organization contacts(Set<Contact> contacts) {
        this.setContacts(contacts);
        return this;
    }

    public Organization addContacts(Contact contact) {
        this.contacts.add(contact);
        contact.setOrganization(this);
        return this;
    }

    public Organization removeContacts(Contact contact) {
        this.contacts.remove(contact);
        contact.setOrganization(null);
        return this;
    }

    public void setContacts(Set<Contact> contacts) {
        if (this.contacts != null) {
            this.contacts.forEach(i -> i.setOrganization(null));
        }
        if (contacts != null) {
            contacts.forEach(i -> i.setOrganization(this));
        }
        this.contacts = contacts;
    }

    public Address getAddress() {
        return this.address;
    }

    public Organization address(Address address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organization)) {
            return false;
        }
        return id != null && id.equals(((Organization) o).id);
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
        return "Organization{" + "id=" + getId() + ", guid='" + getGuid() + "'" + ", forceId='" + getForceId() + "'"
                + ", organizationLicense='" + getOrganizationLicense() + "'" + ", baseUrl='" + getBaseUrl() + "'"
                + ", organizationType='" + getOrganizationType() + "'" + ", name='" + getName() + "'" + "}";
    }

    public OrganizationModel convert(ArrayList<CoreResourceModel> coreResources) {
        if (coreResources.stream().anyMatch(x -> x.getId() == UUID.fromString(this.getGuid()))) {
            return (OrganizationModel) coreResources.stream().filter(x -> x.getId() == UUID.fromString(this.getGuid())).findFirst().get();
        }
        OrganizationModel org = new OrganizationModel();
        if (this.getGuid() != null) {
            org.setId(UUID.fromString(this.getGuid()));
        }
        if (this.getBaseUrl() != null) {
            org.setBaseUrl(this.getBaseUrl());
        }
        if (this.getAddress() != null) {
            org.setAddress(this.getAddress().convert());
        }
        if (this.getContacts() != null) {
            org.setContacts(this.getContacts().stream().map(i -> i.convert()).collect(Collectors.toCollection(ArrayList::new)));
        }
        if (this.getForceId() != null) {
            org.setForceId(this.getForceId());
        }
        if (this.getName() != null) {
            org.setName(this.getName());
        }
        if (this.getOrganizationLicense() != null) {
            org.setOrganizationLicense(this.getOrganizationLicense());
        }
        if (this.getOrganizationType() != null) {
            org.setOrganizationType(this.getOrganizationType().convert());
        }
        coreResources.add(org);
        return org;
    }

    public static Organization convertFrom(OrganizationModel model) {
        Organization org = new Organization();
        if (model.getIdentifiers() != null) {
            org.setIdentifiers(model.getIdentifiers().stream().map(i -> ReferenceIdentifier.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getId() != null) {
            org.setGuid(model.getId().toString());
        }
        if (model.getBaseUrl() != null) {
            org.setBaseUrl(model.getBaseUrl());
        }
        if (model.getAddress() != null) {
            org.setAddress(Address.convertFrom(model.getAddress()));
        }
        if (model.getContacts() != null) {
            org.setContacts(model.getContacts().stream().map(i -> Contact.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getForceId() != null) {
            org.setForceId(model.getForceId());
        }
        if (model.getName() != null) {
            org.setName(model.getName());
        }
        if (model.getOrganizationLicense() != null) {
            org.setOrganizationLicense(model.getOrganizationLicense());
        }
        if (model.getOrganizationType() != null) {
            org.setOrganizationType(OrganizationTypeEnum.valueOf(model.getOrganizationType().name()));
        }
        return org;
    }
}

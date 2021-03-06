package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.ContactModel;

/**
 * A Contact.
 */
@Entity
@Table(name = "contact")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "url")
    private String url;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "givens", "patient", "practitioner" }, allowSetters = true)
    private HumanName name;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contacts", "address" }, allowSetters = true)
    private Organization organization;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Contact id(Long id) {
        this.id = id;
        return this;
    }

    public String getPhone() {
        return this.phone;
    }

    public Contact phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public Contact email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return this.mobile;
    }

    public Contact mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUrl() {
        return this.url;
    }

    public Contact url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HumanName getName() {
        return this.name;
    }

    public Contact name(HumanName humanName) {
        this.setName(humanName);
        return this;
    }

    public void setName(HumanName humanName) {
        this.name = humanName;
    }

    public Organization getOrganization() {
        return this.organization;
    }

    public Contact organization(Organization organization) {
        this.setOrganization(organization);
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contact)) {
            return false;
        }
        return id != null && id.equals(((Contact) o).id);
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
        return "Contact{" + "id=" + getId() + ", phone='" + getPhone() + "'" + ", email='" + getEmail() + "'"
                + ", mobile='" + getMobile() + "'" + ", url='" + getUrl() + "'" + "}";
    }

    public ContactModel convert() {
        ContactModel contact = new ContactModel();
        contact.setPhone(this.getPhone());
        contact.setMobile(this.getMobile());
        contact.setEmail(this.getEmail());
        contact.setUrl(this.getUrl());
        return contact;
    }

    public static Contact convertFrom(ContactModel model) {
        Contact contact = new Contact();
        contact.setPhone(model.getPhone());
        contact.setMobile(model.getMobile());
        contact.setEmail(model.getEmail());
        contact.setUrl(model.getUrl());
        return contact;
    }
}

package com.platformsandsolutions.hcpnphiesportal.domain;

import java.io.Serializable;
import java.util.Arrays;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.AddressModel;

/**
 * A Address.
 */
@Entity
@Table(name = "address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "address_line", nullable = false)
    private String addressLine;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "district")
    private String district;

    @Column(name = "state")
    private String state;

    @NotNull
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "country")
    private String country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address id(Long id) {
        this.id = id;
        return this;
    }

    public String getAddressLine() {
        return this.addressLine;
    }

    public Address addressLine(String addressLine) {
        this.addressLine = addressLine;
        return this;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return this.city;
    }

    public Address city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return this.district;
    }

    public Address district(String district) {
        this.district = district;
        return this;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return this.state;
    }

    public Address state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public Address postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return this.country;
    }

    public Address country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return id != null && id.equals(((Address) o).id);
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
        return "Address{" + "id=" + getId() + ", addressLine='" + getAddressLine() + "'" + ", city='" + getCity() + "'"
                + ", district='" + getDistrict() + "'" + ", state='" + getState() + "'" + ", postalCode='"
                + getPostalCode() + "'" + ", country='" + getCountry() + "'" + "}";
    }

    public AddressModel convert() {
        AddressModel address = new AddressModel();
        address.setAddressLine(Arrays.asList(this.getAddressLine()));
        address.setCity(this.getCity());
        address.setDistrict(this.getDistrict());
        address.setState(this.getState());
        address.setPostalCode(this.getPostalCode());
        address.setCountry(this.getCountry());
        return address;
    }

    public static Address convertFrom(AddressModel model) {
        Address address = new Address();
        if (model.getAddressLine() != null) {
            address.setAddressLine(model.getAddressLine().get(0));
        }
        address.setCity(model.getCity());
        address.setDistrict(model.getDistrict());
        address.setState(model.getState());
        address.setPostalCode(model.getPostalCode());
        address.setCountry(model.getCountry());
        return address;
    }
}

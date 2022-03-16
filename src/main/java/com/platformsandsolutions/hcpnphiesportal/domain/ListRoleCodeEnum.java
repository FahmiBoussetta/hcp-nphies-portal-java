package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.RoleCodeEnum;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.utils.Enums;

/**
 * A ListRoleCodeEnum.
 */
@Entity
@Table(name = "list_role_code_enum")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ListRoleCodeEnum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "r")
    private RoleCodeEnum r;

    @ManyToOne
    @JsonIgnoreProperties(value = { "codes", "specialties", "practitioner", "organization" }, allowSetters = true)
    private PractitionerRole practitionerRole;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ListRoleCodeEnum id(Long id) {
        this.id = id;
        return this;
    }

    public RoleCodeEnum getR() {
        return this.r;
    }

    public ListRoleCodeEnum r(RoleCodeEnum r) {
        this.r = r;
        return this;
    }

    public void setR(RoleCodeEnum r) {
        this.r = r;
    }

    public PractitionerRole getPractitionerRole() {
        return this.practitionerRole;
    }

    public ListRoleCodeEnum practitionerRole(PractitionerRole practitionerRole) {
        this.setPractitionerRole(practitionerRole);
        return this;
    }

    public void setPractitionerRole(PractitionerRole practitionerRole) {
        this.practitionerRole = practitionerRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ListRoleCodeEnum)) {
            return false;
        }
        return id != null && id.equals(((ListRoleCodeEnum) o).id);
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
        return "ListRoleCodeEnum{" + "id=" + getId() + ", r='" + getR() + "'" + "}";
    }

    public Enums.RoleCodeEnum convert() {
        return this.getR().convert();
    }

    public static ListRoleCodeEnum convertFrom(platform.fhir_client.utils.Enums.RoleCodeEnum i) {
        ListRoleCodeEnum r = new ListRoleCodeEnum();
        r.setR(RoleCodeEnum.valueOf(i.name()));
        return r;
    }
}

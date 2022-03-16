package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.CoreResourceModel;
import platform.fhir_client.models.PractitionerRoleModel;

/**
 * A PractitionerRole.
 */
@Entity
@Table(name = "practitioner_role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PractitionerRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid")
    private String guid;

    @Column(name = "force_id")
    private String forceId;

    @Column(name = "start")
    private Instant start;

    @Column(name = "end")
    private Instant end;

    @OneToMany(mappedBy = "practitionerRole", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "practitionerRole" }, allowSetters = true)
    private Set<ListRoleCodeEnum> codes = new HashSet<>();

    @OneToMany(mappedBy = "practitionerRole", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "practitionerRole" }, allowSetters = true)
    private Set<ListSpecialtyEnum> specialties = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "names" }, allowSetters = true)
    private Practitioner practitioner;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contacts", "address" }, allowSetters = true)
    private Organization organization;

    @ManyToMany(mappedBy = "practitionerRoleIdentifiers")
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

    public PractitionerRole id(Long id) {
        this.id = id;
        return this;
    }

    public String getGuid() {
        return this.guid;
    }

    public PractitionerRole guid(String guid) {
        this.guid = guid;
        return this;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getForceId() {
        return this.forceId;
    }

    public PractitionerRole forceId(String forceId) {
        this.forceId = forceId;
        return this;
    }

    public void setForceId(String forceId) {
        this.forceId = forceId;
    }

    public Instant getStart() {
        return this.start;
    }

    public PractitionerRole start(Instant start) {
        this.start = start;
        return this;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return this.end;
    }

    public PractitionerRole end(Instant end) {
        this.end = end;
        return this;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public Set<ListRoleCodeEnum> getCodes() {
        return this.codes;
    }

    public PractitionerRole codes(Set<ListRoleCodeEnum> listRoleCodeEnums) {
        this.setCodes(listRoleCodeEnums);
        return this;
    }

    public PractitionerRole addCodes(ListRoleCodeEnum listRoleCodeEnum) {
        this.codes.add(listRoleCodeEnum);
        listRoleCodeEnum.setPractitionerRole(this);
        return this;
    }

    public PractitionerRole removeCodes(ListRoleCodeEnum listRoleCodeEnum) {
        this.codes.remove(listRoleCodeEnum);
        listRoleCodeEnum.setPractitionerRole(null);
        return this;
    }

    public void setCodes(Set<ListRoleCodeEnum> listRoleCodeEnums) {
        if (this.codes != null) {
            this.codes.forEach(i -> i.setPractitionerRole(null));
        }
        if (listRoleCodeEnums != null) {
            listRoleCodeEnums.forEach(i -> i.setPractitionerRole(this));
        }
        this.codes = listRoleCodeEnums;
    }

    public Set<ListSpecialtyEnum> getSpecialties() {
        return this.specialties;
    }

    public PractitionerRole specialties(Set<ListSpecialtyEnum> listSpecialtyEnums) {
        this.setSpecialties(listSpecialtyEnums);
        return this;
    }

    public PractitionerRole addSpecialties(ListSpecialtyEnum listSpecialtyEnum) {
        this.specialties.add(listSpecialtyEnum);
        listSpecialtyEnum.setPractitionerRole(this);
        return this;
    }

    public PractitionerRole removeSpecialties(ListSpecialtyEnum listSpecialtyEnum) {
        this.specialties.remove(listSpecialtyEnum);
        listSpecialtyEnum.setPractitionerRole(null);
        return this;
    }

    public void setSpecialties(Set<ListSpecialtyEnum> listSpecialtyEnums) {
        if (this.specialties != null) {
            this.specialties.forEach(i -> i.setPractitionerRole(null));
        }
        if (listSpecialtyEnums != null) {
            listSpecialtyEnums.forEach(i -> i.setPractitionerRole(this));
        }
        this.specialties = listSpecialtyEnums;
    }

    public Practitioner getPractitioner() {
        return this.practitioner;
    }

    public PractitionerRole practitioner(Practitioner practitioner) {
        this.setPractitioner(practitioner);
        return this;
    }

    public void setPractitioner(Practitioner practitioner) {
        this.practitioner = practitioner;
    }

    public Organization getOrganization() {
        return this.organization;
    }

    public PractitionerRole organization(Organization organization) {
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
        if (!(o instanceof PractitionerRole)) {
            return false;
        }
        return id != null && id.equals(((PractitionerRole) o).id);
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
        return "PractitionerRole{" + "id=" + getId() + ", guid='" + getGuid() + "'" + ", forceId='" + getForceId() + "'"
                + ", start='" + getStart() + "'" + ", end='" + getEnd() + "'" + "}";
    }

    public PractitionerRoleModel convert(ArrayList<CoreResourceModel> coreResources) {
        if (coreResources.stream().anyMatch(x -> x.getId() == UUID.fromString(this.getGuid()))) {
            return (PractitionerRoleModel) coreResources
                .stream()
                .filter(x -> x.getId() == UUID.fromString(this.getGuid()))
                .findFirst()
                .get();
        }
        PractitionerRoleModel p = new PractitionerRoleModel();
        p.setId(UUID.fromString(this.getGuid()));
        p.setPractitioner(this.getPractitioner().convert(coreResources));
        p.setForceId(this.getForceId());
        if (this.getStart() != null) {
            p.setStart(Date.from(this.getStart()));
        }
        if (this.getEnd() != null) {
            p.setEnd(Date.from(this.getEnd()));
        }
        p.setCodes(this.getCodes().stream().map(i -> i.convert()).collect(Collectors.toCollection(ArrayList::new)));
        p.setOrganization(this.getOrganization().convert(coreResources));
        p.setSpecialties(this.getSpecialties().stream().map(i -> i.convert()).collect(Collectors.toCollection(ArrayList::new)));
        coreResources.add(p);
        return p;
    }

    public static PractitionerRole convertFrom(PractitionerRoleModel model) {
        PractitionerRole p = new PractitionerRole();
        if (model.getIdentifiers() != null) {
            p.setIdentifiers(model.getIdentifiers().stream().map(i -> ReferenceIdentifier.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getId() != null) {
            p.setGuid(model.getId().toString());
        }
        if (model.getPractitioner() != null) {
            p.setPractitioner(Practitioner.convertFrom(model.getPractitioner()));
        }
        if (model.getForceId() != null) {
            p.setForceId(model.getForceId());
        }
        if (model.getStart() != null) {
            p.setStart(model.getStart().toInstant());
        }
        if (model.getEnd() != null) {
            p.setEnd(model.getEnd().toInstant());
        }
        if (model.getCodes() != null) {
            p.setCodes(model.getCodes().stream().map(i -> ListRoleCodeEnum.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getOrganization() != null) {
            p.setOrganization(Organization.convertFrom(model.getOrganization()));
        }
        if (model.getSpecialties() != null) {
            p.setSpecialties(model.getSpecialties().stream().map(i -> ListSpecialtyEnum.convertFrom(i)).collect(Collectors.toSet()));
        }

        return p;
    }
}

package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.ActPriorityEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.EncounterClassEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.ServiceTypeEnum;
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
import platform.fhir_client.models.EncounterModel;
import platform.fhir_client.models.IdentifierModel;

/**
 * A Encounter.
 */
@Entity
@Table(name = "encounter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Encounter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid")
    private String guid;

    @Column(name = "force_id")
    private String forceId;

    @Column(name = "identifier")
    private String identifier;

    @Enumerated(EnumType.STRING)
    @Column(name = "encounter_class")
    private EncounterClassEnum encounterClass;

    @Column(name = "start")
    private Instant start;

    @Column(name = "end")
    private Instant end;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type")
    private ServiceTypeEnum serviceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private ActPriorityEnum priority;

    @ManyToOne
    @JsonIgnoreProperties(value = { "names", "contacts", "address" }, allowSetters = true)
    private Patient subject;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "origin" }, allowSetters = true)
    private Hospitalization hospitalization;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contacts", "address" }, allowSetters = true)
    private Organization serviceProvider;

    @ManyToMany(mappedBy = "encounterIdentifiers")
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

    public Encounter id(Long id) {
        this.id = id;
        return this;
    }

    public String getGuid() {
        return this.guid;
    }

    public Encounter guid(String guid) {
        this.guid = guid;
        return this;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getForceId() {
        return this.forceId;
    }

    public Encounter forceId(String forceId) {
        this.forceId = forceId;
        return this;
    }

    public void setForceId(String forceId) {
        this.forceId = forceId;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public Encounter identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public EncounterClassEnum getEncounterClass() {
        return this.encounterClass;
    }

    public Encounter encounterClass(EncounterClassEnum encounterClass) {
        this.encounterClass = encounterClass;
        return this;
    }

    public void setEncounterClass(EncounterClassEnum encounterClass) {
        this.encounterClass = encounterClass;
    }

    public Instant getStart() {
        return this.start;
    }

    public Encounter start(Instant start) {
        this.start = start;
        return this;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return this.end;
    }

    public Encounter end(Instant end) {
        this.end = end;
        return this;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public ServiceTypeEnum getServiceType() {
        return this.serviceType;
    }

    public Encounter serviceType(ServiceTypeEnum serviceType) {
        this.serviceType = serviceType;
        return this;
    }

    public void setServiceType(ServiceTypeEnum serviceType) {
        this.serviceType = serviceType;
    }

    public ActPriorityEnum getPriority() {
        return this.priority;
    }

    public Encounter priority(ActPriorityEnum priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(ActPriorityEnum priority) {
        this.priority = priority;
    }

    public Patient getSubject() {
        return this.subject;
    }

    public Encounter subject(Patient patient) {
        this.setSubject(patient);
        return this;
    }

    public void setSubject(Patient patient) {
        this.subject = patient;
    }

    public Hospitalization getHospitalization() {
        return this.hospitalization;
    }

    public Encounter hospitalization(Hospitalization hospitalization) {
        this.setHospitalization(hospitalization);
        return this;
    }

    public void setHospitalization(Hospitalization hospitalization) {
        this.hospitalization = hospitalization;
    }

    public Organization getServiceProvider() {
        return this.serviceProvider;
    }

    public Encounter serviceProvider(Organization organization) {
        this.setServiceProvider(organization);
        return this;
    }

    public void setServiceProvider(Organization organization) {
        this.serviceProvider = organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Encounter)) {
            return false;
        }
        return id != null && id.equals(((Encounter) o).id);
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
        return "Encounter{" + "id=" + getId() + ", guid='" + getGuid() + "'" + ", forceId='" + getForceId() + "'"
                + ", identifier='" + getIdentifier() + "'" + ", encounterClass='" + getEncounterClass() + "'"
                + ", start='" + getStart() + "'" + ", end='" + getEnd() + "'" + ", serviceType='" + getServiceType()
                + "'" + ", priority='" + getPriority() + "'" + "}";
    }

    public EncounterModel convert(ArrayList<CoreResourceModel> coreResources) {
        if (coreResources.stream().anyMatch(x -> x.getId() == UUID.fromString(this.getGuid()))) {
            return (EncounterModel) coreResources.stream().filter(x -> x.getId() == UUID.fromString(this.getGuid())).findFirst().get();
        }
        EncounterModel e = new EncounterModel();
        if (this.getGuid() != null) {
            e.setId(UUID.fromString(this.getGuid()));
        }
        if (this.getIdentifier() != null) {
            IdentifierModel i = new IdentifierModel();
            i.setValue("enc-" + this.getIdentifier());
            e.addIdentifier(i);
        }
        if (this.getEncounterClass() != null) {
            e.setClass(this.getEncounterClass().convert());
        }
        if (this.getEnd() != null) {
            e.setEnd(Date.from(this.getEnd()));
        }
        if (this.getForceId() != null) {
            e.setForceId(this.getForceId());
        }
        if (this.getHospitalization() != null) {
            e.setHospitalization(this.getHospitalization().convert(coreResources));
        }
        if (this.getPriority() != null) {
            e.setPriority(this.getPriority().convert());
        }
        if (this.getServiceProvider() != null) {
            e.setServiceProvider(this.getServiceProvider().convert(coreResources));
        }
        if (this.getServiceType() != null) {
            e.setServiceType(this.getServiceType().convert());
        }
        if (this.getStart() != null) {
            e.setStart(Date.from(this.getStart()));
        }
        if (this.getSubject() != null) {
            e.setSubject(this.getSubject().convert(coreResources));
        }
        coreResources.add(e);
        return e;
    }

    public static Encounter convertFrom(EncounterModel model) {
        Encounter e = new Encounter();
        if (model.getIdentifiers() != null) {
            e.setIdentifiers(model.getIdentifiers().stream().map(i -> ReferenceIdentifier.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getId() != null) {
            e.setGuid(model.getId().toString());
        }
        if (model.getIdentifiers() != null) {
            e.setIdentifier(model.getIdentifiers().get(0).getValue());
        }
        if (model.getClass_() != null) {
            e.setEncounterClass(EncounterClassEnum.valueOf(model.getClass_().name()));
        }
        if (model.getEnd() != null) {
            e.setEnd(model.getEnd().toInstant());
        }
        if (model.getForceId() != null) {
            e.setForceId(model.getForceId());
        }
        if (model.getHospitalization() != null) {
            e.setHospitalization(Hospitalization.convertFrom(model.getHospitalization()));
        }
        if (model.getPriority() != null) {
            e.setPriority(ActPriorityEnum.valueOf(model.getPriority().name()));
        }
        if (model.getServiceProvider() != null) {
            e.setServiceProvider(Organization.convertFrom(model.getServiceProvider()));
        }
        if (model.getServiceType() != null) {
            e.setServiceType(ServiceTypeEnum.valueOf(model.getServiceType().name()));
        }
        if (model.getStart() != null) {
            e.setStart(model.getStart().toInstant());
        }
        if (model.getSubject() != null) {
            e.setSubject(Patient.convertFrom(model.getSubject()));
        }
        return e;
    }
}

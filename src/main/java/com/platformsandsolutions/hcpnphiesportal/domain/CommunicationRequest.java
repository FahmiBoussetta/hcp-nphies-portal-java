package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CommunicationRequest.
 */
@Entity
@Table(name = "communication_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommunicationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private String value;

    @Column(name = "jhi_system")
    private String system;

    @Column(name = "parsed")
    private String parsed;

    @Column(name = "limit_date")
    private Instant limitDate;

    @OneToMany(mappedBy = "communicationRequest", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contentAttachment", "contentReference", "communication", "communicationRequest" }, allowSetters = true)
    private Set<Payload> payloads = new HashSet<>();

    @OneToMany(mappedBy = "communicationRequest", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "communication", "communicationRequest" }, allowSetters = true)
    private Set<Note> notes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "names", "contacts", "address" }, allowSetters = true)
    private Patient subject;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "errors",
            "relateds",
            "careTeams",
            "supportingInfos",
            "diagnoses",
            "insurances",
            "items",
            "encounter",
            "eligibilityResponse",
            "patient",
            "provider",
            "insurer",
            "prescription",
            "originalPrescription",
            "payee",
            "referral",
            "facility",
            "accident",
        },
        allowSetters = true
    )
    private Claim about;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contacts", "address" }, allowSetters = true)
    private Organization sender;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "basedOns", "mediums", "reasonCodes", "payloads", "notes", "errors", "subject", "sender", "recipient", "about" },
        allowSetters = true
    )
    private Communication communication;

    @ManyToMany(mappedBy = "comReqIdentifiers")
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

    public CommunicationRequest id(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return this.value;
    }

    public CommunicationRequest value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSystem() {
        return this.system;
    }

    public CommunicationRequest system(String system) {
        this.system = system;
        return this;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getParsed() {
        return this.parsed;
    }

    public CommunicationRequest parsed(String parsed) {
        this.parsed = parsed;
        return this;
    }

    public void setParsed(String parsed) {
        this.parsed = parsed;
    }

    public Instant getLimitDate() {
        return this.limitDate;
    }

    public CommunicationRequest limitDate(Instant limitDate) {
        this.limitDate = limitDate;
        return this;
    }

    public void setLimitDate(Instant limitDate) {
        this.limitDate = limitDate;
    }

    public Set<Payload> getPayloads() {
        return this.payloads;
    }

    public CommunicationRequest payloads(Set<Payload> payloads) {
        this.setPayloads(payloads);
        return this;
    }

    public CommunicationRequest addPayload(Payload payload) {
        this.payloads.add(payload);
        payload.setCommunicationRequest(this);
        return this;
    }

    public CommunicationRequest removePayload(Payload payload) {
        this.payloads.remove(payload);
        payload.setCommunicationRequest(null);
        return this;
    }

    public void setPayloads(Set<Payload> payloads) {
        if (this.payloads != null) {
            this.payloads.forEach(i -> i.setCommunicationRequest(null));
        }
        if (payloads != null) {
            payloads.forEach(i -> i.setCommunicationRequest(this));
        }
        this.payloads = payloads;
    }

    public Set<Note> getNotes() {
        return this.notes;
    }

    public CommunicationRequest notes(Set<Note> notes) {
        this.setNotes(notes);
        return this;
    }

    public CommunicationRequest addNote(Note note) {
        this.notes.add(note);
        note.setCommunicationRequest(this);
        return this;
    }

    public CommunicationRequest removeNote(Note note) {
        this.notes.remove(note);
        note.setCommunicationRequest(null);
        return this;
    }

    public void setNotes(Set<Note> notes) {
        if (this.notes != null) {
            this.notes.forEach(i -> i.setCommunicationRequest(null));
        }
        if (notes != null) {
            notes.forEach(i -> i.setCommunicationRequest(this));
        }
        this.notes = notes;
    }

    public Patient getSubject() {
        return this.subject;
    }

    public CommunicationRequest subject(Patient patient) {
        this.setSubject(patient);
        return this;
    }

    public void setSubject(Patient patient) {
        this.subject = patient;
    }

    public Claim getAbout() {
        return this.about;
    }

    public CommunicationRequest about(Claim claim) {
        this.setAbout(claim);
        return this;
    }

    public void setAbout(Claim claim) {
        this.about = claim;
    }

    public Organization getSender() {
        return this.sender;
    }

    public CommunicationRequest sender(Organization organization) {
        this.setSender(organization);
        return this;
    }

    public void setSender(Organization organization) {
        this.sender = organization;
    }

    public Communication getCommunication() {
        return this.communication;
    }

    public CommunicationRequest communication(Communication communication) {
        this.setCommunication(communication);
        return this;
    }

    public void setCommunication(Communication communication) {
        this.communication = communication;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommunicationRequest)) {
            return false;
        }
        return id != null && id.equals(((CommunicationRequest) o).id);
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
        return "CommunicationRequest{" + "id=" + getId() + ", value='" + getValue() + "'" + ", system='" + getSystem()
                + "'" + ", parsed='" + getParsed() + "'" + ", limitDate='" + getLimitDate() + "'" + "}";
    }
}

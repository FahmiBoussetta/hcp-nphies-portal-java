package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Note.
 */
@Entity
@Table(name = "note")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "author")
    private String author;

    @Column(name = "time")
    private Instant time;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "basedOns", "mediums", "reasonCodes", "payloads", "notes", "errors", "subject", "sender", "recipient", "about" },
        allowSetters = true
    )
    private Communication communication;

    @ManyToOne
    @JsonIgnoreProperties(value = { "payloads", "notes", "subject", "about", "sender", "communication" }, allowSetters = true)
    private CommunicationRequest communicationRequest;

    @ManyToOne
    @JsonIgnoreProperties(value = { "errors", "items", "totals" }, allowSetters = true)
    private ClaimResponse claimResponse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Note id(Long id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return this.text;
    }

    public Note text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return this.author;
    }

    public Note author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Instant getTime() {
        return this.time;
    }

    public Note time(Instant time) {
        this.time = time;
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public Communication getCommunication() {
        return this.communication;
    }

    public Note communication(Communication communication) {
        this.setCommunication(communication);
        return this;
    }

    public void setCommunication(Communication communication) {
        this.communication = communication;
    }

    public CommunicationRequest getCommunicationRequest() {
        return this.communicationRequest;
    }

    public Note communicationRequest(CommunicationRequest communicationRequest) {
        this.setCommunicationRequest(communicationRequest);
        return this;
    }

    public void setCommunicationRequest(CommunicationRequest communicationRequest) {
        this.communicationRequest = communicationRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Note)) {
            return false;
        }
        return id != null && id.equals(((Note) o).id);
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
        return "Note{" + "id=" + getId() + ", text='" + getText() + "'" + ", author='" + getAuthor() + "'" + ", time='"
                + getTime() + "'" + "}";
    }
}

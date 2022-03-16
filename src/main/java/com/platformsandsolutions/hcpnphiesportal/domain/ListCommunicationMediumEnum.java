package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.CommunicationMediumEnum;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ListCommunicationMediumEnum.
 */
@Entity
@Table(name = "list_communication_medium_enum")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ListCommunicationMediumEnum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "cm")
    private CommunicationMediumEnum cm;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "basedOns", "mediums", "reasonCodes", "payloads", "notes", "errors", "subject", "sender", "recipient", "about" },
        allowSetters = true
    )
    private Communication communication;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ListCommunicationMediumEnum id(Long id) {
        this.id = id;
        return this;
    }

    public CommunicationMediumEnum getCm() {
        return this.cm;
    }

    public ListCommunicationMediumEnum cm(CommunicationMediumEnum cm) {
        this.cm = cm;
        return this;
    }

    public void setCm(CommunicationMediumEnum cm) {
        this.cm = cm;
    }

    public Communication getCommunication() {
        return this.communication;
    }

    public ListCommunicationMediumEnum communication(Communication communication) {
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
        if (!(o instanceof ListCommunicationMediumEnum)) {
            return false;
        }
        return id != null && id.equals(((ListCommunicationMediumEnum) o).id);
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
        return "ListCommunicationMediumEnum{" + "id=" + getId() + ", cm='" + getCm() + "'" + "}";
    }
}

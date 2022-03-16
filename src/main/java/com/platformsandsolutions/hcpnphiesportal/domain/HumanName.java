package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.HumanNameModel;

/**
 * A HumanName.
 */
@Entity
@Table(name = "human_name")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HumanName implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "family", nullable = false)
    private String family;

    @OneToMany(mappedBy = "human", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "human" }, allowSetters = true)
    private Set<Givens> givens = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "names", "contacts", "address" }, allowSetters = true)
    private Patient patient;

    @ManyToOne
    @JsonIgnoreProperties(value = { "names" }, allowSetters = true)
    private Practitioner practitioner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HumanName id(Long id) {
        this.id = id;
        return this;
    }

    public String getFamily() {
        return this.family;
    }

    public HumanName family(String family) {
        this.family = family;
        return this;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public Set<Givens> getGivens() {
        return this.givens;
    }

    public HumanName givens(Set<Givens> givens) {
        this.setGivens(givens);
        return this;
    }

    public HumanName addGiven(Givens givens) {
        this.givens.add(givens);
        givens.setHuman(this);
        return this;
    }

    public HumanName removeGiven(Givens givens) {
        this.givens.remove(givens);
        givens.setHuman(null);
        return this;
    }

    public void setGivens(Set<Givens> givens) {
        if (this.givens != null) {
            this.givens.forEach(i -> i.setHuman(null));
        }
        if (givens != null) {
            givens.forEach(i -> i.setHuman(this));
        }
        this.givens = givens;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public HumanName patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Practitioner getPractitioner() {
        return this.practitioner;
    }

    public HumanName practitioner(Practitioner practitioner) {
        this.setPractitioner(practitioner);
        return this;
    }

    public void setPractitioner(Practitioner practitioner) {
        this.practitioner = practitioner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HumanName)) {
            return false;
        }
        return id != null && id.equals(((HumanName) o).id);
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
        return "HumanName{" + "id=" + getId() + ", family='" + getFamily() + "'" + "}";
    }

    public HumanNameModel convert() {
        HumanNameModel name = new HumanNameModel();
        name.setFamily(this.getFamily());
        name.setGiven(this.getGivens().stream().map(i -> i.getGiven()).collect(Collectors.toCollection(ArrayList::new)));
        name.setPrefix(this.getGivens().stream().map(i -> i.getPrefix()).collect(Collectors.toCollection(ArrayList::new)));
        name.setSuffix(this.getGivens().stream().map(i -> i.getSuffix()).collect(Collectors.toCollection(ArrayList::new)));
        return name;
    }

    public static HumanName convertFrom(HumanNameModel model) {
        HumanName name = new HumanName();
        name.setFamily(model.getFamily());
        name.setGivens(
            IntStream
                .range(0, model.getGiven().size())
                .mapToObj(
                    index -> {
                        Givens g = new Givens();
                        if (model.getGiven() != null) {
                            g.setGiven(model.getGiven().get(index));
                        }
                        if (model.getPrefix() != null) {
                            g.setPrefix(model.getPrefix().get(index));
                        }
                        if (model.getSuffix() != null) {
                            g.setSuffix(model.getSuffix().get(index));
                        }
                        return g;
                    }
                )
                .collect(Collectors.toSet())
        );
        return name;
    }
}

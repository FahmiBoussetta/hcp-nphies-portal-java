package com.platformsandsolutions.hcpnphiesportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.BodySiteEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.SubSiteEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import platform.fhir_client.models.CoreResourceModel;
import platform.fhir_client.models.ItemModel;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    @Column(name = "is_package")
    private Boolean isPackage;

    @Column(name = "tax", precision = 21, scale = 2)
    private BigDecimal tax;

    @Column(name = "payer_share", precision = 21, scale = 2)
    private BigDecimal payerShare;

    @NotNull
    @Column(name = "patient_share", precision = 21, scale = 2, nullable = false)
    private BigDecimal patientShare;

    @Column(name = "transportation_srca")
    private String transportationSRCA;

    @Column(name = "imaging")
    private String imaging;

    @Column(name = "laboratory")
    private String laboratory;

    @Column(name = "medical_device")
    private String medicalDevice;

    @Column(name = "oral_health_ip")
    private String oralHealthIP;

    @Column(name = "oral_health_op")
    private String oralHealthOP;

    @Column(name = "jhi_procedure")
    private String procedure;

    @Column(name = "services")
    private String services;

    @Column(name = "medication_code")
    private String medicationCode;

    @Column(name = "serviced_date")
    private Instant servicedDate;

    @Column(name = "serviced_date_start")
    private Instant servicedDateStart;

    @Column(name = "serviced_date_end")
    private Instant servicedDateEnd;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "unit_price", nullable = false)
    private Integer unitPrice;

    @Column(name = "factor", precision = 21, scale = 2)
    private BigDecimal factor;

    @Enumerated(EnumType.STRING)
    @Column(name = "body_site")
    private BodySiteEnum bodySite;

    @Enumerated(EnumType.STRING)
    @Column(name = "sub_site")
    private SubSiteEnum subSite;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "item" }, allowSetters = true)
    private Set<DiagnosisSequence> diagnosisSequences = new HashSet<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "item" }, allowSetters = true)
    private Set<CareTeamSequence> careTeamSequences = new HashSet<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "item" }, allowSetters = true)
    private Set<InformationSequence> informationSequences = new HashSet<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "item", "detailItem", "subDetailItem" }, allowSetters = true)
    private Set<ReferenceIdentifier> udis = new HashSet<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "udis", "subDetails", "item" }, allowSetters = true)
    private Set<DetailItem> details = new HashSet<>();

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
    private Claim claim;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public Item sequence(Integer sequence) {
        this.sequence = sequence;
        return this;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Boolean getIsPackage() {
        return this.isPackage;
    }

    public Item isPackage(Boolean isPackage) {
        this.isPackage = isPackage;
        return this;
    }

    public void setIsPackage(Boolean isPackage) {
        this.isPackage = isPackage;
    }

    public BigDecimal getTax() {
        return this.tax;
    }

    public Item tax(BigDecimal tax) {
        this.tax = tax;
        return this;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getPayerShare() {
        return this.payerShare;
    }

    public Item payerShare(BigDecimal payerShare) {
        this.payerShare = payerShare;
        return this;
    }

    public void setPayerShare(BigDecimal payerShare) {
        this.payerShare = payerShare;
    }

    public BigDecimal getPatientShare() {
        return this.patientShare;
    }

    public Item patientShare(BigDecimal patientShare) {
        this.patientShare = patientShare;
        return this;
    }

    public void setPatientShare(BigDecimal patientShare) {
        this.patientShare = patientShare;
    }

    public String getTransportationSRCA() {
        return this.transportationSRCA;
    }

    public Item transportationSRCA(String transportationSRCA) {
        this.transportationSRCA = transportationSRCA;
        return this;
    }

    public void setTransportationSRCA(String transportationSRCA) {
        this.transportationSRCA = transportationSRCA;
    }

    public String getImaging() {
        return this.imaging;
    }

    public Item imaging(String imaging) {
        this.imaging = imaging;
        return this;
    }

    public void setImaging(String imaging) {
        this.imaging = imaging;
    }

    public String getLaboratory() {
        return this.laboratory;
    }

    public Item laboratory(String laboratory) {
        this.laboratory = laboratory;
        return this;
    }

    public void setLaboratory(String laboratory) {
        this.laboratory = laboratory;
    }

    public String getMedicalDevice() {
        return this.medicalDevice;
    }

    public Item medicalDevice(String medicalDevice) {
        this.medicalDevice = medicalDevice;
        return this;
    }

    public void setMedicalDevice(String medicalDevice) {
        this.medicalDevice = medicalDevice;
    }

    public String getOralHealthIP() {
        return this.oralHealthIP;
    }

    public Item oralHealthIP(String oralHealthIP) {
        this.oralHealthIP = oralHealthIP;
        return this;
    }

    public void setOralHealthIP(String oralHealthIP) {
        this.oralHealthIP = oralHealthIP;
    }

    public String getOralHealthOP() {
        return this.oralHealthOP;
    }

    public Item oralHealthOP(String oralHealthOP) {
        this.oralHealthOP = oralHealthOP;
        return this;
    }

    public void setOralHealthOP(String oralHealthOP) {
        this.oralHealthOP = oralHealthOP;
    }

    public String getProcedure() {
        return this.procedure;
    }

    public Item procedure(String procedure) {
        this.procedure = procedure;
        return this;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getServices() {
        return this.services;
    }

    public Item services(String services) {
        this.services = services;
        return this;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getMedicationCode() {
        return this.medicationCode;
    }

    public Item medicationCode(String medicationCode) {
        this.medicationCode = medicationCode;
        return this;
    }

    public void setMedicationCode(String medicationCode) {
        this.medicationCode = medicationCode;
    }

    public Instant getServicedDate() {
        return this.servicedDate;
    }

    public Item servicedDate(Instant servicedDate) {
        this.servicedDate = servicedDate;
        return this;
    }

    public void setServicedDate(Instant servicedDate) {
        this.servicedDate = servicedDate;
    }

    public Instant getServicedDateStart() {
        return this.servicedDateStart;
    }

    public Item servicedDateStart(Instant servicedDateStart) {
        this.servicedDateStart = servicedDateStart;
        return this;
    }

    public void setServicedDateStart(Instant servicedDateStart) {
        this.servicedDateStart = servicedDateStart;
    }

    public Instant getServicedDateEnd() {
        return this.servicedDateEnd;
    }

    public Item servicedDateEnd(Instant servicedDateEnd) {
        this.servicedDateEnd = servicedDateEnd;
        return this;
    }

    public void setServicedDateEnd(Instant servicedDateEnd) {
        this.servicedDateEnd = servicedDateEnd;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Item quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getUnitPrice() {
        return this.unitPrice;
    }

    public Item unitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getFactor() {
        return this.factor;
    }

    public Item factor(BigDecimal factor) {
        this.factor = factor;
        return this;
    }

    public void setFactor(BigDecimal factor) {
        this.factor = factor;
    }

    public BodySiteEnum getBodySite() {
        return this.bodySite;
    }

    public Item bodySite(BodySiteEnum bodySite) {
        this.bodySite = bodySite;
        return this;
    }

    public void setBodySite(BodySiteEnum bodySite) {
        this.bodySite = bodySite;
    }

    public SubSiteEnum getSubSite() {
        return this.subSite;
    }

    public Item subSite(SubSiteEnum subSite) {
        this.subSite = subSite;
        return this;
    }

    public void setSubSite(SubSiteEnum subSite) {
        this.subSite = subSite;
    }

    public Set<DiagnosisSequence> getDiagnosisSequences() {
        return this.diagnosisSequences;
    }

    public Item diagnosisSequences(Set<DiagnosisSequence> diagnosisSequences) {
        this.setDiagnosisSequences(diagnosisSequences);
        return this;
    }

    public Item addDiagnosisSequence(DiagnosisSequence diagnosisSequence) {
        this.diagnosisSequences.add(diagnosisSequence);
        diagnosisSequence.setItem(this);
        return this;
    }

    public Item removeDiagnosisSequence(DiagnosisSequence diagnosisSequence) {
        this.diagnosisSequences.remove(diagnosisSequence);
        diagnosisSequence.setItem(null);
        return this;
    }

    public void setDiagnosisSequences(Set<DiagnosisSequence> diagnosisSequences) {
        if (this.diagnosisSequences != null) {
            this.diagnosisSequences.forEach(i -> i.setItem(null));
        }
        if (diagnosisSequences != null) {
            diagnosisSequences.forEach(i -> i.setItem(this));
        }
        this.diagnosisSequences = diagnosisSequences;
    }

    public Set<CareTeamSequence> getCareTeamSequences() {
        return this.careTeamSequences;
    }

    public Item careTeamSequences(Set<CareTeamSequence> careTeamSequences) {
        this.setCareTeamSequences(careTeamSequences);
        return this;
    }

    public Item addCareTeamSequence(CareTeamSequence careTeamSequence) {
        this.careTeamSequences.add(careTeamSequence);
        careTeamSequence.setItem(this);
        return this;
    }

    public Item removeCareTeamSequence(CareTeamSequence careTeamSequence) {
        this.careTeamSequences.remove(careTeamSequence);
        careTeamSequence.setItem(null);
        return this;
    }

    public void setCareTeamSequences(Set<CareTeamSequence> careTeamSequences) {
        if (this.careTeamSequences != null) {
            this.careTeamSequences.forEach(i -> i.setItem(null));
        }
        if (careTeamSequences != null) {
            careTeamSequences.forEach(i -> i.setItem(this));
        }
        this.careTeamSequences = careTeamSequences;
    }

    public Set<InformationSequence> getInformationSequences() {
        return this.informationSequences;
    }

    public Item informationSequences(Set<InformationSequence> informationSequences) {
        this.setInformationSequences(informationSequences);
        return this;
    }

    public Item addInformationSequence(InformationSequence informationSequence) {
        this.informationSequences.add(informationSequence);
        informationSequence.setItem(this);
        return this;
    }

    public Item removeInformationSequence(InformationSequence informationSequence) {
        this.informationSequences.remove(informationSequence);
        informationSequence.setItem(null);
        return this;
    }

    public void setInformationSequences(Set<InformationSequence> informationSequences) {
        if (this.informationSequences != null) {
            this.informationSequences.forEach(i -> i.setItem(null));
        }
        if (informationSequences != null) {
            informationSequences.forEach(i -> i.setItem(this));
        }
        this.informationSequences = informationSequences;
    }

    public Set<ReferenceIdentifier> getUdis() {
        return this.udis;
    }

    public Item udis(Set<ReferenceIdentifier> referenceIdentifiers) {
        this.setUdis(referenceIdentifiers);
        return this;
    }

    public Item addUdi(ReferenceIdentifier referenceIdentifier) {
        this.udis.add(referenceIdentifier);
        referenceIdentifier.setItem(this);
        return this;
    }

    public Item removeUdi(ReferenceIdentifier referenceIdentifier) {
        this.udis.remove(referenceIdentifier);
        referenceIdentifier.setItem(null);
        return this;
    }

    public void setUdis(Set<ReferenceIdentifier> referenceIdentifiers) {
        if (this.udis != null) {
            this.udis.forEach(i -> i.setItem(null));
        }
        if (referenceIdentifiers != null) {
            referenceIdentifiers.forEach(i -> i.setItem(this));
        }
        this.udis = referenceIdentifiers;
    }

    public Set<DetailItem> getDetails() {
        return this.details;
    }

    public Item details(Set<DetailItem> detailItems) {
        this.setDetails(detailItems);
        return this;
    }

    public Item addDetails(DetailItem detailItem) {
        this.details.add(detailItem);
        detailItem.setItem(this);
        return this;
    }

    public Item removeDetails(DetailItem detailItem) {
        this.details.remove(detailItem);
        detailItem.setItem(null);
        return this;
    }

    public void setDetails(Set<DetailItem> detailItems) {
        if (this.details != null) {
            this.details.forEach(i -> i.setItem(null));
        }
        if (detailItems != null) {
            detailItems.forEach(i -> i.setItem(this));
        }
        this.details = detailItems;
    }

    public Claim getClaim() {
        return this.claim;
    }

    public Item claim(Claim claim) {
        this.setClaim(claim);
        return this;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return id != null && id.equals(((Item) o).id);
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
        return "Item{" + "id=" + getId() + ", sequence=" + getSequence() + ", isPackage='" + getIsPackage() + "'"
                + ", tax=" + getTax() + ", payerShare=" + getPayerShare() + ", patientShare=" + getPatientShare()
                + ", transportationSRCA='" + getTransportationSRCA() + "'" + ", imaging='" + getImaging() + "'"
                + ", laboratory='" + getLaboratory() + "'" + ", medicalDevice='" + getMedicalDevice() + "'"
                + ", oralHealthIP='" + getOralHealthIP() + "'" + ", oralHealthOP='" + getOralHealthOP() + "'"
                + ", procedure='" + getProcedure() + "'" + ", services='" + getServices() + "'" + ", medicationCode='"
                + getMedicationCode() + "'" + ", servicedDate='" + getServicedDate() + "'" + ", servicedDateStart='"
                + getServicedDateStart() + "'" + ", servicedDateEnd='" + getServicedDateEnd() + "'" + ", quantity="
                + getQuantity() + ", unitPrice=" + getUnitPrice() + ", factor=" + getFactor() + ", bodySite='"
                + getBodySite() + "'" + ", subSite='" + getSubSite() + "'" + "}";
    }

    public ItemModel convert(ArrayList<CoreResourceModel> coreResources) {
        ItemModel it = new ItemModel();
        if (this.getBodySite() != null) {
            it.setBodySite(this.getBodySite().convert());
        }
        if (this.getCareTeamSequences() != null) {
            it.setCareTeamSequence(this.getCareTeamSequences().stream().map(i -> i.getCareSeq()).collect(Collectors.toList()));
        }
        if (this.getDetails() != null) {
            it.setDetails(this.getDetails().stream().map(i -> i.convert(coreResources)).collect(Collectors.toList()));
        }
        if (this.getDiagnosisSequences() != null) {
            it.setDiagnosisSequence(this.getDiagnosisSequences().stream().map(i -> i.getDiagSeq()).collect(Collectors.toList()));
        }
        if (this.getFactor() != null) {
            it.setFactor(this.getFactor());
        }
        if (this.getImaging() != null) {
            it.setImaging(this.getImaging());
        }
        if (this.getInformationSequences() != null) {
            it.setInformationSequence(this.getInformationSequences().stream().map(i -> i.getInfSeq()).collect(Collectors.toList()));
        }
        if (this.getLaboratory() != null) {
            it.setLaboratory(this.getLaboratory());
        }
        if (this.getMedicalDevice() != null) {
            it.setMedicalDevice(this.getMedicalDevice());
        }
        if (this.getMedicationCode() != null) {
            it.setMedicationCode(this.getMedicationCode());
        }
        if (this.getOralHealthIP() != null) {
            it.setOralHealthIP(this.getOralHealthIP());
        }
        if (this.getOralHealthOP() != null) {
            it.setOralHealthOP(this.getOralHealthOP());
        }
        if (this.getIsPackage() != null) {
            it.setPackage(this.getIsPackage());
        }
        if (this.getPatientShare() != null) {
            it.setPatientShare(this.getPatientShare());
        }
        if (this.getPayerShare() != null) {
            it.setPayerShare(this.getPayerShare());
        }
        if (this.getProcedure() != null) {
            it.setProcedure(this.getProcedure());
        }
        if (this.getQuantity() != null) {
            it.setQuantity(this.getQuantity());
        }
        if (this.getSequence() != null) {
            it.setSequence(this.getSequence());
        }
        if (this.getServices() != null) {
            it.setService(this.getServices());
        }
        if (this.getServicedDate() != null) {
            it.setServicedDate(Date.from(this.getServicedDate()));
        }
        if (this.getServicedDateEnd() != null) {
            it.setServicedDateEnd(Date.from(this.getServicedDateEnd()));
        }
        if (this.getServicedDateStart() != null) {
            it.setServicedDateStart(Date.from(this.getServicedDateStart()));
        }
        if (this.getSubSite() != null) {
            it.setSubSite(this.getSubSite().convert());
        }
        if (this.getTax() != null) {
            it.setTax(this.getTax());
        }
        if (this.getTransportationSRCA() != null) {
            it.setTransportationSRCA(this.getTransportationSRCA());
        }
        if (this.getUdis() != null) {
            it.setUdi(this.getUdis().stream().map(i -> i.convert()).collect(Collectors.toList()));
        }
        if (this.getUnitPrice() != null) {
            it.setUnitPrice(this.getUnitPrice());
        }
        return it;
    }

    public static Item convertFrom(ItemModel model) {
        Item it = new Item();
        if (model.getBodySite() != null) {
            it.setBodySite(BodySiteEnum.valueOf(model.getBodySite().name()));
        }
        if (model.getCareTeamSequence() != null) {
            it.setCareTeamSequences(
                model.getCareTeamSequence().stream().map(i -> CareTeamSequence.convertFrom(i)).collect(Collectors.toSet())
            );
        }
        if (model.getDetails() != null) {
            it.setDetails(model.getDetails().stream().map(i -> DetailItem.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getDiagnosisSequence() != null) {
            it.setDiagnosisSequences(
                model.getDiagnosisSequence().stream().map(i -> DiagnosisSequence.convertFrom(i)).collect(Collectors.toSet())
            );
        }
        if (model.getFactor() != null) {
            it.setFactor(model.getFactor());
        }
        if (model.getImaging() != null) {
            it.setImaging(model.getImaging());
        }
        if (model.getInformationSequence() != null) {
            it.setInformationSequences(
                model.getInformationSequence().stream().map(i -> InformationSequence.convertFrom(i)).collect(Collectors.toSet())
            );
        }
        if (model.getLaboratory() != null) {
            it.setLaboratory(model.getLaboratory());
        }
        if (model.getMedicalDevice() != null) {
            it.setMedicalDevice(model.getMedicalDevice());
        }
        if (model.getMedicationCode() != null) {
            it.setMedicationCode(model.getMedicationCode());
        }
        if (model.getOralHealthIP() != null) {
            it.setOralHealthIP(model.getOralHealthIP());
        }
        if (model.getOralHealthOP() != null) {
            it.setOralHealthOP(model.getOralHealthOP());
        }
        if (model.getPackage() != null) {
            it.setIsPackage(model.getPackage());
        }
        if (model.getPatientShare() != null) {
            it.setPatientShare(model.getPatientShare());
        }
        if (model.getPayerShare() != null) {
            it.setPayerShare(model.getPayerShare());
        }
        if (model.getProcedure() != null) {
            it.setProcedure(model.getProcedure());
        }
        if (model.getQuantity() != null) {
            it.setQuantity(model.getQuantity());
        }
        if (model.getSequence() != null) {
            it.setSequence(model.getSequence());
        }
        if (model.getService() != null) {
            it.setServices(model.getService());
        }
        if (model.getServicedDate() != null) {
            it.setServicedDate(model.getServicedDate().toInstant());
        }
        if (model.getServicedDateEnd() != null) {
            it.setServicedDateEnd(model.getServicedDateEnd().toInstant());
        }
        if (model.getServicedDateStart() != null) {
            it.setServicedDateStart(model.getServicedDateStart().toInstant());
        }
        if (model.getSubSite() != null) {
            it.setSubSite(SubSiteEnum.valueOf(model.getSubSite().name()));
        }
        if (model.getTax() != null) {
            it.setTax(model.getTax());
        }
        if (model.getTransportationSRCA() != null) {
            it.setTransportationSRCA(model.getTransportationSRCA());
        }
        if (model.getUdi() != null) {
            it.setUdis(model.getUdi().stream().map(i -> ReferenceIdentifier.convertFrom(i)).collect(Collectors.toSet()));
        }
        if (model.getUnitPrice() != null) {
            it.setUnitPrice(model.getUnitPrice());
        }
        return it;
    }
}

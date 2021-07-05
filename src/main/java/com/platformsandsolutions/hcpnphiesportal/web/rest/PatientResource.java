package com.platformsandsolutions.hcpnphiesportal.web.rest;

import com.platformsandsolutions.hcpnphiesportal.domain.HumanName;
import com.platformsandsolutions.hcpnphiesportal.domain.Patient;
import com.platformsandsolutions.hcpnphiesportal.repository.PatientRepository;
import com.platformsandsolutions.hcpnphiesportal.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.platformsandsolutions.hcpnphiesportal.domain.Patient}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PatientResource {

    private final Logger log = LoggerFactory.getLogger(PatientResource.class);

    private static final String ENTITY_NAME = "patient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatientRepository patientRepository;
    private final HumanNameResource humanNameResource;

    public PatientResource(PatientRepository patientRepository, HumanNameResource humanNameResource) {
        this.patientRepository = patientRepository;
        this.humanNameResource = humanNameResource;
    }

    /**
     * {@code POST  /patients} : Create a new patient.
     *
     * @param patient the patient to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patient, or with status {@code 400 (Bad Request)} if the patient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patients")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) throws URISyntaxException {
        log.debug("REST request to save Patient : {}", patient);
        if (patient.getId() != null) {
            throw new BadRequestAlertException("A new patient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        patient.setGuid(UUID.randomUUID().toString());
        Patient result = patientRepository.save(patient);

        for (HumanName subEntity : patient.getNames()) {
            log.debug("create name : {}", subEntity.getId());
            humanNameResource.createHumanName(subEntity);
        }

        return ResponseEntity
            .created(new URI("/api/patients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /patients/:id} : Updates an existing patient.
     *
     * @param id the id of the patient to save.
     * @param patient the patient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patient,
     * or with status {@code 400 (Bad Request)} if the patient is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patients/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable(value = "id", required = false) final Long id, @RequestBody Patient patient)
        throws URISyntaxException {
        log.debug("REST request to update Patient : {}, {}", id, patient);
        if (patient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patient.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (patient.getGuid() == null || patient.getGuid() == "") {
            patient.setGuid(UUID.randomUUID().toString());
        }

        Patient result = patientRepository.save(patient);

        Collection<Long> newList = patient.getNames().stream().map(HumanName::getId).collect(Collectors.toCollection(ArrayList::new));

        List<HumanName> oldList = humanNameResource.getAllHumanNames();
        for (HumanName subEntity : oldList) {
            if (
                subEntity.getPatient() != null &&
                subEntity.getPatient().getId().equals(patient.getId()) &&
                !newList.contains(subEntity.getId())
            ) {
                log.debug("delete name : {}", subEntity.getId());
                humanNameResource.deleteHumanName(subEntity.getId());
            }
        }

        for (HumanName subEntity : patient.getNames()) {
            if (!oldList.contains(subEntity)) {
                log.debug("add name : {}", subEntity.getId());
                humanNameResource.createHumanName(subEntity);
            }
        }

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patient.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /patients/:id} : Partial updates given fields of an existing patient, field will ignore if it is null
     *
     * @param id the id of the patient to save.
     * @param patient the patient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patient,
     * or with status {@code 400 (Bad Request)} if the patient is not valid,
     * or with status {@code 404 (Not Found)} if the patient is not found,
     * or with status {@code 500 (Internal Server Error)} if the patient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/patients/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Patient> partialUpdatePatient(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Patient patient
    ) throws URISyntaxException {
        log.debug("REST request to partial update Patient partially : {}, {}", id, patient);
        if (patient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patient.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Patient> result = patientRepository
            .findById(patient.getId())
            .map(
                existingPatient -> {
                    if (patient.getGuid() != null) {
                        existingPatient.setGuid(patient.getGuid());
                    }
                    if (patient.getForceId() != null) {
                        existingPatient.setForceId(patient.getForceId());
                    }
                    if (patient.getResidentNumber() != null) {
                        existingPatient.setResidentNumber(patient.getResidentNumber());
                    }
                    if (patient.getPassportNumber() != null) {
                        existingPatient.setPassportNumber(patient.getPassportNumber());
                    }
                    if (patient.getNationalHealthId() != null) {
                        existingPatient.setNationalHealthId(patient.getNationalHealthId());
                    }
                    if (patient.getIqama() != null) {
                        existingPatient.setIqama(patient.getIqama());
                    }
                    if (patient.getReligion() != null) {
                        existingPatient.setReligion(patient.getReligion());
                    }
                    if (patient.getGender() != null) {
                        existingPatient.setGender(patient.getGender());
                    }
                    if (patient.getBirthDate() != null) {
                        existingPatient.setBirthDate(patient.getBirthDate());
                    }
                    if (patient.getDeceasedDate() != null) {
                        existingPatient.setDeceasedDate(patient.getDeceasedDate());
                    }
                    if (patient.getMaritalStatus() != null) {
                        existingPatient.setMaritalStatus(patient.getMaritalStatus());
                    }

                    return existingPatient;
                }
            )
            .map(patientRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patient.getId().toString())
        );
    }

    /**
     * {@code GET  /patients} : get all the patients.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patients in body.
     */
    @GetMapping("/patients")
    public List<Patient> getAllPatients() {
        log.debug("REST request to get all Patients");
        return patientRepository.findAll();
    }

    /**
     * {@code GET  /patients/:id} : get the "id" patient.
     *
     * @param id the id of the patient to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patient, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patients/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable Long id) {
        log.debug("REST request to get Patient : {}", id);
        Optional<Patient> patient = patientRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(patient);
    }

    /**
     * {@code DELETE  /patients/:id} : delete the "id" patient.
     *
     * @param id the id of the patient to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/patients/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        log.debug("REST request to delete Patient : {}", id);
        patientRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

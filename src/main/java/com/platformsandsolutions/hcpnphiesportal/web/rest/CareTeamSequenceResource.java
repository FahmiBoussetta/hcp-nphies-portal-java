package com.platformsandsolutions.hcpnphiesportal.web.rest;

import com.platformsandsolutions.hcpnphiesportal.domain.CareTeamSequence;
import com.platformsandsolutions.hcpnphiesportal.repository.CareTeamSequenceRepository;
import com.platformsandsolutions.hcpnphiesportal.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.platformsandsolutions.hcpnphiesportal.domain.CareTeamSequence}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CareTeamSequenceResource {

    private final Logger log = LoggerFactory.getLogger(CareTeamSequenceResource.class);

    private static final String ENTITY_NAME = "careTeamSequence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CareTeamSequenceRepository careTeamSequenceRepository;

    public CareTeamSequenceResource(CareTeamSequenceRepository careTeamSequenceRepository) {
        this.careTeamSequenceRepository = careTeamSequenceRepository;
    }

    /**
     * {@code POST  /care-team-sequences} : Create a new careTeamSequence.
     *
     * @param careTeamSequence the careTeamSequence to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new careTeamSequence, or with status {@code 400 (Bad Request)} if the careTeamSequence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/care-team-sequences")
    public ResponseEntity<CareTeamSequence> createCareTeamSequence(@RequestBody CareTeamSequence careTeamSequence)
        throws URISyntaxException {
        log.debug("REST request to save CareTeamSequence : {}", careTeamSequence);
        if (careTeamSequence.getId() != null) {
            throw new BadRequestAlertException("A new careTeamSequence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CareTeamSequence result = careTeamSequenceRepository.save(careTeamSequence);
        return ResponseEntity
            .created(new URI("/api/care-team-sequences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /care-team-sequences/:id} : Updates an existing careTeamSequence.
     *
     * @param id the id of the careTeamSequence to save.
     * @param careTeamSequence the careTeamSequence to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated careTeamSequence,
     * or with status {@code 400 (Bad Request)} if the careTeamSequence is not valid,
     * or with status {@code 500 (Internal Server Error)} if the careTeamSequence couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/care-team-sequences/{id}")
    public ResponseEntity<CareTeamSequence> updateCareTeamSequence(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CareTeamSequence careTeamSequence
    ) throws URISyntaxException {
        log.debug("REST request to update CareTeamSequence : {}, {}", id, careTeamSequence);
        if (careTeamSequence.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, careTeamSequence.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!careTeamSequenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CareTeamSequence result = careTeamSequenceRepository.save(careTeamSequence);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, careTeamSequence.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /care-team-sequences/:id} : Partial updates given fields of an existing careTeamSequence, field will ignore if it is null
     *
     * @param id the id of the careTeamSequence to save.
     * @param careTeamSequence the careTeamSequence to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated careTeamSequence,
     * or with status {@code 400 (Bad Request)} if the careTeamSequence is not valid,
     * or with status {@code 404 (Not Found)} if the careTeamSequence is not found,
     * or with status {@code 500 (Internal Server Error)} if the careTeamSequence couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/care-team-sequences/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CareTeamSequence> partialUpdateCareTeamSequence(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CareTeamSequence careTeamSequence
    ) throws URISyntaxException {
        log.debug("REST request to partial update CareTeamSequence partially : {}, {}", id, careTeamSequence);
        if (careTeamSequence.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, careTeamSequence.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!careTeamSequenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CareTeamSequence> result = careTeamSequenceRepository
            .findById(careTeamSequence.getId())
            .map(
                existingCareTeamSequence -> {
                    if (careTeamSequence.getCareSeq() != null) {
                        existingCareTeamSequence.setCareSeq(careTeamSequence.getCareSeq());
                    }

                    return existingCareTeamSequence;
                }
            )
            .map(careTeamSequenceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, careTeamSequence.getId().toString())
        );
    }

    /**
     * {@code GET  /care-team-sequences} : get all the careTeamSequences.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of careTeamSequences in body.
     */
    @GetMapping("/care-team-sequences")
    public List<CareTeamSequence> getAllCareTeamSequences() {
        log.debug("REST request to get all CareTeamSequences");
        return careTeamSequenceRepository.findAll();
    }

    /**
     * {@code GET  /care-team-sequences/:id} : get the "id" careTeamSequence.
     *
     * @param id the id of the careTeamSequence to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the careTeamSequence, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/care-team-sequences/{id}")
    public ResponseEntity<CareTeamSequence> getCareTeamSequence(@PathVariable Long id) {
        log.debug("REST request to get CareTeamSequence : {}", id);
        Optional<CareTeamSequence> careTeamSequence = careTeamSequenceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(careTeamSequence);
    }

    /**
     * {@code DELETE  /care-team-sequences/:id} : delete the "id" careTeamSequence.
     *
     * @param id the id of the careTeamSequence to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/care-team-sequences/{id}")
    public ResponseEntity<Void> deleteCareTeamSequence(@PathVariable Long id) {
        log.debug("REST request to delete CareTeamSequence : {}", id);
        careTeamSequenceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

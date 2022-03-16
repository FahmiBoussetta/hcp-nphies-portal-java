package com.platformsandsolutions.hcpnphiesportal.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platformsandsolutions.hcpnphiesportal.domain.Claim;
import com.platformsandsolutions.hcpnphiesportal.domain.ClaimResponse;
import com.platformsandsolutions.hcpnphiesportal.repository.ClaimRepository;
import com.platformsandsolutions.hcpnphiesportal.repository.ClaimResponseRepository;
import com.platformsandsolutions.hcpnphiesportal.web.rest.errors.BadRequestAlertException;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import platform.fhir_client.models.ClaimModel;
import platform.fhir_client.models.ClaimResponseModel;
import platform.fhir_client.models.CoreResourceModel;
import platform.fhir_client.utils.Constants;
import platform.fhir_client.utils.FHIRHelper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.platformsandsolutions.hcpnphiesportal.domain.Claim}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ClaimResource {

    private final Logger log = LoggerFactory.getLogger(ClaimResource.class);

    private static final String ENTITY_NAME = "claim";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClaimRepository claimRepository;
    private final ClaimResponseRepository claimResponseRepository;

    public ClaimResource(ClaimRepository claimRepository, ClaimResponseRepository claimResponseRepository) {
        this.claimRepository = claimRepository;
        this.claimResponseRepository = claimResponseRepository;
    }

    /**
     * {@code POST  /claims} : Create a new claim.
     *
     * @param claim the claim to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new claim, or with status {@code 400 (Bad Request)} if the
     *         claim has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/claims")
    public ResponseEntity<Claim> createClaim(@Valid @RequestBody Claim claim) throws URISyntaxException {
        log.debug("REST request to save Claim : {}", claim);
        if (claim.getId() != null) {
            throw new BadRequestAlertException("A new claim cannot already have an ID", ENTITY_NAME, "idexists");
        }

        claim.setGuid(UUID.randomUUID().toString());

        Claim result = claimRepository.save(claim);
        return ResponseEntity
            .created(new URI("/api/claims/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /claims} : Send a claim.
     *
     * @param id the id of the claim to send.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new claim, or with status {@code 400 (Bad Request)} if the
     *         claim has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/claims/send/{id}")
    public ResponseEntity<ClaimResponse> sendClaim(@PathVariable(value = "id", required = false) final Long id) throws URISyntaxException {
        log.debug("REST request to send Claim : {}", id);
        if (!claimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Claim claim = claimRepository.findOneWithEagerRelationships(id).get();

        FHIRHelper fhirHelper = new FHIRHelper();

        String path = System.getProperty("user.dir");
        String fileName = (new File(path, "PR-FHIR.p12")).getPath();
        try {
            fhirHelper.useServerWithProviderCertificate(
                Constants.SERVER_URL,
                Constants.SERVER_VERSION,
                fileName,
                "puyjdDe4A2dh",
                "http://www.pr-fhir.sa"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<CoreResourceModel> coreResources1 = new ArrayList<CoreResourceModel>();
        coreResources1.add(fhirHelper.getSender());
        ClaimModel cl = claim.convert(coreResources1);
        cl.setProvider(fhirHelper.getSender());

        fhirHelper.initCoreResources(coreResources1);
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        String json2 = "";

        fhirHelper.sendMessage(cl, false);
        try {
            json = mapper.writeValueAsString(cl);
            json2 = mapper.writeValueAsString(fhirHelper.getInputResources().entrySet().iterator().next().getValue());
            log.debug("**********************************************REST request to send fhirHelper : {}", json);
            log.debug("**********************************************REST request to send fhirHelper : {}", json2);
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }
        log.debug("fhirHelper : {}", " i " + fhirHelper.getStep());
        if (fhirHelper.getInputResources().entrySet().iterator().next().getValue().getParsedRequest() != null) {
            claim.setParsed(fhirHelper.getInputResources().entrySet().iterator().next().getValue().getParsedRequest());
            claimRepository.save(claim);
            log.debug(
                "fhirHelper : {}",
                fhirHelper.getInputResources().entrySet().iterator().next().getValue().getParsedRequest() + " i " + fhirHelper.getStep()
            );
        }

        if (fhirHelper.getOutputResources() != null && fhirHelper.getOutputResources().size() > 0) {
            ClaimResponseModel model = fhirHelper.getClaimResponses(true).get(0);
            ClaimResponse claimResponse = ClaimResponse.convertFrom(model);
            ClaimResponse result = claimResponseRepository.save(claimResponse);
            claim.setIsQueued("queued".equals(result.getOutcome().name().toLowerCase()));
            claimRepository.save(claim);
            log.debug(
                "**********************************************REST request to send fhirHelper : {}" +
                ("queued".equals(result.getOutcome().name().toLowerCase())),
                fhirHelper.getOutputResources().entrySet().iterator().next().getValue().getParsedRequest()
            );
            return ResponseEntity
                .created(new URI("/api/claim-response/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, "claimResponse", result.getId().toString()))
                .body(result);
        }

        return null;
    }

    /**
     * {@code POST  /claims} : Send a claim.
     *
     * @param id the id of the claim to send.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new claim, or with status {@code 400 (Bad Request)} if the
     *         claim has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/claims/status/{id}")
    public ResponseEntity<ClaimResponse> statusClaim(@PathVariable(value = "id", required = false) final Long id)
        throws URISyntaxException {
        log.debug("REST request to send Claim : {}", id);
        if (!claimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Claim claim = claimRepository.findOneWithEagerRelationships(id).get();

        FHIRHelper fhirHelper = new FHIRHelper();

        String path = System.getProperty("user.dir");
        String fileName = (new File(path, "PR-FHIR.p12")).getPath();
        try {
            fhirHelper.useServerWithProviderCertificate(
                Constants.SERVER_URL,
                Constants.SERVER_VERSION,
                fileName,
                "puyjdDe4A2dh",
                "http://www.pr-fhir.sa"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<CoreResourceModel> coreResources1 = new ArrayList<CoreResourceModel>();
        coreResources1.add(fhirHelper.getSender());
        ClaimModel cl = claim.convert(coreResources1);
        cl.setProvider(fhirHelper.getSender());

        fhirHelper.initCoreResources(coreResources1);
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        String json2 = "";

        fhirHelper.sendMessage(cl, false);

        ClaimModel c = fhirHelper.getClaims(true).size() > 0 ? fhirHelper.getClaims(true).get(0) : null;
        try {
            json = mapper.writeValueAsString(cl);
            json2 = mapper.writeValueAsString(c);
            log.debug("**********************************************REST request to send fhirHelper : {}", json);
            log.debug("**********************************************REST request to send fhirHelper : {}", json2);
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }

        if (c != null && c.getParsedRequest() != null) {
            claim.setParsed(c.getParsedRequest());
            claimRepository.save(claim);
            log.debug("fhirHelper : {}", c.getParsedRequest() + " i " + fhirHelper.getStep());
        }

        ClaimResponseModel model = fhirHelper.getClaimResponses(true).size() > 0 ? fhirHelper.getClaimResponses(true).get(0) : null;
        if (model != null) {
            log.debug("**********************************************REST request to send fhirHelper : {}", model.getParsedRequest());
            ClaimResponse claimResponse = ClaimResponse.convertFrom(model);
            ClaimResponse result = claimResponseRepository.save(claimResponse);
            claim.setIsQueued("queued".equals(result.getOutcome().name().toLowerCase()));
            claimRepository.save(claim);
            return ResponseEntity
                .created(new URI("/api/claim-response/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, "claimResponse", result.getId().toString()))
                .body(result);
        }

        return null;
    }

    /**
     * {@code PUT  /claims/:id} : Updates an existing claim.
     *
     * @param id    the id of the claim to save.
     * @param claim the claim to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated claim, or with status {@code 400 (Bad Request)} if the
     *         claim is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the claim couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/claims/{id}")
    public ResponseEntity<Claim> updateClaim(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Claim claim)
        throws URISyntaxException {
        log.debug("REST request to update Claim : {}, {}", id, claim);
        if (claim.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, claim.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!claimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Claim result = claimRepository.save(claim);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, claim.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /claims/:id} : Partial updates given fields of an existing
     * claim, field will ignore if it is null
     *
     * @param id    the id of the claim to save.
     * @param claim the claim to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated claim, or with status {@code 400 (Bad Request)} if the
     *         claim is not valid, or with status {@code 404 (Not Found)} if the
     *         claim is not found, or with status
     *         {@code 500 (Internal Server Error)} if the claim couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/claims/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Claim> partialUpdateClaim(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Claim claim
    ) throws URISyntaxException {
        log.debug("REST request to partial update Claim partially : {}, {}", id, claim);
        if (claim.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, claim.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!claimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Claim> result = claimRepository
            .findById(claim.getId())
            .map(
                existingClaim -> {
                    if (claim.getGuid() != null) {
                        existingClaim.setGuid(claim.getGuid());
                    }
                    if (claim.getIsQueued() != null) {
                        existingClaim.setIsQueued(claim.getIsQueued());
                    }
                    if (claim.getParsed() != null) {
                        existingClaim.setParsed(claim.getParsed());
                    }
                    if (claim.getIdentifier() != null) {
                        existingClaim.setIdentifier(claim.getIdentifier());
                    }
                    if (claim.getUse() != null) {
                        existingClaim.setUse(claim.getUse());
                    }
                    if (claim.getType() != null) {
                        existingClaim.setType(claim.getType());
                    }
                    if (claim.getSubType() != null) {
                        existingClaim.setSubType(claim.getSubType());
                    }
                    if (claim.getEligibilityOffline() != null) {
                        existingClaim.setEligibilityOffline(claim.getEligibilityOffline());
                    }
                    if (claim.getEligibilityOfflineDate() != null) {
                        existingClaim.setEligibilityOfflineDate(claim.getEligibilityOfflineDate());
                    }
                    if (claim.getAuthorizationOfflineDate() != null) {
                        existingClaim.setAuthorizationOfflineDate(claim.getAuthorizationOfflineDate());
                    }
                    if (claim.getBillableStart() != null) {
                        existingClaim.setBillableStart(claim.getBillableStart());
                    }
                    if (claim.getBillableEnd() != null) {
                        existingClaim.setBillableEnd(claim.getBillableEnd());
                    }
                    if (claim.getPriority() != null) {
                        existingClaim.setPriority(claim.getPriority());
                    }
                    if (claim.getFundsReserve() != null) {
                        existingClaim.setFundsReserve(claim.getFundsReserve());
                    }

                    return existingClaim;
                }
            )
            .map(claimRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, claim.getId().toString())
        );
    }

    /**
     * {@code GET  /claims} : get all the claims.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of claims in body.
     */
    @GetMapping("/claims")
    public List<Claim> getAllClaims() {
        log.debug("REST request to get all Claims");
        return claimRepository.findAll();
    }

    /**
     * {@code GET  /claims/:id} : get the "id" claim.
     *
     * @param id the id of the claim to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the claim, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/claims/{id}")
    public ResponseEntity<Claim> getClaim(@PathVariable Long id) {
        log.debug("REST request to get Claim : {}", id);
        Optional<Claim> claim = claimRepository.findById(id);
        log.debug("find claino : {}", claim.toString());
        return ResponseUtil.wrapOrNotFound(claim);
    }

    /**
     * {@code DELETE  /claims/:id} : delete the "id" claim.
     *
     * @param id the id of the claim to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/claims/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        log.debug("REST request to delete Claim : {}", id);
        claimRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.platformsandsolutions.hcpnphiesportal.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platformsandsolutions.hcpnphiesportal.domain.CoverageEligibilityRequest;
import com.platformsandsolutions.hcpnphiesportal.domain.CoverageEligibilityResponse;
import com.platformsandsolutions.hcpnphiesportal.domain.ListEligibilityPurposeEnum;
import com.platformsandsolutions.hcpnphiesportal.repository.CoverageEligibilityRequestRepository;
import com.platformsandsolutions.hcpnphiesportal.repository.CoverageEligibilityResponseRepository;
import com.platformsandsolutions.hcpnphiesportal.web.rest.errors.BadRequestAlertException;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import platform.fhir_client.models.CoreResourceModel;
import platform.fhir_client.models.CoverageEligibilityRequestModel;
import platform.fhir_client.models.CoverageEligibilityResponseModel;
import platform.fhir_client.utils.Constants;
import platform.fhir_client.utils.FHIRHelper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.platformsandsolutions.hcpnphiesportal.domain.CoverageEligibilityRequest}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CoverageEligibilityRequestResource {

    private final Logger log = LoggerFactory.getLogger(CoverageEligibilityRequestResource.class);

    private static final String ENTITY_NAME = "coverageEligibilityRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoverageEligibilityRequestRepository coverageEligibilityRequestRepository;
    private final ListEligibilityPurposeEnumResource listEligibilityPurposeEnumResource;
    private final CoverageEligibilityResponseRepository coverageEligibilityResponseRepository;

    public CoverageEligibilityRequestResource(
        CoverageEligibilityRequestRepository coverageEligibilityRequestRepository,
        ListEligibilityPurposeEnumResource listEligibilityPurposeEnumResource,
        CoverageEligibilityResponseRepository coverageEligibilityResponseRepository
    ) {
        this.coverageEligibilityRequestRepository = coverageEligibilityRequestRepository;
        this.listEligibilityPurposeEnumResource = listEligibilityPurposeEnumResource;
        this.coverageEligibilityResponseRepository = coverageEligibilityResponseRepository;
    }

    /**
     * {@code POST  /coverage-eligibility-requests} : Create a new
     * coverageEligibilityRequest.
     *
     * @param coverageEligibilityRequest the coverageEligibilityRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new coverageEligibilityRequest, or with status
     *         {@code 400 (Bad Request)} if the coverageEligibilityRequest has
     *         already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/coverage-eligibility-requests")
    public ResponseEntity<CoverageEligibilityRequest> createCoverageEligibilityRequest(
        @Valid @RequestBody CoverageEligibilityRequest coverageEligibilityRequest
    ) throws URISyntaxException {
        log.debug("REST request to save CoverageEligibilityRequest : {}", coverageEligibilityRequest);
        if (coverageEligibilityRequest.getId() != null) {
            throw new BadRequestAlertException("A new coverageEligibilityRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }

        coverageEligibilityRequest.setGuid(UUID.randomUUID().toString());

        CoverageEligibilityRequest result = coverageEligibilityRequestRepository.save(coverageEligibilityRequest);
        return ResponseEntity
            .created(new URI("/api/coverage-eligibility-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /coverage-eligibility-requests} : Send a
     * coverageEligibilityRequest.
     *
     * @param id the id of the coverageEligibilityRequest to send.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new coverageEligibilityRequest, or with status
     *         {@code 400 (Bad Request)} if the coverageEligibilityRequest has
     *         already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/coverage-eligibility-requests/send/{id}")
    public ResponseEntity<CoverageEligibilityResponse> sendCoverageEligibilityRequest(
        @PathVariable(value = "id", required = false) final Long id
    ) throws URISyntaxException {
        log.debug("REST request to send CoverageEligibilityRequest : {}", id);
        if (!coverageEligibilityRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CoverageEligibilityRequest coverageEligibilityRequest = coverageEligibilityRequestRepository
            .findOneWithEagerRelationships(id)
            .get();

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
        CoverageEligibilityRequestModel covEliReq = coverageEligibilityRequest.convert(coreResources1);
        covEliReq.setProvider(fhirHelper.getSender());

        fhirHelper.initCoreResources(coreResources1);
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        String json2 = "";

        fhirHelper.sendMessage(covEliReq, false);

        try {
            json = mapper.writeValueAsString(coverageEligibilityRequest);
            json2 = mapper.writeValueAsString(covEliReq);
            log.debug("**********************************************REST request to send fhirHelper : {}", json);
            log.debug("**********************************************REST request to send fhirHelper : {}", json2);
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }

        if (fhirHelper.getInputResources().entrySet().iterator().next().getValue().getParsedRequest() != null) {
            coverageEligibilityRequest.setParsed(fhirHelper.getInputResources().entrySet().iterator().next().getValue().getParsedRequest());
            coverageEligibilityRequestRepository.save(coverageEligibilityRequest);
            log.debug(
                "fhirHelper : {}",
                fhirHelper.getInputResources().entrySet().iterator().next().getValue().getParsedRequest() + " i " + fhirHelper.getStep()
            );
        }

        if (fhirHelper.getOutputResources() != null && fhirHelper.getOutputResources().size() > 0) {
            try {
                json = mapper.writeValueAsString(fhirHelper.getOutputResources().entrySet().iterator().next().getValue());
                log.debug("**********************************************REST request to send fhirHelper : {}", json);
            } catch (JsonProcessingException e1) {
                e1.printStackTrace();
            }

            CoverageEligibilityResponseModel model = (CoverageEligibilityResponseModel) fhirHelper
                .getOutputResources()
                .entrySet()
                .iterator()
                .next()
                .getValue();
            CoverageEligibilityResponse coverageEligibilityResponse = CoverageEligibilityResponse.convertFrom(model);
            CoverageEligibilityResponse result = coverageEligibilityResponseRepository.save(coverageEligibilityResponse);
            coverageEligibilityRequest.setCoverageEligibilityResponse(result);
            coverageEligibilityRequestRepository.save(coverageEligibilityRequest);
            return ResponseEntity
                .created(new URI("/api/coverage-eligibility-response/" + result.getId()))
                .headers(
                    HeaderUtil.createEntityCreationAlert(applicationName, true, "coverageEligibilityResponse", result.getId().toString())
                )
                .body(result);
        }

        return null;
    }

    /**
     * {@code PUT  /coverage-eligibility-requests/:id} : Updates an existing
     * coverageEligibilityRequest.
     *
     * @param id                         the id of the coverageEligibilityRequest to
     *                                   save.
     * @param coverageEligibilityRequest the coverageEligibilityRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated coverageEligibilityRequest, or with status
     *         {@code 400 (Bad Request)} if the coverageEligibilityRequest is not
     *         valid, or with status {@code 500 (Internal Server Error)} if the
     *         coverageEligibilityRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/coverage-eligibility-requests/{id}")
    public ResponseEntity<CoverageEligibilityRequest> updateCoverageEligibilityRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CoverageEligibilityRequest coverageEligibilityRequest
    ) throws URISyntaxException {
        log.debug("REST request to update CoverageEligibilityRequest : {}, {}", id, coverageEligibilityRequest);
        if (coverageEligibilityRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coverageEligibilityRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coverageEligibilityRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (coverageEligibilityRequest.getGuid() == null || coverageEligibilityRequest.getGuid() == "") {
            coverageEligibilityRequest.setGuid(UUID.randomUUID().toString());
        }

        Collection<Long> newList = coverageEligibilityRequest
            .getPurposes()
            .stream()
            .map(ListEligibilityPurposeEnum::getId)
            .collect(Collectors.toCollection(ArrayList::new));

        CoverageEligibilityRequest result = coverageEligibilityRequestRepository.save(coverageEligibilityRequest);

        List<ListEligibilityPurposeEnum> oldList = listEligibilityPurposeEnumResource.getAllListEligibilityPurposeEnums();
        for (ListEligibilityPurposeEnum subEntity : oldList) {
            if (
                subEntity.getCoverageEligibilityRequest() != null &&
                subEntity.getCoverageEligibilityRequest().getId().equals(coverageEligibilityRequest.getId()) &&
                !newList.contains(subEntity.getId())
            ) {
                listEligibilityPurposeEnumResource.deleteListEligibilityPurposeEnum(subEntity.getId());
            }
        }

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coverageEligibilityRequest.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /coverage-eligibility-requests/:id} : Partial updates given
     * fields of an existing coverageEligibilityRequest, field will ignore if it is
     * null
     *
     * @param id                         the id of the coverageEligibilityRequest to
     *                                   save.
     * @param coverageEligibilityRequest the coverageEligibilityRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated coverageEligibilityRequest, or with status
     *         {@code 400 (Bad Request)} if the coverageEligibilityRequest is not
     *         valid, or with status {@code 404 (Not Found)} if the
     *         coverageEligibilityRequest is not found, or with status
     *         {@code 500 (Internal Server Error)} if the coverageEligibilityRequest
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/coverage-eligibility-requests/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CoverageEligibilityRequest> partialUpdateCoverageEligibilityRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CoverageEligibilityRequest coverageEligibilityRequest
    ) throws URISyntaxException {
        log.debug("REST request to partial update CoverageEligibilityRequest partially : {}, {}", id, coverageEligibilityRequest);
        if (coverageEligibilityRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coverageEligibilityRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coverageEligibilityRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CoverageEligibilityRequest> result = coverageEligibilityRequestRepository
            .findById(coverageEligibilityRequest.getId())
            .map(
                existingCoverageEligibilityRequest -> {
                    if (coverageEligibilityRequest.getGuid() != null) {
                        existingCoverageEligibilityRequest.setGuid(coverageEligibilityRequest.getGuid());
                    }
                    if (coverageEligibilityRequest.getParsed() != null) {
                        existingCoverageEligibilityRequest.setParsed(coverageEligibilityRequest.getParsed());
                    }
                    if (coverageEligibilityRequest.getPriority() != null) {
                        existingCoverageEligibilityRequest.setPriority(coverageEligibilityRequest.getPriority());
                    }
                    if (coverageEligibilityRequest.getIdentifier() != null) {
                        existingCoverageEligibilityRequest.setIdentifier(coverageEligibilityRequest.getIdentifier());
                    }
                    if (coverageEligibilityRequest.getServicedDate() != null) {
                        existingCoverageEligibilityRequest.setServicedDate(coverageEligibilityRequest.getServicedDate());
                    }
                    if (coverageEligibilityRequest.getServicedDateEnd() != null) {
                        existingCoverageEligibilityRequest.setServicedDateEnd(coverageEligibilityRequest.getServicedDateEnd());
                    }

                    return existingCoverageEligibilityRequest;
                }
            )
            .map(coverageEligibilityRequestRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coverageEligibilityRequest.getId().toString())
        );
    }

    /**
     * {@code GET  /coverage-eligibility-requests} : get all the
     * coverageEligibilityRequests.
     *
     * @param eagerload flag to eager load entities from relationships (This is
     *                  applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of coverageEligibilityRequests in body.
     */
    @GetMapping("/coverage-eligibility-requests")
    public List<CoverageEligibilityRequest> getAllCoverageEligibilityRequests(
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get all CoverageEligibilityRequests");
        return coverageEligibilityRequestRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /coverage-eligibility-requests/:id} : get the "id"
     * coverageEligibilityRequest.
     *
     * @param id the id of the coverageEligibilityRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the coverageEligibilityRequest, or with status
     *         {@code 404 (Not Found)}.
     */
    @GetMapping("/coverage-eligibility-requests/{id}")
    public ResponseEntity<CoverageEligibilityRequest> getCoverageEligibilityRequest(@PathVariable Long id) {
        log.debug("REST request to get CoverageEligibilityRequest : {}", id);
        Optional<CoverageEligibilityRequest> coverageEligibilityRequest = coverageEligibilityRequestRepository.findOneWithEagerRelationships(
            id
        );
        return ResponseUtil.wrapOrNotFound(coverageEligibilityRequest);
    }

    /**
     * {@code DELETE  /coverage-eligibility-requests/:id} : delete the "id"
     * coverageEligibilityRequest.
     *
     * @param id the id of the coverageEligibilityRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/coverage-eligibility-requests/{id}")
    public ResponseEntity<Void> deleteCoverageEligibilityRequest(@PathVariable Long id) {
        log.debug("REST request to delete CoverageEligibilityRequest : {}", id);
        coverageEligibilityRequestRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

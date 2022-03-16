package com.platformsandsolutions.hcpnphiesportal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.platformsandsolutions.hcpnphiesportal.IntegrationTest;
import com.platformsandsolutions.hcpnphiesportal.domain.CareTeamSequence;
import com.platformsandsolutions.hcpnphiesportal.repository.CareTeamSequenceRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CareTeamSequenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CareTeamSequenceResourceIT {

    private static final Integer DEFAULT_CARE_SEQ = 1;
    private static final Integer UPDATED_CARE_SEQ = 2;

    private static final String ENTITY_API_URL = "/api/care-team-sequences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CareTeamSequenceRepository careTeamSequenceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCareTeamSequenceMockMvc;

    private CareTeamSequence careTeamSequence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CareTeamSequence createEntity(EntityManager em) {
        CareTeamSequence careTeamSequence = new CareTeamSequence().careSeq(DEFAULT_CARE_SEQ);
        return careTeamSequence;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CareTeamSequence createUpdatedEntity(EntityManager em) {
        CareTeamSequence careTeamSequence = new CareTeamSequence().careSeq(UPDATED_CARE_SEQ);
        return careTeamSequence;
    }

    @BeforeEach
    public void initTest() {
        careTeamSequence = createEntity(em);
    }

    @Test
    @Transactional
    void createCareTeamSequence() throws Exception {
        int databaseSizeBeforeCreate = careTeamSequenceRepository.findAll().size();
        // Create the CareTeamSequence
        restCareTeamSequenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(careTeamSequence))
            )
            .andExpect(status().isCreated());

        // Validate the CareTeamSequence in the database
        List<CareTeamSequence> careTeamSequenceList = careTeamSequenceRepository.findAll();
        assertThat(careTeamSequenceList).hasSize(databaseSizeBeforeCreate + 1);
        CareTeamSequence testCareTeamSequence = careTeamSequenceList.get(careTeamSequenceList.size() - 1);
        assertThat(testCareTeamSequence.getCareSeq()).isEqualTo(DEFAULT_CARE_SEQ);
    }

    @Test
    @Transactional
    void createCareTeamSequenceWithExistingId() throws Exception {
        // Create the CareTeamSequence with an existing ID
        careTeamSequence.setId(1L);

        int databaseSizeBeforeCreate = careTeamSequenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCareTeamSequenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(careTeamSequence))
            )
            .andExpect(status().isBadRequest());

        // Validate the CareTeamSequence in the database
        List<CareTeamSequence> careTeamSequenceList = careTeamSequenceRepository.findAll();
        assertThat(careTeamSequenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCareTeamSequences() throws Exception {
        // Initialize the database
        careTeamSequenceRepository.saveAndFlush(careTeamSequence);

        // Get all the careTeamSequenceList
        restCareTeamSequenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(careTeamSequence.getId().intValue())))
            .andExpect(jsonPath("$.[*].careSeq").value(hasItem(DEFAULT_CARE_SEQ)));
    }

    @Test
    @Transactional
    void getCareTeamSequence() throws Exception {
        // Initialize the database
        careTeamSequenceRepository.saveAndFlush(careTeamSequence);

        // Get the careTeamSequence
        restCareTeamSequenceMockMvc
            .perform(get(ENTITY_API_URL_ID, careTeamSequence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(careTeamSequence.getId().intValue()))
            .andExpect(jsonPath("$.careSeq").value(DEFAULT_CARE_SEQ));
    }

    @Test
    @Transactional
    void getNonExistingCareTeamSequence() throws Exception {
        // Get the careTeamSequence
        restCareTeamSequenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCareTeamSequence() throws Exception {
        // Initialize the database
        careTeamSequenceRepository.saveAndFlush(careTeamSequence);

        int databaseSizeBeforeUpdate = careTeamSequenceRepository.findAll().size();

        // Update the careTeamSequence
        CareTeamSequence updatedCareTeamSequence = careTeamSequenceRepository.findById(careTeamSequence.getId()).get();
        // Disconnect from session so that the updates on updatedCareTeamSequence are not directly saved in db
        em.detach(updatedCareTeamSequence);
        updatedCareTeamSequence.careSeq(UPDATED_CARE_SEQ);

        restCareTeamSequenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCareTeamSequence.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCareTeamSequence))
            )
            .andExpect(status().isOk());

        // Validate the CareTeamSequence in the database
        List<CareTeamSequence> careTeamSequenceList = careTeamSequenceRepository.findAll();
        assertThat(careTeamSequenceList).hasSize(databaseSizeBeforeUpdate);
        CareTeamSequence testCareTeamSequence = careTeamSequenceList.get(careTeamSequenceList.size() - 1);
        assertThat(testCareTeamSequence.getCareSeq()).isEqualTo(UPDATED_CARE_SEQ);
    }

    @Test
    @Transactional
    void putNonExistingCareTeamSequence() throws Exception {
        int databaseSizeBeforeUpdate = careTeamSequenceRepository.findAll().size();
        careTeamSequence.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCareTeamSequenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, careTeamSequence.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(careTeamSequence))
            )
            .andExpect(status().isBadRequest());

        // Validate the CareTeamSequence in the database
        List<CareTeamSequence> careTeamSequenceList = careTeamSequenceRepository.findAll();
        assertThat(careTeamSequenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCareTeamSequence() throws Exception {
        int databaseSizeBeforeUpdate = careTeamSequenceRepository.findAll().size();
        careTeamSequence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCareTeamSequenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(careTeamSequence))
            )
            .andExpect(status().isBadRequest());

        // Validate the CareTeamSequence in the database
        List<CareTeamSequence> careTeamSequenceList = careTeamSequenceRepository.findAll();
        assertThat(careTeamSequenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCareTeamSequence() throws Exception {
        int databaseSizeBeforeUpdate = careTeamSequenceRepository.findAll().size();
        careTeamSequence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCareTeamSequenceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(careTeamSequence))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CareTeamSequence in the database
        List<CareTeamSequence> careTeamSequenceList = careTeamSequenceRepository.findAll();
        assertThat(careTeamSequenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCareTeamSequenceWithPatch() throws Exception {
        // Initialize the database
        careTeamSequenceRepository.saveAndFlush(careTeamSequence);

        int databaseSizeBeforeUpdate = careTeamSequenceRepository.findAll().size();

        // Update the careTeamSequence using partial update
        CareTeamSequence partialUpdatedCareTeamSequence = new CareTeamSequence();
        partialUpdatedCareTeamSequence.setId(careTeamSequence.getId());

        restCareTeamSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCareTeamSequence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCareTeamSequence))
            )
            .andExpect(status().isOk());

        // Validate the CareTeamSequence in the database
        List<CareTeamSequence> careTeamSequenceList = careTeamSequenceRepository.findAll();
        assertThat(careTeamSequenceList).hasSize(databaseSizeBeforeUpdate);
        CareTeamSequence testCareTeamSequence = careTeamSequenceList.get(careTeamSequenceList.size() - 1);
        assertThat(testCareTeamSequence.getCareSeq()).isEqualTo(DEFAULT_CARE_SEQ);
    }

    @Test
    @Transactional
    void fullUpdateCareTeamSequenceWithPatch() throws Exception {
        // Initialize the database
        careTeamSequenceRepository.saveAndFlush(careTeamSequence);

        int databaseSizeBeforeUpdate = careTeamSequenceRepository.findAll().size();

        // Update the careTeamSequence using partial update
        CareTeamSequence partialUpdatedCareTeamSequence = new CareTeamSequence();
        partialUpdatedCareTeamSequence.setId(careTeamSequence.getId());

        partialUpdatedCareTeamSequence.careSeq(UPDATED_CARE_SEQ);

        restCareTeamSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCareTeamSequence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCareTeamSequence))
            )
            .andExpect(status().isOk());

        // Validate the CareTeamSequence in the database
        List<CareTeamSequence> careTeamSequenceList = careTeamSequenceRepository.findAll();
        assertThat(careTeamSequenceList).hasSize(databaseSizeBeforeUpdate);
        CareTeamSequence testCareTeamSequence = careTeamSequenceList.get(careTeamSequenceList.size() - 1);
        assertThat(testCareTeamSequence.getCareSeq()).isEqualTo(UPDATED_CARE_SEQ);
    }

    @Test
    @Transactional
    void patchNonExistingCareTeamSequence() throws Exception {
        int databaseSizeBeforeUpdate = careTeamSequenceRepository.findAll().size();
        careTeamSequence.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCareTeamSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, careTeamSequence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(careTeamSequence))
            )
            .andExpect(status().isBadRequest());

        // Validate the CareTeamSequence in the database
        List<CareTeamSequence> careTeamSequenceList = careTeamSequenceRepository.findAll();
        assertThat(careTeamSequenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCareTeamSequence() throws Exception {
        int databaseSizeBeforeUpdate = careTeamSequenceRepository.findAll().size();
        careTeamSequence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCareTeamSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(careTeamSequence))
            )
            .andExpect(status().isBadRequest());

        // Validate the CareTeamSequence in the database
        List<CareTeamSequence> careTeamSequenceList = careTeamSequenceRepository.findAll();
        assertThat(careTeamSequenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCareTeamSequence() throws Exception {
        int databaseSizeBeforeUpdate = careTeamSequenceRepository.findAll().size();
        careTeamSequence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCareTeamSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(careTeamSequence))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CareTeamSequence in the database
        List<CareTeamSequence> careTeamSequenceList = careTeamSequenceRepository.findAll();
        assertThat(careTeamSequenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCareTeamSequence() throws Exception {
        // Initialize the database
        careTeamSequenceRepository.saveAndFlush(careTeamSequence);

        int databaseSizeBeforeDelete = careTeamSequenceRepository.findAll().size();

        // Delete the careTeamSequence
        restCareTeamSequenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, careTeamSequence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CareTeamSequence> careTeamSequenceList = careTeamSequenceRepository.findAll();
        assertThat(careTeamSequenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

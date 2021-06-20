package mu.mcbc.mcshares.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import mu.mcbc.mcshares.IntegrationTest;
import mu.mcbc.mcshares.domain.Corporate;
import mu.mcbc.mcshares.repository.CorporateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CorporateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CorporateResourceIT {

    private static final Instant DEFAULT_DATE_INCORP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_INCORP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REG_NO = "AAAAAAAAAA";
    private static final String UPDATED_REG_NO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/corporates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CorporateRepository corporateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCorporateMockMvc;

    private Corporate corporate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Corporate createEntity(EntityManager em) {
        Corporate corporate = new Corporate().dateIncorp(DEFAULT_DATE_INCORP).regNo(DEFAULT_REG_NO);
        return corporate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Corporate createUpdatedEntity(EntityManager em) {
        Corporate corporate = new Corporate().dateIncorp(UPDATED_DATE_INCORP).regNo(UPDATED_REG_NO);
        return corporate;
    }

    @BeforeEach
    public void initTest() {
        corporate = createEntity(em);
    }

    @Test
    @Transactional
    void createCorporate() throws Exception {
        int databaseSizeBeforeCreate = corporateRepository.findAll().size();
        // Create the Corporate
        restCorporateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(corporate)))
            .andExpect(status().isCreated());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeCreate + 1);
        Corporate testCorporate = corporateList.get(corporateList.size() - 1);
        assertThat(testCorporate.getDateIncorp()).isEqualTo(DEFAULT_DATE_INCORP);
        assertThat(testCorporate.getRegNo()).isEqualTo(DEFAULT_REG_NO);
    }

    @Test
    @Transactional
    void createCorporateWithExistingId() throws Exception {
        // Create the Corporate with an existing ID
        corporate.setId("existing_id");

        int databaseSizeBeforeCreate = corporateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorporateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(corporate)))
            .andExpect(status().isBadRequest());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCorporates() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList
        restCorporateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corporate.getId())))
            .andExpect(jsonPath("$.[*].dateIncorp").value(hasItem(DEFAULT_DATE_INCORP.toString())))
            .andExpect(jsonPath("$.[*].regNo").value(hasItem(DEFAULT_REG_NO)));
    }

    @Test
    @Transactional
    void getCorporate() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get the corporate
        restCorporateMockMvc
            .perform(get(ENTITY_API_URL_ID, corporate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(corporate.getId()))
            .andExpect(jsonPath("$.dateIncorp").value(DEFAULT_DATE_INCORP.toString()))
            .andExpect(jsonPath("$.regNo").value(DEFAULT_REG_NO));
    }

    @Test
    @Transactional
    void getNonExistingCorporate() throws Exception {
        // Get the corporate
        restCorporateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCorporate() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();

        // Update the corporate
        Corporate updatedCorporate = corporateRepository.findById(corporate.getId()).get();
        // Disconnect from session so that the updates on updatedCorporate are not directly saved in db
        em.detach(updatedCorporate);
        updatedCorporate.dateIncorp(UPDATED_DATE_INCORP).regNo(UPDATED_REG_NO);

        restCorporateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCorporate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCorporate))
            )
            .andExpect(status().isOk());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
        Corporate testCorporate = corporateList.get(corporateList.size() - 1);
        assertThat(testCorporate.getDateIncorp()).isEqualTo(UPDATED_DATE_INCORP);
        assertThat(testCorporate.getRegNo()).isEqualTo(UPDATED_REG_NO);
    }

    @Test
    @Transactional
    void putNonExistingCorporate() throws Exception {
        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();
        corporate.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorporateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, corporate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corporate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCorporate() throws Exception {
        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();
        corporate.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorporateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corporate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCorporate() throws Exception {
        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();
        corporate.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorporateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(corporate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCorporateWithPatch() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();

        // Update the corporate using partial update
        Corporate partialUpdatedCorporate = new Corporate();
        partialUpdatedCorporate.setId(corporate.getId());

        partialUpdatedCorporate.dateIncorp(UPDATED_DATE_INCORP);

        restCorporateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorporate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCorporate))
            )
            .andExpect(status().isOk());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
        Corporate testCorporate = corporateList.get(corporateList.size() - 1);
        assertThat(testCorporate.getDateIncorp()).isEqualTo(UPDATED_DATE_INCORP);
        assertThat(testCorporate.getRegNo()).isEqualTo(DEFAULT_REG_NO);
    }

    @Test
    @Transactional
    void fullUpdateCorporateWithPatch() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();

        // Update the corporate using partial update
        Corporate partialUpdatedCorporate = new Corporate();
        partialUpdatedCorporate.setId(corporate.getId());

        partialUpdatedCorporate.dateIncorp(UPDATED_DATE_INCORP).regNo(UPDATED_REG_NO);

        restCorporateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorporate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCorporate))
            )
            .andExpect(status().isOk());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
        Corporate testCorporate = corporateList.get(corporateList.size() - 1);
        assertThat(testCorporate.getDateIncorp()).isEqualTo(UPDATED_DATE_INCORP);
        assertThat(testCorporate.getRegNo()).isEqualTo(UPDATED_REG_NO);
    }

    @Test
    @Transactional
    void patchNonExistingCorporate() throws Exception {
        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();
        corporate.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorporateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, corporate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(corporate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCorporate() throws Exception {
        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();
        corporate.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorporateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(corporate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCorporate() throws Exception {
        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();
        corporate.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorporateMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(corporate))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCorporate() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        int databaseSizeBeforeDelete = corporateRepository.findAll().size();

        // Delete the corporate
        restCorporateMockMvc
            .perform(delete(ENTITY_API_URL_ID, corporate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

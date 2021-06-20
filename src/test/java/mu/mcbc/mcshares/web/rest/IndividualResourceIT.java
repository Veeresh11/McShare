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
import mu.mcbc.mcshares.domain.Individual;
import mu.mcbc.mcshares.repository.IndividualRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link IndividualResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IndividualResourceIT {

    private static final Instant DEFAULT_DOB = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DOB = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/individuals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private IndividualRepository individualRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndividualMockMvc;

    private Individual individual;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Individual createEntity(EntityManager em) {
        Individual individual = new Individual().dob(DEFAULT_DOB);
        return individual;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Individual createUpdatedEntity(EntityManager em) {
        Individual individual = new Individual().dob(UPDATED_DOB);
        return individual;
    }

    @BeforeEach
    public void initTest() {
        individual = createEntity(em);
    }

    @Test
    @Transactional
    void createIndividual() throws Exception {
        int databaseSizeBeforeCreate = individualRepository.findAll().size();
        // Create the Individual
        restIndividualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(individual)))
            .andExpect(status().isCreated());

        // Validate the Individual in the database
        List<Individual> individualList = individualRepository.findAll();
        assertThat(individualList).hasSize(databaseSizeBeforeCreate + 1);
        Individual testIndividual = individualList.get(individualList.size() - 1);
        assertThat(testIndividual.getDob()).isEqualTo(DEFAULT_DOB);
    }

    @Test
    @Transactional
    void createIndividualWithExistingId() throws Exception {
        // Create the Individual with an existing ID
        individual.setId("existing_id");

        int databaseSizeBeforeCreate = individualRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndividualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(individual)))
            .andExpect(status().isBadRequest());

        // Validate the Individual in the database
        List<Individual> individualList = individualRepository.findAll();
        assertThat(individualList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIndividuals() throws Exception {
        // Initialize the database
        individualRepository.saveAndFlush(individual);

        // Get all the individualList
        restIndividualMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(individual.getId())))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())));
    }

    @Test
    @Transactional
    void getIndividual() throws Exception {
        // Initialize the database
        individualRepository.saveAndFlush(individual);

        // Get the individual
        restIndividualMockMvc
            .perform(get(ENTITY_API_URL_ID, individual.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(individual.getId()))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()));
    }

    @Test
    @Transactional
    void getNonExistingIndividual() throws Exception {
        // Get the individual
        restIndividualMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIndividual() throws Exception {
        // Initialize the database
        individualRepository.saveAndFlush(individual);

        int databaseSizeBeforeUpdate = individualRepository.findAll().size();

        // Update the individual
        Individual updatedIndividual = individualRepository.findById(individual.getId()).get();
        // Disconnect from session so that the updates on updatedIndividual are not directly saved in db
        em.detach(updatedIndividual);
        updatedIndividual.dob(UPDATED_DOB);

        restIndividualMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIndividual.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIndividual))
            )
            .andExpect(status().isOk());

        // Validate the Individual in the database
        List<Individual> individualList = individualRepository.findAll();
        assertThat(individualList).hasSize(databaseSizeBeforeUpdate);
        Individual testIndividual = individualList.get(individualList.size() - 1);
        assertThat(testIndividual.getDob()).isEqualTo(UPDATED_DOB);
    }

    @Test
    @Transactional
    void putNonExistingIndividual() throws Exception {
        int databaseSizeBeforeUpdate = individualRepository.findAll().size();
        individual.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndividualMockMvc
            .perform(
                put(ENTITY_API_URL_ID, individual.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(individual))
            )
            .andExpect(status().isBadRequest());

        // Validate the Individual in the database
        List<Individual> individualList = individualRepository.findAll();
        assertThat(individualList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIndividual() throws Exception {
        int databaseSizeBeforeUpdate = individualRepository.findAll().size();
        individual.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndividualMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(individual))
            )
            .andExpect(status().isBadRequest());

        // Validate the Individual in the database
        List<Individual> individualList = individualRepository.findAll();
        assertThat(individualList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIndividual() throws Exception {
        int databaseSizeBeforeUpdate = individualRepository.findAll().size();
        individual.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndividualMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(individual)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Individual in the database
        List<Individual> individualList = individualRepository.findAll();
        assertThat(individualList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIndividualWithPatch() throws Exception {
        // Initialize the database
        individualRepository.saveAndFlush(individual);

        int databaseSizeBeforeUpdate = individualRepository.findAll().size();

        // Update the individual using partial update
        Individual partialUpdatedIndividual = new Individual();
        partialUpdatedIndividual.setId(individual.getId());

        restIndividualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndividual.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIndividual))
            )
            .andExpect(status().isOk());

        // Validate the Individual in the database
        List<Individual> individualList = individualRepository.findAll();
        assertThat(individualList).hasSize(databaseSizeBeforeUpdate);
        Individual testIndividual = individualList.get(individualList.size() - 1);
        assertThat(testIndividual.getDob()).isEqualTo(DEFAULT_DOB);
    }

    @Test
    @Transactional
    void fullUpdateIndividualWithPatch() throws Exception {
        // Initialize the database
        individualRepository.saveAndFlush(individual);

        int databaseSizeBeforeUpdate = individualRepository.findAll().size();

        // Update the individual using partial update
        Individual partialUpdatedIndividual = new Individual();
        partialUpdatedIndividual.setId(individual.getId());

        partialUpdatedIndividual.dob(UPDATED_DOB);

        restIndividualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndividual.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIndividual))
            )
            .andExpect(status().isOk());

        // Validate the Individual in the database
        List<Individual> individualList = individualRepository.findAll();
        assertThat(individualList).hasSize(databaseSizeBeforeUpdate);
        Individual testIndividual = individualList.get(individualList.size() - 1);
        assertThat(testIndividual.getDob()).isEqualTo(UPDATED_DOB);
    }

    @Test
    @Transactional
    void patchNonExistingIndividual() throws Exception {
        int databaseSizeBeforeUpdate = individualRepository.findAll().size();
        individual.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndividualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, individual.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(individual))
            )
            .andExpect(status().isBadRequest());

        // Validate the Individual in the database
        List<Individual> individualList = individualRepository.findAll();
        assertThat(individualList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIndividual() throws Exception {
        int databaseSizeBeforeUpdate = individualRepository.findAll().size();
        individual.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndividualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(individual))
            )
            .andExpect(status().isBadRequest());

        // Validate the Individual in the database
        List<Individual> individualList = individualRepository.findAll();
        assertThat(individualList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIndividual() throws Exception {
        int databaseSizeBeforeUpdate = individualRepository.findAll().size();
        individual.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndividualMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(individual))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Individual in the database
        List<Individual> individualList = individualRepository.findAll();
        assertThat(individualList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIndividual() throws Exception {
        // Initialize the database
        individualRepository.saveAndFlush(individual);

        int databaseSizeBeforeDelete = individualRepository.findAll().size();

        // Delete the individual
        restIndividualMockMvc
            .perform(delete(ENTITY_API_URL_ID, individual.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Individual> individualList = individualRepository.findAll();
        assertThat(individualList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

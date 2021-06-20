//package mu.mcbc.mcshares.web.rest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.atomic.AtomicLong;
//import javax.persistence.EntityManager;
//import mu.mcbc.mcshares.IntegrationTest;
//import mu.mcbc.mcshares.domain.Shares;
//import mu.mcbc.mcshares.repository.SharesRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Integration tests for the {@link SharesResource} REST controller.
// */
//@IntegrationTest
//@AutoConfigureMockMvc
//@WithMockUser
//class SharesResourceIT {
//
//    private static final Long DEFAULT_NUM_SHARES = 1L;
//    private static final Long UPDATED_NUM_SHARES = 2L;
//
//    private static final Double DEFAULT_SHARE_PRICE = 1D;
//    private static final Double UPDATED_SHARE_PRICE = 2D;
//
//    private static final String ENTITY_API_URL = "/api/shares";
//    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
//
//    private static Random random = new Random();
//    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
//
//    @Autowired
//    private SharesRepository sharesRepository;
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private MockMvc restSharesMockMvc;
//
//    private Shares shares;
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Shares createEntity(EntityManager em) {
//        Shares shares = new Shares().numShares(DEFAULT_NUM_SHARES).sharePrice(DEFAULT_SHARE_PRICE);
//        return shares;
//    }
//
//    /**
//     * Create an updated entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Shares createUpdatedEntity(EntityManager em) {
//        Shares shares = new Shares().numShares(UPDATED_NUM_SHARES).sharePrice(UPDATED_SHARE_PRICE);
//        return shares;
//    }
//
//    @BeforeEach
//    public void initTest() {
//        shares = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    void createShares() throws Exception {
//        int databaseSizeBeforeCreate = sharesRepository.findAll().size();
//        // Create the Shares
//        restSharesMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shares)))
//            .andExpect(status().isCreated());
//
//        // Validate the Shares in the database
//        List<Shares> sharesList = sharesRepository.findAll();
//        assertThat(sharesList).hasSize(databaseSizeBeforeCreate + 1);
//        Shares testShares = sharesList.get(sharesList.size() - 1);
//        assertThat(testShares.getNumShares()).isEqualTo(DEFAULT_NUM_SHARES);
//        assertThat(testShares.getSharePrice()).isEqualTo(DEFAULT_SHARE_PRICE);
//    }
//
//    @Test
//    @Transactional
//    void createSharesWithExistingId() throws Exception {
//        // Create the Shares with an existing ID
//        shares.setId(1L);
//
//        int databaseSizeBeforeCreate = sharesRepository.findAll().size();
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restSharesMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shares)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Shares in the database
//        List<Shares> sharesList = sharesRepository.findAll();
//        assertThat(sharesList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    void getAllShares() throws Exception {
//        // Initialize the database
//        sharesRepository.saveAndFlush(shares);
//
//        // Get all the sharesList
//        restSharesMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(shares.getId().intValue())))
//            .andExpect(jsonPath("$.[*].numShares").value(hasItem(DEFAULT_NUM_SHARES.intValue())))
//            .andExpect(jsonPath("$.[*].sharePrice").value(hasItem(DEFAULT_SHARE_PRICE.doubleValue())));
//    }
//
//    @Test
//    @Transactional
//    void getShares() throws Exception {
//        // Initialize the database
//        sharesRepository.saveAndFlush(shares);
//
//        // Get the shares
//        restSharesMockMvc
//            .perform(get(ENTITY_API_URL_ID, shares.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.id").value(shares.getId().intValue()))
//            .andExpect(jsonPath("$.numShares").value(DEFAULT_NUM_SHARES.intValue()))
//            .andExpect(jsonPath("$.sharePrice").value(DEFAULT_SHARE_PRICE.doubleValue()));
//    }
//
//    @Test
//    @Transactional
//    void getNonExistingShares() throws Exception {
//        // Get the shares
//        restSharesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    void putNewShares() throws Exception {
//        // Initialize the database
//        sharesRepository.saveAndFlush(shares);
//
//        int databaseSizeBeforeUpdate = sharesRepository.findAll().size();
//
//        // Update the shares
//        Shares updatedShares = sharesRepository.findById(shares.getId()).get();
//        // Disconnect from session so that the updates on updatedShares are not directly saved in db
//        em.detach(updatedShares);
//        updatedShares.numShares(UPDATED_NUM_SHARES).sharePrice(UPDATED_SHARE_PRICE);
//
//        restSharesMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, updatedShares.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(updatedShares))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Shares in the database
//        List<Shares> sharesList = sharesRepository.findAll();
//        assertThat(sharesList).hasSize(databaseSizeBeforeUpdate);
//        Shares testShares = sharesList.get(sharesList.size() - 1);
//        assertThat(testShares.getNumShares()).isEqualTo(UPDATED_NUM_SHARES);
//        assertThat(testShares.getSharePrice()).isEqualTo(UPDATED_SHARE_PRICE);
//    }
//
//    @Test
//    @Transactional
//    void putNonExistingShares() throws Exception {
//        int databaseSizeBeforeUpdate = sharesRepository.findAll().size();
//        shares.setId(count.incrementAndGet());
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restSharesMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, shares.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(shares))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Shares in the database
//        List<Shares> sharesList = sharesRepository.findAll();
//        assertThat(sharesList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithIdMismatchShares() throws Exception {
//        int databaseSizeBeforeUpdate = sharesRepository.findAll().size();
//        shares.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restSharesMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(shares))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Shares in the database
//        List<Shares> sharesList = sharesRepository.findAll();
//        assertThat(sharesList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithMissingIdPathParamShares() throws Exception {
//        int databaseSizeBeforeUpdate = sharesRepository.findAll().size();
//        shares.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restSharesMockMvc
//            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shares)))
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the Shares in the database
//        List<Shares> sharesList = sharesRepository.findAll();
//        assertThat(sharesList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void partialUpdateSharesWithPatch() throws Exception {
//        // Initialize the database
//        sharesRepository.saveAndFlush(shares);
//
//        int databaseSizeBeforeUpdate = sharesRepository.findAll().size();
//
//        // Update the shares using partial update
//        Shares partialUpdatedShares = new Shares();
//        partialUpdatedShares.setId(shares.getId());
//
//        partialUpdatedShares.numShares(UPDATED_NUM_SHARES);
//
//        restSharesMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedShares.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShares))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Shares in the database
//        List<Shares> sharesList = sharesRepository.findAll();
//        assertThat(sharesList).hasSize(databaseSizeBeforeUpdate);
//        Shares testShares = sharesList.get(sharesList.size() - 1);
//        assertThat(testShares.getNumShares()).isEqualTo(UPDATED_NUM_SHARES);
//        assertThat(testShares.getSharePrice()).isEqualTo(DEFAULT_SHARE_PRICE);
//    }
//
//    @Test
//    @Transactional
//    void fullUpdateSharesWithPatch() throws Exception {
//        // Initialize the database
//        sharesRepository.saveAndFlush(shares);
//
//        int databaseSizeBeforeUpdate = sharesRepository.findAll().size();
//
//        // Update the shares using partial update
//        Shares partialUpdatedShares = new Shares();
//        partialUpdatedShares.setId(shares.getId());
//
//        partialUpdatedShares.numShares(UPDATED_NUM_SHARES).sharePrice(UPDATED_SHARE_PRICE);
//
//        restSharesMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedShares.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShares))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Shares in the database
//        List<Shares> sharesList = sharesRepository.findAll();
//        assertThat(sharesList).hasSize(databaseSizeBeforeUpdate);
//        Shares testShares = sharesList.get(sharesList.size() - 1);
//        assertThat(testShares.getNumShares()).isEqualTo(UPDATED_NUM_SHARES);
//        assertThat(testShares.getSharePrice()).isEqualTo(UPDATED_SHARE_PRICE);
//    }
//
//    @Test
//    @Transactional
//    void patchNonExistingShares() throws Exception {
//        int databaseSizeBeforeUpdate = sharesRepository.findAll().size();
//        shares.setId(count.incrementAndGet());
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restSharesMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, shares.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(shares))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Shares in the database
//        List<Shares> sharesList = sharesRepository.findAll();
//        assertThat(sharesList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithIdMismatchShares() throws Exception {
//        int databaseSizeBeforeUpdate = sharesRepository.findAll().size();
//        shares.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restSharesMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(shares))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Shares in the database
//        List<Shares> sharesList = sharesRepository.findAll();
//        assertThat(sharesList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithMissingIdPathParamShares() throws Exception {
//        int databaseSizeBeforeUpdate = sharesRepository.findAll().size();
//        shares.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restSharesMockMvc
//            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(shares)))
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the Shares in the database
//        List<Shares> sharesList = sharesRepository.findAll();
//        assertThat(sharesList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void deleteShares() throws Exception {
//        // Initialize the database
//        sharesRepository.saveAndFlush(shares);
//
//        int databaseSizeBeforeDelete = sharesRepository.findAll().size();
//
//        // Delete the shares
//        restSharesMockMvc
//            .perform(delete(ENTITY_API_URL_ID, shares.getId()).accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isNoContent());
//
//        // Validate the database contains one less item
//        List<Shares> sharesList = sharesRepository.findAll();
//        assertThat(sharesList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//}

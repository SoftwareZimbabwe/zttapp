package org.zt.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
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
import org.zt.IntegrationTest;
import org.zt.domain.LostItem;
import org.zt.repository.LostItemRepository;
import org.zt.service.dto.LostItemDTO;
import org.zt.service.mapper.LostItemMapper;

/**
 * Integration tests for the {@link LostItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LostItemResourceIT {

    private static final LocalDate DEFAULT_DATE_LOST = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_LOST = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/lost-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LostItemRepository lostItemRepository;

    @Autowired
    private LostItemMapper lostItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLostItemMockMvc;

    private LostItem lostItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LostItem createEntity(EntityManager em) {
        LostItem lostItem = new LostItem()
            .dateLost(DEFAULT_DATE_LOST)
            .description(DEFAULT_DESCRIPTION)
            .province(DEFAULT_PROVINCE)
            .itemNumber(DEFAULT_ITEM_NUMBER);
        return lostItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LostItem createUpdatedEntity(EntityManager em) {
        LostItem lostItem = new LostItem()
            .dateLost(UPDATED_DATE_LOST)
            .description(UPDATED_DESCRIPTION)
            .province(UPDATED_PROVINCE)
            .itemNumber(UPDATED_ITEM_NUMBER);
        return lostItem;
    }

    @BeforeEach
    public void initTest() {
        lostItem = createEntity(em);
    }

    @Test
    @Transactional
    void createLostItem() throws Exception {
        int databaseSizeBeforeCreate = lostItemRepository.findAll().size();
        // Create the LostItem
        LostItemDTO lostItemDTO = lostItemMapper.toDto(lostItem);
        restLostItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lostItemDTO)))
            .andExpect(status().isCreated());

        // Validate the LostItem in the database
        List<LostItem> lostItemList = lostItemRepository.findAll();
        assertThat(lostItemList).hasSize(databaseSizeBeforeCreate + 1);
        LostItem testLostItem = lostItemList.get(lostItemList.size() - 1);
        assertThat(testLostItem.getDateLost()).isEqualTo(DEFAULT_DATE_LOST);
        assertThat(testLostItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLostItem.getProvince()).isEqualTo(DEFAULT_PROVINCE);
        assertThat(testLostItem.getItemNumber()).isEqualTo(DEFAULT_ITEM_NUMBER);
    }

    @Test
    @Transactional
    void createLostItemWithExistingId() throws Exception {
        // Create the LostItem with an existing ID
        lostItem.setId(1L);
        LostItemDTO lostItemDTO = lostItemMapper.toDto(lostItem);

        int databaseSizeBeforeCreate = lostItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLostItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lostItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LostItem in the database
        List<LostItem> lostItemList = lostItemRepository.findAll();
        assertThat(lostItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLostItems() throws Exception {
        // Initialize the database
        lostItemRepository.saveAndFlush(lostItem);

        // Get all the lostItemList
        restLostItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lostItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateLost").value(hasItem(DEFAULT_DATE_LOST.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE)))
            .andExpect(jsonPath("$.[*].itemNumber").value(hasItem(DEFAULT_ITEM_NUMBER)));
    }

    @Test
    @Transactional
    void getLostItem() throws Exception {
        // Initialize the database
        lostItemRepository.saveAndFlush(lostItem);

        // Get the lostItem
        restLostItemMockMvc
            .perform(get(ENTITY_API_URL_ID, lostItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lostItem.getId().intValue()))
            .andExpect(jsonPath("$.dateLost").value(DEFAULT_DATE_LOST.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.province").value(DEFAULT_PROVINCE))
            .andExpect(jsonPath("$.itemNumber").value(DEFAULT_ITEM_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingLostItem() throws Exception {
        // Get the lostItem
        restLostItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLostItem() throws Exception {
        // Initialize the database
        lostItemRepository.saveAndFlush(lostItem);

        int databaseSizeBeforeUpdate = lostItemRepository.findAll().size();

        // Update the lostItem
        LostItem updatedLostItem = lostItemRepository.findById(lostItem.getId()).get();
        // Disconnect from session so that the updates on updatedLostItem are not directly saved in db
        em.detach(updatedLostItem);
        updatedLostItem
            .dateLost(UPDATED_DATE_LOST)
            .description(UPDATED_DESCRIPTION)
            .province(UPDATED_PROVINCE)
            .itemNumber(UPDATED_ITEM_NUMBER);
        LostItemDTO lostItemDTO = lostItemMapper.toDto(updatedLostItem);

        restLostItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lostItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lostItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the LostItem in the database
        List<LostItem> lostItemList = lostItemRepository.findAll();
        assertThat(lostItemList).hasSize(databaseSizeBeforeUpdate);
        LostItem testLostItem = lostItemList.get(lostItemList.size() - 1);
        assertThat(testLostItem.getDateLost()).isEqualTo(UPDATED_DATE_LOST);
        assertThat(testLostItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLostItem.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(testLostItem.getItemNumber()).isEqualTo(UPDATED_ITEM_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingLostItem() throws Exception {
        int databaseSizeBeforeUpdate = lostItemRepository.findAll().size();
        lostItem.setId(count.incrementAndGet());

        // Create the LostItem
        LostItemDTO lostItemDTO = lostItemMapper.toDto(lostItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLostItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lostItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lostItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LostItem in the database
        List<LostItem> lostItemList = lostItemRepository.findAll();
        assertThat(lostItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLostItem() throws Exception {
        int databaseSizeBeforeUpdate = lostItemRepository.findAll().size();
        lostItem.setId(count.incrementAndGet());

        // Create the LostItem
        LostItemDTO lostItemDTO = lostItemMapper.toDto(lostItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLostItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lostItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LostItem in the database
        List<LostItem> lostItemList = lostItemRepository.findAll();
        assertThat(lostItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLostItem() throws Exception {
        int databaseSizeBeforeUpdate = lostItemRepository.findAll().size();
        lostItem.setId(count.incrementAndGet());

        // Create the LostItem
        LostItemDTO lostItemDTO = lostItemMapper.toDto(lostItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLostItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lostItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LostItem in the database
        List<LostItem> lostItemList = lostItemRepository.findAll();
        assertThat(lostItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLostItemWithPatch() throws Exception {
        // Initialize the database
        lostItemRepository.saveAndFlush(lostItem);

        int databaseSizeBeforeUpdate = lostItemRepository.findAll().size();

        // Update the lostItem using partial update
        LostItem partialUpdatedLostItem = new LostItem();
        partialUpdatedLostItem.setId(lostItem.getId());

        partialUpdatedLostItem.dateLost(UPDATED_DATE_LOST).province(UPDATED_PROVINCE);

        restLostItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLostItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLostItem))
            )
            .andExpect(status().isOk());

        // Validate the LostItem in the database
        List<LostItem> lostItemList = lostItemRepository.findAll();
        assertThat(lostItemList).hasSize(databaseSizeBeforeUpdate);
        LostItem testLostItem = lostItemList.get(lostItemList.size() - 1);
        assertThat(testLostItem.getDateLost()).isEqualTo(UPDATED_DATE_LOST);
        assertThat(testLostItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLostItem.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(testLostItem.getItemNumber()).isEqualTo(DEFAULT_ITEM_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateLostItemWithPatch() throws Exception {
        // Initialize the database
        lostItemRepository.saveAndFlush(lostItem);

        int databaseSizeBeforeUpdate = lostItemRepository.findAll().size();

        // Update the lostItem using partial update
        LostItem partialUpdatedLostItem = new LostItem();
        partialUpdatedLostItem.setId(lostItem.getId());

        partialUpdatedLostItem
            .dateLost(UPDATED_DATE_LOST)
            .description(UPDATED_DESCRIPTION)
            .province(UPDATED_PROVINCE)
            .itemNumber(UPDATED_ITEM_NUMBER);

        restLostItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLostItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLostItem))
            )
            .andExpect(status().isOk());

        // Validate the LostItem in the database
        List<LostItem> lostItemList = lostItemRepository.findAll();
        assertThat(lostItemList).hasSize(databaseSizeBeforeUpdate);
        LostItem testLostItem = lostItemList.get(lostItemList.size() - 1);
        assertThat(testLostItem.getDateLost()).isEqualTo(UPDATED_DATE_LOST);
        assertThat(testLostItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLostItem.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(testLostItem.getItemNumber()).isEqualTo(UPDATED_ITEM_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingLostItem() throws Exception {
        int databaseSizeBeforeUpdate = lostItemRepository.findAll().size();
        lostItem.setId(count.incrementAndGet());

        // Create the LostItem
        LostItemDTO lostItemDTO = lostItemMapper.toDto(lostItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLostItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lostItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lostItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LostItem in the database
        List<LostItem> lostItemList = lostItemRepository.findAll();
        assertThat(lostItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLostItem() throws Exception {
        int databaseSizeBeforeUpdate = lostItemRepository.findAll().size();
        lostItem.setId(count.incrementAndGet());

        // Create the LostItem
        LostItemDTO lostItemDTO = lostItemMapper.toDto(lostItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLostItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lostItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LostItem in the database
        List<LostItem> lostItemList = lostItemRepository.findAll();
        assertThat(lostItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLostItem() throws Exception {
        int databaseSizeBeforeUpdate = lostItemRepository.findAll().size();
        lostItem.setId(count.incrementAndGet());

        // Create the LostItem
        LostItemDTO lostItemDTO = lostItemMapper.toDto(lostItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLostItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lostItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LostItem in the database
        List<LostItem> lostItemList = lostItemRepository.findAll();
        assertThat(lostItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLostItem() throws Exception {
        // Initialize the database
        lostItemRepository.saveAndFlush(lostItem);

        int databaseSizeBeforeDelete = lostItemRepository.findAll().size();

        // Delete the lostItem
        restLostItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, lostItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LostItem> lostItemList = lostItemRepository.findAll();
        assertThat(lostItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

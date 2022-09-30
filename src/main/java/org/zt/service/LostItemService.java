package org.zt.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zt.service.dto.LostItemDTO;

/**
 * Service Interface for managing {@link org.zt.domain.LostItem}.
 */
public interface LostItemService {
    /**
     * Save a lostItem.
     *
     * @param lostItemDTO the entity to save.
     * @return the persisted entity.
     */
    LostItemDTO save(LostItemDTO lostItemDTO);

    /**
     * Updates a lostItem.
     *
     * @param lostItemDTO the entity to update.
     * @return the persisted entity.
     */
    LostItemDTO update(LostItemDTO lostItemDTO);

    /**
     * Partially updates a lostItem.
     *
     * @param lostItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LostItemDTO> partialUpdate(LostItemDTO lostItemDTO);

    /**
     * Get all the lostItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LostItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" lostItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LostItemDTO> findOne(Long id);

    /**
     * Delete the "id" lostItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

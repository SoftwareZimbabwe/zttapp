package org.zt.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.zt.domain.LostItem;

/**
 * Spring Data JPA repository for the LostItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LostItemRepository extends JpaRepository<LostItem, Long> {}

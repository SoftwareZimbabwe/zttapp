package org.zt.service.mapper;

import org.mapstruct.*;
import org.zt.domain.LostItem;
import org.zt.service.dto.LostItemDTO;

/**
 * Mapper for the entity {@link LostItem} and its DTO {@link LostItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface LostItemMapper extends EntityMapper<LostItemDTO, LostItem> {}

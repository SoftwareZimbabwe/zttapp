package org.zt.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.zt.domain.LostItem;
import org.zt.repository.LostItemRepository;
import org.zt.service.LostItemService;
import org.zt.service.dto.LostItemDTO;
import org.zt.service.mapper.LostItemMapper;

/**
 * Service Implementation for managing {@link LostItem}.
 */
@Service
@Transactional
public class LostItemServiceImpl implements LostItemService {

    private final Logger log = LoggerFactory.getLogger(LostItemServiceImpl.class);

    private final LostItemRepository lostItemRepository;

    private final LostItemMapper lostItemMapper;

    public LostItemServiceImpl(LostItemRepository lostItemRepository, LostItemMapper lostItemMapper) {
        this.lostItemRepository = lostItemRepository;
        this.lostItemMapper = lostItemMapper;
    }

    @Override
    public LostItemDTO save(LostItemDTO lostItemDTO) {
        log.debug("Request to save LostItem : {}", lostItemDTO);
        lostItemDTO.setItemNumber(getRandomNumbeString());
        LostItem lostItem = lostItemMapper.toEntity(lostItemDTO);
        lostItem = lostItemRepository.save(lostItem);
        return lostItemMapper.toDto(lostItem);
    }

    private String getRandomNumbeString() {
        try {
            String uri = "http://www.randomnumberapi.com/api/v1.0/random?min=100&max=1000&count=1";
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(uri, String.class);
            return result.replace("[", "").replace("]", "");
        } catch (Exception e) {
            return "Local-" + Math.random();
        }
    }

    @Override
    public LostItemDTO update(LostItemDTO lostItemDTO) {
        log.debug("Request to update LostItem : {}", lostItemDTO);
        LostItem lostItem = lostItemMapper.toEntity(lostItemDTO);
        lostItem = lostItemRepository.save(lostItem);
        return lostItemMapper.toDto(lostItem);
    }

    @Override
    public Optional<LostItemDTO> partialUpdate(LostItemDTO lostItemDTO) {
        log.debug("Request to partially update LostItem : {}", lostItemDTO);

        return lostItemRepository
            .findById(lostItemDTO.getId())
            .map(existingLostItem -> {
                lostItemMapper.partialUpdate(existingLostItem, lostItemDTO);

                return existingLostItem;
            })
            .map(lostItemRepository::save)
            .map(lostItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LostItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LostItems");
        return lostItemRepository.findAll(pageable).map(lostItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LostItemDTO> findOne(Long id) {
        log.debug("Request to get LostItem : {}", id);
        return lostItemRepository.findById(id).map(lostItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LostItem : {}", id);
        lostItemRepository.deleteById(id);
    }
}

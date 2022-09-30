package org.zt.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.zt.repository.LostItemRepository;
import org.zt.service.LostItemService;
import org.zt.service.dto.LostItemDTO;
import org.zt.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.zt.domain.LostItem}.
 */
@RestController
@RequestMapping("/api")
public class LostItemResource {

    private final Logger log = LoggerFactory.getLogger(LostItemResource.class);

    private static final String ENTITY_NAME = "lostItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LostItemService lostItemService;

    private final LostItemRepository lostItemRepository;

    public LostItemResource(LostItemService lostItemService, LostItemRepository lostItemRepository) {
        this.lostItemService = lostItemService;
        this.lostItemRepository = lostItemRepository;
    }

    /**
     * {@code POST  /lost-items} : Create a new lostItem.
     *
     * @param lostItemDTO the lostItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lostItemDTO, or with status {@code 400 (Bad Request)} if the lostItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lost-items")
    public ResponseEntity<LostItemDTO> createLostItem(@RequestBody LostItemDTO lostItemDTO) throws URISyntaxException {
        log.debug("REST request to save LostItem : {}", lostItemDTO);
        if (lostItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new lostItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LostItemDTO result = lostItemService.save(lostItemDTO);
        return ResponseEntity
            .created(new URI("/api/lost-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lost-items/:id} : Updates an existing lostItem.
     *
     * @param id the id of the lostItemDTO to save.
     * @param lostItemDTO the lostItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lostItemDTO,
     * or with status {@code 400 (Bad Request)} if the lostItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lostItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lost-items/{id}")
    public ResponseEntity<LostItemDTO> updateLostItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LostItemDTO lostItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LostItem : {}, {}", id, lostItemDTO);
        if (lostItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lostItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lostItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LostItemDTO result = lostItemService.update(lostItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lostItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lost-items/:id} : Partial updates given fields of an existing lostItem, field will ignore if it is null
     *
     * @param id the id of the lostItemDTO to save.
     * @param lostItemDTO the lostItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lostItemDTO,
     * or with status {@code 400 (Bad Request)} if the lostItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lostItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lostItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lost-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LostItemDTO> partialUpdateLostItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LostItemDTO lostItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LostItem partially : {}, {}", id, lostItemDTO);
        if (lostItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lostItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lostItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LostItemDTO> result = lostItemService.partialUpdate(lostItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lostItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lost-items} : get all the lostItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lostItems in body.
     */
    @GetMapping("/lost-items")
    public ResponseEntity<List<LostItemDTO>> getAllLostItems(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of LostItems");
        Page<LostItemDTO> page = lostItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lost-items/:id} : get the "id" lostItem.
     *
     * @param id the id of the lostItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lostItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lost-items/{id}")
    public ResponseEntity<LostItemDTO> getLostItem(@PathVariable Long id) {
        log.debug("REST request to get LostItem : {}", id);
        Optional<LostItemDTO> lostItemDTO = lostItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lostItemDTO);
    }

    /**
     * {@code DELETE  /lost-items/:id} : delete the "id" lostItem.
     *
     * @param id the id of the lostItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lost-items/{id}")
    public ResponseEntity<Void> deleteLostItem(@PathVariable Long id) {
        log.debug("REST request to delete LostItem : {}", id);
        lostItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.transformuk.hee.tis.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.profile.domain.Immigration;
import com.transformuk.hee.tis.profile.repository.ImmigrationRepository;
import com.transformuk.hee.tis.profile.service.dto.ImmigrationDTO;
import com.transformuk.hee.tis.profile.service.mapper.ImmigrationMapper;
import com.transformuk.hee.tis.profile.web.rest.util.HeaderUtil;
import com.transformuk.hee.tis.profile.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Immigration.
 */
@RestController
@RequestMapping("/api")
public class ImmigrationResource {

  private static final String ENTITY_NAME = "immigration";
  private final Logger log = LoggerFactory.getLogger(ImmigrationResource.class);
  private final ImmigrationRepository immigrationRepository;

  private final ImmigrationMapper immigrationMapper;

  public ImmigrationResource(ImmigrationRepository immigrationRepository, ImmigrationMapper immigrationMapper) {
    this.immigrationRepository = immigrationRepository;
    this.immigrationMapper = immigrationMapper;
  }

  /**
   * POST  /immigrations : Create a new immigration.
   *
   * @param immigrationDTO the immigrationDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new immigrationDTO, or with status 400 (Bad Request) if the immigration has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/immigrations")
  @Timed
  @PreAuthorize("hasAuthority('profile:add:modify:entities')")
  public ResponseEntity<ImmigrationDTO> createImmigration(@Valid @RequestBody ImmigrationDTO immigrationDTO) throws URISyntaxException {
    log.debug("REST request to save Immigration : {}", immigrationDTO);
    if (immigrationDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new immigration cannot already have an ID")).body(null);
    }
    Immigration immigration = immigrationMapper.immigrationDTOToImmigration(immigrationDTO);
    immigration = immigrationRepository.save(immigration);
    ImmigrationDTO result = immigrationMapper.immigrationToImmigrationDTO(immigration);
    return ResponseEntity.created(new URI("/api/immigrations/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /immigrations : Updates an existing immigration.
   *
   * @param immigrationDTO the immigrationDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated immigrationDTO,
   * or with status 400 (Bad Request) if the immigrationDTO is not valid,
   * or with status 500 (Internal Server Error) if the immigrationDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/immigrations")
  @Timed
  @PreAuthorize("hasAuthority('profile:add:modify:entities')")
  public ResponseEntity<ImmigrationDTO> updateImmigration(@Valid @RequestBody ImmigrationDTO immigrationDTO) throws URISyntaxException {
    log.debug("REST request to update Immigration : {}", immigrationDTO);
    if (immigrationDTO.getId() == null) {
      return createImmigration(immigrationDTO);
    }
    Immigration immigration = immigrationMapper.immigrationDTOToImmigration(immigrationDTO);
    immigration = immigrationRepository.save(immigration);
    ImmigrationDTO result = immigrationMapper.immigrationToImmigrationDTO(immigration);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, immigrationDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /immigrations : get all the immigrations.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of immigrations in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @GetMapping("/immigrations")
  @Timed
  @PreAuthorize("hasAuthority('profile:view:entities')")
  public ResponseEntity<List<ImmigrationDTO>> getAllImmigrations(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of Immigrations");
    Page<Immigration> page = immigrationRepository.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/immigrations");
    return new ResponseEntity<>(immigrationMapper.immigrationsToImmigrationDTOs(page.getContent()), headers, HttpStatus.OK);
  }

  /**
   * GET  /immigrations/:id : get the "id" immigration.
   *
   * @param id the id of the immigrationDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the immigrationDTO, or with status 404 (Not Found)
   */
  @GetMapping("/immigrations/{id}")
  @Timed
  @PreAuthorize("hasAuthority('profile:view:entities')")
  public ResponseEntity<ImmigrationDTO> getImmigration(@PathVariable Long id) {
    log.debug("REST request to get Immigration : {}", id);
    Immigration immigration = immigrationRepository.findOne(id);
    ImmigrationDTO immigrationDTO = immigrationMapper.immigrationToImmigrationDTO(immigration);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(immigrationDTO));
  }

  /**
   * DELETE  /immigrations/:id : delete the "id" immigration.
   *
   * @param id the id of the immigrationDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/immigrations/{id}")
  @Timed
  @PreAuthorize("hasAuthority('profile:delete:entities')")
  public ResponseEntity<Void> deleteImmigration(@PathVariable Long id) {
    log.debug("REST request to delete Immigration : {}", id);
    immigrationRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-immigrations : Bulk create a new immigrations.
   *
   * @param immigrationDTOS List of the immigrationDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new immigrationDTOS, or with status 400 (Bad Request) if the EqualityAndDiversity has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-immigrations")
  @Timed
  @PreAuthorize("hasAuthority('profile:add:modify:entities')")
  public ResponseEntity<List<ImmigrationDTO>> bulkCreateImmigration(@Valid @RequestBody List<ImmigrationDTO> immigrationDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save Immigration : {}", immigrationDTOS);
    if (!Collections.isEmpty(immigrationDTOS)) {
      List<Long> entityIds = immigrationDTOS.stream()
          .filter(immigrationDTO -> immigrationDTO.getId() != null)
          .map(immigrationDTO -> immigrationDTO.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new Immigration cannot already have an ID")).body(null);
      }
    }
    List<Immigration> immigrationList = immigrationMapper.immigrationDTOsToImmigrations(immigrationDTOS);
    immigrationList = immigrationRepository.save(immigrationList);
    List<ImmigrationDTO> result = immigrationMapper.immigrationsToImmigrationDTOs(immigrationList);
    List<Long> ids = result.stream().map(immigration -> immigration.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * PUT  /bulk-immigrations : Updates an existing immigrations.
   *
   * @param immigrationDTOS List of the immigrationDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated immigrationDTOS,
   * or with status 400 (Bad Request) if the immigrationDTOS is not valid,
   * or with status 500 (Internal Server Error) if the immigrationDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-immigrations")
  @Timed
  @PreAuthorize("hasAuthority('profile:add:modify:entities')")
  public ResponseEntity<List<ImmigrationDTO>> bulkUpdateImmigration(@Valid @RequestBody List<ImmigrationDTO> immigrationDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update Immigration : {}", immigrationDTOS);
    if (Collections.isEmpty(immigrationDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(immigrationDTOS)) {
      List<ImmigrationDTO> entitiesWithNoId = immigrationDTOS.stream().filter(i -> i.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<Immigration> immigrationList = immigrationMapper.immigrationDTOsToImmigrations(immigrationDTOS);
    immigrationList = immigrationRepository.save(immigrationList);
    List<ImmigrationDTO> results = immigrationMapper.immigrationsToImmigrationDTOs(immigrationList);
    List<Long> ids = results.stream().map(i -> i.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }
}

package com.transformuk.hee.tis.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.profile.domain.OrganisationalEntity;
import com.transformuk.hee.tis.profile.dto.OrganisationalEntityDTO;
import com.transformuk.hee.tis.profile.repository.OrganisationalEntityRepository;
import com.transformuk.hee.tis.profile.service.mapper.OrganisationalEntityMapper;
import com.transformuk.hee.tis.profile.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing OrganisationalEntityDTO.
 */
@RestController
@RequestMapping("/api")
public class OrganisationalEntityResource {

  private static final String ENTITY_NAME = "OrganisationalEntity";
  private final Logger log = LoggerFactory.getLogger(OrganisationalEntityResource.class);
  private final OrganisationalEntityRepository entityRepository;

  private final OrganisationalEntityMapper entityMapper;

  public OrganisationalEntityResource(OrganisationalEntityRepository entityRepository, OrganisationalEntityMapper entityMapper) {
    this.entityRepository = entityRepository;
    this.entityMapper = entityMapper;
  }

  /**
   * GET  /entities : get all the entities.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of roles in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @GetMapping("/entities")
  @Timed
  @PreAuthorize("hasAuthority('profile:view:entities')")
  public ResponseEntity<List<OrganisationalEntityDTO>> getAllEntities(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of Entities");
    Page<OrganisationalEntity> page = entityRepository.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/entities");
    return new ResponseEntity<>(entityMapper.organisationalEntitiesToEntityDTOs(page.getContent()), headers,
            HttpStatus.OK);
  }
}

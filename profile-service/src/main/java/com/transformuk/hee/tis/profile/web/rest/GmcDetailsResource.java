package com.transformuk.hee.tis.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.profile.domain.GmcDetails;
import com.transformuk.hee.tis.profile.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.profile.service.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.profile.service.mapper.GmcDetailsMapper;
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
 * REST controller for managing GmcDetails.
 */
@RestController
@RequestMapping("/api")
public class GmcDetailsResource {

  private static final String ENTITY_NAME = "gmcDetails";
  private final Logger log = LoggerFactory.getLogger(GmcDetailsResource.class);
  private final GmcDetailsRepository gmcDetailsRepository;

  private final GmcDetailsMapper gmcDetailsMapper;

  public GmcDetailsResource(GmcDetailsRepository gmcDetailsRepository, GmcDetailsMapper gmcDetailsMapper) {
    this.gmcDetailsRepository = gmcDetailsRepository;
    this.gmcDetailsMapper = gmcDetailsMapper;
  }

  /**
   * POST  /gmc-details : Create a new gmcDetails.
   *
   * @param gmcDetailsDTO the gmcDetailsDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new gmcDetailsDTO, or with status 400 (Bad Request) if the gmcDetails has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/gmc-details")
  @Timed
  @PreAuthorize("hasAuthority('profile:add:modify:entities')")
  public ResponseEntity<GmcDetailsDTO> createGmcDetails(@Valid @RequestBody GmcDetailsDTO gmcDetailsDTO) throws URISyntaxException {
    log.debug("REST request to save GmcDetails : {}", gmcDetailsDTO);
    if (gmcDetailsDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new gmcDetails cannot already have an ID")).body(null);
    }
    GmcDetails gmcDetails = gmcDetailsMapper.gmcDetailsDTOToGmcDetails(gmcDetailsDTO);
    gmcDetails = gmcDetailsRepository.save(gmcDetails);
    GmcDetailsDTO result = gmcDetailsMapper.gmcDetailsToGmcDetailsDTO(gmcDetails);
    return ResponseEntity.created(new URI("/api/gmc-details/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /gmc-details : Updates an existing gmcDetails.
   *
   * @param gmcDetailsDTO the gmcDetailsDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated gmcDetailsDTO,
   * or with status 400 (Bad Request) if the gmcDetailsDTO is not valid,
   * or with status 500 (Internal Server Error) if the gmcDetailsDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/gmc-details")
  @Timed
  @PreAuthorize("hasAuthority('profile:add:modify:entities')")
  public ResponseEntity<GmcDetailsDTO> updateGmcDetails(@Valid @RequestBody GmcDetailsDTO gmcDetailsDTO) throws URISyntaxException {
    log.debug("REST request to update GmcDetails : {}", gmcDetailsDTO);
    if (gmcDetailsDTO.getId() == null) {
      return createGmcDetails(gmcDetailsDTO);
    }
    GmcDetails gmcDetails = gmcDetailsMapper.gmcDetailsDTOToGmcDetails(gmcDetailsDTO);
    gmcDetails = gmcDetailsRepository.save(gmcDetails);
    GmcDetailsDTO result = gmcDetailsMapper.gmcDetailsToGmcDetailsDTO(gmcDetails);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gmcDetailsDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /gmc-details : get all the gmcDetails.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of gmcDetails in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @GetMapping("/gmc-details")
  @Timed
  @PreAuthorize("hasAuthority('profile:view:entities')")
  public ResponseEntity<List<GmcDetailsDTO>> getAllGmcDetails(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of GmcDetails");
    Page<GmcDetails> page = gmcDetailsRepository.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gmc-details");
    return new ResponseEntity<>(gmcDetailsMapper.gmcDetailsToGmcDetailsDTOs(page.getContent()), headers, HttpStatus.OK);
  }

  /**
   * GET  /gmc-details/:id : get the "id" gmcDetails.
   *
   * @param id the id of the gmcDetailsDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the gmcDetailsDTO, or with status 404 (Not Found)
   */
  @GetMapping("/gmc-details/{id}")
  @Timed
  @PreAuthorize("hasAuthority('profile:view:entities')")
  public ResponseEntity<GmcDetailsDTO> getGmcDetails(@PathVariable Long id) {
    log.debug("REST request to get GmcDetails : {}", id);
    GmcDetails gmcDetails = gmcDetailsRepository.findOne(id);
    GmcDetailsDTO gmcDetailsDTO = gmcDetailsMapper.gmcDetailsToGmcDetailsDTO(gmcDetails);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gmcDetailsDTO));
  }

  /**
   * DELETE  /gmc-details/:id : delete the "id" gmcDetails.
   *
   * @param id the id of the gmcDetailsDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/gmc-details/{id}")
  @Timed
  @PreAuthorize("hasAuthority('profile:delete:entities')")
  public ResponseEntity<Void> deleteGmcDetails(@PathVariable Long id) {
    log.debug("REST request to delete GmcDetails : {}", id);
    gmcDetailsRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-gmc-details : Bulk create a new gmc-details.
   *
   * @param gmcDetailsDTOS List of the gmcDetailsDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new gmcDetailsDTOS, or with status 400 (Bad Request) if the GmcDetails has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-gmc-details")
  @Timed
  @PreAuthorize("hasAuthority('profile:add:modify:entities')")
  public ResponseEntity<List<GmcDetailsDTO>> bulkCreateGmcDetails(@Valid @RequestBody List<GmcDetailsDTO> gmcDetailsDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save GmcDetails : {}", gmcDetailsDTOS);
    if (!Collections.isEmpty(gmcDetailsDTOS)) {
      List<Long> entityIds = gmcDetailsDTOS.stream()
          .filter(gmc -> gmc.getId() != null)
          .map(gmc -> gmc.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new GmcDetail cannot already have an ID")).body(null);
      }
    }
    List<GmcDetails> gmcDetailsList = gmcDetailsMapper.gmcDetailsDTOsToGmcDetails(gmcDetailsDTOS);
    gmcDetailsList = gmcDetailsRepository.save(gmcDetailsList);
    List<GmcDetailsDTO> result = gmcDetailsMapper.gmcDetailsToGmcDetailsDTOs(gmcDetailsList);
    List<Long> ids = result.stream().map(gmc -> gmc.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * PUT  /bulk-gmc-details : Updates an existing gmc-details.
   *
   * @param gmcDetailsDTOS List of the gmcDetailsDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated gmcDetailsDTOS,
   * or with status 400 (Bad Request) if the gmcDetailsDTOS is not valid,
   * or with status 500 (Internal Server Error) if the gmcDetailsDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-gmc-details")
  @Timed
  @PreAuthorize("hasAuthority('profile:add:modify:entities')")
  public ResponseEntity<List<GmcDetailsDTO>> bulkUpdateGmcDetails(@Valid @RequestBody List<GmcDetailsDTO> gmcDetailsDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update GmcDetails : {}", gmcDetailsDTOS);
    if (Collections.isEmpty(gmcDetailsDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(gmcDetailsDTOS)) {
      List<GmcDetailsDTO> entitiesWithNoId = gmcDetailsDTOS.stream().filter(gmcDetailsDTO -> gmcDetailsDTO.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<GmcDetails> gmcDetailsList = gmcDetailsMapper.gmcDetailsDTOsToGmcDetails(gmcDetailsDTOS);
    gmcDetailsList = gmcDetailsRepository.save(gmcDetailsList);
    List<GmcDetailsDTO> results = gmcDetailsMapper.gmcDetailsToGmcDetailsDTOs(gmcDetailsList);
    List<Long> ids = results.stream().map(gmcDetailsDTO -> gmcDetailsDTO.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }
}

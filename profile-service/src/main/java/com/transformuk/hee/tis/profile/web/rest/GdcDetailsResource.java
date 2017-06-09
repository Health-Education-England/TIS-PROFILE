package com.transformuk.hee.tis.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.profile.domain.GdcDetails;
import com.transformuk.hee.tis.profile.repository.GdcDetailsRepository;
import com.transformuk.hee.tis.profile.service.dto.GdcDetailsDTO;
import com.transformuk.hee.tis.profile.service.mapper.GdcDetailsMapper;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing GdcDetails.
 */
@RestController
@RequestMapping("/api")
public class GdcDetailsResource {

	private static final String ENTITY_NAME = "gdcDetails";
	private final Logger log = LoggerFactory.getLogger(GdcDetailsResource.class);
	private final GdcDetailsRepository gdcDetailsRepository;

	private final GdcDetailsMapper gdcDetailsMapper;

	public GdcDetailsResource(GdcDetailsRepository gdcDetailsRepository, GdcDetailsMapper gdcDetailsMapper) {
		this.gdcDetailsRepository = gdcDetailsRepository;
		this.gdcDetailsMapper = gdcDetailsMapper;
	}

	/**
	 * POST  /gdc-details : Create a new gdcDetails.
	 *
	 * @param gdcDetailsDTO the gdcDetailsDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new gdcDetailsDTO, or with status 400 (Bad Request) if the gdcDetails has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/gdc-details")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<GdcDetailsDTO> createGdcDetails(@Valid @RequestBody GdcDetailsDTO gdcDetailsDTO) throws URISyntaxException {
		log.debug("REST request to save GdcDetails : {}", gdcDetailsDTO);
		if (gdcDetailsDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new gdcDetails cannot already have an ID")).body(null);
		}
		GdcDetails gdcDetails = gdcDetailsMapper.gdcDetailsDTOToGdcDetails(gdcDetailsDTO);
		gdcDetails = gdcDetailsRepository.save(gdcDetails);
		GdcDetailsDTO result = gdcDetailsMapper.gdcDetailsToGdcDetailsDTO(gdcDetails);
		return ResponseEntity.created(new URI("/api/gdc-details/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /gdc-details : Updates an existing gdcDetails.
	 *
	 * @param gdcDetailsDTO the gdcDetailsDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated gdcDetailsDTO,
	 * or with status 400 (Bad Request) if the gdcDetailsDTO is not valid,
	 * or with status 500 (Internal Server Error) if the gdcDetailsDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/gdc-details")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<GdcDetailsDTO> updateGdcDetails(@Valid @RequestBody GdcDetailsDTO gdcDetailsDTO) throws URISyntaxException {
		log.debug("REST request to update GdcDetails : {}", gdcDetailsDTO);
		if (gdcDetailsDTO.getId() == null) {
			return createGdcDetails(gdcDetailsDTO);
		}
		GdcDetails gdcDetails = gdcDetailsMapper.gdcDetailsDTOToGdcDetails(gdcDetailsDTO);
		gdcDetails = gdcDetailsRepository.save(gdcDetails);
		GdcDetailsDTO result = gdcDetailsMapper.gdcDetailsToGdcDetailsDTO(gdcDetails);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gdcDetailsDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /gdc-details : get all the gdcDetails.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of gdcDetails in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/gdc-details")
	@Timed
	@PreAuthorize("hasAuthority('profile:view:entities')")
	public ResponseEntity<List<GdcDetailsDTO>> getAllGdcDetails(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of GdcDetails");
		Page<GdcDetails> page = gdcDetailsRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gdc-details");
		return new ResponseEntity<>(gdcDetailsMapper.gdcDetailsToGdcDetailsDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET  /gdc-details/:id : get the "id" gdcDetails.
	 *
	 * @param id the id of the gdcDetailsDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the gdcDetailsDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/gdc-details/{id}")
	@Timed
	@PreAuthorize("hasAuthority('profile:view:entities')")
	public ResponseEntity<GdcDetailsDTO> getGdcDetails(@PathVariable Long id) {
		log.debug("REST request to get GdcDetails : {}", id);
		GdcDetails gdcDetails = gdcDetailsRepository.findOne(id);
		GdcDetailsDTO gdcDetailsDTO = gdcDetailsMapper.gdcDetailsToGdcDetailsDTO(gdcDetails);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gdcDetailsDTO));
	}

	/**
	 * DELETE  /gdc-details/:id : delete the "id" gdcDetails.
	 *
	 * @param id the id of the gdcDetailsDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/gdc-details/{id}")
	@Timed
	@PreAuthorize("hasAuthority('profile:delete:entities')")
	public ResponseEntity<Void> deleteGdcDetails(@PathVariable Long id) {
		log.debug("REST request to delete GdcDetails : {}", id);
		gdcDetailsRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}


	/**
	 * POST  /bulk-gdc-details : Bulk create a new gdc-details.
	 *
	 * @param gdcDetailsDTOS List of the gdcDetailsDTOS to create
	 * @return the ResponseEntity with status 200 (Created) and with body the new gdcDetailsDTOS, or with status 400 (Bad Request) if the GdcDetails has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/bulk-gdc-details")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<List<GdcDetailsDTO>> bulkCreateGdcDetails(@Valid @RequestBody List<GdcDetailsDTO> gdcDetailsDTOS) throws URISyntaxException {
		log.debug("REST request to bulk save GdcDetails : {}", gdcDetailsDTOS);
		if (!Collections.isEmpty(gdcDetailsDTOS)) {
			List<Long> entityIds = gdcDetailsDTOS.stream()
					.filter(gdc -> gdc.getId() != null)
					.map(gdc -> gdc.getId())
					.collect(Collectors.toList());
			if (!Collections.isEmpty(entityIds)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new GdcDetail cannot already have an ID")).body(null);
			}
		}
		List<GdcDetails> gdcDetailsList = gdcDetailsMapper.gdcDetailsDTOsToGdcDetails(gdcDetailsDTOS);
		gdcDetailsList = gdcDetailsRepository.save(gdcDetailsList);
		List<GdcDetailsDTO> result = gdcDetailsMapper.gdcDetailsToGdcDetailsDTOs(gdcDetailsList);
		List<Long> ids = result.stream().map(gdc -> gdc.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(result);
	}

	/**
	 * PUT  /bulk-gdc-details : Updates an existing gdc-details.
	 *
	 * @param gdcDetailsDTOS List of the gdcDetailsDTOS to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated gdcDetailsDTOS,
	 * or with status 400 (Bad Request) if the gdcDetailsDTOS is not valid,
	 * or with status 500 (Internal Server Error) if the gdcDetailsDTOS couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/bulk-gdc-details")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<List<GdcDetailsDTO>> bulkUpdateGdcDetails(@Valid @RequestBody List<GdcDetailsDTO> gdcDetailsDTOS) throws URISyntaxException {
		log.debug("REST request to bulk update GdcDetails : {}", gdcDetailsDTOS);
		if (Collections.isEmpty(gdcDetailsDTOS)) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
					"The request body for this end point cannot be empty")).body(null);
		} else if (!Collections.isEmpty(gdcDetailsDTOS)) {
			List<GdcDetailsDTO> entitiesWithNoId = gdcDetailsDTOS.stream().filter(gdcDetailsDTO -> gdcDetailsDTO.getId() == null).collect(Collectors.toList());
			if (!Collections.isEmpty(entitiesWithNoId)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
						"bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
			}
		}
		List<GdcDetails> gdcDetailsList = gdcDetailsMapper.gdcDetailsDTOsToGdcDetails(gdcDetailsDTOS);
		gdcDetailsList = gdcDetailsRepository.save(gdcDetailsList);
		List<GdcDetailsDTO> results = gdcDetailsMapper.gdcDetailsToGdcDetailsDTOs(gdcDetailsList);
		List<Long> ids = results.stream().map(gdcDetailsDTO -> gdcDetailsDTO.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(results);
	}
}

package com.transformuk.hee.tis.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.profile.domain.GmcDetails;
import com.transformuk.hee.tis.profile.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.profile.service.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.profile.service.mapper.GmcDetailsMapper;
import com.transformuk.hee.tis.profile.web.rest.util.HeaderUtil;
import com.transformuk.hee.tis.profile.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
	public ResponseEntity<Void> deleteGmcDetails(@PathVariable Long id) {
		log.debug("REST request to delete GmcDetails : {}", id);
		gmcDetailsRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}

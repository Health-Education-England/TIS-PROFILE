package com.transformuk.hee.tis.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.profile.domain.PersonalDetails;
import com.transformuk.hee.tis.profile.repository.PersonalDetailsRepository;
import com.transformuk.hee.tis.profile.service.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.profile.service.mapper.PersonalDetailsMapper;
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
 * REST controller for managing PersonalDetails.
 */
@RestController
@RequestMapping("/api")
public class PersonalDetailsResource {

	private static final String ENTITY_NAME = "personalDetails";
	private final Logger log = LoggerFactory.getLogger(PersonalDetailsResource.class);
	private final PersonalDetailsRepository personalDetailsRepository;

	private final PersonalDetailsMapper personalDetailsMapper;

	public PersonalDetailsResource(PersonalDetailsRepository personalDetailsRepository, PersonalDetailsMapper personalDetailsMapper) {
		this.personalDetailsRepository = personalDetailsRepository;
		this.personalDetailsMapper = personalDetailsMapper;
	}

	/**
	 * POST  /personal-details : Create a new personalDetails.
	 *
	 * @param personalDetailsDTO the personalDetailsDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new personalDetailsDTO, or with status 400 (Bad Request) if the personalDetails has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/personal-details")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<PersonalDetailsDTO> createPersonalDetails(@Valid @RequestBody PersonalDetailsDTO personalDetailsDTO) throws URISyntaxException {
		log.debug("REST request to save PersonalDetails : {}", personalDetailsDTO);
		if (personalDetailsDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new personalDetails cannot already have an ID")).body(null);
		}
		PersonalDetails personalDetails = personalDetailsMapper.personalDetailsDTOToPersonalDetails(personalDetailsDTO);
		personalDetails = personalDetailsRepository.save(personalDetails);
		PersonalDetailsDTO result = personalDetailsMapper.personalDetailsToPersonalDetailsDTO(personalDetails);
		return ResponseEntity.created(new URI("/api/personal-details/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /personal-details : Updates an existing personalDetails.
	 *
	 * @param personalDetailsDTO the personalDetailsDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated personalDetailsDTO,
	 * or with status 400 (Bad Request) if the personalDetailsDTO is not valid,
	 * or with status 500 (Internal Server Error) if the personalDetailsDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/personal-details")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<PersonalDetailsDTO> updatePersonalDetails(@Valid @RequestBody PersonalDetailsDTO personalDetailsDTO) throws URISyntaxException {
		log.debug("REST request to update PersonalDetails : {}", personalDetailsDTO);
		if (personalDetailsDTO.getId() == null) {
			return createPersonalDetails(personalDetailsDTO);
		}
		PersonalDetails personalDetails = personalDetailsMapper.personalDetailsDTOToPersonalDetails(personalDetailsDTO);
		personalDetails = personalDetailsRepository.save(personalDetails);
		PersonalDetailsDTO result = personalDetailsMapper.personalDetailsToPersonalDetailsDTO(personalDetails);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, personalDetailsDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /personal-details : get all the personalDetails.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of personalDetails in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/personal-details")
	@Timed
	@PreAuthorize("hasAuthority('profile:view:entities')")
	public ResponseEntity<List<PersonalDetailsDTO>> getAllPersonalDetails(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of PersonalDetails");
		Page<PersonalDetails> page = personalDetailsRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/personal-details");
		return new ResponseEntity<>(personalDetailsMapper.personalDetailsToPersonalDetailsDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET  /personal-details/:id : get the "id" personalDetails.
	 *
	 * @param id the id of the personalDetailsDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the personalDetailsDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/personal-details/{id}")
	@Timed
	@PreAuthorize("hasAuthority('profile:view:entities')")
	public ResponseEntity<PersonalDetailsDTO> getPersonalDetails(@PathVariable Long id) {
		log.debug("REST request to get PersonalDetails : {}", id);
		PersonalDetails personalDetails = personalDetailsRepository.findOne(id);
		PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.personalDetailsToPersonalDetailsDTO(personalDetails);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(personalDetailsDTO));
	}

	/**
	 * DELETE  /personal-details/:id : delete the "id" personalDetails.
	 *
	 * @param id the id of the personalDetailsDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/personal-details/{id}")
	@Timed
	@PreAuthorize("hasAuthority('profile:delete:entities')")
	public ResponseEntity<Void> deletePersonalDetails(@PathVariable Long id) {
		log.debug("REST request to delete PersonalDetails : {}", id);
		personalDetailsRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}


	/**
	 * POST  /bulk-personal-details : Bulk create a new personal-details.
	 *
	 * @param personalDetailsDTOS List of the personalDetailsDTOS to create
	 * @return the ResponseEntity with status 200 (Created) and with body the new personalDetailsDTOS, or with status 400 (Bad Request) if the PersonalDetails has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/bulk-personal-details")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<List<PersonalDetailsDTO>> bulkCreatePersonalDetails(@Valid @RequestBody List<PersonalDetailsDTO> personalDetailsDTOS) throws URISyntaxException {
		log.debug("REST request to bulk save PersonalDetails : {}", personalDetailsDTOS);
		if (!Collections.isEmpty(personalDetailsDTOS)) {
			List<Long> entityIds = personalDetailsDTOS.stream()
					.filter(pd -> pd.getId() != null)
					.map(pd -> pd.getId())
					.collect(Collectors.toList());
			if (!Collections.isEmpty(entityIds)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new PersonalDetails cannot already have an ID")).body(null);
			}
		}
		List<PersonalDetails> personalDetailsList = personalDetailsMapper.personalDetailsDTOsToPersonalDetails(personalDetailsDTOS);
		personalDetailsList = personalDetailsRepository.save(personalDetailsList);
		List<PersonalDetailsDTO> result = personalDetailsMapper.personalDetailsToPersonalDetailsDTOs(personalDetailsList);
		List<Long> ids = result.stream().map(pd -> pd.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(result);
	}

	/**
	 * PUT  /bulk-personal-details : Updates an existing personal-details.
	 *
	 * @param personalDetailsDTOS List of the personalDetailsDTOS to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated personalDetailsDTOS,
	 * or with status 400 (Bad Request) if the personalDetailsDTOS is not valid,
	 * or with status 500 (Internal Server Error) if the personalDetailsDTOS couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/bulk-personal-details")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<List<PersonalDetailsDTO>> bulkUpdatePersonalDetails(@Valid @RequestBody List<PersonalDetailsDTO> personalDetailsDTOS) throws URISyntaxException {
		log.debug("REST request to bulk update PersonalDetails : {}", personalDetailsDTOS);
		if (Collections.isEmpty(personalDetailsDTOS)) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
					"The request body for this end point cannot be empty")).body(null);
		} else if (!Collections.isEmpty(personalDetailsDTOS)) {
			List<PersonalDetailsDTO> entitiesWithNoId = personalDetailsDTOS.stream().filter(pd -> pd.getId() == null).collect(Collectors.toList());
			if (!Collections.isEmpty(entitiesWithNoId)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
						"bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
			}
		}
		List<PersonalDetails> personalDetailsList = personalDetailsMapper.personalDetailsDTOsToPersonalDetails(personalDetailsDTOS);
		personalDetailsList = personalDetailsRepository.save(personalDetailsList);
		List<PersonalDetailsDTO> results = personalDetailsMapper.personalDetailsToPersonalDetailsDTOs(personalDetailsList);
		List<Long> ids = results.stream().map(pd -> pd.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(results);
	}

}

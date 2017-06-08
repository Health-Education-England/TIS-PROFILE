package com.transformuk.hee.tis.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.profile.domain.EqualityAndDiversity;
import com.transformuk.hee.tis.profile.repository.EqualityAndDiversityRepository;
import com.transformuk.hee.tis.profile.service.dto.EqualityAndDiversityDTO;
import com.transformuk.hee.tis.profile.service.mapper.EqualityAndDiversityMapper;
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
 * REST controller for managing EqualityAndDiversity.
 */
@RestController
@RequestMapping("/api")
public class EqualityAndDiversityResource {

	private static final String ENTITY_NAME = "equalityAndDiversity";
	private final Logger log = LoggerFactory.getLogger(EqualityAndDiversityResource.class);
	private final EqualityAndDiversityRepository equalityAndDiversityRepository;

	private final EqualityAndDiversityMapper equalityAndDiversityMapper;

	public EqualityAndDiversityResource(EqualityAndDiversityRepository equalityAndDiversityRepository, EqualityAndDiversityMapper equalityAndDiversityMapper) {
		this.equalityAndDiversityRepository = equalityAndDiversityRepository;
		this.equalityAndDiversityMapper = equalityAndDiversityMapper;
	}

	/**
	 * POST  /equality-and-diversities : Create a new equalityAndDiversity.
	 *
	 * @param equalityAndDiversityDTO the equalityAndDiversityDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new equalityAndDiversityDTO, or with status 400 (Bad Request) if the equalityAndDiversity has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/equality-and-diversities")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<EqualityAndDiversityDTO> createEqualityAndDiversity(@Valid @RequestBody EqualityAndDiversityDTO equalityAndDiversityDTO) throws URISyntaxException {
		log.debug("REST request to save EqualityAndDiversity : {}", equalityAndDiversityDTO);
		if (equalityAndDiversityDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new equalityAndDiversity cannot already have an ID")).body(null);
		}
		EqualityAndDiversity equalityAndDiversity = equalityAndDiversityMapper.equalityAndDiversityDTOToEqualityAndDiversity(equalityAndDiversityDTO);
		equalityAndDiversity = equalityAndDiversityRepository.save(equalityAndDiversity);
		EqualityAndDiversityDTO result = equalityAndDiversityMapper.equalityAndDiversityToEqualityAndDiversityDTO(equalityAndDiversity);
		return ResponseEntity.created(new URI("/api/equality-and-diversities/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /equality-and-diversities : Updates an existing equalityAndDiversity.
	 *
	 * @param equalityAndDiversityDTO the equalityAndDiversityDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated equalityAndDiversityDTO,
	 * or with status 400 (Bad Request) if the equalityAndDiversityDTO is not valid,
	 * or with status 500 (Internal Server Error) if the equalityAndDiversityDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/equality-and-diversities")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<EqualityAndDiversityDTO> updateEqualityAndDiversity(@Valid @RequestBody EqualityAndDiversityDTO equalityAndDiversityDTO) throws URISyntaxException {
		log.debug("REST request to update EqualityAndDiversity : {}", equalityAndDiversityDTO);
		if (equalityAndDiversityDTO.getId() == null) {
			return createEqualityAndDiversity(equalityAndDiversityDTO);
		}
		EqualityAndDiversity equalityAndDiversity = equalityAndDiversityMapper.equalityAndDiversityDTOToEqualityAndDiversity(equalityAndDiversityDTO);
		equalityAndDiversity = equalityAndDiversityRepository.save(equalityAndDiversity);
		EqualityAndDiversityDTO result = equalityAndDiversityMapper.equalityAndDiversityToEqualityAndDiversityDTO(equalityAndDiversity);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, equalityAndDiversityDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /equality-and-diversities : get all the equalityAndDiversities.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of equalityAndDiversities in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/equality-and-diversities")
	@Timed
	@PreAuthorize("hasAuthority('profile:view:entities')")
	public ResponseEntity<List<EqualityAndDiversityDTO>> getAllEqualityAndDiversities(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of EqualityAndDiversities");
		Page<EqualityAndDiversity> page = equalityAndDiversityRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/equality-and-diversities");
		return new ResponseEntity<>(equalityAndDiversityMapper.equalityAndDiversitiesToEqualityAndDiversityDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET  /equality-and-diversities/:id : get the "id" equalityAndDiversity.
	 *
	 * @param id the id of the equalityAndDiversityDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the equalityAndDiversityDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/equality-and-diversities/{id}")
	@Timed
	@PreAuthorize("hasAuthority('profile:view:entities')")
	public ResponseEntity<EqualityAndDiversityDTO> getEqualityAndDiversity(@PathVariable Long id) {
		log.debug("REST request to get EqualityAndDiversity : {}", id);
		EqualityAndDiversity equalityAndDiversity = equalityAndDiversityRepository.findOne(id);
		EqualityAndDiversityDTO equalityAndDiversityDTO = equalityAndDiversityMapper.equalityAndDiversityToEqualityAndDiversityDTO(equalityAndDiversity);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(equalityAndDiversityDTO));
	}

	/**
	 * DELETE  /equality-and-diversities/:id : delete the "id" equalityAndDiversity.
	 *
	 * @param id the id of the equalityAndDiversityDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/equality-and-diversities/{id}")
	@Timed
	@PreAuthorize("hasAuthority('profile:delete:entities')")
	public ResponseEntity<Void> deleteEqualityAndDiversity(@PathVariable Long id) {
		log.debug("REST request to delete EqualityAndDiversity : {}", id);
		equalityAndDiversityRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}


	/**
	 * POST  /bulk-equality-and-diversities : Bulk create a new equality-and-diversities.
	 *
	 * @param equalityAndDiversityDTOS List of the equalityAndDiversityDTOS to create
	 * @return the ResponseEntity with status 200 (Created) and with body the new equalityAndDiversityDTOS, or with status 400 (Bad Request) if the EqualityAndDiversity has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/bulk-equality-and-diversities")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<List<EqualityAndDiversityDTO>> bulkCreateEqualityAndDiversity(@Valid @RequestBody List<EqualityAndDiversityDTO> equalityAndDiversityDTOS) throws URISyntaxException {
		log.debug("REST request to bulk save EqualityAndDiversity : {}", equalityAndDiversityDTOS);
		if (!Collections.isEmpty(equalityAndDiversityDTOS)) {
			List<Long> entityIds = equalityAndDiversityDTOS.stream()
					.filter(ead -> ead.getId() != null)
					.map(ead -> ead.getId())
					.collect(Collectors.toList());
			if (!Collections.isEmpty(entityIds)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new EqualityAndDiversity cannot already have an ID")).body(null);
			}
		}
		List<EqualityAndDiversity> equalityAndDiversityList = equalityAndDiversityMapper.equalityAndDiversityDTOsToEqualityAndDiversities(equalityAndDiversityDTOS);
		equalityAndDiversityList = equalityAndDiversityRepository.save(equalityAndDiversityList);
		List<EqualityAndDiversityDTO> result = equalityAndDiversityMapper.equalityAndDiversitiesToEqualityAndDiversityDTOs(equalityAndDiversityList);
		List<Long> ids = result.stream().map(at -> at.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(result);
	}

	/**
	 * PUT  /bulk-equality-and-diversities : Updates an existing EqualityAndDiversity.
	 *
	 * @param equalityAndDiversityDTOS List of the equalityAndDiversityDTOS to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated equalityAndDiversityDTOS,
	 * or with status 400 (Bad Request) if the equalityAndDiversityDTOS is not valid,
	 * or with status 500 (Internal Server Error) if the equalityAndDiversityDTOS couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/bulk-equality-and-diversities")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<List<EqualityAndDiversityDTO>> bulkUpdateEqualityAndDiversity(@Valid @RequestBody List<EqualityAndDiversityDTO> equalityAndDiversityDTOS) throws URISyntaxException {
		log.debug("REST request to bulk update EqualityAndDiversity : {}", equalityAndDiversityDTOS);
		if (Collections.isEmpty(equalityAndDiversityDTOS)) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
					"The request body for this end point cannot be empty")).body(null);
		} else if (!Collections.isEmpty(equalityAndDiversityDTOS)) {
			List<EqualityAndDiversityDTO> entitiesWithNoId = equalityAndDiversityDTOS.stream().filter(at -> at.getId() == null).collect(Collectors.toList());
			if (!Collections.isEmpty(entitiesWithNoId)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
						"bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
			}		}
		List<EqualityAndDiversity> equalityAndDiversityList = equalityAndDiversityMapper.equalityAndDiversityDTOsToEqualityAndDiversities(equalityAndDiversityDTOS);
		equalityAndDiversityList = equalityAndDiversityRepository.save(equalityAndDiversityList);
		List<EqualityAndDiversityDTO> results = equalityAndDiversityMapper.equalityAndDiversitiesToEqualityAndDiversityDTOs(equalityAndDiversityList);
		List<Long> ids = results.stream().map(at -> at.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(results);
	}
}

package com.transformuk.hee.tis.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.profile.domain.Immigration;
import com.transformuk.hee.tis.profile.repository.ImmigrationRepository;
import com.transformuk.hee.tis.profile.service.dto.ImmigrationDTO;
import com.transformuk.hee.tis.profile.service.mapper.ImmigrationMapper;
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
	public ResponseEntity<Void> deleteImmigration(@PathVariable Long id) {
		log.debug("REST request to delete Immigration : {}", id);
		immigrationRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}

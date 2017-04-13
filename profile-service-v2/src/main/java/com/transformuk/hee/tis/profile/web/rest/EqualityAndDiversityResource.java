package com.transformuk.hee.tis.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.profile.domain.EqualityAndDiversity;
import com.transformuk.hee.tis.profile.repository.EqualityAndDiversityRepository;
import com.transformuk.hee.tis.profile.service.dto.EqualityAndDiversityDTO;
import com.transformuk.hee.tis.profile.service.mapper.EqualityAndDiversityMapper;
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
	public ResponseEntity<Void> deleteEqualityAndDiversity(@PathVariable Long id) {
		log.debug("REST request to delete EqualityAndDiversity : {}", id);
		equalityAndDiversityRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}

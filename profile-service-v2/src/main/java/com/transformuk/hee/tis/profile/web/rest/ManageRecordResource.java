package com.transformuk.hee.tis.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.profile.domain.ManageRecord;
import com.transformuk.hee.tis.profile.repository.ManageRecordRepository;
import com.transformuk.hee.tis.profile.service.dto.ManageRecordDTO;
import com.transformuk.hee.tis.profile.service.mapper.ManageRecordMapper;
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
 * REST controller for managing ManageRecord.
 */
@RestController
@RequestMapping("/api")
public class ManageRecordResource {

	private static final String ENTITY_NAME = "manageRecord";
	private final Logger log = LoggerFactory.getLogger(ManageRecordResource.class);
	private final ManageRecordRepository manageRecordRepository;

	private final ManageRecordMapper manageRecordMapper;

	public ManageRecordResource(ManageRecordRepository manageRecordRepository, ManageRecordMapper manageRecordMapper) {
		this.manageRecordRepository = manageRecordRepository;
		this.manageRecordMapper = manageRecordMapper;
	}

	/**
	 * POST  /manage-records : Create a new manageRecord.
	 *
	 * @param manageRecordDTO the manageRecordDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new manageRecordDTO, or with status 400 (Bad Request) if the manageRecord has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/manage-records")
	@Timed
	public ResponseEntity<ManageRecordDTO> createManageRecord(@Valid @RequestBody ManageRecordDTO manageRecordDTO) throws URISyntaxException {
		log.debug("REST request to save ManageRecord : {}", manageRecordDTO);
		if (manageRecordDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new manageRecord cannot already have an ID")).body(null);
		}
		ManageRecord manageRecord = manageRecordMapper.manageRecordDTOToManageRecord(manageRecordDTO);
		manageRecord = manageRecordRepository.save(manageRecord);
		ManageRecordDTO result = manageRecordMapper.manageRecordToManageRecordDTO(manageRecord);
		return ResponseEntity.created(new URI("/api/manage-records/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /manage-records : Updates an existing manageRecord.
	 *
	 * @param manageRecordDTO the manageRecordDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated manageRecordDTO,
	 * or with status 400 (Bad Request) if the manageRecordDTO is not valid,
	 * or with status 500 (Internal Server Error) if the manageRecordDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/manage-records")
	@Timed
	public ResponseEntity<ManageRecordDTO> updateManageRecord(@Valid @RequestBody ManageRecordDTO manageRecordDTO) throws URISyntaxException {
		log.debug("REST request to update ManageRecord : {}", manageRecordDTO);
		if (manageRecordDTO.getId() == null) {
			return createManageRecord(manageRecordDTO);
		}
		ManageRecord manageRecord = manageRecordMapper.manageRecordDTOToManageRecord(manageRecordDTO);
		manageRecord = manageRecordRepository.save(manageRecord);
		ManageRecordDTO result = manageRecordMapper.manageRecordToManageRecordDTO(manageRecord);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, manageRecordDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /manage-records : get all the manageRecords.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of manageRecords in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/manage-records")
	@Timed
	public ResponseEntity<List<ManageRecordDTO>> getAllManageRecords(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of ManageRecords");
		Page<ManageRecord> page = manageRecordRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/manage-records");
		return new ResponseEntity<>(manageRecordMapper.manageRecordsToManageRecordDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET  /manage-records/:id : get the "id" manageRecord.
	 *
	 * @param id the id of the manageRecordDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the manageRecordDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/manage-records/{id}")
	@Timed
	public ResponseEntity<ManageRecordDTO> getManageRecord(@PathVariable Long id) {
		log.debug("REST request to get ManageRecord : {}", id);
		ManageRecord manageRecord = manageRecordRepository.findOne(id);
		ManageRecordDTO manageRecordDTO = manageRecordMapper.manageRecordToManageRecordDTO(manageRecord);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(manageRecordDTO));
	}

	/**
	 * DELETE  /manage-records/:id : delete the "id" manageRecord.
	 *
	 * @param id the id of the manageRecordDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/manage-records/{id}")
	@Timed
	public ResponseEntity<Void> deleteManageRecord(@PathVariable Long id) {
		log.debug("REST request to delete ManageRecord : {}", id);
		manageRecordRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}

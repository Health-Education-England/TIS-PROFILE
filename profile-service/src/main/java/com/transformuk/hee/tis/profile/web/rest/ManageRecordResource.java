package com.transformuk.hee.tis.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.profile.domain.ManageRecord;
import com.transformuk.hee.tis.profile.repository.ManageRecordRepository;
import com.transformuk.hee.tis.profile.service.dto.ManageRecordDTO;
import com.transformuk.hee.tis.profile.service.mapper.ManageRecordMapper;
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
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
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
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
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
	@PreAuthorize("hasAuthority('profile:view:entities')")
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
	@PreAuthorize("hasAuthority('profile:view:entities')")
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
	@PreAuthorize("hasAuthority('profile:delete:entities')")
	public ResponseEntity<Void> deleteManageRecord(@PathVariable Long id) {
		log.debug("REST request to delete ManageRecord : {}", id);
		manageRecordRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}


	/**
	 * POST  /bulk-manage-records : Bulk create a new manage-records.
	 *
	 * @param manageRecordDTOS List of the manageRecordDTOS to create
	 * @return the ResponseEntity with status 200 (Created) and with body the new manageRecordDTOS, or with status 400 (Bad Request) if the EqualityAndDiversity has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/bulk-manage-records")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<List<ManageRecordDTO>> bulkCreateManageRecord(@Valid @RequestBody List<ManageRecordDTO> manageRecordDTOS) throws URISyntaxException {
		log.debug("REST request to bulk save ManageRecord : {}", manageRecordDTOS);
		if (!Collections.isEmpty(manageRecordDTOS)) {
			List<Long> entityIds = manageRecordDTOS.stream()
					.filter(mr -> mr.getId() != null)
					.map(mr -> mr.getId())
					.collect(Collectors.toList());
			if (!Collections.isEmpty(entityIds)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new ManageRecord cannot already have an ID")).body(null);
			}
		}
		List<ManageRecord> manageRecordList = manageRecordMapper.manageRecordDTOsToManageRecords(manageRecordDTOS);
		manageRecordList = manageRecordRepository.save(manageRecordList);
		List<ManageRecordDTO> result = manageRecordMapper.manageRecordsToManageRecordDTOs(manageRecordList);
		List<Long> ids = result.stream().map(mr -> mr.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(result);
	}

	/**
	 * PUT  /bulk-manage-records : Updates an existing manage-records.
	 *
	 * @param manageRecordDTOS List of the manageRecordDTOS to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated manageRecordDTOS,
	 * or with status 400 (Bad Request) if the manageRecordDTOS is not valid,
	 * or with status 500 (Internal Server Error) if the manageRecordDTOS couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/bulk-manage-records")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<List<ManageRecordDTO>> bulkUpdateManageRecord(@Valid @RequestBody List<ManageRecordDTO> manageRecordDTOS) throws URISyntaxException {
		log.debug("REST request to bulk update ManageRecord : {}", manageRecordDTOS);
		if (Collections.isEmpty(manageRecordDTOS)) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
					"The request body for this end point cannot be empty")).body(null);
		} else if (!Collections.isEmpty(manageRecordDTOS)) {
			List<ManageRecordDTO> entitiesWithNoId = manageRecordDTOS.stream().filter(mr -> mr.getId() == null).collect(Collectors.toList());
			if (!Collections.isEmpty(entitiesWithNoId)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
						"bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
			}
		}
		List<ManageRecord> manageRecordList = manageRecordMapper.manageRecordDTOsToManageRecords(manageRecordDTOS);
		manageRecordList = manageRecordRepository.save(manageRecordList);
		List<ManageRecordDTO> results = manageRecordMapper.manageRecordsToManageRecordDTOs(manageRecordList);
		List<Long> ids = results.stream().map(mr -> mr.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(results);
	}
}

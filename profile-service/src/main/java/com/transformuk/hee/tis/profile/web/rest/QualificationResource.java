package com.transformuk.hee.tis.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.profile.domain.Qualification;
import com.transformuk.hee.tis.profile.repository.QualificationRepository;
import com.transformuk.hee.tis.profile.service.dto.QualificationDTO;
import com.transformuk.hee.tis.profile.service.mapper.QualificationMapper;
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
 * REST controller for managing Qualification.
 */
@RestController
@RequestMapping("/api")
public class QualificationResource {

	private static final String ENTITY_NAME = "qualification";
	private final Logger log = LoggerFactory.getLogger(QualificationResource.class);
	private final QualificationRepository qualificationRepository;

	private final QualificationMapper qualificationMapper;

	public QualificationResource(QualificationRepository qualificationRepository, QualificationMapper qualificationMapper) {
		this.qualificationRepository = qualificationRepository;
		this.qualificationMapper = qualificationMapper;
	}

	/**
	 * POST  /qualifications : Create a new qualification.
	 *
	 * @param qualificationDTO the qualificationDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new qualificationDTO, or with status 400 (Bad Request) if the qualification has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/qualifications")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<QualificationDTO> createQualification(@Valid @RequestBody QualificationDTO qualificationDTO) throws URISyntaxException {
		log.debug("REST request to save Qualification : {}", qualificationDTO);
		if (qualificationDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new qualification cannot already have an ID")).body(null);
		}
		Qualification qualification = qualificationMapper.qualificationDTOToQualification(qualificationDTO);
		qualification = qualificationRepository.save(qualification);
		QualificationDTO result = qualificationMapper.qualificationToQualificationDTO(qualification);
		return ResponseEntity.created(new URI("/api/qualifications/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /qualifications : Updates an existing qualification.
	 *
	 * @param qualificationDTO the qualificationDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated qualificationDTO,
	 * or with status 400 (Bad Request) if the qualificationDTO is not valid,
	 * or with status 500 (Internal Server Error) if the qualificationDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/qualifications")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<QualificationDTO> updateQualification(@Valid @RequestBody QualificationDTO qualificationDTO) throws URISyntaxException {
		log.debug("REST request to update Qualification : {}", qualificationDTO);
		if (qualificationDTO.getId() == null) {
			return createQualification(qualificationDTO);
		}
		Qualification qualification = qualificationMapper.qualificationDTOToQualification(qualificationDTO);
		qualification = qualificationRepository.save(qualification);
		QualificationDTO result = qualificationMapper.qualificationToQualificationDTO(qualification);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, qualificationDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /qualifications : get all the qualifications.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of qualifications in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/qualifications")
	@Timed
	@PreAuthorize("hasAuthority('profile:view:entities')")
	public ResponseEntity<List<QualificationDTO>> getAllQualifications(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of Qualifications");
		Page<Qualification> page = qualificationRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/qualifications");
		return new ResponseEntity<>(qualificationMapper.qualificationsToQualificationDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET  /qualifications/:id : get the "id" qualification.
	 *
	 * @param id the id of the qualificationDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the qualificationDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/qualifications/{id}")
	@Timed
	@PreAuthorize("hasAuthority('profile:view:entities')")
	public ResponseEntity<QualificationDTO> getQualification(@PathVariable Long id) {
		log.debug("REST request to get Qualification : {}", id);
		Qualification qualification = qualificationRepository.findOne(id);
		QualificationDTO qualificationDTO = qualificationMapper.qualificationToQualificationDTO(qualification);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(qualificationDTO));
	}

	/**
	 * DELETE  /qualifications/:id : delete the "id" qualification.
	 *
	 * @param id the id of the qualificationDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/qualifications/{id}")
	@Timed
	@PreAuthorize("hasAuthority('profile:delete:entities')")
	public ResponseEntity<Void> deleteQualification(@PathVariable Long id) {
		log.debug("REST request to delete Qualification : {}", id);
		qualificationRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}


	/**
	 * POST  /bulk-qualifications : Bulk create a new qualifications.
	 *
	 * @param qualificationDTOS List of the qualificationDTOS to create
	 * @return the ResponseEntity with status 200 (Created) and with body the new qualificationDTOS, or with status 400 (Bad Request) if the Qualification has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/bulk-qualifications")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<List<QualificationDTO>> bulkCreateQualification(@Valid @RequestBody List<QualificationDTO> qualificationDTOS) throws URISyntaxException {
		log.debug("REST request to bulk save Qualification : {}", qualificationDTOS);
		if (!Collections.isEmpty(qualificationDTOS)) {
			List<Long> entityIds = qualificationDTOS.stream()
					.filter(q -> q.getId() != null)
					.map(q -> q.getId())
					.collect(Collectors.toList());
			if (!Collections.isEmpty(entityIds)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new Qualification cannot already have an ID")).body(null);
			}
		}
		List<Qualification> qualificationList = qualificationMapper.qualificationDTOsToQualifications(qualificationDTOS);
		qualificationList = qualificationRepository.save(qualificationList);
		List<QualificationDTO> result = qualificationMapper.qualificationsToQualificationDTOs(qualificationList);
		List<Long> ids = result.stream().map(q -> q.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(result);
	}

	/**
	 * PUT  /bulk-qualifications : Updates an existing qualifications.
	 *
	 * @param qualificationDTOS List of the qualificationDTOS to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated qualificationDTOS,
	 * or with status 400 (Bad Request) if the qualificationDTOS is not valid,
	 * or with status 500 (Internal Server Error) if the qualificationDTOS couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/bulk-qualifications")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<List<QualificationDTO>> bulkUpdateQualification(@Valid @RequestBody List<QualificationDTO> qualificationDTOS) throws URISyntaxException {
		log.debug("REST request to bulk update Qualification : {}", qualificationDTOS);
		if (Collections.isEmpty(qualificationDTOS)) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
					"The request body for this end point cannot be empty")).body(null);
		} else if (!Collections.isEmpty(qualificationDTOS)) {
			List<QualificationDTO> entitiesWithNoId = qualificationDTOS.stream().filter(q -> q.getId() == null).collect(Collectors.toList());
			if (!Collections.isEmpty(entitiesWithNoId)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
						"bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
			}
		}
		List<Qualification> qualificationList = qualificationMapper.qualificationDTOsToQualifications(qualificationDTOS);
		qualificationList = qualificationRepository.save(qualificationList);
		List<QualificationDTO> results = qualificationMapper.qualificationsToQualificationDTOs(qualificationList);
		List<Long> ids = results.stream().map(q -> q.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(results);
	}
}

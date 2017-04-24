package com.transformuk.hee.tis.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import com.transformuk.hee.tis.profile.service.mapper.HeeUserMapper;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing HeeUser.
 */
@RestController
@RequestMapping("/api")
public class HeeUserResource {

	private static final String ENTITY_NAME = "heeUser";
	private final Logger log = LoggerFactory.getLogger(HeeUserResource.class);
	private final HeeUserRepository heeUserRepository;

	private final HeeUserMapper heeUserMapper;

	public HeeUserResource(HeeUserRepository heeUserRepository, HeeUserMapper heeUserMapper) {
		this.heeUserRepository = heeUserRepository;
		this.heeUserMapper = heeUserMapper;
	}

	/**
	 * POST  /hee-users : Create a new heeUser.
	 *
	 * @param heeUserDTO the heeUserDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new heeUserDTO, or with status 400 (Bad Request) if the heeUser has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/hee-users")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<HeeUserDTO> createHeeUser(@Valid @RequestBody HeeUserDTO heeUserDTO) throws URISyntaxException {
		log.debug("REST request to save HeeUser : {}", heeUserDTO);
		HeeUser heeUser = heeUserMapper.heeUserDTOToHeeUser(heeUserDTO);
		heeUser = heeUserRepository.save(heeUser);
		HeeUserDTO result = heeUserMapper.heeUserToHeeUserDTO(heeUser);
		return ResponseEntity.created(new URI("/api/hee-users/" + result.getName()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getName()))
				.body(result);
	}

	/**
	 * PUT  /hee-users : Updates an existing heeUser.
	 *
	 * @param heeUserDTO the heeUserDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated heeUserDTO,
	 * or with status 400 (Bad Request) if the heeUserDTO is not valid,
	 * or with status 500 (Internal Server Error) if the heeUserDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/hee-users")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<HeeUserDTO> updateHeeUser(@Valid @RequestBody HeeUserDTO heeUserDTO) throws URISyntaxException {
		log.debug("REST request to update HeeUser : {}", heeUserDTO);
		if (heeUserDTO.getName() == null) {
			return createHeeUser(heeUserDTO);
		}
		HeeUser heeUser = heeUserMapper.heeUserDTOToHeeUser(heeUserDTO);
		heeUser = heeUserRepository.save(heeUser);
		HeeUserDTO result = heeUserMapper.heeUserToHeeUserDTO(heeUser);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, heeUserDTO.getName().toString()))
				.body(result);
	}

	/**
	 * GET  /hee-users : get all the heeUsers.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of heeUsers in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/hee-users")
	@Timed
	@PreAuthorize("hasAuthority('profile:view:entities')")
	public ResponseEntity<List<HeeUserDTO>> getAllHeeUsers(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of HeeUsers");
		Page<HeeUser> page = heeUserRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/hee-users");
		return new ResponseEntity<>(heeUserMapper.heeUsersToHeeUserDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET  /hee-users/:name : get the "name" heeUser.
	 *
	 * @param name the name of the heeUserDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the heeUserDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/hee-users/{name}")
	@Timed
	@PreAuthorize("hasAuthority('profile:view:entities')")
	public ResponseEntity<HeeUserDTO> getHeeUser(@PathVariable String name) {
		log.debug("REST request to get HeeUser : {}", name);
		HeeUser heeUser = heeUserRepository.findOne(name);
		HeeUserDTO heeUserDTO = heeUserMapper.heeUserToHeeUserDTO(heeUser);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(heeUserDTO));
	}

	/**
	 * DELETE  /hee-users/:name : delete the "name" heeUser.
	 *
	 * @param name the name of the heeUserDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/hee-users/{name}")
	@Timed
	@PreAuthorize("hasAuthority('profile:delete:entities')")
	public ResponseEntity<Void> deleteHeeUser(@PathVariable String name) {
		log.debug("REST request to delete HeeUser : {}", name);
		heeUserRepository.delete(name);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, name)).build();
	}

}

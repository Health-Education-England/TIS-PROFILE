package com.transformuk.hee.tis.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.profile.domain.Permission;
import com.transformuk.hee.tis.profile.repository.PermissionRepository;
import com.transformuk.hee.tis.profile.service.dto.PermissionDTO;
import com.transformuk.hee.tis.profile.service.mapper.PermissionMapper;
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
 * REST controller for managing Permission.
 */
@RestController
@RequestMapping("/api")
public class PermissionResource {

	private static final String ENTITY_NAME = "permission";
	private final Logger log = LoggerFactory.getLogger(PermissionResource.class);
	private final PermissionRepository permissionRepository;

	private final PermissionMapper permissionMapper;

	public PermissionResource(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
		this.permissionRepository = permissionRepository;
		this.permissionMapper = permissionMapper;
	}

	/**
	 * POST  /permissions : Create a new permission.
	 *
	 * @param permissionDTO the permissionDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new permissionDTO, or with status 400 (Bad Request) if the permission has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/permissions")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<PermissionDTO> createPermission(@Valid @RequestBody PermissionDTO permissionDTO) throws URISyntaxException {
		log.debug("REST request to save Permission : {}", permissionDTO);
		Permission permission = permissionMapper.permissionDTOToPermission(permissionDTO);
		permission = permissionRepository.save(permission);
		PermissionDTO result = permissionMapper.permissionToPermissionDTO(permission);
		return ResponseEntity.created(new URI("/api/permissions/" + result.getName()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getName().toString()))
				.body(result);
	}

	/**
	 * PUT  /permissions : Updates an existing permission. Please note, given that a permission is just
	 * one string, updating a permission just means creating a new one, the existing one will still be there
	 *
	 * @param permissionDTO the permissionDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated permissionDTO,
	 * or with status 400 (Bad Request) if the permissionDTO is not valid,
	 * or with status 500 (Internal Server Error) if the permissionDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/permissions")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<PermissionDTO> updatePermission(@Valid @RequestBody PermissionDTO permissionDTO) throws URISyntaxException {
		log.debug("REST request to update Permission : {}", permissionDTO);
		return createPermission(permissionDTO);
	}

	/**
	 * GET  /permissions : get all the permissions.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of permissions in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/permissions")
	@Timed
	@PreAuthorize("hasAuthority('profile:view:entities')")
	public ResponseEntity<List<PermissionDTO>> getAllPermissions(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of Permissions");
		Page<Permission> page = permissionRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/permissions");
		return new ResponseEntity<>(permissionMapper.permissionsToPermissionDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET  /permissions/:name : get the "name" permission.
	 *
	 * @param name the id of the permissionDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the permissionDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/permissions/{name}")
	@Timed
	@PreAuthorize("hasAuthority('profile:view:entities')")
	public ResponseEntity<PermissionDTO> getPermission(@PathVariable String name) {
		log.debug("REST request to get Permission : {}", name);
		Permission permission = permissionRepository.findOne(name);
		PermissionDTO permissionDTO = permissionMapper.permissionToPermissionDTO(permission);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(permissionDTO));
	}

	/**
	 * DELETE  /permissions/:name : delete the "name" permission.
	 *
	 * @param name the id of the permissionDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/permissions/{name}")
	@Timed
	@PreAuthorize("hasAuthority('profile:delete:entities')")
	public ResponseEntity<Void> deletePermission(@PathVariable String name) {
		log.debug("REST request to delete Permission : {}", name);
		permissionRepository.delete(name);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, name)).build();
	}

}

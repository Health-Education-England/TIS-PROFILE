package com.transformuk.hee.tis.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.profile.dto.Permission;
import com.transformuk.hee.tis.profile.repository.PermissionRepository;
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
	public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission permissionDTO) throws URISyntaxException {
		log.debug("REST request to save Permission : {}", permissionDTO);
		com.transformuk.hee.tis.profile.domain.Permission permission = permissionMapper.permissionDTOToPermission(permissionDTO);
		permission = permissionRepository.save(permission);
		Permission result = permissionMapper.permissionToPermissionDTO(permission);
		return ResponseEntity.created(new URI("/api/permissions/" + result.getName()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getName().toString()))
				.body(result);
	}

	/**
	 * PUT  /permissions : Updates an existing permission. Please note, given that a permission is just
	 * one string, updating a permission just means creating a new one, the existing one will still be there
	 *
	 * @param permission the permission to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated permission,
	 * or with status 400 (Bad Request) if the permission is not valid,
	 * or with status 500 (Internal Server Error) if the permission couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/permissions")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<Permission> updatePermission(@Valid @RequestBody Permission permission) throws URISyntaxException {
		log.debug("REST request to update Permission : {}", permission);
		return createPermission(permission);
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
	public ResponseEntity<List<Permission>> getAllPermissions(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of Permissions");
		Page<com.transformuk.hee.tis.profile.domain.Permission> page = permissionRepository.findAll(pageable);
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
	public ResponseEntity<Permission> getPermission(@PathVariable String name) {
		log.debug("REST request to get Permission : {}", name);
		com.transformuk.hee.tis.profile.domain.Permission permission = permissionRepository.findOne(name);
		Permission permissionDTO = permissionMapper.permissionToPermissionDTO(permission);
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

package com.transformuk.hee.tis.profile.web.rest;

import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.dto.RoleDTO;
import com.transformuk.hee.tis.profile.repository.RoleRepository;
import com.transformuk.hee.tis.profile.service.mapper.RoleMapper;
import com.transformuk.hee.tis.profile.validators.RoleValidator;
import com.transformuk.hee.tis.profile.web.rest.util.HeaderUtil;
import com.transformuk.hee.tis.profile.web.rest.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiParam;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Role.
 */
@RestController
@RequestMapping("/api")
public class RoleResource {

  private static final String ENTITY_NAME = "role";
  private final Logger log = LoggerFactory.getLogger(RoleResource.class);
  private final RoleRepository roleRepository;

  private final RoleMapper roleMapper;

  private final RoleValidator roleValidator;

  public RoleResource(RoleRepository roleRepository, RoleMapper roleMapper,
      RoleValidator roleValidator) {
    this.roleRepository = roleRepository;
    this.roleMapper = roleMapper;
    this.roleValidator = roleValidator;
  }

  /**
   * POST  /roles : Create a new role.
   *
   * @param roleDTO the roleDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new roleDTO, or with
   * status 400 (Bad Request) if the role has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/roles")
  @Timed
  @PreAuthorize("hasAuthority('profile:add:modify:entities')")
  public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleDTO roleDTO)
      throws URISyntaxException {
    log.debug("REST request to save Role : {}", roleDTO);
    Role role = roleMapper.roleDTOToRole(roleDTO);
    //Validate
    validateRole(role);

    role = roleRepository.save(role);
    RoleDTO result = roleMapper.roleToRoleDTO(role);
    return ResponseEntity.created(new URI("/api/roles/" + result.getName()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getName().toString()))
        .body(result);
  }

  /**
   * PUT  /roles : Updates an existing role. Please note, given that a permission is just one
   * string, updating a permission just means creating a new one, the existing one will still be
   * there
   *
   * @param roleDTO the roleDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated roleDTO, or with
   * status 400 (Bad Request) if the roleDTO is not valid, or with status 500 (Internal Server
   * Error) if the roleDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/roles")
  @Timed
  @PreAuthorize("hasAuthority('profile:add:modify:entities')")
  public ResponseEntity<RoleDTO> updateRole(@Valid @RequestBody RoleDTO roleDTO)
      throws URISyntaxException {
    log.debug("REST request to update Role : {}", roleDTO);
    Role dbRole = roleRepository.findByName(roleDTO.getName());
    if (dbRole == null || dbRole.getName() == null) {
      return createRole(roleDTO);
    }
    Role role = roleMapper.roleDTOToRole(roleDTO);
    //Validate
    validateRole(role);
    role = roleRepository.save(role);
    RoleDTO result = roleMapper.roleToRoleDTO(role);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, roleDTO.getName().toString()))
        .body(result);
  }

  /**
   * GET  /roles : get all the roles.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of roles in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @GetMapping("/roles")
  @Timed
  @PreAuthorize("hasAuthority('profile:view:entities')")
  public ResponseEntity<List<RoleDTO>> getAllRoles(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of Roles");
    Page<Role> page = roleRepository.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/roles");
    return new ResponseEntity<>(roleMapper.rolesToRoleDTOs(page.getContent()), headers,
        HttpStatus.OK);
  }

  /**
   * GET  /roles/:name : get the "name" role.
   *
   * @param name the name of the roleDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the roleDTO, or with status 404
   * (Not Found)
   */
  @GetMapping("/roles/{name}")
  @Timed
  @PreAuthorize("hasAuthority('profile:view:entities')")
  public ResponseEntity<RoleDTO> getRole(@PathVariable String name) {
    log.debug("REST request to get Role : {}", name);
    Role role = roleRepository.getById(name);
    RoleDTO roleDTO = roleMapper.roleToRoleDTO(role);
    return ResponseEntity.of(Optional.ofNullable(roleDTO));
  }

  /**
   * DELETE  /roles/:name : delete the "name" role.
   *
   * @param name the name of the roleDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/roles/{name}")
  @Timed
  @PreAuthorize("hasAuthority('profile:delete:entities')")
  public ResponseEntity<Void> deleteRole(@PathVariable String name) {
    log.debug("REST request to delete Role : {}", name);
    //validate before delete
    roleValidator.validateBeforeDelete(name);

    roleRepository.deleteById(name);
    return ResponseEntity.noContent().build();
  }

  private void validateRole(Role role) {
    //validate permissions
    roleValidator.validatePermissions(role.getPermissions());
  }
}

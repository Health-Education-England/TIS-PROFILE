package com.transformuk.hee.tis.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.UserTrust;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.profile.repository.UserTrustRepository;
import com.transformuk.hee.tis.profile.service.KeycloakAdminClientService;
import com.transformuk.hee.tis.profile.service.UserService;
import com.transformuk.hee.tis.profile.service.UserTrustService;
import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import com.transformuk.hee.tis.profile.service.mapper.HeeUserMapper;
import com.transformuk.hee.tis.profile.validators.HeeUserValidator;
import com.transformuk.hee.tis.profile.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing HeeUser.
 */
@RestController
@RequestMapping("/api")
public class HeeUserResource {

  private static final String ENTITY_NAME = "heeUser";
  private static final String REALM_LIN = "lin";
  private final Logger log = LoggerFactory.getLogger(HeeUserResource.class);
  private final HeeUserRepository heeUserRepository;
  private final HeeUserMapper heeUserMapper;
  private final UserTrustRepository userTrustRepository;
  private final UserTrustService userTrustService;
  private final UserService userService;

  private KeycloakAdminClientService keyclockAdminClientService;

  private HeeUserValidator heeUserValidator;

  public HeeUserResource(HeeUserRepository heeUserRepository, HeeUserMapper heeUserMapper,
                         KeycloakAdminClientService keyclockAdminClientService, HeeUserValidator heeUserValidator,
                         UserTrustRepository userTrustRepository, UserTrustService userTrustService,
                         UserService userService) {
    this.heeUserRepository = heeUserRepository;
    this.heeUserMapper = heeUserMapper;
    this.keyclockAdminClientService = keyclockAdminClientService;
    this.heeUserValidator = heeUserValidator;
    this.userTrustRepository = userTrustRepository;
    this.userTrustService = userTrustService;
    this.userService = userService;
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
    heeUser.setPassword(heeUserDTO.getPassword());
    //Validate password
    heeUserValidator.validatePassword(heeUser.getPassword());
    //Validate isTemporaryPassword
    heeUserValidator.validateIsTemporary(heeUser.getTemporaryPassword());
    //Validate
    validateHeeUser(heeUser);

    // First try to create user in KeyClock
    keyclockAdminClientService.createUser(heeUser);

    Set<UserTrust> associatedTrusts = heeUser.getAssociatedTrusts();
    if(CollectionUtils.isNotEmpty(associatedTrusts)){
      for (UserTrust userTrust : associatedTrusts) {
        userTrust.setHeeUser(heeUser);
      }
    }
    heeUser = heeUserRepository.save(heeUser);
    userTrustRepository.save(associatedTrusts);
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

    HeeUser dbHeeUser = heeUserRepository.findOne(heeUserDTO.getName());
    if (dbHeeUser == null || dbHeeUser.getName() == null) {
      return createHeeUser(heeUserDTO);
    }
    HeeUser heeUser = heeUserMapper.heeUserDTOToHeeUser(heeUserDTO);
    //Validate
    validateHeeUser(heeUser);

    // First try to update user in KeyClock
    keyclockAdminClientService.updateUser(heeUser);
    heeUserRepository.save(heeUser);
    userTrustService.assignTrustsToUser(heeUserDTO);
    HeeUserDTO result = heeUserMapper.heeUserToHeeUserDTO(heeUserRepository.findByNameWithTrusts(heeUserDTO.getName()).orElse(null));
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
  public ResponseEntity<Page<HeeUserDTO>> getAllHeeUsers(@ApiParam Pageable pageable,
                                                         @RequestParam(required = false) String search) {
    log.debug("REST request to get a page of HeeUsers");
    Page<HeeUserDTO> heeUserDTOS = userService.findAllUsersWithTrust(pageable, search);
    return new ResponseEntity<>(heeUserDTOS, HttpStatus.OK);
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
    HeeUserDTO heeUserDTO = userService.findSingleUserWithTrust(name);
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

  private void validateHeeUser(HeeUser heeUser) {
    //validate GMC id
    heeUserValidator.validateGmcId(heeUser.getGmcId());
    //Validate DBC code
    heeUserValidator.validateDBCIds(heeUser.getDesignatedBodyCodes());
    //Validate Role name
    heeUserValidator.validateRoles(heeUser.getRoles());
  }

}

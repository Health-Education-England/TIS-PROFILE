package com.transformuk.hee.tis.profile.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.dto.PermissionDTO;
import com.transformuk.hee.tis.profile.dto.PermissionType;
import com.transformuk.hee.tis.profile.repository.PermissionRepository;
import com.transformuk.hee.tis.profile.service.mapper.PermissionMapper;
import com.transformuk.hee.tis.profile.web.rest.errors.ExceptionTranslator;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the PermissionResource REST controller.
 *
 * @see PermissionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
public class PermissionResourceIntTest {

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final PermissionType DEFAULT_TYPE = PermissionType.CONCERN;
  private static final String DEFAULT_DESC = "desc";
  private static final String DEFAULT_PRINCIPAL = "PRI";
  private static final String DEFAULT_RESOURCE = "RES";
  private static final String DEFAULT_ACTIONS = "View";
  private static final String DEFAULT_EFFECT = "Allow";

  @Autowired
  private PermissionRepository permissionRepository;

  @Autowired
  private PermissionMapper permissionMapper;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  private MockMvc restPermissionMockMvc;

  private com.transformuk.hee.tis.profile.domain.Permission permission;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static com.transformuk.hee.tis.profile.domain.Permission createEntity() {
    return new com.transformuk.hee.tis.profile.domain.Permission(
        DEFAULT_NAME, DEFAULT_TYPE, DEFAULT_DESC, DEFAULT_PRINCIPAL, DEFAULT_RESOURCE,
        DEFAULT_ACTIONS, DEFAULT_EFFECT
    );
  }

  @Before
  public void setup() {
    PermissionResource permissionResource = new PermissionResource(permissionRepository,
        permissionMapper);
    this.restPermissionMockMvc = MockMvcBuilders.standaloneSetup(permissionResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    permission = createEntity();
  }

  @Test
  @Transactional
  public void createPermission() throws Exception {
    int databaseSizeBeforeCreate = permissionRepository.findAll().size();

    // Create the Permission
    PermissionDTO permissionDto = permissionMapper.permissionToPermissionDTO(this.permission);
    restPermissionMockMvc.perform(post("/api/permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(permissionDto)))
        .andExpect(status().isCreated());

    // Validate the Permission in the database
    List<com.transformuk.hee.tis.profile.domain.Permission> permissionList = permissionRepository
        .findAll();
    assertThat(permissionList).hasSize(databaseSizeBeforeCreate + 1);
    com.transformuk.hee.tis.profile.domain.Permission testPermission = permissionRepository
        .getById(this.permission.getName());
    assertThat(testPermission.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testPermission.getDescription()).isEqualTo(DEFAULT_DESC);
    assertThat(testPermission.getType()).isEqualTo(DEFAULT_TYPE);
    assertThat(testPermission.getPrincipal()).isEqualTo(DEFAULT_PRINCIPAL);
    assertThat(testPermission.getResource()).isEqualTo(DEFAULT_RESOURCE);
    assertThat(testPermission.getActions()).isEqualTo(DEFAULT_ACTIONS);
    assertThat(testPermission.getEffect()).isEqualTo(DEFAULT_EFFECT);
  }

  @Test
  @Transactional
  public void createPermissionWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = permissionRepository.findAll().size();

    // Create the Permission with an existing ID
    this.permission.setName("revalidation:see:dbc:trainees");
    PermissionDTO permissionDto = permissionMapper.permissionToPermissionDTO(this.permission);

    // Creating a permissionDto with the same name will do nothing but not fail
    restPermissionMockMvc.perform(post("/api/permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(permissionDto)))
        .andExpect(status().isCreated());

    // Validate the Alice in the database
    List<com.transformuk.hee.tis.profile.domain.Permission> permissionList = permissionRepository
        .findAll();
    assertThat(permissionList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = permissionRepository.findAll().size();
    // set the field null
    this.permission.setName(null);

    // Create the Permission, which fails.
    PermissionDTO permissionDto = permissionMapper.permissionToPermissionDTO(this.permission);

    restPermissionMockMvc.perform(post("/api/permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(permissionDto)))
        .andExpect(status().isBadRequest());

    List<com.transformuk.hee.tis.profile.domain.Permission> permissionList = permissionRepository
        .findAll();
    assertThat(permissionList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllPermissions() throws Exception {
    // Initialize the database
    permissionRepository.saveAndFlush(permission);

    // Get all the permissionList
    restPermissionMockMvc.perform(get("/api/permissions?sort=name,asc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
        .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
        .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESC)))
        .andExpect(jsonPath("$.[*].principal").value(hasItem(DEFAULT_PRINCIPAL)))
        .andExpect(jsonPath("$.[*].resource").value(hasItem(DEFAULT_RESOURCE)))
        .andExpect(jsonPath("$.[*].actions.[*]").value(hasItem(DEFAULT_ACTIONS)))
        .andExpect(jsonPath("$.[*].effect").value(hasItem(DEFAULT_EFFECT)));
  }

  @Test
  @Transactional
  public void getPermission() throws Exception {
    // Initialize the database
    permissionRepository.saveAndFlush(permission);

    // Get the permission
    restPermissionMockMvc.perform(get("/api/permissions/{id}", permission.getName()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
        .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
        .andExpect(jsonPath("$.description").value(DEFAULT_DESC))
        .andExpect(jsonPath("$.principal").value(DEFAULT_PRINCIPAL))
        .andExpect(jsonPath("$.resource").value(DEFAULT_RESOURCE))
        .andExpect(jsonPath("$.actions").value(DEFAULT_ACTIONS))
        .andExpect(jsonPath("$.effect").value(DEFAULT_EFFECT));
  }

  @Test
  @Transactional
  public void getNonExistingPermission() throws Exception {
    // Get the permission
    restPermissionMockMvc.perform(get("/api/permissions/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void deletePermission() throws Exception {
    // Initialize the database
    permissionRepository.saveAndFlush(permission);
    int databaseSizeBeforeDelete = permissionRepository.findAll().size();

    // Get the permission
    restPermissionMockMvc.perform(delete("/api/permissions/{id}", permission.getName())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database is empty
    List<com.transformuk.hee.tis.profile.domain.Permission> permissionList = permissionRepository
        .findAll();
    assertThat(permissionList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(com.transformuk.hee.tis.profile.domain.Permission.class);
  }
}

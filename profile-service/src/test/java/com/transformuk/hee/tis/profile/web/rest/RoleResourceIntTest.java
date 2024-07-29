package com.transformuk.hee.tis.profile.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.Permission;
import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.dto.RoleDTO;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.profile.repository.PermissionRepository;
import com.transformuk.hee.tis.profile.repository.RoleRepository;
import com.transformuk.hee.tis.profile.service.mapper.RoleMapper;
import com.transformuk.hee.tis.profile.validators.RoleValidator;
import com.transformuk.hee.tis.profile.web.rest.errors.ExceptionTranslator;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
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
 * Test class for the RoleResource REST controller.
 *
 * @see RoleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
public class RoleResourceIntTest {

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String DEFAULT_PERMISSION_NAME = "default:view";
  private static final String DEFAULT_PERMISSION_PRINCIPAL = "principal";
  private static final String DEFAULT_PERMISSION_RESOURCE = "resource";
  private static final String DEFAULT_PERMISSION_ACTIONS = "View";
  private static final String DEFAULT_PERMISSION_EFFECT = "Allow";

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private RoleMapper roleMapper;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private RoleValidator roleValidator;

  @Autowired
  private PermissionRepository permissionRepository;

  @Autowired
  private HeeUserRepository heeUserRepository;

  @Autowired
  private EntityManager em;

  private MockMvc restRoleMockMvc;

  private Role role;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static Role createEntity() {
    return new Role()
        .name(DEFAULT_NAME);
  }

  @Before
  public void setup() {
    RoleResource roleResource = new RoleResource(roleRepository, roleMapper, roleValidator);
    this.restRoleMockMvc = MockMvcBuilders.standaloneSetup(roleResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    role = createEntity();
  }

  @Test
  @Transactional
  public void createRole() throws Exception {
    int databaseSizeBeforeCreate = roleRepository.findAll().size();
    int dbPermissionSizeBeforeCreate = permissionRepository.findAll().size();

    // Create the Role
    RoleDTO roleDTO = roleMapper.roleToRoleDTO(role);
    restRoleMockMvc.perform(post("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleDTO)))
        .andExpect(status().isCreated());

    // Validate the Role in the database
    List<Role> roleList = roleRepository.findAll();
    assertThat(roleList).hasSize(databaseSizeBeforeCreate + 1);
    Role testRole = roleRepository.getById(role.getName());
    assertThat(testRole.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(permissionRepository.findAll()).hasSize(dbPermissionSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void shouldThrowExceptionInvalidPermissionRole() throws Exception {
    int databaseSizeBeforeCreate = roleRepository.findAll().size();
    // Given Create the Role
    Permission permission = new Permission();
    permission.setName(DEFAULT_PERMISSION_NAME);
    permission.setPrincipal(DEFAULT_PERMISSION_PRINCIPAL);
    permission.setResource(DEFAULT_PERMISSION_RESOURCE);
    permission.setActions(DEFAULT_PERMISSION_ACTIONS);
    permission.setEffect(DEFAULT_PERMISSION_EFFECT);
    role.setPermissions(Collections.singleton(permission));

    RoleDTO roleDTO = roleMapper.roleToRoleDTO(role);
    restRoleMockMvc.perform(post("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleDTO)))
        .andExpect(status().is4xxClientError());

    // Validate the Role in the database
    List<Role> roleList = roleRepository.findAll();
    assertThat(roleList).hasSize(databaseSizeBeforeCreate);
    Role testRole = roleRepository.findByName(role.getName());
    assertThat(testRole).isNull();
  }

  @Test
  @Transactional
  public void createRoleWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = roleRepository.findAll().size();
    int dbPermissionSizeBeforeCreate = permissionRepository.findAll().size();

    // Create the Role with an existing ID
    role.setName("RevalAdmin");
    RoleDTO roleDTO = roleMapper.roleToRoleDTO(role);

    // Creating a role with the same name will do nothing but not fail
    restRoleMockMvc.perform(post("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleDTO)))
        .andExpect(status().isCreated());

    // Validate the Alice in the database
    List<Role> roleList = roleRepository.findAll();
    assertThat(roleList).hasSize(databaseSizeBeforeCreate);
    assertThat(permissionRepository.findAll()).hasSize(dbPermissionSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = roleRepository.findAll().size();
    // set the field null
    role.setName(null);

    // Create the Role, which fails.
    RoleDTO roleDTO = roleMapper.roleToRoleDTO(role);

    restRoleMockMvc.perform(post("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleDTO)))
        .andExpect(status().isBadRequest());

    List<Role> roleList = roleRepository.findAll();
    assertThat(roleList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllRoles() throws Exception {
    // Given
    // Initialize the database
    roleRepository.saveAndFlush(role);

    // Need to override default page size (20) as number of Roles increases
    String resultSize = Integer.toString(roleRepository.findAll().size());

    // When and Then
    // Get all the roleList
    restRoleMockMvc.perform(get("/api/roles?size=" + resultSize))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
  }

  @Test
  @Transactional
  public void getRole() throws Exception {
    // Initialize the database
    roleRepository.saveAndFlush(role);

    // Get the role
    restRoleMockMvc.perform(get("/api/roles/{id}", role.getName()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
  }

  @Test
  @Transactional
  public void getNonExistingRole() throws Exception {
    // Get the role
    restRoleMockMvc.perform(get("/api/roles/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateNonExistingRole() throws Exception {
    int databaseSizeBeforeUpdate = roleRepository.findAll().size();
    int dbPermissionSizeBeforeCreate = permissionRepository.findAll().size();

    // Create the Role
    RoleDTO roleDTO = roleMapper.roleToRoleDTO(role);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restRoleMockMvc.perform(put("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleDTO)))
        .andExpect(status().isCreated());

    // Validate the Role in the database
    List<Role> roleList = roleRepository.findAll();
    assertThat(roleList).hasSize(databaseSizeBeforeUpdate + 1);
    assertThat(permissionRepository.findAll()).hasSize(dbPermissionSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void deleteRole() throws Exception {
    // Initialize the database
    roleRepository.saveAndFlush(role);
    int databaseSizeBeforeDelete = roleRepository.findAll().size();
    int dbPermissionSizeBeforeCreate = permissionRepository.findAll().size();

    // Get the role
    restRoleMockMvc.perform(delete("/api/roles/{id}", role.getName())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database is empty
    List<Role> roleList = roleRepository.findAll();
    assertThat(roleList).hasSize(databaseSizeBeforeDelete - 1);
    assertThat(permissionRepository.findAll()).hasSize(dbPermissionSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void shouldThrowErrorRoleAssociatedWithUser() throws Exception {
    // Initialize the database
    roleRepository.saveAndFlush(role);
    //create User with same role associated with it
    HeeUser heeUser = TestUtil.createEntityHeeUser(em);
    heeUser.setRoles(Collections.singleton(role));
    heeUser.setActive(TestUtil.UPDATED_ACTIVE);
    heeUserRepository.saveAndFlush(heeUser);

    int databaseSizeBeforeDelete = roleRepository.findAll().size();

    // Get the role
    restRoleMockMvc.perform(delete("/api/roles/{id}", role.getName())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().is4xxClientError());

    // Validate the database is empty
    List<Role> roleList = roleRepository.findAll();
    assertThat(roleList).hasSize(databaseSizeBeforeDelete);

  }


  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Role.class);
  }
}

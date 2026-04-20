package com.transformuk.hee.tis.profile.web.rest;

import static com.google.common.collect.Sets.newHashSet;
import static com.transformuk.hee.tis.profile.web.rest.TestUtil.DEFAULT_ACTIVE;
import static com.transformuk.hee.tis.profile.web.rest.TestUtil.DEFAULT_EMAIL_ADDRESS;
import static com.transformuk.hee.tis.profile.web.rest.TestUtil.DEFAULT_FIRST_NAME;
import static com.transformuk.hee.tis.profile.web.rest.TestUtil.DEFAULT_GMC_ID;
import static com.transformuk.hee.tis.profile.web.rest.TestUtil.DEFAULT_LAST_NAME;
import static com.transformuk.hee.tis.profile.web.rest.TestUtil.DEFAULT_NAME;
import static com.transformuk.hee.tis.profile.web.rest.TestUtil.DEFAULT_PHONE_NUMBER;
import static com.transformuk.hee.tis.profile.web.rest.TestUtil.UPDATED_ACTIVE;
import static com.transformuk.hee.tis.profile.web.rest.TestUtil.UPDATED_EMAIL_ADDRESS;
import static com.transformuk.hee.tis.profile.web.rest.TestUtil.UPDATED_FIRST_NAME;
import static com.transformuk.hee.tis.profile.web.rest.TestUtil.UPDATED_GMC_ID;
import static com.transformuk.hee.tis.profile.web.rest.TestUtil.UPDATED_LAST_NAME;
import static com.transformuk.hee.tis.profile.web.rest.TestUtil.UPDATED_NAME;
import static com.transformuk.hee.tis.profile.web.rest.TestUtil.UPDATED_PHONE_NUMBER;
import static com.transformuk.hee.tis.profile.web.rest.TestUtil.createEntityHeeUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.Permission;
import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.profile.repository.PermissionRepository;
import com.transformuk.hee.tis.profile.repository.RoleRepository;
import com.transformuk.hee.tis.profile.repository.UserTrustRepository;
import com.transformuk.hee.tis.profile.service.UserProgrammeService;
import com.transformuk.hee.tis.profile.service.LoginService;
import com.transformuk.hee.tis.profile.service.UserService;
import com.transformuk.hee.tis.profile.service.UserTrustService;
import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import com.transformuk.hee.tis.profile.service.dto.UserTrustDTO;
import com.transformuk.hee.tis.profile.service.mapper.HeeUserMapper;
import com.transformuk.hee.tis.profile.validators.HeeUserValidator;
import com.transformuk.hee.tis.profile.web.rest.errors.ExceptionTranslator;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the HeeUserResource REST controller.
 *
 * @see HeeUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
public class HeeUserResourceIntTest {

  // JWT with preferred_username = "jamesh"
  private static final String TOKEN =
      "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJrcEk5UC1hQ3JaTXJ4cG5aeWNnNnlISk9VZ3g0a2hUYS04TlJyMkRhY0g0In0.eyJqdGkiOiI3ZjJiNzA4MC1lYjYxLTQ1YTgtYmUwNS0xYWFjODNkMTY3ZjciLCJleHAiOjE0Nzc1ODA5ODQsIm5iZiI6MCwiaWF0IjoxNDc3NTgwNjg0LCJpc3MiOiJodHRwczovL2Rldi1hcGkudHJhbnNmb3JtY2xvdWQubmV0L2F1dGgvcmVhbG1zL2xpbiIsImF1ZCI6ImFwaS1nYXRld2F5Iiwic3ViIjoiNGY5YWRhY2MtZjEyNC00M2FmLTkyZDMtYjVlZDc3NjhlYTU0IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYXBpLWdhdGV3YXkiLCJub25jZSI6IlA2NnVVT2JJTVBtY19Wb1RudmlYdk1KWE0zYks0RUo3WHJUeHpRbTN0ZUkiLCJhdXRoX3RpbWUiOjE0Nzc1ODA2ODQsInNlc3Npb25fc3RhdGUiOiIyNzg1NDE2Ny1hNWY0LTRkNTItOGQ3OC02OTY3M2ZmZTMwODgiLCJhY3IiOiIxIiwiY2xpZW50X3Nlc3Npb24iOiIyNjNmZDg1Ni02YmZjLTQ4ZWQtODZlNC1jMzFkOTdmYmNlZGMiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly9kZXYtYXBpLnRyYW5zZm9ybWNsb3VkLm5ldCIsImh0dHBzOi8vYXBwcy5saW4ubmhzLnVrIiwiaHR0cDovL2xvY2FsaG9zdDo4MDg3IiwiaHR0cHM6Ly9zdGFnZS1hcHBzLmxpbi5uaHMudWsiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIlJWQWRtaW4iLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJ2aWV3LXByb2ZpbGUiXX19LCJuYW1lIjoiSmFtZXMgSHVkc29uIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiamFtZXNoIiwiZ2l2ZW5fbmFtZSI6IkphbWVzIiwiZmFtaWx5X25hbWUiOiJIdWRzb24ifQ.VJ_8MDyM-1_MMlmhl4N-ZXHyq0G8AlaSLBR4eqlrXLD5CC29dW807WARalNGDqwlNSUuvK6tDiGRt5XKYWo6HDNBL-7Sp3QT2FXew6dD8zJwN8iR34aJGDGg94Kd0PkFESybqQFb4-sntCfKHQ3aRZkpD2WkyNZXEQEuDURYuqyJulqmKXqZxfnYWkd8JgSN1oTyUc4sFPWHjzI9A_y_0Tb13hAvFlPFWwKhCSSZqjRtC65JADOYMeIbyPCsSCKq0DqY2DCZpBivp5Wp0sZu0SSkww_rkwV5tql4gXV5kYmHWJa1rx_OmTAv6UKWYG4aFaqGmvcNhXkZGrweSCEmUw";
  private static final String LTFT_ADMIN_ROLE = "NHSE LTFT Admin";
  private static final String LTFT_DBC = "1-LTFT-TEST";
  private static final String REQUESTER_ROLE = "HEE Requester";
  private static final String REQUESTER_PERMISSION = "profile:view:entities";

  @Autowired
  private HeeUserRepository heeUserRepository;

  @Autowired
  private PermissionRepository permissionRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private HeeUserMapper heeUserMapper;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private HeeUserValidator heeUserValidator;

  @Autowired
  private UserTrustRepository userTrustRepository;

  @Autowired
  private UserTrustService userTrustService;

  @Autowired
  private UserProgrammeService userProgrammeService;

  @Autowired
  private UserService userService;

  @Autowired
  private LoginService loginService;

  private MockMvc restHeeUserMockMvc;

  private HeeUser heeUser;


  @Before
  public void setup() {
    HeeUserResource heeUserResource = new HeeUserResource(heeUserRepository, heeUserMapper,
        heeUserValidator, userTrustRepository, userTrustService, userProgrammeService,
        userService, loginService);
    this.restHeeUserMockMvc = MockMvcBuilders.standaloneSetup(heeUserResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    heeUser = createEntityHeeUser();
    heeUser.setDesignatedBodyCodes(newHashSet("NONE"));
  }

  @Test
  @Transactional
  public void createHeeUser() throws Exception {
    int databaseSizeBeforeCreate = heeUserRepository.findAll().size();
    int databasePermissionSizeBeforeCreate = permissionRepository.findAll().size();

    // Create the HeeUser
    HeeUserDTO heeUserDTO = heeUserMapper.heeUserToHeeUserDTO(heeUser);
    restHeeUserMockMvc.perform(post("/api/hee-users")
            .contentType(TestUtil.JSON)
            .content(TestUtil.convertObjectToJsonBytes(heeUserDTO)))
        .andExpect(status().isCreated());

    // Validate the HeeUser in the database
    List<HeeUser> heeUserList = heeUserRepository.findAll();
    assertThat(heeUserList).hasSize(databaseSizeBeforeCreate + 1);
    HeeUser testHeeUser = heeUserRepository.getById(DEFAULT_NAME);
    assertThat(testHeeUser.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testHeeUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
    assertThat(testHeeUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    assertThat(testHeeUser.getGmcId()).isEqualTo(DEFAULT_GMC_ID);
    assertThat(testHeeUser.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    assertThat(testHeeUser.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
    assertThat(testHeeUser.isActive()).isEqualTo(DEFAULT_ACTIVE);
    assertThat(permissionRepository.findAll()).hasSize(databasePermissionSizeBeforeCreate);

  }

  @Test
  @Transactional
  public void createHeeUserWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = heeUserRepository.findAll().size();

    // Create the HeeUser with an existing name
    heeUser.setName(DEFAULT_NAME);
    HeeUserDTO heeUserDTO = heeUserMapper.heeUserToHeeUserDTO(heeUser);

    // A user with the same name will update the existing user
    restHeeUserMockMvc.perform(post("/api/hee-users")
            .contentType(TestUtil.JSON)
            .content(TestUtil.convertObjectToJsonBytes(heeUserDTO)))
        .andExpect(status().isCreated());

    // Validate the Alice in the database
    List<HeeUser> heeUserList = heeUserRepository.findAll();
    assertThat(heeUserList).hasSize(databaseSizeBeforeCreate + 1);
  }

  @Test
  @Transactional
  public void checkNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = heeUserRepository.findAll().size();
    // set the field null
    heeUser.setName(null);

    // Create the HeeUser, which fails.
    HeeUserDTO heeUserDTO = heeUserMapper.heeUserToHeeUserDTO(heeUser);

    restHeeUserMockMvc.perform(post("/api/hee-users")
            .contentType(TestUtil.JSON)
            .content(TestUtil.convertObjectToJsonBytes(heeUserDTO)))
        .andExpect(status().isBadRequest());

    List<HeeUser> heeUserList = heeUserRepository.findAll();
    assertThat(heeUserList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllHeeUsers() throws Exception {
    // Initialize the database
    heeUserRepository.saveAndFlush(heeUser);

    // Get all the heeUserList
    restHeeUserMockMvc.perform(get("/api/hee-users?sort=name,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.JSON))
        .andExpect(jsonPath("$.content.[*].name").value(hasItem(DEFAULT_NAME)))
        .andExpect(
            jsonPath("$.content.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
        .andExpect(jsonPath("$.content.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
        .andExpect(jsonPath("$.content.[*].gmcId").value(hasItem(DEFAULT_GMC_ID)))
        .andExpect(
            jsonPath("$.content.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
        .andExpect(
            jsonPath("$.content.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
        .andExpect(jsonPath("$.content.[*].active").value(hasItem(DEFAULT_ACTIVE)))
        .andExpect(jsonPath("$.size").value(20))
        .andExpect(jsonPath("$.totalPages").value(1));
  }

  @Test
  @Transactional
  public void getHeeUser() throws Exception {
    // Initialize the database
    heeUserRepository.saveAndFlush(heeUser);

    // Get the heeUser
    restHeeUserMockMvc.perform(get("/api/hee-users/{id}", heeUser.getName()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.JSON))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
        .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
        .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
        .andExpect(jsonPath("$.gmcId").value(DEFAULT_GMC_ID))
        .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
        .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS))
        .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
  }

  @Test
  @Transactional
  public void getNonExistingHeeUser() throws Exception {
    // Get the heeUser
    restHeeUserMockMvc.perform(get("/api/hee-users/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void getHeeUserByNameIgnoreCase() throws Exception {
    String username = "Abc";
    heeUser.setName(username);
    heeUserRepository.saveAndFlush(heeUser);
    restHeeUserMockMvc.perform(get("/api/hee-users/{username}/ignore-case", "abc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.JSON))
        .andExpect(jsonPath("$.[*].name").value(username));
  }

  @Test
  @Transactional
  public void updateHeeUser() throws Exception {
    // Initialize the database
    heeUserRepository.saveAndFlush(heeUser);
    int databaseSizeBeforeUpdate = heeUserRepository.findAll().size();
    int databasePermissionSizeBeforeCreate = permissionRepository.findAll().size();

    // Update the heeUser
    HeeUser updatedHeeUser = heeUserRepository.getById(heeUser.getName());
    HeeUserDTO heeUserDto = heeUserMapper.heeUserToHeeUserDTO(updatedHeeUser);
    heeUserDto.setFirstName(UPDATED_FIRST_NAME);
    heeUserDto.setLastName(UPDATED_LAST_NAME);
    heeUserDto.setGmcId(UPDATED_GMC_ID);
    heeUserDto.setPhoneNumber(UPDATED_PHONE_NUMBER);
    heeUserDto.setEmailAddress(UPDATED_EMAIL_ADDRESS);
    heeUserDto.setActive(UPDATED_ACTIVE);
    UserTrustDTO trustDto = new UserTrustDTO();
    final String UPDATED_TRUST_CODE = "Ed";
    final long UPDATED_TRUST_ID = 2L;
    final String UPDATED_TRUST_NAME = "Kitten";
    trustDto.setTrustCode(UPDATED_TRUST_CODE);
    trustDto.setTrustId(UPDATED_TRUST_ID);
    trustDto.setTrustName(UPDATED_TRUST_NAME);
    heeUserDto.setAssociatedTrusts(Set.of(trustDto));

    restHeeUserMockMvc.perform(put("/api/hee-users")
            .contentType(TestUtil.JSON)
            .content(TestUtil.convertObjectToJsonBytes(heeUserDto)))
        .andExpect(status().isOk());

    // Validate the HeeUser in the database
    List<HeeUser> heeUserList = heeUserRepository.findAll();
    assertThat(heeUserList).hasSize(databaseSizeBeforeUpdate);
    HeeUser testHeeUser = heeUserRepository.getById(DEFAULT_NAME);
    assertThat(testHeeUser.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testHeeUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
    assertThat(testHeeUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    assertThat(testHeeUser.getGmcId()).isEqualTo(UPDATED_GMC_ID);
    assertThat(testHeeUser.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    assertThat(testHeeUser.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
    assertThat(testHeeUser.isActive()).isEqualTo(UPDATED_ACTIVE);
    assertThat(testHeeUser.getAssociatedTrusts()).extracting("trustCode", "trustId", "trustName")
        .contains(tuple(UPDATED_TRUST_CODE, UPDATED_TRUST_ID, UPDATED_TRUST_NAME));

    assertThat(permissionRepository.findAll()).hasSize(databasePermissionSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void updateNonExistingHeeUser() throws Exception {
    int databaseSizeBeforeUpdate = heeUserRepository.findAll().size();

    // Create the HeeUser
    HeeUserDTO heeUserDTO = heeUserMapper.heeUserToHeeUserDTO(heeUser);
    heeUserDTO.setName(UPDATED_NAME);
    heeUserDTO.setLastName(UPDATED_NAME);
    heeUserDTO.setEmailAddress("");

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restHeeUserMockMvc.perform(put("/api/hee-users")
            .contentType(TestUtil.JSON)
            .content(TestUtil.convertObjectToJsonBytes(heeUserDTO)))
        .andExpect(status().is2xxSuccessful());

    // Validate the HeeUser in the database
    List<HeeUser> heeUserList = heeUserRepository.findAll();
    assertThat(heeUserList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteHeeUser() throws Exception {
    // Initialize the database
    heeUser.setName("test@hee.nhs.uk");
    heeUserRepository.saveAndFlush(heeUser);
    int databaseSizeBeforeDelete = heeUserRepository.findAll().size();

    // Get the heeUser
    restHeeUserMockMvc.perform(delete("/api/hee-users/{id}", heeUser.getName())
            .accept(TestUtil.JSON))
        .andExpect(status().isNoContent());

    // Validate the database is empty
    List<HeeUser> heeUserList = heeUserRepository.findAll();
    assertThat(heeUserList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void getLtftAdminsShouldReturnAdminsWhenRequesterHasMatchingDbc() throws Exception {
    // findByActive requires inner join on roles AND permissions; use an existing seeded permission
    Permission requesterPermission = permissionRepository.findById(REQUESTER_PERMISSION)
        .orElseThrow(() -> new IllegalStateException("Permission not found: " + REQUESTER_PERMISSION));

    Role requesterRole = new Role();
    requesterRole.setName(REQUESTER_ROLE);
    requesterRole.setPermissions(newHashSet(requesterPermission));
    roleRepository.saveAndFlush(requesterRole);

    Role ltftAdminRole = new Role();
    ltftAdminRole.setName(LTFT_ADMIN_ROLE);
    ltftAdminRole.setPermissions(newHashSet(requesterPermission));
    roleRepository.saveAndFlush(ltftAdminRole);

    // Save the requesting user with the matching DBC so they are authorised to view admins
    HeeUser requester = new HeeUser();
    requester.setName("jamesh");
    requester.setLastName("Hudson");
    requester.setEmailAddress("jamesh@hee.nhs.uk");
    requester.setActive(true);
    requester.setRoles(newHashSet(requesterRole));
    requester.setDesignatedBodyCodes(newHashSet(LTFT_DBC));
    heeUserRepository.saveAndFlush(requester);

    // Save an active LTFT admin with the matching role and DBC
    HeeUser ltftAdmin = new HeeUser();
    ltftAdmin.setName("ltft.admin.test");
    ltftAdmin.setFirstName("Admin");
    ltftAdmin.setLastName("User");
    ltftAdmin.setEmailAddress("admin@hee.nhs.uk");
    ltftAdmin.setActive(true);
    ltftAdmin.setRoles(newHashSet(ltftAdminRole));
    ltftAdmin.setDesignatedBodyCodes(newHashSet(LTFT_DBC));
    heeUserRepository.saveAndFlush(ltftAdmin);

    restHeeUserMockMvc.perform(get("/api/hee-users/ltft-admins")
            .param("ltftDbc", LTFT_DBC)
            .header("Authorization", "Bearer " + TOKEN))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.JSON))
        .andExpect(jsonPath("$.[*].name").value(hasItem("ltft.admin.test")));
  }

  @Test
  @Transactional
  public void getLtftAdminsShouldReturnForbiddenWhenRequesterLacksDbc() throws Exception {
    // findByActive requires inner join on roles AND permissions; use an existing seeded permission
    Permission requesterPermission = permissionRepository.findById(REQUESTER_PERMISSION)
        .orElseThrow(() -> new IllegalStateException("Permission not found: " + REQUESTER_PERMISSION));

    Role requesterRole = new Role();
    requesterRole.setName(REQUESTER_ROLE);
    requesterRole.setPermissions(newHashSet(requesterPermission));
    roleRepository.saveAndFlush(requesterRole);

    HeeUser requester = new HeeUser();
    requester.setName("jamesh");
    requester.setLastName("Hudson");
    requester.setEmailAddress("jamesh@hee.nhs.uk");
    requester.setActive(true);
    requester.setRoles(newHashSet(requesterRole));
    requester.setDesignatedBodyCodes(newHashSet("1-OTHER-DBC"));
    heeUserRepository.saveAndFlush(requester);

    restHeeUserMockMvc.perform(get("/api/hee-users/ltft-admins")
            .param("ltftDbc", LTFT_DBC)
            .header("Authorization", "Bearer " + TOKEN))
        .andExpect(status().isForbidden());
  }

  @Test
  @Transactional
  public void getLtftAdminsShouldReturnEmptyListWhenNoAdminsHaveMatchingDbc() throws Exception {
    // findByActive requires inner join on roles AND permissions; use an existing seeded permission
    Permission requesterPermission = permissionRepository.findById(REQUESTER_PERMISSION)
        .orElseThrow(() -> new IllegalStateException("Permission not found: " + REQUESTER_PERMISSION));

    Role requesterRole = new Role();
    requesterRole.setName(REQUESTER_ROLE);
    requesterRole.setPermissions(newHashSet(requesterPermission));
    roleRepository.saveAndFlush(requesterRole);

    Role ltftAdminRole = new Role();
    ltftAdminRole.setName(LTFT_ADMIN_ROLE);
    ltftAdminRole.setPermissions(newHashSet(requesterPermission));
    roleRepository.saveAndFlush(ltftAdminRole);

    // Requester has the DBC; findByActive requires at least one role via inner join
    HeeUser requester = new HeeUser();
    requester.setName("jamesh");
    requester.setLastName("Hudson");
    requester.setEmailAddress("jamesh@hee.nhs.uk");
    requester.setActive(true);
    requester.setRoles(newHashSet(requesterRole));
    requester.setDesignatedBodyCodes(newHashSet(LTFT_DBC));
    heeUserRepository.saveAndFlush(requester);

    // LTFT admin only has a different DBC
    HeeUser ltftAdmin = new HeeUser();
    ltftAdmin.setName("ltft.admin.other");
    ltftAdmin.setFirstName("Other");
    ltftAdmin.setLastName("Admin");
    ltftAdmin.setEmailAddress("other@hee.nhs.uk");
    ltftAdmin.setActive(true);
    ltftAdmin.setRoles(newHashSet(ltftAdminRole));
    ltftAdmin.setDesignatedBodyCodes(newHashSet("1-OTHER-DBC"));
    heeUserRepository.saveAndFlush(ltftAdmin);

    restHeeUserMockMvc.perform(get("/api/hee-users/ltft-admins")
            .param("ltftDbc", LTFT_DBC)
            .header("Authorization", "Bearer " + TOKEN))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.JSON))
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(HeeUser.class);
  }
}

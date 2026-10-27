package com.transformuk.hee.tis.profile.web.rest;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.Permission;
import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.domain.UserProgramme;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.profile.repository.PermissionRepository;
import com.transformuk.hee.tis.profile.repository.RoleRepository;
import com.transformuk.hee.tis.profile.web.rest.errors.ExceptionTranslator;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the permissions required by the LTFT admins and reviewers endpoints of the
 * HeeUserResource REST controller.
 *
 * <p>Unlike HeeUserResourceIntTest, the Spring managed HeeUserResource is used so that the
 * {@code @PreAuthorize} checks are applied. The permissions of the requesting user are set with
 * {@code @WithMockUser}.
 *
 * @see HeeUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
public class HeeUserResourceLtftAccessIntTest {

  // JWT with preferred_username = "jamesh"
  private static final String TOKEN =
      "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJrcEk5UC1hQ3JaTXJ4cG5aeWNnNnlISk9VZ3g0a2hUYS04TlJyMkRhY0g0In0.eyJqdGkiOiI3ZjJiNzA4MC1lYjYxLTQ1YTgtYmUwNS0xYWFjODNkMTY3ZjciLCJleHAiOjE0Nzc1ODA5ODQsIm5iZiI6MCwiaWF0IjoxNDc3NTgwNjg0LCJpc3MiOiJodHRwczovL2Rldi1hcGkudHJhbnNmb3JtY2xvdWQubmV0L2F1dGgvcmVhbG1zL2xpbiIsImF1ZCI6ImFwaS1nYXRld2F5Iiwic3ViIjoiNGY5YWRhY2MtZjEyNC00M2FmLTkyZDMtYjVlZDc3NjhlYTU0IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYXBpLWdhdGV3YXkiLCJub25jZSI6IlA2NnVVT2JJTVBtY19Wb1RudmlYdk1KWE0zYks0RUo3WHJUeHpRbTN0ZUkiLCJhdXRoX3RpbWUiOjE0Nzc1ODA2ODQsInNlc3Npb25fc3RhdGUiOiIyNzg1NDE2Ny1hNWY0LTRkNTItOGQ3OC02OTY3M2ZmZTMwODgiLCJhY3IiOiIxIiwiY2xpZW50X3Nlc3Npb24iOiIyNjNmZDg1Ni02YmZjLTQ4ZWQtODZlNC1jMzFkOTdmYmNlZGMiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly9kZXYtYXBpLnRyYW5zZm9ybWNsb3VkLm5ldCIsImh0dHBzOi8vYXBwcy5saW4ubmhzLnVrIiwiaHR0cDovL2xvY2FsaG9zdDo4MDg3IiwiaHR0cHM6Ly9zdGFnZS1hcHBzLmxpbi5uaHMudWsiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIlJWQWRtaW4iLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJ2aWV3LXByb2ZpbGUiXX19LCJuYW1lIjoiSmFtZXMgSHVkc29uIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiamFtZXNoIiwiZ2l2ZW5fbmFtZSI6IkphbWVzIiwiZmFtaWx5X25hbWUiOiJIdWRzb24ifQ.VJ_8MDyM-1_MMlmhl4N-ZXHyq0G8AlaSLBR4eqlrXLD5CC29dW807WARalNGDqwlNSUuvK6tDiGRt5XKYWo6HDNBL-7Sp3QT2FXew6dD8zJwN8iR34aJGDGg94Kd0PkFESybqQFb4-sntCfKHQ3aRZkpD2WkyNZXEQEuDURYuqyJulqmKXqZxfnYWkd8JgSN1oTyUc4sFPWHjzI9A_y_0Tb13hAvFlPFWwKhCSSZqjRtC65JADOYMeIbyPCsSCKq0DqY2DCZpBivp5Wp0sZu0SSkww_rkwV5tql4gXV5kYmHWJa1rx_OmTAv6UKWYG4aFaqGmvcNhXkZGrweSCEmUw";
  private static final String LTFT_ADMIN_ROLE = "NHSE LTFT Admin";
  private static final String LTFT_DBC = "1-LTFT-TEST";
  // The permission granted by the NHSE LTFT Admin role
  private static final String LTFT_VIEW_PERMISSION = "ltft:view";
  // A permission which does not grant access to the LTFT endpoints
  private static final String OTHER_PERMISSION = "profile:add:modify:entities";

  @Autowired
  private HeeUserResource heeUserResource;

  @Autowired
  private HeeUserRepository heeUserRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PermissionRepository permissionRepository;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  private MockMvc restHeeUserMockMvc;

  @Before
  public void setup() {
    this.restHeeUserMockMvc = MockMvcBuilders.standaloneSetup(heeUserResource)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Test
  @Transactional
  @WithMockUser(authorities = LTFT_VIEW_PERMISSION)
  public void getLtftAdminsShouldReturnAdminsWhenRequesterHasLtftViewPermission()
      throws Exception {
    Role ltftAdminRole = new Role();
    ltftAdminRole.setName(LTFT_ADMIN_ROLE);
    ltftAdminRole.setPermissions(newHashSet(getOrCreatePermission(LTFT_VIEW_PERMISSION)));
    roleRepository.saveAndFlush(ltftAdminRole);

    // Save the requesting user with the matching DBC so they are authorised to view admins
    HeeUser requester = new HeeUser();
    requester.setName("jamesh");
    requester.setLastName("Hudson");
    requester.setEmailAddress("jamesh@hee.nhs.uk");
    requester.setActive(true);
    requester.setRoles(newHashSet(ltftAdminRole));
    requester.setDesignatedBodyCodes(newHashSet(LTFT_DBC));
    heeUserRepository.saveAndFlush(requester);

    // Save an active LTFT admin with the matching role and DBC
    createUser("Admin", "User", true, newHashSet(ltftAdminRole), newHashSet(LTFT_DBC),
        newHashSet());

    restHeeUserMockMvc.perform(get("/api/hee-users/ltft-admins")
            .param("ltftDbc", LTFT_DBC)
            .header("Authorization", "Bearer " + TOKEN))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.JSON))
        .andExpect(jsonPath("$.[*].name").value(hasItem("admin.user")));
  }

  @Test
  @Transactional
  @WithMockUser(authorities = OTHER_PERMISSION)
  public void getLtftAdminsShouldReturnForbiddenWhenRequesterLacksLtftViewPermission()
      throws Exception {
    restHeeUserMockMvc.perform(get("/api/hee-users/ltft-admins")
            .param("ltftDbc", LTFT_DBC)
            .header("Authorization", "Bearer " + TOKEN))
        .andExpect(status().isForbidden());
  }

  @Test
  @Transactional
  @WithMockUser(authorities = LTFT_VIEW_PERMISSION)
  public void getLtftReviewersShouldReturnReviewersWhenRequesterHasLtftViewPermission()
      throws Exception {
    Role ltftAdminRole = new Role();
    ltftAdminRole.setName(LTFT_ADMIN_ROLE);
    ltftAdminRole.setPermissions(newHashSet(getOrCreatePermission(LTFT_VIEW_PERMISSION)));
    roleRepository.saveAndFlush(ltftAdminRole);

    UserProgramme reviewerProgramme = new UserProgramme();
    reviewerProgramme.setProgrammeNumber("10000123");
    reviewerProgramme.setProgrammeName("LTFT Programme");
    reviewerProgramme.setProgrammeId(123L);

    createUser("Reviewer", "One", true, newHashSet(ltftAdminRole), newHashSet(),
        newHashSet(reviewerProgramme));

    restHeeUserMockMvc.perform(get("/api/hee-users/ltft-reviewers")
            .param("programmeNumber", "10000123")
            .header("Authorization", "Bearer " + TOKEN))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.JSON))
        .andExpect(jsonPath("$.[*].name").value(hasItem("reviewer.one")));
  }

  @Test
  @Transactional
  @WithMockUser(authorities = OTHER_PERMISSION)
  public void getLtftReviewersShouldReturnForbiddenWhenRequesterLacksLtftViewPermission()
      throws Exception {
    restHeeUserMockMvc.perform(get("/api/hee-users/ltft-reviewers")
            .param("programmeNumber", "10000123")
            .header("Authorization", "Bearer " + TOKEN))
        .andExpect(status().isForbidden());
  }

  /**
   * Create and persist a reviewer test user.The username is generated from the supplied first and
   * last names using lowercase dot-separated names, and the email address is derived from that
   * username with an {@code @example.com} suffix.
   *
   * @param firstName  the user's first name
   * @param lastName   the user's last name; spaces are converted to dots when generating the
   *                   username
   * @param active     whether the user should be marked active
   * @param roles      the roles to assign to the user
   * @param dbcs       the designated body codes to associate with the user
   * @param programmes the programmes to associate with the user
   */
  private void createUser(String firstName, String lastName, boolean active, Set<Role> roles,
      Set<String> dbcs, Set<UserProgramme> programmes) {
    String username = (firstName + "." + lastName).replace(" ", ".").toLowerCase();
    HeeUser user = new HeeUser();
    user.setName(username);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setEmailAddress(username + "@example.com");
    user.setActive(active);
    user.setRoles(roles);
    user.setDesignatedBodyCodes(dbcs);

    for (UserProgramme programme : programmes) {
      user.addAssociatedProgramme(programme);
    }

    heeUserRepository.saveAndFlush(user);
  }

  /**
   * Helper function to get an existing Permission by name or create and save a new one if it
   * doesn't exist.
   *
   * @param permissionName the name of the Permission to find or create.
   * @return the existing or newly created Permission.
   */
  private Permission getOrCreatePermission(String permissionName) {
    return permissionRepository.findById(permissionName)
        .orElseGet(() -> {
          Permission permission = new Permission();
          permission.setName(permissionName);
          return permissionRepository.saveAndFlush(permission);
        });
  }
}

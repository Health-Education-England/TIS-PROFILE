package com.transformuk.hee.tis.profile.web.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.util.collections.Sets.newSet;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.assembler.UserProfileAssembler;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.Permission;
import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.dto.PermissionType;
import com.transformuk.hee.tis.profile.service.LoginService;
import com.transformuk.hee.tis.profile.web.rest.errors.ExceptionTranslator;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ProfileApp.class)
@ComponentScan("com.transformuk.hee.tis.profile.assembler")
class UserProfileControllerTest {

  private static final String GMC_ID = "123";
  private static final String DESIGNATED_BODY_CODE = "1-DGBODY";
  private static final String PERMISSION_NAME = "Perm1";
  private static final String PERMISSION_PRINCIPAL = "Principal";
  private static final String PERMISSION_RESOURCE = "Resource";
  private static final String PERMISSION_ACTIONS = "Create,Update";
  private static final String PERMISSION_EFFECT = "Allow";
  private static final String RV_ADMIN = "RVAdmin";
  private static final String PCS_ADMIN = "PCSAdmin";

  private static final String[] ROLES = {RV_ADMIN};
  private static final String USER_NAME = "jamesh";
  private static final String TOKEN =
      "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJrcEk5UC1hQ3JaTXJ4cG5aeWNnNnlISk9VZ3g0a2hUYS04TlJyMkRhY0g0In0.eyJqdGkiOiI3ZjJiNzA4MC1lYjYxLTQ1YTgtYmUwNS0xYWFjODNkMTY3ZjciLCJleHAiOjE0Nzc1ODA5ODQsIm5iZiI6MCwiaWF0IjoxNDc3NTgwNjg0LCJpc3MiOiJodHRwczovL2Rldi1hcGkudHJhbnNmb3JtY2xvdWQubmV0L2F1dGgvcmVhbG1zL2xpbiIsImF1ZCI6ImFwaS1nYXRld2F5Iiwic3ViIjoiNGY5YWRhY2MtZjEyNC00M2FmLTkyZDMtYjVlZDc3NjhlYTU0IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYXBpLWdhdGV3YXkiLCJub25jZSI6IlA2NnVVT2JJTVBtY19Wb1RudmlYdk1KWE0zYks0RUo3WHJUeHpRbTN0ZUkiLCJhdXRoX3RpbWUiOjE0Nzc1ODA2ODQsInNlc3Npb25fc3RhdGUiOiIyNzg1NDE2Ny1hNWY0LTRkNTItOGQ3OC02OTY3M2ZmZTMwODgiLCJhY3IiOiIxIiwiY2xpZW50X3Nlc3Npb24iOiIyNjNmZDg1Ni02YmZjLTQ4ZWQtODZlNC1jMzFkOTdmYmNlZGMiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly9kZXYtYXBpLnRyYW5zZm9ybWNsb3VkLm5ldCIsImh0dHBzOi8vYXBwcy5saW4ubmhzLnVrIiwiaHR0cDovL2xvY2FsaG9zdDo4MDg3IiwiaHR0cHM6Ly9zdGFnZS1hcHBzLmxpbi5uaHMudWsiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIlJWQWRtaW4iLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJ2aWV3LXByb2ZpbGUiXX19LCJuYW1lIjoiSmFtZXMgSHVkc29uIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiamFtZXNoIiwiZ2l2ZW5fbmFtZSI6IkphbWVzIiwiZmFtaWx5X25hbWUiOiJIdWRzb24ifQ.VJ_8MDyM-1_MMlmhl4N-ZXHyq0G8AlaSLBR4eqlrXLD5CC29dW807WARalNGDqwlNSUuvK6tDiGRt5XKYWo6HDNBL-7Sp3QT2FXew6dD8zJwN8iR34aJGDGg94Kd0PkFESybqQFb4-sntCfKHQ3aRZkpD2WkyNZXEQEuDURYuqyJulqmKXqZxfnYWkd8JgSN1oTyUc4sFPWHjzI9A_y_0Tb13hAvFlPFWwKhCSSZqjRtC65JADOYMeIbyPCsSCKq0DqY2DCZpBivp5Wp0sZu0SSkww_rkwV5tql4gXV5kYmHWJa1rx_OmTAv6UKWYG4aFaqGmvcNhXkZGrweSCEmUw";
  private static final String UPDATED_TOKEN =
      "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJrcEk5UC1hQ3JaTXJ4cG5aeWNnNnlISk9VZ3g0a2hUYS04TlJyMkRhY0g0In0.eyJqdGkiOiI4ZGI4NWY1NC0zMmQ3LTRiYWItOWJhMi05ZjJmODhiYTk4ODQiLCJleHAiOjE1MTMyNDM1MzcsIm5iZiI6MCwiaWF0IjoxNTEzMjQzNDE3LCJpc3MiOiJodHRwczovL2Rldi1hcHBzLnRpcy5uaHMudWsvYXV0aC9yZWFsbXMvbGluIiwiYXVkIjoiYXBpLXRva2VucyIsInN1YiI6IjRmOWFkYWNjLWYxMjQtNDNhZi05MmQzLWI1ZWQ3NzY4ZWE1NCIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFwaS10b2tlbnMiLCJhdXRoX3RpbWUiOjAsInNlc3Npb25fc3RhdGUiOiI5YTk2ZWRhNy1mZmM5LTRlZTktYmZkYy02OTkxYTkyOTI0M2MiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIlByb2ZpbGVBZG1pbiIsIlJWQWRtaW4iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJuYW1lIjoiSmFtZXMgSHVkc29uIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiamFtZXNoIiwiZ2l2ZW5fbmFtZSI6IkphbWVzIiwiZmFtaWx5X25hbWUiOiJIdWRzb24ifQ.DbAn_sTwlATr2OGiULK1043pwiGfgH3H6uiqyDMZ7Nh-FxZ0ds9m7SyqWFYkGXkHWX1SURTs36Wvi8WzEMfzR283h2S2lC_a8AfGtd0gYKKTgyWgyOoOqvOnvntE47L0U3-ICfQZ8NFfspGvBY0NnSeTlUjLHdlZWH7iQKgSccnnHM31Uu_rnfQRQoF16BRojIs5kT6fPLN8J69YlFQELyKuQ9-Zb4K_q_hXoOVFkikogTNEnD9OpdpauICICTp9Jyo3em1aGwcvlt36zhvSEZDeIrTo5fVmcRxEpwwubDAcgVkO7zvZVAGlTjI2IJLLKH_N8PhLOs-YWO2NM2zxXg";

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;
  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
  @Autowired
  private ExceptionTranslator exceptionTranslator;
  @Autowired
  private UserProfileAssembler assembler;

  @MockBean
  private LoginService loginService;

  private MockMvc mvc;
  @Captor
  private ArgumentCaptor<AuditEvent> captor;

  @BeforeEach
  void setup() {
    MockitoAnnotations.initMocks(this);
    UserProfileController profileController = new UserProfileController(loginService, assembler);
    mvc = MockMvcBuilders.standaloneSetup(profileController)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Test
  void shouldReturnUnauthorizedWhenNoToken() throws Exception {
    mvc.perform(get("/api/userupdate"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void shouldPreferAuthorizationTokenWhenAvailable() throws Exception {
    given(loginService.getUserByToken("authToken")).willReturn(getUser());

    mvc.perform(get("/api/userupdate")
            .header("OIDC_access_token", "oidcToken")
            .header("Authorization", "authToken"))
        .andExpect(status().isOk());

    verify(loginService).getUserByToken("authToken");
  }

  @Test
  void shouldUseOidcAccessTokenWhenAuthorizationTokenNotAvailable() throws Exception {
    given(loginService.getUserByToken("oidcToken")).willReturn(getUser());

    mvc.perform(get("/api/userupdate")
            .header("OIDC_access_token", "oidcToken"))
        .andExpect(status().isOk());

    verify(loginService).getUserByToken("oidcToken");
  }

  @Test
  void shouldReturnUserProfileDetails() throws Exception {
    //Given
    HeeUser user = getUser();

    given(loginService.getUserByToken(TOKEN)).willReturn(user);

    // When & then
    mvc.perform(get("/api/userinfo")
            .header("OIDC_access_token", TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userName").value(USER_NAME))
        .andExpect(jsonPath("$.gmcId").value(GMC_ID))
        .andExpect(jsonPath("$.designatedBodyCodes[0]").value(DESIGNATED_BODY_CODE))
        .andExpect(jsonPath("$.roles").isNotEmpty())
        .andExpect(jsonPath("$.roles").value(hasItems(ROLES)));
  }

  @Test
  void shouldGETListOfUsers() throws Exception {
    //Given
    HeeUser user = new HeeUser(USER_NAME);
    user.setGmcId("123");
    user.setFirstName("James");
    List<HeeUser> userList = Lists.newArrayList(user);
    given(loginService.getUsers(newSet(DESIGNATED_BODY_CODE), null)).willReturn(userList);

    //When
    mvc.perform(get("/api/users").param("designatedBodyCode",
            DESIGNATED_BODY_CODE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.total").value(1))
        .andExpect(jsonPath("$.users[0].name").value("jamesh"))
        .andExpect(jsonPath("$.users[0].gmcId").value("123"));
  }

  @Test
  void shouldGETListOfUsersByPermissions() throws Exception {
    //Given
    String permissions = "revalidation:submit:to:gmc,revalidation:submit:on:behalf:of:ro";
    HeeUser user = new HeeUser(USER_NAME);
    user.setGmcId("123");
    user.setFirstName("James");
    List<HeeUser> userList = Lists.newArrayList(user);
    given(loginService.getUsers(newSet(DESIGNATED_BODY_CODE), permissions)).willReturn(userList);

    //When
    mvc.perform(get("/api/users")
            .param("designatedBodyCode", DESIGNATED_BODY_CODE)
            .param("permissions", permissions))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.total").value(1))
        .andExpect(jsonPath("$.users[0].name").value("jamesh"))
        .andExpect(jsonPath("$.users[0].gmcId").value("123"));
  }

  @Test
  void shouldGetRoUserByDBC() throws Exception {
    //Given
    HeeUser user = getUser();
    given(loginService.getRVOfficer(DESIGNATED_BODY_CODE)).willReturn(user);

    //When
    mvc.perform(get("/api/users/ro-user/" + DESIGNATED_BODY_CODE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userName").value(USER_NAME))
        .andExpect(jsonPath("$.gmcId").value(GMC_ID));
  }

  @Test
  void shouldReturnPermissionsAndRoles() throws Exception {
    //Given
    HeeUser user = getUser();

    given(loginService.getUserByToken(TOKEN)).willReturn(user);

    //When
    mvc.perform(get("/api/userinfo")
            .header("OIDC_access_token", TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userName").value(USER_NAME))
        .andExpect(jsonPath("$.roles").value(hasItems(ROLES)))
        .andExpect(jsonPath("$.permissions").value(hasItems("Perm1")))
        .andExpect(jsonPath("$.permissionPolicies[0].name").value(PERMISSION_NAME))
        .andExpect(jsonPath("$.permissionPolicies[0].principal").value(PERMISSION_PRINCIPAL))
        .andExpect(jsonPath("$.permissionPolicies[0].resource").value(PERMISSION_RESOURCE))
        .andExpect(jsonPath("$.permissionPolicies[0].actions").value(hasItems("Create", "Update")))
        .andExpect(jsonPath("$.permissionPolicies[0].effect").value(PERMISSION_EFFECT));
  }

  @Test
  void shouldFailAuthenticationWhenNoHeaders() throws Exception {
    mvc.perform(get("/api/userinfo"))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.message").value(containsString("error")));
  }


  private HeeUser getUser() {
    HeeUser user = new HeeUser(USER_NAME);
    user.setGmcId(GMC_ID);
    user.setDesignatedBodyCodes(newSet(DESIGNATED_BODY_CODE));
    Set<Permission> permissions = newSet(new Permission(
        PERMISSION_NAME, PermissionType.CONCERN, "Description",
        PERMISSION_PRINCIPAL, PERMISSION_RESOURCE, PERMISSION_ACTIONS, PERMISSION_EFFECT)
    );
    user.setRoles(newSet(new Role(RV_ADMIN, permissions)));
    return user;
  }

  private Role getRole(String rolename) {
    Role role = new Role();
    Set<Permission> permissions = newSet(new Permission(
        PERMISSION_NAME, PermissionType.CONCERN, "Description",
        PERMISSION_PRINCIPAL, PERMISSION_RESOURCE, PERMISSION_ACTIONS, PERMISSION_EFFECT));
    role.setName(rolename);
    role.setPermissions(permissions);
    return role;
  }

}

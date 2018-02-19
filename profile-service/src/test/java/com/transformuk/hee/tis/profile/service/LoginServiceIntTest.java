package com.transformuk.hee.tis.profile.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.profile.repository.RoleRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)

public class LoginServiceIntTest {

  private static final String TOKEN_NO_GROUPS_NO_ROLES_NO_ATTR =
      "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJrcEk5UC1hQ3JaTXJ4cG5aeWNnNnlISk9VZ3g0a2hUYS04TlJyMkRhY0g0In0.eyJqdGkiOiI4NWQ5ZjVhZS03OWFiLTQxZDktOGFjYi1iZTM2NzA2YWI3M2UiLCJleHAiOjE1MTc0OTUwNzEsIm5iZiI6MCwiaWF0IjoxNTE3NDk0OTUxLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODcvYXV0aC9yZWFsbXMvbGluIiwiYXVkIjoiYXBpLXRva2VucyIsInN1YiI6IjBhYWIzOGI0LWYxYjktNGFmOC1hZjQyLWY3MDU5OWQ0NGY2ZiIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFwaS10b2tlbnMiLCJhdXRoX3RpbWUiOjAsInNlc3Npb25fc3RhdGUiOiIxMzJjNmJiZi1hZWRkLTRiMGMtYjYyMi0wOTBkNmQ1NWRkYjUiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6IlRlc3QgTm9Sb2xlc05vR3JvdXBzTm9BdHRyaWJ1dGVzIiwicHJlZmVycmVkX3VzZXJuYW1lIjoidGVzdG5vcm9sZXNub2F0dHJpYnV0ZXNub2dyb3VwcyIsImdpdmVuX25hbWUiOiJUZXN0IiwiZmFtaWx5X25hbWUiOiJOb1JvbGVzTm9Hcm91cHNOb0F0dHJpYnV0ZXMiLCJlbWFpbCI6InRlc3RAZmRhZmQuY29tIn0.KHz9_qZJxLRRiE42se4A03RKkEmJgBND7mUu74OPC1_u-Dxqeq_0hnoc82dTEyk7P4xeeOsg_t62uEPOgKFcbyKRPYDjUm1JHH0uCac3uDulCStL1FgyQ6DZWhxY4PCS_ZOIt047OwciCX_EzQH6wt52vxO63ybHXJFcc1wUcRr0EmR1g4TWjXtirpvGNPrA0BhVzO-wcmXEWeMBRyMwAgKB5XqU9TEE-DfBGBomZX5S1bIuaEEWaYL7lpzjzleHz4RaS-DhLQb8HzoFqJJ_gGflLuK8ceUT5oHiDijOR7PdI7bF-kSsy7175bIKjvyhyxxKB6_Sk6TQvr72jej_JQ";

  private static final String TOKEN_JAMESH_RVADMIN =
      "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJrcEk5UC1hQ3JaTXJ4cG5aeWNnNnlISk9VZ3g0a2hUYS04TlJyMkRhY0g0In0.eyJqdGkiOiI3ZjJiNzA4MC1lYjYxLTQ1YTgtYmUwNS0xYWFjODNkMTY3ZjciLCJleHAiOjE0Nzc1ODA5ODQsIm5iZiI6MCwiaWF0IjoxNDc3NTgwNjg0LCJpc3MiOiJodHRwczovL2Rldi1hcGkudHJhbnNmb3JtY2xvdWQubmV0L2F1dGgvcmVhbG1zL2xpbiIsImF1ZCI6ImFwaS1nYXRld2F5Iiwic3ViIjoiNGY5YWRhY2MtZjEyNC00M2FmLTkyZDMtYjVlZDc3NjhlYTU0IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYXBpLWdhdGV3YXkiLCJub25jZSI6IlA2NnVVT2JJTVBtY19Wb1RudmlYdk1KWE0zYks0RUo3WHJUeHpRbTN0ZUkiLCJhdXRoX3RpbWUiOjE0Nzc1ODA2ODQsInNlc3Npb25fc3RhdGUiOiIyNzg1NDE2Ny1hNWY0LTRkNTItOGQ3OC02OTY3M2ZmZTMwODgiLCJhY3IiOiIxIiwiY2xpZW50X3Nlc3Npb24iOiIyNjNmZDg1Ni02YmZjLTQ4ZWQtODZlNC1jMzFkOTdmYmNlZGMiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly9kZXYtYXBpLnRyYW5zZm9ybWNsb3VkLm5ldCIsImh0dHBzOi8vYXBwcy5saW4ubmhzLnVrIiwiaHR0cDovL2xvY2FsaG9zdDo4MDg3IiwiaHR0cHM6Ly9zdGFnZS1hcHBzLmxpbi5uaHMudWsiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIlJWQWRtaW4iLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJ2aWV3LXByb2ZpbGUiXX19LCJuYW1lIjoiSmFtZXMgSHVkc29uIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiamFtZXNoIiwiZ2l2ZW5fbmFtZSI6IkphbWVzIiwiZmFtaWx5X25hbWUiOiJIdWRzb24ifQ.VJ_8MDyM-1_MMlmhl4N-ZXHyq0G8AlaSLBR4eqlrXLD5CC29dW807WARalNGDqwlNSUuvK6tDiGRt5XKYWo6HDNBL-7Sp3QT2FXew6dD8zJwN8iR34aJGDGg94Kd0PkFESybqQFb4-sntCfKHQ3aRZkpD2WkyNZXEQEuDURYuqyJulqmKXqZxfnYWkd8JgSN1oTyUc4sFPWHjzI9A_y_0Tb13hAvFlPFWwKhCSSZqjRtC65JADOYMeIbyPCsSCKq0DqY2DCZpBivp5Wp0sZu0SSkww_rkwV5tql4gXV5kYmHWJa1rx_OmTAv6UKWYG4aFaqGmvcNhXkZGrweSCEmUw";

  private static final String TOKEN_JAMESH_PLUS_ROLE =
      "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJrcEk5UC1hQ3JaTXJ4cG5aeWNnNnlISk9VZ3g0a2hUYS04TlJyMkRhY0g0In0.eyJqdGkiOiIyNzIwMzVlNy01MDZjLTQzY2QtYjhhZS02YjhiOTI4ODU4ODciLCJleHAiOjE1MTc5MDYzODgsIm5iZiI6MCwiaWF0IjoxNTE3OTA2MjY4LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODcvYXV0aC9yZWFsbXMvbGluIiwiYXVkIjoiYXBpLXRva2VucyIsInN1YiI6IjRmOWFkYWNjLWYxMjQtNDNhZi05MmQzLWI1ZWQ3NzY4ZWE1NCIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFwaS10b2tlbnMiLCJhdXRoX3RpbWUiOjAsInNlc3Npb25fc3RhdGUiOiJhNWI3ZjRiMC03NWMxLTRiYTAtODFhNy04MTY2NTAwMzExMGMiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIlJWQWRtaW4iLCJ1bWFfYXV0aG9yaXphdGlvbiIsIkNvbmNlcm5zQWRtaW4iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJuYW1lIjoiSmFtZXMgSHVkc29uIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiamFtZXNoIiwiZ2l2ZW5fbmFtZSI6IkphbWVzIiwiZmFtaWx5X25hbWUiOiJIdWRzb24ifQ.dPm51HhYQEU6U2E1YAgE8JYgaqlcRW7KK48XkGKtnBAcmEIJKVIYUsMed-OyDmdDQz9_eQg3j08PIu4sYxknlNzGMDYvf-CL61VcI1Ip59Ngg03WIr7IhYwJuqF1AA-LYvUoCWKYi44DPk__4y8QrU7_bGmkXN0Az68dQwqRfPk8v2B1u0fdjNFOFUI5VhdOsNRvBAF4w92pvH2OhpBeyuGAeB_DgDCpuRRzwrDNAlX4c7V3pxpTtu8nNyvw1uYQeu_ewf6YsU0RfXwUKh77u4xvNKzVTOoA8fLPNJXiaZ3lRodw1HRst8Oy32q8c732PtkA0vhaTLjgDQPDOiiCBQ";

  private static final String LO_WESSEX_CODE = "1-AIIDHJ";
  private static final String LO_WESSEX_NAME = "Wessex";
  private static final String JH_USERNAME = "jamesh";
  private static final String CONCERNS_ROLE = "ConcernsAdmin";
  private static final String GMC_ATTR = "GMC_ID";
  private static final String GMC_VALUE = "GMC123";


  @Autowired
  private LoginService loginService;

  @Autowired
  private HeeUserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @MockBean
  private KeycloakAdminClientService keycloakAdminClientService;

  @Test
  @Transactional
  public void shouldCreateUser() throws Exception {
    // Given
    Map<String, List<String>> userAttributes = Maps.newHashMap();
    userAttributes.put(GMC_ATTR, Lists.newArrayList(GMC_VALUE));
    int databaseSizeBeforeCreate = userRepository.findAll().size();
    given(keycloakAdminClientService.getUserAttributes(JH_USERNAME)).willReturn(userAttributes);

    // When
    HeeUser heeUser = loginService.createUserByToken(TOKEN_JAMESH_RVADMIN);
    int databaseSizeAfterCreating = userRepository.findAll().size();
    HeeUser returnedUser = loginService.getUserByToken(TOKEN_JAMESH_RVADMIN);

    // Then
    Assert.assertEquals(heeUser, returnedUser);
    Assert.assertEquals(1,databaseSizeAfterCreating-databaseSizeBeforeCreate);
  }

  @Test(expected = EntityNotFoundException.class)
  @Transactional
  public void shouldDealWithKeyCloakUserWithNoRolesGroupsOrAttributes() throws Exception {
    // Given
    HeeUser newUser = loginService.createUserByToken(TOKEN_NO_GROUPS_NO_ROLES_NO_ATTR);

    // When
    HeeUser returnedUser = loginService.getUserByToken(TOKEN_NO_GROUPS_NO_ROLES_NO_ATTR);
  }

  @Test
  @Transactional
  public void shouldUpdateRolesInRepository() {
    // Given
    HeeUser newUser = loginService.createUserByToken(TOKEN_JAMESH_RVADMIN);
    int dataBaseSizeBeforeUpdate = userRepository.findAll().size();
    Map<String, List<String>> userAttributes = Maps.newHashMap();
    userAttributes.put(GMC_ATTR, Lists.newArrayList(GMC_VALUE));
    given(keycloakAdminClientService.getUserAttributes(JH_USERNAME)).willReturn(userAttributes);
    Role addedRole = roleRepository.findByName(CONCERNS_ROLE);

    // Check new user in DB
    HeeUser createdUser = loginService.getUserByToken(TOKEN_JAMESH_RVADMIN);
    Assert.assertEquals(newUser,createdUser);
    Assert.assertFalse(createdUser.getRoles().contains(addedRole));
    // Update user
    HeeUser updatedUser = loginService.updateUserByToken(TOKEN_JAMESH_PLUS_ROLE);
    int dataBaseSizeAfterUpdate = userRepository.findAll().size();

    //Then
    Assert.assertEquals(dataBaseSizeBeforeUpdate,dataBaseSizeAfterUpdate);
    Assert.assertTrue(updatedUser.getRoles().contains(addedRole));
  }

  @Test
  @Transactional
  public void shouldRemoveUserRoles() {
    // Given
    Role addedRole = roleRepository.findByName(CONCERNS_ROLE);
    HeeUser newUser = loginService.createUserByToken(TOKEN_JAMESH_PLUS_ROLE);
    int dataBaseSizeBeforeUpdate = userRepository.findAll().size();
    Map<String, List<String>> userAttributes = Maps.newHashMap();
    userAttributes.put(GMC_ATTR, Lists.newArrayList(GMC_VALUE));
    given(keycloakAdminClientService.getUserAttributes(JH_USERNAME)).willReturn(userAttributes);

    // Check new user in DB
    HeeUser createdUser = loginService.getUserByToken(TOKEN_JAMESH_PLUS_ROLE);
    Assert.assertEquals(newUser,createdUser);
    Assert.assertTrue(createdUser.getRoles().contains(addedRole));
    // Update user
    HeeUser updatedUser = loginService.updateUserByToken(TOKEN_JAMESH_RVADMIN);
    int dataBaseSizeAfterUpdate = userRepository.findAll().size();

    //Then
    Assert.assertEquals(dataBaseSizeBeforeUpdate,dataBaseSizeAfterUpdate);
    Assert.assertFalse(updatedUser.getRoles().contains(addedRole));
  }

  @Test
  @Transactional
  public void shouldAddUserDBCs() {
    // Given
    HeeUser newUser = loginService.createUserByToken(TOKEN_JAMESH_RVADMIN);
    int dataBaseSizeBeforeUpdate = userRepository.findAll().size();
    Map<String, List<String>> userAttributes = Maps.newHashMap();
    userAttributes.put(GMC_ATTR, Lists.newArrayList(GMC_VALUE));
    given(keycloakAdminClientService.getUserAttributes(JH_USERNAME)).willReturn(userAttributes);
    GroupRepresentation wessexGroup = new GroupRepresentation();
    wessexGroup.setName(LO_WESSEX_NAME);
    List<GroupRepresentation> groupRepresentationList = Lists.newArrayList(wessexGroup);
    // Check user is not in group
    HeeUser user = userRepository.findByActive(JH_USERNAME);
    Assert.assertFalse(user.getDesignatedBodyCodes().contains(LO_WESSEX_CODE));

    // When
    when(keycloakAdminClientService.getUserGroups(JH_USERNAME)).thenReturn(groupRepresentationList);
    // Set dbc
    loginService.updateUserByToken(TOKEN_JAMESH_RVADMIN);

    // Then
    HeeUser updatedUser = userRepository.findByActive(JH_USERNAME);
    int dataBaseSizeAfterUpdate = userRepository.findAll().size();
    Assert.assertEquals(dataBaseSizeBeforeUpdate,dataBaseSizeAfterUpdate);
    Assert.assertTrue(updatedUser.getDesignatedBodyCodes().contains(LO_WESSEX_CODE));
  }

  @Test
  @Transactional
  public void shouldRemoveDBCs() {
    // Given
    loginService.createUserByToken(TOKEN_JAMESH_RVADMIN);
    int dataBaseSizeBeforeUpdate = userRepository.findAll().size();
    Map<String, List<String>> userAttributes = Maps.newHashMap();
    userAttributes.put(GMC_ATTR, Lists.newArrayList(GMC_VALUE));
    given(keycloakAdminClientService.getUserAttributes(JH_USERNAME)).willReturn(userAttributes);
    GroupRepresentation wessexGroup = new GroupRepresentation();
    wessexGroup.setName(LO_WESSEX_NAME);
    List<GroupRepresentation> groupRepresentationList = Lists.newArrayList(wessexGroup);
    // Check user is in group
    given(keycloakAdminClientService.getUserGroups(JH_USERNAME)).willReturn(groupRepresentationList);
    loginService.updateUserByToken(TOKEN_JAMESH_RVADMIN);
    HeeUser user = userRepository.findByActive(JH_USERNAME);
    Assert.assertTrue(user.getDesignatedBodyCodes().contains(LO_WESSEX_CODE));

    // When
    when(keycloakAdminClientService.getUserGroups(JH_USERNAME)).thenReturn(Lists.newArrayList());
    // Set dbc
    loginService.updateUserByToken(TOKEN_JAMESH_RVADMIN);

    // Then
    HeeUser updatedUser = userRepository.findByActive(JH_USERNAME);
    int dataBaseSizeAfterUpdate = userRepository.findAll().size();
    Assert.assertEquals(dataBaseSizeBeforeUpdate,dataBaseSizeAfterUpdate);
    Assert.assertFalse(updatedUser.getDesignatedBodyCodes().contains(LO_WESSEX_CODE));
  }
}

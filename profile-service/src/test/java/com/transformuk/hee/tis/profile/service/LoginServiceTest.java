package com.transformuk.hee.tis.profile.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.profile.repository.RoleRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.collections.Sets.newSet;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

  public static final String USER_NAME_JAMESH = "jamesh";
  public static final String USER_NAME_ALISON = "alison";
  public static final String DESIGNATED_BODY_CODE_1DGBODY = "1-DGBODY";
  public static final String DESIGNATED_BODY_CODE_1AIIDWT = "1-AIIDWT";
  public static final String DBC_ATTRIBUTE = "DBC";
  public static final String GMC_ID_ATTRIBUTE = "GMC_ID";

  public static final String TOKEN =
      "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJrcEk5UC1hQ3JaTXJ4cG5aeWNnNnlISk9VZ3g0a2hUYS04TlJyMkRhY0g0In0.eyJqdGkiOiI3ZjJiNzA4MC1lYjYxLTQ1YTgtYmUwNS0xYWFjODNkMTY3ZjciLCJleHAiOjE0Nzc1ODA5ODQsIm5iZiI6MCwiaWF0IjoxNDc3NTgwNjg0LCJpc3MiOiJodHRwczovL2Rldi1hcGkudHJhbnNmb3JtY2xvdWQubmV0L2F1dGgvcmVhbG1zL2xpbiIsImF1ZCI6ImFwaS1nYXRld2F5Iiwic3ViIjoiNGY5YWRhY2MtZjEyNC00M2FmLTkyZDMtYjVlZDc3NjhlYTU0IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYXBpLWdhdGV3YXkiLCJub25jZSI6IlA2NnVVT2JJTVBtY19Wb1RudmlYdk1KWE0zYks0RUo3WHJUeHpRbTN0ZUkiLCJhdXRoX3RpbWUiOjE0Nzc1ODA2ODQsInNlc3Npb25fc3RhdGUiOiIyNzg1NDE2Ny1hNWY0LTRkNTItOGQ3OC02OTY3M2ZmZTMwODgiLCJhY3IiOiIxIiwiY2xpZW50X3Nlc3Npb24iOiIyNjNmZDg1Ni02YmZjLTQ4ZWQtODZlNC1jMzFkOTdmYmNlZGMiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly9kZXYtYXBpLnRyYW5zZm9ybWNsb3VkLm5ldCIsImh0dHBzOi8vYXBwcy5saW4ubmhzLnVrIiwiaHR0cDovL2xvY2FsaG9zdDo4MDg3IiwiaHR0cHM6Ly9zdGFnZS1hcHBzLmxpbi5uaHMudWsiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIlJWQWRtaW4iLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJ2aWV3LXByb2ZpbGUiXX19LCJuYW1lIjoiSmFtZXMgSHVkc29uIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiamFtZXNoIiwiZ2l2ZW5fbmFtZSI6IkphbWVzIiwiZmFtaWx5X25hbWUiOiJIdWRzb24ifQ.VJ_8MDyM-1_MMlmhl4N-ZXHyq0G8AlaSLBR4eqlrXLD5CC29dW807WARalNGDqwlNSUuvK6tDiGRt5XKYWo6HDNBL-7Sp3QT2FXew6dD8zJwN8iR34aJGDGg94Kd0PkFESybqQFb4-sntCfKHQ3aRZkpD2WkyNZXEQEuDURYuqyJulqmKXqZxfnYWkd8JgSN1oTyUc4sFPWHjzI9A_y_0Tb13hAvFlPFWwKhCSSZqjRtC65JADOYMeIbyPCsSCKq0DqY2DCZpBivp5Wp0sZu0SSkww_rkwV5tql4gXV5kYmHWJa1rx_OmTAv6UKWYG4aFaqGmvcNhXkZGrweSCEmUw";

  private static final String UPDATED_TOKEN =
      "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJrcEk5UC1hQ3JaTXJ4cG5aeWNnNnlISk9VZ3g0a2hUYS04TlJyMkRhY0g0In0.eyJqdGkiOiI4ZGI4NWY1NC0zMmQ3LTRiYWItOWJhMi05ZjJmODhiYTk4ODQiLCJleHAiOjE1MTMyNDM1MzcsIm5iZiI6MCwiaWF0IjoxNTEzMjQzNDE3LCJpc3MiOiJodHRwczovL2Rldi1hcHBzLnRpcy5uaHMudWsvYXV0aC9yZWFsbXMvbGluIiwiYXVkIjoiYXBpLXRva2VucyIsInN1YiI6IjRmOWFkYWNjLWYxMjQtNDNhZi05MmQzLWI1ZWQ3NzY4ZWE1NCIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFwaS10b2tlbnMiLCJhdXRoX3RpbWUiOjAsInNlc3Npb25fc3RhdGUiOiI5YTk2ZWRhNy1mZmM5LTRlZTktYmZkYy02OTkxYTkyOTI0M2MiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIlByb2ZpbGVBZG1pbiIsIlJWQWRtaW4iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJuYW1lIjoiSmFtZXMgSHVkc29uIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiamFtZXNoIiwiZ2l2ZW5fbmFtZSI6IkphbWVzIiwiZmFtaWx5X25hbWUiOiJIdWRzb24ifQ.DbAn_sTwlATr2OGiULK1043pwiGfgH3H6uiqyDMZ7Nh-FxZ0ds9m7SyqWFYkGXkHWX1SURTs36Wvi8WzEMfzR283h2S2lC_a8AfGtd0gYKKTgyWgyOoOqvOnvntE47L0U3-ICfQZ8NFfspGvBY0NnSeTlUjLHdlZWH7iQKgSccnnHM31Uu_rnfQRQoF16BRojIs5kT6fPLN8J69YlFQELyKuQ9-Zb4K_q_hXoOVFkikogTNEnD9OpdpauICICTp9Jyo3em1aGwcvlt36zhvSEZDeIrTo5fVmcRxEpwwubDAcgVkO7zvZVAGlTjI2IJLLKH_N8PhLOs-YWO2NM2zxXg";

  @Mock
  private HeeUserRepository userRepository;
  @Mock
  private RoleRepository roleRepositoryMock;
  @Mock
  private KeycloakAdminClientService keycloakAdminClientServiceMock;
  @Mock
  private Role rvAdminRoleMock;
  @Mock
  private Role profileAdminRoleMock;

  @InjectMocks
  private LoginService service;


  @Test
  public void shouldFetchUsersWithPermissions() {
    // given
    String permissions = "revalidation:submit:to:gmc,revalidation:submit:on:behalf:of:ro";
    List<HeeUser> users = Lists.newArrayList();
    given(userRepository.findDistinctByExactDesignatedBodyCodesAndPermissions(
        any(String.class), anyListOf(String.class))).willReturn(users);

    // when
    List<HeeUser> actualUsers = service.getUsers(newSet(DESIGNATED_BODY_CODE_1DGBODY), permissions);

    // then
    assertSame(users, actualUsers);
  }

  @Test
  public void shouldFetchUsersWithOutPermissions() {
    // given
    List<HeeUser> users = Lists.newArrayList();
    given(userRepository.findDistinctByExactDesignatedBodyCodes(
        DESIGNATED_BODY_CODE_1DGBODY)).willReturn(users);

    // when
    List<HeeUser> actualUsers = service.getUsers(newSet(DESIGNATED_BODY_CODE_1DGBODY), null);

    // then
    assertSame(users, actualUsers);
  }

  /**
   * User Alison has two designatedBodyCodes 1-AIIDWT,1-DGBODY and
   * user Jamesh has one designatedBodyCodes 1-AIIDWT,1-DGBODY
   * when we fetch by two designatedBodyCodes 1-AIIDWT,1-DGBODY it should match with other users exactly
   * same designatedBodyCodes
   * Expected: two user Alison and Jamesh
   */
  @Test
  public void shouldFetchTwoUsersExactDesignatedBodyCodes() {
    // given
    Set<String> designatedBodyCodes = Sets.newHashSet(DESIGNATED_BODY_CODE_1DGBODY, DESIGNATED_BODY_CODE_1AIIDWT);
    HeeUser aUser = new HeeUser();
    aUser.setName(USER_NAME_ALISON);
    aUser.setDesignatedBodyCodes(designatedBodyCodes);

    HeeUser bUser = new HeeUser();
    bUser.setName(USER_NAME_JAMESH);
    bUser.setDesignatedBodyCodes(designatedBodyCodes);

    List<HeeUser> users = Lists.newArrayList(aUser, bUser);
    given(userRepository.findDistinctByExactDesignatedBodyCodes(
        DESIGNATED_BODY_CODE_1AIIDWT + "," + DESIGNATED_BODY_CODE_1DGBODY)).willReturn(users);

    // when
    List<HeeUser> actualUsers = service.getUsers(newSet(DESIGNATED_BODY_CODE_1DGBODY, DESIGNATED_BODY_CODE_1AIIDWT),
        null);

    // then
    assertSame(users, actualUsers);

  }

  /**
   * User Alison has two designatedBodyCodes 1-AIIDWT,1-DGBODY and
   * user Jamesh has one designatedBodyCodes 1-AIIDWT
   * when we fetch by two designatedBodyCodes 1-AIIDWT,1-DGBODY it should match with other users exactly
   * same designatedBodyCodes
   * Expected: only one user Alison
   */
  @Test
  public void shouldFetchOneUsersExactDesignatedBodyCodes1() {
    // given
    Set<String> twoDesignatedBodyCodes = Sets.newHashSet(DESIGNATED_BODY_CODE_1DGBODY, DESIGNATED_BODY_CODE_1AIIDWT);
    Set<String> oneDesignatedBodyCodes = Sets.newHashSet(DESIGNATED_BODY_CODE_1DGBODY);
    HeeUser aUser = new HeeUser();
    aUser.setName(USER_NAME_ALISON);
    aUser.setDesignatedBodyCodes(twoDesignatedBodyCodes);

    HeeUser bUser = new HeeUser();
    bUser.setName(USER_NAME_JAMESH);
    bUser.setDesignatedBodyCodes(oneDesignatedBodyCodes);

    List<HeeUser> users = Lists.newArrayList(aUser, bUser);
    given(userRepository.findDistinctByExactDesignatedBodyCodes(
        DESIGNATED_BODY_CODE_1AIIDWT + "," + DESIGNATED_BODY_CODE_1DGBODY))
        .willReturn(Lists.newArrayList(aUser));

    // when
    List<HeeUser> actualUsers = service.getUsers(newSet(DESIGNATED_BODY_CODE_1DGBODY, DESIGNATED_BODY_CODE_1AIIDWT),
        null);

    // then
    assertArrayEquals(Lists.newArrayList(aUser).toArray(), actualUsers.toArray());

  }

  /**
   * User Alison has two designatedBodyCodes 1-DGBODY,1-AIIDWT and
   * user Jamesh has one designatedBodyCodes 1-AIIDWT
   * when we fetch by two designatedBodyCodes 1-AIIDWT it should match with other users exactly
   * same designatedBodyCodes
   * Expected: only one user Jamesh
   */
  @Test
  public void shouldFetchOneUsersExactDesignatedBodyCodes2() {
    // given
    Set<String> twoDesignatedBodyCodes = Sets.newHashSet(DESIGNATED_BODY_CODE_1DGBODY, DESIGNATED_BODY_CODE_1AIIDWT);
    Set<String> oneDesignatedBodyCodes = Sets.newHashSet(DESIGNATED_BODY_CODE_1DGBODY);
    HeeUser aUser = new HeeUser();
    aUser.setName(USER_NAME_ALISON);
    aUser.setDesignatedBodyCodes(twoDesignatedBodyCodes);

    HeeUser bUser = new HeeUser();
    bUser.setName(USER_NAME_JAMESH);
    bUser.setDesignatedBodyCodes(oneDesignatedBodyCodes);

    List<HeeUser> users = Lists.newArrayList(aUser, bUser);
    given(userRepository.findDistinctByExactDesignatedBodyCodes(
        DESIGNATED_BODY_CODE_1AIIDWT))
        .willReturn(Lists.newArrayList(bUser));

    // when
    List<HeeUser> actualUsers = service.getUsers(newSet(DESIGNATED_BODY_CODE_1AIIDWT),
        null);

    // then
    assertArrayEquals(Lists.newArrayList(bUser).toArray(), actualUsers.toArray());

  }

  @Test
  public void shouldGetUserByToken() throws Exception {
    // given
    HeeUser aUser = new HeeUser();
    aUser.setName(USER_NAME_JAMESH);

    given(userRepository.findByActive(USER_NAME_JAMESH)).willReturn(aUser);

    // when
    HeeUser user = service.getUserByToken(TOKEN);

    // then
    assertThat(user).isSameAs(aUser);
  }

  @Test
  public void createUserByTokenShouldCreateNewUserWithRolesAndDbcs() {
    HashSet<String> roleNames = Sets.newHashSet("RVAdmin",
        "uma_authorization");
    List<Role> foundRoles = Lists.newArrayList(rvAdminRoleMock);
    HeeUser createdUser = new HeeUser();
    Map<String, List<String>> userAttributes = Maps.newHashMap();
    userAttributes.put(DBC_ATTRIBUTE, Lists.newArrayList("DBC1,DBC2"));
    userAttributes.put(GMC_ID_ATTRIBUTE, Lists.newArrayList("GMC123"));

    when(roleRepositoryMock.findByNameIn(roleNames)).thenReturn(foundRoles);
    when(userRepository.save(any(HeeUser.class))).thenReturn(createdUser);
    when(keycloakAdminClientServiceMock.getUserAttributes(any())).thenReturn(userAttributes);

    HeeUser result = service.createUserByToken(TOKEN);
    Assert.assertEquals(createdUser, result);


    ArgumentCaptor<HeeUser> heeUserArgumentCaptor = ArgumentCaptor.forClass(HeeUser.class);
    verify(userRepository).save(heeUserArgumentCaptor.capture());

    HeeUser capturedHeeUser = heeUserArgumentCaptor.getValue();

    Assert.assertEquals("jamesh", capturedHeeUser.getName());
    Assert.assertEquals("James", capturedHeeUser.getFirstName());
    Assert.assertEquals("Hudson", capturedHeeUser.getLastName());
    Assert.assertEquals("GMC123", capturedHeeUser.getGmcId());
    Assert.assertEquals(StringUtils.EMPTY, capturedHeeUser.getEmailAddress());
    Assert.assertTrue(capturedHeeUser.getDesignatedBodyCodes().contains("DBC1"));
    Assert.assertTrue(capturedHeeUser.getDesignatedBodyCodes().contains("DBC2"));
    Assert.assertTrue(capturedHeeUser.isActive());
  }

  @Test
  public void shouldUpdateUserRolesByToken() throws Exception {
    HeeUser user = new HeeUser();
    List<Role> foundUserRoles = Lists.newArrayList(rvAdminRoleMock);
    user.setRoles(Sets.newHashSet(foundUserRoles));

    HeeUser updatedUser = new HeeUser();
    HashSet<String> roleNames = Sets.newHashSet("RVAdmin",
        "ProfileAdmin");
    List<Role> foundRoles = Lists.newArrayList(rvAdminRoleMock,profileAdminRoleMock);
    updatedUser.setRoles(Sets.newHashSet(foundRoles));

    Map<String, List<String>> userAttributes = Maps.newHashMap();
    userAttributes.put(DBC_ATTRIBUTE, Lists.newArrayList("DBC1,DBC2"));
    userAttributes.put(GMC_ID_ATTRIBUTE, Lists.newArrayList("GMC123"));

    when(roleRepositoryMock.findByNameIn(roleNames)).thenReturn(foundRoles);
    when(userRepository.findByActive(USER_NAME_JAMESH)).thenReturn(user);
    when(userRepository.save(any(HeeUser.class))).thenReturn(updatedUser);
    when(keycloakAdminClientServiceMock.getUserAttributes(any())).thenReturn(userAttributes);

    HeeUser originalUser = service.getUserByToken(UPDATED_TOKEN);
    Assert.assertEquals(user, originalUser);

    HeeUser result = service.updateUserRolesByToken(UPDATED_TOKEN);
    Assert.assertEquals(updatedUser, result);
  }

  @Test
  public void shouldAddUserRolesByToken() throws Exception {
    //given
    HeeUser user = new HeeUser();
    user.setName(USER_NAME_JAMESH);
    List<Role> foundUserRoles = Lists.newArrayList(rvAdminRoleMock);
    user.setRoles(Sets.newHashSet(foundUserRoles));

    HeeUser updatedUser = new HeeUser();
    updatedUser.setName(USER_NAME_JAMESH);
    HashSet<String> roleNames = Sets.newHashSet("RVAdmin",
        "ProfileAdmin");
    List<Role> foundRoles = Lists.newArrayList(rvAdminRoleMock,profileAdminRoleMock);
    updatedUser.setRoles(Sets.newHashSet(foundRoles));

    Map<String, List<String>> userAttributes = Maps.newHashMap();
    userAttributes.put(DBC_ATTRIBUTE, Lists.newArrayList("DBC1,DBC2"));
    userAttributes.put(GMC_ID_ATTRIBUTE, Lists.newArrayList("GMC123"));

    // when
    when(roleRepositoryMock.findByNameIn(roleNames)).thenReturn(foundRoles);
    when(userRepository.findByActive(USER_NAME_JAMESH)).thenReturn(user);
    when(userRepository.save(any(HeeUser.class))).thenReturn(updatedUser);
    when(keycloakAdminClientServiceMock.getUserAttributes(any())).thenReturn(userAttributes);

    // then
    HeeUser originalUser = service.getUserByToken(UPDATED_TOKEN);
    Assert.assertEquals(user, originalUser);

    HeeUser result = service.updateUserRolesByToken(UPDATED_TOKEN);
    Assert.assertEquals(updatedUser, result);
  }

  @Test
  public void shouldRemoveUserRolesByToken() {
    // given
    HeeUser user = new HeeUser();
    user.setName(USER_NAME_JAMESH);
    List<Role> foundUserRoles = Lists.newArrayList(rvAdminRoleMock, profileAdminRoleMock);
    user.setRoles(Sets.newHashSet(foundUserRoles));

    HeeUser updatedUser = new HeeUser();
    updatedUser.setName(USER_NAME_JAMESH);
    HashSet<String> roleNames = Sets.newHashSet("RVAdmin",
        "uma_authorization");
    List<Role> foundRoles = Lists.newArrayList(rvAdminRoleMock);
    updatedUser.setRoles(Sets.newHashSet(foundRoles));

    Map<String, List<String>> userAttributes = Maps.newHashMap();
    userAttributes.put(DBC_ATTRIBUTE, Lists.newArrayList("DBC1,DBC2"));
    userAttributes.put(GMC_ID_ATTRIBUTE, Lists.newArrayList("GMC123"));

    // when
    when(roleRepositoryMock.findByNameIn(roleNames)).thenReturn(foundRoles);
    when(userRepository.findByActive(USER_NAME_JAMESH)).thenReturn(user);
    when(userRepository.save(any(HeeUser.class))).thenReturn(user);
    when(keycloakAdminClientServiceMock.getUserAttributes(any())).thenReturn(userAttributes);

    // then
    HeeUser originalUser = service.getUserByToken(UPDATED_TOKEN);
    Assert.assertEquals(user, originalUser);

    HeeUser result = service.updateUserRolesByToken(TOKEN);
    Assert.assertEquals(updatedUser, result);
  }

  @Test(expected = EntityNotFoundException.class)
  public void shouldThrowExceptionWhenUserNameNotFound() {
    // given
    given(userRepository.findByActive(USER_NAME_JAMESH)).willReturn(null);

    // when
    service.getUserByToken(TOKEN);
  }
}

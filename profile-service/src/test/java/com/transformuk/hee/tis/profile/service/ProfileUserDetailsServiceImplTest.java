package com.transformuk.hee.tis.profile.service;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.transformuk.hee.tis.profile.assembler.UserProfileAssembler;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.security.model.UserProfile;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProfileUserDetailsServiceImplTest {

  public static final String TOKEN =
      "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJrcEk5UC1hQ3JaTXJ4cG5aeWNnNnlISk9VZ3g0a2hUYS04TlJyMkRhY0g0In0.eyJqdGkiOiI3ZjJiNzA4MC1lYjYxLTQ1YTgtYmUwNS0xYWFjODNkMTY3ZjciLCJleHAiOjE0Nzc1ODA5ODQsIm5iZiI6MCwiaWF0IjoxNDc3NTgwNjg0LCJpc3MiOiJodHRwczovL2Rldi1hcGkudHJhbnNmb3JtY2xvdWQubmV0L2F1dGgvcmVhbG1zL2xpbiIsImF1ZCI6ImFwaS1nYXRld2F5Iiwic3ViIjoiNGY5YWRhY2MtZjEyNC00M2FmLTkyZDMtYjVlZDc3NjhlYTU0IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYXBpLWdhdGV3YXkiLCJub25jZSI6IlA2NnVVT2JJTVBtY19Wb1RudmlYdk1KWE0zYks0RUo3WHJUeHpRbTN0ZUkiLCJhdXRoX3RpbWUiOjE0Nzc1ODA2ODQsInNlc3Npb25fc3RhdGUiOiIyNzg1NDE2Ny1hNWY0LTRkNTItOGQ3OC02OTY3M2ZmZTMwODgiLCJhY3IiOiIxIiwiY2xpZW50X3Nlc3Npb24iOiIyNjNmZDg1Ni02YmZjLTQ4ZWQtODZlNC1jMzFkOTdmYmNlZGMiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly9kZXYtYXBpLnRyYW5zZm9ybWNsb3VkLm5ldCIsImh0dHBzOi8vYXBwcy5saW4ubmhzLnVrIiwiaHR0cDovL2xvY2FsaG9zdDo4MDg3IiwiaHR0cHM6Ly9zdGFnZS1hcHBzLmxpbi5uaHMudWsiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIlJWQWRtaW4iLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJ2aWV3LXByb2ZpbGUiXX19LCJuYW1lIjoiSmFtZXMgSHVkc29uIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiamFtZXNoIiwiZ2l2ZW5fbmFtZSI6IkphbWVzIiwiZmFtaWx5X25hbWUiOiJIdWRzb24ifQ.VJ_8MDyM-1_MMlmhl4N-ZXHyq0G8AlaSLBR4eqlrXLD5CC29dW807WARalNGDqwlNSUuvK6tDiGRt5XKYWo6HDNBL-7Sp3QT2FXew6dD8zJwN8iR34aJGDGg94Kd0PkFESybqQFb4-sntCfKHQ3aRZkpD2WkyNZXEQEuDURYuqyJulqmKXqZxfnYWkd8JgSN1oTyUc4sFPWHjzI9A_y_0Tb13hAvFlPFWwKhCSSZqjRtC65JADOYMeIbyPCsSCKq0DqY2DCZpBivp5Wp0sZu0SSkww_rkwV5tql4gXV5kYmHWJa1rx_OmTAv6UKWYG4aFaqGmvcNhXkZGrweSCEmUw";


  @Mock
  private LoginService loginService;
  @Mock
  private UserProfileAssembler userProfileAssembler;

  @InjectMocks
  private ProfileUserDetailsServiceImpl service;

  @Test
  public void shouldGetUserProfile() {
    // given
    HeeUser aUser = new HeeUser();
    UserProfile aProfile = new UserProfile();
    given(loginService.getUserByToken(TOKEN)).willReturn(aUser);
    given(userProfileAssembler.toUserProfile(aUser)).willReturn(aProfile);
    // when
    Optional<UserProfile> profile = service.getProfile(TOKEN);
    // then
    Assert.assertTrue(profile.isPresent());
    assertThat(profile.get()).isSameAs(aProfile);
  }
}

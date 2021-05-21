package com.transformuk.hee.tis.profile.client.command;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.security.model.UserProfile;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class GetUserProfileCommandTest {

  private GetUserProfileCommand command;

  private RestTemplate restTemplate;

  @Before
  public void setUp() {
    restTemplate = mock(RestTemplate.class);

    String header = Base64.getEncoder().encodeToString("{\"alg\":\"none\"}".getBytes());
    String content = Base64.getEncoder().encodeToString("{\"iss\":\"testIssuer\"}".getBytes());
    String token = String.format("%s.%s.", header, content);
    command = new GetUserProfileCommand(restTemplate, "endpoint", token);
  }

  @Test
  public void shouldAddTokenIssuerToRequestHeadersWhenIssuerExists() {
    ArgumentCaptor<HttpEntity> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
    when(restTemplate.exchange(eq("endpoint"), eq(HttpMethod.GET), entityCaptor.capture(),
        eq(UserProfile.class))).thenReturn(ResponseEntity.ok(new UserProfile()));

    command.run();

    HttpEntity entity = entityCaptor.getValue();
    String tokenIssuer = entity.getHeaders().get("Token-Issuer").get(0);
    assertThat("Unexpected token issuer.", tokenIssuer, is("testIssuer"));
  }

  @Test
  public void shouldNotAddTokenIssuerToRequestHeadersWhenIssuerNotExists() {
    String header = Base64.getEncoder().encodeToString("{\"alg\":\"none\"}".getBytes());
    String content = Base64.getEncoder().encodeToString("{}".getBytes());
    String token = String.format("%s.%s.", header, content);
    command = new GetUserProfileCommand(restTemplate, "endpoint", token);

    ArgumentCaptor<HttpEntity> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
    when(restTemplate.exchange(eq("endpoint"), eq(HttpMethod.GET), entityCaptor.capture(),
        eq(UserProfile.class))).thenReturn(ResponseEntity.ok(new UserProfile()));

    command.run();

    HttpEntity entity = entityCaptor.getValue();
    List<String> tokenIssuers = entity.getHeaders().get("Token-Issuer");
    assertThat("Unexpected token issuer.", tokenIssuers, nullValue());
  }

  @Test
  public void shouldReturnNoProfileWhenException() {
    when(restTemplate.exchange(eq("endpoint"), eq(HttpMethod.GET), any(HttpEntity.class),
        eq(UserProfile.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    Optional<UserProfile> result = command.run();

    assertThat("Unexpected profile presence.", result.isPresent(), is(false));
  }

  @Test
  public void shouldReturnProfileWhenNoException() {
    UserProfile userProfile = new UserProfile();
    userProfile.setFirstName("Test");
    userProfile.setLastName("User");

    when(restTemplate.exchange(eq("endpoint"), eq(HttpMethod.GET), any(HttpEntity.class),
        eq(UserProfile.class))).thenReturn(ResponseEntity.ok(userProfile));

    Optional<UserProfile> result = command.run();

    assertThat("Unexpected profile presence.", result.isPresent(), is(true));
    assertThat("Unexpected profile.", result.orElse(null), is(userProfile));
  }
}

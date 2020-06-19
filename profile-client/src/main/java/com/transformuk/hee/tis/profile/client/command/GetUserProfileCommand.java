package com.transformuk.hee.tis.profile.client.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.transformuk.hee.tis.security.model.UserProfile;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Hystrix command that makes a request to the profile service.
 * <p>
 * For more information on hystrix see: https://github.com/Netflix/Hystrix
 */
public class GetUserProfileCommand extends HystrixCommand<Optional<UserProfile>> {

  private static final Logger LOG = LoggerFactory.getLogger(GetUserProfileCommand.class);
  private static final String GROUP_KEY = "PROFILE";
  private static final String COMMAND_KEY = "GET_USER_PROFILE";
  private static final String OIDC_TOKEN_HEADER = "OIDC_access_token";
  private static final String AUTH_TOKEN_HEADER = "Authorization";
  private static final String AUTH_TOKEN_BEARER = "Bearer ";


  private RestTemplate restTemplate;
  private String urlEndpoint;
  private String securityToken;

  public GetUserProfileCommand(RestTemplate restTemplate, String urlEndpoint,
      String securityToken) {
    super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(GROUP_KEY))
        .andCommandKey(HystrixCommandKey.Factory.asKey(COMMAND_KEY)));
    this.restTemplate = restTemplate;
    this.urlEndpoint = urlEndpoint;
    this.securityToken = securityToken;
  }

  /**
   * Make the get user info rest call to the profile service
   * <p>
   * Do NOT catch all errors as we need the HTTP 5xx errors to contribute to the error count for the
   * circuit breaker client errors should not contribute to the error count
   *
   * @return Optional user profile if its there
   * @throws Exception HTTP 5xx or other exceptions that can occur during th rest call
   */
  @Override
  protected Optional<UserProfile> run() throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.set(OIDC_TOKEN_HEADER, securityToken);
    headers.set(AUTH_TOKEN_HEADER, AUTH_TOKEN_BEARER + securityToken);
    HttpEntity<?> entity = new HttpEntity<String>(headers);
    try {
      ResponseEntity<UserProfile> responseEntity = restTemplate
          .exchange(urlEndpoint, HttpMethod.GET, entity,
              UserProfile.class);
      return Optional.of(responseEntity.getBody());
    } catch (HttpClientErrorException e) {
      LOG.debug("A client error occurred during a rest call to [{}]", urlEndpoint);
      LOG.debug("HTTP Status and body [{},{}]", e.getStatusCode(), e.getResponseBodyAsString());
    }
    return Optional.empty();
  }
}

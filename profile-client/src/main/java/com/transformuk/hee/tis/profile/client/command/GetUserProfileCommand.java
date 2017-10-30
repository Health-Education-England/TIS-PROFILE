package com.transformuk.hee.tis.profile.client.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.transformuk.hee.tis.security.model.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

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

  public GetUserProfileCommand(RestTemplate restTemplate, String urlEndpoint, String securityToken) {
    super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(GROUP_KEY))
        .andCommandKey(HystrixCommandKey.Factory.asKey(COMMAND_KEY)));
    this.restTemplate = restTemplate;
    this.urlEndpoint = urlEndpoint;
    this.securityToken = securityToken;
  }

  @Override
  protected Optional<UserProfile> run() throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.set(OIDC_TOKEN_HEADER, securityToken);
    headers.set(AUTH_TOKEN_HEADER, AUTH_TOKEN_BEARER + securityToken);
    HttpEntity<?> entity = new HttpEntity<String>(headers);
    try {
      ResponseEntity<UserProfile> responseEntity = restTemplate.exchange(urlEndpoint, HttpMethod.GET, entity,
          UserProfile.class);
      return Optional.of(responseEntity.getBody());
    } catch (HttpStatusCodeException e) {
      LOG.debug("An error occurred during a rest call to [{}]", urlEndpoint);
      LOG.debug("HTTP Status and body [{},{}]", e.getStatusCode(), e.getResponseBodyAsString());
    }
    return Optional.empty();
  }

  /**
   * If an exception occurs that wasn't handled in the try catch block use the following fallback
   * @return
   */
  @Override
  protected Optional<UserProfile> getFallback() {
    return Optional.empty();
  }
}

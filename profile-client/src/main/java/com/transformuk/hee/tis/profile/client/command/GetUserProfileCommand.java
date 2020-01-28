package com.transformuk.hee.tis.profile.client.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.transformuk.hee.tis.security.model.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

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
  private String host;
  private String urlEndpoint;
  private String securityToken;
  private boolean useServiceDiscovery;
  private DiscoveryClient discoveryClient;

  public GetUserProfileCommand(RestTemplate restTemplate, String host, String urlEndpoint,
                               String securityToken, boolean useServiceDiscovery, DiscoveryClient discoveryClient) {
    super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(GROUP_KEY))
        .andCommandKey(HystrixCommandKey.Factory.asKey(COMMAND_KEY)));
    this.restTemplate = restTemplate;
    this.host = host;
    this.urlEndpoint = urlEndpoint;
    this.securityToken = securityToken;
    this.useServiceDiscovery = useServiceDiscovery;
    this.discoveryClient = discoveryClient;
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
    return runInternal(generateUrl(0));
  }

  private Optional<UserProfile> runInternal(String url) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(OIDC_TOKEN_HEADER, securityToken);
    headers.set(AUTH_TOKEN_HEADER, AUTH_TOKEN_BEARER + securityToken);
    HttpEntity<?> entity = new HttpEntity<String>(headers);
    try {
      ResponseEntity<UserProfile> responseEntity = restTemplate
          .exchange(url, HttpMethod.GET, entity,
              UserProfile.class);
      return Optional.of(responseEntity.getBody());
    } catch (HttpClientErrorException e) {
      LOG.debug("A client error occurred during a rest call to [{}]", urlEndpoint);
      LOG.debug("HTTP Status and body [{},{}]", e.getStatusCode(), e.getResponseBodyAsString());
    }

    return Optional.empty();
  }

  private String generateUrl(int instanceIdx) {
    if (useServiceDiscovery) {
      List<ServiceInstance> instances = discoveryClient.getInstances("PROFILE");
      ServiceInstance serviceInstance = instances.get(instanceIdx);
      String host = serviceInstance.getHost();
      int port = serviceInstance.getPort();
      return "http://" + host + ":" + port + "/profile" + urlEndpoint;
    } else {
      return host + urlEndpoint;
    }
  }

  /**
   * If an exception occurs that wasn't handled in the try catch block use the following fallback
   *
   * @return An empty optional denoting no user profile available
   */
  @Override
  protected Optional<UserProfile> getFallback() {
    LOG.warn("In fallback method of get user profile");
    if (useServiceDiscovery) {
      LOG.info("Making another Profile call as previous one failed");
      runInternal(generateUrl(1));
    }
    LOG.warn("Not using service discovery so falling back to previous behaviour");
    return Optional.empty();
  }
}

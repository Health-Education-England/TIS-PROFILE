package com.transformuk.hee.tis.profile.client.service.impl;

import static java.util.Objects.requireNonNull;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.transformuk.hee.tis.profile.client.command.GetUserProfileCommand;
import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.security.service.JwtProfileService;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

/**
 * Service that's used within the context of a microservices spring security.
 * <p>
 * Its used to retrieve the the users details from the profile service.
 */
public class JwtProfileServiceImpl implements JwtProfileService {

  private static final Logger LOG = LoggerFactory.getLogger(JwtProfileServiceImpl.class);
  private static final String USER_INFO_ENDPOINT = "/api/userinfo";

  @Value("${profile.service.url}")
  private String serviceUrl;

  @Value("${profile.service.jwt.cache.size}")
  private long maxCacheSize;

  @Value("${profile.service.jwt.cache.ttl}")
  private int ttlDuration;

  private RestTemplate restTemplate;
  private Cache<String, Optional<UserProfile>> userProfileCache;

  public JwtProfileServiceImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    userProfileCache = CacheBuilder.newBuilder()
        .maximumSize(maxCacheSize)
        .expireAfterWrite(ttlDuration, TimeUnit.SECONDS)
        .removalListener((value) -> LOG.debug("{} was just removed from the cache", value.getKey()))
        .build();
  }

  /**
   * Get a UserProfile using the provided security token. This method should only be used during
   * authenticating the user in Spring Security
   * <p>
   * This method uses both caching and hystrix to reduce the amount of calls to the service, so if
   * the services is down or slow, we can fallback gracefully
   *
   * @return UserProfile containing information of the current authenticated user
   */
  public Optional<UserProfile> getProfile(String securityToken) {
    requireNonNull(securityToken, "securityToken must not be null");

    Optional<UserProfile> optionalUserProfile = userProfileCache.getIfPresent(securityToken);
    if (optionalUserProfile != null) {
      return optionalUserProfile;
    } else {
      optionalUserProfile = getUserProfile(securityToken);
      userProfileCache.put(securityToken, optionalUserProfile);
      return optionalUserProfile;
    }
  }

  protected Optional<UserProfile> getUserProfile(String securityToken) {
    try {
      return new GetUserProfileCommand(restTemplate, serviceUrl + USER_INFO_ENDPOINT, securityToken)
          .execute();
    } catch (Exception e) {
      LOG.error("Exception thrown from profile command {}", e.getMessage());
      throw e;
    }
  }

  public void setUserProfileCache(Cache<String, Optional<UserProfile>> userProfileCache) {
    this.userProfileCache = userProfileCache;
  }
}

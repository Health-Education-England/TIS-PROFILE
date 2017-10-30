package com.transformuk.hee.tis.profile.client.service.impl;

import com.google.common.cache.Cache;
import com.transformuk.hee.tis.security.model.UserProfile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JwtProfileServiceImplTest {

  @Spy
  @InjectMocks
  private JwtProfileServiceImpl testObj;

  @Mock
  private RestTemplate restTemplateMock;
  @Mock
  private Cache<String, Optional<UserProfile>> userProfileCacheMock;

  private String securityToken;

  @Before
  public void setup() {
    securityToken = UUID.randomUUID().toString();
    testObj.setUserProfileCache(userProfileCacheMock);
  }


  @Test(expected = NullPointerException.class)
  public void getProfileShouldThrowNPEWhenProvidedInvalidParam() {
    try {
      testObj.getProfile(null);
    } catch (Exception e) {
      Mockito.verifyZeroInteractions(userProfileCacheMock);
      throw e;
    }
  }

  @Test
  public void getProfileShouldMakeCommandCallWhenNoCachedDataAvailable() {
    when(userProfileCacheMock.getIfPresent(securityToken)).thenReturn(null);
    Optional<UserProfile> optionalUserProfile = Optional.empty();
    doReturn(optionalUserProfile).when(testObj).getUserProfile(securityToken);

    Optional<UserProfile> result = testObj.getProfile(securityToken);

    Assert.assertSame(optionalUserProfile, result);
  }

  @Test
  public void getProfileShouldUserProfileFromCache() {

    UserProfile userProfile = new UserProfile();
    Optional<UserProfile> optionalUserProfile = Optional.of(userProfile);
    when(userProfileCacheMock.getIfPresent(securityToken)).thenReturn(optionalUserProfile);

    Optional<UserProfile> result = testObj.getProfile(securityToken);

    Mockito.verify(userProfileCacheMock, Mockito.never()).put(any(), any());
    Assert.assertSame(optionalUserProfile, result);
  }

}
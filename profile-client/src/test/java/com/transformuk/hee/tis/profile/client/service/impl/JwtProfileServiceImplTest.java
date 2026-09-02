package com.transformuk.hee.tis.profile.client.service.impl;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.google.common.cache.Cache;
import com.transformuk.hee.tis.security.model.UserProfile;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

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

  @Test
  public void shouldNotLogSecurityTokenWhenCacheEntryIsRemoved() throws Exception {

    //Given
    String securityToken = "VERY_SENSITIVE_USER_TOKEN";
    String tokenSuffix = securityToken.substring(securityToken.length() - 10);

    Logger logger = (Logger) LoggerFactory.getLogger(JwtProfileServiceImpl.class);

    ListAppender<ILoggingEvent> appender = new ListAppender<>();
    appender.start();
    logger.addAppender(appender);

    JwtProfileServiceImpl service = new JwtProfileServiceImpl(restTemplateMock);

    Field cacheField = JwtProfileServiceImpl.class.getDeclaredField("userProfileCache");

    cacheField.setAccessible(true);

    Cache<String, Optional<UserProfile>> cache = (Cache<String, Optional<UserProfile>>) cacheField.get(
        service);

    cache.put(securityToken, Optional.of(new UserProfile()));

    //When
    cache.invalidate(securityToken);
    cache.cleanUp();

    //Then
    boolean fullTokenLogged = appender.list.stream()
        .anyMatch(event -> event.getFormattedMessage().contains(securityToken));

    boolean tokenSuffixLogged = appender.list.stream()
        .anyMatch(event -> event.getFormattedMessage().contains(tokenSuffix));

    assertFalse(fullTokenLogged);
    Assert.assertTrue(tokenSuffixLogged);

    logger.detachAppender(appender);
  }
}

package com.transformuk.hee.tis.profile.client.service.impl;

import static ch.qos.logback.classic.Level.DEBUG;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.slf4j.LoggerFactory.getLogger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.google.common.cache.Cache;
import com.transformuk.hee.tis.security.model.UserProfile;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JwtProfileServiceImplTest {

  @Spy
  @InjectMocks
  private JwtProfileServiceImpl testObj;

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
      verifyZeroInteractions(userProfileCacheMock);
      throw e;
    }
  }

  @Test
  public void getProfileShouldMakeCommandCallWhenNoCachedDataAvailable() {
    when(userProfileCacheMock.getIfPresent(securityToken)).thenReturn(null);
    Optional<UserProfile> optionalUserProfile = empty();
    doReturn(optionalUserProfile).when(testObj).getUserProfile(securityToken);

    Optional<UserProfile> result = testObj.getProfile(securityToken);

    assertSame(optionalUserProfile, result);
  }

  @Test
  public void getProfileShouldUserProfileFromCache() {

    UserProfile userProfile = new UserProfile();
    Optional<UserProfile> optionalUserProfile = of(userProfile);
    when(userProfileCacheMock.getIfPresent(securityToken)).thenReturn(optionalUserProfile);

    Optional<UserProfile> result = testObj.getProfile(securityToken);

    verify(userProfileCacheMock, never()).put(any(), any());
    assertSame(optionalUserProfile, result);
  }

  @Test
  public void initializeCacheShouldUseInjectedProperties() throws Exception {

    initializeInjectedFields();

    testObj.initUserProfileCache();

    Field cacheField = JwtProfileServiceImpl.class.getDeclaredField("userProfileCache");
    cacheField.setAccessible(true);
    Cache<String, Optional<UserProfile>> cache = (Cache<String, Optional<UserProfile>>) cacheField.get(
        testObj);
    cache.put("first", empty());
    cache.put("second", empty());

    assertEquals(1, cache.size());
  }

  @Test
  public void shouldNotLogSecurityTokenEntirelyWhenCacheEntryIsRemoved() throws Exception {

    //Given
    String tokenSuffix = securityToken.substring(securityToken.length() - 10);

    Logger logger = (Logger) getLogger(JwtProfileServiceImpl.class);
    Level originalLevel = logger.getLevel();

    ListAppender<ILoggingEvent> appender = new ListAppender<>();
    appender.start();

    try {
      logger.setLevel(DEBUG);
      logger.addAppender(appender);

      initializeInjectedFields();

      testObj.initUserProfileCache();

      Field cacheField = JwtProfileServiceImpl.class.getDeclaredField("userProfileCache");
      cacheField.setAccessible(true);
      Cache<String, Optional<UserProfile>> cache = (Cache<String, Optional<UserProfile>>) cacheField.get(
          testObj);

      cache.put(securityToken, of(new UserProfile()));

      //When
      cache.invalidate(securityToken);
      cache.cleanUp();

      //Then
      boolean fullTokenLogged = appender.list.stream()
          .anyMatch(event -> event.getFormattedMessage().contains(securityToken));

      boolean tokenSuffixLogged = appender.list.stream()
          .anyMatch(event -> event.getFormattedMessage().contains(tokenSuffix));

      assertFalse(fullTokenLogged);
      assertTrue(tokenSuffixLogged);
    } finally {
      logger.detachAppender(appender);
      appender.stop();
      logger.setLevel(originalLevel);
    }
  }

  void initializeInjectedFields() throws Exception {
    Field maxCacheSizeField = JwtProfileServiceImpl.class.getDeclaredField("maxCacheSize");
    maxCacheSizeField.setAccessible(true);
    maxCacheSizeField.setLong(testObj, 1);

    Field ttlDurationField = JwtProfileServiceImpl.class.getDeclaredField("ttlDuration");
    ttlDurationField.setAccessible(true);
    ttlDurationField.setInt(testObj, 60);
  }
}

package com.transformuk.hee.tis.profile.service;

import com.google.common.collect.Maps;
import com.transform.hee.tis.keycloak.KeycloakAdminClient;
import com.transform.hee.tis.keycloak.User;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.transformuk.hee.tis.profile.web.rest.TestUtil.createEntityHeeUser;
import static junit.framework.TestCase.assertSame;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KeycloakAdminClientServiceTest {

  private static final String REALM_LIN = "lin";

  @Mock
  private KeycloakAdminClient keycloakAdminClient;

  private EntityManager em;

  private HeeUser heeUser;

  @InjectMocks
  private KeycloakAdminClientService keyclockAdminClientService;

  @Before
  public void initTest() {
    heeUser = createEntityHeeUser(em);
  }

  @Test
  public void createUser() {
    //When
    keyclockAdminClientService.createUser(heeUser);

    //then
    ArgumentCaptor<String> relmArg = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<User> userArg = ArgumentCaptor.forClass(User.class);
    verify(keycloakAdminClient).createUser(relmArg.capture(), userArg.capture());

    assertThat(relmArg.getValue()).isEqualTo(REALM_LIN);
    assertThat(userArg.getValue().getFirstname()).isEqualTo(heeUser.getFirstName());
    assertThat(userArg.getValue().getEmail()).isEqualTo(heeUser.getEmailAddress());
    assertThat(userArg.getValue().getSurname()).isEqualTo(heeUser.getLastName());
    assertThat(userArg.getValue().getUsername()).isEqualTo(heeUser.getName());

  }

  @Test
  public void updateUser() {
    //Given
    String existingUserId = "111";
    User mockUser = mock(User.class);
    given(mockUser.getId()).willReturn(existingUserId);
    given(keycloakAdminClient.findByUsername(REALM_LIN, heeUser.getName())).willReturn(mockUser);

    //When
    keyclockAdminClientService.updateUser(heeUser);

    //then
    ArgumentCaptor<String> relmArg = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<User> userArg = ArgumentCaptor.forClass(User.class);
    verify(keycloakAdminClient).updateUser(relmArg.capture(), any(), userArg.capture());

    assertThat(relmArg.getValue()).isEqualTo(REALM_LIN);
    assertThat(userArg.getValue().getFirstname()).isEqualTo(heeUser.getFirstName());
    assertThat(userArg.getValue().getEmail()).isEqualTo(heeUser.getEmailAddress());
    assertThat(userArg.getValue().getSurname()).isEqualTo(heeUser.getLastName());
    assertThat(userArg.getValue().getUsername()).isEqualTo(heeUser.getName());

  }

  @Test
  public void getUserAttributesShouldReturnTheAttributesOnAUser() {
    Map<String, List<String>> attributes = Maps.newHashMap();
    when(keycloakAdminClient.getAttributesForUser(anyString(), eq(heeUser.getName()))).thenReturn(attributes);

    Map<String, List<String>> result = keyclockAdminClientService.getUserAttributes(heeUser);

    verify(keycloakAdminClient).getAttributesForUser("lin", "AAAAAAAAAA");

    assertSame(attributes, result);
  }

}

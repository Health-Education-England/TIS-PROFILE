package com.transformuk.hee.tis.profile.service;

import com.transform.hee.tis.keycloak.KeyclockAdminClient;
import com.transform.hee.tis.keycloak.User;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;

import static com.transformuk.hee.tis.profile.web.rest.TestUtil.createEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class KeyclockAdminClientServiceTest {

	private static final String REALM_LIN = "lin";

	@Mock
	private KeyclockAdminClient keyclockAdminClient;

	private EntityManager em;

	private HeeUser heeUser;

	@InjectMocks
	private KeyclockAdminClientService keyclockAdminClientService;

	@Before
	public void initTest() {
		heeUser = createEntity(em);
	}

	@Test
	public void createUser() {
		//When
		keyclockAdminClientService.createUser(heeUser);

		//then
		ArgumentCaptor<String> relmArg = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<User> userArg = ArgumentCaptor.forClass(User.class);
		verify(keyclockAdminClient).createUser(relmArg.capture(), userArg.capture());

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
		given(keyclockAdminClient.findByUsername(REALM_LIN, heeUser.getName())).willReturn(mockUser);

		//When
		keyclockAdminClientService.updateUser(heeUser);

		//then
		ArgumentCaptor<String> relmArg = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<User> userArg = ArgumentCaptor.forClass(User.class);
		verify(keyclockAdminClient).updateUser(relmArg.capture(), any(), userArg.capture());

		assertThat(relmArg.getValue()).isEqualTo(REALM_LIN);
		assertThat(userArg.getValue().getFirstname()).isEqualTo(heeUser.getFirstName());
		assertThat(userArg.getValue().getEmail()).isEqualTo(heeUser.getEmailAddress());
		assertThat(userArg.getValue().getSurname()).isEqualTo(heeUser.getLastName());
		assertThat(userArg.getValue().getUsername()).isEqualTo(heeUser.getName());

	}

}

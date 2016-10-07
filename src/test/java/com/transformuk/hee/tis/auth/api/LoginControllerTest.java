package com.transformuk.hee.tis.auth.api;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.auth.model.LoginResponse;
import com.transformuk.hee.tis.auth.model.User;
import com.transformuk.hee.tis.auth.model.UserListResponse;
import com.transformuk.hee.tis.auth.model.UserProfile;
import com.transformuk.hee.tis.auth.service.LoginService;
import com.transformuk.hee.tis.auth.service.PermissionsService;
import org.flywaydb.core.Flyway;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.endsWith;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(LoginController.class)
public class LoginControllerTest {

	private static final String TIS_USER = "tisUser";
	private static final String TIS_PASSWORD = "tisPassword";
	private static final String TIS_TOKENID = "CNSAKFAjncdjfjkw";
	public static final String USER_NAME = "jamesH";

	@MockBean
	private PermissionsService permissionsService;

	@MockBean
	private LoginService loginService;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private RestTemplate restTemplate;

	@MockBean // mocking to override init method otherwise test try to connect DB.
	private Flyway flyway;

	@Captor
	private ArgumentCaptor<AuditEvent> captor;

	@Test
	public void shouldReturnUserProfileDetails() throws Exception {
		//Given
		String roles = "RVAdmin";

		UserProfile userProfile = new UserProfile();
		userProfile.setUsername(TIS_USER);
		userProfile.setCn(newArrayList(TIS_USER));
		userProfile.setIsMemberOf(newArrayList(roles));

		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setTokenId(TIS_TOKENID);
		given(restTemplate.postForObject(any(String.class), any(HttpEntity.class), eq(LoginResponse.class))).
				willReturn(loginResponse);

		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
				eq(UserProfile.class), eq(TIS_USER))).willReturn(new ResponseEntity(userProfile, HttpStatus.OK));

		//When
		this.mvc.perform(post("/identity/authenticate")
				.header("X-TIS-Username", TIS_USER)
				.header("X-TIS-Password", TIS_PASSWORD))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.tokenId").value(TIS_TOKENID))
				.andExpect(jsonPath("$.userName").value(TIS_USER))
				.andExpect(jsonPath("$.fullName").value(TIS_USER))
				.andExpect(jsonPath("$.roles").value(roles));

		//Then
		ArgumentCaptor<HttpEntity> requestEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
		verify(restTemplate).
				exchange(endsWith("/openam/json/users/{userName}"),
						eq(HttpMethod.GET), requestEntityCaptor.capture(), eq(UserProfile.class), eq(TIS_USER));


		HttpEntity entity = requestEntityCaptor.getValue();
		assertThat(entity.getHeaders().get("iplanetDirectoryPro")).contains(TIS_TOKENID);
	}

	@Test
	@Ignore
	public void shouldGETListOfUsers() throws Exception {
		//Given
		User user = new User(USER_NAME);
		user.setGmcId("123");
		UserListResponse response = new UserListResponse(1, newArrayList(user));
		given(loginService.getUsers(0, 1, "first_name=James")).willReturn(response);

		//When
		this.mvc.perform(get("/identity/users").param("offset","0").param("limit", "1").param("filters", "first_name=James"))
				.andExpect(status().isOk())
				.andExpect(content().string(
						"{\"total\":1,\"users\":[{\"user_name\":\"jamesH\",\"first_name\":null,\"last_name\":null," +
								"\"gmc_id\":\"123\",\"designated_body_code\":null,\"phone_number\":null}]," +
								"\"_links\":{\"self\":{\"href\":\"http://localhost/identity/users?" +
								"offset=0&limit=1&filters=first_name%3DJames\"}}}"));
	}

	@Test
	@Ignore
	public void shouldGETUserByUserName() throws Exception {
		//Given
		User user = new User(USER_NAME);
		user.setGmcId("123");
		given(loginService.getUserByUserName(USER_NAME)).willReturn(user);

		//When
		this.mvc.perform(get("/identity/user").header("Username", USER_NAME))
				.andExpect(status().isOk())
				.andExpect(content().string(
						"{\"user_name\":\"jamesH\",\"first_name\":null,\"last_name\":null,\"gmc_id\":\"123\"," +
								"\"designated_body_code\":null,\"phone_number\":null," +
								"\"_links\":{\"self\":{\"href\":\"http://localhost/identity/user\"}}}"));
	}

	@Test
	public void shouldReturnPermissionsAndRoles() throws Exception {
		//Given
		UserProfile userProfile = new UserProfile();
		userProfile.setUsername(TIS_USER);
		userProfile.setCn(newArrayList(TIS_USER));
		userProfile.setIsMemberOf(newArrayList(newArrayList("cn=Trainee,ou=groups,dc=openam,dc=forgerock,dc=org",
				"cn=RVAdmin,ou=groups,dc=openam,dc=forgerock,dc=org",
				"cn=RVOfficer,ou=groups,dc=openam,dc=forgerock,dc=org")));

		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setTokenId(TIS_TOKENID);
		given(restTemplate.postForObject(any(String.class), any(HttpEntity.class), eq(LoginResponse.class))).
				willReturn(loginResponse);
		given(permissionsService.getPermissions(any(Set.class))).willReturn(Sets.newHashSet("Perm1", "Perm2", "Perm3"));

		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
				eq(UserProfile.class), eq(TIS_USER))).willReturn(new ResponseEntity(userProfile, HttpStatus.OK));

		//When
		this.mvc.perform(post("/identity/authenticate")
				.header("X-TIS-Username", TIS_USER)
				.header("X-TIS-Password", TIS_PASSWORD))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.tokenId").value(TIS_TOKENID))
				.andExpect(jsonPath("$.userName").value(TIS_USER))
				.andExpect(jsonPath("$.fullName").value(TIS_USER))
				.andExpect(jsonPath("$.roles").value(hasItems("Trainee", "RVAdmin", "RVOfficer")))
				.andExpect(jsonPath("$.permissions").value(hasItems("Perm1", "Perm2", "Perm3")));

		//Then
		ArgumentCaptor<HttpEntity> requestEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
		verify(restTemplate).
				exchange(endsWith("/openam/json/users/{userName}"),
						eq(HttpMethod.GET), requestEntityCaptor.capture(), eq(UserProfile.class), eq(TIS_USER));

		HttpEntity entity = requestEntityCaptor.getValue();
		assertThat(entity.getHeaders().get("iplanetDirectoryPro")).contains(TIS_TOKENID);
	}

	@Test
	public void shouldFailAuthenticationWhenNoHeaders() throws Exception {
		this.mvc.perform(post("/identity/authenticate"))
				.andExpect(status().is5xxServerError()).andExpect(jsonPath("$.errorMessage")
				.value(containsString("Missing request header 'X-TIS-Username'")));
	}

	@Test
	public void shouldFailAuthenticationWhenMissingPasswordHeader() throws Exception {
		this.mvc.perform(post("/identity/authenticate")
				.header("X-TIS-Username", TIS_USER))
				.andExpect(status().is5xxServerError()).andExpect(jsonPath("$.errorMessage")
				.value(containsString("Missing request header 'X-TIS-Password'")));
	}

	@Test
	public void shouldLogout() throws Exception {
		//Given
		given(restTemplate.postForObject(any(String.class), any(HttpEntity.class), eq(String.class))).willReturn("");

		//When
		this.mvc.perform(post("/identity/logout")
				.header("X-TIS-TokenId", TIS_TOKENID))
				.andExpect(status().isOk());

		//Then
		ArgumentCaptor<HttpEntity> requestEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
		verify(restTemplate).postForObject(
				endsWith("/openam/json/sessions/?_action=logout"),
				requestEntityCaptor.capture(), eq(String.class));
		HttpEntity entity = requestEntityCaptor.getValue();
		assertThat(entity.getHeaders().get("iplanetDirectoryPro")).contains(TIS_TOKENID);
	}

	@Test
	public void shouldFailLogoutWhenNoTokenHeader() throws Exception {
		this.mvc.perform(post("/identity/logout"))
				.andExpect(status().is5xxServerError()).andExpect(jsonPath("$.errorMessage")
				.value(containsString("Missing request header 'X-TIS-TokenId'")));
	}

}

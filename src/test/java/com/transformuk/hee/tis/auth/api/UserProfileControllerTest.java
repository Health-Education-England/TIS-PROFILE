package com.transformuk.hee.tis.auth.api;

import com.transformuk.hee.tis.auth.model.User;
import com.transformuk.hee.tis.auth.model.UserListResponse;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserProfileController.class)
public class UserProfileControllerTest {

	private static final String TIS_USER = "tisUser";
	public static final String GMC_ID = "123";
	public static final String DESIGNATED_BODY_CODE = "1-DGBODY";

	public static final String FULL_NAME = "James Hudson";
	public static final String[] ROLES = {"view-profile", "manage-account", "judges", "admin", 
			"uma_authorization", "offline_access"};
	public static final String USER_NAME = "jamesh";
	public static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJHVFNIYkRwN0JSeVhORTQ2cWZtVFFvZ1lrOFF2MERldENNSEVjNkFFeDhzIn0.eyJqdGkiOiJlYzFkMjU1Mi1lNDJjLTRhMWUtYmIyOC0yMTlkNGM3MTY5M2IiLCJleHAiOjE0NzY5NzEzNjAsIm5iZiI6MCwiaWF0IjoxNDc2OTcxMDYwLCJpc3MiOiJodHRwczovL2Rldi1hcGkudHJhbnNmb3JtY2xvdWQubmV0L2F1dGgvcmVhbG1zL2xpbiIsImF1ZCI6InJldmFsaWRhdGlvbiIsInN1YiI6ImIxNzAyZDFjLWM4YmItNDg4Mi05OWY3LTJjNzc2MDY4MWIwNSIsInR5cCI6IkJlYXJlciIsImF6cCI6InJldmFsaWRhdGlvbiIsIm5vbmNlIjoiaUZDRVNpMk5EYnZNMnhjQlFKN2pyTExieFA1c3pIQ1phN2s1cndRd3dmWSIsImF1dGhfdGltZSI6MTQ3Njk3MTA2MCwic2Vzc2lvbl9zdGF0ZSI6IjU1YmE3MGI5LTJmMTUtNDA2OC04ZjRmLTRmZDIzNTY0ZTI0YyIsImFjciI6IjEiLCJjbGllbnRfc2Vzc2lvbiI6ImJlNjQ4ODY0LWE3Y2UtNDBlMS05MGQ4LTNkY2VkOWZlYTY5MSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL2Rldi1hcGkudHJhbnNmb3JtY2xvdWQubmV0Il0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJhZG1pbiIsInVtYV9hdXRob3JpemF0aW9uIiwianVkZ2VzIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsInZpZXctcHJvZmlsZSJdfX0sInJvbGVzIjoiW3ZpZXctcHJvZmlsZSwgbWFuYWdlLWFjY291bnQsIGp1ZGdlcywgYWRtaW4sIHVtYV9hdXRob3JpemF0aW9uLCBvZmZsaW5lX2FjY2Vzc10iLCJuYW1lIjoiSmFtZXMgSHVkc29uIiwiZ3JvdXBzIjpbIkVhc3Qgb2YgRW5nbGFuZCJdLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqYW1lc2giLCJnaXZlbl9uYW1lIjoiSmFtZXMiLCJmYW1pbHlfbmFtZSI6Ikh1ZHNvbiJ9.VxPtvyu8jlgsHumUEKEttM27jsOirn26KokGEp9MfoiOe-Z1L_IiEs-KYsdzW2J2Fwx7amgGidlvfD0uU_EuEaoU0Wrt1uuWRHMzaVztbc1ekl0vIqk7YYvz9I84ngKug8YITgTg3ZlKLOhBUrSVeT9Pz9mFTrJZhKfX7XARVsOc2HZJqgmMG5IYitZfD5uti0enuD9EfYNqnCv_6cEbc45lFNSAMjcyJWSkNN9VPEo-_NSZQrLVmOB3oNZ5vetsw5ijb6y9TQUcrDzUu6qu74_J3n2w9PrrRXVmYeYphetNZGE2LyBScJyMuYvzu6oAik2banzLc9jGiw22tGEuQQ";
	
	

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
		User user = new User(USER_NAME);
		user.setGmcId(GMC_ID);
		user.setDesignatedBodyCode(DESIGNATED_BODY_CODE);

		given(loginService.getUserByUserName(USER_NAME)).willReturn(user);

		// When & then
		this.mvc.perform(get("/identity/userinfo")
				.header("OIDC_access_token", TOKEN))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userName").value(USER_NAME))
				.andExpect(jsonPath("$.fullName").value(FULL_NAME))
				.andExpect(jsonPath("$.gmcId").value(GMC_ID))
				.andExpect(jsonPath("$.designatedBodyCode").value(DESIGNATED_BODY_CODE))
				.andExpect(jsonPath("$.roles").isNotEmpty())
				.andExpect(jsonPath("$.roles").value(hasItems(ROLES)));
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
		this.mvc.perform(get("/identity/users").param("offset", "0").param("limit", "1").param("filters", 
				"first_name=James"))
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
		User user = new User(USER_NAME);
		
		given(loginService.getUserByUserName(USER_NAME)).willReturn(user);
		given(permissionsService.getPermissions(any(Set.class))).willReturn(newHashSet("Perm1", "Perm2", "Perm3"));

		//When
		this.mvc.perform(get("/identity/userinfo")
				.header("OIDC_access_token", TOKEN))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userName").value(USER_NAME))
				.andExpect(jsonPath("$.roles").value(hasItems(ROLES)))
				.andExpect(jsonPath("$.permissions").value(hasItems("Perm1", "Perm2", "Perm3")));
	}

	@Test
	public void shouldFailAuthenticationWhenNoHeaders() throws Exception {
		this.mvc.perform(get("/identity/userinfo"))
				.andExpect(status().is5xxServerError()).andExpect(jsonPath("$.errorMessage")
				.value(containsString("Missing request header 'OIDC_access_token'")));
	}

}

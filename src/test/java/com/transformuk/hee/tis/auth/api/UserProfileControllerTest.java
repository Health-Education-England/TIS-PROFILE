package com.transformuk.hee.tis.auth.api;

import com.transformuk.hee.tis.auth.model.Permission;
import com.transformuk.hee.tis.auth.model.Role;
import com.transformuk.hee.tis.auth.model.User;
import com.transformuk.hee.tis.auth.model.UserListResponse;
import com.transformuk.hee.tis.auth.service.LoginService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserProfileController.class)
public class UserProfileControllerTest {

	private static final String TIS_USER = "tisUser";
	public static final String GMC_ID = "123";
	public static final String DESIGNATED_BODY_CODE = "1-DGBODY";
	public static final String PERMISSION = "Perm1";
	public static final String RV_ADMIN = "RVAdmin";

	public static final String[] ROLES = {RV_ADMIN};
	public static final String USER_NAME = "jamesh";
	public static final String TOKEN = 
			"eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJrcEk5UC1hQ3JaTXJ4cG5aeWNnNnlISk9VZ3g0a2hUYS04TlJyMkRhY0g0In0.eyJqdGkiOiI3ZjJiNzA4MC1lYjYxLTQ1YTgtYmUwNS0xYWFjODNkMTY3ZjciLCJleHAiOjE0Nzc1ODA5ODQsIm5iZiI6MCwiaWF0IjoxNDc3NTgwNjg0LCJpc3MiOiJodHRwczovL2Rldi1hcGkudHJhbnNmb3JtY2xvdWQubmV0L2F1dGgvcmVhbG1zL2xpbiIsImF1ZCI6ImFwaS1nYXRld2F5Iiwic3ViIjoiNGY5YWRhY2MtZjEyNC00M2FmLTkyZDMtYjVlZDc3NjhlYTU0IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYXBpLWdhdGV3YXkiLCJub25jZSI6IlA2NnVVT2JJTVBtY19Wb1RudmlYdk1KWE0zYks0RUo3WHJUeHpRbTN0ZUkiLCJhdXRoX3RpbWUiOjE0Nzc1ODA2ODQsInNlc3Npb25fc3RhdGUiOiIyNzg1NDE2Ny1hNWY0LTRkNTItOGQ3OC02OTY3M2ZmZTMwODgiLCJhY3IiOiIxIiwiY2xpZW50X3Nlc3Npb24iOiIyNjNmZDg1Ni02YmZjLTQ4ZWQtODZlNC1jMzFkOTdmYmNlZGMiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly9kZXYtYXBpLnRyYW5zZm9ybWNsb3VkLm5ldCIsImh0dHBzOi8vYXBwcy5saW4ubmhzLnVrIiwiaHR0cDovL2xvY2FsaG9zdDo4MDg3IiwiaHR0cHM6Ly9zdGFnZS1hcHBzLmxpbi5uaHMudWsiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIlJWQWRtaW4iLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJ2aWV3LXByb2ZpbGUiXX19LCJuYW1lIjoiSmFtZXMgSHVkc29uIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiamFtZXNoIiwiZ2l2ZW5fbmFtZSI6IkphbWVzIiwiZmFtaWx5X25hbWUiOiJIdWRzb24ifQ.VJ_8MDyM-1_MMlmhl4N-ZXHyq0G8AlaSLBR4eqlrXLD5CC29dW807WARalNGDqwlNSUuvK6tDiGRt5XKYWo6HDNBL-7Sp3QT2FXew6dD8zJwN8iR34aJGDGg94Kd0PkFESybqQFb4-sntCfKHQ3aRZkpD2WkyNZXEQEuDURYuqyJulqmKXqZxfnYWkd8JgSN1oTyUc4sFPWHjzI9A_y_0Tb13hAvFlPFWwKhCSSZqjRtC65JADOYMeIbyPCsSCKq0DqY2DCZpBivp5Wp0sZu0SSkww_rkwV5tql4gXV5kYmHWJa1rx_OmTAv6UKWYG4aFaqGmvcNhXkZGrweSCEmUw";

	@MockBean
	private LoginService loginService;

	@Autowired
	private MockMvc mvc;

	@Captor
	private ArgumentCaptor<AuditEvent> captor;

	@Test
	public void shouldReturnUserProfileDetails() throws Exception {
		//Given
		User user = getUser();

		given(loginService.getUserByUserName(USER_NAME)).willReturn(user);

		// When & then
		this.mvc.perform(get("/identity/userinfo")
				.header("OIDC_access_token", TOKEN))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userName").value(USER_NAME))
				.andExpect(jsonPath("$.gmcId").value(GMC_ID))
				.andExpect(jsonPath("$.designatedBodyCode").value(DESIGNATED_BODY_CODE))
				.andExpect(jsonPath("$.roles").isNotEmpty())
				.andExpect(jsonPath("$.roles").value(hasItems(ROLES)));
	}

	@Test
	public void shouldGETListOfUsers() throws Exception {
		//Given
		User user = new User(USER_NAME);
		user.setGmcId("123");
		user.setFirstName("James");
		UserListResponse response = new UserListResponse(1, newArrayList(user));
		given(loginService.getUsers(0, 1, "firstName=James")).willReturn(response);

		//When
		this.mvc.perform(get("/identity/users").param("offset", "0").param("limit", "1").param("filter",
				"firstName=James"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.total").value(1))
				.andExpect(jsonPath("$.users[0].name").value("jamesh"))
				.andExpect(jsonPath("$.users[0].gmcId").value("123"));
	}

	@Test
	public void shouldGETUserByUserName() throws Exception {
		//Given
		User user = new User(USER_NAME);
		user.setGmcId("123");
		given(loginService.getUserByUserName(USER_NAME)).willReturn(user);

		//When
		this.mvc.perform(get("/identity/user").header("Username", USER_NAME))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("jamesh"))
				.andExpect(jsonPath("$.gmcId").value("123"));
	}

	@Test
	public void shouldRetunrBadRequestForInvalidUserName() throws Exception {
		//Given
		given(loginService.getUserByUserName(USER_NAME)).willThrow(EntityNotFoundException.class);

		//When
		this.mvc.perform(get("/identity/user").header("Username", "InValidUser"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldReturnPermissionsAndRoles() throws Exception {
		//Given
		User user = getUser();
		
		given(loginService.getUserByUserName(USER_NAME)).willReturn(user);

		//When
		this.mvc.perform(get("/identity/userinfo")
				.header("OIDC_access_token", TOKEN))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userName").value(USER_NAME))
				.andExpect(jsonPath("$.roles").value(hasItems(ROLES)))
				.andExpect(jsonPath("$.permissions").value(hasItems("Perm1")));
	}

	@Test
	public void shouldFailAuthenticationWhenNoHeaders() throws Exception {
		this.mvc.perform(get("/identity/userinfo"))
				.andExpect(status().is5xxServerError()).andExpect(jsonPath("$.errorMessage")
				.value(containsString("Missing request header 'OIDC_access_token'")));
	}
	
	private User getUser() {
		User user = new User(USER_NAME);
		user.setGmcId(GMC_ID);
		user.setDesignatedBodyCode(DESIGNATED_BODY_CODE);
		Set<Permission> permissions = Sets.newSet(new Permission(PERMISSION));
		user.setRoles(Sets.newSet(new Role(RV_ADMIN, permissions)));
		return user;
	}

}

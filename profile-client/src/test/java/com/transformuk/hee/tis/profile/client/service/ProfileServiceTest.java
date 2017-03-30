package com.transformuk.hee.tis.profile.client.service;

import com.transformuk.hee.tis.profile.dto.PagedTraineeIdResponse;
import com.transformuk.hee.tis.profile.dto.TraineeId;
import com.transformuk.hee.tis.profile.dto.UserProfile;
import com.transformuk.hee.tis.security.client.KeycloakRestTemplate;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class
)
public class ProfileServiceTest {

	private static final String DBC = "1-DGBODY";
	private static final String PROFILE_URL = "http://localhost:8888/trainee-id";
	//	private static final String DBC = "1-85KJU0";
	private static final String PERMISSIONS = "revalidation:submit:on:behalf:of:ro";
	private static final String USER_ID = "James";

	@Captor
	private ArgumentCaptor<HttpEntity> entityArgumentCaptor;
	@Mock
	private KeycloakRestTemplate keycloakRestTemplate;
	@InjectMocks
	private ProfileService profileService;

	@Before
	public void setUp() throws Exception {
		profileService.setServiceUrl("http://localhost:8888/trainee-id");
	}


	@Test
	public void shouldGetAllTraineeIdMappings() {
		// given
		Pageable pageable = new PageRequest(0, 100);
		List<TraineeId> traineeIds = newArrayList(new TraineeId(999L, "1234567	"));
		PagedTraineeIdResponse response = new PagedTraineeIdResponse(traineeIds, 1);
		given(keycloakRestTemplate.getForObject(any(URI.class), eq(PagedTraineeIdResponse.class))).willReturn(response);

		// when
		Page<TraineeId> allTraineeIdMappings = profileService.getPagedTraineeIds(DBC, pageable);

		// then
		assertEquals(traineeIds, allTraineeIdMappings.getContent());
	}

	@Test
	public void shouldPassSecurityTokenAsHeader() {
		// given
		ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
		given(keycloakRestTemplate.getForEntity(eq(PROFILE_URL + "/api/users/ro-user/" + DBC),
				eq(UserProfile.class))).willReturn(responseEntity);

		// when
		profileService.getRODetails(DBC);

		// then
		verify(keycloakRestTemplate).getForEntity(eq(PROFILE_URL + "/api/users/ro-user/" + DBC), eq(UserProfile.class));

	}

	@Test
	public void shouldGetRODetails() throws Exception {
		// given
		UserProfile userProfile = new UserProfile();
		ResponseEntity responseEntity = new ResponseEntity(userProfile, HttpStatus.OK);
		given(keycloakRestTemplate.getForEntity(eq(PROFILE_URL + "/api/users/ro-user/" + DBC),
				eq(UserProfile.class))).willReturn(responseEntity);

		// when
		UserProfile user = profileService.getRODetails(DBC);

		// then
		Assert.assertSame(userProfile, user);

	}

	@Test
	public void shouldGetAllUsers() throws Exception {
		// given
		JSONObject jsonObject = new JSONObject();
		ResponseEntity responseEntity = new ResponseEntity(jsonObject, HttpStatus.OK);
		given(keycloakRestTemplate.getForEntity(any(String.class), eq(JSONObject.class))).willReturn(responseEntity);

		// when
		profileService.getAllUsers(PERMISSIONS, DBC);

		// then
		verify(keycloakRestTemplate).getForEntity(eq(PROFILE_URL + "/api/users?offset&limit&designatedBodyCode=" + DBC +
				"&permissions=" + PERMISSIONS), eq(JSONObject.class));
	}


//	public static void mockUserprofile(String userName, String... designatedBodyCodes) {
//		UserProfile userProfile = new UserProfile();
//		userProfile.setUserName(userName);
//		userProfile.setDesignatedBodyCodes(Sets.newSet(designatedBodyCodes));
//		AuthenticatedUser authenticatedUser = new AuthenticatedUser(userName, "dummyToekn", userProfile, null);
//		UsernamePasswordAuthenticationToken authenticationToken = new
//				UsernamePasswordAuthenticationToken(authenticatedUser, null);
//
//		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//	}
//
//	public static UserProfile mockWithPermissions(String userName, String...permissions) {
//		UserProfile userProfile = new UserProfile();
//		userProfile.setUserName(userName);
//		userProfile.setDesignatedBodyCodes(newSet(DBC));
//		Set<String> permSet = new HashSet<>(Arrays.asList(permissions));
//		userProfile.setPermissions(permSet);
//		return userProfile;
//	}
}
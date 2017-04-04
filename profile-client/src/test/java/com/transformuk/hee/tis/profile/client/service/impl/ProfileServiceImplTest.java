package com.transformuk.hee.tis.profile.client.service.impl;

import com.transformuk.hee.tis.profile.client.service.impl.ProfileServiceImpl;
import com.transformuk.hee.tis.profile.dto.PagedTraineeIdResponse;
import com.transformuk.hee.tis.profile.dto.TraineeId;
import com.transformuk.hee.tis.profile.dto.UserProfile;
import com.transformuk.hee.tis.security.client.KeycloakRestTemplate;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class ProfileServiceImplTest {

	private static final String DBC = "1-DGBODY";
	private static final String PROFILE_URL = "http://localhost:8888/trainee-id";
	private static final String PERMISSIONS = "revalidation:submit:on:behalf:of:ro";

	@Mock
	private KeycloakRestTemplate keycloakRestTemplate;
	@InjectMocks
	private ProfileServiceImpl profileServiceImpl;

	@Before
	public void setUp() throws Exception {
		profileServiceImpl.setServiceUrl("http://localhost:8888/trainee-id");
	}


	@Test
	public void shouldGetAllTraineeIdMappings() {
		// given
		Pageable pageable = new PageRequest(0, 100);
		List<TraineeId> traineeIds = newArrayList(new TraineeId(999L, "1234567	"));
		PagedTraineeIdResponse response = new PagedTraineeIdResponse(traineeIds, 1);
		given(keycloakRestTemplate.getForObject(any(URI.class), eq(PagedTraineeIdResponse.class))).willReturn(response);

		// when
		Page<TraineeId> allTraineeIdMappings = profileServiceImpl.getPagedTraineeIds(DBC, pageable);

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
		profileServiceImpl.getRODetails(DBC);

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
		UserProfile user = profileServiceImpl.getRODetails(DBC);

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
		profileServiceImpl.getAllUsers(PERMISSIONS, DBC);

		// then
		verify(keycloakRestTemplate).getForEntity(eq(PROFILE_URL + "/api/users?offset&limit&designatedBodyCode=" + DBC +
				"&permissions=" + PERMISSIONS), eq(JSONObject.class));
	}
}
package com.transformuk.hee.tis.profile.client.service.impl;

import com.transformuk.hee.tis.profile.client.service.ProfileService;
import com.transformuk.hee.tis.profile.dto.*;
import com.transformuk.hee.tis.security.client.KeycloakRestTemplate;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

/**
 * The default implementation for the profile service. Provides methods to which we use to communicate and use the
 * Tis Profile Service
 */
@Service
public class ProfileServiceImpl implements ProfileService {

	private static final String PAGE_QUERY_PARAM = "page";
	private static final String SIZE_QUERY_PARAM = "size";
	private static final String TRAINEE_MAPPINGS_ENDPOINT = "/api/trainee-id/{dbc}/mappings";
	private static final String USER_INFO_ENDPOINT = "/api/userinfo";
	private static final String USERS_RO_USER_ENDPOINT = "/api/users/ro-user/";
	private static final String USERS_ENDPOINT = "/api/users";
	private static final String TRAINEE_DBC_REGISTER_ENDPOINT = "/api/trainee-id/{designatedBodyCode}/register";

	private KeycloakRestTemplate keycloakRestTemplate;

	@Value("${profile.pagination.offset}")
	private String offset;

	@Value("${profile.pagination.limit}")
	private String limit;

	@Value("${profile.service.url}")
	private String serviceUrl;

	@Autowired
	public ProfileServiceImpl(KeycloakRestTemplate keycloakRestTemplate) {
		this.keycloakRestTemplate = keycloakRestTemplate;
	}

	/**
	 * Return a paginated result of trainee id's for a designated body code
	 *
	 * @param dbc      designated body code
	 * @param pageable Pageable object that defines the size and page number to return
	 * @return Paged object containing trainee id's, pages and totals
	 */
	public Page<TraineeId> getPagedTraineeIds(String dbc, Pageable pageable) {
		UriComponents uriComponents = fromHttpUrl(serviceUrl + TRAINEE_MAPPINGS_ENDPOINT)
				.queryParam(PAGE_QUERY_PARAM, pageable.getPageNumber())
				.queryParam(SIZE_QUERY_PARAM, pageable.getPageSize())
				.buildAndExpand(dbc);
		PagedTraineeIdResponse pagedTraineeIdResponse = keycloakRestTemplate.getForObject(uriComponents.encode().toUri(),
				PagedTraineeIdResponse.class);
		return new PageImpl<>(pagedTraineeIdResponse.getContent(), pageable, pagedTraineeIdResponse.getTotalElements());
	}

	/**
	 * Returns trainee ids for given list of gmc doctors
	 *
	 * @return List of {@link TraineeId}
	 */
	public List<TraineeProfileDto> getTraineeIdsForGmcNumbers(String designatedBodyCode, List<RegistrationRequest> registrationRequests) {
		HttpEntity<List<RegistrationRequest>> requestEntity = new HttpEntity<>(registrationRequests);
		String url = serviceUrl + TRAINEE_DBC_REGISTER_ENDPOINT;
		UriComponents uriComponents = fromHttpUrl(url).buildAndExpand(designatedBodyCode);
		ResponseEntity<TraineeIdListResponse> response = keycloakRestTemplate.exchange(uriComponents.encode().toUri(), POST,
				requestEntity, TraineeIdListResponse.class);
		return response.getBody().getTraineeIds();
	}


	/**
	 * Get a UserProfile for the current authenticated user
	 *
	 * @return UserProfile containing information of the current authenticated user
	 */
	public UserProfile getProfile() {
		ResponseEntity<UserProfile> responseEntity = keycloakRestTemplate.getForEntity(serviceUrl + USER_INFO_ENDPOINT, UserProfile.class);
		return responseEntity.getBody();
	}

	/**
	 * Get the user profile of revalidation officer for a designated body code
	 *
	 * @param designatedBodyCode the designated body code of the officer you wish to search for
	 * @return The UserProfile of the revalidation officer for the designated body code
	 */
	public UserProfile getRODetails(String designatedBodyCode) {
		String url = serviceUrl + USERS_RO_USER_ENDPOINT + designatedBodyCode;
		ResponseEntity<UserProfile> responseEntity = keycloakRestTemplate.getForEntity(url, UserProfile.class);
		return responseEntity.getBody();
	}

	/**
	 * Gets user details given designatedBody from Auth Service
	 * (https://dev-api.transformcloud.net/profile/swagger-ui.html#!/login-controller/getUsersUsingGET)
	 *
	 * @param designatedBodyCodes user's designatedBody
	 * @param permissions         submit_to_gmc permissions
	 * @return json representation of AuthService's getUser's end point response.
	 */
	public JSONObject getAllUsers(String permissions, String... designatedBodyCodes) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serviceUrl + USERS_ENDPOINT)
				.queryParam("offset", offset)
				.queryParam("limit", limit)
				.queryParam("designatedBodyCode", designatedBodyCodes)
				.queryParam("permissions", permissions);
		ResponseEntity<JSONObject> responseEntity = keycloakRestTemplate.getForEntity(builder.toUriString(), JSONObject.class);
		return responseEntity.getBody();
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
}

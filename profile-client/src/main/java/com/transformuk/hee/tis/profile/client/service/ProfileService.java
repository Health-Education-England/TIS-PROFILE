package com.transformuk.hee.tis.profile.client.service;

import com.transformuk.hee.tis.profile.dto.PagedTraineeIdResponse;
import com.transformuk.hee.tis.profile.dto.TraineeId;
import com.transformuk.hee.tis.profile.dto.UserProfile;
import com.transformuk.hee.tis.security.client.KeycloakRestTemplate;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Service
public class ProfileService {

	private static final String PAGE_QUERY_PARAM = "page";
	private static final String SIZE_QUERY_PARAM = "size";
	private static final String TRAINEE_MAPPINGS_ENDPOINT = "/api/trainee-id/{dbc}/mappings";
	private static final String USER_INFO_ENDPOINT = "/api/userinfo";
	private static final String USERS_RO_USER_ENDPOINT = "/api/users/ro-user/";
	private static final String USERS_ENDPOINT = "/api/users";

	private KeycloakRestTemplate keycloakRestTemplate;

	@Value("${profile.pagination.offset}")
	private String offset;

	@Value("${profile.pagination.limit}")
	private String limit;

	@Value("${profile.service.url}")
	private String serviceUrl;

	@Autowired
	public ProfileService(KeycloakRestTemplate keycloakRestTemplate) {
		this.keycloakRestTemplate = keycloakRestTemplate;
	}

	public Page<TraineeId> getPagedTraineeIds(String dbc, Pageable pageable) {
		UriComponents uriComponents = fromHttpUrl(serviceUrl + TRAINEE_MAPPINGS_ENDPOINT)
				.queryParam(PAGE_QUERY_PARAM, pageable.getPageNumber())
				.queryParam(SIZE_QUERY_PARAM, pageable.getPageSize())
				.buildAndExpand(dbc);
		PagedTraineeIdResponse pagedTraineeIdResponse = keycloakRestTemplate.getForObject(uriComponents.encode().toUri(),
				PagedTraineeIdResponse.class);
		return new PageImpl<>(pagedTraineeIdResponse.getContent(), pageable, pagedTraineeIdResponse.getTotalElements());
	}

	public UserProfile getProfile() {
		ResponseEntity<UserProfile> responseEntity = keycloakRestTemplate.getForEntity(serviceUrl + USER_INFO_ENDPOINT, UserProfile.class);
		return responseEntity.getBody();
	}

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

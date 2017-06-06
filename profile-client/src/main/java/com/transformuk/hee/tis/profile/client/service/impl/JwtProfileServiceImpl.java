package com.transformuk.hee.tis.profile.client.service.impl;

import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.security.service.JwtProfileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static java.util.Objects.requireNonNull;

/**
 * Service that's used within the context of a microservices spring security.
 * <p>
 * Its used to retrieve the the users details from the profile service.
 */
public class JwtProfileServiceImpl implements JwtProfileService {

	private static final String USER_INFO_ENDPOINT = "/api/userinfo";
	private static final String OIDC_TOKEN_HEADER = "OIDC_access_token";
	private static final String AUTH_TOKEN_HEADER = "Authorization";
	private static final String AUTH_TOKEN_BEARER = "Bearer ";

	@Value("${profile.service.url}")
	private String serviceUrl;

	private RestTemplate restTemplate;

	public JwtProfileServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * Get a UserProfile using the provided security token. This method should only be used during authenticating the user
	 * in Spring Security
	 *
	 * @return UserProfile containing information of the current authenticated user
	 */
	public UserProfile getProfile(String securityToken) {
		requireNonNull(securityToken, "securityToken must not be null");
		HttpHeaders headers = new HttpHeaders();
		headers.set(OIDC_TOKEN_HEADER, securityToken);
		headers.set(AUTH_TOKEN_HEADER, AUTH_TOKEN_BEARER + securityToken);
		HttpEntity<?> entity = new HttpEntity<String>(headers);
		ResponseEntity<UserProfile> responseEntity = restTemplate.exchange(serviceUrl + USER_INFO_ENDPOINT, HttpMethod.GET, entity,
				UserProfile.class);
		return responseEntity.getBody();
	}
}

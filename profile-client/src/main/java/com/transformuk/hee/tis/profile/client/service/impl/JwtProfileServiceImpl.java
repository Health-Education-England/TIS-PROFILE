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

public class JwtProfileServiceImpl implements JwtProfileService {
	private static final String USER_INFO_ENDPOINT = "/api/userinfo";

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
		headers.set("OIDC_access_token", securityToken);
		HttpEntity<?> entity = new HttpEntity<String>(headers);
		ResponseEntity<UserProfile> responseEntity = restTemplate.exchange(serviceUrl + USER_INFO_ENDPOINT, HttpMethod.GET, entity,
				UserProfile.class);
		return responseEntity.getBody();
	}
}

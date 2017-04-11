package com.transformuk.hee.tis.profile.client.config;

import com.transformuk.hee.tis.profile.client.service.impl.JwtProfileServiceImpl;
import com.transformuk.hee.tis.security.config.TisSecurityConfig;
import com.transformuk.hee.tis.security.service.JwtProfileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@Import(TisSecurityConfig.class)
public class ProfileClientConfig {

	@Value("${PROFILE_REST_TIMEOUT:1000}")
	private int timeout;

	@Bean
	public RestTemplate profileClientRestTemplate(){
		RestTemplate restTemplate = new RestTemplate();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(timeout);
		factory.setConnectTimeout(timeout);
		restTemplate.setRequestFactory(factory);
		return restTemplate;
	}

	@Bean
	public JwtProfileService jwtProfileService(RestTemplate profileClientRestTemplate) {
		return new JwtProfileServiceImpl(profileClientRestTemplate);
	}
}

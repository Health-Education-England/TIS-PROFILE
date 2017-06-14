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

/**
 * Configuration class that must be imported if you wish your microservice to communicate to the profile service
 * during spring security checks
 * <p>
 * This class was called ProfileClientProdConfig but now renamed to be more informative
 */
@Configuration
@Import(TisSecurityConfig.class)
public class JwtSpringSecurityConfig {

	@Value("${PROFILE_REST_TIMEOUT:30000}")
	private int timeout;

	@Bean
	public JwtProfileService jwtProfileService() {
		RestTemplate restTemplate = new RestTemplate();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(timeout);
		factory.setConnectTimeout(timeout);
		restTemplate.setRequestFactory(factory);

		return new JwtProfileServiceImpl(restTemplate);
	}
}

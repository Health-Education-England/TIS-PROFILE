package com.transformuk.hee.tis.profile.client.config;


import com.transformuk.hee.tis.security.client.KeycloakClientRequestFactory;
import com.transformuk.hee.tis.security.client.KeycloakRestTemplate;
import com.transformuk.hee.tis.security.config.KeycloakClientConfig;
import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class to define a rest template to be used by the Profile Client
 * <p>
 * This rest template is to only be used when a service needs to communicate with the profile service
 * on behalf of itself, where there is no interaction if a browser or end client.
 * e.g. during an ETL, some batch processing etc.
 * <p>
 * This rest template talks to keycloak directly to get a jwt key using the services credentials
 */
@Configuration
@Import(KeycloakClientConfig.class)
public class ProfileClientConfig {
	@Bean
	public RestTemplate profileRestTemplate(Keycloak keycloak) {
		final KeycloakClientRequestFactory keycloakClientRequestFactory = new KeycloakClientRequestFactory(keycloak);
		return new KeycloakRestTemplate(keycloakClientRequestFactory);
	}
}

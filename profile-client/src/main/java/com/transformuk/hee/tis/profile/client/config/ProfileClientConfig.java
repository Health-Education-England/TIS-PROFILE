package com.transformuk.hee.tis.profile.client.config;

import com.transformuk.hee.tis.security.client.KeycloakClientRequestFactory;
import com.transformuk.hee.tis.security.client.KeycloakRestTemplate;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Provides a Keycloak client config
 */
@Configuration
public class ProfileClientConfig {

	@Value("${kc.realm}")
	private String realm;

	@Value("${kc.client.id}")
	private String clientId;

	@Value("${kc.server.url}")
	private String serverUrl;

	@Value("${kc.username}")
	private String userName;

	@Value("${kc.password}")
	private String password;

	@Value("${kc.timeout}")
	private int timeout;


	@Bean
	public Keycloak keycloak() {
		Keycloak kc = KeycloakBuilder.builder()
				.serverUrl(serverUrl)
				.realm(realm)
				.clientId(clientId)
				.username(userName)
				.password(password)
				.build();
		return kc;
	}

	@Bean
	public KeycloakRestTemplate keycloakRestTemplate(KeycloakClientRequestFactory keycloakClientRequestFactory) {
		return new KeycloakRestTemplate(keycloakClientRequestFactory);
	}

	@Bean
	public KeycloakClientRequestFactory keycloakClientRequestFactory(Keycloak keycloak) {
		KeycloakClientRequestFactory factory = new KeycloakClientRequestFactory(keycloak);
		factory.setReadTimeout(timeout);
		factory.setConnectTimeout(timeout);
		return factory;
	}

}

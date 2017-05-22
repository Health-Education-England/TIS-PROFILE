package com.transformuk.hee.tis.profile.config;

import com.transform.keycloak.KeyclockAdminClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * Created by sunil on 19/05/2017.
 */
public class KeyclockAdminClientConfig {

	@Value("${kc.master.realm}")
	private String masterRealm;

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

	@Bean
	public KeyclockAdminClient keyclockAdminClient() {
		KeyclockAdminClient client = new KeyclockAdminClient();
		client.init(serverUrl, masterRealm, clientId, userName, password);
		return client;
	}
}

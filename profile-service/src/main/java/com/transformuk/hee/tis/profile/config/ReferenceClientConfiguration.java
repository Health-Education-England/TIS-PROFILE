package com.transformuk.hee.tis.profile.config;

import com.transformuk.hee.tis.reference.client.config.ReferenceClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ReferenceClientConfiguration extends ReferenceClientConfig{

	@Bean
	public RestTemplate referenceRestTemplate() {
		return super.prodBrowserInitiatedReferenceRestTemplate();
	}
}

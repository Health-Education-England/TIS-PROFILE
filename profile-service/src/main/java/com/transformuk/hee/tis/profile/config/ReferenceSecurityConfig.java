package com.transformuk.hee.tis.profile.config;

import com.transformuk.hee.tis.reference.client.config.ReferenceClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ReferenceClientConfig.class)
public class ReferenceSecurityConfig {

}

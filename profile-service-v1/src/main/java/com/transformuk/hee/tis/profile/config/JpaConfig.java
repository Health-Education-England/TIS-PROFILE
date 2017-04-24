package com.transformuk.hee.tis.profile.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.transformuk.hee.tis.profile.repository", repositoryFactoryBeanClass =
		EnversRevisionRepositoryFactoryBean.class)
public class JpaConfig {
}

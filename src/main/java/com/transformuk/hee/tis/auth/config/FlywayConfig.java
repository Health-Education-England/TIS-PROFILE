package com.transformuk.hee.tis.auth.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Flyway configuration
 */
@Configuration
public class FlywayConfig {

	@Value("${flyway.url}")
	private String url;

	@Value("${flyway.password}")
	private String password;

	@Value("${flyway.user}")
	private String user;

	@Value("${flyway.locations}")
	private String migrationFilesLocations;

	@Value("${flyway.baseline-on-migrate}")
	private boolean baseLineOnMigrate;

	@Value("${flyway.clean-on-validation-error}")
	private boolean cleanOnValidationError;

	@Value("${flyway.out-of-order}")
	private boolean outOfOrder;

	@Value("${flyway.baseline-version}")
	private String baseLineVersion;

	@Bean(initMethod = "migrate")
	Flyway flyway() {
		Flyway flyway = new Flyway();
		flyway.setBaselineOnMigrate(baseLineOnMigrate);
		flyway.setLocations(migrationFilesLocations);
		flyway.setBaselineVersionAsString(baseLineVersion);
		flyway.setDataSource(url, user, password);
		flyway.setCleanOnValidationError(cleanOnValidationError);
		flyway.setOutOfOrder(outOfOrder);
		flyway.info();
		return flyway;
	}
}

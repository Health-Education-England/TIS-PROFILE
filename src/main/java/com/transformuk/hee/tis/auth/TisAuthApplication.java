package com.transformuk.hee.tis.auth;

import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.transformuk.hee.tis.audit.repository.TisAuditRepository;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.DispatcherServlet;

import static org.springframework.boot.SpringApplication.run;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
public class TisAuthApplication {

	private static final int TIMEOUT = 6000;

	@Value("${flyway.url}")
	private String url;

	@Value("${flyway.password}")
	private String password;

	@Value("${flyway.user}")
	private String user;

	@Value("${flyway.baseline-version}")
	private String baseLineVersion;

	@Value("${flyway.locations}")
	private String migrationFilesLocations;

	@Value("${flyway.schemas}")
	private String schemas;

	@Value("${flyway.baseline-on-migrate}")
	private boolean baseLineOnMigrate;

	@Value("${flyway.clean-on-validation-error}")
	private boolean cleanOnValidationError;

	@Value("${flyway.out-of-order}")
	private boolean outOfOrder;

	public static void main(String[] args) {run(TisAuthApplication.class, args);}

	@Bean(initMethod = "migrate")
	Flyway flyway() {
		Flyway flyway = new Flyway();
		flyway.setBaselineOnMigrate(baseLineOnMigrate);
		flyway.setLocations(migrationFilesLocations);
		flyway.setDataSource(url,user,password);
		flyway.setSchemas(schemas);
		flyway.setCleanOnValidationError(cleanOnValidationError);
		flyway.setOutOfOrder(outOfOrder);
		flyway.info();
		flyway.setBaselineVersionAsString(baseLineVersion);
		return flyway;
	}

	/**
	 * Allows to throw exception when no exception handler found
	 * @return DispatcherServlet
	 */
	@Bean
	public DispatcherServlet dispatcherServlet() {
		DispatcherServlet ds = new DispatcherServlet();
		ds.setThrowExceptionIfNoHandlerFound(true);
		return ds;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(TIMEOUT);
		factory.setConnectTimeout(TIMEOUT);
		restTemplate.setRequestFactory(factory);
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		return restTemplate;
	}

	@Bean
	public Validator validator() {
		return new LocalValidatorFactoryBean();
	}

	@Bean
	public AuditEventRepository auditEventRepository() {
		return new TisAuditRepository();
	}

	@Bean
	public com.jayway.jsonpath.Configuration jsonPathConfiguration() {
		return com.jayway.jsonpath.Configuration.defaultConfiguration()
				.jsonProvider(new JacksonJsonProvider()).mappingProvider(new JacksonMappingProvider());
	}
}

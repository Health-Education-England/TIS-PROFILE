package com.transformuk.hee.tis.auth;

import com.transformuk.hee.tis.audit.repository.TisAuditRepository;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.DispatcherServlet;

import static org.springframework.boot.SpringApplication.run;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@EnableSpringDataWebSupport
public class TisProfileApplication {

	public static void main(String[] args) {
		run(TisProfileApplication.class, args);
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	/**
	 * Allows to throw exception when no exception handler found
	 *
	 * @return DispatcherServlet
	 */
	@Bean
	public DispatcherServlet dispatcherServlet() {
		DispatcherServlet ds = new DispatcherServlet();
		ds.setThrowExceptionIfNoHandlerFound(true);
		return ds;
	}

	@Bean
	public Validator validator() {
		return new LocalValidatorFactoryBean();
	}

	@Bean
	public AuditEventRepository auditEventRepository() {
		return new TisAuditRepository();
	}

}

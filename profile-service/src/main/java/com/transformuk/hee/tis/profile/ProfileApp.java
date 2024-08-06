package com.transformuk.hee.tis.profile;

import com.transformuk.hee.tis.audit.repository.TisAuditRepository;
import com.transformuk.hee.tis.profile.config.ApplicationProperties;
import com.transformuk.hee.tis.profile.config.DefaultProfileUtil;
import io.github.jhipster.config.JHipsterConstants;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.DispatcherServlet;

@ComponentScan({
    "com.transformuk.hee.tis.profile",
    "com.transformuk.hee.tis.reference.client"
})
@EnableAutoConfiguration(exclude = {MetricFilterAutoConfiguration.class,
    MetricRepositoryAutoConfiguration.class})
@EnableConfigurationProperties({ApplicationProperties.class})
@PropertySource(
    {
        "classpath:/config/application.yml",
        "classpath:/config/referenceclientapplication.properties"
    }
)
public class ProfileApp {

  public static final String SERVICE_NAME = "tis-profile";
  private static final Logger log = LoggerFactory.getLogger(ProfileApp.class);
  private final Environment env;

  public ProfileApp(Environment env) {
    this.env = env;
  }

  /**
   * Main method, used to run the application.
   *
   * @param args the command line arguments
   * @throws UnknownHostException if the local host name could not be resolved into an address
   */
  public static void main(String[] args) throws UnknownHostException {
    SpringApplication app = new SpringApplication(ProfileApp.class);
    DefaultProfileUtil.addDefaultProfile(app);
    Environment env = app.run(args).getEnvironment();
    String protocol = "http";
    if (env.getProperty("server.ssl.key-store") != null) {
      protocol = "https";
    }
    log.info("\n----------------------------------------------------------\n\t" +
            "Application '{}' is running! Access URLs:\n\t" +
            "Local: \t\t{}://localhost:{}\n\t" +
            "External: \t{}://{}:{}\n\t" +
            "Profile(s): \t{}\n----------------------------------------------------------",
        env.getProperty("spring.application.name"),
        protocol,
        env.getProperty("server.port"),
        protocol,
        InetAddress.getLocalHost().getHostAddress(),
        env.getProperty("server.port"),
        env.getActiveProfiles());
  }

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  /**
   * Initializes profile.
   * <p>
   * Spring profiles can be configured with a program arguments --spring.profiles.active=your-active-profile
   * <p>
   * You can find more information on how profiles work with JHipster on <a
   * href="http://jhipster.github.io/profiles/">http://jhipster.github.io/profiles/</a>.
   */
  @PostConstruct
  public void initApplication() {
    Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
    if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles
        .contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
      log.error("You have misconfigured your application! It should not run " +
          "with both the 'dev' and 'prod' profiles at the same time.");
    }
    if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles
        .contains(JHipsterConstants.SPRING_PROFILE_CLOUD)) {
      log.error("You have misconfigured your application! It should not" +
          "run with both the 'dev' and 'cloud' profiles at the same time.");
    }
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

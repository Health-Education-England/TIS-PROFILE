package com.transformuk.hee.tis.profile.config;

import com.transformuk.hee.tis.profile.monitoring.HealthEndpoint;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MonitoringConfiguration {

  @Bean
  public Endpoint healthz(){
    return new HealthEndpoint();
  }
}

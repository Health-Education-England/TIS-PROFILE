package com.transformuk.hee.tis.profile.config;

import com.transformuk.hee.tis.profile.aop.auditing.AuditingAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AuditingAspectConfiguration {

  @Bean
  @Autowired
  public AuditingAspect auditingAspect(AuditEventRepository auditEventRepository) {
    return new AuditingAspect(auditEventRepository);
  }
}

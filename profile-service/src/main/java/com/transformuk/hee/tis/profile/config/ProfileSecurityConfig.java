package com.transformuk.hee.tis.profile.config;

import com.transformuk.hee.tis.profile.assembler.UserProfileAssembler;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.profile.service.JwtUtil;
import com.transformuk.hee.tis.profile.service.LoginService;
import com.transformuk.hee.tis.profile.service.ProfileUserDetailsServiceImpl;
import com.transformuk.hee.tis.security.JwtAuthenticationEntryPoint;
import com.transformuk.hee.tis.security.JwtAuthenticationProvider;
import com.transformuk.hee.tis.security.RestAccessDeniedHandler;
import com.transformuk.hee.tis.security.config.TisSecurityConfig;
import com.transformuk.hee.tis.security.service.UserDetailsService;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * Tis Security spring bean config.
 */
@Configuration
@ComponentScan(value = "com.transformuk.hee.tis.security",
    excludeFilters = {@ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        value = {TisSecurityConfig.class})
    })
public class ProfileSecurityConfig {
  private LoginService loginService;
  private UserProfileAssembler userProfileAssembler;
  private HeeUserRepository userRepository;
  private JwtUtil jwtUtil;

  @Bean
  public UserDetailsService userDetailsService() {
    return new ProfileUserDetailsServiceImpl(loginService, userProfileAssembler, userRepository, jwtUtil);
  }

  @Bean
  public JwtAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
    return new JwtAuthenticationProvider(userDetailsService);
  }

  @Bean
  public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
    return new JwtAuthenticationEntryPoint();
  }

  @Bean
  public RestAccessDeniedHandler accessDeniedHandler(AuditEventRepository auditEventRepository) {
    return new RestAccessDeniedHandler(auditEventRepository);
  }

}

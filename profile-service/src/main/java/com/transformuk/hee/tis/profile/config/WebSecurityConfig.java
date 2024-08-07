package com.transformuk.hee.tis.profile.config;

import com.transformuk.hee.tis.security.JwtAuthenticationEntryPoint;
import com.transformuk.hee.tis.security.JwtAuthenticationProvider;
import com.transformuk.hee.tis.security.JwtAuthenticationSuccessHandler;
import com.transformuk.hee.tis.security.RestAccessDeniedHandler;
import com.transformuk.hee.tis.security.filter.JwtAuthenticationTokenFilter;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtAuthenticationEntryPoint unauthorizedHandler;
  @Autowired
  private RestAccessDeniedHandler accessDeniedHandler;
  @Autowired
  private JwtAuthenticationProvider authenticationProvider;

  @Bean
  @Override
  public AuthenticationManager authenticationManager() throws Exception {
    return new ProviderManager(Arrays.asList(authenticationProvider));
  }

  public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
    JwtAuthenticationTokenFilter authenticationTokenFilter = new JwtAuthenticationTokenFilter(
        "/api/**");
    authenticationTokenFilter.setAuthenticationManager(authenticationManager());
    authenticationTokenFilter
        .setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
    return authenticationTokenFilter;
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/api/userinfo", "/api/userupdate");
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        // we don't need CSRF because our token is invulnerable
        .csrf().disable()
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
        .accessDeniedHandler(accessDeniedHandler)
        .and()
        // don't create session
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //.and()
    // Custom JWT based security filter
    httpSecurity
        .addFilterBefore(authenticationTokenFilterBean(),
            UsernamePasswordAuthenticationFilter.class);
  }
}

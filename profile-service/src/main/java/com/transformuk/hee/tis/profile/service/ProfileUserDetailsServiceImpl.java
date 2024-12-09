package com.transformuk.hee.tis.profile.service;

import com.transformuk.hee.tis.profile.assembler.UserProfileAssembler;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.security.service.UserDetailsService;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class to retrieve user profile details from profile store.
 */
@Transactional(readOnly = true)
public class ProfileUserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private final LoginService loginService;
  @Autowired
  private final UserProfileAssembler userProfileAssembler;
  @Autowired
  private final JwtUtil jwtUtil;
  @Autowired
  private final HeeUserRepository userRepository;

  @Value("${profile.service.jwt.jwk-set-uri}")
  private String jwkSetUri;

  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  private String issuerUri;

  public ProfileUserDetailsServiceImpl(LoginService loginService,
      UserProfileAssembler userProfileAssembler, HeeUserRepository userRepository,
      JwtUtil jwtUtil) {
    this.loginService = loginService;
    this.userProfileAssembler = userProfileAssembler;
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public Optional<UserProfile> getProfile(String token) {
    Map<String, Object> claims = jwtUtil.getAllClaimsFromToken(token);
    String emailUser = (String) claims.get("email");
    HeeUser user = userRepository.findByActive(emailUser);
    if (user == null) {
      return Optional.empty();
    }
    return Optional.of(userProfileAssembler.toUserProfile(user));
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri)
        .build();
    jwtDecoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuerUri));
    return jwtDecoder;
  }
}

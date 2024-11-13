package com.transformuk.hee.tis.profile.service;

import com.transformuk.hee.tis.profile.assembler.UserProfileAssembler;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.security.service.UserDetailsService;
import java.util.Map;
import java.util.Optional;
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

  private final LoginService loginService;
  private final UserProfileAssembler userProfileAssembler;

  String jwkSetUri = "https://cognito-idp.eu-west-2.amazonaws.com/user_poolId/.well-known/jwks.json";
  JwtUtil jwtUtil = new JwtUtil(jwkSetUri);

  private final HeeUserRepository userRepository;

  public ProfileUserDetailsServiceImpl(LoginService loginService,
      UserProfileAssembler userProfileAssembler, HeeUserRepository userRepository) {
    this.loginService = loginService;
    this.userProfileAssembler = userProfileAssembler;
    this.userRepository = userRepository;
  }

  @Override
  public Optional<UserProfile> getProfile(String token) {
    System.out.println("gggg token   "+token);
    HeeUser user = loginService.getUserByToken(token);
    //String userTest = jwtUtil.getUsernameFromToken(token);
    Map<String, Object> claims = jwtUtil.getAllClaimsFromToken(token);
    String emailUser = (String) claims.get("email");
    //JwtAuthToken jwtAuthToken = decode(token);
    //HeeUser user = userRepository.findByActive(emailUser);
    if (user == null) {
      return Optional.empty();
    }
    return Optional.of(userProfileAssembler.toUserProfile(user));
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri("https://cognito-idp.eu-west-2.amazonaws.com/eu-west-2_u33BF2JoO/.well-known/jwks.json").build();
    jwtDecoder.setJwtValidator(JwtValidators.createDefaultWithIssuer("https://cognito-idp.eu-west-2.amazonaws.com/eu-west-2_u33BF2JoO"));
    return jwtDecoder;
  }

}

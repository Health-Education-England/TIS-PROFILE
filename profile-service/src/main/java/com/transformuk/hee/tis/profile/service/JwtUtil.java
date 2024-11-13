package com.transformuk.hee.tis.profile.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import java.util.Map;

public class JwtUtil {

  private final JwtDecoder jwtDecoder;

  public JwtUtil(String jwkSetUri) {
    this.jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
  }

  public String getUsernameFromToken(String token) {

    Jwt decodedJwt = jwtDecoder.decode(token);
    return decodedJwt.getClaimAsString("cognito:username");
  }

  public Map<String, Object> getAllClaimsFromToken(String token) {
    Jwt decodedJwt = jwtDecoder.decode(token);
    return decodedJwt.getClaims();
  }
}


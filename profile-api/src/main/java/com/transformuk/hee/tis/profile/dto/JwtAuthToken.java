package com.transformuk.hee.tis.profile.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtAuthToken {

  private String username;
  private List<String> cn = new ArrayList<>();
  private Set<String> roles = new HashSet<>();

  public List<String> getCn() {
    return cn;
  }

  public void setCn(List<String> cn) {
    this.cn = cn;
  }

  public Set<String> getRoles() {
    return roles;
  }

  public void setRoles(Set<String> roles) {
    this.roles = roles;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}

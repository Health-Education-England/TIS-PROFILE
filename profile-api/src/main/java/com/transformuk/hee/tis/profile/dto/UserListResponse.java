package com.transformuk.hee.tis.profile.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserListResponse {

  private long total;

  private List<UserInfoResponse> users;

  public UserListResponse(long total, List<UserInfoResponse> users) {
    this.total = total;
    this.users = users;
  }

  public long getTotal() {
    return total;
  }

  public List<UserInfoResponse> getUsers() {
    return users;
  }

  @Override
  public String toString() {
    return "UserListResponse{" +
        ", total=" + total +
        ", users=" + users +
        '}';
  }
}

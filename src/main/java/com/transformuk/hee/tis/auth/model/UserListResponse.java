package com.transformuk.hee.tis.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "List of users for a given pagination and filter")
public class UserListResponse {

	@ApiModelProperty(value = "Total number of users in the system matching the given sorting and filter criteria")
	private long total;

	@ApiModelProperty(value = "List of users to be returned in this response")
	private List<User> users;

	public UserListResponse(long total, List<User> users) {
		this.total = total;
		this.users = users;
	}

	public long getTotal() {
		return total;
	}

	public List<User> getUsers() {
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

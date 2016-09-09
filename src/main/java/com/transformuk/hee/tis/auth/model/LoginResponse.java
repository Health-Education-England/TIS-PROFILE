package com.transformuk.hee.tis.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Set;

/**
 * Response class to hold login results like tokenId, userDetails etc.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {

	private String tokenId;
	private String userName;
	private String fullName;
	private Set<String> roles;
	private Set<String> permissions;

	@ApiModelProperty(value = "tokenId that applications can present as a cookie value "
			+ "for other operations that require authentication")
	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	@ApiModelProperty(value = "the roles the current user has in the TIS system")
	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	@ApiModelProperty(value = "the aggregated Set of permissions for the user ," +
			" with no duplicates corresponding to the roles")
	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return "LoginResponse{" +
				"fullName='" + fullName + '\'' +
				", tokenId='" + tokenId + '\'' +
				", userName='" + userName + '\'' +
				", roles='" + roles + '\'' +
				", permissions='" + permissions + '\'' +
				'}';
	}
}
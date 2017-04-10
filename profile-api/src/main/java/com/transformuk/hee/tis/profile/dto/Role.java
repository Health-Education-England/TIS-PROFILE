package com.transformuk.hee.tis.profile.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Set;

@ApiModel(description = "Role that can be assigned to a user")
public class Role {
	private String name;
	private Set<Permission> permissions;

	@ApiModelProperty(required = true, value = "Role name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(required = true, value = "Permissions given to a role")
	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
}

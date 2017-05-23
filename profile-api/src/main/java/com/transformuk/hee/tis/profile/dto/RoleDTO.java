package com.transformuk.hee.tis.profile.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@ApiModel(description = "Role that can be assigned to a user")
public class RoleDTO implements Serializable {
	@NotNull
	private String name;
	private Set<PermissionDTO> permissions;

	@ApiModelProperty(required = true, value = "Role name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(required = true, value = "Permissions given to a role")
	public Set<PermissionDTO> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<PermissionDTO> permissions) {
		this.permissions = permissions;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		RoleDTO role = (RoleDTO) o;

		if (!Objects.equals(name, role.name)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name);
	}
}

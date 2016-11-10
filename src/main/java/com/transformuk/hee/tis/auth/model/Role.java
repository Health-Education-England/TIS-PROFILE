package com.transformuk.hee.tis.auth.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Role")
@ApiModel(description = "Role that can be assigned to a user")
public class Role {
	private String name;
	private Set<Permission> permissions;

	public Role(String name, Set<Permission> permissions) {
		this.name = name;
		this.permissions = permissions;
	}

	public Role() {
		super();
	}

	@ApiModelProperty(required = true, value = "Role name")
	@Id
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(required = true, value = "Permissions given to a role")
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "RolePermission", joinColumns = @JoinColumn(name = "roleName", referencedColumnName = "name"),
			inverseJoinColumns = @JoinColumn(name = "permissionName", referencedColumnName = "name"))
	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return "Role{" +
				"name='" + name + '\'' +
				", permissions=" + permissions +
				'}';
	}
}

package com.transformuk.hee.tis.auth.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "role")
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
	@JsonProperty("name")
	@Id
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(required = true, value = "Permissions given to a role")
	@JsonProperty("permissions")
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_name", referencedColumnName = "name"),
			inverseJoinColumns = @JoinColumn(name = "permission_name", referencedColumnName = "name"))
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

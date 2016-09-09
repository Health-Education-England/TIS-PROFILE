package com.transformuk.hee.tis.auth.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.transformuk.hee.tis.auth.model.Permission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "role")
@ApiModel(description = "Role that can be assigned to a user")
public class Role {
	private long id;
	private String name;
	private Set<Permission> permissions;

	public Role(String name, Set<Permission> permissions) {
		this.name = name;
		this.permissions = permissions;
	}

	public Role() {
		super();
	}

	@ApiModelProperty(required = true, value = "Role identifier")
	@JsonProperty("id")
	@Id
	@GeneratedValue(strategy = AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ApiModelProperty(required = true, value = "Role name")
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(required = true, value = "Permissions given to a role")
	@JsonProperty("permissions")
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return "Role{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}

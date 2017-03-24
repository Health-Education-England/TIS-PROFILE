package com.transformuk.hee.tis.profile.model;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Role")
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

	@Id
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

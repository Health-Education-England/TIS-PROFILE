package com.transformuk.hee.tis.auth.model;



import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "permission")
@ApiModel(description = "Permission given to a role")
public class Permission {

	private long id;
	private String name;
	private Set<Role> roles;

	public Permission(String name, Set<Role> roles) {
		this.name = name;
		this.roles = roles;
	}

	public Permission(String name) {
		this.name = name;
	}

	public Permission(){
		super();
	}

	@ApiModelProperty(required = true, value = "Permission identifier")
	@JsonProperty("id")
	@Id
	@GeneratedValue(strategy = AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ApiModelProperty(required = true, value = "Permission name")
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(required = true, value = "Roles with this permission")
	@JsonProperty("roles")
	@ManyToMany(mappedBy = "permissions")
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "Permission{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
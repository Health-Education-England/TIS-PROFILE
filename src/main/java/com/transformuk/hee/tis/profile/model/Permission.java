package com.transformuk.hee.tis.profile.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Permission")
@ApiModel(description = "Permission given to a role")
public class Permission {
	private String name;

	public Permission(String name) {
		this.name = name;
	}

	public Permission() {
		super();
	}

	@ApiModelProperty(required = true, value = "Permission name")
	@Id
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Permission{" +
				"name='" + name + '\'' +
				'}';
	}
}
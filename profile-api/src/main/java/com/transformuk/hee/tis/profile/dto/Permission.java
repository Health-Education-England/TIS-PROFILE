package com.transformuk.hee.tis.profile.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Permission given to a role")
public class Permission {
	private String name;

	@ApiModelProperty(required = true, value = "Permission name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

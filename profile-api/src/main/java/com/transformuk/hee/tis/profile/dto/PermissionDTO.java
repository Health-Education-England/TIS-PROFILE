package com.transformuk.hee.tis.profile.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(description = "Permission given to a role")
public class PermissionDTO implements Serializable{
	@NotNull
	private String name;

	private PermissionType type;

	private String description;

	@ApiModelProperty(required = true, value = "Permission name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PermissionType getType() {
		return type;
	}

	public void setType(PermissionType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PermissionDTO that = (PermissionDTO) o;

		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (type != that.type) return false;
		return description != null ? description.equals(that.description) : that.description == null;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "PermissionDTO{" +
				"name='" + name + '\'' +
				", type=" + type +
				", description='" + description + '\'' +
				'}';
	}
}

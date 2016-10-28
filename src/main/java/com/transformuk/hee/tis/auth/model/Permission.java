package com.transformuk.hee.tis.auth.model;



import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "permission")
@ApiModel(description = "Permission given to a role")
public class Permission {
	private String name;
	public Permission(String name) {
		this.name = name;
	}

	public Permission(){
		super();
	}

	@ApiModelProperty(required = true, value = "Permission name")
	@JsonProperty("name")
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
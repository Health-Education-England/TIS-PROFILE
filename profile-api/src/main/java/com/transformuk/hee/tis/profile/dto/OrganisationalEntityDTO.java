package com.transformuk.hee.tis.profile.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@ApiModel(description = "Role that can be assigned to a user")
public class OrganisationalEntityDTO implements Serializable {
  @NotNull
  private String name;

  @ApiModelProperty(required = true, value = "Entity name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    OrganisationalEntityDTO role = (OrganisationalEntityDTO) o;

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

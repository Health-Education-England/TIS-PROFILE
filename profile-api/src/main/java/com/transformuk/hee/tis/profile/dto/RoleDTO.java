package com.transformuk.hee.tis.profile.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class RoleDTO implements Serializable {
  @NotNull
  private String name;
  private Set<PermissionDTO> permissions;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<PermissionDTO> getPermissions() {
    return permissions;
  }

  public void setPermissions(Set<PermissionDTO> permissions) {
    this.permissions = permissions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RoleDTO role = (RoleDTO) o;

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

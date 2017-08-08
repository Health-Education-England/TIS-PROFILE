package com.transformuk.hee.tis.profile.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A Role.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Role implements Serializable {

  private static final long serialVersionUID = 1L;
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
  @NotNull
  @Column(name = "name", nullable = false)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Role name(String name) {
    this.name = name;
    return this;
  }

  @OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
  @JoinTable(name = "RolePermission", joinColumns = @JoinColumn(name = "roleName", referencedColumnName = "name"),
      inverseJoinColumns = @JoinColumn(name = "permissionName", referencedColumnName = "name"))
  public Set<Permission> getPermissions() {
    return permissions;
  }

  public void setPermissions(Set<Permission> permissions) {
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
    Role role = (Role) o;
    if (role.name == null || name == null) {
      return false;
    }
    return Objects.equals(name, role.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }

  @Override
  public String toString() {
    return "Role{" +
        "name='" + name + "'" +
        ", permissions=" + permissions +
        '}';
  }
}

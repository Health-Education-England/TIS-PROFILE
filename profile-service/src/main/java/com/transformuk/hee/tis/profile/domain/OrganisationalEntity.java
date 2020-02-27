package com.transformuk.hee.tis.profile.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Role.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrganisationalEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private String name;

  public OrganisationalEntity(String name) {
    this.name = name;
  }

  public OrganisationalEntity() {
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

  public OrganisationalEntity name(String name) {
    this.name = name;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrganisationalEntity role = (OrganisationalEntity) o;
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
        '}';
  }
}

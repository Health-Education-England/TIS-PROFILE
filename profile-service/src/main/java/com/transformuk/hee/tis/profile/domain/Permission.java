package com.transformuk.hee.tis.profile.domain;

import com.transformuk.hee.tis.profile.dto.PermissionType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A Permission.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Permission implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private PermissionType type;

  @Column(name = "description")
  private String description;

  @Column(name = "principal")
  private String principal;

  @Column(name = "resource")
  private String resource;

  @Column(name = "actions")
  private String actions;

  @Column(name = "effect")
  private String effect;

  public Permission() {
  }

  public Permission(String name, PermissionType type, String description,
                    String principal, String resource, String actions, String effect) {
    this.name = name;
    this.type = type;
    this.description = description;
    this.principal = principal;
    this.resource = resource;
    this.actions = actions;
    this.effect = effect;
  }

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

  public String getPrincipal() {
    return principal;
  }

  public void setPrincipal(String principal) {
    this.principal = principal;
  }

  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  public String getActions() {
    return actions;
  }

  public void setActions(String actions) {
    this.actions = actions;
  }

  public String getEffect() {
    return effect;
  }

  public void setEffect(String effect) {
    this.effect = effect;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Permission that = (Permission) o;
    if (that.name == null || name == null) {
      return false;
    }
    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    if (type != that.type) return false;
    if (description != null ? !description.equals(that.description) : that.description != null) return false;
    if (principal != null ? !principal.equals(that.principal) : that.principal != null) return false;
    if (resource != null ? !resource.equals(that.resource) : that.resource != null) return false;
    if (actions != null ? !actions.equals(that.actions) : that.actions != null) return false;
    return effect != null ? effect.equals(that.effect) : that.effect == null;

  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (principal != null ? principal.hashCode() : 0);
    result = 31 * result + (resource != null ? resource.hashCode() : 0);
    result = 31 * result + (actions != null ? actions.hashCode() : 0);
    result = 31 * result + (effect != null ? effect.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Permission{" +
        "name='" + name + '\'' +
        ", type=" + type +
        ", description='" + description + '\'' +
        ", principal='" + principal + '\'' +
        ", resource='" + resource + '\'' +
        ", actions='" + actions + '\'' +
        ", effect='" + effect + '\'' +
        '}';
  }
}

package com.transformuk.hee.tis.profile.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "Permission given to a role")
public class PermissionDTO implements Serializable {
  @NotNull
  private String name;

  private PermissionType type;

  private String description;

  @NotNull
  private String principal;

  @NotNull
  private String resource;

  @NotNull
  private List<String> actions;

  @NotNull
  private String effect;

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

  @ApiModelProperty(required = true, value = "Principal")
  public String getPrincipal() {
    return principal;
  }

  public void setPrincipal(String principal) {
    this.principal = principal;
  }

  @ApiModelProperty(required = true, value = "Resource")
  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  @ApiModelProperty(required = true, value = "Actions (Create, Update, View, Delete, Submit)")
  public List<String> getActions() {
    return actions;
  }

  public void setActions(List<String> actions) {
    this.actions = actions;
  }

  @ApiModelProperty(required = true, value = "Effect (Allow or Deny)")
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

    PermissionDTO that = (PermissionDTO) o;

    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    if (type != that.type) return false;
    if (description != null ? !description.equals(that.description) : that.description != null) return false;
    if (principal != null ? !principal.equals(that.principal) : that.principal != null) return false;
    if (resource != null ? !resource.equals(that.resource) : that.resource != null) return false;
    if (actions != null ? !actions.equals(that.actions) : that.actions != null) return false;
    return effect != null ? effect.equals(that.effect) : that.effect != null;

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
    return "PermissionDTO{" +
        "name='" + name + '\'' +
        ", type=" + type +
        ", description='" + description + '\'' +
        ", principal='" + principal + '\'' +
        ", resource='" + resource + '\'' +
        ", actions=" + actions +
        ", effect='" + effect + '\'' +
        '}';
  }
}

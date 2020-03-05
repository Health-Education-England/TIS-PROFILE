package com.transformuk.hee.tis.profile.service.dto;

public class UserOrganisationalEntityDTO {

  private Long id;
  private HeeUserDTO heeUserDTO;
  private Long organisationalEntityId;
  private String organisationalEntityName;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public HeeUserDTO getHeeUserDTO() {
    return heeUserDTO;
  }

  public void setHeeUserDTO(HeeUserDTO heeUserDTO) {
    this.heeUserDTO = heeUserDTO;
  }

  public Long getOrganisationalEntityId() {
    return organisationalEntityId;
  }

  public void setOrganisationalEntityId(Long organisationalEntityId) {
    this.organisationalEntityId = organisationalEntityId;
  }

  public String getOrganisationalEntityName() {
    return organisationalEntityName;
  }

  public void setOrganisationalEntityName(String organisationalEntityName) {
    this.organisationalEntityName = organisationalEntityName;
  }
}

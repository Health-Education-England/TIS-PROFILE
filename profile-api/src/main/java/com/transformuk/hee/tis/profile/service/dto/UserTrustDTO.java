package com.transformuk.hee.tis.profile.service.dto;

public class UserTrustDTO {

  private Long id;
  private HeeUserDTO heeUserDTO;
  private Long trustId;
  private String trustCode;
  private String trustName;

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

  public Long getTrustId() {
    return trustId;
  }

  public void setTrustId(Long trustId) {
    this.trustId = trustId;
  }

  public String getTrustCode() {
    return trustCode;
  }

  public void setTrustCode(String trustCode) {
    this.trustCode = trustCode;
  }

  public String getTrustName() {
    return trustName;
  }

  public void setTrustName(String trustName) {
    this.trustName = trustName;
  }
}

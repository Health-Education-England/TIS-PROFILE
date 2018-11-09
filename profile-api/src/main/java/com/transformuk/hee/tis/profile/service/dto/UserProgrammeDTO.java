package com.transformuk.hee.tis.profile.service.dto;

public class UserProgrammeDTO {

  private Long id;
  private HeeUserDTO heeUserDTO;
  private Long programmenId;
  private String programmenName;
  private String programmeNumber;

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

  public Long getProgrammeId() {
    return programmenId;
  }

  public void setProgrammeId(Long programmenId) {
    this.programmenId = programmenId;
  }

  public String getProgrammeName() {
    return programmenName;
  }

  public void setProgrammeName(String programmenName) {
    this.programmenName = programmenName;
  }

  public String getProgrammeNumber() {
    return programmeNumber;
  }

  public void setProgrammeNumber(String programmeNumber) {
    this.programmeNumber = programmeNumber;
  }
}

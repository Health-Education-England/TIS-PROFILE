package com.transformuk.hee.tis.profile.service.dto;

public class UserProgrammeDTO {

  private Long id;
  private HeeUserDTO heeUserDTO;
  private Long programmeId;
  private String programmeName;
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
    return programmeId;
  }

  public void setProgrammeId(Long programmeId) {
    this.programmeId = programmeId;
  }

  public String getProgrammeName() {
    return programmeName;
  }

  public void setProgrammeName(String programmeName) {
    this.programmeName = programmeName;
  }

  public String getProgrammeNumber() {
    return programmeNumber;
  }

  public void setProgrammeNumber(String programmeNumber) {
    this.programmeNumber = programmeNumber;
  }
}

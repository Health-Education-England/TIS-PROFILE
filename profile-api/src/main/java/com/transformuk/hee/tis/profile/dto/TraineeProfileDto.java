package com.transformuk.hee.tis.profile.dto;

import java.time.LocalDate;

public class TraineeProfileDto {
  private Long tisId;
  private String gmcNumber;
  private boolean active;
  private String designatedBodyCode;
  private LocalDate dateAdded;

  public Long getTisId() {
    return tisId;
  }

  public void setTisId(Long tisId) {
    this.tisId = tisId;
  }

  public String getGmcNumber() {
    return gmcNumber;
  }

  public void setGmcNumber(String gmcNumber) {
    this.gmcNumber = gmcNumber;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String getDesignatedBodyCode() {
    return designatedBodyCode;
  }

  public void setDesignatedBodyCode(String designatedBodyCode) {
    this.designatedBodyCode = designatedBodyCode;
  }

  public LocalDate getDateAdded() {
    return dateAdded;
  }

  public void setDateAdded(LocalDate dateAdded) {
    this.dateAdded = dateAdded;
  }
}

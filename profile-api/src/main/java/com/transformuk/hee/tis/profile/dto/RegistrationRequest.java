package com.transformuk.hee.tis.profile.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class RegistrationRequest {

  private String gmcNumber;
  private LocalDate dateAdded;

  public String getGmcNumber() {
    return gmcNumber;
  }

  public void setGmcNumber(String gmcNumber) {
    this.gmcNumber = gmcNumber;
  }

  public LocalDate getDateAdded() {
    return dateAdded;
  }

  public void setDateAdded(LocalDate dateAdded) {
    this.dateAdded = dateAdded;
  }
}

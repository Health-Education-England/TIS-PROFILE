package com.transformuk.hee.tis.profile.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoResponse {
  private String name;
  private String firstName;
  private String lastName;
  private String fullName;
  private String gmcId;
  private Set<String> designatedBodyCodes;

  private String phoneNumber;
  private String emailAddress;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getGmcId() {
    return gmcId;
  }

  public void setGmcId(String gmcId) {
    this.gmcId = gmcId;
  }

  public Set<String> getDesignatedBodyCodes() {
    return designatedBodyCodes;
  }

  public void setDesignatedBodyCodes(Set<String> designatedBodyCodes) {
    this.designatedBodyCodes = designatedBodyCodes;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  @Override
  public String toString() {
    return "UserInfoResponse{" +
        "name='" + name + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", fullName='" + fullName + '\'' +
        ", gmcId='" + gmcId + '\'' +
        ", designatedBodyCodes=" + designatedBodyCodes +
        ", phoneNumber='" + phoneNumber + '\'' +
        ", emailAddress='" + emailAddress + '\'' +
        '}';
  }
}

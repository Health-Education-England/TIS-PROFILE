package com.transformuk.hee.tis.profile.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

import java.util.Set;

/**
 * Response class to hold UserDetails like name, roles, permissions etc.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetails {

  private String userName;
  private String fullName;
  private String firstName;
  private String lastName;
  private Set<String> roles;
  private Set<String> permissions;
  private String gmcId;
  private String designatedBodyCode;
  private String phoneNumber;
  private String emailAddress;

  @ApiModelProperty(value = "the roles the current user has in the TIS system")
  public Set<String> getRoles() {
    return roles;
  }

  public void setRoles(Set<String> roles) {
    this.roles = roles;
  }

  @ApiModelProperty(value = "the aggregated Set of permissions for the user ," +
      " with no duplicates corresponding to the roles")
  public Set<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(Set<String> permissions) {
    this.permissions = permissions;
  }

  @ApiModelProperty(value = "The user name unique identifier")
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @ApiModelProperty(value = "The user's first name")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @ApiModelProperty(value = "The user's last name")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @ApiModelProperty(value = "The user's full name")
  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  @ApiModelProperty(value = "The GMC ID of the user")
  public String getGmcId() {
    return gmcId;
  }

  public void setGmcId(String gmcId) {
    this.gmcId = gmcId;
  }

  @ApiModelProperty(value = "The user's designated body code")
  public String getDesignatedBodyCode() {
    return designatedBodyCode;
  }

  public void setDesignatedBodyCode(String designatedBodyCode) {
    this.designatedBodyCode = designatedBodyCode;
  }

  @ApiModelProperty(value = "The user's phone number")
  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @ApiModelProperty(value = "The users email address")
  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  @Override
  public String toString() {
    return "UserDetails{" +
        "fullName='" + fullName + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", userName='" + userName + '\'' +
        ", roles='" + roles + '\'' +
        ", permissions='" + permissions + '\'' +
        ", gmcId='" + gmcId + '\'' +
        ", designatedBodyCode='" + designatedBodyCode + '\'' +
        ", emailAddress='" + emailAddress + '\'' +
        ", phoneNumber='" + phoneNumber + '\'' +
        '}';
  }
}
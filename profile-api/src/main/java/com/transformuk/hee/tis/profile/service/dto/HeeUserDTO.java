package com.transformuk.hee.tis.profile.service.dto;


import com.transformuk.hee.tis.profile.dto.RoleDTO;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the HeeUser entity.
 */
public class HeeUserDTO implements Serializable {

  @NotNull
  private String name;

  private String firstName;

  private String lastName;

  private String gmcId;

  private String phoneNumber;

  private String emailAddress;

  private Boolean active;

  private String password;

  private Boolean isTemporaryPassword;

  private Set<RoleDTO> roles;
  private Set<String> designatedBodyCodes;
  private Set<UserTrustDTO> associatedTrusts;
  private Set<UserProgrammeDTO> associatedProgrammes;

  public HeeUserDTO() {
  }

  public HeeUserDTO(String name, String firstName, String lastName, String gmcId,
      String phoneNumber, String emailAddress, Boolean active,
      String password, Boolean isTemporaryPassword, Set<RoleDTO> roles,
      Set<String> designatedBodyCodes) {
    this.name = name;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gmcId = gmcId;
    this.phoneNumber = phoneNumber;
    this.emailAddress = emailAddress;
    this.active = active;
    this.password = password;
    this.isTemporaryPassword = isTemporaryPassword;
    this.roles = roles;
    this.designatedBodyCodes = designatedBodyCodes;
  }

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

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean getTemporaryPassword() {
    return isTemporaryPassword;
  }

  public void setTemporaryPassword(Boolean temporaryPassword) {
    isTemporaryPassword = temporaryPassword;
  }

  public Set<RoleDTO> getRoles() {
    return roles;
  }

  public void setRoles(Set<RoleDTO> roles) {
    this.roles = roles;
  }

  public Set<String> getDesignatedBodyCodes() {
    return designatedBodyCodes;
  }

  public void setDesignatedBodyCodes(Set<String> designatedBodyCodes) {
    this.designatedBodyCodes = designatedBodyCodes;
  }

  public Set<UserTrustDTO> getAssociatedTrusts() {
    return associatedTrusts;
  }

  public void setAssociatedTrusts(Set<UserTrustDTO> associatedTrusts) {
    this.associatedTrusts = associatedTrusts;
  }

  public Set<UserProgrammeDTO> getAssociatedProgrammes() {
    return associatedProgrammes;
  }

  public void setAssociatedProgrammes(Set<UserProgrammeDTO> associatedProgrammes) {
    this.associatedProgrammes = associatedProgrammes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    HeeUserDTO heeUserDTO = (HeeUserDTO) o;

    if (!Objects.equals(name, heeUserDTO.name)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }

  @Override
  public String toString() {
    return "HeeUserDTO{" +
        "name='" + name + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", gmcId='" + gmcId + '\'' +
        ", phoneNumber='" + phoneNumber + '\'' +
        ", emailAddress='" + emailAddress + '\'' +
        ", active=" + active +
        ", roles=" + roles +
        ", designatedBodyCodes=" + designatedBodyCodes +
        ", associatedTrusts=" + associatedTrusts +
        ", associatedProgrammes=" + associatedProgrammes +
        '}';
  }
}

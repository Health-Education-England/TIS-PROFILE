package com.transformuk.hee.tis.profile.service.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the PersonalDetails entity.
 */
public class PersonalDetailsDTO implements Serializable {

  private Long id;

  @NotNull
  private BigDecimal tisId;

  private String surnameNb;

  private String legalSurname;

  private String forenames;

  private String legalForenames;

  private String knownAs;

  private String maidenName;

  private String initials;

  private String title;

  private String telephoneNumber;

  private String mobileNumber;

  private String emailAddress;

  private String correspondenceAddress;

  private String correspondenceAddressPostCode;

  private String status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getTisId() {
    return tisId;
  }

  public void setTisId(BigDecimal tisId) {
    this.tisId = tisId;
  }

  public String getSurnameNb() {
    return surnameNb;
  }

  public void setSurnameNb(String surnameNb) {
    this.surnameNb = surnameNb;
  }

  public String getLegalSurname() {
    return legalSurname;
  }

  public void setLegalSurname(String legalSurname) {
    this.legalSurname = legalSurname;
  }

  public String getForenames() {
    return forenames;
  }

  public void setForenames(String forenames) {
    this.forenames = forenames;
  }

  public String getLegalForenames() {
    return legalForenames;
  }

  public void setLegalForenames(String legalForenames) {
    this.legalForenames = legalForenames;
  }

  public String getKnownAs() {
    return knownAs;
  }

  public void setKnownAs(String knownAs) {
    this.knownAs = knownAs;
  }

  public String getMaidenName() {
    return maidenName;
  }

  public void setMaidenName(String maidenName) {
    this.maidenName = maidenName;
  }

  public String getInitials() {
    return initials;
  }

  public void setInitials(String initials) {
    this.initials = initials;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTelephoneNumber() {
    return telephoneNumber;
  }

  public void setTelephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getCorrespondenceAddress() {
    return correspondenceAddress;
  }

  public void setCorrespondenceAddress(String correspondenceAddress) {
    this.correspondenceAddress = correspondenceAddress;
  }

  public String getCorrespondenceAddressPostCode() {
    return correspondenceAddressPostCode;
  }

  public void setCorrespondenceAddressPostCode(String correspondenceAddressPostCode) {
    this.correspondenceAddressPostCode = correspondenceAddressPostCode;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PersonalDetailsDTO personalDetailsDTO = (PersonalDetailsDTO) o;

    if (!Objects.equals(id, personalDetailsDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "PersonalDetailsDTO{" +
        "id=" + id +
        ", tisId='" + tisId + "'" +
        ", surnameNb='" + surnameNb + "'" +
        ", legalSurname='" + legalSurname + "'" +
        ", forenames='" + forenames + "'" +
        ", legalForenames='" + legalForenames + "'" +
        ", knownAs='" + knownAs + "'" +
        ", maidenName='" + maidenName + "'" +
        ", initials='" + initials + "'" +
        ", title='" + title + "'" +
        ", telephoneNumber='" + telephoneNumber + "'" +
        ", mobileNumber='" + mobileNumber + "'" +
        ", emailAddress='" + emailAddress + "'" +
        ", correspondenceAddress='" + correspondenceAddress + "'" +
        ", correspondenceAddressPostCode='" + correspondenceAddressPostCode + "'" +
        ", status='" + status + "'" +
        '}';
  }
}

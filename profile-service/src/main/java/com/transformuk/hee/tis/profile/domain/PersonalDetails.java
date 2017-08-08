package com.transformuk.hee.tis.profile.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A PersonalDetails.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PersonalDetails implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "tisId", precision = 10, scale = 2, nullable = false)
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

  public PersonalDetails tisId(BigDecimal tisId) {
    this.tisId = tisId;
    return this;
  }

  public String getSurnameNb() {
    return surnameNb;
  }

  public void setSurnameNb(String surnameNb) {
    this.surnameNb = surnameNb;
  }

  public PersonalDetails surnameNb(String surnameNb) {
    this.surnameNb = surnameNb;
    return this;
  }

  public String getLegalSurname() {
    return legalSurname;
  }

  public void setLegalSurname(String legalSurname) {
    this.legalSurname = legalSurname;
  }

  public PersonalDetails legalSurname(String legalSurname) {
    this.legalSurname = legalSurname;
    return this;
  }

  public String getForenames() {
    return forenames;
  }

  public void setForenames(String forenames) {
    this.forenames = forenames;
  }

  public PersonalDetails forenames(String forenames) {
    this.forenames = forenames;
    return this;
  }

  public String getLegalForenames() {
    return legalForenames;
  }

  public void setLegalForenames(String legalForenames) {
    this.legalForenames = legalForenames;
  }

  public PersonalDetails legalForenames(String legalForenames) {
    this.legalForenames = legalForenames;
    return this;
  }

  public String getKnownAs() {
    return knownAs;
  }

  public void setKnownAs(String knownAs) {
    this.knownAs = knownAs;
  }

  public PersonalDetails knownAs(String knownAs) {
    this.knownAs = knownAs;
    return this;
  }

  public String getMaidenName() {
    return maidenName;
  }

  public void setMaidenName(String maidenName) {
    this.maidenName = maidenName;
  }

  public PersonalDetails maidenName(String maidenName) {
    this.maidenName = maidenName;
    return this;
  }

  public String getInitials() {
    return initials;
  }

  public void setInitials(String initials) {
    this.initials = initials;
  }

  public PersonalDetails initials(String initials) {
    this.initials = initials;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public PersonalDetails title(String title) {
    this.title = title;
    return this;
  }

  public String getTelephoneNumber() {
    return telephoneNumber;
  }

  public void setTelephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
  }

  public PersonalDetails telephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
    return this;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public PersonalDetails mobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
    return this;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public PersonalDetails emailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

  public String getCorrespondenceAddress() {
    return correspondenceAddress;
  }

  public void setCorrespondenceAddress(String correspondenceAddress) {
    this.correspondenceAddress = correspondenceAddress;
  }

  public PersonalDetails correspondenceAddress(String correspondenceAddress) {
    this.correspondenceAddress = correspondenceAddress;
    return this;
  }

  public String getCorrespondenceAddressPostCode() {
    return correspondenceAddressPostCode;
  }

  public void setCorrespondenceAddressPostCode(String correspondenceAddressPostCode) {
    this.correspondenceAddressPostCode = correspondenceAddressPostCode;
  }

  public PersonalDetails correspondenceAddressPostCode(String correspondenceAddressPostCode) {
    this.correspondenceAddressPostCode = correspondenceAddressPostCode;
    return this;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public PersonalDetails status(String status) {
    this.status = status;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersonalDetails personalDetails = (PersonalDetails) o;
    if (personalDetails.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, personalDetails.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "PersonalDetails{" +
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

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
import java.time.LocalDate;
import java.util.Objects;

/**
 * A EqualityAndDiversity.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EqualityAndDiversity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "tisId", precision = 10, scale = 2, nullable = false)
  private BigDecimal tisId;

  private String maritalStatus;

  private LocalDate dateOfBirth;

  private String gender;

  private String nationality;

  private String dualNationality;

  private String sexualOrientation;

  private String religiousBelief;

  private String ethnicOrigin;

  private Boolean disability;

  private String disabilityDetails;

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

  public EqualityAndDiversity tisId(BigDecimal tisId) {
    this.tisId = tisId;
    return this;
  }

  public String getMaritalStatus() {
    return maritalStatus;
  }

  public void setMaritalStatus(String maritalStatus) {
    this.maritalStatus = maritalStatus;
  }

  public EqualityAndDiversity maritalStatus(String maritalStatus) {
    this.maritalStatus = maritalStatus;
    return this;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public EqualityAndDiversity dateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public EqualityAndDiversity gender(String gender) {
    this.gender = gender;
    return this;
  }

  public String getNationality() {
    return nationality;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  public EqualityAndDiversity nationality(String nationality) {
    this.nationality = nationality;
    return this;
  }

  public String getDualNationality() {
    return dualNationality;
  }

  public void setDualNationality(String dualNationality) {
    this.dualNationality = dualNationality;
  }

  public EqualityAndDiversity dualNationality(String dualNationality) {
    this.dualNationality = dualNationality;
    return this;
  }

  public String getSexualOrientation() {
    return sexualOrientation;
  }

  public void setSexualOrientation(String sexualOrientation) {
    this.sexualOrientation = sexualOrientation;
  }

  public EqualityAndDiversity sexualOrientation(String sexualOrientation) {
    this.sexualOrientation = sexualOrientation;
    return this;
  }

  public String getReligiousBelief() {
    return religiousBelief;
  }

  public void setReligiousBelief(String religiousBelief) {
    this.religiousBelief = religiousBelief;
  }

  public EqualityAndDiversity religiousBelief(String religiousBelief) {
    this.religiousBelief = religiousBelief;
    return this;
  }

  public String getEthnicOrigin() {
    return ethnicOrigin;
  }

  public void setEthnicOrigin(String ethnicOrigin) {
    this.ethnicOrigin = ethnicOrigin;
  }

  public EqualityAndDiversity ethnicOrigin(String ethnicOrigin) {
    this.ethnicOrigin = ethnicOrigin;
    return this;
  }

  public Boolean isDisability() {
    return disability;
  }

  public EqualityAndDiversity disability(Boolean disability) {
    this.disability = disability;
    return this;
  }

  public void setDisability(Boolean disability) {
    this.disability = disability;
  }

  public String getDisabilityDetails() {
    return disabilityDetails;
  }

  public void setDisabilityDetails(String disabilityDetails) {
    this.disabilityDetails = disabilityDetails;
  }

  public EqualityAndDiversity disabilityDetails(String disabilityDetails) {
    this.disabilityDetails = disabilityDetails;
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
    EqualityAndDiversity equalityAndDiversity = (EqualityAndDiversity) o;
    if (equalityAndDiversity.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, equalityAndDiversity.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "EqualityAndDiversity{" +
        "id=" + id +
        ", tisId='" + tisId + "'" +
        ", maritalStatus='" + maritalStatus + "'" +
        ", dateOfBirth='" + dateOfBirth + "'" +
        ", gender='" + gender + "'" +
        ", nationality='" + nationality + "'" +
        ", dualNationality='" + dualNationality + "'" +
        ", sexualOrientation='" + sexualOrientation + "'" +
        ", religiousBelief='" + religiousBelief + "'" +
        ", ethnicOrigin='" + ethnicOrigin + "'" +
        ", disability='" + disability + "'" +
        ", disabilityDetails='" + disabilityDetails + "'" +
        '}';
  }
}

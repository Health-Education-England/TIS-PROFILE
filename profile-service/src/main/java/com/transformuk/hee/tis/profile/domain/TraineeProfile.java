package com.transformuk.hee.tis.profile.domain;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;

@Entity(name = "TraineeProfile")
@Audited
public class TraineeProfile {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long tisId;
  private String gmcNumber;
  private boolean active;
  private String designatedBodyCode;
  private LocalDate dateAdded;

  public TraineeProfile() {
  }

  public TraineeProfile(Long tisId, String gmcNumber) {
    this.gmcNumber = gmcNumber;
    this.tisId = tisId;
  }

  public String getGmcNumber() {
    return gmcNumber;
  }

  public void setGmcNumber(String gmcNumber) {
    this.gmcNumber = gmcNumber;
  }

  public Long getTisId() {
    return tisId;
  }

  public void setTisId(Long tisId) {
    this.tisId = tisId;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public LocalDate getDateAdded() {
    return dateAdded;
  }

  public void setDateAdded(LocalDate dateAdded) {
    this.dateAdded = dateAdded;
  }

  public String getDesignatedBodyCode() {
    return designatedBodyCode;
  }

  public void setDesignatedBodyCode(String designatedBodyCode) {
    this.designatedBodyCode = designatedBodyCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TraineeProfile traineeProfile = (TraineeProfile) o;
    return Objects.equals(tisId, traineeProfile.tisId) &&
        Objects.equals(gmcNumber, traineeProfile.gmcNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tisId, gmcNumber);
  }

  @Override
  public String toString() {
    return "TraineeProfileDto{" +
        "active=" + active +
        ", tisId=" + tisId +
        ", gmcNumber='" + gmcNumber + '\'' +
        ", designatedBodyCode='" + designatedBodyCode + '\'' +
        ", dateAdded=" + dateAdded +
        '}';
  }
}

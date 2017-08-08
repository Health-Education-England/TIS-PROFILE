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
 * A Immigration.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Immigration implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "tisId", precision = 10, scale = 2, nullable = false)
  private BigDecimal tisId;

  private Boolean eeaResident;

  private String permitToWork;

  private String settled;

  private LocalDate visaIssued;

  private LocalDate visaValidTo;

  private String visaDetailsNumber;

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

  public Immigration tisId(BigDecimal tisId) {
    this.tisId = tisId;
    return this;
  }

  public Boolean isEeaResident() {
    return eeaResident;
  }

  public Immigration eeaResident(Boolean eeaResident) {
    this.eeaResident = eeaResident;
    return this;
  }

  public void setEeaResident(Boolean eeaResident) {
    this.eeaResident = eeaResident;
  }

  public String getPermitToWork() {
    return permitToWork;
  }

  public void setPermitToWork(String permitToWork) {
    this.permitToWork = permitToWork;
  }

  public Immigration permitToWork(String permitToWork) {
    this.permitToWork = permitToWork;
    return this;
  }

  public String getSettled() {
    return settled;
  }

  public void setSettled(String settled) {
    this.settled = settled;
  }

  public Immigration settled(String settled) {
    this.settled = settled;
    return this;
  }

  public LocalDate getVisaIssued() {
    return visaIssued;
  }

  public void setVisaIssued(LocalDate visaIssued) {
    this.visaIssued = visaIssued;
  }

  public Immigration visaIssued(LocalDate visaIssued) {
    this.visaIssued = visaIssued;
    return this;
  }

  public LocalDate getVisaValidTo() {
    return visaValidTo;
  }

  public void setVisaValidTo(LocalDate visaValidTo) {
    this.visaValidTo = visaValidTo;
  }

  public Immigration visaValidTo(LocalDate visaValidTo) {
    this.visaValidTo = visaValidTo;
    return this;
  }

  public String getVisaDetailsNumber() {
    return visaDetailsNumber;
  }

  public void setVisaDetailsNumber(String visaDetailsNumber) {
    this.visaDetailsNumber = visaDetailsNumber;
  }

  public Immigration visaDetailsNumber(String visaDetailsNumber) {
    this.visaDetailsNumber = visaDetailsNumber;
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
    Immigration immigration = (Immigration) o;
    if (immigration.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, immigration.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Immigration{" +
        "id=" + id +
        ", tisId='" + tisId + "'" +
        ", eeaResident='" + eeaResident + "'" +
        ", permitToWork='" + permitToWork + "'" +
        ", settled='" + settled + "'" +
        ", visaIssued='" + visaIssued + "'" +
        ", visaValidTo='" + visaValidTo + "'" +
        ", visaDetailsNumber='" + visaDetailsNumber + "'" +
        '}';
  }
}

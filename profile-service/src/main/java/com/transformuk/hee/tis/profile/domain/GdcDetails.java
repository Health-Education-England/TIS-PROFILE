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
 * A GdcDetails.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GdcDetails implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "tisId", precision = 10, scale = 2, nullable = false)
  private BigDecimal tisId;

  private String gdcNumber;

  private String gdcStatus;

  private LocalDate gdcStartDate;

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

  public GdcDetails tisId(BigDecimal tisId) {
    this.tisId = tisId;
    return this;
  }

  public String getGdcNumber() {
    return gdcNumber;
  }

  public void setGdcNumber(String gdcNumber) {
    this.gdcNumber = gdcNumber;
  }

  public GdcDetails gdcNumber(String gdcNumber) {
    this.gdcNumber = gdcNumber;
    return this;
  }

  public String getGdcStatus() {
    return gdcStatus;
  }

  public void setGdcStatus(String gdcStatus) {
    this.gdcStatus = gdcStatus;
  }

  public GdcDetails gdcStatus(String gdcStatus) {
    this.gdcStatus = gdcStatus;
    return this;
  }

  public LocalDate getGdcStartDate() {
    return gdcStartDate;
  }

  public void setGdcStartDate(LocalDate gdcStartDate) {
    this.gdcStartDate = gdcStartDate;
  }

  public GdcDetails gdcStartDate(LocalDate gdcStartDate) {
    this.gdcStartDate = gdcStartDate;
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
    GdcDetails gdcDetails = (GdcDetails) o;
    if (gdcDetails.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, gdcDetails.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "GdcDetails{" +
        "id=" + id +
        ", tisId='" + tisId + "'" +
        ", gdcNumber='" + gdcNumber + "'" +
        ", gdcStatus='" + gdcStatus + "'" +
        ", gdcStartDate='" + gdcStartDate + "'" +
        '}';
  }
}

package com.transformuk.hee.tis.profile.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the GdcDetails entity.
 */
public class GdcDetailsDTO implements Serializable {

  private Long id;

  @NotNull
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

  public String getGdcNumber() {
    return gdcNumber;
  }

  public void setGdcNumber(String gdcNumber) {
    this.gdcNumber = gdcNumber;
  }

  public String getGdcStatus() {
    return gdcStatus;
  }

  public void setGdcStatus(String gdcStatus) {
    this.gdcStatus = gdcStatus;
  }

  public LocalDate getGdcStartDate() {
    return gdcStartDate;
  }

  public void setGdcStartDate(LocalDate gdcStartDate) {
    this.gdcStartDate = gdcStartDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    GdcDetailsDTO gdcDetailsDTO = (GdcDetailsDTO) o;

    if (!Objects.equals(id, gdcDetailsDTO.id)) {
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
    return "GdcDetailsDTO{" +
        "id=" + id +
        ", tisId='" + tisId + "'" +
        ", gdcNumber='" + gdcNumber + "'" +
        ", gdcStatus='" + gdcStatus + "'" +
        ", gdcStartDate='" + gdcStartDate + "'" +
        '}';
  }
}

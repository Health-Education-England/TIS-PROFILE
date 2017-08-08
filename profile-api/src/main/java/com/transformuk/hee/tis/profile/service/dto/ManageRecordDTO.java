package com.transformuk.hee.tis.profile.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the ManageRecord entity.
 */
public class ManageRecordDTO implements Serializable {

  private Long id;

  @NotNull
  private BigDecimal tisId;

  private String recordType;

  private String role;

  private String recordStatus;

  private LocalDate inactiveFrom;

  private BigDecimal changedBy;

  private String inactiveReason;

  private LocalDate inactiveDate;

  private String deletionReason;

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

  public String getRecordType() {
    return recordType;
  }

  public void setRecordType(String recordType) {
    this.recordType = recordType;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getRecordStatus() {
    return recordStatus;
  }

  public void setRecordStatus(String recordStatus) {
    this.recordStatus = recordStatus;
  }

  public LocalDate getInactiveFrom() {
    return inactiveFrom;
  }

  public void setInactiveFrom(LocalDate inactiveFrom) {
    this.inactiveFrom = inactiveFrom;
  }

  public BigDecimal getChangedBy() {
    return changedBy;
  }

  public void setChangedBy(BigDecimal changedBy) {
    this.changedBy = changedBy;
  }

  public String getInactiveReason() {
    return inactiveReason;
  }

  public void setInactiveReason(String inactiveReason) {
    this.inactiveReason = inactiveReason;
  }

  public LocalDate getInactiveDate() {
    return inactiveDate;
  }

  public void setInactiveDate(LocalDate inactiveDate) {
    this.inactiveDate = inactiveDate;
  }

  public String getDeletionReason() {
    return deletionReason;
  }

  public void setDeletionReason(String deletionReason) {
    this.deletionReason = deletionReason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ManageRecordDTO manageRecordDTO = (ManageRecordDTO) o;

    if (!Objects.equals(id, manageRecordDTO.id)) {
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
    return "ManageRecordDTO{" +
        "id=" + id +
        ", tisId='" + tisId + "'" +
        ", recordType='" + recordType + "'" +
        ", role='" + role + "'" +
        ", recordStatus='" + recordStatus + "'" +
        ", inactiveFrom='" + inactiveFrom + "'" +
        ", changedBy='" + changedBy + "'" +
        ", inactiveReason='" + inactiveReason + "'" +
        ", inactiveDate='" + inactiveDate + "'" +
        ", deletionReason='" + deletionReason + "'" +
        '}';
  }
}

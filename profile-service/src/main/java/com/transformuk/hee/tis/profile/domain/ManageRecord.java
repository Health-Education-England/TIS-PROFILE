package com.transformuk.hee.tis.profile.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ManageRecord.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ManageRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "tisId", precision = 10, scale = 2, nullable = false)
	private BigDecimal tisId;

	private String recordType;

	private String role;

	private String recordStatus;

	private LocalDate inactiveFrom;

	@Column(name = "changedBy", precision = 10, scale = 2)
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

	public ManageRecord tisId(BigDecimal tisId) {
		this.tisId = tisId;
		return this;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public ManageRecord recordType(String recordType) {
		this.recordType = recordType;
		return this;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public ManageRecord role(String role) {
		this.role = role;
		return this;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public ManageRecord recordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
		return this;
	}

	public LocalDate getInactiveFrom() {
		return inactiveFrom;
	}

	public void setInactiveFrom(LocalDate inactiveFrom) {
		this.inactiveFrom = inactiveFrom;
	}

	public ManageRecord inactiveFrom(LocalDate inactiveFrom) {
		this.inactiveFrom = inactiveFrom;
		return this;
	}

	public BigDecimal getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(BigDecimal changedBy) {
		this.changedBy = changedBy;
	}

	public ManageRecord changedBy(BigDecimal changedBy) {
		this.changedBy = changedBy;
		return this;
	}

	public String getInactiveReason() {
		return inactiveReason;
	}

	public void setInactiveReason(String inactiveReason) {
		this.inactiveReason = inactiveReason;
	}

	public ManageRecord inactiveReason(String inactiveReason) {
		this.inactiveReason = inactiveReason;
		return this;
	}

	public LocalDate getInactiveDate() {
		return inactiveDate;
	}

	public void setInactiveDate(LocalDate inactiveDate) {
		this.inactiveDate = inactiveDate;
	}

	public ManageRecord inactiveDate(LocalDate inactiveDate) {
		this.inactiveDate = inactiveDate;
		return this;
	}

	public String getDeletionReason() {
		return deletionReason;
	}

	public void setDeletionReason(String deletionReason) {
		this.deletionReason = deletionReason;
	}

	public ManageRecord deletionReason(String deletionReason) {
		this.deletionReason = deletionReason;
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
		ManageRecord manageRecord = (ManageRecord) o;
		if (manageRecord.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, manageRecord.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "ManageRecord{" +
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

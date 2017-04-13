package com.transformuk.hee.tis.profile.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the Immigration entity.
 */
public class ImmigrationDTO implements Serializable {

	private Long id;

	@NotNull
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

	public Boolean getEeaResident() {
		return eeaResident;
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

	public String getSettled() {
		return settled;
	}

	public void setSettled(String settled) {
		this.settled = settled;
	}

	public LocalDate getVisaIssued() {
		return visaIssued;
	}

	public void setVisaIssued(LocalDate visaIssued) {
		this.visaIssued = visaIssued;
	}

	public LocalDate getVisaValidTo() {
		return visaValidTo;
	}

	public void setVisaValidTo(LocalDate visaValidTo) {
		this.visaValidTo = visaValidTo;
	}

	public String getVisaDetailsNumber() {
		return visaDetailsNumber;
	}

	public void setVisaDetailsNumber(String visaDetailsNumber) {
		this.visaDetailsNumber = visaDetailsNumber;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ImmigrationDTO immigrationDTO = (ImmigrationDTO) o;

		if (!Objects.equals(id, immigrationDTO.id)) {
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
		return "ImmigrationDTO{" +
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

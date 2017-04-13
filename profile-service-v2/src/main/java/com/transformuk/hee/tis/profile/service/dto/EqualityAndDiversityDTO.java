package com.transformuk.hee.tis.profile.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the EqualityAndDiversity entity.
 */
public class EqualityAndDiversityDTO implements Serializable {

	private Long id;

	@NotNull
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

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getDualNationality() {
		return dualNationality;
	}

	public void setDualNationality(String dualNationality) {
		this.dualNationality = dualNationality;
	}

	public String getSexualOrientation() {
		return sexualOrientation;
	}

	public void setSexualOrientation(String sexualOrientation) {
		this.sexualOrientation = sexualOrientation;
	}

	public String getReligiousBelief() {
		return religiousBelief;
	}

	public void setReligiousBelief(String religiousBelief) {
		this.religiousBelief = religiousBelief;
	}

	public String getEthnicOrigin() {
		return ethnicOrigin;
	}

	public void setEthnicOrigin(String ethnicOrigin) {
		this.ethnicOrigin = ethnicOrigin;
	}

	public Boolean getDisability() {
		return disability;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		EqualityAndDiversityDTO equalityAndDiversityDTO = (EqualityAndDiversityDTO) o;

		if (!Objects.equals(id, equalityAndDiversityDTO.id)) {
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
		return "EqualityAndDiversityDTO{" +
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

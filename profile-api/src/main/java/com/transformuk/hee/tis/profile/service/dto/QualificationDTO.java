package com.transformuk.hee.tis.profile.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the Qualification entity.
 */
public class QualificationDTO implements Serializable {

	private Long id;

	@NotNull
	private BigDecimal tisId;

	private String qualification;

	private String qualificationType;

	private LocalDate dateAttained;

	private String medicalSchool;

	private String country;

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

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getQualificationType() {
		return qualificationType;
	}

	public void setQualificationType(String qualificationType) {
		this.qualificationType = qualificationType;
	}

	public LocalDate getDateAttained() {
		return dateAttained;
	}

	public void setDateAttained(LocalDate dateAttained) {
		this.dateAttained = dateAttained;
	}

	public String getMedicalSchool() {
		return medicalSchool;
	}

	public void setMedicalSchool(String medicalSchool) {
		this.medicalSchool = medicalSchool;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		QualificationDTO qualificationDTO = (QualificationDTO) o;

		if (!Objects.equals(id, qualificationDTO.id)) {
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
		return "QualificationDTO{" +
				"id=" + id +
				", tisId='" + tisId + "'" +
				", qualification='" + qualification + "'" +
				", qualificationType='" + qualificationType + "'" +
				", dateAttained='" + dateAttained + "'" +
				", medicalSchool='" + medicalSchool + "'" +
				", country='" + country + "'" +
				'}';
	}
}

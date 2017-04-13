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
 * A Qualification.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Qualification implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "tisId", precision = 10, scale = 2, nullable = false)
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

	public Qualification tisId(BigDecimal tisId) {
		this.tisId = tisId;
		return this;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public Qualification qualification(String qualification) {
		this.qualification = qualification;
		return this;
	}

	public String getQualificationType() {
		return qualificationType;
	}

	public void setQualificationType(String qualificationType) {
		this.qualificationType = qualificationType;
	}

	public Qualification qualificationType(String qualificationType) {
		this.qualificationType = qualificationType;
		return this;
	}

	public LocalDate getDateAttained() {
		return dateAttained;
	}

	public void setDateAttained(LocalDate dateAttained) {
		this.dateAttained = dateAttained;
	}

	public Qualification dateAttained(LocalDate dateAttained) {
		this.dateAttained = dateAttained;
		return this;
	}

	public String getMedicalSchool() {
		return medicalSchool;
	}

	public void setMedicalSchool(String medicalSchool) {
		this.medicalSchool = medicalSchool;
	}

	public Qualification medicalSchool(String medicalSchool) {
		this.medicalSchool = medicalSchool;
		return this;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Qualification country(String country) {
		this.country = country;
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
		Qualification qualification = (Qualification) o;
		if (qualification.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, qualification.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Qualification{" +
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

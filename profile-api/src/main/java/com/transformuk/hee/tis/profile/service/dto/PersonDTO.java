package com.transformuk.hee.tis.profile.service.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the Person entity.
 */
public class PersonDTO implements Serializable {

	private Long id;

	@NotNull
	private BigDecimal tisId;

	private String publicHealthId;

	private Boolean active;

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

	public String getPublicHealthId() {
		return publicHealthId;
	}

	public void setPublicHealthId(String publicHealthId) {
		this.publicHealthId = publicHealthId;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		PersonDTO personDTO = (PersonDTO) o;

		if (!Objects.equals(id, personDTO.id)) {
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
		return "PersonDTO{" +
				"id=" + id +
				", tisId='" + tisId + "'" +
				", publicHealthId='" + publicHealthId + "'" +
				", active='" + active + "'" +
				'}';
	}
}

package com.transformuk.hee.tis.profile.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the GmcDetails entity.
 */
public class GmcDetailsDTO implements Serializable {

	private Long id;

	@NotNull
	private BigDecimal tisId;

	@Size(max = 7)
	private String gmcNumber;

	private String gmcStatus;

	private LocalDate gmcStartDate;

	private LocalDate gmcExpiryDate;

	private String designatedBodyCode;

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

	public String getGmcNumber() {
		return gmcNumber;
	}

	public void setGmcNumber(String gmcNumber) {
		this.gmcNumber = gmcNumber;
	}

	public String getGmcStatus() {
		return gmcStatus;
	}

	public void setGmcStatus(String gmcStatus) {
		this.gmcStatus = gmcStatus;
	}

	public LocalDate getGmcStartDate() {
		return gmcStartDate;
	}

	public void setGmcStartDate(LocalDate gmcStartDate) {
		this.gmcStartDate = gmcStartDate;
	}

	public LocalDate getGmcExpiryDate() {
		return gmcExpiryDate;
	}

	public void setGmcExpiryDate(LocalDate gmcExpiryDate) {
		this.gmcExpiryDate = gmcExpiryDate;
	}

	public String getDesignatedBodyCode() {
		return designatedBodyCode;
	}

	public void setDesignatedBodyCode(String designatedBodyCode) {
		this.designatedBodyCode = designatedBodyCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		GmcDetailsDTO gmcDetailsDTO = (GmcDetailsDTO) o;

		if (!Objects.equals(id, gmcDetailsDTO.id)) {
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
		return "GmcDetailsDTO{" +
				"id=" + id +
				", tisId='" + tisId + "'" +
				", gmcNumber='" + gmcNumber + "'" +
				", gmcStatus='" + gmcStatus + "'" +
				", gmcStartDate='" + gmcStartDate + "'" +
				", gmcExpiryDate='" + gmcExpiryDate + "'" +
				", designatedBodyCode='" + designatedBodyCode + "'" +
				'}';
	}
}

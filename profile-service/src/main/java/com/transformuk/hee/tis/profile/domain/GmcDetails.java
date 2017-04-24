package com.transformuk.hee.tis.profile.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A GmcDetails.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GmcDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "tisId", precision = 10, scale = 2, nullable = false)
	private BigDecimal tisId;

	@Size(max = 7)
	@Column(name = "gmcNumber", length = 7)
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

	public GmcDetails tisId(BigDecimal tisId) {
		this.tisId = tisId;
		return this;
	}

	public String getGmcNumber() {
		return gmcNumber;
	}

	public void setGmcNumber(String gmcNumber) {
		this.gmcNumber = gmcNumber;
	}

	public GmcDetails gmcNumber(String gmcNumber) {
		this.gmcNumber = gmcNumber;
		return this;
	}

	public String getGmcStatus() {
		return gmcStatus;
	}

	public void setGmcStatus(String gmcStatus) {
		this.gmcStatus = gmcStatus;
	}

	public GmcDetails gmcStatus(String gmcStatus) {
		this.gmcStatus = gmcStatus;
		return this;
	}

	public LocalDate getGmcStartDate() {
		return gmcStartDate;
	}

	public void setGmcStartDate(LocalDate gmcStartDate) {
		this.gmcStartDate = gmcStartDate;
	}

	public GmcDetails gmcStartDate(LocalDate gmcStartDate) {
		this.gmcStartDate = gmcStartDate;
		return this;
	}

	public LocalDate getGmcExpiryDate() {
		return gmcExpiryDate;
	}

	public void setGmcExpiryDate(LocalDate gmcExpiryDate) {
		this.gmcExpiryDate = gmcExpiryDate;
	}

	public GmcDetails gmcExpiryDate(LocalDate gmcExpiryDate) {
		this.gmcExpiryDate = gmcExpiryDate;
		return this;
	}

	public String getDesignatedBodyCode() {
		return designatedBodyCode;
	}

	public void setDesignatedBodyCode(String designatedBodyCode) {
		this.designatedBodyCode = designatedBodyCode;
	}

	public GmcDetails designatedBodyCode(String designatedBodyCode) {
		this.designatedBodyCode = designatedBodyCode;
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
		GmcDetails gmcDetails = (GmcDetails) o;
		if (gmcDetails.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, gmcDetails.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "GmcDetails{" +
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

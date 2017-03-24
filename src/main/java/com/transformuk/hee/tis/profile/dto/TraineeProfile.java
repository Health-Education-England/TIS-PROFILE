package com.transformuk.hee.tis.profile.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;

@ApiModel(description = "TraineeProfile entity")
public class TraineeProfile {
	private Long tisId;
	private String gmcNumber;
	private boolean active;
	private String designatedBodyCode;
	private LocalDate dateAdded;

	@ApiModelProperty(required = true, value = "Trainee's GmcNumber")
	public Long getTisId() {
		return tisId;
	}

	public void setTisId(Long tisId) {
		this.tisId = tisId;
	}

	@ApiModelProperty(required = true, value = "Trainee identifier")
	public String getGmcNumber() {
		return gmcNumber;
	}

	public void setGmcNumber(String gmcNumber) {
		this.gmcNumber = gmcNumber;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getDesignatedBodyCode() {
		return designatedBodyCode;
	}

	public void setDesignatedBodyCode(String designatedBodyCode) {
		this.designatedBodyCode = designatedBodyCode;
	}

	public LocalDate getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(LocalDate dateAdded) {
		this.dateAdded = dateAdded;
	}
}

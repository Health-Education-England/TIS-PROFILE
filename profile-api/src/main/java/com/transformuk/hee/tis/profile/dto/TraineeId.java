package com.transformuk.hee.tis.profile.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(description = "Trainee identity data")
public class TraineeId {
	private Long tisId;
	private String gmcNumber;

	public TraineeId() {
	}

	public TraineeId(Long tisId, String gmcNumber) {
		this.gmcNumber = gmcNumber;
		this.tisId = tisId;
	}

	@ApiModelProperty(value = "Trainee's gmc number")
	public String getGmcNumber() {
		return gmcNumber;
	}

	public void setGmcNumber(String gmcNumber) {
		this.gmcNumber = gmcNumber;
	}

	@ApiModelProperty(value = "Trainee's TIS ID")
	public Long getTisId() {
		return tisId;
	}

	public void setTisId(Long tisId) {
		this.tisId = tisId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TraineeId traineeId = (TraineeId) o;
		return Objects.equals(tisId, traineeId.tisId) &&
				Objects.equals(gmcNumber, traineeId.gmcNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tisId, gmcNumber);
	}

	@Override
	public String toString() {
		return "TraineeId{" +
				"gmcNumber='" + gmcNumber + '\'' +
				", tisId=" + tisId +
				'}';
	}
}

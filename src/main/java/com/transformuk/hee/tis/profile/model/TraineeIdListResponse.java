package com.transformuk.hee.tis.profile.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Wrapper class to hold TraineeId list.
 */
public class TraineeIdListResponse {

	private List<TraineeProfile> traineeIds;

	public TraineeIdListResponse() {
	}

	public TraineeIdListResponse(List<TraineeProfile> traineeIds) {
		this.traineeIds = traineeIds;
	}

	@ApiModelProperty(required = true, value = "traineeIdList")
	public List<TraineeProfile> getTraineeIds() {
		return traineeIds;
	}
}

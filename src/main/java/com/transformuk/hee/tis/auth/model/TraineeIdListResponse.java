package com.transformuk.hee.tis.auth.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Wrapper class to hold TraineeId list.
 */
public class TraineeIdListResponse {
    
    private List<TraineeId> traineeIds;

    public TraineeIdListResponse() {    }

    public TraineeIdListResponse(List<TraineeId> traineeIds) {
        this.traineeIds = traineeIds;
    }

    @ApiModelProperty(required = true, value = "traineeIdList")
    public List<TraineeId> getTraineeIds() {
        return traineeIds;
    }
}

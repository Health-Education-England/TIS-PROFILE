package com.transformuk.hee.tis.profile.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "Wrapper class to hold TraineeId list")
public class TraineeIdListResponse {

  private List<TraineeProfileDto> traineeIds;

  public TraineeIdListResponse() {
  }

  public TraineeIdListResponse(List<TraineeProfileDto> traineeIds) {
    this.traineeIds = traineeIds;
  }

  @ApiModelProperty(required = true, value = "traineeIdList")
  public List<TraineeProfileDto> getTraineeIds() {
    return traineeIds;
  }
}

package com.transformuk.hee.tis.profile.dto;

import java.util.List;

public class TraineeIdListResponse {

  private List<TraineeProfileDto> traineeIds;

  public TraineeIdListResponse() {
  }

  public TraineeIdListResponse(List<TraineeProfileDto> traineeIds) {
    this.traineeIds = traineeIds;
  }

  public List<TraineeProfileDto> getTraineeIds() {
    return traineeIds;
  }
}

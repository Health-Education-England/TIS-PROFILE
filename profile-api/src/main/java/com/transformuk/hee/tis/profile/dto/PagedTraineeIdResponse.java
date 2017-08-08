package com.transformuk.hee.tis.profile.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Wrapper class to hold paged list.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagedTraineeIdResponse {

  private long totalElements;
  private List<TraineeId> content;

  public PagedTraineeIdResponse() {
  }

  public PagedTraineeIdResponse(List<TraineeId> content, long totalElements) {
    this.content = content;
    this.totalElements = totalElements;
  }

  public List<TraineeId> getContent() {
    return content;
  }

  public void setContent(List<TraineeId> content) {
    this.content = content;
  }

  public long getTotalElements() {
    return totalElements;
  }

  public void setTotalElements(long totalElements) {
    this.totalElements = totalElements;
  }
}

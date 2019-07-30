package com.transformuk.hee.tis.profile.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "Wrapper class to hold paged list.")
public class PagedResponse<T> {

  private long totalElements;
  private long totalPages;
  private List<T> content;

  public PagedResponse(List<T> content, long totalElements, long totalPages) {
    this.content = content;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
  }

  @ApiModelProperty(required = true, value = "The actual content of the response")
  public List<T> getContent() {
    return content;
  }

  @ApiModelProperty(required = true, value = "The total amount of elements in this response")
  public long getTotalElements() {
    return totalElements;
  }

  @ApiModelProperty(required = true, value = "The total amount of pages")
  public long getTotalPages() {
    return totalPages;
  }
}

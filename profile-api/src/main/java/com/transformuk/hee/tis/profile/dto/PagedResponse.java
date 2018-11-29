package com.transformuk.hee.tis.profile.dto;

import java.util.List;

public class PagedResponse<T> {

  private long totalElements;
  private long totalPages;
  private List<T> content;

  public PagedResponse(List<T> content, long totalElements, long totalPages) {
    this.content = content;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
  }

  public List<T> getContent() {
    return content;
  }

  public long getTotalElements() {
    return totalElements;
  }

  public long getTotalPages() {
    return totalPages;
  }
}

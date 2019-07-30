package com.transformuk.hee.tis.profile.client.service.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class CustomPageable<T> extends PageImpl<T> {

  public CustomPageable() {
    this(new ArrayList<>());
  }

  public CustomPageable(List<T> content) {
    super(content);
  }

  public CustomPageable(List<T> content, Pageable pageable, long total) {
    super(content, pageable, total);
  }

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public CustomPageable(@JsonProperty("content") List<T> content,
      @JsonProperty("number") int page,
      @JsonProperty("size") int size,
      @JsonProperty("totalElements") long total) {
    super(content, new PageRequest(page, size), total);
  }

  public String getNextPageRequestParameters() {
    String nextPage = StringUtils.EMPTY;
    if (this.hasNext()) {
      nextPage = "page=" + this.nextPageable().getPageNumber() + "&size=" + this.nextPageable()
          .getPageSize();
    }
    return nextPage;
  }

  public String getPreviousPageRequestParameters() {
    String previousPage = StringUtils.EMPTY;
    if (this.hasNext()) {
      previousPage =
          "page=" + this.previousPageable().getPageNumber() + "&size=" + this.nextPageable()
              .getPageSize();
    }
    return previousPage;
  }
}

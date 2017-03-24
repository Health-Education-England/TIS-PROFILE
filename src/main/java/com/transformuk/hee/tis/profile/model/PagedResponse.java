package com.transformuk.hee.tis.profile.model;

import java.util.List;

/**
 * Wrapper class to hold paged list.
 */
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

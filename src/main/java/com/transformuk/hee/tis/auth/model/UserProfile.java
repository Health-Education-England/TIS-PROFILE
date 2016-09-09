package com.transformuk.hee.tis.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfile {

	private String username;
	private List<String> cn = new ArrayList<>();
	private List<String> isMemberOf = new ArrayList<>();

	public List<String> getCn() {
		return cn;
	}

	public void setCn(List<String> cn) {
		this.cn = cn;
	}

	public List<String> getIsMemberOf() {
		return isMemberOf;
	}

	public void setIsMemberOf(List<String> isMemberOf) {
		this.isMemberOf = isMemberOf;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}

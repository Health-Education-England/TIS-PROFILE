package com.transformuk.hee.tis.profile.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoResponse {
	private String name;
	private String firstName;
	private String lastName;
	private String fullName;
	private String gmcId;
	private Set<String> designatedBodyCodes;
	;
	private String phoneNumber;
	private String emailAddress;

	@ApiModelProperty(required = true, value = "UserDto identifier")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(required = true, value = "UserDto's first name")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@ApiModelProperty(required = true, value = "UserDto's last name")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@ApiModelProperty(required = true, value = "7 digit GMC Reference number of the user(Trainee doctor)")
	public String getGmcId() {
		return gmcId;
	}

	public void setGmcId(String gmcId) {
		this.gmcId = gmcId;
	}

	@ApiModelProperty(required = true, value = "Designated Body codes of user's organisation")
	public Set<String> getDesignatedBodyCodes() {
		return designatedBodyCodes;
	}

	public void setDesignatedBodyCodes(Set<String> designatedBodyCodes) {
		this.designatedBodyCodes = designatedBodyCodes;
	}

	@ApiModelProperty(value = "UserDto's phone number")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@ApiModelProperty(value = "UserDto's email address")
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@ApiModelProperty(value = "The user's full name")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return "UserDto{" +
				"name='" + name + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", gmcId='" + gmcId + '\'' +
				", designatedBodyCodes='" + designatedBodyCodes + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", emailAddress='" + emailAddress + '\'' +
				'}';
	}
}

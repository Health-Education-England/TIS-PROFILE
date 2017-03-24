package com.transformuk.hee.tis.profile.dto;

import com.transformuk.hee.tis.profile.model.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Set;

@ApiModel(description = "UserDto object that contains the logged in user's details")
public class UserDto {

	private String name;
	private String firstName;
	private String lastName;
	private String gmcId;
	private String phoneNumber;
	private String emailAddress;
	private boolean active;
	private Set<Role> roles;
	private Set<String> designatedBodyCodes;

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

	@ApiModelProperty(value = "UserDto's active flag")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@ApiModelProperty(required = true, value = "Roles assigned to a user")
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@ApiModelProperty(required = true, value = "Designated Body codes of user's organisation, if it is not available " +
			"then set default to 'None'")
	public Set<String> getDesignatedBodyCodes() {
		return designatedBodyCodes;
	}

	public void setDesignatedBodyCodes(Set<String> designatedBodyCodes) {
		this.designatedBodyCodes = designatedBodyCodes;
	}
}

package com.transformuk.hee.tis.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoResponse {
    private String name;
    private String firstName;
    private String lastName;
    private String fullName;
    private String gmcId;
    private String designatedBodyCode;
    private String phoneNumber;
    private String emailAddress;

	@ApiModelProperty(required = true, value = "User identifier")
    public String getName() {
        return name;
    }

    @ApiModelProperty(required = true, value = "User's first name")
    public String getFirstName() {
        return firstName;
    }

    @ApiModelProperty(required = true, value = "User's last name")
    public String getLastName() {
        return lastName;
    }

    @ApiModelProperty(required = true, value = "7 digit GMC Reference number of the user(Trainee doctor)")
    public String getGmcId() {
        return gmcId;
    }

    @ApiModelProperty(required = true, value = "Designated Body code of user's organisation")
    public String getDesignatedBodyCode() {
        return designatedBodyCode;
    }

    @ApiModelProperty(value = "User's phone number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGmcId(String gmcId) {
        this.gmcId = gmcId;
    }

    public void setDesignatedBodyCode(String designatedBodyCode) {
        this.designatedBodyCode = designatedBodyCode;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @ApiModelProperty(value = "User's email address")
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
		return "User{" +
				"name='" + name + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", gmcId='" + gmcId + '\'' +
				", designatedBodyCode='" + designatedBodyCode + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", emailAddress='" + emailAddress + '\'' +
				'}';
	}
}

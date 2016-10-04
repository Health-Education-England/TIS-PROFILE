package com.transformuk.hee.tis.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@ApiModel(description = "User object that contains the logged in user's details")
public class User {
    private String userName;
    private String firstName;
    private String lastName;
    private String gmcId;
    private String designatedBodyCode;
    private String phoneNumber;

    public User(){
        super();
    }

    public User(String userName) {
        this.userName = userName;
    }

    @ApiModelProperty(required = true, value = "User identifier")
    @JsonProperty("user_name")
    @Id
    public String getUserName() {
        return userName;
    }

    @ApiModelProperty(required = true, value = "User's first name")
    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }

    @ApiModelProperty(required = true, value = "User's last name")
    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }

    @ApiModelProperty(required = true, value = "7 digit GMC Reference number of the user(Trainee doctor)")
    @JsonProperty("gmc_id")
    public String getGmcId() {
        return gmcId;
    }

    @ApiModelProperty(required = true, value = "Designated Body code of user's organisation")
    @JsonProperty("designated_body_code")
    public String getDesignatedBodyCode() {
        return designatedBodyCode;
    }

    @ApiModelProperty(value = "User's phone number")
    @JsonProperty("phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gmcId='" + gmcId + '\'' +
                ", designatedBodyCode='" + designatedBodyCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}

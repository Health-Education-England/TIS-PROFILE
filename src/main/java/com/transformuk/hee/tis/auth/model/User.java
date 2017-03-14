package com.transformuk.hee.tis.auth.model;

import com.google.common.collect.Sets;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "HeeUser")
@ApiModel(description = "User object that contains the logged in user's details")
public class User {

	public static final String NONE="None";

    private String name;
    private String firstName;
    private String lastName;
    private String gmcId;
    private String phoneNumber;
    private String emailAddress;
    private boolean active;
    private Set<Role> roles;
    private Set<String> designatedBodyCodes;

    public User(){
        super();
    }

    public User(String name) {
        this.name = name;
    }

	@ApiModelProperty(required = true, value = "Roles assigned to a user")
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "UserRole", joinColumns = @JoinColumn(name = "userName", referencedColumnName = "name"),
			inverseJoinColumns = @JoinColumn(name = "roleName", referencedColumnName = "name"))
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@ApiModelProperty(required = true, value = "User identifier")
    @Id
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

    @ApiModelProperty(value = "User's active flag")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @ElementCollection(fetch = EAGER)
    @CollectionTable(name="UserDesignatedBody", joinColumns=@JoinColumn(name="userName"))
    @Column(name="designatedBodyCode")
    @ApiModelProperty(required = true, value = "Designated Body codes of user's organisation, if it is not available " +
			"then set default to 'None'")
    public Set<String> getDesignatedBodyCodes() {
        if(CollectionUtils.isEmpty(this.designatedBodyCodes)){
        	this.designatedBodyCodes = Sets.newHashSet(User.NONE);
		}
    	return designatedBodyCodes;
    }

    public void setDesignatedBodyCodes(Set<String> designatedBodyCodes) {
        this.designatedBodyCodes = designatedBodyCodes;
    }

    @Override
	public String toString() {
		return "User{" +
				"name='" + name + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", gmcId='" + gmcId + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", emailAddress='" + emailAddress + '\'' +
				", active='" + active + '\'' +
				", roles=" + roles +
				'}';
	}
}

package com.transformuk.hee.tis.profile.model;

import com.google.common.collect.Sets;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "HeeUser")
public class User {

	private static final String NONE = "None";

	private String name;
	private String firstName;
	private String lastName;
	private String gmcId;
	private String phoneNumber;
	private String emailAddress;
	private boolean active;
	private Set<Role> roles;
	private Set<String> designatedBodyCodes;

	public User() {
		super();
	}

	public User(String name) {
		this.name = name;
	}

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "UserRole", joinColumns = @JoinColumn(name = "userName", referencedColumnName = "name"),
			inverseJoinColumns = @JoinColumn(name = "roleName", referencedColumnName = "name"))
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Id
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGmcId() {
		return gmcId;
	}

	public void setGmcId(String gmcId) {
		this.gmcId = gmcId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@ElementCollection(fetch = EAGER)
	@CollectionTable(name = "UserDesignatedBody", joinColumns = @JoinColumn(name = "userName"))
	@Column(name = "designatedBodyCode")
	public Set<String> getDesignatedBodyCodes() {
		if (CollectionUtils.isEmpty(this.designatedBodyCodes)) {
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

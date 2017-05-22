package com.transformuk.hee.tis.profile.service.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the HeeUser entity.
 */
public class HeeUserDTO implements Serializable {

	@NotNull
	private String name;

	private String firstName;

	private String lastName;

	private String gmcId;

	private String phoneNumber;

	private String emailAddress;

	private Boolean active;

	private String passowrd;

	private Set<RoleDTO> roles;
	private Set<String> designatedBodyCodes;

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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getPassowrd() {
		return passowrd;
	}

	public void setPassowrd(String passowrd) {
		this.passowrd = passowrd;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleDTO> roles) {
		this.roles = roles;
	}

	public Set<String> getDesignatedBodyCodes() {
		return designatedBodyCodes;
	}

	public void setDesignatedBodyCodes(Set<String> designatedBodyCodes) {
		this.designatedBodyCodes = designatedBodyCodes;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		HeeUserDTO heeUserDTO = (HeeUserDTO) o;

		if (!Objects.equals(name, heeUserDTO.name)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name);
	}

	@Override
	public String toString() {
		return "HeeUserDTO{" +
				"name='" + name + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", gmcId='" + gmcId + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", emailAddress='" + emailAddress + '\'' +
				", active=" + active +
				", roles=" + roles +
				", designatedBodyCodes=" + designatedBodyCodes +
				'}';
	}
}

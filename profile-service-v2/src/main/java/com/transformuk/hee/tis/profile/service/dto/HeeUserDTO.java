package com.transformuk.hee.tis.profile.service.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the HeeUser entity.
 */
public class HeeUserDTO implements Serializable {

	private Long id;

	@NotNull
	private String name;

	private String firstName;

	private String lastName;

	private String gmcId;

	private String phoneNumber;

	private String emailAddress;

	private Boolean active;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		HeeUserDTO heeUserDTO = (HeeUserDTO) o;

		if (!Objects.equals(id, heeUserDTO.id)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "HeeUserDTO{" +
				"id=" + id +
				", name='" + name + "'" +
				", firstName='" + firstName + "'" +
				", lastName='" + lastName + "'" +
				", gmcId='" + gmcId + "'" +
				", phoneNumber='" + phoneNumber + "'" +
				", emailAddress='" + emailAddress + "'" +
				", active='" + active + "'" +
				'}';
	}
}

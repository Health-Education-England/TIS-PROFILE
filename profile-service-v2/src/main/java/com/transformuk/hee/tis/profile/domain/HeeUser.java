package com.transformuk.hee.tis.profile.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A HeeUser.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HeeUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name = "name", nullable = false)
	private String name;

	private String firstName;

	private String lastName;

	private String gmcId;

	private String phoneNumber;

	private String emailAddress;

	private Boolean active;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HeeUser name(String name) {
		this.name = name;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public HeeUser firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public HeeUser lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public String getGmcId() {
		return gmcId;
	}

	public void setGmcId(String gmcId) {
		this.gmcId = gmcId;
	}

	public HeeUser gmcId(String gmcId) {
		this.gmcId = gmcId;
		return this;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public HeeUser phoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public HeeUser emailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
		return this;
	}

	public Boolean isActive() {
		return active;
	}

	public HeeUser active(Boolean active) {
		this.active = active;
		return this;
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
		HeeUser heeUser = (HeeUser) o;
		if (heeUser.name == null || name == null) {
			return false;
		}
		return Objects.equals(name, heeUser.name);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name);
	}

	@Override
	public String toString() {
		return "HeeUser{" +
				"name='" + name + "'" +
				", firstName='" + firstName + "'" +
				", lastName='" + lastName + "'" +
				", gmcId='" + gmcId + "'" +
				", phoneNumber='" + phoneNumber + "'" +
				", emailAddress='" + emailAddress + "'" +
				", active='" + active + "'" +
				'}';
	}
}

package com.transformuk.hee.tis.profile.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Permission")
public class Permission {
	private String name;

	public Permission(String name) {
		this.name = name;
	}

	public Permission() {
		super();
	}

	@Id
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Permission{" +
				"name='" + name + '\'' +
				'}';
	}
}
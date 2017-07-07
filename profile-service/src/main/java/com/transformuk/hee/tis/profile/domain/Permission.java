package com.transformuk.hee.tis.profile.domain;

import com.transformuk.hee.tis.profile.dto.PermissionType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Permission.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Permission implements Serializable {

	private static final long serialVersionUID = 1L;

	public Permission(String name) {
		this.name = name;
	}

	public Permission() {
		super();
	}

	@Id
	@NotNull
	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private PermissionType type;

	@Column(name = "description")
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PermissionType getType() {
		return type;
	}

	public void setType(PermissionType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Permission name(String name) {
		this.name = name;
		return this;
	}

	public Permission type(PermissionType type) {
		this.type = type;
		return this;
	}

	public Permission description(String description) {
		this.description = description;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Permission that = (Permission) o;
		if (that.name == null || name == null) {
			return false;
		}
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (type != that.type) return false;
		return description != null ? description.equals(that.description) : that.description == null;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Permission{" +
				"name='" + name + '\'' +
				", type=" + type +
				", description='" + description + '\'' +
				'}';
	}
}

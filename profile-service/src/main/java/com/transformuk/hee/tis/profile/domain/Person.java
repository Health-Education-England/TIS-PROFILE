package com.transformuk.hee.tis.profile.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Person.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Person implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "tisId", precision = 10, scale = 2, nullable = false)
  private BigDecimal tisId;

  private String publicHealthId;

  private Boolean active;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getTisId() {
    return tisId;
  }

  public void setTisId(BigDecimal tisId) {
    this.tisId = tisId;
  }

  public Person tisId(BigDecimal tisId) {
    this.tisId = tisId;
    return this;
  }

  public String getPublicHealthId() {
    return publicHealthId;
  }

  public void setPublicHealthId(String publicHealthId) {
    this.publicHealthId = publicHealthId;
  }

  public Person publicHealthId(String publicHealthId) {
    this.publicHealthId = publicHealthId;
    return this;
  }

  public Boolean isActive() {
    return active;
  }

  public Person active(Boolean active) {
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
    Person person = (Person) o;
    if (person.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, person.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Person{" +
        "id=" + id +
        ", tisId='" + tisId + "'" +
        ", publicHealthId='" + publicHealthId + "'" +
        ", active='" + active + "'" +
        '}';
  }
}

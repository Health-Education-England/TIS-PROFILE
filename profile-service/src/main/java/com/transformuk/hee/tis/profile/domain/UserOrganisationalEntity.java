package com.transformuk.hee.tis.profile.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "UserOrganisationalEntity")
public class UserOrganisationalEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "heeUser", nullable = false)
  private HeeUser heeUser;

  private Long organisationalEntityId;

  private String organisationalEntityName;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public HeeUser getHeeUser() {
    return heeUser;
  }

  public void setHeeUser(HeeUser heeUser) {
    this.heeUser = heeUser;
  }

  public Long getOrganisationalEntityId() {
    return organisationalEntityId;
  }

  public void setOrganisationalEntityId(Long organisationalEntityId) {
    this.organisationalEntityId = organisationalEntityId;
  }

  public String getOrganisationalEntityName() {
    return organisationalEntityName;
  }

  public void setOrganisationalEntityName(String organisationalEntityName) {
    this.organisationalEntityName = organisationalEntityName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserOrganisationalEntity userOrganisationalEntity = (UserOrganisationalEntity) o;
    return Objects.equals(id, userOrganisationalEntity.id) &&
        Objects.equals(heeUser, userOrganisationalEntity.heeUser) &&
        Objects.equals(organisationalEntityId, userOrganisationalEntity.organisationalEntityId) &&
        Objects.equals(organisationalEntityName, userOrganisationalEntity.organisationalEntityName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, heeUser, organisationalEntityId);
  }

  @Override
  public String toString() {
    return "UserOrganisationalEntity{" +
        "id=" + id +
        ", heeUser=" + heeUser +
        ", organisationalEntityId=" + organisationalEntityId +
        ", organisationalEntityName=" + organisationalEntityName +
        '}';
  }
}

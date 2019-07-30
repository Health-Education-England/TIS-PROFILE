package com.transformuk.hee.tis.profile.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "UserTrust")
public class UserTrust implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "heeUser", nullable = false)
  private HeeUser heeUser;

  private Long trustId;
  private String trustName;
  private String trustCode;

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

  public Long getTrustId() {
    return trustId;
  }

  public void setTrustId(Long trustId) {
    this.trustId = trustId;
  }

  public String getTrustName() {
    return trustName;
  }

  public void setTrustName(String trustName) {
    this.trustName = trustName;
  }

  public String getTrustCode() {
    return trustCode;
  }

  public void setTrustCode(String trustCode) {
    this.trustCode = trustCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserTrust userTrust = (UserTrust) o;
    return Objects.equals(id, userTrust.id) &&
        Objects.equals(heeUser, userTrust.heeUser) &&
        Objects.equals(trustId, userTrust.trustId) &&
        Objects.equals(trustName, userTrust.trustName) &&
        Objects.equals(trustCode, userTrust.trustCode);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id, heeUser, trustId, trustName, trustCode);
  }

  @Override
  public String toString() {
    return "UserTrust{" +
        "id=" + id +
        ", heeUser=" + heeUser +
        ", trustId=" + trustId +
        ", trustName='" + trustName + '\'' +
        ", trustCode='" + trustCode + '\'' +
        '}';
  }
}

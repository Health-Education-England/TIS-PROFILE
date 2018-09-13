package com.transformuk.hee.tis.profile.domain;

import com.google.common.collect.Sets;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

/**
 * A HeeUser.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HeeUser implements Serializable {

  public static final String NONE = "None";
  private static final UserTrust NULL_TRUST = null;
  private static final long serialVersionUID = 1L;
  private String name;
  private String firstName;
  private String lastName;
  private String gmcId;
  private String phoneNumber;
  private String emailAddress;
  private Boolean active;

  private String password;

  private Boolean isTemporaryPassword;

  private Set<Role> roles;
  private Set<String> designatedBodyCodes;

  private Set<UserTrust> associatedTrusts = new HashSet<>();

  public HeeUser() {
    super();
  }

  public HeeUser(String name) {
    this.name = name;
  }


  @Id
  @NotNull
  @Column(name = "name", nullable = false)
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

  @Transient
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Transient
  public Boolean getTemporaryPassword() {
    return isTemporaryPassword;
  }

  public void setTemporaryPassword(Boolean temporaryPassword) {
    isTemporaryPassword = temporaryPassword;
  }

  @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
  @JoinTable(name = "UserRole", joinColumns = @JoinColumn(name = "userName", referencedColumnName = "name"),
      inverseJoinColumns = @JoinColumn(name = "roleName", referencedColumnName = "name"))
  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  @ElementCollection(fetch = EAGER)
  @CollectionTable(name = "UserDesignatedBody", joinColumns = @JoinColumn(name = "userName"))
  @Column(name = "designatedBodyCode")
  public Set<String> getDesignatedBodyCodes() {
    if (CollectionUtils.isEmpty(this.designatedBodyCodes)) {
      this.designatedBodyCodes = Sets.newHashSet(HeeUser.NONE);
    }
    return designatedBodyCodes;
  }

  public void setDesignatedBodyCodes(Set<String> designatedBodyCodes) {
    this.designatedBodyCodes = designatedBodyCodes;
  }

  @OneToMany(fetch = LAZY, mappedBy = "heeUser", cascade = CascadeType.ALL)
  public Set<UserTrust> getAssociatedTrusts() {
//    if (CollectionUtils.isEmpty(this.associatedTrusts)) {
//      this.associatedTrusts = Sets.newHashSet(HeeUser.NULL_TRUST);
//    }
    return associatedTrusts;
  }

  public void setAssociatedTrusts(Set<UserTrust> associatedTrusts) {
    this.associatedTrusts = associatedTrusts;
  }

  public void addAssociatedTrust(UserTrust userTrust) {
    this.associatedTrusts.add(userTrust);
    userTrust.setHeeUser(this);
  }

  public void removeAssociatedTrust(UserTrust userTrust) {
    userTrust.setHeeUser(null);
    this.associatedTrusts.remove(userTrust);
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
        ", roles=" + roles +
        ", designatedBodyCodes=" + designatedBodyCodes +
        '}';
  }
}

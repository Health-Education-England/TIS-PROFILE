package com.transformuk.hee.tis.profile.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "UserProgramme")
public class UserProgramme implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "heeUser", nullable = false)
  private HeeUser heeUser;

  private Long programmenId;
  private String programmenName;
  private String programmeNumber;

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

  public Long getprogrammenId() {
    return programmenId;
  }

  public void setprogrammenId(Long programmenId) {
    this.programmenId = programmenId;
  }

  public String getprogrammenName() {
    return programmenName;
  }

  public void setprogrammenName(String programmenName) {
    this.programmenName = programmenName;
  }

  public String getprogrammeNumber() {
    return programmeNumber;
  }

  public void setprogrammeNumber(String programmeNumber) {
    this.programmeNumber = programmeNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserProgramme userProgramme = (UserProgramme) o;
    return Objects.equals(id, userProgramme.id) &&
        Objects.equals(heeUser, userProgramme.heeUser) &&
        Objects.equals(programmenId, userProgramme.programmenId) &&
        Objects.equals(programmenName, userProgramme.programmenName) &&
        Objects.equals(programmeNumber, userProgramme.programmeNumber);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id, heeUser, programmenId, programmenName, programmeNumber);
  }

  @Override
  public String toString() {
    return "UserProgramme{" +
        "id=" + id +
        ", heeUser=" + heeUser +
        ", programmenId=" + programmenId +
        ", programmenName='" + programmenName + '\'' +
        ", programmeNumber='" + programmeNumber + '\'' +
        '}';
  }
}

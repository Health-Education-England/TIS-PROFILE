package com.transformuk.hee.tis.auth.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;


@Entity(name = "TraineeProfile")
@ApiModel(description = "TraineeProfile entity")
public class TraineeProfile {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long tisId;
    private String gmcNumber;
    private boolean active;
    private String designatedBodyCodes;
    private LocalDate dateAdded;

    public TraineeProfile() { }

    public TraineeProfile(Long tisId, String gmcNumber) {
        this.gmcNumber = gmcNumber;
        this.tisId = tisId;
    }

    @ApiModelProperty(required = true, value = "Trainee's GmcNumber")
    public String getGmcNumber() {
        return gmcNumber;
    }

    public void setGmcNumber(String gmcNumber) {
        this.gmcNumber = gmcNumber;
    }

    @ApiModelProperty(required = true, value = "Trainee identifier")
    public Long getTisId() {
        return tisId;
    }

    public void setTisId(Long tisId) {
        this.tisId = tisId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getDesignatedBodyCodes() {
        return designatedBodyCodes;
    }

    public void setDesignatedBodyCodes(String designatedBodyCodes) {
        this.designatedBodyCodes = designatedBodyCodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraineeProfile traineeProfile = (TraineeProfile) o;
        return Objects.equals(tisId, traineeProfile.tisId) &&
                Objects.equals(gmcNumber, traineeProfile.gmcNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tisId, gmcNumber);
    }

    @Override
    public String toString() {
        return "TraineeProfile{" +
                "active=" + active +
                ", tisId=" + tisId +
                ", gmcNumber='" + gmcNumber + '\'' +
                ", designatedBodyCodes='" + designatedBodyCodes + '\'' +
                ", dateAdded=" + dateAdded +
                '}';
    }
}

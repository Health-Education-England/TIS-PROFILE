package com.transformuk.hee.tis.auth.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;


@Entity(name = "IdMap")
@ApiModel(description = "TraineeId entity")
public class TraineeId {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long tisId;
    private String gmcNumber;

    public TraineeId() { }

    public TraineeId(Long tisId, String gmcNumber) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraineeId traineeId = (TraineeId) o;
        return Objects.equals(tisId, traineeId.tisId) &&
                Objects.equals(gmcNumber, traineeId.gmcNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tisId, gmcNumber);
    }

    @Override
    public String toString() {
        return "TraineeId{" +
                "gmcNumber='" + gmcNumber + '\'' +
                ", tisId=" + tisId +
                '}';
    }
}

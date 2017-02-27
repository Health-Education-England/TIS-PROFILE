package com.transformuk.hee.tis.auth.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;

/**
 * Request class to hold gmc info required for a trainee registration.
 */
public class RegistrationRequest {

    private String gmcNumber;
    private LocalDate dateAdded;

    @ApiModelProperty(required = true, value = "Trainee's GmcNumber")
    public String getGmcNumber() {
        return gmcNumber;
    }

    public void setGmcNumber(String gmcNumber) {
        this.gmcNumber = gmcNumber;
    }

    @ApiModelProperty(value = "date trainee added to designatedBody", example = "30/01/2016")
    @JsonFormat(pattern="dd/MM/yyyy")
    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }
}

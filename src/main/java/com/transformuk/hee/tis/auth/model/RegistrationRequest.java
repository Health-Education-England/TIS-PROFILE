package com.transformuk.hee.tis.auth.model;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.time.LocalDate;

/**
 * Request class to hold gmc info required for a trainee registration.
 */
public class RegistrationRequest {

    private String gmcNumber;
    private LocalDate dateAdded;

    @ApiModelProperty(required = true, value = "Trainee's GmcNumber")
    @NotEmpty
    public String getGmcNumber() {
        return gmcNumber;
    }

    public void setGmcNumber(String gmcNumber) {
        this.gmcNumber = gmcNumber;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }
}

package com.transformuk.hee.tis.auth.model;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Wrapper class to hold list of gmcNumbers
 */
public class GmcNumberWrapper {

    private String gmcNumber;

    @ApiModelProperty(required = true, value = "Trainee's GmcNumber")
    @NotEmpty
    public String getGmcNumber() {
        return gmcNumber;
    }

    public void setGmcNumber(String gmcNumber) {
        this.gmcNumber = gmcNumber;
    }
}

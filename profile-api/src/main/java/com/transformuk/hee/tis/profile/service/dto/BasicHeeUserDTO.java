package com.transformuk.hee.tis.profile.service.dto;


import com.transformuk.hee.tis.profile.dto.RoleDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the BasicHeeUser entity.
 */
@Data
public class BasicHeeUserDTO implements Serializable {

  @NotNull
  private String name;

  private String firstName;

  private String lastName;

  private String gmcId;

  private String emailAddress;

}
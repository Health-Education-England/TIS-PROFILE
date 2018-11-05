package com.transformuk.hee.tis.profile.validators;

import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.repository.RoleRepository;
import com.transformuk.hee.tis.profile.web.rest.errors.CustomParameterizedException;
import com.transformuk.hee.tis.profile.web.rest.errors.ErrorConstants;
import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Set;

/**
 * Validator to validate DBC codes,roles, password and gmc id of a Hee User
 */
@Component
public class HeeUserValidator {

  private static final String DESIGNATED_BODY_CODE = "Designated Body Code: ";
  private static final String ROLE_NAME = "Role name: ";

  @Autowired
  private ReferenceService referenceService;

  @Autowired
  private RoleRepository roleRepository;

  /**
   * Validates given dbc codes are exists and matches.
   *
   * @param dbcCodes
   * @throws CustomParameterizedException if any errors
   */
  public void validateDBCIds(Set<String> dbcCodes) {

    if (!CollectionUtils.isEmpty(dbcCodes)) {
      dbcCodes.forEach(dbcCode -> {
        try {
          // if designated body is None then don't validate
          if (!dbcCode.equalsIgnoreCase(HeeUser.NONE)) {
            ResponseEntity<DBCDTO> dbcdto = referenceService.getDBCByCode(dbcCode);
          }
        } catch (HttpClientErrorException ex) {
          throw new CustomParameterizedException("Invalid " + DESIGNATED_BODY_CODE + dbcCode, ErrorConstants.ERR_VALIDATION);
        }
      });
    }

  }

  /**
   * Validates given role name are exists and matches
   *
   * @param roles
   * @throws CustomParameterizedException if any errors
   */
  public void validateRoles(Set<Role> roles) {
    if (!CollectionUtils.isEmpty(roles)) {
      roles.forEach(role -> {
        Role dbRole = roleRepository.findByName(role.getName());
        if (dbRole == null) {
          throw new CustomParameterizedException("Invalid " + ROLE_NAME + role.getName(), ErrorConstants.ERR_VALIDATION);
        }
      });
    }
  }

  /**
   * Validates given password for new users and it should be atleast 8 chars long
   *
   * @param password
   */
  public void validatePassword(String password) {
    if (StringUtils.isEmpty(password) || password.length() < 8) {
      throw new CustomParameterizedException("Password should be minimum 8 chars long", ErrorConstants.ERR_VALIDATION);
    }
  }

  /**
   * Validates given password is Temporary for new users and it should be atleast 8 chars long
   *
   * @param isTemporaryPassword
   */
  public void validateIsTemporary(Boolean isTemporaryPassword) {
    if (isTemporaryPassword == null) {
      throw new CustomParameterizedException("isTemporaryPassword should be true or false", ErrorConstants.ERR_VALIDATION);
    }
  }

  /**
   * Validates given gmc id for users and it shouldn't be greater than 7 chars long
   *
   * @param gmcId
   */
  public void validateGmcId(String gmcId) {
    if (gmcId.length() > 7) {
      throw new CustomParameterizedException("GMC Id shouldn't be greater than 7 chars long", ErrorConstants.ERR_VALIDATION);
    }
  }

}

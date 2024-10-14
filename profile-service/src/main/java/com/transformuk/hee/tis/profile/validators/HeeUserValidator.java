package com.transformuk.hee.tis.profile.validators;

import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.repository.RoleRepository;
import com.transformuk.hee.tis.profile.web.rest.errors.CustomParameterizedException;
import com.transformuk.hee.tis.profile.web.rest.errors.ErrorConstants;
import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;

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
   * @param dbCodes Designated Body Codes to validate
   * @throws CustomParameterizedException if any errors
   */
  public void validateDbcIds(Set<String> dbCodes) {
    if (!CollectionUtils.isEmpty(dbCodes)) {
      dbCodes.forEach(dbcCode -> {
        try {
          // if designated body is None then don't validate
          if (!dbcCode.equalsIgnoreCase(HeeUser.NONE)) {
            ResponseEntity<DBCDTO> dbcdto = referenceService.getDBCByCode(dbcCode);
          }
        } catch (HttpClientErrorException ex) {
          throw new CustomParameterizedException("Invalid " + DESIGNATED_BODY_CODE + dbcCode,
              ErrorConstants.ERR_VALIDATION);
        }
      });
    }
  }

  /**
   * Validates given role names exist and matches
   *
   * @param roles Roles to validate
   * @throws CustomParameterizedException if any errors
   */
  public void validateRoles(Set<Role> roles) {
    if (!CollectionUtils.isEmpty(roles)) {
      roles.forEach(role -> {
        Role dbRole = roleRepository.findByName(role.getName());
        if (dbRole == null) {
          throw new CustomParameterizedException("Invalid " + ROLE_NAME + role.getName(),
              ErrorConstants.ERR_VALIDATION);
        }
      });
    }
  }

  /**
   * Validates given gmc id for users shouldn't be greater than 7 chars long
   *
   * @param gmcId The identifier given to a doctor by the GMC
   */
  public void validateGmcId(String gmcId) {
    if (gmcId != null && gmcId.length() > 7) {
      throw new CustomParameterizedException("GMC Id shouldn't be greater than 7 chars long",
          ErrorConstants.ERR_VALIDATION);
    }
  }
}
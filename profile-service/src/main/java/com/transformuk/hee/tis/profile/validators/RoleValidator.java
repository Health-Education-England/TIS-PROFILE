package com.transformuk.hee.tis.profile.validators;

import com.transformuk.hee.tis.profile.domain.Permission;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.profile.repository.PermissionRepository;
import com.transformuk.hee.tis.profile.web.rest.errors.CustomParameterizedException;
import com.transformuk.hee.tis.profile.web.rest.errors.ErrorConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * Validator to validate permissions and Hee User associated with role
 */
@Component
public class RoleValidator {

  private static final String PERMISSION_NAME = "Permission name: ";

  @Autowired
  private PermissionRepository permissionRepository;

  @Autowired
  private HeeUserRepository heeUserRepository;

  /**
   * Validates given permission name are exists and matches
   *
   * @param permissions
   * @throws CustomParameterizedException if any errors
   */
  public void validatePermissions(Set<Permission> permissions) {
    if (!CollectionUtils.isEmpty(permissions)) {
      permissions.forEach(permission -> {
        Permission dbPermission = permissionRepository.findByName(permission.getName());
        if (dbPermission == null) {
          throw new CustomParameterizedException("Invalid " + PERMISSION_NAME + permission.getName(),
              ErrorConstants.ERR_VALIDATION);
        }
      });
    }
  }

  /**
   * Validate before deleting a role, it's checks is any user associated with this role
   */
  public void validateBeforeDelete(String roleName) {
    if (!StringUtils.isEmpty(roleName)) {
      long noOfUsersHasRole = heeUserRepository.countByRolesNameAndActive(roleName, true);
      if (noOfUsersHasRole > 0) {
        throw new CustomParameterizedException("This role: " + roleName + " cann't be deleted as it's " +
            "associated with users",
            ErrorConstants.ERR_VALIDATION);
      }
    }
  }

}

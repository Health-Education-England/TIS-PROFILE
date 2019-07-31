package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.Permission;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Permission entity.
 */
@SuppressWarnings("unused")
public interface PermissionRepository extends JpaRepository<Permission, String> {

  /**
   * Returns a permission with given name
   *
   * @param permissionName Name of the permission
   * @return {@link Permission} Permission associated with the given permission name
   */
  Permission findByName(String permissionName);

  /**
   * Finds the permissions with the given names
   *
   * @param permissionNames the list of permission names
   * @return the permission found
   */
  List<Permission> findByNameIn(Set<String> permissionNames);

  /**
   * Finds specific permissions for a user determined by the Principal ending in their user ID
   *
   * @param userId The Id of the user
   * @return a list of permissions
   */
  List<Permission> findByPrincipalEndsWith(String userId);
}

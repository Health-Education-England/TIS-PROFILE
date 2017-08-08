package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Role entity.
 */
@SuppressWarnings("unused")
public interface RoleRepository extends JpaRepository<Role, String> {

  /**
   * Returns a role with given name
   *
   * @param roleName Name of the role
   * @return {@link Role} Role associated with the given role name
   */
  Role findByName(String roleName);

  /**
   * Finds the roles with the given names
   *
   * @param roleNames the list of role names
   * @return the roels found
   */
  List<Role> findByNameIn(Set<String> roleNames);
}

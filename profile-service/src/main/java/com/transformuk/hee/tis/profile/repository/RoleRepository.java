package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.Role;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Role entity.
 */
@SuppressWarnings("unused")
public interface RoleRepository extends JpaRepository<Role, String> {

  /**
   * Returns a role with given name.
   *
   * @param roleName Name of the role
   * @return {@link Role} Role associated with the given role name
   */
  Role findByName(String roleName);

  /**
   * Finds the roles with the given names.
   *
   * @param roleNames the list of role names
   * @return the roles found
   */
  List<Role> findByNameIn(Set<String> roleNames);

  /**
   * Finds the roles not in the given names.
   *
   * @param roleNames the list of role names
   * @param pageable  pageable object that defines the size and page number to return
   * @return the page of the roles found
   */
  Page<Role> findByNameNotIn(Collection<String> roleNames, Pageable pageable);
}

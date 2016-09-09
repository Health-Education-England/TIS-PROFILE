package com.transformuk.hee.tis.auth.repository;

import com.transformuk.hee.tis.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Repository for roles
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	/**
	 * Returns a role with given name
	 *
	 * @param roleName Name of the role
	 * @return {@link Role} Role associated with the given role name
	 */
	Role findByName(@Param("name") String roleName);

	/**
	 * Finds the roles with the given names
	 *
	 * @param roleNames the list of role names
	 * @return the roels found
	 */
	List<Role> findByNameIn(Set<String> roleNames);
}

package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Role entity.
 */
@SuppressWarnings("unused")
public interface RoleRepository extends JpaRepository<Role, String> {

}

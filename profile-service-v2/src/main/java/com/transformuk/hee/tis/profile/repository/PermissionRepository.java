package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Permission entity.
 */
@SuppressWarnings("unused")
public interface PermissionRepository extends JpaRepository<Permission, Long> {

}

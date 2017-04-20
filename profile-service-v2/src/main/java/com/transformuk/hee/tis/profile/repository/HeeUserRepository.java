package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.HeeUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the HeeUser entity.
 */
@SuppressWarnings("unused")
public interface HeeUserRepository extends JpaRepository<HeeUser, String> {

}

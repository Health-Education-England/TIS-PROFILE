package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.Immigration;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Immigration entity.
 */
@SuppressWarnings("unused")
public interface ImmigrationRepository extends JpaRepository<Immigration, Long> {

}

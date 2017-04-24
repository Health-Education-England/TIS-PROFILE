package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.GmcDetails;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the GmcDetails entity.
 */
@SuppressWarnings("unused")
public interface GmcDetailsRepository extends JpaRepository<GmcDetails, Long> {

}

package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.GdcDetails;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the GdcDetails entity.
 */
@SuppressWarnings("unused")
public interface GdcDetailsRepository extends JpaRepository<GdcDetails, Long> {

}

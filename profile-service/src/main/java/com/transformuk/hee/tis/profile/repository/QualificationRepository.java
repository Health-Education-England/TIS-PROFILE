package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Qualification entity.
 */
@SuppressWarnings("unused")
public interface QualificationRepository extends JpaRepository<Qualification, Long> {

}

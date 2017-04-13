package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.PersonalDetails;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the PersonalDetails entity.
 */
@SuppressWarnings("unused")
public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, Long> {

}

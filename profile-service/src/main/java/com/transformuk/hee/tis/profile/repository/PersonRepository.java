package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Person entity.
 */
@SuppressWarnings("unused")
public interface PersonRepository extends JpaRepository<Person, Long> {

}

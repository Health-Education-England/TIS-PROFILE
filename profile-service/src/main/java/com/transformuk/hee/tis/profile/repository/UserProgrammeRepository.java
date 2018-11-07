package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.UserProgramme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring data Repository for {@link UserProgramme} domain entity.
 */
@Repository
public interface UserProgrammeRepository extends JpaRepository<UserProgramme, Long> {

  List<UserProgramme> findByHeeUser(HeeUser heeUser);
}

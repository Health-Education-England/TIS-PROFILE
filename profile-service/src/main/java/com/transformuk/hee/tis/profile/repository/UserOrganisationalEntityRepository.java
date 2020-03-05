package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.UserOrganisationalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring data Repository for {@link UserTrust} domain entity.
 */
@Repository
public interface UserOrganisationalEntityRepository extends JpaRepository<UserOrganisationalEntity, Long> {

  List<UserOrganisationalEntity> findByHeeUser(HeeUser heeUser);
}

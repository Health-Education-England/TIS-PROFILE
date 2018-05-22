package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.UserTrust;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring data Repository for {@link UserTrust} domain entity.
 */
@Repository
public interface UserTrustRepository extends JpaRepository<UserTrust, Long> {

  List<UserTrust> findByHeeUser(HeeUser heeUser);
}

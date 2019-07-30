package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.UserTrust;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring data Repository for {@link UserTrust} domain entity.
 */
@Repository
public interface UserTrustRepository extends JpaRepository<UserTrust, Long> {

  List<UserTrust> findByHeeUser(HeeUser heeUser);
}

package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.HeeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the HeeUser entity.
 */
@SuppressWarnings("unused")
public interface HeeUserRepository extends JpaRepository<HeeUser, String>, JpaSpecificationExecutor<HeeUser> {

	/**
	 * Gets RO by designatedBodyCode
	 *
	 * @param designatedBodyCodes
	 * @return RO user details
	 */
	@Query("select u from HeeUser u join u.roles r join u.designatedBodyCodes dbc where r.name='RVOfficer' and dbc = :dbc")
	HeeUser findRVOfficerByDesignatedBodyCode(@Param("dbc") String designatedBodyCodes);

	@Query("select u from HeeUser u left outer join fetch u.designatedBodyCodes inner join fetch u.roles r inner join fetch r.permissions p " +
			"where u.active = true and u.name = :userName")
	HeeUser findByActive(@Param("userName") String userName);
}

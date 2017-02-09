package com.transformuk.hee.tis.auth.repository;

import com.transformuk.hee.tis.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Repository for users
 */
@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
	
	/**
	 * Gets RO by designatedBodyCode
	 * @param designatedBodyCodes
	 * @return RO user details
	 */
	@Query("select u from User u join u.roles r join u.designatedBodyCodes dbc where r.name='RVOfficer' and dbc = :dbc")
	User findRVOfficerByDesignatedBodyCode(@Param("dbc") String designatedBodyCodes);
	
	@Query("select u from User u inner join fetch u.designatedBodyCodes inner join fetch u.roles r inner join fetch r.permissions p " +
			"where u.active = true and u.name = :userName")
	User findByActive(@Param("userName") String userName);
}

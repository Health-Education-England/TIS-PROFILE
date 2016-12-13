package com.transformuk.hee.tis.auth.repository;

import com.transformuk.hee.tis.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for users
 */
@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
	/**
	 * Gets RO by designatedBodyCode
	 * @param designatedBodyCode
	 * @return RO user details
	 */
	@Query("select u from User u join u.roles r where r.name='RVOfficer' and u.designatedBodyCode = :dbc")
	User findRVOfficerByDesignatedBodyCode(@Param("dbc")String designatedBodyCode);
}

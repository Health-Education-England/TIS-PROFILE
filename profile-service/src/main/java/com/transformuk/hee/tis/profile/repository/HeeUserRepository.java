package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.HeeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

  /**
   * Gets distinct Users by exact designatedBodyCodes and permissions
   *
   * @param designatedBodyCodes
   * @param permissions
   * @return
   */
  @Query(value = "select distinct u.* from HeeUser u " +
      "inner join UserRole ur on ur.userName = u.name " +
      "inner join (" +
      "select userName,GROUP_CONCAT(distinct designatedBodyCode order by designatedBodyCode) dbcs from UserDesignatedBody " +
      "group by userName) as usb on usb.userName = u.name and usb.dbcs = :designatedBodyCodes " +
      "inner join RolePermission rp on rp.roleName = ur.roleName " +
      "where u.active = true " +
      "and rp.permissionName in (:permissions) order by u.firstName ",
      nativeQuery = true)
  List<HeeUser> findDistinctByExactDesignatedBodyCodesAndPermissions(@Param("designatedBodyCodes") String designatedBodyCodes,
                                                                     @Param("permissions") List<String> permissions);

  /**
   * Gets distinct Users by exact designatedBodyCodes
   *
   * @param designatedBodyCodes
   * @return
   */
  @Query(value = "select distinct u.* from HeeUser u " +
      "inner join UserRole ur on ur.userName = u.name " +
      "inner join (" +
      "select userName,GROUP_CONCAT(distinct designatedBodyCode order by designatedBodyCode) dbcs from UserDesignatedBody " +
      "group by userName) as usb on usb.userName = u.name and usb.dbcs = :designatedBodyCodes " +
      "where u.active = true " +
      "order by u.firstName ",
      nativeQuery = true)
  List<HeeUser> findDistinctByExactDesignatedBodyCodes(@Param("designatedBodyCodes") String designatedBodyCodes);


  /**
   * Gets counts of users by roles
   *
   * @param roleName
   * @return
   */
  long countByRolesNameAndActive(@Param("roleName") String roleName, @Param("active") Boolean active);

}

package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.HeeUser;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HeeUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HeeUserRepository extends JpaRepository<HeeUser, String>,
    JpaSpecificationExecutor<HeeUser> {

  /**
   * Gets RO by designatedBodyCode
   *
   * @param designatedBodyCodes
   * @return RO user details
   */
  @Query("select u from HeeUser u " +
      " join u.roles r " +
      " left outer join fetch u.associatedTrusts " +
      " left outer join fetch u.associatedProgrammes " +
      " join u.designatedBodyCodes dbc " +
      " where r.name='RVOfficer' and dbc = :dbc")
  List<HeeUser> findRVOfficerByDesignatedBodyCode(@Param("dbc") String designatedBodyCodes);

  @Query("select u from HeeUser u " +
      "left outer join fetch u.designatedBodyCodes " +
      "left outer join fetch u.associatedTrusts " +
      "left outer join fetch u.associatedProgrammes " +
      "inner join fetch u.roles r " +
      "inner join fetch r.permissions p " +
      "where u.active = true " +
      "and u.name = :userName")
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
      "select userName,GROUP_CONCAT(distinct designatedBodyCode order by designatedBodyCode) dbcs from UserDesignatedBody "
      +
      "group by userName) as usb on usb.userName = u.name and usb.dbcs = :designatedBodyCodes " +
      "inner join RolePermission rp on rp.roleName = ur.roleName " +
      "where u.active = true " +
      "and rp.permissionName in (:permissions) order by u.firstName ",
      nativeQuery = true)
  List<HeeUser> findDistinctByExactDesignatedBodyCodesAndPermissions(
      @Param("designatedBodyCodes") String designatedBodyCodes,
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
      "select userName,GROUP_CONCAT(distinct designatedBodyCode order by designatedBodyCode) dbcs from UserDesignatedBody "
      +
      "group by userName) as usb on usb.userName = u.name and usb.dbcs = :designatedBodyCodes " +
      "where u.active = true " +
      "order by u.firstName ",
      nativeQuery = true)
  List<HeeUser> findDistinctByExactDesignatedBodyCodes(
      @Param("designatedBodyCodes") String designatedBodyCodes);

  /**
   * Gets counts of users by roles
   *
   * @param roleName
   * @return
   */
  long countByRolesNameAndActive(@Param("roleName") String roleName,
      @Param("active") Boolean active);

  @Query(value = "SELECT u FROM HeeUser u " +
      "LEFT JOIN FETCH u.associatedTrusts " +
      "LEFT JOIN FETCH u.associatedProgrammes " +
      "where u.name = :name")
  Optional<HeeUser> findByNameWithTrustsAndProgrammes(@Param("name") String name);

  /**
   * Finds HeeUsers by username with a like search
   *
   * @param page
   * @param name
   * @return
   */
  Page<HeeUser> findByNameIgnoreCaseContaining(Pageable page, String name);

  /**
   * Find HeeUsers ingore case
   * @return
   */
  List<HeeUser> findByNameIgnoreCase(String name);

  @Query(value = "select distinct u.* from HeeUser u " +
          "inner join UserRole ur on ur.userName = u.name " +
  "where ur.roleName IN (:roleNames)"+
          "AND u.active=true", nativeQuery = true)
  List<HeeUser> findHeeUsersByRoleNames(@Param("roleNames") List<String> roleNames);
}

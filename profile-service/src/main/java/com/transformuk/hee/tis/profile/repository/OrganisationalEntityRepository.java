package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.domain.OrganisationalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Role entity.
 */
@SuppressWarnings("unused")
public interface OrganisationalEntityRepository extends JpaRepository<OrganisationalEntity, String> {

  /**
   * Returns a role with given name
   *
   * @param entityName Name of the entity
   * @return {@link Role} Role associated with the given role name
   */
  OrganisationalEntity findByName(String entityName);

  /**
   * Finds the roles with the given names
   *
   * @param entityNames the list of role names
   * @return the roels found
   */
  List<OrganisationalEntity> findByNameIn(Set<String> entityNames);
}

package com.transformuk.hee.tis.profile.service.mapper;

import com.transformuk.hee.tis.profile.domain.OrganisationalEntity;
import com.transformuk.hee.tis.profile.dto.OrganisationalEntityDTO;
import org.mapstruct.Mapper;

import java.util.Arrays;
import java.util.List;

/**
 * Mapper for the entity OrganizationalEntity and its DTO OrganisationalEntityDTO.
 */
@Mapper(componentModel = "spring", uses = {}, imports = Arrays.class)
public interface OrganisationalEntityMapper {

  OrganisationalEntityDTO organisationalEntityToEntityDTO(OrganisationalEntity entity);

  List<OrganisationalEntityDTO> organisationalEntitiesToEntityDTOs(List<OrganisationalEntity> entities);
}

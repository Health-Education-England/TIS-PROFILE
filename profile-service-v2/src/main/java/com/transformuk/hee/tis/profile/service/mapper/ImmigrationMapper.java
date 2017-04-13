package com.transformuk.hee.tis.profile.service.mapper;

import com.transformuk.hee.tis.profile.domain.Immigration;
import com.transformuk.hee.tis.profile.service.dto.ImmigrationDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity Immigration and its DTO ImmigrationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ImmigrationMapper {

	ImmigrationDTO immigrationToImmigrationDTO(Immigration immigration);

	List<ImmigrationDTO> immigrationsToImmigrationDTOs(List<Immigration> immigrations);

	Immigration immigrationDTOToImmigration(ImmigrationDTO immigrationDTO);

	List<Immigration> immigrationDTOsToImmigrations(List<ImmigrationDTO> immigrationDTOs);
}

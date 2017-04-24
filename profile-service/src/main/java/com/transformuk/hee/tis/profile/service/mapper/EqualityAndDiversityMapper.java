package com.transformuk.hee.tis.profile.service.mapper;

import com.transformuk.hee.tis.profile.domain.EqualityAndDiversity;
import com.transformuk.hee.tis.profile.service.dto.EqualityAndDiversityDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity EqualityAndDiversity and its DTO EqualityAndDiversityDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EqualityAndDiversityMapper {

	EqualityAndDiversityDTO equalityAndDiversityToEqualityAndDiversityDTO(EqualityAndDiversity equalityAndDiversity);

	List<EqualityAndDiversityDTO> equalityAndDiversitiesToEqualityAndDiversityDTOs(List<EqualityAndDiversity> equalityAndDiversities);

	EqualityAndDiversity equalityAndDiversityDTOToEqualityAndDiversity(EqualityAndDiversityDTO equalityAndDiversityDTO);

	List<EqualityAndDiversity> equalityAndDiversityDTOsToEqualityAndDiversities(List<EqualityAndDiversityDTO> equalityAndDiversityDTOs);
}

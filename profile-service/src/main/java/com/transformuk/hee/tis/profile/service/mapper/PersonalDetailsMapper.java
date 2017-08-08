package com.transformuk.hee.tis.profile.service.mapper;

import com.transformuk.hee.tis.profile.domain.PersonalDetails;
import com.transformuk.hee.tis.profile.service.dto.PersonalDetailsDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity PersonalDetails and its DTO PersonalDetailsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PersonalDetailsMapper {

  PersonalDetailsDTO personalDetailsToPersonalDetailsDTO(PersonalDetails personalDetails);

  List<PersonalDetailsDTO> personalDetailsToPersonalDetailsDTOs(List<PersonalDetails> personalDetails);

  PersonalDetails personalDetailsDTOToPersonalDetails(PersonalDetailsDTO personalDetailsDTO);

  List<PersonalDetails> personalDetailsDTOsToPersonalDetails(List<PersonalDetailsDTO> personalDetailsDTOs);
}

package com.transformuk.hee.tis.profile.service.mapper;

import com.transformuk.hee.tis.profile.domain.GmcDetails;
import com.transformuk.hee.tis.profile.service.dto.GmcDetailsDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity GmcDetails and its DTO GmcDetailsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GmcDetailsMapper {

  GmcDetailsDTO gmcDetailsToGmcDetailsDTO(GmcDetails gmcDetails);

  List<GmcDetailsDTO> gmcDetailsToGmcDetailsDTOs(List<GmcDetails> gmcDetails);

  GmcDetails gmcDetailsDTOToGmcDetails(GmcDetailsDTO gmcDetailsDTO);

  List<GmcDetails> gmcDetailsDTOsToGmcDetails(List<GmcDetailsDTO> gmcDetailsDTOs);
}

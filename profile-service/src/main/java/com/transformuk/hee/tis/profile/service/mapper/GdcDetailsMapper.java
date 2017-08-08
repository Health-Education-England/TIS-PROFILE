package com.transformuk.hee.tis.profile.service.mapper;

import com.transformuk.hee.tis.profile.domain.GdcDetails;
import com.transformuk.hee.tis.profile.service.dto.GdcDetailsDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity GdcDetails and its DTO GdcDetailsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GdcDetailsMapper {

  GdcDetailsDTO gdcDetailsToGdcDetailsDTO(GdcDetails gdcDetails);

  List<GdcDetailsDTO> gdcDetailsToGdcDetailsDTOs(List<GdcDetails> gdcDetails);

  GdcDetails gdcDetailsDTOToGdcDetails(GdcDetailsDTO gdcDetailsDTO);

  List<GdcDetails> gdcDetailsDTOsToGdcDetails(List<GdcDetailsDTO> gdcDetailsDTOs);
}

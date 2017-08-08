package com.transformuk.hee.tis.profile.service.mapper;

import com.transformuk.hee.tis.profile.domain.Qualification;
import com.transformuk.hee.tis.profile.service.dto.QualificationDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity Qualification and its DTO QualificationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QualificationMapper {

  QualificationDTO qualificationToQualificationDTO(Qualification qualification);

  List<QualificationDTO> qualificationsToQualificationDTOs(List<Qualification> qualifications);

  Qualification qualificationDTOToQualification(QualificationDTO qualificationDTO);

  List<Qualification> qualificationDTOsToQualifications(List<QualificationDTO> qualificationDTOs);
}

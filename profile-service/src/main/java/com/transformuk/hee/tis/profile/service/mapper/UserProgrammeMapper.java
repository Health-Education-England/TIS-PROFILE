package com.transformuk.hee.tis.profile.service.mapper;

import com.transformuk.hee.tis.profile.domain.UserProgramme;
import com.transformuk.hee.tis.profile.service.dto.UserProgrammeDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity UserProgramme and its DTO UserProgrammeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserProgrammeMapper {

  UserProgrammeDTO toDto(UserProgramme userProgramme);

  List<UserProgrammeDTO> toDtos(List<UserProgramme> userProgrammes);

  UserProgramme toEntity(UserProgramme userProgramme);

  List<UserProgramme> toEntities(List<UserProgramme> userProgrammes);
}

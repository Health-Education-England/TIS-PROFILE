package com.transformuk.hee.tis.profile.service.mapper;

import com.transformuk.hee.tis.profile.domain.UserTrust;
import com.transformuk.hee.tis.profile.service.dto.UserTrustDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity UserTrust and its DTO UserTrustDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserTrustMapper {

  UserTrustDTO toDto(UserTrust userTrust);

  List<UserTrustDTO> toDtos(List<UserTrust> userTrusts);

  UserTrust toEntity(UserTrust userTrust);

  List<UserTrust> toEntities(List<UserTrust> userTrusts);
}

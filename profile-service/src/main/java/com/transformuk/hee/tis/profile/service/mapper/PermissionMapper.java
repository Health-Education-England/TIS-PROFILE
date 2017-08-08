package com.transformuk.hee.tis.profile.service.mapper;

import com.transformuk.hee.tis.profile.domain.Permission;
import com.transformuk.hee.tis.profile.dto.PermissionDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity Permission and its DTO Permission.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PermissionMapper {

  PermissionDTO permissionToPermissionDTO(Permission permission);

  List<PermissionDTO> permissionsToPermissionDTOs(List<Permission> permissions);

  Permission permissionDTOToPermission(PermissionDTO permission);

  List<Permission> permissionDTOsToPermissions(List<PermissionDTO> permissions);
}

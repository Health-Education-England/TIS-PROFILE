package com.transformuk.hee.tis.profile.service.mapper;

import com.transformuk.hee.tis.profile.domain.Permission;
import com.transformuk.hee.tis.profile.service.dto.PermissionDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity Permission and its DTO PermissionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PermissionMapper {

	PermissionDTO permissionToPermissionDTO(Permission permission);

	List<PermissionDTO> permissionsToPermissionDTOs(List<Permission> permissions);

	Permission permissionDTOToPermission(PermissionDTO permissionDTO);

	List<Permission> permissionDTOsToPermissions(List<PermissionDTO> permissionDTOs);
}

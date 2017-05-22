package com.transformuk.hee.tis.profile.service.mapper;

import com.transformuk.hee.tis.profile.dto.Permission;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity Permission and its DTO Permission.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PermissionMapper {

	Permission permissionToPermissionDTO(com.transformuk.hee.tis.profile.domain.Permission permission);

	List<Permission> permissionsToPermissionDTOs(List<com.transformuk.hee.tis.profile.domain.Permission> permissions);

	com.transformuk.hee.tis.profile.domain.Permission permissionDTOToPermission(Permission permission);

	List<com.transformuk.hee.tis.profile.domain.Permission> permissionDTOsToPermissions(List<Permission> permissions);
}

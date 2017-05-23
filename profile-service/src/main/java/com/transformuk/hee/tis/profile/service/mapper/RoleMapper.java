package com.transformuk.hee.tis.profile.service.mapper;

import com.transformuk.hee.tis.profile.domain.Permission;
import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.dto.PermissionDTO;
import com.transformuk.hee.tis.profile.dto.RoleDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity Role and its DTO RoleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RoleMapper {

	RoleDTO roleToRoleDTO(Role role);

	List<RoleDTO> rolesToRoleDTOs(List<Role> roles);

	Role roleDTOToRole(RoleDTO roleDTO);

	List<Role> roleDTOsToRoles(List<RoleDTO> roleDTOs);

	PermissionDTO map(Permission permission);

	Permission map(PermissionDTO permissionDTO);
}

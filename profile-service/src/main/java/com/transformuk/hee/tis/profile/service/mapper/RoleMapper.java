package com.transformuk.hee.tis.profile.service.mapper;

import com.transformuk.hee.tis.profile.domain.Permission;
import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.dto.PermissionDTO;
import com.transformuk.hee.tis.profile.dto.RoleDTO;
import java.util.Arrays;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Role and its DTO RoleDTO.
 */
@Mapper(componentModel = "spring", uses = {}, imports = Arrays.class)
public interface RoleMapper {

  RoleDTO roleToRoleDTO(Role role);

  List<RoleDTO> rolesToRoleDTOs(List<Role> roles);

  Role roleDTOToRole(RoleDTO roleDTO);

  List<Role> roleDTOsToRoles(List<RoleDTO> roleDTOs);

  @Mapping(target = "actions",
      expression = "java(Arrays.asList(permission.getActions().split(\",\")))")
  PermissionDTO map(Permission permission);

  @Mapping(target = "actions",
      expression = "java(String.join(\",\", permissionDTO.getActions()))")
  Permission map(PermissionDTO permissionDTO);
}

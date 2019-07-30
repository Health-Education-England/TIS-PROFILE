package com.transformuk.hee.tis.profile.service.mapper;

import com.transformuk.hee.tis.profile.domain.Permission;
import com.transformuk.hee.tis.profile.dto.PermissionDTO;
import java.util.Arrays;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Permission and its DTO Permission.
 */
@Mapper(componentModel = "spring", uses = {}, imports = Arrays.class)
public interface PermissionMapper {

  @Mapping(target = "actions",
      expression = "java(Arrays.asList(permission.getActions().split(\",\")))")
  PermissionDTO permissionToPermissionDTO(Permission permission);

  List<PermissionDTO> permissionsToPermissionDTOs(List<Permission> permissions);

  @Mapping(target = "actions",
      expression = "java(String.join(\",\", permission.getActions()))")
  Permission permissionDTOToPermission(PermissionDTO permission);

  List<Permission> permissionDTOsToPermissions(List<PermissionDTO> permissions);
}

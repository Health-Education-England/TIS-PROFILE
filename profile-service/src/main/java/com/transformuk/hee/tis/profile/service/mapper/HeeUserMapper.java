package com.transformuk.hee.tis.profile.service.mapper;

import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.Permission;
import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.domain.UserProgramme;
import com.transformuk.hee.tis.profile.domain.UserTrust;
import com.transformuk.hee.tis.profile.dto.PermissionDTO;
import com.transformuk.hee.tis.profile.dto.RoleDTO;
import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import com.transformuk.hee.tis.profile.service.dto.UserProgrammeDTO;
import com.transformuk.hee.tis.profile.service.dto.UserTrustDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Arrays;
import java.util.List;

/**
 * Mapper for the entity HeeUser and its DTO HeeUserDTO.
 */
@Mapper(componentModel = "spring", uses = {}, imports = Arrays.class)
public interface HeeUserMapper {

  HeeUserDTO heeUserToHeeUserDTO(HeeUser heeUser);

  List<HeeUserDTO> heeUsersToHeeUserDTOs(List<HeeUser> heeUsers);

  HeeUser heeUserDTOToHeeUser(HeeUserDTO heeUserDTO);

  List<HeeUser> heeUserDTOsToHeeUsers(List<HeeUserDTO> heeUserDTOs);

  RoleDTO map(Role role);

  Role map(RoleDTO roleDTO);

  UserTrust map(UserTrustDTO userTrustDTO);

  UserTrustDTO map(UserTrust userTrust);

  UserProgramme map(UserProgrammeDTO userProgrammeDTO);

  UserProgrammeDTO map(UserProgramme userProgramme);


  @Mapping(target = "actions",
      expression = "java(Arrays.asList(permission.getActions().split(\",\")))")
  PermissionDTO map(Permission permission);

  @Mapping(target = "actions",
      expression = "java(String.join(\",\", permissionDTO.getActions()))")
  Permission map(PermissionDTO permissionDTO);


}

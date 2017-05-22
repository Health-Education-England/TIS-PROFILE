package com.transformuk.hee.tis.profile.service.mapper;

import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import com.transformuk.hee.tis.profile.service.dto.RoleDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity HeeUser and its DTO HeeUserDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HeeUserMapper {

	HeeUserDTO heeUserToHeeUserDTO(HeeUser heeUser);

	List<HeeUserDTO> heeUsersToHeeUserDTOs(List<HeeUser> heeUsers);

	HeeUser heeUserDTOToHeeUser(HeeUserDTO heeUserDTO);

	List<HeeUser> heeUserDTOsToHeeUsers(List<HeeUserDTO> heeUserDTOs);

	RoleDTO map(Role role);

	Role map(RoleDTO roleDTO);
}

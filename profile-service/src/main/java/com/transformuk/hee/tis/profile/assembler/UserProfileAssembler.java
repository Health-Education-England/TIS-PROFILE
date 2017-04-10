package com.transformuk.hee.tis.profile.assembler;

import com.transformuk.hee.tis.profile.model.Permission;
import com.transformuk.hee.tis.profile.model.Role;
import com.transformuk.hee.tis.profile.model.User;
import com.transformuk.hee.tis.security.model.UserProfile;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Assembler to transform domain object from/to REST objects.
 */
@Component
public class UserProfileAssembler {

	public UserProfile toUserProfile(User user) {
		UserProfile userProfile = new UserProfile();
		userProfile.setDesignatedBodyCodes(user.getDesignatedBodyCodes());
		userProfile.setEmailAddress(user.getEmailAddress());
		userProfile.setFirstName(user.getFirstName());
		userProfile.setFullName(user.getFirstName() + " " + user.getLastName());
		userProfile.setGmcId(user.getGmcId());
		userProfile.setLastName(user.getLastName());
		userProfile.setPhoneNumber(user.getPhoneNumber());
		userProfile.setUserName(user.getName());
		Set<Role> roles = user.getRoles();
		userProfile.setRoles(roles.stream().map(Role::getName).collect(toSet()));
		userProfile.setPermissions(getPermissions(roles));
		return userProfile;
	}

	private Set<String> getPermissions(Set<Role> roles) {
		return roles.stream()
				.map(this::getPermissions)
				.flatMap(Collection::stream)
				.collect(toSet());
	}

	private Set<String> getPermissions(Role role) {
		return role.getPermissions().stream()
				.map(Permission::getName)
				.collect(toSet());
	}
}

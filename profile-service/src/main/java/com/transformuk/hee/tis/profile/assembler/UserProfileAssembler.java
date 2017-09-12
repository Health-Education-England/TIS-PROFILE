package com.transformuk.hee.tis.profile.assembler;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.Permission;
import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.repository.PermissionRepository;
import com.transformuk.hee.tis.security.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toSet;

/**
 * Assembler to transform domain object from/to REST objects.
 */
@Component
public class UserProfileAssembler {

  @Autowired
  private PermissionRepository permissionRepository;

  public UserProfile toUserProfile(HeeUser user) {
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

    //Set user permission policies, a combination of policies attached to the user Roles, and
    //policies specific to the user
    Set<com.transformuk.hee.tis.iam.Permission> policies = Sets.newHashSet();
    policies.addAll(permissionRepository.findByPrincipalEndsWith(":" + user.getName()).stream().map(p -> toPermissionPolicy(p)).collect(toSet()));
    policies.addAll(getPermissionPolicies(roles));
    userProfile.setPermissionPolicies(policies);
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

  private Set<com.transformuk.hee.tis.iam.Permission> getPermissionPolicies(Set<Role> roles) {
    return roles.stream()
        .map(Role::getPermissions)
        .flatMap(Collection::stream)
        .map(p -> toPermissionPolicy(p))
        .collect(toSet());
  }

  private com.transformuk.hee.tis.iam.Permission toPermissionPolicy(Permission permission) {
    List<String> actions = permission.getActions() != null ? Arrays.asList(permission.getActions().split(",")) : newArrayList();
    return new com.transformuk.hee.tis.iam.Permission(
        permission.getName(),
        permission.getPrincipal(),
        permission.getResource(),
        actions,
        permission.getEffect()
    );
  }
}

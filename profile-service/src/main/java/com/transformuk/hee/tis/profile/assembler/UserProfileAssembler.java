package com.transformuk.hee.tis.profile.assembler;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toSet;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.Permission;
import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.domain.UserProgramme;
import com.transformuk.hee.tis.profile.domain.UserTrust;
import com.transformuk.hee.tis.profile.repository.PermissionRepository;
import com.transformuk.hee.tis.security.model.Programme;
import com.transformuk.hee.tis.security.model.Trust;
import com.transformuk.hee.tis.security.model.UserProfile;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Assembler to transform domain object from/to REST objects.
 */
@Component
public class UserProfileAssembler {

  static final String PRINCIPLE_SEPARATOR = ":";

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
    userProfile.setAssignedEntities(user.getEntities());

    //Set user permission policies, a combination of policies attached to the user Roles, and
    //policies specific to the user
    Set<com.transformuk.hee.tis.iam.Permission> policies = Sets.newHashSet();
    policies
        .addAll(permissionRepository.findByPrincipalEndsWith(PRINCIPLE_SEPARATOR + user.getName())
            .stream()
            .map(this::toPermissionPolicy)
            .collect(toSet()));

    policies.addAll(getPermissionPolicies(roles));
    userProfile.setPermissionPolicies(policies);

    //get user trust info
    Set<UserTrust> associatedTrusts = user.getAssociatedTrusts();
    if (CollectionUtils.isNotEmpty(associatedTrusts)) {
      Set<Trust> trusts = associatedTrusts.stream().map(this::getTrust).collect(Collectors.toSet());
      userProfile.setAssignedTrusts(trusts);
    }

    Set<UserProgramme> associatedProgrammes = user.getAssociatedProgrammes();
    if (CollectionUtils.isNotEmpty(associatedProgrammes)) {
      Set<Programme> programmes = associatedProgrammes.stream().map(this::getProgramme)
          .collect(Collectors.toSet());
      userProfile.setAssignedProgrammes(programmes);
    }

    return userProfile;
  }

  private Trust getTrust(UserTrust userTrust) {
    return new Trust(userTrust.getTrustId(), userTrust.getTrustCode(), userTrust.getTrustName());
  }

  private Programme getProgramme(UserProgramme userProgramme) {
    return new Programme(userProgramme.getProgrammeId(), userProgramme.getProgrammeName(),
        userProgramme.getProgrammeNumber());
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
    List<String> actions =
        permission.getActions() != null ? Arrays.asList(permission.getActions().split(","))
            : newArrayList();
    return new com.transformuk.hee.tis.iam.Permission(
        permission.getName(),
        permission.getPrincipal(),
        permission.getResource(),
        actions,
        permission.getEffect()
    );
  }
}

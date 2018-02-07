package com.transformuk.hee.tis.profile.service;

import com.google.common.collect.Lists;
import com.transform.hee.tis.keycloak.KeycloakAdminClient;
import com.transform.hee.tis.keycloak.User;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wrapper service class for Keycloak admin client to create/update User
 */
@Service
public class KeycloakAdminClientService {
  private static final String REALM_LIN = "lin";

  @Autowired
  public KeycloakAdminClient keycloakAdminClient;

  /**
   * Create user in Keycloak
   *
   * @param heeUser
   */
  public void createUser(HeeUser heeUser) {
    // create user in KeyCloak
    User userToCreate = heeUserToKeycloakUser(heeUser);
    keycloakAdminClient.createUser(REALM_LIN, userToCreate);
  }

  /**
   * Update user in Keycloak
   *
   * @param heeUser
   */
  public void updateUser(HeeUser heeUser) {
    // First try to create convert user in KeyClock
    User existingUser = keycloakAdminClient.findByUsername(REALM_LIN, heeUser.getName());
    User userToUpdate = heeUserToKeycloakUser(heeUser);
    keycloakAdminClient.updateUser(REALM_LIN, existingUser.getId(), userToUpdate);
  }

  public Map<String, List<String>> getUserAttributes(String username) {
    return keycloakAdminClient.getAttributesForUser(REALM_LIN, username);
  }

  public List<GroupRepresentation> getUserGroups(String username) {
    User user = keycloakAdminClient.findByUsername(REALM_LIN, username);
    List<GroupRepresentation> groupList = keycloakAdminClient.listGroups(REALM_LIN,user);
    return groupList;
  }

  private User heeUserToKeycloakUser(HeeUser heeUser) {
    Map<String,List<String>> attributes = new HashMap<>();
    List dbcs = new ArrayList();
    dbcs.add(heeUser.getDesignatedBodyCodes());
    attributes.put("DBC",dbcs);
    return User.create(heeUser.getFirstName(), heeUser.getLastName(), heeUser.getName(),
        heeUser.getEmailAddress(), heeUser.getPassword(),heeUser.getTemporaryPassword(),attributes);
  }

}

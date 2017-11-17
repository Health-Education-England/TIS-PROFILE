package com.transformuk.hee.tis.profile.service;

import com.transform.hee.tis.keycloak.KeycloakAdminClient;
import com.transform.hee.tis.keycloak.User;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Wrapper service class for Keyclock admin client to create/update User
 */
@Service
public class KeycloakAdminClientService {
  private static final String REALM_LIN = "lin";

  @Autowired
  public KeycloakAdminClient keycloakAdminClient;

  /**
   * Create user in Keyclock
   *
   * @param heeUser
   */
  public void createUser(HeeUser heeUser) {
    // create user in KeyClock
    User userToCreate = heeUserToKeyclockUser(heeUser);
    keycloakAdminClient.createUser(REALM_LIN, userToCreate);
  }

  /**
   * Update user in Keyclock
   *
   * @param heeUser
   */
  public void updateUser(HeeUser heeUser) {
    // First try to create convert user in KeyClock
    User existingUser = keycloakAdminClient.findByUsername(REALM_LIN, heeUser.getName());
    User userToUpdate = heeUserToKeyclockUser(heeUser);
    keycloakAdminClient.updateUser(REALM_LIN, existingUser.getId(), userToUpdate);
  }

  public Map<String, List<String>> getUserAttributes(HeeUser heeUser) {
    return keycloakAdminClient.getAttributesForUser(REALM_LIN, heeUser.getName());
  }

  private User heeUserToKeyclockUser(HeeUser heeUser) {
    return User.create(heeUser.getFirstName(), heeUser.getLastName(), heeUser.getName(),
        heeUser.getEmailAddress(), heeUser.getPassword(),heeUser.getTemporaryPassword());
  }

}

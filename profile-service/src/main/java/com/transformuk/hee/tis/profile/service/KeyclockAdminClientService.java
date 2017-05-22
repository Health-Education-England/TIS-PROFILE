package com.transformuk.hee.tis.profile.service;

import com.transform.hee.tis.keycloak.KeyclockAdminClient;
import com.transform.hee.tis.keycloak.User;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Wrapper service class for Keyclock admin client to create/update User
 */
@Service
public class KeyclockAdminClientService {
	private static final String REALM_LIN = "lin";

	@Autowired
	public KeyclockAdminClient keyclockAdminClient;

	/**
	 * Create user in Keyclock
	 *
	 * @param heeUser
	 */
	public void createUser(HeeUser heeUser) {
		// create user in KeyClock
		User userToCreate = heeUserToKeyclockUser(heeUser);
		keyclockAdminClient.createUser(REALM_LIN, userToCreate);
	}

	/**
	 * Update user in Keyclock
	 *
	 * @param heeUser
	 */
	public void updateUser(HeeUser heeUser) {
		// First try to create convert user in KeyClock
		User existingUser = keyclockAdminClient.findByUsername(REALM_LIN, heeUser.getName());
		User userToUpdate = heeUserToKeyclockUser(heeUser);
		keyclockAdminClient.updateUser(REALM_LIN, existingUser.getId(), userToUpdate);
	}

	private User heeUserToKeyclockUser(HeeUser heeUser) {
		return User.create(heeUser.getFirstName(), heeUser.getLastName(), heeUser.getName(),
				heeUser.getEmailAddress(), heeUser.getPassword());
	}

}

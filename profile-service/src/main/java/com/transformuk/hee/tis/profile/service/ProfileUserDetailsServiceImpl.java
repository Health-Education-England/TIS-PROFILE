package com.transformuk.hee.tis.profile.service;

import com.transformuk.hee.tis.profile.assembler.UserProfileAssembler;
import com.transformuk.hee.tis.profile.model.User;
import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.security.service.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class to retrieve user profile details from profile store.
 */
@Transactional(readOnly = true)
public class ProfileUserDetailsServiceImpl implements UserDetailsService {

	private final LoginService loginService;
	private final UserProfileAssembler userProfileAssembler;

	public ProfileUserDetailsServiceImpl(LoginService loginService, UserProfileAssembler userProfileAssembler) {
		this.loginService = loginService;
		this.userProfileAssembler = userProfileAssembler;
	}

	@Override
	public UserProfile getProfile(String token) {
		User user = loginService.getUserByToken(token);
		return userProfileAssembler.toUserProfile(user);
	}
}

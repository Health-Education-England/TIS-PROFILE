package com.transformuk.hee.tis.auth.service;

import com.transformuk.hee.tis.auth.model.User;
import com.transformuk.hee.tis.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static com.transformuk.hee.tis.auth.filters.UserSpecification.withDBC;
import static com.transformuk.hee.tis.auth.filters.UserSpecification.withPermissions;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.data.domain.Sort.Direction.ASC;

/**
 * Service for user login/logout process and getting user's data
 */
@Service
@Transactional(readOnly = true)
public class LoginService {
	private static final Logger LOG = getLogger(LoginService.class);
	
	private final UserRepository userRepository;

	public LoginService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * @param userName User's unique identifier
	 * @return {@link User} User associated with given unique user name
	 */
	public User getUserByUserName(String userName) {
		User user = userRepository.findOne(userName);
		if (user == null) {
			throw new EntityNotFoundException(format("User with username %s not found", userName));
		}
		return user;
	}

	/**
	 * Returns all users by search criteria
	 *
	 * @param offset the result number to start from
	 * @param limit  the page size
	 * @param designatedBodyCode the designatedBodyCode to use
	 * @param permissions the permissions to use
	 * @return {@link List<User>} list of users
	 */
	public Page<User> getUsers(int offset, int limit, String designatedBodyCode, String permissions) {
		Specifications<User> spec = Specifications.where(withDBC(designatedBodyCode));
		int pageNumber = offset / limit;
		Pageable page = new PageRequest(pageNumber, limit, new Sort(ASC, "firstName"));
		if (permissions != null) {
			spec = spec.and(withPermissions(asList(permissions.split(","))));
		} 
		return userRepository.findAll(spec, page);
	}

	/**
	 * Gets RevalidationOfficer details by designatedBodyCode
	 * @param designatedBodyCode
	 * @return {@link User}
	 */
	public User getRVOfficer(String designatedBodyCode) {
		return userRepository.findRVOfficerByDesignatedBodyCode(designatedBodyCode);
	}
}

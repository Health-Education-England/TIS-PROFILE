package com.transformuk.hee.tis.auth.service;

import com.transformuk.hee.tis.auth.exception.UserNotFoundException;
import com.transformuk.hee.tis.auth.model.User;
import com.transformuk.hee.tis.auth.model.UserListResponse;
import com.transformuk.hee.tis.auth.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.transformuk.hee.tis.auth.filters.UserSpecification.isEqualTo;
import static java.lang.String.format;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * Service for user login/logout process and getting user's data
 */
@Service
public class LoginService {
	private UserRepository userRepository;

	public LoginService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * @param userName User's unique identifier
	 * @return {@link User} User associated with given unique user name
	 */
	public User getUserByUserName(String userName) throws UserNotFoundException {
		User user = userRepository.findOne(userName);
		if (user == null) {
			throw new UserNotFoundException(format("User with username %s not found", userName));
		}
		return user;
	}

	/**
	 * Returns all users by search criteria
	 *
	 * @param offset the result number to start from
	 * @param limit  the page size
	 * @param filter the filter to use
	 * @return {@link List<User>} list of users
	 */
	public UserListResponse getUsers(int offset, int limit, String filter) {
		int pageNumber = offset / limit;
		Pageable page = new PageRequest(pageNumber, limit, new Sort(ASC, "firstName"));
		Page<User> users;
		if (!isEmpty(filter)) {
			String[] nameValue = filter.split("=");
			users = userRepository.findAll(isEqualTo(nameValue[0], nameValue[1]), page);
		} else {
			users = userRepository.findAll(page);
		}
		return new UserListResponse(users.getTotalElements(), users.getContent());
	}
}

package com.transformuk.hee.tis.auth.service;

import com.google.common.base.CharMatcher;
import com.transformuk.hee.tis.auth.model.JwtAuthToken;
import com.transformuk.hee.tis.auth.model.User;
import com.transformuk.hee.tis.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static com.transformuk.hee.tis.auth.filters.UserSpecification.active;
import static com.transformuk.hee.tis.auth.filters.UserSpecification.withDBCs;
import static com.transformuk.hee.tis.auth.filters.UserSpecification.withPermissions;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.data.domain.Sort.Direction.ASC;

/**
 * Service for user login/logout process and getting user's data
 */
@Service
@Transactional(readOnly = true)
public class LoginService {
	
	private static final Logger LOG = getLogger(LoginService.class);

	private JsonParser jsonParser = JsonParserFactory.getJsonParser();
	
	private final UserRepository userRepository;

	public LoginService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * @param token jwtAuthToken
	 * @return {@link User} User associated with given unique user name
	 */
	public User getUserByToken(String token) {
		JwtAuthToken jwtAuthToken = decode(token);
		User user = userRepository.findByActive(jwtAuthToken.getUsername());
		if (user == null) {
			throw new EntityNotFoundException(format("User with username %s either not found or not active", 
					jwtAuthToken.getUsername()));
		}
		return user;
	}

	/**
	 * Returns all active users by search criteria
	 *
	 * @param designatedBodyCodes the designatedBodyCode to use
	 * @param offset the result number to start from
	 * @param limit  the page size
	 * @param designatedBodyCodes
	 *@param permissions the permissions to use  @return {@link List<User>} list of users
	 */
	public Page<User> getUsers(int offset, int limit, Set<String> designatedBodyCodes, String permissions) {
		Specifications<User> spec = Specifications.where(active()).and(withDBCs(designatedBodyCodes));
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

	private JwtAuthToken decode(String token) {
		Jwt jwt = JwtHelper.decode(token);
		Map<String, Object> claims = jsonParser.parseMap(jwt.getClaims());
		String userName = getString(claims, "preferred_username");
		String cn = getString(claims, "name");
		Map<String, Object> accessMap = (Map<String, Object>) claims.get("realm_access");
		String rawRolesString = getString(accessMap, "roles");
		String rolesString = CharMatcher.anyOf("[]").removeFrom(rawRolesString);
		Set<String> roles = Pattern.compile(",").splitAsStream(rolesString)
				.map(s -> s.trim())
				.collect(toSet());

		JwtAuthToken profile = new JwtAuthToken();
		profile.setUsername(userName);
		profile.setCn(asList(cn));
		profile.setRoles(roles);
		return profile;
	}

	private String getString(Map<String, Object> claims, String name) {
		Object v = claims.get(name);
		return v != null ? String.valueOf(v) : null;
	}
}

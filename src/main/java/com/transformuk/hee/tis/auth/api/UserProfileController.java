package com.transformuk.hee.tis.auth.api;


import com.google.common.base.CharMatcher;
import com.transformuk.hee.tis.auth.exception.UserNotFoundException;
import com.transformuk.hee.tis.auth.model.UserDetails;
import com.transformuk.hee.tis.auth.model.User;
import com.transformuk.hee.tis.auth.model.UserListResponse;
import com.transformuk.hee.tis.auth.model.JwtAuthToken;
import com.transformuk.hee.tis.auth.service.LoginService;
import com.transformuk.hee.tis.auth.service.PermissionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.hateoas.Resource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static com.google.common.collect.Iterables.getFirst;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@Api(value = "/identity", description = "API to get user profile with permissions")
@RequestMapping("/identity")
public class UserProfileController {
	private static final Logger LOG = getLogger(UserProfileController.class);


	private PermissionsService permissionsService;
	private LoginService loginService;
	private AuditEventRepository auditEventRepository;

	private JsonParser jsonParser = JsonParserFactory.getJsonParser();

	@Autowired
	public UserProfileController(PermissionsService permissionsService, LoginService loginService,
								 AuditEventRepository auditEventRepository) {
		this.permissionsService = permissionsService;
		this.loginService = loginService;
		this.auditEventRepository = auditEventRepository;
	}

	@ApiOperation(value = "profile()", notes = "gets user profile with permissions", response = UserDetails.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns user profile successfully", response = UserDetails.class)
	})
	@CrossOrigin
	@RequestMapping(path = "/userinfo", method = GET, produces = APPLICATION_JSON_VALUE)
	public UserDetails profile(@RequestHeader(value = "OIDC_access_token") String token) throws
			UserNotFoundException {

		JwtAuthToken jwtAuthToken = decode(token);
		User user = loginService.getUserByUserName(jwtAuthToken.getUsername());
		UserDetails userDetails = toLoginResponse(jwtAuthToken, user);
		return userDetails;
	}

	@ApiOperation(value = "getUser()", notes = "GET a user with given userName", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Got user successfully", response = User.class)
	})
	@CrossOrigin
	@RequestMapping(path = "/user", method = GET, produces = APPLICATION_JSON_VALUE)
	public Resource<User> getUser(@RequestHeader(value = "Username") String userName) throws UserNotFoundException {
		User user = loginService.getUserByUserName(userName);
		Resource<User> resource = new Resource<>(user);
		resource.add(linkTo(methodOn(UserProfileController.class).getUser(userName)).withSelfRel());
		return resource;
	}


	@ApiOperation(value = "getUsers(). http://localhost:8084/users?offset=0&limit=1&filter=firstName=James" +
			"Possible Filters:userName,firstName,lastName,gmcId,designatedBodyCode,phoneNumber", notes = "GETs users",
			response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User's list returned", response = User.class)
	})
	@CrossOrigin
	@RequestMapping(path = "/users", method = GET, produces = APPLICATION_JSON_VALUE)
	public Resource<UserListResponse> getUsers(@RequestParam(value = "offset") int offset,
											   @RequestParam(value = "limit") int limit,
											   @RequestParam(value = "filter", required = false) String filter) {
		UserListResponse response = loginService.getUsers(offset, limit, filter);
		Resource<UserListResponse> resource = new Resource<>(response);
		resource.add(linkTo(methodOn(UserProfileController.class).getUsers(offset, limit, filter)).withSelfRel());
		return resource;
	}

	private JwtAuthToken decode(String token) {
		Jwt jwt = JwtHelper.decode(token);
		Map<String, Object> claims = jsonParser.parseMap(jwt.getClaims());
		String userName = getString(claims, "preferred_username");
		String cn = getString(claims, "name");
		String rawRolesString = getString(claims, "roles"); // list of roles in the form [role1, role2]
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

	private UserDetails toLoginResponse(JwtAuthToken jwtAuthToken, User user) {
		UserDetails userDetails = new UserDetails();
		userDetails.setUserName(jwtAuthToken.getUsername());
		userDetails.setFullName(getFirst(jwtAuthToken.getCn(), null));
		userDetails.setRoles(jwtAuthToken.getRoles());
		userDetails.setPermissions(permissionsService.getPermissions(userDetails.getRoles()));
		userDetails.setDesignatedBodyCode(user.getDesignatedBodyCode());
		userDetails.setPhoneNumber(user.getPhoneNumber());
		userDetails.setGmcId(user.getGmcId());
		userDetails.setFirstName(user.getFirstName());
		userDetails.setLastName(user.getLastName());
		return userDetails;
	}

	private String getString(Map<String, Object> claims, String name) {
		Object v = claims.get(name);
		return v != null ? String.valueOf(v) : null;
	}
}

package com.transformuk.hee.tis.auth.api;


import com.google.common.base.CharMatcher;
import com.transformuk.hee.tis.auth.model.*;
import com.transformuk.hee.tis.auth.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Resource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@Api(value = "/api", description = "API to get user profile with permissions")
@RequestMapping("/api")
public class UserProfileController {
	private static final Logger LOG = getLogger(UserProfileController.class);


	private LoginService loginService;
	private AuditEventRepository auditEventRepository;

	private JsonParser jsonParser = JsonParserFactory.getJsonParser();

	@Autowired
	public UserProfileController(LoginService loginService,
								 AuditEventRepository auditEventRepository) {
		this.loginService = loginService;
		this.auditEventRepository = auditEventRepository;
	}

	@ApiOperation(value = "Gets user profile", notes = "gets user profile with permissions", response = UserDetails
			.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns user profile successfully", response = UserDetails.class)
	})
	@CrossOrigin
	@RequestMapping(path = "/userinfo", method = GET, produces = APPLICATION_JSON_VALUE)
	public UserDetails profile(@RequestHeader(value = "OIDC_access_token") String token)  {

		JwtAuthToken jwtAuthToken = decode(token);
		User user = loginService.getUserByUserName(jwtAuthToken.getUsername());
		UserDetails userDetails = toUserDetails(user);
		return userDetails;
	}

	@ApiOperation(value = "Returns list of users with pagination",
			notes = "http://localhost:8084/users?offset=0&limit=10&designatedBodyCode=DBC&permissions=comma separated values",
			response = UserListResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User's list returned", response = UserListResponse.class)
	})
	@CrossOrigin
	@RequestMapping(path = "/users", method = GET, produces = APPLICATION_JSON_VALUE)
	public Resource<UserListResponse> getUsers(@RequestParam(value = "offset") int offset,
											   @RequestParam(value = "limit", defaultValue = "-1") int limit,
											   @RequestParam(value = "designatedBodyCode") String designatedBodyCode,
											   @RequestParam(value = "permissions", required = false) String permissions) {


		Page<User> page = loginService.getUsers(offset, limit, designatedBodyCode, permissions);
		UserListResponse response = toUserListResponse(page);
		Resource<UserListResponse> resource = new Resource<>(response);
		resource.add(linkTo(methodOn(UserProfileController.class).getUsers(offset, limit, designatedBodyCode, permissions))
				.withSelfRel());
		return resource;
	}

	@ApiOperation(value = "Gets RevalidationOfficer deatils", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Got user successfully", response = User.class)
	})
	@CrossOrigin
	@RequestMapping(path = "/ro-user/{designatedBodyCode}", method = GET, produces = APPLICATION_JSON_VALUE)
	public UserDetails getROByDesignatedBodyCode(@PathVariable(value = "designatedBodyCode") String designatedBodyCode) {
		User user = loginService.getRVOfficer(designatedBodyCode);
		return toUserDetails(user);
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

	private UserDetails toUserDetails(User user) {
		UserDetails userDetails = new UserDetails();
		userDetails.setUserName(user.getName());
		userDetails.setFullName(user.getFirstName() + " " + user.getLastName());
		Set<Role> roles = user.getRoles();
		userDetails.setRoles(roles.stream().map(Role::getName).collect(toSet()));
		userDetails.setPermissions(getPermissions(roles));
		userDetails.setDesignatedBodyCode(user.getDesignatedBodyCode());
		userDetails.setPhoneNumber(user.getPhoneNumber());
		userDetails.setGmcId(user.getGmcId());
		userDetails.setFirstName(user.getFirstName());
		userDetails.setLastName(user.getLastName());
		userDetails.setEmailAddress(user.getEmailAddress());
		return userDetails;
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

	private String getString(Map<String, Object> claims, String name) {
		Object v = claims.get(name);
		return v != null ? String.valueOf(v) : null;
	}

	private UserListResponse toUserListResponse(Page<User> users) {
		return new UserListResponse(users.getTotalElements(), toUserInfoList(users.getContent()));
	}

	private List<UserInfoResponse> toUserInfoList(List<User> users) {
		return users.stream()
				.map(this::toUserInfo)
				.collect(toList());
	}

	private UserInfoResponse toUserInfo(User user) {
		UserInfoResponse userInfo = new UserInfoResponse();
		userInfo.setDesignatedBodyCode(user.getDesignatedBodyCode());
		userInfo.setEmailAddress(user.getEmailAddress());
		userInfo.setFirstName(user.getFirstName());
		userInfo.setLastName(user.getLastName());
		userInfo.setName(user.getName());
		userInfo.setPhoneNumber(user.getPhoneNumber());
		userInfo.setGmcId(user.getGmcId());
		userInfo.setFullName(user.getFirstName() + " " + user.getLastName());
		return userInfo;
	}

}

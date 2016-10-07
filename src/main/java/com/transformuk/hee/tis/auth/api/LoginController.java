package com.transformuk.hee.tis.auth.api;


import com.transformuk.hee.tis.auth.exception.UserNotFoundException;
import com.transformuk.hee.tis.auth.model.LoginResponse;
import com.transformuk.hee.tis.auth.model.User;
import com.transformuk.hee.tis.auth.model.UserListResponse;
import com.transformuk.hee.tis.auth.model.UserProfile;
import com.transformuk.hee.tis.auth.service.LoginService;
import com.transformuk.hee.tis.auth.service.PermissionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.getFirst;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@Api(value = "/identity", description = "API to invoke OpenAm login and logout")
@RequestMapping("/identity")
public class LoginController {
	private static final Logger LOG = getLogger(LoginController.class);

	// OpenAM paths
	private static final String LOGIN_PATH = "openam/json/authenticate";
	private static final String LOGOUT_PATH = "openam/json/sessions/?_action=logout";
	private static final String GET_USER_PATH = "openam/json/users/{userName}";

	// OpenAM header constants
	private static final String IPLANET_DIRECTORY_PRO = "iplanetDirectoryPro";
	private static final String X_OPEN_AM_USERNAME = "X-OpenAM-Username";
	private static final String X_OPEN_AM_PASSWORD = "X-OpenAM-Password";

	private final RestTemplate restTemplate;
	private final String openAMHost;

	private PermissionsService permissionsService;
	private LoginService loginService;

	@Autowired
	public LoginController(@Value("${openAM.host}") String openAMHost, RestTemplate restTemplate,
	                       PermissionsService permissionsService, LoginService loginService) {
		this.openAMHost = openAMHost;
		this.restTemplate = restTemplate;
		this.permissionsService = permissionsService;
		this.loginService = loginService;
	}

	@ApiOperation(value = "authenticate()", notes = "authenticates user", response = LoginResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Authenticated user successfully", response = LoginResponse.class)
	})
	@CrossOrigin
	@RequestMapping(path = "/authenticate", method = POST, produces = APPLICATION_JSON_VALUE)
	public LoginResponse authenticate(@RequestHeader(value = "X-TIS-Username") String userName,
				 @RequestHeader(value = "X-TIS-Password") String password) throws UserNotFoundException {
		String tokenId = getToken(userName, password);
		UserProfile userProfile = getUserProfile(userName, tokenId);
		User user = loginService.getUserByUserName(userName);
		LoginResponse loginResponse = toLoginResponse(userProfile, tokenId, user);
		loginService.logEvent(new AuditEvent(userName, "LoginEvent"));
		return loginResponse;
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
		resource.add(linkTo(methodOn(LoginController.class).getUser(userName)).withSelfRel());
		return resource;
	}


	@ApiOperation(value = "getUsers(). http://localhost:8084/users?offset=0&limit=1&filter=firstName=James" +
			"Possible Filters:userName,firstName,lastName,gmcId,designatedBodyCode,phoneNumber", notes = "GETs users", response = User.class)
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
		resource.add(linkTo(methodOn(LoginController.class).getUsers(offset, limit, filter)).withSelfRel());
		return resource;
	}

	@ApiOperation(value = "logout()", notes = "logouts a user session",
			response = Void.class, responseContainer = "Created")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully logged out",
					response = Void.class)})
	@CrossOrigin
	@RequestMapping(path = "/logout", method = POST)
	public void logout(@RequestHeader(value = "X-TIS-TokenId") String tokenId) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(IPLANET_DIRECTORY_PRO, tokenId);
		HttpEntity<Void> request = new HttpEntity<>(headers);

		String logoutUrl = openAMHost + LOGOUT_PATH;
		restTemplate.postForObject(logoutUrl, request, String.class);
		loginService.logEvent(new AuditEvent(tokenId, "LogoutEvent"));
	}

	private String getToken(String userName, String password) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(X_OPEN_AM_USERNAME, userName);
		headers.add(X_OPEN_AM_PASSWORD, password);
		HttpEntity<Void> request = new HttpEntity<>(headers);

		String loginUrl = openAMHost + LOGIN_PATH;
		return restTemplate.postForObject(loginUrl, request, Map.class).get("tokenId").toString();
	}

	private UserProfile getUserProfile(String userName, String tokenId) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(IPLANET_DIRECTORY_PRO, tokenId);
		HttpEntity<Void> request = new HttpEntity<>(headers);

		String userUrl = openAMHost + GET_USER_PATH;
		ResponseEntity<UserProfile> responseEntity = restTemplate.
				exchange(userUrl, HttpMethod.GET, request, UserProfile.class, userName);
		return responseEntity.getBody();
	}

	private LoginResponse toLoginResponse(UserProfile userProfile, String tokenId, User user) {
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setUserName(userProfile.getUsername());
		loginResponse.setFullName(getFirst(userProfile.getCn(), null));
		loginResponse.setRoles(getRoles(userProfile.getIsMemberOf()));
		loginResponse.setPermissions(permissionsService.getPermissions(loginResponse.getRoles()));
		loginResponse.setToken(tokenId);
		loginResponse.setDesignatedBodyCode(user.getDesignatedBodyCode());
		loginResponse.setPhoneNumber(user.getPhoneNumber());
		loginResponse.setGmcId(user.getGmcId());
		loginResponse.setFirstName(user.getFirstName());
		loginResponse.setLastName(user.getLastName());
		return loginResponse;
	}

	/**
	 * Parses the memberOf Strings returned by the OpenAM REST interface into role names
	 * Plese note OpenAM returns strings in the following format:
	 * "isMemberOf": [
	 * "cn=Trainee,ou=groups,dc=openam,dc=forgerock,dc=org",
	 * "cn=RVAdmin,ou=groups,dc=openam,dc=forgerock,dc=org",
	 * "cn=RVOfficer,ou=groups,dc=openam,dc=forgerock,dc=org"
	 * ]
	 *
	 * @param memberOf the list of strings returned by the OpenAM REST interface
	 * @return the role names found
	 */
	private Set<String> getRoles(List<String> memberOf) {
		return memberOf.stream().map(m -> m.split(",")[0].replace("cn=", "")).collect(Collectors.toSet());
	}
}

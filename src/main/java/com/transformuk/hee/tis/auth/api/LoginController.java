package com.transformuk.hee.tis.auth.api;


import com.transformuk.hee.tis.auth.model.LoginResponse;
import com.transformuk.hee.tis.auth.model.UserProfile;
import com.transformuk.hee.tis.auth.service.PermissionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.getFirst;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@Api(value = "/identity", description = "API to invoke OpenAm login and logout")
@RequestMapping("/identity")
public class LoginController {

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

	@Autowired
	public LoginController(@Value("${openAM.host}") String openAMHost, RestTemplate restTemplate,
	                       PermissionsService permissionsService) {
		this.openAMHost = openAMHost;
		this.restTemplate = restTemplate;
		this.permissionsService = permissionsService;
	}

	@ApiOperation(value = "authenticate()", notes = "authenticates user", response = LoginResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Authenticated user successfully", response = LoginResponse.class)
	})
	@CrossOrigin
	@RequestMapping(path = "/authenticate", method = POST, produces = APPLICATION_JSON_VALUE)
	public LoginResponse authenticate(@RequestHeader(value = "X-TIS-Username") String userName,
	                                  @RequestHeader(value = "X-TIS-Password") String password) {
		String tokenId = getToken(userName, password);
		UserProfile userProfile = getUserProfile(userName, tokenId);
		return toLoginResponse(userProfile, tokenId);
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
	}

	private String getToken(String userName, String password) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(X_OPEN_AM_USERNAME, userName);
		headers.add(X_OPEN_AM_PASSWORD, password);
		HttpEntity<Void> request = new HttpEntity<>(headers);

		String loginUrl = openAMHost + LOGIN_PATH;
		return restTemplate.postForObject(loginUrl, request, LoginResponse.class).getTokenId();
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

	private LoginResponse toLoginResponse(UserProfile userProfile, String tokenId) {
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setUserName(userProfile.getUsername());
		loginResponse.setFullName(getFirst(userProfile.getCn(), null));
		loginResponse.setRoles(getRoles(userProfile.getIsMemberOf()));
		loginResponse.setPermissions(permissionsService.getPermissions(loginResponse.getRoles()));
		loginResponse.setTokenId(tokenId);
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

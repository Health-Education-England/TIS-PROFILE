package com.transformuk.hee.tis.auth.api;

import com.transformuk.hee.tis.auth.assembler.UserProfileAssembler;
import com.transformuk.hee.tis.auth.model.User;
import com.transformuk.hee.tis.auth.model.UserDetails;
import com.transformuk.hee.tis.auth.model.UserInfoResponse;
import com.transformuk.hee.tis.auth.model.UserListResponse;
import com.transformuk.hee.tis.auth.service.LoginService;
import com.transformuk.hee.tis.security.model.UserProfile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
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

	private final LoginService loginService;
	private final UserProfileAssembler assembler;
	private final AuditEventRepository auditEventRepository;

	@Autowired
	public UserProfileController(LoginService loginService,
								 UserProfileAssembler assembler, AuditEventRepository auditEventRepository) {
		this.loginService = loginService;
		this.assembler = assembler;
		this.auditEventRepository = auditEventRepository;
	}

	@ApiOperation(value = "Gets user profile", notes = "gets user profile with permissions", response = UserDetails
			.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns user profile successfully", response = UserDetails.class)
	})
	@CrossOrigin
	@RequestMapping(path = "/userinfo", method = GET, produces = APPLICATION_JSON_VALUE)
	public UserProfile profile(@RequestHeader(value = "OIDC_access_token") String token) {
		User user = loginService.getUserByToken(token);
		return assembler.toUserProfile(user);
	}

	@ApiOperation(value = "Returns list of users with pagination",
			notes = "http://localhost:8084/users?offset=0&limit=10&designatedBodyCode=DBC&permissions=comma separated values",
			response = UserListResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User's list returned", response = UserListResponse.class)
	})
	@CrossOrigin
	@RequestMapping(path = "/users", method = GET, produces = APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('profile:get:users')")
	public Resource<UserListResponse> getUsers(@RequestParam(value = "offset") int offset,
											   @RequestParam(value = "limit") int limit,
											   @RequestParam(value = "designatedBodyCode") Set<String> designatedBodyCodes,
											   @RequestParam(value = "permissions", required = false) String permissions) {
		Page<User> page = loginService.getUsers(offset, limit, designatedBodyCodes, permissions);
		UserListResponse response = toUserListResponse(page);
		Resource<UserListResponse> resource = new Resource<>(response);
		resource.add(linkTo(methodOn(UserProfileController.class).getUsers(offset, limit, designatedBodyCodes, permissions))
				.withSelfRel());
		return resource;
	}

	@ApiOperation(value = "Gets RevalidationOfficer deatils", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Got user successfully", response = User.class)
	})
	@CrossOrigin
	@RequestMapping(path = "/users/ro-user/{designatedBodyCode}", method = GET, produces = APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('profile:get:ro:user')")
	public UserProfile getROByDesignatedBodyCode(@PathVariable(value = "designatedBodyCode") String designatedBodyCode) {
		User user = loginService.getRVOfficer(designatedBodyCode);
		return assembler.toUserProfile(user);
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
		userInfo.setDesignatedBodyCodes(user.getDesignatedBodyCodes());
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

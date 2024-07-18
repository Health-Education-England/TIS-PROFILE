package com.transformuk.hee.tis.profile.web.rest;

import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.assembler.UserProfileAssembler;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.dto.UserInfoResponse;
import com.transformuk.hee.tis.profile.dto.UserListResponse;
import com.transformuk.hee.tis.profile.service.LoginService;
import com.transformuk.hee.tis.security.model.UserProfile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = ProfileApp.SERVICE_NAME, description = "API to get user profile with permissions")
@RequestMapping("/api")
public class UserProfileController {

  private static final Logger LOG = getLogger(UserProfileController.class);

  private final LoginService loginService;
  private final UserProfileAssembler assembler;

  @Autowired
  public UserProfileController(LoginService loginService, UserProfileAssembler assembler) {
    this.loginService = loginService;
    this.assembler = assembler;
  }

  @ApiOperation(value = "Gets user profile", notes = "gets user profile with permissions", response = UserProfile
      .class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Returns user profile successfully", response = UserProfile.class)
  })
  @CrossOrigin
  @RequestMapping(path = "/userinfo", method = GET, produces = APPLICATION_JSON_VALUE)
  public UserProfile profile(@RequestHeader(value = "OIDC_access_token") String token) {
    HeeUser user = loginService.getUserByToken(token);
    return assembler.toUserProfile(user);
  }

  /**
   * This endpoint is being marked as deprecated now as we should not be creating/updating users
   * here as we now have the User Management service
   * <p>
   * So all this does for now is just return the user based on the token provided
   *
   * Both token headers are included to support both Keycloak and Cognito simultaneously.
   * TODO: remove OIDC_access_token once fully migrated.
   *
   * @param oidcAccessToken
   * @param authorizationToken
   * @return
   */
  @CrossOrigin
  @RequestMapping(path = "/userupdate", method = GET, produces = APPLICATION_JSON_VALUE)
  @Deprecated
  public ResponseEntity<UserProfile> amendUser(
          @RequestHeader(value = "OIDC_access_token", required = false) String oidcAccessToken,
          @RequestHeader(value = "Authorization", required = false) String authorizationToken) {
    if (oidcAccessToken == null && authorizationToken == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    String token = authorizationToken != null ? authorizationToken : oidcAccessToken;
    HeeUser user = loginService.getUserByToken(token);
    UserProfile userProfile = assembler.toUserProfile(user);
    return new ResponseEntity<>(userProfile, HttpStatus.OK);
  }

  @ApiOperation(value = "Returns list of users by exact matching of given designatedBodyCodes",
      notes = "http://localhost:8084/users?designatedBodyCode=DBC&permissions=comma separated values",
      response = UserListResponse.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "User list returned", response = UserListResponse.class)
  })
  @CrossOrigin
  @RequestMapping(path = "/users", method = GET, produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('profile:get:users')")
  public Resource<UserListResponse> getUsers(
      @RequestParam(value = "designatedBodyCode") Set<String> designatedBodyCodes,
      @RequestParam(value = "permissions", required = false) String permissions) {
    List<HeeUser> users = loginService.getUsers(designatedBodyCodes, permissions);
    UserListResponse response = toUserListResponse(users);
    Resource<UserListResponse> resource = new Resource<>(response);
    resource.add(
        linkTo(methodOn(UserProfileController.class).getUsers(designatedBodyCodes, permissions))
            .withSelfRel());
    return resource;
  }

  @ApiOperation(value = "Gets RevalidationOfficer deatils", response = UserProfile.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Got user successfully", response = UserProfile.class)
  })
  @CrossOrigin
  @RequestMapping(path = "/users/ro-user/{designatedBodyCode}", method = GET, produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('profile:get:ro:user')")
  public UserProfile getROByDesignatedBodyCode(
      @PathVariable(value = "designatedBodyCode") String designatedBodyCode) {
    LOG.info("getROByDesignatedBodyCode() -> {}", designatedBodyCode);
    HeeUser user = loginService.getRVOfficer(designatedBodyCode);
    LOG.info("After getROByDesignatedBodyCode() -> {}", user);
    return assembler.toUserProfile(user);
  }

  private UserListResponse toUserListResponse(List<HeeUser> users) {
    return new UserListResponse(users.size(), toUserInfoList(users));
  }

  private List<UserInfoResponse> toUserInfoList(List<HeeUser> users) {
    return users.stream()
        .map(this::toUserInfo)
        .collect(toList());
  }

  private UserInfoResponse toUserInfo(HeeUser user) {
    UserInfoResponse userInfo = new UserInfoResponse();
    BeanUtils.copyProperties(user, userInfo);
    userInfo.setFullName(user.getFirstName() + " " + user.getLastName());
    return userInfo;
  }

}

package com.transformuk.hee.tis.profile.web.rest;

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

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

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
  public ResponseEntity<UserProfile> profile(@RequestHeader(value = "OIDC_access_token") String token) {
    HeeUser user;
    HttpStatus httpStatus;
    try {
      user = loginService.getUserByToken(token);
      httpStatus = HttpStatus.OK;
    } catch (EntityNotFoundException enfe) {
      LOG.debug("User not found using token, creating new user");
      user = loginService.createUserByToken(token);
      httpStatus = HttpStatus.CREATED;
    }
    UserProfile userProfile = assembler.toUserProfile(user);
    return new ResponseEntity<>(userProfile, httpStatus);
  }

  @ApiOperation(value = "Creates or Updates user", notes = "updates user roles and groups in auth db and returns " +
      "updated user. Creates if it doesn't yet exist"
      , response = UserProfile.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Returns updated user profile successfully", response = UserProfile.class)
  })
  @CrossOrigin
  @RequestMapping(path = "/userupdate", method = GET, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UserProfile> amendUser(@RequestHeader(value = "OIDC_access_token") String token) {
    HeeUser user;
    HttpStatus httpStatus;
    try {
      loginService.getUserByToken(token);
      user = loginService.updateUserByToken(token);
      httpStatus = HttpStatus.OK;
    } catch (EntityNotFoundException enfe) {
      LOG.debug("User not found using token, creating new user");
      user = loginService.createUserByToken(token);
      httpStatus = HttpStatus.CREATED;
    }
    UserProfile userProfile = assembler.toUserProfile(user);
    return new ResponseEntity<>(userProfile, httpStatus);
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
  public Resource<UserListResponse> getUsers(@RequestParam(value = "designatedBodyCode") Set<String> designatedBodyCodes,
                                             @RequestParam(value = "permissions", required = false) String permissions) {
    List<HeeUser> users = loginService.getUsers(designatedBodyCodes, permissions);
    UserListResponse response = toUserListResponse(users);
    Resource<UserListResponse> resource = new Resource<>(response);
    resource.add(linkTo(methodOn(UserProfileController.class).getUsers(designatedBodyCodes, permissions))
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
  public UserProfile getROByDesignatedBodyCode(@PathVariable(value = "designatedBodyCode") String designatedBodyCode) {
    HeeUser user = loginService.getRVOfficer(designatedBodyCode);
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

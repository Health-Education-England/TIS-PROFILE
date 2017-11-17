package com.transformuk.hee.tis.profile.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.dto.JwtAuthToken;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.profile.repository.RoleRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Service for user login/logout process and getting user's data
 */
@Service
@Transactional(readOnly = true)
public class LoginService {

  private static final Logger LOG = getLogger(LoginService.class);
  private static final String GIVEN_NAME_FIELD = "given_name";
  private static final String FAMILY_NAME_FIELD = "family_name";
  private static final String PREFERRED_USERNAME_FIELD = "preferred_username";
  private static final String EMAIL_FIELD = "email";
  private static final String GMC_ID_FIELD = "gmc_id";
  private static final String REALM_ACCESS_FIELD = "realm_access";
  private static final String ROLES_FIELD = "roles";
  private static final String DBC_ATTRIBUTE = "DBC";
  private static final String GMC_ID_ATTRIBUTE = "GMC_ID";

  private final HeeUserRepository userRepository;
  private final RoleRepository roleRepository;
  private final KeycloakAdminClientService keycloakAdminClientService;

  private JsonParser jsonParser = JsonParserFactory.getJsonParser();

  public LoginService(HeeUserRepository userRepository, RoleRepository roleRepository,
                      KeycloakAdminClientService keycloakAdminClientService) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.keycloakAdminClientService = keycloakAdminClientService;
  }

  /**
   * @param token jwtAuthToken
   * @return {@link HeeUser} User associated with given unique user name
   */
  public HeeUser getUserByToken(String token) {
    JwtAuthToken jwtAuthToken = decode(token);
    HeeUser user = userRepository.findByActive(jwtAuthToken.getUsername());
    if (user == null) {
      throw new EntityNotFoundException(format("User with username %s either not found or not active",
          jwtAuthToken.getUsername()));
    }
    return user;
  }

  /**
   * Returns all active users by exact matching with given designatedBodyCodes and permissions if any
   *
   * @param designatedBodyCodes the designatedBodyCode to use
   * @param designatedBodyCodes
   * @param permissions         the permissions to use  @return {@link List<HeeUser>} list of users
   */
  public List<HeeUser> getUsers(Set<String> designatedBodyCodes, String permissions) {
    List<String> sortedDesignatedBodyCodes = Lists.newArrayList(designatedBodyCodes);
    Collections.sort(sortedDesignatedBodyCodes);
    String designatedBodyCodesValue = StringUtils.join(sortedDesignatedBodyCodes, ",");
    List<String> permissionList = Lists.newArrayList();
    if (permissions != null) {
      permissionList.addAll(asList(permissions.split(",")));
      return userRepository.findDistinctByExactDesignatedBodyCodesAndPermissions(designatedBodyCodesValue, permissionList);
    }
    return userRepository.findDistinctByExactDesignatedBodyCodes(
        designatedBodyCodesValue);
  }

  /**
   * Gets RevalidationOfficer details by designatedBodyCode
   *
   * @param designatedBodyCode
   * @return {@link HeeUser}
   */
  public HeeUser getRVOfficer(String designatedBodyCode) {
    return userRepository.findRVOfficerByDesignatedBodyCode(designatedBodyCode);
  }

  /**
   * Create a new user in the system using all the details available from the token
   *
   * @param token The OIDC token received from the browser
   * @return
   */
  @Transactional
  public HeeUser createUserByToken(String token) {
    Jwt jwt = JwtHelper.decode(token);
    Map<String, Object> claims = jsonParser.parseMap(jwt.getClaims());
    String firstName = getString(claims, GIVEN_NAME_FIELD);
    String surname = getString(claims, FAMILY_NAME_FIELD);
    String username = getString(claims, PREFERRED_USERNAME_FIELD);
    String email = getString(claims, EMAIL_FIELD);

    Map<String, Map> realmAccessMap = (Map<String, Map>) claims.get(REALM_ACCESS_FIELD);
    List<Role> foundRoles = null;
    if(MapUtils.isNotEmpty(realmAccessMap)){
      List<String> roles = (List<String>) realmAccessMap.get(ROLES_FIELD);
      foundRoles = roleRepository.findByNameIn(Sets.newHashSet(roles));
    }

    HeeUser newUser = new HeeUser()
        .active(true)
        .emailAddress(StringUtils.defaultString(email, StringUtils.EMPTY))
        .firstName(firstName)
        .lastName(StringUtils.defaultString(surname, StringUtils.EMPTY))
        .name(StringUtils.defaultString(username, StringUtils.EMPTY));
    newUser.setRoles(Sets.newHashSet(foundRoles));

    Map<String, List<String>> userAttributes = keycloakAdminClientService.getUserAttributes(newUser);
    List<String> dbcList = null;
    String gmcId = null;

    if(MapUtils.isNotEmpty(userAttributes)){
      List<String> dbcAttribute = userAttributes.get(DBC_ATTRIBUTE);
      if(CollectionUtils.isNotEmpty(dbcAttribute)){
        dbcList = Lists.newArrayList(StringUtils.split(dbcAttribute.get(0), ","));
      }
      List<String> gmcIdList = userAttributes.get(GMC_ID_ATTRIBUTE);
      if(CollectionUtils.isNotEmpty(gmcIdList)){
        gmcId = gmcIdList.get(0);
      }
    }

    newUser.gmcId(gmcId)
    .setDesignatedBodyCodes(Sets.newHashSet(dbcList));

    newUser = userRepository.save(newUser);

    return newUser;
  }

  private JwtAuthToken decode(String token) {
    Jwt jwt = JwtHelper.decode(token);
    Map<String, Object> claims = jsonParser.parseMap(jwt.getClaims());
    String userName = getString(claims, "preferred_username");
    String cn = getString(claims, "name");
    JwtAuthToken profile = new JwtAuthToken();
    profile.setUsername(userName);
    profile.setCn(asList(cn));
    return profile;
  }

  private String getString(Map<String, Object> claims, String name) {
    Object v = claims.get(name);
    return v != null ? String.valueOf(v) : null;
  }
}

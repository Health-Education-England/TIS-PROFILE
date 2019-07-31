package com.transformuk.hee.tis.profile.service;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.dto.JwtAuthToken;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for user login/logout process and getting user's data
 */
@Service
@Transactional(readOnly = true)
public class LoginService {

  private static final Logger LOG = getLogger(LoginService.class);
  private final HeeUserRepository userRepository;

  private JsonParser jsonParser = JsonParserFactory.getJsonParser();

  public LoginService(HeeUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * @param token jwtAuthToken
   * @return {@link HeeUser} User associated with given unique user name
   */
  public HeeUser getUserByToken(String token) {
    JwtAuthToken jwtAuthToken = decode(token);
    HeeUser user = userRepository.findByActive(jwtAuthToken.getUsername());
    if (user == null) {
      throw new EntityNotFoundException(
          format("User with username %s either not found or not active",
              jwtAuthToken.getUsername()));
    }
    return user;
  }

  /**
   * Returns all active users by exact matching with given designatedBodyCodes and permissions if
   * any
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
      return userRepository
          .findDistinctByExactDesignatedBodyCodesAndPermissions(designatedBodyCodesValue,
              permissionList);
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
    LOG.info("getRVOfficer()-> {}", designatedBodyCode);
    HeeUser rvOfficer = new HeeUser();
    List<HeeUser> heeUsers = userRepository.findRVOfficerByDesignatedBodyCode(designatedBodyCode);
    LOG.info("Found list in getRVOfficer()-> {}", heeUsers.size());
    if (CollectionUtils.isNotEmpty(heeUsers)) {
      rvOfficer = heeUsers.iterator().next();
    }
    LOG.info("Found user in getRVOfficer()-> {}", rvOfficer);
    return rvOfficer;
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

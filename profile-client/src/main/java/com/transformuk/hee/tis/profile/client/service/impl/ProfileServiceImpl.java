package com.transformuk.hee.tis.profile.client.service.impl;

import com.google.common.collect.Maps;
import com.transformuk.hee.tis.client.impl.AbstractClientService;
import com.transformuk.hee.tis.profile.client.service.ProfileService;
import com.transformuk.hee.tis.profile.dto.JsonPatchDTO;
import com.transformuk.hee.tis.profile.dto.PagedTraineeIdResponse;
import com.transformuk.hee.tis.profile.dto.PermissionDTO;
import com.transformuk.hee.tis.profile.dto.RegistrationRequest;
import com.transformuk.hee.tis.profile.dto.RoleDTO;
import com.transformuk.hee.tis.profile.dto.TraineeId;
import com.transformuk.hee.tis.profile.dto.TraineeIdListResponse;
import com.transformuk.hee.tis.profile.dto.TraineeProfileDto;
import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import com.transformuk.hee.tis.security.model.UserProfile;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

/**
 * The default implementation for the profile service. Provides methods to which we use to communicate and use the
 * Tis Profile Service
 */
@Service
public class ProfileServiceImpl extends AbstractClientService implements ProfileService {

  private static final Logger LOG = getLogger(ProfileServiceImpl.class);

  private static final String PAGE_QUERY_PARAM = "page";
  private static final String SIZE_QUERY_PARAM = "size";
  private static final String TRAINEE_MAPPINGS_ENDPOINT = "/api/trainee-id/{dbc}/mappings";
  private static final String USERS_RO_USER_ENDPOINT = "/api/users/ro-user/";
  private static final String ALL_HEE_USERS_ENDPOINT = "/api/hee-users";
  private static final String USERS_ENDPOINT = "/api/users";
  private static final String TRAINEE_DBC_REGISTER_ENDPOINT = "/api/trainee-id/{designatedBodyCode}/register";
  private static final Map<Class, ParameterizedTypeReference> classToParamTypeRefMap;
  private static final String SINGLE_USER_ENDPOINT = "/api/single-hee-users";

  static {
    classToParamTypeRefMap = Maps.newHashMap();
    classToParamTypeRefMap.put(HeeUserDTO.class, new ParameterizedTypeReference<List<HeeUserDTO>>() {
    });
    classToParamTypeRefMap.put(PermissionDTO.class, new ParameterizedTypeReference<List<PermissionDTO>>() {
    });
    classToParamTypeRefMap.put(RoleDTO.class, new ParameterizedTypeReference<List<RoleDTO>>() {
    });
    classToParamTypeRefMap.put(TraineeProfileDto.class, new ParameterizedTypeReference<List<TraineeProfileDto>>() {
    });
  }

  @Autowired
  private RestTemplate profileRestTemplate;

  @Value("${profile.pagination.offset}")
  private String offset;

  @Value("${profile.pagination.limit}")
  private String limit;

  @Value("${profile.service.url}")
  private String serviceUrl;

  public ProfileServiceImpl(@Value("${profile.client.rate.limit}") double standardRequestsPerSecondLimit,
                            @Value("${profile.client.bulk.rate.limit}") double bulkRequestsPerSecondLimit) {
    super(standardRequestsPerSecondLimit, bulkRequestsPerSecondLimit);
  }

  /**
   * Return a paginated result of trainee id's for a designated body code
   *
   * @param dbc      designated body code
   * @param pageable Pageable object that defines the size and page number to return
   * @return Paged object containing trainee id's, pages and totals
   */
  public Page<TraineeId> getPagedTraineeIds(String dbc, Pageable pageable) {
    UriComponents uriComponents = fromHttpUrl(serviceUrl + TRAINEE_MAPPINGS_ENDPOINT)
        .queryParam(PAGE_QUERY_PARAM, pageable.getPageNumber())
        .queryParam(SIZE_QUERY_PARAM, pageable.getPageSize())
        .buildAndExpand(dbc);
    PagedTraineeIdResponse pagedTraineeIdResponse = profileRestTemplate.getForObject(uriComponents.encode().toUri(),
        PagedTraineeIdResponse.class);
    return new PageImpl<>(pagedTraineeIdResponse.getContent(), pageable, pagedTraineeIdResponse.getTotalElements());
  }

  /**
   * Returns trainee ids for given list of gmc doctors
   *
   * @return List of {@link TraineeId}
   */
  public List<TraineeProfileDto> getTraineeIdsForGmcNumbers(String designatedBodyCode, List<RegistrationRequest> registrationRequests) {
    HttpEntity<List<RegistrationRequest>> requestEntity = new HttpEntity<>(registrationRequests);
    String url = serviceUrl + TRAINEE_DBC_REGISTER_ENDPOINT;
    UriComponents uriComponents = fromHttpUrl(url).buildAndExpand(designatedBodyCode);
    ResponseEntity<TraineeIdListResponse> response = profileRestTemplate.exchange(uriComponents.encode().toUri(), POST,
        requestEntity, TraineeIdListResponse.class);
    return response.getBody().getTraineeIds();
  }

  /**
   * Get the user profile of revalidation officer for a designated body code
   *
   * @param designatedBodyCode the designated body code of the officer you wish to search for
   * @return The UserProfile of the revalidation officer for the designated body code
   */
  public UserProfile getRODetails(String designatedBodyCode) {
    String url = serviceUrl + USERS_RO_USER_ENDPOINT + designatedBodyCode;
    LOG.info("GetRODetails --> {}" , url);
    ResponseEntity<UserProfile> responseEntity = profileRestTemplate.getForEntity(url, UserProfile.class);
    LOG.info("After GetRODetails call");
    return responseEntity.getBody();
  }

  /**
   * Gets user details given designatedBody from Auth Service
   * (https://dev-api.transformcloud.net/profile/swagger-ui.html#!/login-controller/getUsersUsingGET)
   *
   * @param designatedBodyCodes user's designatedBody
   * @param permissions         submit_to_gmc permissions
   * @return json representation of AuthService's getUser's end point response.
   */
  public JSONObject getAllUsers(String permissions, String... designatedBodyCodes) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serviceUrl + USERS_ENDPOINT)
        .queryParam("offset", offset)
        .queryParam("limit", limit)
        .queryParam("designatedBodyCode", designatedBodyCodes)
        .queryParam("permissions", permissions);
    ResponseEntity<JSONObject> responseEntity = profileRestTemplate.getForEntity(builder.toUriString(), JSONObject.class);
    return responseEntity.getBody();
  }

  public Page<HeeUserDTO> getAllAdminUsers(Pageable pageable, String username) {
    ParameterizedTypeReference<CustomPageable<HeeUserDTO>> typeReference = getHeeUserDtoListReference();
    String searchParam = StringUtils.EMPTY;
    if (StringUtils.isNotEmpty(username)) {
      searchParam = "?search=" + username;
    }

    String pageParam = StringUtils.EMPTY;
    if(pageable != null) {
      if(StringUtils.isNotEmpty(username)) {
        pageParam = pageParam + "&";
      } else {
        pageParam = "?";
      }

      pageParam = pageParam + "page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();
    }

    ResponseEntity<CustomPageable<HeeUserDTO>> responseEntity = profileRestTemplate.exchange(serviceUrl + ALL_HEE_USERS_ENDPOINT + searchParam + pageParam,
        HttpMethod.GET, null, typeReference);
    return responseEntity.getBody();
  }

  public HeeUserDTO getSingleAdminUser(String username) {
    ParameterizedTypeReference<HeeUserDTO> typeReference = getHeeUserDtoReference();
    String url = serviceUrl + SINGLE_USER_ENDPOINT + "?username=" + username;
    ResponseEntity<HeeUserDTO> responseEntity = profileRestTemplate.exchange(url,
        HttpMethod.GET, null, typeReference);
    return responseEntity.getBody();
  }

  @Override
  public boolean deleteUser(String username) {
    ResponseEntity<Void> exchange = profileRestTemplate.exchange(serviceUrl + ALL_HEE_USERS_ENDPOINT + "/{username}", HttpMethod.DELETE, null, Void.class, username);
    return HttpStatus.OK.equals(exchange.getStatusCode());
  }


  @Override
  public List<JsonPatchDTO> getJsonPathByTableDtoNameOrderByDateAddedAsc(String endpointUrl, Class objectDTO) {
    ParameterizedTypeReference<List<JsonPatchDTO>> typeReference = getJsonPatchDtoReference();
    ResponseEntity<List<JsonPatchDTO>> response = profileRestTemplate.exchange(serviceUrl + endpointUrl + objectDTO.getSimpleName(),
        HttpMethod.GET, null, typeReference);
    return response.getBody();
  }

  private ParameterizedTypeReference<List<JsonPatchDTO>> getJsonPatchDtoReference() {
    return new ParameterizedTypeReference<List<JsonPatchDTO>>() {
    };
  }

  private ParameterizedTypeReference<CustomPageable<HeeUserDTO>> getHeeUserDtoListReference() {
    return new ParameterizedTypeReference<CustomPageable<HeeUserDTO>>() {
    };
  }

  private ParameterizedTypeReference<HeeUserDTO> getHeeUserDtoReference() {
    return new ParameterizedTypeReference<HeeUserDTO>() {
    };
  }

  @Override
  public RestTemplate getRestTemplate() {
    return profileRestTemplate;
  }

  @Override
  public String getServiceUrl() {
    return this.serviceUrl;
  }

  public void setServiceUrl(String serviceUrl) {
    this.serviceUrl = serviceUrl;
  }

  @Override
  public Map<Class, ParameterizedTypeReference> getClassToParamTypeRefMap() {
    return classToParamTypeRefMap;
  }

}

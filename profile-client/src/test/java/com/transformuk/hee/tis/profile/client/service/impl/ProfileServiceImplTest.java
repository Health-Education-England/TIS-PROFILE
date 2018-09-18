package com.transformuk.hee.tis.profile.client.service.impl;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.profile.dto.PagedTraineeIdResponse;
import com.transformuk.hee.tis.profile.dto.RegistrationRequest;
import com.transformuk.hee.tis.profile.dto.TraineeId;
import com.transformuk.hee.tis.profile.dto.TraineeIdListResponse;
import com.transformuk.hee.tis.profile.dto.TraineeProfileDto;
import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import com.transformuk.hee.tis.security.model.UserProfile;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;


@RunWith(BlockJUnit4ClassRunner.class)
public class ProfileServiceImplTest {

  private static final double RATE_LIMIT = 100d;
  private static final double BULK_RATE_LIMIT = 10d;
  private static final String DBC = "1-DGBODY";
  private static final String PROFILE_URL = "http://localhost:8888/trainee-id";
  private static final String PERMISSIONS = "revalidation:submit:on:behalf:of:ro";
  private static final String GMC_NUMBER = "1234567";
  private static final long TIS_ID = 999L;
  private static final String FIRSTNAME = "Firstname";
  private static final String FIRSTNAME1 = "ARRRRGGHH";

  @InjectMocks
  private ProfileServiceImpl profileServiceImpl;

  @Mock
  private RestTemplate profileRestTemplate;

  @Captor
  private ArgumentCaptor<String> endpointCaptor;
  @Captor
  private ArgumentCaptor<HttpEntity> httpEntityArgumentCaptor;
  @Captor
  private ArgumentCaptor<HttpMethod> httpMethodArgumentCaptor;
  @Captor
  private ArgumentCaptor<Class> classArgumentCaptor;
  @Captor
  private ArgumentCaptor<ParameterizedTypeReference<Page<HeeUserDTO>>> parameterizedTypeReferenceArgumentCaptor;
  @Captor
  private ArgumentCaptor<ParameterizedTypeReference<HeeUserDTO>> parameterizedTypeReferenceArgumentCaptorSingleDTO;

  @Before
  public void setUp() throws Exception {
    profileServiceImpl = new ProfileServiceImpl(RATE_LIMIT, BULK_RATE_LIMIT);
    profileServiceImpl.setServiceUrl(PROFILE_URL);
    MockitoAnnotations.initMocks(this);
  }


  @Test
  public void shouldGetAllTraineeIdMappings() {
    // given
    Pageable pageable = new PageRequest(0, 100);
    List<TraineeId> traineeIds = newArrayList(new TraineeId(999L, "1234567	"));
    PagedTraineeIdResponse response = new PagedTraineeIdResponse(traineeIds, 1);
    given(profileRestTemplate.getForObject(any(URI.class), eq(PagedTraineeIdResponse.class))).willReturn(response);

    // when
    Page<TraineeId> allTraineeIdMappings = profileServiceImpl.getPagedTraineeIds(DBC, pageable);

    // then
    assertEquals(traineeIds, allTraineeIdMappings.getContent());
  }

  @Test
  public void shouldPassSecurityTokenAsHeader() {
    // given
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
    given(profileRestTemplate.getForEntity(eq(PROFILE_URL + "/api/users/ro-user/" + DBC),
        eq(UserProfile.class))).willReturn(responseEntity);

    // when
    profileServiceImpl.getRODetails(DBC);

    // then
    verify(profileRestTemplate).getForEntity(eq(PROFILE_URL + "/api/users/ro-user/" + DBC), eq(UserProfile.class));

  }

  @Test
  public void shouldGetRODetails() throws Exception {
    // given
    UserProfile userProfile = new UserProfile();
    ResponseEntity responseEntity = new ResponseEntity(userProfile, HttpStatus.OK);
    given(profileRestTemplate.getForEntity(eq(PROFILE_URL + "/api/users/ro-user/" + DBC),
        eq(UserProfile.class))).willReturn(responseEntity);

    // when
    UserProfile user = profileServiceImpl.getRODetails(DBC);

    // then
    Assert.assertSame(userProfile, user);

  }

  @Test
  public void shouldGetAllUsers() throws Exception {
    // given
    JSONObject jsonObject = new JSONObject();
    ResponseEntity responseEntity = new ResponseEntity(jsonObject, HttpStatus.OK);
    given(profileRestTemplate.getForEntity(any(String.class), eq(JSONObject.class))).willReturn(responseEntity);

    // when
    profileServiceImpl.getAllUsers(PERMISSIONS, DBC);

    // then
    verify(profileRestTemplate).getForEntity(eq(PROFILE_URL + "/api/users?offset&limit&designatedBodyCode=" + DBC +
        "&permissions=" + PERMISSIONS), eq(JSONObject.class));
  }

  @Test
  public void getSingleAdminUserShouldReturnHeeUserDTO() {
    HeeUserDTO heeUserDTO = new HeeUserDTO();
    heeUserDTO.setFirstName(FIRSTNAME);
    given(profileRestTemplate.exchange(eq(PROFILE_URL + "/api/hee-users/Username"),eq(HttpMethod.GET),eq(null),
        parameterizedTypeReferenceArgumentCaptorSingleDTO.capture()))
        .willReturn(new ResponseEntity<>(heeUserDTO, OK));

    HeeUserDTO result = profileServiceImpl.getSingleAdminUser("Username");
    ParameterizedTypeReference<HeeUserDTO> parameterizedTypeReferenceArgumentCaptorSingleDTOValue =
        parameterizedTypeReferenceArgumentCaptorSingleDTO.getValue();

    assertEquals(result, heeUserDTO);
    verify(profileRestTemplate).exchange(PROFILE_URL + "/api/hee-users/Username",HttpMethod.GET,null,
        parameterizedTypeReferenceArgumentCaptorSingleDTOValue);
  }

  @Test
  public void getAllAdminUsersShouldReturnFoundAdminUsersWithSearchParamAndNoPageable() {
    HeeUserDTO heeUserDTO = new HeeUserDTO();
    HeeUserDTO heeUserDTO1 = new HeeUserDTO();
    heeUserDTO.setFirstName(FIRSTNAME);
    heeUserDTO1.setFirstName(FIRSTNAME1);
    List<HeeUserDTO> heeUserDTOS = Lists.newArrayList(heeUserDTO,heeUserDTO1);
    Page<HeeUserDTO> heeUserDTOSPage = new PageImpl<>(heeUserDTOS);
    PageRequest pageRequest = new PageRequest(0,11);


    given(profileRestTemplate.exchange(eq(PROFILE_URL + "/api/hee-users?search=Username"),eq(HttpMethod.GET),eq(null), parameterizedTypeReferenceArgumentCaptor.capture()))
        .willReturn(new ResponseEntity<>(heeUserDTOSPage, HttpStatus.OK));

    Page<HeeUserDTO> result = profileServiceImpl.getAllAdminUsers(null, "Username");

    ParameterizedTypeReference<Page<HeeUserDTO>> capturedParamValue = parameterizedTypeReferenceArgumentCaptor.getValue();
    assertEquals(result, heeUserDTOSPage);
    assertEquals(2, result.getContent().size());
    assertEquals(2, result.getTotalElements());
    assertEquals(0, result.getNumber());
    verify(profileRestTemplate).exchange(PROFILE_URL + "/api/hee-users?search=Username", HttpMethod.GET,null, capturedParamValue);
  }

  @Test
  public void getAllAdminUsersShouldReturnFoundPagedAdminUsersWithSearchParam() {
    HeeUserDTO heeUserDTO = new HeeUserDTO();
    HeeUserDTO heeUserDTO1 = new HeeUserDTO();
    heeUserDTO.setFirstName(FIRSTNAME);
    heeUserDTO1.setFirstName(FIRSTNAME1);
    List<HeeUserDTO> heeUserDTOS = Lists.newArrayList(heeUserDTO,heeUserDTO1);
    Page<HeeUserDTO> heeUserDTOSPage = new PageImpl<>(heeUserDTOS);
    PageRequest pageRequest = new PageRequest(0,11);


    given(profileRestTemplate.exchange(eq(PROFILE_URL + "/api/hee-users?search=Username&page=0&size=11"),eq(HttpMethod.GET),eq(null), parameterizedTypeReferenceArgumentCaptor.capture()))
        .willReturn(new ResponseEntity<>(heeUserDTOSPage, HttpStatus.OK));

    Page<HeeUserDTO> result = profileServiceImpl.getAllAdminUsers(pageRequest, "Username");

    ParameterizedTypeReference<Page<HeeUserDTO>> capturedParamValue = parameterizedTypeReferenceArgumentCaptor.getValue();
    assertEquals(result, heeUserDTOSPage);
    assertEquals(2, result.getContent().size());
    assertEquals(2, result.getTotalElements());
    assertEquals(0, result.getNumber());
    verify(profileRestTemplate).exchange(PROFILE_URL + "/api/hee-users?search=Username&page=0&size=11", HttpMethod.GET,null, capturedParamValue);
  }

  @Test
  public void getAllAdminUsersShouldReturnPagedAdminUsers() {
    HeeUserDTO heeUserDTO = new HeeUserDTO();
    HeeUserDTO heeUserDTO1 = new HeeUserDTO();
    heeUserDTO.setFirstName(FIRSTNAME);
    heeUserDTO1.setFirstName(FIRSTNAME1);
    List<HeeUserDTO> heeUserDTOS = Lists.newArrayList(heeUserDTO,heeUserDTO1);
    Page<HeeUserDTO> heeUserDTOSPage = new PageImpl<>(heeUserDTOS);
    PageRequest pageRequest = new PageRequest(0,11);


    given(profileRestTemplate.exchange(eq(PROFILE_URL + "/api/hee-users?page=0&size=11"),eq(HttpMethod.GET),eq(null), parameterizedTypeReferenceArgumentCaptor.capture()))
        .willReturn(new ResponseEntity<>(heeUserDTOSPage, HttpStatus.OK));

    Page<HeeUserDTO> result = profileServiceImpl.getAllAdminUsers(pageRequest, null);

    ParameterizedTypeReference<Page<HeeUserDTO>> capturedParamValue = parameterizedTypeReferenceArgumentCaptor.getValue();
    assertEquals(result, heeUserDTOSPage);
    assertEquals(2, result.getContent().size());
    assertEquals(2, result.getTotalElements());
    assertEquals(0, result.getNumber());
    verify(profileRestTemplate).exchange(PROFILE_URL + "/api/hee-users?page=0&size=11", HttpMethod.GET,null, capturedParamValue);
  }

  @Test
  public void getAllAdminUsersShouldReturnAllAdminUsersWithNoSearchParam() {
    HeeUserDTO heeUserDTO = new HeeUserDTO();
    HeeUserDTO heeUserDTO1 = new HeeUserDTO();
    heeUserDTO.setFirstName(FIRSTNAME);
    heeUserDTO1.setFirstName(FIRSTNAME1);
    List<HeeUserDTO> heeUserDTOS = Lists.newArrayList(heeUserDTO,heeUserDTO1);
    Page<HeeUserDTO> heeUserDTOSPage = new PageImpl<>(heeUserDTOS);


    given(profileRestTemplate.exchange(eq(PROFILE_URL + "/api/hee-users"),eq(HttpMethod.GET),eq(null), parameterizedTypeReferenceArgumentCaptor.capture()))
        .willReturn(new ResponseEntity<>(heeUserDTOSPage, HttpStatus.OK));

    Page<HeeUserDTO> result = profileServiceImpl.getAllAdminUsers(null, null);

    ParameterizedTypeReference<Page<HeeUserDTO>> capturedParamValue = parameterizedTypeReferenceArgumentCaptor.getValue();
    assertEquals(result, heeUserDTOSPage);
    assertEquals(2, result.getContent().size());
    assertEquals(2, result.getTotalElements());
    assertEquals(0, result.getNumber());
    verify(profileRestTemplate).exchange(PROFILE_URL + "/api/hee-users", HttpMethod.GET,null, capturedParamValue);
  }

  @Test
  public void shouldGetTraineeIdsForGmcNumbers() {
    // given
    RegistrationRequest request = new RegistrationRequest();
    request.setGmcNumber(GMC_NUMBER);
    TraineeProfileDto traineeProfileDto = new TraineeProfileDto();
    traineeProfileDto.setTisId(TIS_ID);
    traineeProfileDto.setGmcNumber(GMC_NUMBER);
    TraineeIdListResponse listResponse = new TraineeIdListResponse(newArrayList(traineeProfileDto));
    given(profileRestTemplate.exchange(any(URI.class), eq(POST), any(ResponseEntity.class), eq(TraineeIdListResponse.class)))
        .willReturn(new ResponseEntity<>(listResponse, OK));

    // when
    List<TraineeProfileDto> traineeProfileList = profileServiceImpl.getTraineeIdsForGmcNumbers(DBC, newArrayList(request));

    // then
    assertEquals(1, traineeProfileList.size());
    assertEquals(traineeProfileDto, traineeProfileList.get(0));
  }

}
package com.transformuk.hee.tis.profile.web.rest;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.profile.repository.UserTrustRepository;
import com.transformuk.hee.tis.profile.service.UserProgrammeService;
import com.transformuk.hee.tis.profile.service.UserService;
import com.transformuk.hee.tis.profile.service.UserTrustService;
import com.transformuk.hee.tis.profile.service.dto.BasicHeeUserDTO;
import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import com.transformuk.hee.tis.profile.service.mapper.HeeUserMapper;
import com.transformuk.hee.tis.profile.validators.HeeUserValidator;
import com.transformuk.hee.tis.profile.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
public class HeeUserResourceInt2Test {

  private static final String TESTNAME_1 = "TESTNAME1";
  private static final String TESTNAME_2 = "TESTNAME2@hee.nhs.uk";
  private static final char CTRL_A = '\u0001';
  @MockBean
  private HeeUserRepository heeUserRepositoryMock;
  @MockBean
  private HeeUserMapper heeUserMapperMock;
  @MockBean
  private HeeUserValidator heeUserValidatorMock;
  @MockBean
  private UserTrustRepository userTrustRepositoryMock;
  @MockBean
  private UserTrustService userTrustServiceMock;
  @MockBean
  private UserProgrammeService userProgrammeService;
  @MockBean
  private UserService userServiceMock;

  @Autowired
  private PageableArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  private MockMvc restHeeUserMockMvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    HeeUserResource heeUserResource = new HeeUserResource(heeUserRepositoryMock,
        heeUserMapperMock,
        heeUserValidatorMock,
        userTrustRepositoryMock,
        userTrustServiceMock,
        userProgrammeService,
        userServiceMock);
    this.restHeeUserMockMvc = MockMvcBuilders.standaloneSetup(heeUserResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Test
  public void getAllHeeUsersShouldReturnPageOfHeeUsers() throws Exception {
    Pageable page = new PageRequest(0, 10);
    HeeUserDTO heeUserDTO1 = new HeeUserDTO();
    HeeUserDTO heeUserDTO2 = new HeeUserDTO();
    heeUserDTO1.setFirstName(TESTNAME_1);
    heeUserDTO2.setFirstName(TESTNAME_2);
    Page<HeeUserDTO> heeUserList = new PageImpl<>(Lists.newArrayList(heeUserDTO1, heeUserDTO2));

    when(userServiceMock.findAllUsersWithTrust(page, null)).thenReturn(heeUserList);

    restHeeUserMockMvc
        .perform(get("/api/hee-users?size=10&page=0").contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.content.[*].firstName").value(hasItems(TESTNAME_1, TESTNAME_2)));
  }

  @Test
  public void getHeeUserShouldReturnSingeHeeUserDto() throws Exception {
    HeeUserDTO heeUserDTO = new HeeUserDTO();
    heeUserDTO.setFirstName(TESTNAME_1);
    heeUserDTO.setName(TESTNAME_2);

    when(userServiceMock.findSingleUserWithTrustAndProgrammes(TESTNAME_2)).thenReturn(heeUserDTO);

    restHeeUserMockMvc
        .perform(get("/api/hee-users/" + TESTNAME_2).contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.firstName").value(TESTNAME_1));
  }

  @Test
  public void getSingleHeeUserShouldReturnSingeHeeUserDto() throws Exception {
    HeeUserDTO heeUserDTO = new HeeUserDTO();
    heeUserDTO.setFirstName(TESTNAME_1);
    heeUserDTO.setName(TESTNAME_2);

    when(userServiceMock.findSingleUserWithTrustAndProgrammes(TESTNAME_2)).thenReturn(heeUserDTO);

    restHeeUserMockMvc.perform(get("/api/single-hee-users/?username=" + TESTNAME_2)
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.firstName").value(TESTNAME_1));
  }

  @Test
  public void getSingleHeeUserShouldRemoveInvalidCharactersFromSearchString() throws Exception {
    HeeUserDTO heeUserDTO = new HeeUserDTO();
    heeUserDTO.setFirstName(TESTNAME_1);
    heeUserDTO.setName(TESTNAME_2);

    when(userServiceMock.findSingleUserWithTrustAndProgrammes(TESTNAME_2)).thenReturn(heeUserDTO);

    restHeeUserMockMvc.perform(get("/api/single-hee-users/?username=" + TESTNAME_2 + CTRL_A)
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.firstName").value(TESTNAME_1));
  }

  @Test
  public void getUsersByRolesShouldReturnAllOfThemByTheirRoles() throws Exception {
    BasicHeeUserDTO basicHeeUserDTO1 = new BasicHeeUserDTO();
    BasicHeeUserDTO basicHeeUserDTO2 = new BasicHeeUserDTO();
    basicHeeUserDTO1.setName(TESTNAME_1);
    basicHeeUserDTO2.setName(TESTNAME_2);
    List<BasicHeeUserDTO> basicHeeUserDTOList = new ArrayList<>();
    basicHeeUserDTOList.add(basicHeeUserDTO1);
    basicHeeUserDTOList.add(basicHeeUserDTO2);
    List<String> roleNames = new ArrayList<>();
    roleNames.add("HEEAdminRevalidation");
    roleNames.add("RVAdmin");

    when(userServiceMock.findUsersByRoles(roleNames)).thenReturn(basicHeeUserDTOList);

    restHeeUserMockMvc.perform(get("/api/hee-users-with-roles/{roleNames}", roleNames)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
  }
}
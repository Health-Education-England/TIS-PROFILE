package com.transformuk.hee.tis.profile.web.rest;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.profile.repository.UserTrustRepository;
import com.transformuk.hee.tis.profile.service.LoginService;
import com.transformuk.hee.tis.profile.service.UserProgrammeService;
import com.transformuk.hee.tis.profile.service.UserService;
import com.transformuk.hee.tis.profile.service.UserTrustService;
import com.transformuk.hee.tis.profile.service.dto.BasicHeeUserDTO;
import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import com.transformuk.hee.tis.profile.service.mapper.HeeUserMapper;
import com.transformuk.hee.tis.profile.validators.HeeUserValidator;
import com.transformuk.hee.tis.profile.web.rest.errors.ExceptionTranslator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
public class HeeUserResourceInt2Test {

  private static final String TESTNAME_1 = "TESTNAME1";
  private static final String TESTNAME_2 = "TESTNAME2@hee.nhs.uk";
  private static final String EXISTING_EMAIL = "existing@nhs.net";
  private static final String DIFFERENT_EMAIL = "different@nhs.net";
  private static final String UPDATED_FIRSTNAME = "UpdatedFirst";
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
  @MockBean
  private LoginService loginServiceMock;

  @Autowired
  private PageableArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  private MockMvc restHeeUserMockMvc;

  @Before
  public void setup() {
    HeeUserResource heeUserResource = new HeeUserResource(heeUserRepositoryMock,
        heeUserMapperMock,
        heeUserValidatorMock,
        userTrustRepositoryMock,
        userTrustServiceMock,
        userProgrammeService,
        userServiceMock,
        loginServiceMock);
    this.restHeeUserMockMvc = MockMvcBuilders.standaloneSetup(heeUserResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Test
  public void getAllHeeUsersShouldReturnPageOfHeeUsers() throws Exception {
    Pageable page = PageRequest.of(0, 10);
    HeeUserDTO heeUserDTO1 = new HeeUserDTO();
    HeeUserDTO heeUserDTO2 = new HeeUserDTO();
    heeUserDTO1.setFirstName(TESTNAME_1);
    heeUserDTO2.setFirstName(TESTNAME_2);
    Page<HeeUserDTO> heeUserList = new PageImpl<>(Lists.newArrayList(heeUserDTO1, heeUserDTO2));

    when(userServiceMock.findAllUsersWithTrust(page, null)).thenReturn(heeUserList);

    restHeeUserMockMvc
        .perform(get("/api/hee-users?size=10&page=0").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content.[*].firstName").value(hasItems(TESTNAME_1, TESTNAME_2)));
  }

  @Test
  public void getHeeUserShouldReturnSingeHeeUserDto() throws Exception {
    HeeUserDTO heeUserDTO = new HeeUserDTO();
    heeUserDTO.setFirstName(TESTNAME_1);
    heeUserDTO.setName(TESTNAME_2);

    when(userServiceMock.findSingleUserWithTrustAndProgrammes(TESTNAME_2)).thenReturn(heeUserDTO);

    restHeeUserMockMvc
        .perform(get("/api/hee-users/" + TESTNAME_2).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.firstName").value(TESTNAME_1));
  }

  @Test
  public void getSingleHeeUserShouldReturnSingeHeeUserDto() throws Exception {
    HeeUserDTO heeUserDTO = new HeeUserDTO();
    heeUserDTO.setFirstName(TESTNAME_1);
    heeUserDTO.setName(TESTNAME_2);

    when(userServiceMock.findSingleUserWithTrustAndProgrammes(TESTNAME_2)).thenReturn(heeUserDTO);

    restHeeUserMockMvc.perform(get("/api/single-hee-users/?username=" + TESTNAME_2)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.firstName").value(TESTNAME_1));
  }

  @Test
  public void getSingleHeeUserShouldRemoveInvalidCharactersFromSearchString() throws Exception {
    HeeUserDTO heeUserDTO = new HeeUserDTO();
    heeUserDTO.setFirstName(TESTNAME_1);
    heeUserDTO.setName(TESTNAME_2);

    when(userServiceMock.findSingleUserWithTrustAndProgrammes(TESTNAME_2)).thenReturn(heeUserDTO);

    restHeeUserMockMvc.perform(get("/api/single-hee-users/?username=" + TESTNAME_2 + CTRL_A)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void getLtftAdminsShouldReturnAdminsWhenUserHasDbc() throws Exception {
    String dbc = "1-AIIDWX";
    String token = "someJwtToken";

    HeeUser currentUser = new HeeUser();
    currentUser.setName(TESTNAME_1);
    currentUser.setDesignatedBodyCodes(newHashSet(dbc));

    BasicHeeUserDTO admin1 = new BasicHeeUserDTO();
    admin1.setName("admin1");
    BasicHeeUserDTO admin2 = new BasicHeeUserDTO();
    admin2.setName("admin2");
    List<BasicHeeUserDTO> admins = Lists.newArrayList(admin1, admin2);

    when(loginServiceMock.getUserByToken(token)).thenReturn(currentUser);
    when(userServiceMock.findLtftAdmins("NHSE LTFT Admin", dbc)).thenReturn(admins);

    restHeeUserMockMvc.perform(get("/api/hee-users/ltft-admins")
            .param("ltftDbc", dbc)
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.[*].name").value(hasItems("admin1", "admin2")));
  }

  @Test
  public void getLtftAdminsShouldReturnForbiddenWhenUserDoesNotHaveDbc() throws Exception {
    String dbc = "1-AIIDWX";
    String token = "someJwtToken";

    HeeUser currentUser = new HeeUser();
    currentUser.setName(TESTNAME_1);
    currentUser.setDesignatedBodyCodes(newHashSet("1-OTHER"));

    when(loginServiceMock.getUserByToken(token)).thenReturn(currentUser);

    restHeeUserMockMvc.perform(get("/api/hee-users/ltft-admins")
            .param("ltftDbc", dbc)
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  public void getLtftAdminsShouldReturnEmptyListWhenNoAdminsFound() throws Exception {
    String dbc = "1-AIIDWX";
    String token = "someJwtToken";

    HeeUser currentUser = new HeeUser();
    currentUser.setName(TESTNAME_1);
    currentUser.setDesignatedBodyCodes(newHashSet(dbc));

    when(loginServiceMock.getUserByToken(token)).thenReturn(currentUser);
    when(userServiceMock.findLtftAdmins("NHSE LTFT Admin", dbc)).thenReturn(new ArrayList<>());

    restHeeUserMockMvc.perform(get("/api/hee-users/ltft-admins")
            .param("ltftDbc", dbc)
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  public void updateHeeUserWithDifferentEmailShouldReturnBadRequest() throws Exception {
    HeeUser existingUser = new HeeUser();
    existingUser.setName(TESTNAME_1);
    existingUser.setEmailAddress(EXISTING_EMAIL);

    HeeUserDTO heeUserDTO = new HeeUserDTO();
    heeUserDTO.setName(TESTNAME_1);
    heeUserDTO.setEmailAddress(DIFFERENT_EMAIL);

    HeeUser heeUser = new HeeUser();
    heeUser.setName(TESTNAME_1);
    heeUser.setEmailAddress(DIFFERENT_EMAIL);

    when(heeUserRepositoryMock.findById(TESTNAME_1)).thenReturn(Optional.of(existingUser));
    when(heeUserMapperMock.heeUserDTOToHeeUser(heeUserDTO)).thenReturn(heeUser);

    restHeeUserMockMvc.perform(put("/api/hee-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(heeUserDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void updateHeeUserWithSameEmailShouldSucceed() throws Exception {
    HeeUser existingUser = new HeeUser();
    existingUser.setName(TESTNAME_1);
    existingUser.setEmailAddress(EXISTING_EMAIL);
    existingUser.setAssociatedTrusts(new java.util.HashSet<>());
    existingUser.setAssociatedProgrammes(new java.util.HashSet<>());

    HeeUserDTO heeUserDTO = new HeeUserDTO();
    heeUserDTO.setName(TESTNAME_1);
    heeUserDTO.setFirstName(UPDATED_FIRSTNAME);
    heeUserDTO.setEmailAddress(EXISTING_EMAIL);

    HeeUser heeUser = new HeeUser();
    heeUser.setName(TESTNAME_1);
    heeUser.setFirstName(UPDATED_FIRSTNAME);
    heeUser.setEmailAddress(EXISTING_EMAIL);

    when(heeUserRepositoryMock.findById(TESTNAME_1)).thenReturn(Optional.of(existingUser));
    when(heeUserMapperMock.heeUserDTOToHeeUser(heeUserDTO)).thenReturn(heeUser);
    when(heeUserRepositoryMock.save(heeUser)).thenReturn(heeUser);
    when(heeUserRepositoryMock.findByNameWithTrustsAndProgrammes(TESTNAME_1))
        .thenReturn(Optional.of(heeUser));
    when(heeUserMapperMock.heeUserToHeeUserDTO(heeUser)).thenReturn(heeUserDTO);

    restHeeUserMockMvc.perform(put("/api/hee-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(heeUserDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value(UPDATED_FIRSTNAME));
  }
}

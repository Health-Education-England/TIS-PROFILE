package com.transformuk.hee.tis.profile.web.rest;


import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.TraineeProfile;
import com.transformuk.hee.tis.profile.dto.RegistrationRequest;
import com.transformuk.hee.tis.profile.service.TraineeProfileService;
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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
public class TraineeProfileControllerTest {

  private static final String DESIGNATED_BODY_CODE = "1-DGBODY";
  private static final String GMC_NUMBER = "gmc123";
  private static final Long TIS_ID = 1L;
  private static final String REQUEST = "[ { \"gmcNumber\": \"gmc123\" } ]";

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;
  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @MockBean
  private TraineeProfileService traineeProfileService;

  private MockMvc mvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    TraineeProfileController profileController = new TraineeProfileController(
        traineeProfileService);
    this.mvc = MockMvcBuilders.standaloneSetup(profileController)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Test
  public void shouldReturnTraineeIds() throws Exception {
    //Given
    TraineeProfile traineeProfile = new TraineeProfile(TIS_ID, GMC_NUMBER);
    given(traineeProfileService
        .findOrCreate(eq(DESIGNATED_BODY_CODE), anyListOf(RegistrationRequest.class))).willReturn
        (newArrayList(traineeProfile));

    // When & Then
    this.mvc.perform(post("/api/trainee-id/1-DGBODY/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(REQUEST))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.traineeIds").isArray())
        .andExpect(jsonPath("$.traineeIds[0].tisId").value(TIS_ID.intValue()))
        .andExpect(jsonPath("$.traineeIds[0].gmcNumber").value(GMC_NUMBER));
  }

  @Test
  public void shouldReturn500ForInternException() throws Exception {
    //Given
    given(traineeProfileService
        .findOrCreate(eq(DESIGNATED_BODY_CODE), anyListOf(RegistrationRequest.class)))
        .willThrow(RuntimeException.class);

    // When & Then
    this.mvc.perform(post("/api/trainee-id/1-DGBODY/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(REQUEST))
        .andExpect(status().isInternalServerError());
  }

  @Test
  public void shouldReturnExistingTraineeIds() throws Exception {
    //Given
    Pageable pageable = PageRequest.of(0, 10);
    TraineeProfile existingTraineeProfile = new TraineeProfile(1L, GMC_NUMBER);
    Page<TraineeProfile> page = new PageImpl<>(newArrayList(existingTraineeProfile));

    given(traineeProfileService.findAll(DESIGNATED_BODY_CODE, pageable)).willReturn(page);

    // When & Then
    this.mvc.perform(get("/api/trainee-id/1-DGBODY/mappings?page=0&size=10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(1))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.content[0].tisId").value(TIS_ID.intValue()))
        .andExpect(jsonPath("$.content[0].gmcNumber").value(GMC_NUMBER));
  }

}

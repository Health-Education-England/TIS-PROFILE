package com.transformuk.hee.tis.profile.web.rest;

import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.Qualification;
import com.transformuk.hee.tis.profile.repository.QualificationRepository;
import com.transformuk.hee.tis.profile.service.dto.QualificationDTO;
import com.transformuk.hee.tis.profile.service.mapper.QualificationMapper;
import com.transformuk.hee.tis.profile.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the QualificationResource REST controller.
 *
 * @see QualificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
public class QualificationResourceIntTest {

  private static final BigDecimal DEFAULT_TIS_ID = new BigDecimal(1);
  private static final BigDecimal UPDATED_TIS_ID = new BigDecimal(2);

  private static final String DEFAULT_QUALIFICATION = "AAAAAAAAAA";
  private static final String UPDATED_QUALIFICATION = "BBBBBBBBBB";

  private static final String DEFAULT_QUALIFICATION_TYPE = "AAAAAAAAAA";
  private static final String UPDATED_QUALIFICATION_TYPE = "BBBBBBBBBB";

  private static final LocalDate DEFAULT_DATE_ATTAINED = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_DATE_ATTAINED = LocalDate.now(ZoneId.systemDefault());

  private static final String DEFAULT_MEDICAL_SCHOOL = "AAAAAAAAAA";
  private static final String UPDATED_MEDICAL_SCHOOL = "BBBBBBBBBB";

  private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
  private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

  @Autowired
  private QualificationRepository qualificationRepository;

  @Autowired
  private QualificationMapper qualificationMapper;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restQualificationMockMvc;

  private Qualification qualification;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Qualification createEntity(EntityManager em) {
    Qualification qualification = new Qualification()
        .tisId(DEFAULT_TIS_ID)
        .qualification(DEFAULT_QUALIFICATION)
        .qualificationType(DEFAULT_QUALIFICATION_TYPE)
        .dateAttained(DEFAULT_DATE_ATTAINED)
        .medicalSchool(DEFAULT_MEDICAL_SCHOOL)
        .country(DEFAULT_COUNTRY);
    return qualification;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    QualificationResource qualificationResource = new QualificationResource(qualificationRepository, qualificationMapper);
    this.restQualificationMockMvc = MockMvcBuilders.standaloneSetup(qualificationResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    qualification = createEntity(em);
  }

  @Test
  @Transactional
  public void createQualification() throws Exception {
    int databaseSizeBeforeCreate = qualificationRepository.findAll().size();

    // Create the Qualification
    QualificationDTO qualificationDTO = qualificationMapper.qualificationToQualificationDTO(qualification);
    restQualificationMockMvc.perform(post("/api/qualifications")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
        .andExpect(status().isCreated());

    // Validate the Qualification in the database
    List<Qualification> qualificationList = qualificationRepository.findAll();
    assertThat(qualificationList).hasSize(databaseSizeBeforeCreate + 1);
    Qualification testQualification = qualificationList.get(qualificationList.size() - 1);
    assertThat(testQualification.getTisId()).isEqualTo(DEFAULT_TIS_ID);
    assertThat(testQualification.getQualification()).isEqualTo(DEFAULT_QUALIFICATION);
    assertThat(testQualification.getQualificationType()).isEqualTo(DEFAULT_QUALIFICATION_TYPE);
    assertThat(testQualification.getDateAttained()).isEqualTo(DEFAULT_DATE_ATTAINED);
    assertThat(testQualification.getMedicalSchool()).isEqualTo(DEFAULT_MEDICAL_SCHOOL);
    assertThat(testQualification.getCountry()).isEqualTo(DEFAULT_COUNTRY);
  }

  @Test
  @Transactional
  public void createQualificationWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = qualificationRepository.findAll().size();

    // Create the Qualification with an existing ID
    qualification.setId(1L);
    QualificationDTO qualificationDTO = qualificationMapper.qualificationToQualificationDTO(qualification);

    // An entity with an existing ID cannot be created, so this API call must fail
    restQualificationMockMvc.perform(post("/api/qualifications")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<Qualification> qualificationList = qualificationRepository.findAll();
    assertThat(qualificationList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkTisIdIsRequired() throws Exception {
    int databaseSizeBeforeTest = qualificationRepository.findAll().size();
    // set the field null
    qualification.setTisId(null);

    // Create the Qualification, which fails.
    QualificationDTO qualificationDTO = qualificationMapper.qualificationToQualificationDTO(qualification);

    restQualificationMockMvc.perform(post("/api/qualifications")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
        .andExpect(status().isBadRequest());

    List<Qualification> qualificationList = qualificationRepository.findAll();
    assertThat(qualificationList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllQualifications() throws Exception {
    // Initialize the database
    qualificationRepository.saveAndFlush(qualification);

    // Get all the qualificationList
    restQualificationMockMvc.perform(get("/api/qualifications?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(qualification.getId().intValue())))
        .andExpect(jsonPath("$.[*].tisId").value(hasItem(DEFAULT_TIS_ID.intValue())))
        .andExpect(jsonPath("$.[*].qualification").value(hasItem(DEFAULT_QUALIFICATION.toString())))
        .andExpect(jsonPath("$.[*].qualificationType").value(hasItem(DEFAULT_QUALIFICATION_TYPE.toString())))
        .andExpect(jsonPath("$.[*].dateAttained").value(hasItem(DEFAULT_DATE_ATTAINED.toString())))
        .andExpect(jsonPath("$.[*].medicalSchool").value(hasItem(DEFAULT_MEDICAL_SCHOOL.toString())))
        .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())));
  }

  @Test
  @Transactional
  public void getQualification() throws Exception {
    // Initialize the database
    qualificationRepository.saveAndFlush(qualification);

    // Get the qualification
    restQualificationMockMvc.perform(get("/api/qualifications/{id}", qualification.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(qualification.getId().intValue()))
        .andExpect(jsonPath("$.tisId").value(DEFAULT_TIS_ID.intValue()))
        .andExpect(jsonPath("$.qualification").value(DEFAULT_QUALIFICATION.toString()))
        .andExpect(jsonPath("$.qualificationType").value(DEFAULT_QUALIFICATION_TYPE.toString()))
        .andExpect(jsonPath("$.dateAttained").value(DEFAULT_DATE_ATTAINED.toString()))
        .andExpect(jsonPath("$.medicalSchool").value(DEFAULT_MEDICAL_SCHOOL.toString()))
        .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingQualification() throws Exception {
    // Get the qualification
    restQualificationMockMvc.perform(get("/api/qualifications/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateQualification() throws Exception {
    // Initialize the database
    qualificationRepository.saveAndFlush(qualification);
    int databaseSizeBeforeUpdate = qualificationRepository.findAll().size();

    // Update the qualification
    Qualification updatedQualification = qualificationRepository.findOne(qualification.getId());
    updatedQualification
        .tisId(UPDATED_TIS_ID)
        .qualification(UPDATED_QUALIFICATION)
        .qualificationType(UPDATED_QUALIFICATION_TYPE)
        .dateAttained(UPDATED_DATE_ATTAINED)
        .medicalSchool(UPDATED_MEDICAL_SCHOOL)
        .country(UPDATED_COUNTRY);
    QualificationDTO qualificationDTO = qualificationMapper.qualificationToQualificationDTO(updatedQualification);

    restQualificationMockMvc.perform(put("/api/qualifications")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
        .andExpect(status().isOk());

    // Validate the Qualification in the database
    List<Qualification> qualificationList = qualificationRepository.findAll();
    assertThat(qualificationList).hasSize(databaseSizeBeforeUpdate);
    Qualification testQualification = qualificationList.get(qualificationList.size() - 1);
    assertThat(testQualification.getTisId()).isEqualTo(UPDATED_TIS_ID);
    assertThat(testQualification.getQualification()).isEqualTo(UPDATED_QUALIFICATION);
    assertThat(testQualification.getQualificationType()).isEqualTo(UPDATED_QUALIFICATION_TYPE);
    assertThat(testQualification.getDateAttained()).isEqualTo(UPDATED_DATE_ATTAINED);
    assertThat(testQualification.getMedicalSchool()).isEqualTo(UPDATED_MEDICAL_SCHOOL);
    assertThat(testQualification.getCountry()).isEqualTo(UPDATED_COUNTRY);
  }

  @Test
  @Transactional
  public void updateNonExistingQualification() throws Exception {
    int databaseSizeBeforeUpdate = qualificationRepository.findAll().size();

    // Create the Qualification
    QualificationDTO qualificationDTO = qualificationMapper.qualificationToQualificationDTO(qualification);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restQualificationMockMvc.perform(put("/api/qualifications")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
        .andExpect(status().isCreated());

    // Validate the Qualification in the database
    List<Qualification> qualificationList = qualificationRepository.findAll();
    assertThat(qualificationList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteQualification() throws Exception {
    // Initialize the database
    qualificationRepository.saveAndFlush(qualification);
    int databaseSizeBeforeDelete = qualificationRepository.findAll().size();

    // Get the qualification
    restQualificationMockMvc.perform(delete("/api/qualifications/{id}", qualification.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<Qualification> qualificationList = qualificationRepository.findAll();
    assertThat(qualificationList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Qualification.class);
  }
}

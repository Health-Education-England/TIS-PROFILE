package com.transformuk.hee.tis.profile.web.rest;

import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.Immigration;
import com.transformuk.hee.tis.profile.repository.ImmigrationRepository;
import com.transformuk.hee.tis.profile.service.dto.ImmigrationDTO;
import com.transformuk.hee.tis.profile.service.mapper.ImmigrationMapper;
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
 * Test class for the ImmigrationResource REST controller.
 *
 * @see ImmigrationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
public class ImmigrationResourceIntTest {

  private static final BigDecimal DEFAULT_TIS_ID = new BigDecimal(1);
  private static final BigDecimal UPDATED_TIS_ID = new BigDecimal(2);

  private static final Boolean DEFAULT_EEA_RESIDENT = false;
  private static final Boolean UPDATED_EEA_RESIDENT = true;

  private static final String DEFAULT_PERMIT_TO_WORK = "AAAAAAAAAA";
  private static final String UPDATED_PERMIT_TO_WORK = "BBBBBBBBBB";

  private static final String DEFAULT_SETTLED = "AAAAAAAAAA";
  private static final String UPDATED_SETTLED = "BBBBBBBBBB";

  private static final LocalDate DEFAULT_VISA_ISSUED = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_VISA_ISSUED = LocalDate.now(ZoneId.systemDefault());

  private static final LocalDate DEFAULT_VISA_VALID_TO = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_VISA_VALID_TO = LocalDate.now(ZoneId.systemDefault());

  private static final String DEFAULT_VISA_DETAILS_NUMBER = "AAAAAAAAAA";
  private static final String UPDATED_VISA_DETAILS_NUMBER = "BBBBBBBBBB";

  @Autowired
  private ImmigrationRepository immigrationRepository;

  @Autowired
  private ImmigrationMapper immigrationMapper;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restImmigrationMockMvc;

  private Immigration immigration;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Immigration createEntity(EntityManager em) {
    Immigration immigration = new Immigration()
        .tisId(DEFAULT_TIS_ID)
        .eeaResident(DEFAULT_EEA_RESIDENT)
        .permitToWork(DEFAULT_PERMIT_TO_WORK)
        .settled(DEFAULT_SETTLED)
        .visaIssued(DEFAULT_VISA_ISSUED)
        .visaValidTo(DEFAULT_VISA_VALID_TO)
        .visaDetailsNumber(DEFAULT_VISA_DETAILS_NUMBER);
    return immigration;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    ImmigrationResource immigrationResource = new ImmigrationResource(immigrationRepository, immigrationMapper);
    this.restImmigrationMockMvc = MockMvcBuilders.standaloneSetup(immigrationResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    immigration = createEntity(em);
  }

  @Test
  @Transactional
  public void createImmigration() throws Exception {
    int databaseSizeBeforeCreate = immigrationRepository.findAll().size();

    // Create the Immigration
    ImmigrationDTO immigrationDTO = immigrationMapper.immigrationToImmigrationDTO(immigration);
    restImmigrationMockMvc.perform(post("/api/immigrations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(immigrationDTO)))
        .andExpect(status().isCreated());

    // Validate the Immigration in the database
    List<Immigration> immigrationList = immigrationRepository.findAll();
    assertThat(immigrationList).hasSize(databaseSizeBeforeCreate + 1);
    Immigration testImmigration = immigrationList.get(immigrationList.size() - 1);
    assertThat(testImmigration.getTisId()).isEqualTo(DEFAULT_TIS_ID);
    assertThat(testImmigration.isEeaResident()).isEqualTo(DEFAULT_EEA_RESIDENT);
    assertThat(testImmigration.getPermitToWork()).isEqualTo(DEFAULT_PERMIT_TO_WORK);
    assertThat(testImmigration.getSettled()).isEqualTo(DEFAULT_SETTLED);
    assertThat(testImmigration.getVisaIssued()).isEqualTo(DEFAULT_VISA_ISSUED);
    assertThat(testImmigration.getVisaValidTo()).isEqualTo(DEFAULT_VISA_VALID_TO);
    assertThat(testImmigration.getVisaDetailsNumber()).isEqualTo(DEFAULT_VISA_DETAILS_NUMBER);
  }

  @Test
  @Transactional
  public void createImmigrationWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = immigrationRepository.findAll().size();

    // Create the Immigration with an existing ID
    immigration.setId(1L);
    ImmigrationDTO immigrationDTO = immigrationMapper.immigrationToImmigrationDTO(immigration);

    // An entity with an existing ID cannot be created, so this API call must fail
    restImmigrationMockMvc.perform(post("/api/immigrations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(immigrationDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<Immigration> immigrationList = immigrationRepository.findAll();
    assertThat(immigrationList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkTisIdIsRequired() throws Exception {
    int databaseSizeBeforeTest = immigrationRepository.findAll().size();
    // set the field null
    immigration.setTisId(null);

    // Create the Immigration, which fails.
    ImmigrationDTO immigrationDTO = immigrationMapper.immigrationToImmigrationDTO(immigration);

    restImmigrationMockMvc.perform(post("/api/immigrations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(immigrationDTO)))
        .andExpect(status().isBadRequest());

    List<Immigration> immigrationList = immigrationRepository.findAll();
    assertThat(immigrationList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllImmigrations() throws Exception {
    // Initialize the database
    immigrationRepository.saveAndFlush(immigration);

    // Get all the immigrationList
    restImmigrationMockMvc.perform(get("/api/immigrations?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(immigration.getId().intValue())))
        .andExpect(jsonPath("$.[*].tisId").value(hasItem(DEFAULT_TIS_ID.intValue())))
        .andExpect(jsonPath("$.[*].eeaResident").value(hasItem(DEFAULT_EEA_RESIDENT.booleanValue())))
        .andExpect(jsonPath("$.[*].permitToWork").value(hasItem(DEFAULT_PERMIT_TO_WORK.toString())))
        .andExpect(jsonPath("$.[*].settled").value(hasItem(DEFAULT_SETTLED.toString())))
        .andExpect(jsonPath("$.[*].visaIssued").value(hasItem(DEFAULT_VISA_ISSUED.toString())))
        .andExpect(jsonPath("$.[*].visaValidTo").value(hasItem(DEFAULT_VISA_VALID_TO.toString())))
        .andExpect(jsonPath("$.[*].visaDetailsNumber").value(hasItem(DEFAULT_VISA_DETAILS_NUMBER.toString())));
  }

  @Test
  @Transactional
  public void getImmigration() throws Exception {
    // Initialize the database
    immigrationRepository.saveAndFlush(immigration);

    // Get the immigration
    restImmigrationMockMvc.perform(get("/api/immigrations/{id}", immigration.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(immigration.getId().intValue()))
        .andExpect(jsonPath("$.tisId").value(DEFAULT_TIS_ID.intValue()))
        .andExpect(jsonPath("$.eeaResident").value(DEFAULT_EEA_RESIDENT.booleanValue()))
        .andExpect(jsonPath("$.permitToWork").value(DEFAULT_PERMIT_TO_WORK.toString()))
        .andExpect(jsonPath("$.settled").value(DEFAULT_SETTLED.toString()))
        .andExpect(jsonPath("$.visaIssued").value(DEFAULT_VISA_ISSUED.toString()))
        .andExpect(jsonPath("$.visaValidTo").value(DEFAULT_VISA_VALID_TO.toString()))
        .andExpect(jsonPath("$.visaDetailsNumber").value(DEFAULT_VISA_DETAILS_NUMBER.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingImmigration() throws Exception {
    // Get the immigration
    restImmigrationMockMvc.perform(get("/api/immigrations/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateImmigration() throws Exception {
    // Initialize the database
    immigrationRepository.saveAndFlush(immigration);
    int databaseSizeBeforeUpdate = immigrationRepository.findAll().size();

    // Update the immigration
    Immigration updatedImmigration = immigrationRepository.findOne(immigration.getId());
    updatedImmigration
        .tisId(UPDATED_TIS_ID)
        .eeaResident(UPDATED_EEA_RESIDENT)
        .permitToWork(UPDATED_PERMIT_TO_WORK)
        .settled(UPDATED_SETTLED)
        .visaIssued(UPDATED_VISA_ISSUED)
        .visaValidTo(UPDATED_VISA_VALID_TO)
        .visaDetailsNumber(UPDATED_VISA_DETAILS_NUMBER);
    ImmigrationDTO immigrationDTO = immigrationMapper.immigrationToImmigrationDTO(updatedImmigration);

    restImmigrationMockMvc.perform(put("/api/immigrations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(immigrationDTO)))
        .andExpect(status().isOk());

    // Validate the Immigration in the database
    List<Immigration> immigrationList = immigrationRepository.findAll();
    assertThat(immigrationList).hasSize(databaseSizeBeforeUpdate);
    Immigration testImmigration = immigrationList.get(immigrationList.size() - 1);
    assertThat(testImmigration.getTisId()).isEqualTo(UPDATED_TIS_ID);
    assertThat(testImmigration.isEeaResident()).isEqualTo(UPDATED_EEA_RESIDENT);
    assertThat(testImmigration.getPermitToWork()).isEqualTo(UPDATED_PERMIT_TO_WORK);
    assertThat(testImmigration.getSettled()).isEqualTo(UPDATED_SETTLED);
    assertThat(testImmigration.getVisaIssued()).isEqualTo(UPDATED_VISA_ISSUED);
    assertThat(testImmigration.getVisaValidTo()).isEqualTo(UPDATED_VISA_VALID_TO);
    assertThat(testImmigration.getVisaDetailsNumber()).isEqualTo(UPDATED_VISA_DETAILS_NUMBER);
  }

  @Test
  @Transactional
  public void updateNonExistingImmigration() throws Exception {
    int databaseSizeBeforeUpdate = immigrationRepository.findAll().size();

    // Create the Immigration
    ImmigrationDTO immigrationDTO = immigrationMapper.immigrationToImmigrationDTO(immigration);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restImmigrationMockMvc.perform(put("/api/immigrations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(immigrationDTO)))
        .andExpect(status().isCreated());

    // Validate the Immigration in the database
    List<Immigration> immigrationList = immigrationRepository.findAll();
    assertThat(immigrationList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteImmigration() throws Exception {
    // Initialize the database
    immigrationRepository.saveAndFlush(immigration);
    int databaseSizeBeforeDelete = immigrationRepository.findAll().size();

    // Get the immigration
    restImmigrationMockMvc.perform(delete("/api/immigrations/{id}", immigration.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<Immigration> immigrationList = immigrationRepository.findAll();
    assertThat(immigrationList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Immigration.class);
  }
}

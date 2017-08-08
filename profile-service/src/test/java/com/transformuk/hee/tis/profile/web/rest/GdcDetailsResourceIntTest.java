package com.transformuk.hee.tis.profile.web.rest;

import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.GdcDetails;
import com.transformuk.hee.tis.profile.repository.GdcDetailsRepository;
import com.transformuk.hee.tis.profile.service.dto.GdcDetailsDTO;
import com.transformuk.hee.tis.profile.service.mapper.GdcDetailsMapper;
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
 * Test class for the GdcDetailsResource REST controller.
 *
 * @see GdcDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
public class GdcDetailsResourceIntTest {

  private static final BigDecimal DEFAULT_TIS_ID = new BigDecimal(1);
  private static final BigDecimal UPDATED_TIS_ID = new BigDecimal(2);

  private static final String DEFAULT_GDC_NUMBER = "AAAAAAAAAA";
  private static final String UPDATED_GDC_NUMBER = "BBBBBBBBBB";

  private static final String DEFAULT_GDC_STATUS = "AAAAAAAAAA";
  private static final String UPDATED_GDC_STATUS = "BBBBBBBBBB";

  private static final LocalDate DEFAULT_GDC_START_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_GDC_START_DATE = LocalDate.now(ZoneId.systemDefault());

  @Autowired
  private GdcDetailsRepository gdcDetailsRepository;

  @Autowired
  private GdcDetailsMapper gdcDetailsMapper;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restGdcDetailsMockMvc;

  private GdcDetails gdcDetails;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static GdcDetails createEntity(EntityManager em) {
    GdcDetails gdcDetails = new GdcDetails()
        .tisId(DEFAULT_TIS_ID)
        .gdcNumber(DEFAULT_GDC_NUMBER)
        .gdcStatus(DEFAULT_GDC_STATUS)
        .gdcStartDate(DEFAULT_GDC_START_DATE);
    return gdcDetails;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    GdcDetailsResource gdcDetailsResource = new GdcDetailsResource(gdcDetailsRepository, gdcDetailsMapper);
    this.restGdcDetailsMockMvc = MockMvcBuilders.standaloneSetup(gdcDetailsResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    gdcDetails = createEntity(em);
  }

  @Test
  @Transactional
  public void createGdcDetails() throws Exception {
    int databaseSizeBeforeCreate = gdcDetailsRepository.findAll().size();

    // Create the GdcDetails
    GdcDetailsDTO gdcDetailsDTO = gdcDetailsMapper.gdcDetailsToGdcDetailsDTO(gdcDetails);
    restGdcDetailsMockMvc.perform(post("/api/gdc-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(gdcDetailsDTO)))
        .andExpect(status().isCreated());

    // Validate the GdcDetails in the database
    List<GdcDetails> gdcDetailsList = gdcDetailsRepository.findAll();
    assertThat(gdcDetailsList).hasSize(databaseSizeBeforeCreate + 1);
    GdcDetails testGdcDetails = gdcDetailsList.get(gdcDetailsList.size() - 1);
    assertThat(testGdcDetails.getTisId()).isEqualTo(DEFAULT_TIS_ID);
    assertThat(testGdcDetails.getGdcNumber()).isEqualTo(DEFAULT_GDC_NUMBER);
    assertThat(testGdcDetails.getGdcStatus()).isEqualTo(DEFAULT_GDC_STATUS);
    assertThat(testGdcDetails.getGdcStartDate()).isEqualTo(DEFAULT_GDC_START_DATE);
  }

  @Test
  @Transactional
  public void createGdcDetailsWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = gdcDetailsRepository.findAll().size();

    // Create the GdcDetails with an existing ID
    gdcDetails.setId(1L);
    GdcDetailsDTO gdcDetailsDTO = gdcDetailsMapper.gdcDetailsToGdcDetailsDTO(gdcDetails);

    // An entity with an existing ID cannot be created, so this API call must fail
    restGdcDetailsMockMvc.perform(post("/api/gdc-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(gdcDetailsDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<GdcDetails> gdcDetailsList = gdcDetailsRepository.findAll();
    assertThat(gdcDetailsList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkTisIdIsRequired() throws Exception {
    int databaseSizeBeforeTest = gdcDetailsRepository.findAll().size();
    // set the field null
    gdcDetails.setTisId(null);

    // Create the GdcDetails, which fails.
    GdcDetailsDTO gdcDetailsDTO = gdcDetailsMapper.gdcDetailsToGdcDetailsDTO(gdcDetails);

    restGdcDetailsMockMvc.perform(post("/api/gdc-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(gdcDetailsDTO)))
        .andExpect(status().isBadRequest());

    List<GdcDetails> gdcDetailsList = gdcDetailsRepository.findAll();
    assertThat(gdcDetailsList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllGdcDetails() throws Exception {
    // Initialize the database
    gdcDetailsRepository.saveAndFlush(gdcDetails);

    // Get all the gdcDetailsList
    restGdcDetailsMockMvc.perform(get("/api/gdc-details?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(gdcDetails.getId().intValue())))
        .andExpect(jsonPath("$.[*].tisId").value(hasItem(DEFAULT_TIS_ID.intValue())))
        .andExpect(jsonPath("$.[*].gdcNumber").value(hasItem(DEFAULT_GDC_NUMBER.toString())))
        .andExpect(jsonPath("$.[*].gdcStatus").value(hasItem(DEFAULT_GDC_STATUS.toString())))
        .andExpect(jsonPath("$.[*].gdcStartDate").value(hasItem(DEFAULT_GDC_START_DATE.toString())));
  }

  @Test
  @Transactional
  public void getGdcDetails() throws Exception {
    // Initialize the database
    gdcDetailsRepository.saveAndFlush(gdcDetails);

    // Get the gdcDetails
    restGdcDetailsMockMvc.perform(get("/api/gdc-details/{id}", gdcDetails.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(gdcDetails.getId().intValue()))
        .andExpect(jsonPath("$.tisId").value(DEFAULT_TIS_ID.intValue()))
        .andExpect(jsonPath("$.gdcNumber").value(DEFAULT_GDC_NUMBER.toString()))
        .andExpect(jsonPath("$.gdcStatus").value(DEFAULT_GDC_STATUS.toString()))
        .andExpect(jsonPath("$.gdcStartDate").value(DEFAULT_GDC_START_DATE.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingGdcDetails() throws Exception {
    // Get the gdcDetails
    restGdcDetailsMockMvc.perform(get("/api/gdc-details/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateGdcDetails() throws Exception {
    // Initialize the database
    gdcDetailsRepository.saveAndFlush(gdcDetails);
    int databaseSizeBeforeUpdate = gdcDetailsRepository.findAll().size();

    // Update the gdcDetails
    GdcDetails updatedGdcDetails = gdcDetailsRepository.findOne(gdcDetails.getId());
    updatedGdcDetails
        .tisId(UPDATED_TIS_ID)
        .gdcNumber(UPDATED_GDC_NUMBER)
        .gdcStatus(UPDATED_GDC_STATUS)
        .gdcStartDate(UPDATED_GDC_START_DATE);
    GdcDetailsDTO gdcDetailsDTO = gdcDetailsMapper.gdcDetailsToGdcDetailsDTO(updatedGdcDetails);

    restGdcDetailsMockMvc.perform(put("/api/gdc-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(gdcDetailsDTO)))
        .andExpect(status().isOk());

    // Validate the GdcDetails in the database
    List<GdcDetails> gdcDetailsList = gdcDetailsRepository.findAll();
    assertThat(gdcDetailsList).hasSize(databaseSizeBeforeUpdate);
    GdcDetails testGdcDetails = gdcDetailsList.get(gdcDetailsList.size() - 1);
    assertThat(testGdcDetails.getTisId()).isEqualTo(UPDATED_TIS_ID);
    assertThat(testGdcDetails.getGdcNumber()).isEqualTo(UPDATED_GDC_NUMBER);
    assertThat(testGdcDetails.getGdcStatus()).isEqualTo(UPDATED_GDC_STATUS);
    assertThat(testGdcDetails.getGdcStartDate()).isEqualTo(UPDATED_GDC_START_DATE);
  }

  @Test
  @Transactional
  public void updateNonExistingGdcDetails() throws Exception {
    int databaseSizeBeforeUpdate = gdcDetailsRepository.findAll().size();

    // Create the GdcDetails
    GdcDetailsDTO gdcDetailsDTO = gdcDetailsMapper.gdcDetailsToGdcDetailsDTO(gdcDetails);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restGdcDetailsMockMvc.perform(put("/api/gdc-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(gdcDetailsDTO)))
        .andExpect(status().isCreated());

    // Validate the GdcDetails in the database
    List<GdcDetails> gdcDetailsList = gdcDetailsRepository.findAll();
    assertThat(gdcDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteGdcDetails() throws Exception {
    // Initialize the database
    gdcDetailsRepository.saveAndFlush(gdcDetails);
    int databaseSizeBeforeDelete = gdcDetailsRepository.findAll().size();

    // Get the gdcDetails
    restGdcDetailsMockMvc.perform(delete("/api/gdc-details/{id}", gdcDetails.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<GdcDetails> gdcDetailsList = gdcDetailsRepository.findAll();
    assertThat(gdcDetailsList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(GdcDetails.class);
  }
}

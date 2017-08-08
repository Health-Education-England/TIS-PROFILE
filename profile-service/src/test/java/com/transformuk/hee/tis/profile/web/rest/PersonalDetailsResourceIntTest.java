package com.transformuk.hee.tis.profile.web.rest;

import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.PersonalDetails;
import com.transformuk.hee.tis.profile.repository.PersonalDetailsRepository;
import com.transformuk.hee.tis.profile.service.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.profile.service.mapper.PersonalDetailsMapper;
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
 * Test class for the PersonalDetailsResource REST controller.
 *
 * @see PersonalDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
public class PersonalDetailsResourceIntTest {

  private static final BigDecimal DEFAULT_TIS_ID = new BigDecimal(1);
  private static final BigDecimal UPDATED_TIS_ID = new BigDecimal(2);

  private static final String DEFAULT_SURNAME_NB = "AAAAAAAAAA";
  private static final String UPDATED_SURNAME_NB = "BBBBBBBBBB";

  private static final String DEFAULT_LEGAL_SURNAME = "AAAAAAAAAA";
  private static final String UPDATED_LEGAL_SURNAME = "BBBBBBBBBB";

  private static final String DEFAULT_FORENAMES = "AAAAAAAAAA";
  private static final String UPDATED_FORENAMES = "BBBBBBBBBB";

  private static final String DEFAULT_LEGAL_FORENAMES = "AAAAAAAAAA";
  private static final String UPDATED_LEGAL_FORENAMES = "BBBBBBBBBB";

  private static final String DEFAULT_KNOWN_AS = "AAAAAAAAAA";
  private static final String UPDATED_KNOWN_AS = "BBBBBBBBBB";

  private static final String DEFAULT_MAIDEN_NAME = "AAAAAAAAAA";
  private static final String UPDATED_MAIDEN_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_INITIALS = "AAAAAAAAAA";
  private static final String UPDATED_INITIALS = "BBBBBBBBBB";

  private static final String DEFAULT_TITLE = "AAAAAAAAAA";
  private static final String UPDATED_TITLE = "BBBBBBBBBB";

  private static final String DEFAULT_TELEPHONE_NUMBER = "AAAAAAAAAA";
  private static final String UPDATED_TELEPHONE_NUMBER = "BBBBBBBBBB";

  private static final String DEFAULT_MOBILE_NUMBER = "AAAAAAAAAA";
  private static final String UPDATED_MOBILE_NUMBER = "BBBBBBBBBB";

  private static final String DEFAULT_EMAIL_ADDRESS = "AAAAAAAAAA";
  private static final String UPDATED_EMAIL_ADDRESS = "BBBBBBBBBB";

  private static final String DEFAULT_CORRESPONDENCE_ADDRESS = "AAAAAAAAAA";
  private static final String UPDATED_CORRESPONDENCE_ADDRESS = "BBBBBBBBBB";

  private static final String DEFAULT_CORRESPONDENCE_ADDRESS_POST_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CORRESPONDENCE_ADDRESS_POST_CODE = "BBBBBBBBBB";

  private static final String DEFAULT_STATUS = "AAAAAAAAAA";
  private static final String UPDATED_STATUS = "BBBBBBBBBB";

  @Autowired
  private PersonalDetailsRepository personalDetailsRepository;

  @Autowired
  private PersonalDetailsMapper personalDetailsMapper;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restPersonalDetailsMockMvc;

  private PersonalDetails personalDetails;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static PersonalDetails createEntity(EntityManager em) {
    PersonalDetails personalDetails = new PersonalDetails()
        .tisId(DEFAULT_TIS_ID)
        .surnameNb(DEFAULT_SURNAME_NB)
        .legalSurname(DEFAULT_LEGAL_SURNAME)
        .forenames(DEFAULT_FORENAMES)
        .legalForenames(DEFAULT_LEGAL_FORENAMES)
        .knownAs(DEFAULT_KNOWN_AS)
        .maidenName(DEFAULT_MAIDEN_NAME)
        .initials(DEFAULT_INITIALS)
        .title(DEFAULT_TITLE)
        .telephoneNumber(DEFAULT_TELEPHONE_NUMBER)
        .mobileNumber(DEFAULT_MOBILE_NUMBER)
        .emailAddress(DEFAULT_EMAIL_ADDRESS)
        .correspondenceAddress(DEFAULT_CORRESPONDENCE_ADDRESS)
        .correspondenceAddressPostCode(DEFAULT_CORRESPONDENCE_ADDRESS_POST_CODE)
        .status(DEFAULT_STATUS);
    return personalDetails;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    PersonalDetailsResource personalDetailsResource = new PersonalDetailsResource(personalDetailsRepository, personalDetailsMapper);
    this.restPersonalDetailsMockMvc = MockMvcBuilders.standaloneSetup(personalDetailsResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    personalDetails = createEntity(em);
  }

  @Test
  @Transactional
  public void createPersonalDetails() throws Exception {
    int databaseSizeBeforeCreate = personalDetailsRepository.findAll().size();

    // Create the PersonalDetails
    PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.personalDetailsToPersonalDetailsDTO(personalDetails);
    restPersonalDetailsMockMvc.perform(post("/api/personal-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO)))
        .andExpect(status().isCreated());

    // Validate the PersonalDetails in the database
    List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
    assertThat(personalDetailsList).hasSize(databaseSizeBeforeCreate + 1);
    PersonalDetails testPersonalDetails = personalDetailsList.get(personalDetailsList.size() - 1);
    assertThat(testPersonalDetails.getTisId()).isEqualTo(DEFAULT_TIS_ID);
    assertThat(testPersonalDetails.getSurnameNb()).isEqualTo(DEFAULT_SURNAME_NB);
    assertThat(testPersonalDetails.getLegalSurname()).isEqualTo(DEFAULT_LEGAL_SURNAME);
    assertThat(testPersonalDetails.getForenames()).isEqualTo(DEFAULT_FORENAMES);
    assertThat(testPersonalDetails.getLegalForenames()).isEqualTo(DEFAULT_LEGAL_FORENAMES);
    assertThat(testPersonalDetails.getKnownAs()).isEqualTo(DEFAULT_KNOWN_AS);
    assertThat(testPersonalDetails.getMaidenName()).isEqualTo(DEFAULT_MAIDEN_NAME);
    assertThat(testPersonalDetails.getInitials()).isEqualTo(DEFAULT_INITIALS);
    assertThat(testPersonalDetails.getTitle()).isEqualTo(DEFAULT_TITLE);
    assertThat(testPersonalDetails.getTelephoneNumber()).isEqualTo(DEFAULT_TELEPHONE_NUMBER);
    assertThat(testPersonalDetails.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
    assertThat(testPersonalDetails.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
    assertThat(testPersonalDetails.getCorrespondenceAddress()).isEqualTo(DEFAULT_CORRESPONDENCE_ADDRESS);
    assertThat(testPersonalDetails.getCorrespondenceAddressPostCode()).isEqualTo(DEFAULT_CORRESPONDENCE_ADDRESS_POST_CODE);
    assertThat(testPersonalDetails.getStatus()).isEqualTo(DEFAULT_STATUS);
  }

  @Test
  @Transactional
  public void createPersonalDetailsWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = personalDetailsRepository.findAll().size();

    // Create the PersonalDetails with an existing ID
    personalDetails.setId(1L);
    PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.personalDetailsToPersonalDetailsDTO(personalDetails);

    // An entity with an existing ID cannot be created, so this API call must fail
    restPersonalDetailsMockMvc.perform(post("/api/personal-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
    assertThat(personalDetailsList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkTisIdIsRequired() throws Exception {
    int databaseSizeBeforeTest = personalDetailsRepository.findAll().size();
    // set the field null
    personalDetails.setTisId(null);

    // Create the PersonalDetails, which fails.
    PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.personalDetailsToPersonalDetailsDTO(personalDetails);

    restPersonalDetailsMockMvc.perform(post("/api/personal-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO)))
        .andExpect(status().isBadRequest());

    List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
    assertThat(personalDetailsList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllPersonalDetails() throws Exception {
    // Initialize the database
    personalDetailsRepository.saveAndFlush(personalDetails);

    // Get all the personalDetailsList
    restPersonalDetailsMockMvc.perform(get("/api/personal-details?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(personalDetails.getId().intValue())))
        .andExpect(jsonPath("$.[*].tisId").value(hasItem(DEFAULT_TIS_ID.intValue())))
        .andExpect(jsonPath("$.[*].surnameNb").value(hasItem(DEFAULT_SURNAME_NB.toString())))
        .andExpect(jsonPath("$.[*].legalSurname").value(hasItem(DEFAULT_LEGAL_SURNAME.toString())))
        .andExpect(jsonPath("$.[*].forenames").value(hasItem(DEFAULT_FORENAMES.toString())))
        .andExpect(jsonPath("$.[*].legalForenames").value(hasItem(DEFAULT_LEGAL_FORENAMES.toString())))
        .andExpect(jsonPath("$.[*].knownAs").value(hasItem(DEFAULT_KNOWN_AS.toString())))
        .andExpect(jsonPath("$.[*].maidenName").value(hasItem(DEFAULT_MAIDEN_NAME.toString())))
        .andExpect(jsonPath("$.[*].initials").value(hasItem(DEFAULT_INITIALS.toString())))
        .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
        .andExpect(jsonPath("$.[*].telephoneNumber").value(hasItem(DEFAULT_TELEPHONE_NUMBER.toString())))
        .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER.toString())))
        .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS.toString())))
        .andExpect(jsonPath("$.[*].correspondenceAddress").value(hasItem(DEFAULT_CORRESPONDENCE_ADDRESS.toString())))
        .andExpect(jsonPath("$.[*].correspondenceAddressPostCode").value(hasItem(DEFAULT_CORRESPONDENCE_ADDRESS_POST_CODE.toString())))
        .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
  }

  @Test
  @Transactional
  public void getPersonalDetails() throws Exception {
    // Initialize the database
    personalDetailsRepository.saveAndFlush(personalDetails);

    // Get the personalDetails
    restPersonalDetailsMockMvc.perform(get("/api/personal-details/{id}", personalDetails.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(personalDetails.getId().intValue()))
        .andExpect(jsonPath("$.tisId").value(DEFAULT_TIS_ID.intValue()))
        .andExpect(jsonPath("$.surnameNb").value(DEFAULT_SURNAME_NB.toString()))
        .andExpect(jsonPath("$.legalSurname").value(DEFAULT_LEGAL_SURNAME.toString()))
        .andExpect(jsonPath("$.forenames").value(DEFAULT_FORENAMES.toString()))
        .andExpect(jsonPath("$.legalForenames").value(DEFAULT_LEGAL_FORENAMES.toString()))
        .andExpect(jsonPath("$.knownAs").value(DEFAULT_KNOWN_AS.toString()))
        .andExpect(jsonPath("$.maidenName").value(DEFAULT_MAIDEN_NAME.toString()))
        .andExpect(jsonPath("$.initials").value(DEFAULT_INITIALS.toString()))
        .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
        .andExpect(jsonPath("$.telephoneNumber").value(DEFAULT_TELEPHONE_NUMBER.toString()))
        .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_MOBILE_NUMBER.toString()))
        .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS.toString()))
        .andExpect(jsonPath("$.correspondenceAddress").value(DEFAULT_CORRESPONDENCE_ADDRESS.toString()))
        .andExpect(jsonPath("$.correspondenceAddressPostCode").value(DEFAULT_CORRESPONDENCE_ADDRESS_POST_CODE.toString()))
        .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingPersonalDetails() throws Exception {
    // Get the personalDetails
    restPersonalDetailsMockMvc.perform(get("/api/personal-details/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updatePersonalDetails() throws Exception {
    // Initialize the database
    personalDetailsRepository.saveAndFlush(personalDetails);
    int databaseSizeBeforeUpdate = personalDetailsRepository.findAll().size();

    // Update the personalDetails
    PersonalDetails updatedPersonalDetails = personalDetailsRepository.findOne(personalDetails.getId());
    updatedPersonalDetails
        .tisId(UPDATED_TIS_ID)
        .surnameNb(UPDATED_SURNAME_NB)
        .legalSurname(UPDATED_LEGAL_SURNAME)
        .forenames(UPDATED_FORENAMES)
        .legalForenames(UPDATED_LEGAL_FORENAMES)
        .knownAs(UPDATED_KNOWN_AS)
        .maidenName(UPDATED_MAIDEN_NAME)
        .initials(UPDATED_INITIALS)
        .title(UPDATED_TITLE)
        .telephoneNumber(UPDATED_TELEPHONE_NUMBER)
        .mobileNumber(UPDATED_MOBILE_NUMBER)
        .emailAddress(UPDATED_EMAIL_ADDRESS)
        .correspondenceAddress(UPDATED_CORRESPONDENCE_ADDRESS)
        .correspondenceAddressPostCode(UPDATED_CORRESPONDENCE_ADDRESS_POST_CODE)
        .status(UPDATED_STATUS);
    PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.personalDetailsToPersonalDetailsDTO(updatedPersonalDetails);

    restPersonalDetailsMockMvc.perform(put("/api/personal-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO)))
        .andExpect(status().isOk());

    // Validate the PersonalDetails in the database
    List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
    assertThat(personalDetailsList).hasSize(databaseSizeBeforeUpdate);
    PersonalDetails testPersonalDetails = personalDetailsList.get(personalDetailsList.size() - 1);
    assertThat(testPersonalDetails.getTisId()).isEqualTo(UPDATED_TIS_ID);
    assertThat(testPersonalDetails.getSurnameNb()).isEqualTo(UPDATED_SURNAME_NB);
    assertThat(testPersonalDetails.getLegalSurname()).isEqualTo(UPDATED_LEGAL_SURNAME);
    assertThat(testPersonalDetails.getForenames()).isEqualTo(UPDATED_FORENAMES);
    assertThat(testPersonalDetails.getLegalForenames()).isEqualTo(UPDATED_LEGAL_FORENAMES);
    assertThat(testPersonalDetails.getKnownAs()).isEqualTo(UPDATED_KNOWN_AS);
    assertThat(testPersonalDetails.getMaidenName()).isEqualTo(UPDATED_MAIDEN_NAME);
    assertThat(testPersonalDetails.getInitials()).isEqualTo(UPDATED_INITIALS);
    assertThat(testPersonalDetails.getTitle()).isEqualTo(UPDATED_TITLE);
    assertThat(testPersonalDetails.getTelephoneNumber()).isEqualTo(UPDATED_TELEPHONE_NUMBER);
    assertThat(testPersonalDetails.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
    assertThat(testPersonalDetails.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
    assertThat(testPersonalDetails.getCorrespondenceAddress()).isEqualTo(UPDATED_CORRESPONDENCE_ADDRESS);
    assertThat(testPersonalDetails.getCorrespondenceAddressPostCode()).isEqualTo(UPDATED_CORRESPONDENCE_ADDRESS_POST_CODE);
    assertThat(testPersonalDetails.getStatus()).isEqualTo(UPDATED_STATUS);
  }

  @Test
  @Transactional
  public void updateNonExistingPersonalDetails() throws Exception {
    int databaseSizeBeforeUpdate = personalDetailsRepository.findAll().size();

    // Create the PersonalDetails
    PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.personalDetailsToPersonalDetailsDTO(personalDetails);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restPersonalDetailsMockMvc.perform(put("/api/personal-details")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO)))
        .andExpect(status().isCreated());

    // Validate the PersonalDetails in the database
    List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
    assertThat(personalDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deletePersonalDetails() throws Exception {
    // Initialize the database
    personalDetailsRepository.saveAndFlush(personalDetails);
    int databaseSizeBeforeDelete = personalDetailsRepository.findAll().size();

    // Get the personalDetails
    restPersonalDetailsMockMvc.perform(delete("/api/personal-details/{id}", personalDetails.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
    assertThat(personalDetailsList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(PersonalDetails.class);
  }
}

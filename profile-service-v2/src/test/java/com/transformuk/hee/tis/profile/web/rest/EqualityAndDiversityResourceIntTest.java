package com.transformuk.hee.tis.profile.web.rest;

import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.EqualityAndDiversity;
import com.transformuk.hee.tis.profile.repository.EqualityAndDiversityRepository;
import com.transformuk.hee.tis.profile.service.dto.EqualityAndDiversityDTO;
import com.transformuk.hee.tis.profile.service.mapper.EqualityAndDiversityMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EqualityAndDiversityResource REST controller.
 *
 * @see EqualityAndDiversityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
public class EqualityAndDiversityResourceIntTest {

	private static final BigDecimal DEFAULT_TIS_ID = new BigDecimal(1);
	private static final BigDecimal UPDATED_TIS_ID = new BigDecimal(2);

	private static final String DEFAULT_MARITAL_STATUS = "AAAAAAAAAA";
	private static final String UPDATED_MARITAL_STATUS = "BBBBBBBBBB";

	private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

	private static final String DEFAULT_GENDER = "AAAAAAAAAA";
	private static final String UPDATED_GENDER = "BBBBBBBBBB";

	private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
	private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

	private static final String DEFAULT_DUAL_NATIONALITY = "AAAAAAAAAA";
	private static final String UPDATED_DUAL_NATIONALITY = "BBBBBBBBBB";

	private static final String DEFAULT_SEXUAL_ORIENTATION = "AAAAAAAAAA";
	private static final String UPDATED_SEXUAL_ORIENTATION = "BBBBBBBBBB";

	private static final String DEFAULT_RELIGIOUS_BELIEF = "AAAAAAAAAA";
	private static final String UPDATED_RELIGIOUS_BELIEF = "BBBBBBBBBB";

	private static final String DEFAULT_ETHNIC_ORIGIN = "AAAAAAAAAA";
	private static final String UPDATED_ETHNIC_ORIGIN = "BBBBBBBBBB";

	private static final Boolean DEFAULT_DISABILITY = false;
	private static final Boolean UPDATED_DISABILITY = true;

	private static final String DEFAULT_DISABILITY_DETAILS = "AAAAAAAAAA";
	private static final String UPDATED_DISABILITY_DETAILS = "BBBBBBBBBB";

	@Autowired
	private EqualityAndDiversityRepository equalityAndDiversityRepository;

	@Autowired
	private EqualityAndDiversityMapper equalityAndDiversityMapper;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restEqualityAndDiversityMockMvc;

	private EqualityAndDiversity equalityAndDiversity;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static EqualityAndDiversity createEntity(EntityManager em) {
		EqualityAndDiversity equalityAndDiversity = new EqualityAndDiversity()
				.tisId(DEFAULT_TIS_ID)
				.maritalStatus(DEFAULT_MARITAL_STATUS)
				.dateOfBirth(DEFAULT_DATE_OF_BIRTH)
				.gender(DEFAULT_GENDER)
				.nationality(DEFAULT_NATIONALITY)
				.dualNationality(DEFAULT_DUAL_NATIONALITY)
				.sexualOrientation(DEFAULT_SEXUAL_ORIENTATION)
				.religiousBelief(DEFAULT_RELIGIOUS_BELIEF)
				.ethnicOrigin(DEFAULT_ETHNIC_ORIGIN)
				.disability(DEFAULT_DISABILITY)
				.disabilityDetails(DEFAULT_DISABILITY_DETAILS);
		return equalityAndDiversity;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		EqualityAndDiversityResource equalityAndDiversityResource = new EqualityAndDiversityResource(equalityAndDiversityRepository, equalityAndDiversityMapper);
		this.restEqualityAndDiversityMockMvc = MockMvcBuilders.standaloneSetup(equalityAndDiversityResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		equalityAndDiversity = createEntity(em);
	}

	@Test
	@Transactional
	public void createEqualityAndDiversity() throws Exception {
		int databaseSizeBeforeCreate = equalityAndDiversityRepository.findAll().size();

		// Create the EqualityAndDiversity
		EqualityAndDiversityDTO equalityAndDiversityDTO = equalityAndDiversityMapper.equalityAndDiversityToEqualityAndDiversityDTO(equalityAndDiversity);
		restEqualityAndDiversityMockMvc.perform(post("/api/equality-and-diversities")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(equalityAndDiversityDTO)))
				.andExpect(status().isCreated());

		// Validate the EqualityAndDiversity in the database
		List<EqualityAndDiversity> equalityAndDiversityList = equalityAndDiversityRepository.findAll();
		assertThat(equalityAndDiversityList).hasSize(databaseSizeBeforeCreate + 1);
		EqualityAndDiversity testEqualityAndDiversity = equalityAndDiversityList.get(equalityAndDiversityList.size() - 1);
		assertThat(testEqualityAndDiversity.getTisId()).isEqualTo(DEFAULT_TIS_ID);
		assertThat(testEqualityAndDiversity.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
		assertThat(testEqualityAndDiversity.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
		assertThat(testEqualityAndDiversity.getGender()).isEqualTo(DEFAULT_GENDER);
		assertThat(testEqualityAndDiversity.getNationality()).isEqualTo(DEFAULT_NATIONALITY);
		assertThat(testEqualityAndDiversity.getDualNationality()).isEqualTo(DEFAULT_DUAL_NATIONALITY);
		assertThat(testEqualityAndDiversity.getSexualOrientation()).isEqualTo(DEFAULT_SEXUAL_ORIENTATION);
		assertThat(testEqualityAndDiversity.getReligiousBelief()).isEqualTo(DEFAULT_RELIGIOUS_BELIEF);
		assertThat(testEqualityAndDiversity.getEthnicOrigin()).isEqualTo(DEFAULT_ETHNIC_ORIGIN);
		assertThat(testEqualityAndDiversity.isDisability()).isEqualTo(DEFAULT_DISABILITY);
		assertThat(testEqualityAndDiversity.getDisabilityDetails()).isEqualTo(DEFAULT_DISABILITY_DETAILS);
	}

	@Test
	@Transactional
	public void createEqualityAndDiversityWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = equalityAndDiversityRepository.findAll().size();

		// Create the EqualityAndDiversity with an existing ID
		equalityAndDiversity.setId(1L);
		EqualityAndDiversityDTO equalityAndDiversityDTO = equalityAndDiversityMapper.equalityAndDiversityToEqualityAndDiversityDTO(equalityAndDiversity);

		// An entity with an existing ID cannot be created, so this API call must fail
		restEqualityAndDiversityMockMvc.perform(post("/api/equality-and-diversities")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(equalityAndDiversityDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<EqualityAndDiversity> equalityAndDiversityList = equalityAndDiversityRepository.findAll();
		assertThat(equalityAndDiversityList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void checkTisIdIsRequired() throws Exception {
		int databaseSizeBeforeTest = equalityAndDiversityRepository.findAll().size();
		// set the field null
		equalityAndDiversity.setTisId(null);

		// Create the EqualityAndDiversity, which fails.
		EqualityAndDiversityDTO equalityAndDiversityDTO = equalityAndDiversityMapper.equalityAndDiversityToEqualityAndDiversityDTO(equalityAndDiversity);

		restEqualityAndDiversityMockMvc.perform(post("/api/equality-and-diversities")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(equalityAndDiversityDTO)))
				.andExpect(status().isBadRequest());

		List<EqualityAndDiversity> equalityAndDiversityList = equalityAndDiversityRepository.findAll();
		assertThat(equalityAndDiversityList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllEqualityAndDiversities() throws Exception {
		// Initialize the database
		equalityAndDiversityRepository.saveAndFlush(equalityAndDiversity);

		// Get all the equalityAndDiversityList
		restEqualityAndDiversityMockMvc.perform(get("/api/equality-and-diversities?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(equalityAndDiversity.getId().intValue())))
				.andExpect(jsonPath("$.[*].tisId").value(hasItem(DEFAULT_TIS_ID.intValue())))
				.andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
				.andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
				.andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
				.andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY.toString())))
				.andExpect(jsonPath("$.[*].dualNationality").value(hasItem(DEFAULT_DUAL_NATIONALITY.toString())))
				.andExpect(jsonPath("$.[*].sexualOrientation").value(hasItem(DEFAULT_SEXUAL_ORIENTATION.toString())))
				.andExpect(jsonPath("$.[*].religiousBelief").value(hasItem(DEFAULT_RELIGIOUS_BELIEF.toString())))
				.andExpect(jsonPath("$.[*].ethnicOrigin").value(hasItem(DEFAULT_ETHNIC_ORIGIN.toString())))
				.andExpect(jsonPath("$.[*].disability").value(hasItem(DEFAULT_DISABILITY.booleanValue())))
				.andExpect(jsonPath("$.[*].disabilityDetails").value(hasItem(DEFAULT_DISABILITY_DETAILS.toString())));
	}

	@Test
	@Transactional
	public void getEqualityAndDiversity() throws Exception {
		// Initialize the database
		equalityAndDiversityRepository.saveAndFlush(equalityAndDiversity);

		// Get the equalityAndDiversity
		restEqualityAndDiversityMockMvc.perform(get("/api/equality-and-diversities/{id}", equalityAndDiversity.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(equalityAndDiversity.getId().intValue()))
				.andExpect(jsonPath("$.tisId").value(DEFAULT_TIS_ID.intValue()))
				.andExpect(jsonPath("$.maritalStatus").value(DEFAULT_MARITAL_STATUS.toString()))
				.andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
				.andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
				.andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY.toString()))
				.andExpect(jsonPath("$.dualNationality").value(DEFAULT_DUAL_NATIONALITY.toString()))
				.andExpect(jsonPath("$.sexualOrientation").value(DEFAULT_SEXUAL_ORIENTATION.toString()))
				.andExpect(jsonPath("$.religiousBelief").value(DEFAULT_RELIGIOUS_BELIEF.toString()))
				.andExpect(jsonPath("$.ethnicOrigin").value(DEFAULT_ETHNIC_ORIGIN.toString()))
				.andExpect(jsonPath("$.disability").value(DEFAULT_DISABILITY.booleanValue()))
				.andExpect(jsonPath("$.disabilityDetails").value(DEFAULT_DISABILITY_DETAILS.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingEqualityAndDiversity() throws Exception {
		// Get the equalityAndDiversity
		restEqualityAndDiversityMockMvc.perform(get("/api/equality-and-diversities/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateEqualityAndDiversity() throws Exception {
		// Initialize the database
		equalityAndDiversityRepository.saveAndFlush(equalityAndDiversity);
		int databaseSizeBeforeUpdate = equalityAndDiversityRepository.findAll().size();

		// Update the equalityAndDiversity
		EqualityAndDiversity updatedEqualityAndDiversity = equalityAndDiversityRepository.findOne(equalityAndDiversity.getId());
		updatedEqualityAndDiversity
				.tisId(UPDATED_TIS_ID)
				.maritalStatus(UPDATED_MARITAL_STATUS)
				.dateOfBirth(UPDATED_DATE_OF_BIRTH)
				.gender(UPDATED_GENDER)
				.nationality(UPDATED_NATIONALITY)
				.dualNationality(UPDATED_DUAL_NATIONALITY)
				.sexualOrientation(UPDATED_SEXUAL_ORIENTATION)
				.religiousBelief(UPDATED_RELIGIOUS_BELIEF)
				.ethnicOrigin(UPDATED_ETHNIC_ORIGIN)
				.disability(UPDATED_DISABILITY)
				.disabilityDetails(UPDATED_DISABILITY_DETAILS);
		EqualityAndDiversityDTO equalityAndDiversityDTO = equalityAndDiversityMapper.equalityAndDiversityToEqualityAndDiversityDTO(updatedEqualityAndDiversity);

		restEqualityAndDiversityMockMvc.perform(put("/api/equality-and-diversities")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(equalityAndDiversityDTO)))
				.andExpect(status().isOk());

		// Validate the EqualityAndDiversity in the database
		List<EqualityAndDiversity> equalityAndDiversityList = equalityAndDiversityRepository.findAll();
		assertThat(equalityAndDiversityList).hasSize(databaseSizeBeforeUpdate);
		EqualityAndDiversity testEqualityAndDiversity = equalityAndDiversityList.get(equalityAndDiversityList.size() - 1);
		assertThat(testEqualityAndDiversity.getTisId()).isEqualTo(UPDATED_TIS_ID);
		assertThat(testEqualityAndDiversity.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
		assertThat(testEqualityAndDiversity.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
		assertThat(testEqualityAndDiversity.getGender()).isEqualTo(UPDATED_GENDER);
		assertThat(testEqualityAndDiversity.getNationality()).isEqualTo(UPDATED_NATIONALITY);
		assertThat(testEqualityAndDiversity.getDualNationality()).isEqualTo(UPDATED_DUAL_NATIONALITY);
		assertThat(testEqualityAndDiversity.getSexualOrientation()).isEqualTo(UPDATED_SEXUAL_ORIENTATION);
		assertThat(testEqualityAndDiversity.getReligiousBelief()).isEqualTo(UPDATED_RELIGIOUS_BELIEF);
		assertThat(testEqualityAndDiversity.getEthnicOrigin()).isEqualTo(UPDATED_ETHNIC_ORIGIN);
		assertThat(testEqualityAndDiversity.isDisability()).isEqualTo(UPDATED_DISABILITY);
		assertThat(testEqualityAndDiversity.getDisabilityDetails()).isEqualTo(UPDATED_DISABILITY_DETAILS);
	}

	@Test
	@Transactional
	public void updateNonExistingEqualityAndDiversity() throws Exception {
		int databaseSizeBeforeUpdate = equalityAndDiversityRepository.findAll().size();

		// Create the EqualityAndDiversity
		EqualityAndDiversityDTO equalityAndDiversityDTO = equalityAndDiversityMapper.equalityAndDiversityToEqualityAndDiversityDTO(equalityAndDiversity);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restEqualityAndDiversityMockMvc.perform(put("/api/equality-and-diversities")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(equalityAndDiversityDTO)))
				.andExpect(status().isCreated());

		// Validate the EqualityAndDiversity in the database
		List<EqualityAndDiversity> equalityAndDiversityList = equalityAndDiversityRepository.findAll();
		assertThat(equalityAndDiversityList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteEqualityAndDiversity() throws Exception {
		// Initialize the database
		equalityAndDiversityRepository.saveAndFlush(equalityAndDiversity);
		int databaseSizeBeforeDelete = equalityAndDiversityRepository.findAll().size();

		// Get the equalityAndDiversity
		restEqualityAndDiversityMockMvc.perform(delete("/api/equality-and-diversities/{id}", equalityAndDiversity.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<EqualityAndDiversity> equalityAndDiversityList = equalityAndDiversityRepository.findAll();
		assertThat(equalityAndDiversityList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(EqualityAndDiversity.class);
	}
}

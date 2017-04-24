package com.transformuk.hee.tis.profile.web.rest;

import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.GmcDetails;
import com.transformuk.hee.tis.profile.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.profile.service.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.profile.service.mapper.GmcDetailsMapper;
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
 * Test class for the GmcDetailsResource REST controller.
 *
 * @see GmcDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
public class GmcDetailsResourceIntTest {

	private static final BigDecimal DEFAULT_TIS_ID = new BigDecimal(1);
	private static final BigDecimal UPDATED_TIS_ID = new BigDecimal(2);

	private static final String DEFAULT_GMC_NUMBER = "AAAAAAA";
	private static final String UPDATED_GMC_NUMBER = "BBBBBBB";

	private static final String DEFAULT_GMC_STATUS = "AAAAAAAAAA";
	private static final String UPDATED_GMC_STATUS = "BBBBBBBBBB";

	private static final LocalDate DEFAULT_GMC_START_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_GMC_START_DATE = LocalDate.now(ZoneId.systemDefault());

	private static final LocalDate DEFAULT_GMC_EXPIRY_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_GMC_EXPIRY_DATE = LocalDate.now(ZoneId.systemDefault());

	private static final String DEFAULT_DESIGNATED_BODY_CODE = "AAAAAAAAAA";
	private static final String UPDATED_DESIGNATED_BODY_CODE = "BBBBBBBBBB";

	@Autowired
	private GmcDetailsRepository gmcDetailsRepository;

	@Autowired
	private GmcDetailsMapper gmcDetailsMapper;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restGmcDetailsMockMvc;

	private GmcDetails gmcDetails;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static GmcDetails createEntity(EntityManager em) {
		GmcDetails gmcDetails = new GmcDetails()
				.tisId(DEFAULT_TIS_ID)
				.gmcNumber(DEFAULT_GMC_NUMBER)
				.gmcStatus(DEFAULT_GMC_STATUS)
				.gmcStartDate(DEFAULT_GMC_START_DATE)
				.gmcExpiryDate(DEFAULT_GMC_EXPIRY_DATE)
				.designatedBodyCode(DEFAULT_DESIGNATED_BODY_CODE);
		return gmcDetails;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		GmcDetailsResource gmcDetailsResource = new GmcDetailsResource(gmcDetailsRepository, gmcDetailsMapper);
		this.restGmcDetailsMockMvc = MockMvcBuilders.standaloneSetup(gmcDetailsResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		gmcDetails = createEntity(em);
	}

	@Test
	@Transactional
	public void createGmcDetails() throws Exception {
		int databaseSizeBeforeCreate = gmcDetailsRepository.findAll().size();

		// Create the GmcDetails
		GmcDetailsDTO gmcDetailsDTO = gmcDetailsMapper.gmcDetailsToGmcDetailsDTO(gmcDetails);
		restGmcDetailsMockMvc.perform(post("/api/gmc-details")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gmcDetailsDTO)))
				.andExpect(status().isCreated());

		// Validate the GmcDetails in the database
		List<GmcDetails> gmcDetailsList = gmcDetailsRepository.findAll();
		assertThat(gmcDetailsList).hasSize(databaseSizeBeforeCreate + 1);
		GmcDetails testGmcDetails = gmcDetailsList.get(gmcDetailsList.size() - 1);
		assertThat(testGmcDetails.getTisId()).isEqualTo(DEFAULT_TIS_ID);
		assertThat(testGmcDetails.getGmcNumber()).isEqualTo(DEFAULT_GMC_NUMBER);
		assertThat(testGmcDetails.getGmcStatus()).isEqualTo(DEFAULT_GMC_STATUS);
		assertThat(testGmcDetails.getGmcStartDate()).isEqualTo(DEFAULT_GMC_START_DATE);
		assertThat(testGmcDetails.getGmcExpiryDate()).isEqualTo(DEFAULT_GMC_EXPIRY_DATE);
		assertThat(testGmcDetails.getDesignatedBodyCode()).isEqualTo(DEFAULT_DESIGNATED_BODY_CODE);
	}

	@Test
	@Transactional
	public void createGmcDetailsWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = gmcDetailsRepository.findAll().size();

		// Create the GmcDetails with an existing ID
		gmcDetails.setId(1L);
		GmcDetailsDTO gmcDetailsDTO = gmcDetailsMapper.gmcDetailsToGmcDetailsDTO(gmcDetails);

		// An entity with an existing ID cannot be created, so this API call must fail
		restGmcDetailsMockMvc.perform(post("/api/gmc-details")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gmcDetailsDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<GmcDetails> gmcDetailsList = gmcDetailsRepository.findAll();
		assertThat(gmcDetailsList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void checkTisIdIsRequired() throws Exception {
		int databaseSizeBeforeTest = gmcDetailsRepository.findAll().size();
		// set the field null
		gmcDetails.setTisId(null);

		// Create the GmcDetails, which fails.
		GmcDetailsDTO gmcDetailsDTO = gmcDetailsMapper.gmcDetailsToGmcDetailsDTO(gmcDetails);

		restGmcDetailsMockMvc.perform(post("/api/gmc-details")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gmcDetailsDTO)))
				.andExpect(status().isBadRequest());

		List<GmcDetails> gmcDetailsList = gmcDetailsRepository.findAll();
		assertThat(gmcDetailsList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllGmcDetails() throws Exception {
		// Initialize the database
		gmcDetailsRepository.saveAndFlush(gmcDetails);

		// Get all the gmcDetailsList
		restGmcDetailsMockMvc.perform(get("/api/gmc-details?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(gmcDetails.getId().intValue())))
				.andExpect(jsonPath("$.[*].tisId").value(hasItem(DEFAULT_TIS_ID.intValue())))
				.andExpect(jsonPath("$.[*].gmcNumber").value(hasItem(DEFAULT_GMC_NUMBER.toString())))
				.andExpect(jsonPath("$.[*].gmcStatus").value(hasItem(DEFAULT_GMC_STATUS.toString())))
				.andExpect(jsonPath("$.[*].gmcStartDate").value(hasItem(DEFAULT_GMC_START_DATE.toString())))
				.andExpect(jsonPath("$.[*].gmcExpiryDate").value(hasItem(DEFAULT_GMC_EXPIRY_DATE.toString())))
				.andExpect(jsonPath("$.[*].designatedBodyCode").value(hasItem(DEFAULT_DESIGNATED_BODY_CODE.toString())));
	}

	@Test
	@Transactional
	public void getGmcDetails() throws Exception {
		// Initialize the database
		gmcDetailsRepository.saveAndFlush(gmcDetails);

		// Get the gmcDetails
		restGmcDetailsMockMvc.perform(get("/api/gmc-details/{id}", gmcDetails.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(gmcDetails.getId().intValue()))
				.andExpect(jsonPath("$.tisId").value(DEFAULT_TIS_ID.intValue()))
				.andExpect(jsonPath("$.gmcNumber").value(DEFAULT_GMC_NUMBER.toString()))
				.andExpect(jsonPath("$.gmcStatus").value(DEFAULT_GMC_STATUS.toString()))
				.andExpect(jsonPath("$.gmcStartDate").value(DEFAULT_GMC_START_DATE.toString()))
				.andExpect(jsonPath("$.gmcExpiryDate").value(DEFAULT_GMC_EXPIRY_DATE.toString()))
				.andExpect(jsonPath("$.designatedBodyCode").value(DEFAULT_DESIGNATED_BODY_CODE.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingGmcDetails() throws Exception {
		// Get the gmcDetails
		restGmcDetailsMockMvc.perform(get("/api/gmc-details/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateGmcDetails() throws Exception {
		// Initialize the database
		gmcDetailsRepository.saveAndFlush(gmcDetails);
		int databaseSizeBeforeUpdate = gmcDetailsRepository.findAll().size();

		// Update the gmcDetails
		GmcDetails updatedGmcDetails = gmcDetailsRepository.findOne(gmcDetails.getId());
		updatedGmcDetails
				.tisId(UPDATED_TIS_ID)
				.gmcNumber(UPDATED_GMC_NUMBER)
				.gmcStatus(UPDATED_GMC_STATUS)
				.gmcStartDate(UPDATED_GMC_START_DATE)
				.gmcExpiryDate(UPDATED_GMC_EXPIRY_DATE)
				.designatedBodyCode(UPDATED_DESIGNATED_BODY_CODE);
		GmcDetailsDTO gmcDetailsDTO = gmcDetailsMapper.gmcDetailsToGmcDetailsDTO(updatedGmcDetails);

		restGmcDetailsMockMvc.perform(put("/api/gmc-details")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gmcDetailsDTO)))
				.andExpect(status().isOk());

		// Validate the GmcDetails in the database
		List<GmcDetails> gmcDetailsList = gmcDetailsRepository.findAll();
		assertThat(gmcDetailsList).hasSize(databaseSizeBeforeUpdate);
		GmcDetails testGmcDetails = gmcDetailsList.get(gmcDetailsList.size() - 1);
		assertThat(testGmcDetails.getTisId()).isEqualTo(UPDATED_TIS_ID);
		assertThat(testGmcDetails.getGmcNumber()).isEqualTo(UPDATED_GMC_NUMBER);
		assertThat(testGmcDetails.getGmcStatus()).isEqualTo(UPDATED_GMC_STATUS);
		assertThat(testGmcDetails.getGmcStartDate()).isEqualTo(UPDATED_GMC_START_DATE);
		assertThat(testGmcDetails.getGmcExpiryDate()).isEqualTo(UPDATED_GMC_EXPIRY_DATE);
		assertThat(testGmcDetails.getDesignatedBodyCode()).isEqualTo(UPDATED_DESIGNATED_BODY_CODE);
	}

	@Test
	@Transactional
	public void updateNonExistingGmcDetails() throws Exception {
		int databaseSizeBeforeUpdate = gmcDetailsRepository.findAll().size();

		// Create the GmcDetails
		GmcDetailsDTO gmcDetailsDTO = gmcDetailsMapper.gmcDetailsToGmcDetailsDTO(gmcDetails);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restGmcDetailsMockMvc.perform(put("/api/gmc-details")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gmcDetailsDTO)))
				.andExpect(status().isCreated());

		// Validate the GmcDetails in the database
		List<GmcDetails> gmcDetailsList = gmcDetailsRepository.findAll();
		assertThat(gmcDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteGmcDetails() throws Exception {
		// Initialize the database
		gmcDetailsRepository.saveAndFlush(gmcDetails);
		int databaseSizeBeforeDelete = gmcDetailsRepository.findAll().size();

		// Get the gmcDetails
		restGmcDetailsMockMvc.perform(delete("/api/gmc-details/{id}", gmcDetails.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<GmcDetails> gmcDetailsList = gmcDetailsRepository.findAll();
		assertThat(gmcDetailsList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(GmcDetails.class);
	}
}

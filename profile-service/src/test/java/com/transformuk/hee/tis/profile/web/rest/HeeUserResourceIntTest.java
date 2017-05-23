package com.transformuk.hee.tis.profile.web.rest;

import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.profile.repository.PermissionRepository;
import com.transformuk.hee.tis.profile.service.KeyclockAdminClientService;
import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import com.transformuk.hee.tis.profile.service.mapper.HeeUserMapper;
import com.transformuk.hee.tis.profile.validators.HeeUserValidator;
import com.transformuk.hee.tis.profile.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.util.List;

import static com.transformuk.hee.tis.profile.web.rest.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HeeUserResource REST controller.
 *
 * @see HeeUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
public class HeeUserResourceIntTest {


	@Autowired
	private HeeUserRepository heeUserRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private HeeUserMapper heeUserMapper;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Mock
	private KeyclockAdminClientService keyclockAdminClientService;

	@Mock
	private HeeUserValidator heeUserValidator;

	@Autowired
	private EntityManager em;

	private MockMvc restHeeUserMockMvc;

	private HeeUser heeUser;


	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		HeeUserResource heeUserResource = new HeeUserResource(heeUserRepository, heeUserMapper,
				keyclockAdminClientService, heeUserValidator);
		this.restHeeUserMockMvc = MockMvcBuilders.standaloneSetup(heeUserResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		heeUser = createEntityHeeUser(em);
	}

	@Test
	@Transactional
	public void createHeeUser() throws Exception {
		int databaseSizeBeforeCreate = heeUserRepository.findAll().size();
		int databasePermissionSizeBeforeCreate = permissionRepository.findAll().size();

		// Create the HeeUser
		HeeUserDTO heeUserDTO = heeUserMapper.heeUserToHeeUserDTO(heeUser);
		restHeeUserMockMvc.perform(post("/api/hee-users")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(heeUserDTO)))
				.andExpect(status().isCreated());

		// Validate the HeeUser in the database
		List<HeeUser> heeUserList = heeUserRepository.findAll();
		assertThat(heeUserList).hasSize(databaseSizeBeforeCreate + 1);
		HeeUser testHeeUser = heeUserRepository.findOne(DEFAULT_NAME);
		assertThat(testHeeUser.getName()).isEqualTo(DEFAULT_NAME);
		assertThat(testHeeUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
		assertThat(testHeeUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
		assertThat(testHeeUser.getGmcId()).isEqualTo(DEFAULT_GMC_ID);
		assertThat(testHeeUser.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
		assertThat(testHeeUser.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
		assertThat(testHeeUser.isActive()).isEqualTo(DEFAULT_ACTIVE);
		assertThat(permissionRepository.findAll().size()).isEqualTo(databasePermissionSizeBeforeCreate);

	}

	@Test
	@Transactional
	public void createHeeUserWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = heeUserRepository.findAll().size();

		// Create the HeeUser with an existing name
		heeUser.setName(DEFAULT_NAME);
		HeeUserDTO heeUserDTO = heeUserMapper.heeUserToHeeUserDTO(heeUser);

		// A user with the same name will update the existing user
		restHeeUserMockMvc.perform(post("/api/hee-users")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(heeUserDTO)))
				.andExpect(status().isCreated());

		// Validate the Alice in the database
		List<HeeUser> heeUserList = heeUserRepository.findAll();
		assertThat(heeUserList).hasSize(databaseSizeBeforeCreate + 1);
	}

	@Test
	@Transactional
	public void checkNameIsRequired() throws Exception {
		int databaseSizeBeforeTest = heeUserRepository.findAll().size();
		// set the field null
		heeUser.setName(null);

		// Create the HeeUser, which fails.
		HeeUserDTO heeUserDTO = heeUserMapper.heeUserToHeeUserDTO(heeUser);

		restHeeUserMockMvc.perform(post("/api/hee-users")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(heeUserDTO)))
				.andExpect(status().isBadRequest());

		List<HeeUser> heeUserList = heeUserRepository.findAll();
		assertThat(heeUserList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllHeeUsers() throws Exception {
		// Initialize the database
		heeUserRepository.saveAndFlush(heeUser);

		// Get all the heeUserList
		restHeeUserMockMvc.perform(get("/api/hee-users?sort=name,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
				.andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
				.andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
				.andExpect(jsonPath("$.[*].gmcId").value(hasItem(DEFAULT_GMC_ID.toString())))
				.andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
				.andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS.toString())))
				.andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
	}

	@Test
	@Transactional
	public void getHeeUser() throws Exception {
		// Initialize the database
		heeUserRepository.saveAndFlush(heeUser);

		// Get the heeUser
		restHeeUserMockMvc.perform(get("/api/hee-users/{id}", heeUser.getName()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
				.andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
				.andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
				.andExpect(jsonPath("$.gmcId").value(DEFAULT_GMC_ID.toString()))
				.andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
				.andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS.toString()))
				.andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
	}

	@Test
	@Transactional
	public void getNonExistingHeeUser() throws Exception {
		// Get the heeUser
		restHeeUserMockMvc.perform(get("/api/hee-users/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateHeeUser() throws Exception {
		// Initialize the database
		heeUserRepository.saveAndFlush(heeUser);
		int databaseSizeBeforeUpdate = heeUserRepository.findAll().size();
		int databasePermissionSizeBeforeCreate = permissionRepository.findAll().size();

		// Update the heeUser
		HeeUser updatedHeeUser = heeUserRepository.findOne(heeUser.getName());
		updatedHeeUser
				.firstName(UPDATED_FIRST_NAME)
				.lastName(UPDATED_LAST_NAME)
				.gmcId(UPDATED_GMC_ID)
				.phoneNumber(UPDATED_PHONE_NUMBER)
				.emailAddress(UPDATED_EMAIL_ADDRESS)
				.active(UPDATED_ACTIVE);
		HeeUserDTO heeUserDTO = heeUserMapper.heeUserToHeeUserDTO(updatedHeeUser);

		restHeeUserMockMvc.perform(put("/api/hee-users")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(heeUserDTO)))
				.andExpect(status().isOk());

		// Validate the HeeUser in the database
		List<HeeUser> heeUserList = heeUserRepository.findAll();
		assertThat(heeUserList).hasSize(databaseSizeBeforeUpdate);
		HeeUser testHeeUser = heeUserRepository.findOne(DEFAULT_NAME);
		assertThat(testHeeUser.getName()).isEqualTo(DEFAULT_NAME);
		assertThat(testHeeUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
		assertThat(testHeeUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
		assertThat(testHeeUser.getGmcId()).isEqualTo(UPDATED_GMC_ID);
		assertThat(testHeeUser.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
		assertThat(testHeeUser.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
		assertThat(testHeeUser.isActive()).isEqualTo(UPDATED_ACTIVE);

		assertThat(permissionRepository.findAll().size()).isEqualTo(databasePermissionSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void updateNonExistingHeeUser() throws Exception {
		int databaseSizeBeforeUpdate = heeUserRepository.findAll().size();

		// Create the HeeUser
		HeeUserDTO heeUserDTO = heeUserMapper.heeUserToHeeUserDTO(heeUser);
		heeUserDTO.setName(UPDATED_NAME);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restHeeUserMockMvc.perform(put("/api/hee-users")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(heeUserDTO)))
				.andExpect(status().is2xxSuccessful());

		// Validate the HeeUser in the database
		List<HeeUser> heeUserList = heeUserRepository.findAll();
		assertThat(heeUserList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteHeeUser() throws Exception {
		// Initialize the database
		heeUserRepository.saveAndFlush(heeUser);
		int databaseSizeBeforeDelete = heeUserRepository.findAll().size();

		// Get the heeUser
		restHeeUserMockMvc.perform(delete("/api/hee-users/{id}", heeUser.getName())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<HeeUser> heeUserList = heeUserRepository.findAll();
		assertThat(heeUserList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(HeeUser.class);
	}
}

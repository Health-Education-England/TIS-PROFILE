package com.transformuk.hee.tis.profile.web.rest;

import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.Person;
import com.transformuk.hee.tis.profile.repository.PersonRepository;
import com.transformuk.hee.tis.profile.service.dto.PersonDTO;
import com.transformuk.hee.tis.profile.service.mapper.PersonMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PersonResource REST controller.
 *
 * @see PersonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
public class PersonResourceIntTest {

	private static final BigDecimal DEFAULT_TIS_ID = new BigDecimal(1);
	private static final BigDecimal UPDATED_TIS_ID = new BigDecimal(2);

	private static final String DEFAULT_PUBLIC_HEALTH_ID = "AAAAAAAAAA";
	private static final String UPDATED_PUBLIC_HEALTH_ID = "BBBBBBBBBB";

	private static final Boolean DEFAULT_ACTIVE = false;
	private static final Boolean UPDATED_ACTIVE = true;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private PersonMapper personMapper;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restPersonMockMvc;

	private Person person;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static Person createEntity(EntityManager em) {
		Person person = new Person()
				.tisId(DEFAULT_TIS_ID)
				.publicHealthId(DEFAULT_PUBLIC_HEALTH_ID)
				.active(DEFAULT_ACTIVE);
		return person;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		PersonResource personResource = new PersonResource(personRepository, personMapper);
		this.restPersonMockMvc = MockMvcBuilders.standaloneSetup(personResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		person = createEntity(em);
	}

	@Test
	@Transactional
	public void createPerson() throws Exception {
		int databaseSizeBeforeCreate = personRepository.findAll().size();

		// Create the Person
		PersonDTO personDTO = personMapper.personToPersonDTO(person);
		restPersonMockMvc.perform(post("/api/people")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(personDTO)))
				.andExpect(status().isCreated());

		// Validate the Person in the database
		List<Person> personList = personRepository.findAll();
		assertThat(personList).hasSize(databaseSizeBeforeCreate + 1);
		Person testPerson = personList.get(personList.size() - 1);
		assertThat(testPerson.getTisId()).isEqualTo(DEFAULT_TIS_ID);
		assertThat(testPerson.getPublicHealthId()).isEqualTo(DEFAULT_PUBLIC_HEALTH_ID);
		assertThat(testPerson.isActive()).isEqualTo(DEFAULT_ACTIVE);
	}

	@Test
	@Transactional
	public void createPersonWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = personRepository.findAll().size();

		// Create the Person with an existing ID
		person.setId(1L);
		PersonDTO personDTO = personMapper.personToPersonDTO(person);

		// An entity with an existing ID cannot be created, so this API call must fail
		restPersonMockMvc.perform(post("/api/people")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(personDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<Person> personList = personRepository.findAll();
		assertThat(personList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void checkTisIdIsRequired() throws Exception {
		int databaseSizeBeforeTest = personRepository.findAll().size();
		// set the field null
		person.setTisId(null);

		// Create the Person, which fails.
		PersonDTO personDTO = personMapper.personToPersonDTO(person);

		restPersonMockMvc.perform(post("/api/people")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(personDTO)))
				.andExpect(status().isBadRequest());

		List<Person> personList = personRepository.findAll();
		assertThat(personList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllPeople() throws Exception {
		// Initialize the database
		personRepository.saveAndFlush(person);

		// Get all the personList
		restPersonMockMvc.perform(get("/api/people?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
				.andExpect(jsonPath("$.[*].tisId").value(hasItem(DEFAULT_TIS_ID.intValue())))
				.andExpect(jsonPath("$.[*].publicHealthId").value(hasItem(DEFAULT_PUBLIC_HEALTH_ID.toString())))
				.andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
	}

	@Test
	@Transactional
	public void getPerson() throws Exception {
		// Initialize the database
		personRepository.saveAndFlush(person);

		// Get the person
		restPersonMockMvc.perform(get("/api/people/{id}", person.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(person.getId().intValue()))
				.andExpect(jsonPath("$.tisId").value(DEFAULT_TIS_ID.intValue()))
				.andExpect(jsonPath("$.publicHealthId").value(DEFAULT_PUBLIC_HEALTH_ID.toString()))
				.andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
	}

	@Test
	@Transactional
	public void getNonExistingPerson() throws Exception {
		// Get the person
		restPersonMockMvc.perform(get("/api/people/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updatePerson() throws Exception {
		// Initialize the database
		personRepository.saveAndFlush(person);
		int databaseSizeBeforeUpdate = personRepository.findAll().size();

		// Update the person
		Person updatedPerson = personRepository.findOne(person.getId());
		updatedPerson
				.tisId(UPDATED_TIS_ID)
				.publicHealthId(UPDATED_PUBLIC_HEALTH_ID)
				.active(UPDATED_ACTIVE);
		PersonDTO personDTO = personMapper.personToPersonDTO(updatedPerson);

		restPersonMockMvc.perform(put("/api/people")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(personDTO)))
				.andExpect(status().isOk());

		// Validate the Person in the database
		List<Person> personList = personRepository.findAll();
		assertThat(personList).hasSize(databaseSizeBeforeUpdate);
		Person testPerson = personList.get(personList.size() - 1);
		assertThat(testPerson.getTisId()).isEqualTo(UPDATED_TIS_ID);
		assertThat(testPerson.getPublicHealthId()).isEqualTo(UPDATED_PUBLIC_HEALTH_ID);
		assertThat(testPerson.isActive()).isEqualTo(UPDATED_ACTIVE);
	}

	@Test
	@Transactional
	public void updateNonExistingPerson() throws Exception {
		int databaseSizeBeforeUpdate = personRepository.findAll().size();

		// Create the Person
		PersonDTO personDTO = personMapper.personToPersonDTO(person);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restPersonMockMvc.perform(put("/api/people")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(personDTO)))
				.andExpect(status().isCreated());

		// Validate the Person in the database
		List<Person> personList = personRepository.findAll();
		assertThat(personList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deletePerson() throws Exception {
		// Initialize the database
		personRepository.saveAndFlush(person);
		int databaseSizeBeforeDelete = personRepository.findAll().size();

		// Get the person
		restPersonMockMvc.perform(delete("/api/people/{id}", person.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<Person> personList = personRepository.findAll();
		assertThat(personList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Person.class);
	}
}

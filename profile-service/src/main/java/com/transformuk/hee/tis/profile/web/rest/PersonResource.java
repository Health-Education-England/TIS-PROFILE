package com.transformuk.hee.tis.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.profile.domain.Person;
import com.transformuk.hee.tis.profile.repository.PersonRepository;
import com.transformuk.hee.tis.profile.service.dto.PersonDTO;
import com.transformuk.hee.tis.profile.service.mapper.PersonMapper;
import com.transformuk.hee.tis.profile.web.rest.util.HeaderUtil;
import com.transformuk.hee.tis.profile.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Person.
 */
@RestController
@RequestMapping("/api")
public class PersonResource {

	private static final String ENTITY_NAME = "person";
	private final Logger log = LoggerFactory.getLogger(PersonResource.class);
	private final PersonRepository personRepository;

	private final PersonMapper personMapper;

	public PersonResource(PersonRepository personRepository, PersonMapper personMapper) {
		this.personRepository = personRepository;
		this.personMapper = personMapper;
	}

	/**
	 * POST  /people : Create a new person.
	 *
	 * @param personDTO the personDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new personDTO, or with status 400 (Bad Request) if the person has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/people")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<PersonDTO> createPerson(@Valid @RequestBody PersonDTO personDTO) throws URISyntaxException {
		log.debug("REST request to save Person : {}", personDTO);
		if (personDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new person cannot already have an ID")).body(null);
		}
		Person person = personMapper.personDTOToPerson(personDTO);
		person = personRepository.save(person);
		PersonDTO result = personMapper.personToPersonDTO(person);
		return ResponseEntity.created(new URI("/api/people/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /people : Updates an existing person.
	 *
	 * @param personDTO the personDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated personDTO,
	 * or with status 400 (Bad Request) if the personDTO is not valid,
	 * or with status 500 (Internal Server Error) if the personDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/people")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<PersonDTO> updatePerson(@Valid @RequestBody PersonDTO personDTO) throws URISyntaxException {
		log.debug("REST request to update Person : {}", personDTO);
		if (personDTO.getId() == null) {
			return createPerson(personDTO);
		}
		Person person = personMapper.personDTOToPerson(personDTO);
		person = personRepository.save(person);
		PersonDTO result = personMapper.personToPersonDTO(person);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, personDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /people : get all the people.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of people in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/people")
	@Timed
	@PreAuthorize("hasAuthority('profile:view:entities')")
	public ResponseEntity<List<PersonDTO>> getAllPeople(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of People");
		Page<Person> page = personRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/people");
		return new ResponseEntity<>(personMapper.peopleToPersonDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET  /people/:id : get the "id" person.
	 *
	 * @param id the id of the personDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the personDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/people/{id}")
	@Timed
	@PreAuthorize("hasAuthority('profile:view:entities')")
	public ResponseEntity<PersonDTO> getPerson(@PathVariable Long id) {
		log.debug("REST request to get Person : {}", id);
		Person person = personRepository.findOne(id);
		PersonDTO personDTO = personMapper.personToPersonDTO(person);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(personDTO));
	}

	/**
	 * DELETE  /people/:id : delete the "id" person.
	 *
	 * @param id the id of the personDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/people/{id}")
	@Timed
	@PreAuthorize("hasAuthority('profile:delete:entities')")
	public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
		log.debug("REST request to delete Person : {}", id);
		personRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}


	/**
	 * POST  /bulk-people : Bulk create a new Person.
	 *
	 * @param personDTOS List of the personDTOS to create
	 * @return the ResponseEntity with status 200 (Created) and with body the new personDTOS, or with status 400 (Bad Request) if the Person has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/bulk-people")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<List<PersonDTO>> bulkCreatePerson(@Valid @RequestBody List<PersonDTO> personDTOS) throws URISyntaxException {
		log.debug("REST request to bulk save EqualityAndDiversity : {}", personDTOS);
		if (!Collections.isEmpty(personDTOS)) {
			List<Long> entityIds = personDTOS.stream()
					.filter(p -> p.getId() != null)
					.map(p -> p.getId())
					.collect(Collectors.toList());
			if (!Collections.isEmpty(entityIds)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new Person cannot already have an ID")).body(null);
			}
		}
		List<Person> personList = personMapper.personDTOsToPeople(personDTOS);
		personList = personRepository.save(personList);
		List<PersonDTO> result = personMapper.peopleToPersonDTOs(personList);
		List<Long> ids = result.stream().map(at -> at.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(result);
	}

	/**
	 * PUT  /bulk-people : Updates an existing Person.
	 *
	 * @param personDTOS List of the personDTOS to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated personDTOS,
	 * or with status 400 (Bad Request) if the personDTOS is not valid,
	 * or with status 500 (Internal Server Error) if the personDTOS couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/bulk-people")
	@Timed
	@PreAuthorize("hasAuthority('profile:add:modify:entities')")
	public ResponseEntity<List<PersonDTO>> bulkUpdatePerson(@Valid @RequestBody List<PersonDTO> personDTOS) throws URISyntaxException {
		log.debug("REST request to bulk update Person : {}", personDTOS);
		if (Collections.isEmpty(personDTOS)) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
					"The request body for this end point cannot be empty")).body(null);
		} else if (!Collections.isEmpty(personDTOS)) {
			List<PersonDTO> entitiesWithNoId = personDTOS.stream().filter(p -> p.getId() == null).collect(Collectors.toList());
			if (!Collections.isEmpty(entitiesWithNoId)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
						"bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
			}
		}
		List<Person> personList = personMapper.personDTOsToPeople(personDTOS);
		personList = personRepository.save(personList);
		List<PersonDTO> results = personMapper.peopleToPersonDTOs(personList);
		List<Long> ids = results.stream().map(p -> p.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(results);
	}
}

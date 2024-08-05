package com.transformuk.hee.tis.profile.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.JsonPatch;
import com.transformuk.hee.tis.profile.repository.JsonPatchRepository;
import com.transformuk.hee.tis.profile.service.mapper.JsonPatchMapper;
import com.transformuk.hee.tis.profile.web.rest.errors.ExceptionTranslator;
import com.transformuk.hee.tis.reference.api.dto.JsonPatchDTO;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the JsonPatchResource REST controller.
 *
 * @see JsonPatchResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
public class JsonPatchResourceIntTest {

  private static final String DEFAULT_TABLE_DTO_NAME = "AAAAAAAAAA";
  private static final String DEFAULT_PATCH = "[{\"op\": \"replace\", \"path\": \"/nationality\", \"value\": \"United Arab Emirates Rebase Test\"}]";
  private static final String DEFAULT_PATCH_ID = "1";

  private static final String UPDATED_DEFAULT_PATCH_ID = "2";
  private static final String UPDATED_DEFAULT_PATCH = "[{\"op\": \"replace\", \"path\": \"/nationality\", \"value\": \"United Arab Emirates\"}]";

  @Autowired
  private JsonPatchRepository jsonPatchRepository;

  @Autowired
  private JsonPatchMapper jsonPatchMapper;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  private MockMvc restCountryMockMvc;

  private JsonPatch jsonPatch;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static JsonPatch createEntity() {
    return new JsonPatch()
        .tableDtoName(DEFAULT_TABLE_DTO_NAME)
        .patchId(DEFAULT_PATCH_ID)
        .patch(DEFAULT_PATCH)
        .enabled(true)
        .dateAdded(new Date());
  }

  @Before
  public void setup() {
    JsonPatchResource jsonPatchResource = new JsonPatchResource(jsonPatchRepository,
        jsonPatchMapper);
    this.restCountryMockMvc = MockMvcBuilders.standaloneSetup(jsonPatchResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    jsonPatch = createEntity();
  }

  @Test
  @Transactional
  public void createJsonPatch() throws Exception {
    int databaseSizeBeforeCreate = jsonPatchRepository.findAll().size();

    // Create the JsonPatch
    JsonPatchDTO jsonPatchDTO = jsonPatchMapper.jsonPatchToJsonPatchDTO(jsonPatch);
    restCountryMockMvc.perform(post("/api/jsonPatches")
            .contentType(TestUtil.JSON)
            .content(TestUtil.convertObjectToJsonBytes(jsonPatchDTO)))
        .andExpect(status().isCreated());

    // Validate the JsonPatch in the database
    List<JsonPatch> jsonPatchList = jsonPatchRepository.findAll();
    assertThat(jsonPatchList).hasSize(databaseSizeBeforeCreate + 1);
    JsonPatch testJsonPatch = jsonPatchList.get(jsonPatchList.size() - 1);
    assertThat(testJsonPatch.getTableDtoName()).isEqualTo(DEFAULT_TABLE_DTO_NAME);
    assertThat(testJsonPatch.getPatchId()).isEqualTo(DEFAULT_PATCH_ID);
    assertThat(testJsonPatch.getPatch()).isEqualTo(DEFAULT_PATCH);
    assertThat(testJsonPatch.getEnabled()).isTrue();

  }

  @Test
  @Transactional
  public void createJsonPatchWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = jsonPatchRepository.findAll().size();

    // Create the JsonPatch with an existing ID
    jsonPatch.setId(1L);
    JsonPatchDTO jsonPatchDTO = jsonPatchMapper.jsonPatchToJsonPatchDTO(jsonPatch);

    // An entity with an existing ID cannot be created, so this API call must fail
    restCountryMockMvc.perform(post("/api/jsonPatches")
            .contentType(TestUtil.JSON)
            .content(TestUtil.convertObjectToJsonBytes(jsonPatchDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<JsonPatch> jsonPatchList = jsonPatchRepository.findAll();
    assertThat(jsonPatchList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkTableDtoNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = jsonPatchRepository.findAll().size();
    // set the field null
    jsonPatch.setTableDtoName(null);

    // Create the Country, which fails.
    JsonPatchDTO jsonPatchDTO = jsonPatchMapper.jsonPatchToJsonPatchDTO(jsonPatch);

    restCountryMockMvc.perform(post("/api/jsonPatches")
            .contentType(TestUtil.JSON)
            .content(TestUtil.convertObjectToJsonBytes(jsonPatchDTO)))
        .andExpect(status().isBadRequest());

    List<JsonPatch> jsonPatchList = jsonPatchRepository.findAll();
    assertThat(jsonPatchList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllJsonPatches() throws Exception {
    // Initialize the database
    jsonPatchRepository.saveAndFlush(jsonPatch);

    // Get all the jsonPatchList
    restCountryMockMvc.perform(get("/api/jsonPatches?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.JSON))
        .andExpect(jsonPath("$.[*].id").value(hasItem(jsonPatch.getId().intValue())))
        .andExpect(jsonPath("$.[*].tableDtoName").value(hasItem(DEFAULT_TABLE_DTO_NAME)))
        .andExpect(jsonPath("$.[*].patchId").value(hasItem(DEFAULT_PATCH_ID)))
        .andExpect(jsonPath("$.[*].patch").value(hasItem(DEFAULT_PATCH)))
        .andExpect(jsonPath("$.[*].enabled").value(hasItem(true)));
  }

  @Test
  @Transactional
  public void getUpdateTypeJsonPatches() throws Exception {
    // Initialize the database
    jsonPatchRepository.saveAndFlush(jsonPatch);

    // Get all the jsonPatchList
    restCountryMockMvc.perform(
            get("/api/jsonPatches/updateType/{table}?sort=id,desc", DEFAULT_TABLE_DTO_NAME))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.JSON))
        .andExpect(jsonPath("$.[*].id").value(hasItem(jsonPatch.getId().intValue())))
        .andExpect(jsonPath("$.[*].tableDtoName").value(hasItem(DEFAULT_TABLE_DTO_NAME)))
        .andExpect(jsonPath("$.[*].patchId").value(hasItem(DEFAULT_PATCH_ID)))
        .andExpect(jsonPath("$.[*].patch").value(hasItem(DEFAULT_PATCH)))
        .andExpect(jsonPath("$.[*].enabled").value(hasItem(true)));
  }

  @Test
  @Transactional
  public void getJsonPatch() throws Exception {
    // Initialize the database
    jsonPatchRepository.saveAndFlush(jsonPatch);

    // Get the jsonPatch
    restCountryMockMvc.perform(get("/api/jsonPatches/{id}", jsonPatch.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.JSON))
        .andExpect(jsonPath("$.id").value(jsonPatch.getId().intValue()))
        .andExpect(jsonPath("$.tableDtoName").value(DEFAULT_TABLE_DTO_NAME))
        .andExpect(jsonPath("$.patchId").value(DEFAULT_PATCH_ID))
        .andExpect(jsonPath("$.patch").value(DEFAULT_PATCH))
        .andExpect(jsonPath("$.enabled").value(true));
  }

  @Test
  @Transactional
  public void getNonExistingJsonPatch() throws Exception {
    // Get the jsonPatch
    restCountryMockMvc.perform(get("/api/jsonPatches/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateJsonPatch() throws Exception {
    // Initialize the database
    jsonPatchRepository.saveAndFlush(jsonPatch);
    int databaseSizeBeforeUpdate = jsonPatchRepository.findAll().size();

    // Update the jsonPatch
    JsonPatch updateJsonPatch = jsonPatchRepository.getById(jsonPatch.getId());
    updateJsonPatch
        .patchId(UPDATED_DEFAULT_PATCH_ID)
        .patch(UPDATED_DEFAULT_PATCH);
    JsonPatchDTO jsonPatchDTO = jsonPatchMapper.jsonPatchToJsonPatchDTO(updateJsonPatch);

    restCountryMockMvc.perform(put("/api/jsonPatches")
            .contentType(TestUtil.JSON)
            .content(TestUtil.convertObjectToJsonBytes(jsonPatchDTO)))
        .andExpect(status().isOk());

    // Validate the JsonPatch in the database
    List<JsonPatch> jsonPatchList = jsonPatchRepository.findAll();
    assertThat(jsonPatchList).hasSize(databaseSizeBeforeUpdate);
    JsonPatch testJsonPatch = jsonPatchList.get(jsonPatchList.size() - 1);
    assertThat(testJsonPatch.getPatchId()).isEqualTo(UPDATED_DEFAULT_PATCH_ID);
    assertThat(testJsonPatch.getPatch()).isEqualTo(UPDATED_DEFAULT_PATCH);
  }

  @Test
  @Transactional
  public void bulkSoftDeleteJsonPatch() throws Exception {
    // Initialize the database
    jsonPatchRepository.saveAndFlush(jsonPatch);
    int databaseSizeBeforeUpdate = jsonPatchRepository.findAll().size();

    // Update the jsonPatch
    JsonPatch updateJsonPatch = jsonPatchRepository.getById(jsonPatch.getId());
    updateJsonPatch
        .patchId(UPDATED_DEFAULT_PATCH_ID)
        .patch(UPDATED_DEFAULT_PATCH);
    JsonPatchDTO jsonPatchDto = jsonPatchMapper.jsonPatchToJsonPatchDTO(updateJsonPatch);

    restCountryMockMvc.perform(put("/api/bulk-jsonPatches")
            .contentType(TestUtil.JSON)
            .content(TestUtil.convertObjectToJsonBytes(Collections.singletonList(jsonPatchDto))))
        .andExpect(status().isOk());

    // Validate the JsonPatch in the database
    List<JsonPatch> jsonPatchList = jsonPatchRepository.findAll();
    assertThat(jsonPatchList).hasSize(databaseSizeBeforeUpdate);
    JsonPatch testJsonPatch = jsonPatchList.get(jsonPatchList.size() - 1);
    assertThat(testJsonPatch.getPatchId()).isEqualTo(UPDATED_DEFAULT_PATCH_ID);
    assertThat(testJsonPatch.getPatch()).isEqualTo(UPDATED_DEFAULT_PATCH);
    assertThat(testJsonPatch.getEnabled()).isFalse();
  }

  @Test
  @Transactional
  public void bulkSoftDeleteBadRequest() throws Exception {
    // Initialize the database
    JsonPatchDTO patchWithoutId = jsonPatchMapper.jsonPatchToJsonPatchDTO(createEntity());
    jsonPatchRepository.saveAndFlush(jsonPatch);
    int databaseSizeBeforeUpdate = jsonPatchRepository.findAll().size();

    // Update the jsonPatch
    JsonPatch updateJsonPatch = jsonPatchRepository.getById(jsonPatch.getId());
    JsonPatchDTO jsonPatchDto = jsonPatchMapper.jsonPatchToJsonPatchDTO(updateJsonPatch);

    restCountryMockMvc.perform(put("/api/bulk-jsonPatches")
            .contentType(TestUtil.JSON)
            .content(TestUtil.convertObjectToJsonBytes(Arrays.asList(jsonPatchDto, patchWithoutId))))
        .andExpect(status().isBadRequest());

    // Validate the JsonPatch in the database
    List<JsonPatch> jsonPatchList = jsonPatchRepository.findAll();
    assertThat(jsonPatchList).hasSize(databaseSizeBeforeUpdate);
    JsonPatch testJsonPatch = jsonPatchList.get(jsonPatchList.size() - 1);
    assertThat(testJsonPatch.getPatchId()).isEqualTo(DEFAULT_PATCH_ID);
    assertThat(testJsonPatch.getPatch()).isEqualTo(DEFAULT_PATCH);
    assertThat(testJsonPatch.getEnabled()).isTrue();
  }

  @Test
  @Transactional
  public void updateNonExistingJsonPatch() throws Exception {
    int databaseSizeBeforeUpdate = jsonPatchRepository.findAll().size();

    // Create the JsonPatch
    JsonPatchDTO jsonPatchDTO = jsonPatchMapper.jsonPatchToJsonPatchDTO(jsonPatch);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restCountryMockMvc.perform(put("/api/jsonPatches")
            .contentType(TestUtil.JSON)
            .content(TestUtil.convertObjectToJsonBytes(jsonPatchDTO)))
        .andExpect(status().isCreated());

    // Validate the JsonPatch in the database
    List<JsonPatch> jsonPatchList = jsonPatchRepository.findAll();
    assertThat(jsonPatchList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteJsonPatch() throws Exception {
    // Initialize the database
    jsonPatchRepository.saveAndFlush(jsonPatch);
    int databaseSizeBeforeDelete = jsonPatchRepository.findAll().size();

    // Get the jsonPatch
    restCountryMockMvc.perform(put("/api/jsonPatches/{id}", jsonPatch.getId())
            .accept(TestUtil.JSON))
        .andExpect(status().isOk());

    // Validate the database is soft deleted
    List<JsonPatch> jsonPatchList = jsonPatchRepository.findAll();
    assertThat(jsonPatchList).hasSize(databaseSizeBeforeDelete);
    JsonPatch testJsonPatch = jsonPatchList.get(jsonPatchList.size() - 1);
    assertThat(testJsonPatch.getEnabled()).isFalse();
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(JsonPatch.class);
  }
}

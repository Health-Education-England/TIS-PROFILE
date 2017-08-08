package com.transformuk.hee.tis.profile.web.rest;

import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.ManageRecord;
import com.transformuk.hee.tis.profile.repository.ManageRecordRepository;
import com.transformuk.hee.tis.profile.service.dto.ManageRecordDTO;
import com.transformuk.hee.tis.profile.service.mapper.ManageRecordMapper;
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
 * Test class for the ManageRecordResource REST controller.
 *
 * @see ManageRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
public class ManageRecordResourceIntTest {

  private static final BigDecimal DEFAULT_TIS_ID = new BigDecimal(1);
  private static final BigDecimal UPDATED_TIS_ID = new BigDecimal(2);

  private static final String DEFAULT_RECORD_TYPE = "AAAAAAAAAA";
  private static final String UPDATED_RECORD_TYPE = "BBBBBBBBBB";

  private static final String DEFAULT_ROLE = "AAAAAAAAAA";
  private static final String UPDATED_ROLE = "BBBBBBBBBB";

  private static final String DEFAULT_RECORD_STATUS = "AAAAAAAAAA";
  private static final String UPDATED_RECORD_STATUS = "BBBBBBBBBB";

  private static final LocalDate DEFAULT_INACTIVE_FROM = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_INACTIVE_FROM = LocalDate.now(ZoneId.systemDefault());

  private static final BigDecimal DEFAULT_CHANGED_BY = new BigDecimal(1);
  private static final BigDecimal UPDATED_CHANGED_BY = new BigDecimal(2);

  private static final String DEFAULT_INACTIVE_REASON = "AAAAAAAAAA";
  private static final String UPDATED_INACTIVE_REASON = "BBBBBBBBBB";

  private static final LocalDate DEFAULT_INACTIVE_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_INACTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

  private static final String DEFAULT_DELETION_REASON = "AAAAAAAAAA";
  private static final String UPDATED_DELETION_REASON = "BBBBBBBBBB";

  @Autowired
  private ManageRecordRepository manageRecordRepository;

  @Autowired
  private ManageRecordMapper manageRecordMapper;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restManageRecordMockMvc;

  private ManageRecord manageRecord;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static ManageRecord createEntity(EntityManager em) {
    ManageRecord manageRecord = new ManageRecord()
        .tisId(DEFAULT_TIS_ID)
        .recordType(DEFAULT_RECORD_TYPE)
        .role(DEFAULT_ROLE)
        .recordStatus(DEFAULT_RECORD_STATUS)
        .inactiveFrom(DEFAULT_INACTIVE_FROM)
        .changedBy(DEFAULT_CHANGED_BY)
        .inactiveReason(DEFAULT_INACTIVE_REASON)
        .inactiveDate(DEFAULT_INACTIVE_DATE)
        .deletionReason(DEFAULT_DELETION_REASON);
    return manageRecord;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    ManageRecordResource manageRecordResource = new ManageRecordResource(manageRecordRepository, manageRecordMapper);
    this.restManageRecordMockMvc = MockMvcBuilders.standaloneSetup(manageRecordResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    manageRecord = createEntity(em);
  }

  @Test
  @Transactional
  public void createManageRecord() throws Exception {
    int databaseSizeBeforeCreate = manageRecordRepository.findAll().size();

    // Create the ManageRecord
    ManageRecordDTO manageRecordDTO = manageRecordMapper.manageRecordToManageRecordDTO(manageRecord);
    restManageRecordMockMvc.perform(post("/api/manage-records")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(manageRecordDTO)))
        .andExpect(status().isCreated());

    // Validate the ManageRecord in the database
    List<ManageRecord> manageRecordList = manageRecordRepository.findAll();
    assertThat(manageRecordList).hasSize(databaseSizeBeforeCreate + 1);
    ManageRecord testManageRecord = manageRecordList.get(manageRecordList.size() - 1);
    assertThat(testManageRecord.getTisId()).isEqualTo(DEFAULT_TIS_ID);
    assertThat(testManageRecord.getRecordType()).isEqualTo(DEFAULT_RECORD_TYPE);
    assertThat(testManageRecord.getRole()).isEqualTo(DEFAULT_ROLE);
    assertThat(testManageRecord.getRecordStatus()).isEqualTo(DEFAULT_RECORD_STATUS);
    assertThat(testManageRecord.getInactiveFrom()).isEqualTo(DEFAULT_INACTIVE_FROM);
    assertThat(testManageRecord.getChangedBy()).isEqualTo(DEFAULT_CHANGED_BY);
    assertThat(testManageRecord.getInactiveReason()).isEqualTo(DEFAULT_INACTIVE_REASON);
    assertThat(testManageRecord.getInactiveDate()).isEqualTo(DEFAULT_INACTIVE_DATE);
    assertThat(testManageRecord.getDeletionReason()).isEqualTo(DEFAULT_DELETION_REASON);
  }

  @Test
  @Transactional
  public void createManageRecordWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = manageRecordRepository.findAll().size();

    // Create the ManageRecord with an existing ID
    manageRecord.setId(1L);
    ManageRecordDTO manageRecordDTO = manageRecordMapper.manageRecordToManageRecordDTO(manageRecord);

    // An entity with an existing ID cannot be created, so this API call must fail
    restManageRecordMockMvc.perform(post("/api/manage-records")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(manageRecordDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<ManageRecord> manageRecordList = manageRecordRepository.findAll();
    assertThat(manageRecordList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkTisIdIsRequired() throws Exception {
    int databaseSizeBeforeTest = manageRecordRepository.findAll().size();
    // set the field null
    manageRecord.setTisId(null);

    // Create the ManageRecord, which fails.
    ManageRecordDTO manageRecordDTO = manageRecordMapper.manageRecordToManageRecordDTO(manageRecord);

    restManageRecordMockMvc.perform(post("/api/manage-records")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(manageRecordDTO)))
        .andExpect(status().isBadRequest());

    List<ManageRecord> manageRecordList = manageRecordRepository.findAll();
    assertThat(manageRecordList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllManageRecords() throws Exception {
    // Initialize the database
    manageRecordRepository.saveAndFlush(manageRecord);

    // Get all the manageRecordList
    restManageRecordMockMvc.perform(get("/api/manage-records?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(manageRecord.getId().intValue())))
        .andExpect(jsonPath("$.[*].tisId").value(hasItem(DEFAULT_TIS_ID.intValue())))
        .andExpect(jsonPath("$.[*].recordType").value(hasItem(DEFAULT_RECORD_TYPE.toString())))
        .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
        .andExpect(jsonPath("$.[*].recordStatus").value(hasItem(DEFAULT_RECORD_STATUS.toString())))
        .andExpect(jsonPath("$.[*].inactiveFrom").value(hasItem(DEFAULT_INACTIVE_FROM.toString())))
        .andExpect(jsonPath("$.[*].changedBy").value(hasItem(DEFAULT_CHANGED_BY.intValue())))
        .andExpect(jsonPath("$.[*].inactiveReason").value(hasItem(DEFAULT_INACTIVE_REASON.toString())))
        .andExpect(jsonPath("$.[*].inactiveDate").value(hasItem(DEFAULT_INACTIVE_DATE.toString())))
        .andExpect(jsonPath("$.[*].deletionReason").value(hasItem(DEFAULT_DELETION_REASON.toString())));
  }

  @Test
  @Transactional
  public void getManageRecord() throws Exception {
    // Initialize the database
    manageRecordRepository.saveAndFlush(manageRecord);

    // Get the manageRecord
    restManageRecordMockMvc.perform(get("/api/manage-records/{id}", manageRecord.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(manageRecord.getId().intValue()))
        .andExpect(jsonPath("$.tisId").value(DEFAULT_TIS_ID.intValue()))
        .andExpect(jsonPath("$.recordType").value(DEFAULT_RECORD_TYPE.toString()))
        .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
        .andExpect(jsonPath("$.recordStatus").value(DEFAULT_RECORD_STATUS.toString()))
        .andExpect(jsonPath("$.inactiveFrom").value(DEFAULT_INACTIVE_FROM.toString()))
        .andExpect(jsonPath("$.changedBy").value(DEFAULT_CHANGED_BY.intValue()))
        .andExpect(jsonPath("$.inactiveReason").value(DEFAULT_INACTIVE_REASON.toString()))
        .andExpect(jsonPath("$.inactiveDate").value(DEFAULT_INACTIVE_DATE.toString()))
        .andExpect(jsonPath("$.deletionReason").value(DEFAULT_DELETION_REASON.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingManageRecord() throws Exception {
    // Get the manageRecord
    restManageRecordMockMvc.perform(get("/api/manage-records/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateManageRecord() throws Exception {
    // Initialize the database
    manageRecordRepository.saveAndFlush(manageRecord);
    int databaseSizeBeforeUpdate = manageRecordRepository.findAll().size();

    // Update the manageRecord
    ManageRecord updatedManageRecord = manageRecordRepository.findOne(manageRecord.getId());
    updatedManageRecord
        .tisId(UPDATED_TIS_ID)
        .recordType(UPDATED_RECORD_TYPE)
        .role(UPDATED_ROLE)
        .recordStatus(UPDATED_RECORD_STATUS)
        .inactiveFrom(UPDATED_INACTIVE_FROM)
        .changedBy(UPDATED_CHANGED_BY)
        .inactiveReason(UPDATED_INACTIVE_REASON)
        .inactiveDate(UPDATED_INACTIVE_DATE)
        .deletionReason(UPDATED_DELETION_REASON);
    ManageRecordDTO manageRecordDTO = manageRecordMapper.manageRecordToManageRecordDTO(updatedManageRecord);

    restManageRecordMockMvc.perform(put("/api/manage-records")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(manageRecordDTO)))
        .andExpect(status().isOk());

    // Validate the ManageRecord in the database
    List<ManageRecord> manageRecordList = manageRecordRepository.findAll();
    assertThat(manageRecordList).hasSize(databaseSizeBeforeUpdate);
    ManageRecord testManageRecord = manageRecordList.get(manageRecordList.size() - 1);
    assertThat(testManageRecord.getTisId()).isEqualTo(UPDATED_TIS_ID);
    assertThat(testManageRecord.getRecordType()).isEqualTo(UPDATED_RECORD_TYPE);
    assertThat(testManageRecord.getRole()).isEqualTo(UPDATED_ROLE);
    assertThat(testManageRecord.getRecordStatus()).isEqualTo(UPDATED_RECORD_STATUS);
    assertThat(testManageRecord.getInactiveFrom()).isEqualTo(UPDATED_INACTIVE_FROM);
    assertThat(testManageRecord.getChangedBy()).isEqualTo(UPDATED_CHANGED_BY);
    assertThat(testManageRecord.getInactiveReason()).isEqualTo(UPDATED_INACTIVE_REASON);
    assertThat(testManageRecord.getInactiveDate()).isEqualTo(UPDATED_INACTIVE_DATE);
    assertThat(testManageRecord.getDeletionReason()).isEqualTo(UPDATED_DELETION_REASON);
  }

  @Test
  @Transactional
  public void updateNonExistingManageRecord() throws Exception {
    int databaseSizeBeforeUpdate = manageRecordRepository.findAll().size();

    // Create the ManageRecord
    ManageRecordDTO manageRecordDTO = manageRecordMapper.manageRecordToManageRecordDTO(manageRecord);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restManageRecordMockMvc.perform(put("/api/manage-records")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(manageRecordDTO)))
        .andExpect(status().isCreated());

    // Validate the ManageRecord in the database
    List<ManageRecord> manageRecordList = manageRecordRepository.findAll();
    assertThat(manageRecordList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteManageRecord() throws Exception {
    // Initialize the database
    manageRecordRepository.saveAndFlush(manageRecord);
    int databaseSizeBeforeDelete = manageRecordRepository.findAll().size();

    // Get the manageRecord
    restManageRecordMockMvc.perform(delete("/api/manage-records/{id}", manageRecord.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<ManageRecord> manageRecordList = manageRecordRepository.findAll();
    assertThat(manageRecordList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ManageRecord.class);
  }
}

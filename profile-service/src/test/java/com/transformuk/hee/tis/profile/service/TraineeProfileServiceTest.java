package com.transformuk.hee.tis.profile.service;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.transformuk.hee.tis.profile.domain.TraineeProfile;
import com.transformuk.hee.tis.profile.dto.RegistrationRequest;
import com.transformuk.hee.tis.profile.repository.TraineeProfileRepository;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class TraineeProfileServiceTest {

  private static final String EXISTING_GMC_NUMBER = "gmcOld";
  private static final String NEW_GMC_NUMBER = "gmcNew";
  private static final String DBC = "adbc";

  @Mock
  private TraineeProfileRepository traineeProfileRepository;
  @InjectMocks
  private TraineeProfileService service;

  @Test
  public void shouldReturnExistingIds() {
    RegistrationRequest request = new RegistrationRequest();
    request.setGmcNumber(EXISTING_GMC_NUMBER);

    List<String> gmcNumbers = newArrayList(EXISTING_GMC_NUMBER);
    TraineeProfile existingTraineeProfile = new TraineeProfile(1L, EXISTING_GMC_NUMBER);
    existingTraineeProfile.setActive(true);
    existingTraineeProfile.setDesignatedBodyCode(DBC);

    // given
    given(traineeProfileRepository.findByDesignatedBodyCode(DBC))
        .willReturn(newArrayList(existingTraineeProfile));
    given(traineeProfileRepository.findByGmcNumberIn(gmcNumbers))
        .willReturn(newArrayList(existingTraineeProfile));

    // when
    List<TraineeProfile> traineeProfiles = service.findOrCreate(DBC, newArrayList(request));

    // then
    assertThat(traineeProfiles).isEqualTo(newArrayList(existingTraineeProfile));
    verify(traineeProfileRepository, times(1)).findByGmcNumberIn(gmcNumbers);
  }

  @Test
  public void shouldSkipDuplicateIds() {
    RegistrationRequest request = new RegistrationRequest();
    request.setGmcNumber(EXISTING_GMC_NUMBER);

    RegistrationRequest dupeRequest1 = new RegistrationRequest();
    dupeRequest1.setGmcNumber("dupedGmc");
    RegistrationRequest dupeRequest2 = new RegistrationRequest();
    dupeRequest2.setGmcNumber("dupedGmc");

    List<String> gmcNumbers = newArrayList(EXISTING_GMC_NUMBER);
    TraineeProfile existingTraineeProfile = new TraineeProfile(1L, EXISTING_GMC_NUMBER);
    existingTraineeProfile.setActive(true);
    existingTraineeProfile.setDesignatedBodyCode(DBC);

    // given
    given(traineeProfileRepository.findByDesignatedBodyCode(DBC))
        .willReturn(newArrayList(existingTraineeProfile));
    given(traineeProfileRepository.findByGmcNumberIn(gmcNumbers))
        .willReturn(newArrayList(existingTraineeProfile));

    // when
    List<TraineeProfile> traineeProfiles = service.findOrCreate(DBC,
        newArrayList(request, dupeRequest1, dupeRequest2));

    // then
    assertThat(traineeProfiles).isEqualTo(newArrayList(existingTraineeProfile));
    verify(traineeProfileRepository, times(1)).findByGmcNumberIn(gmcNumbers);
  }

  @Test
  public void shouldCreateIfNotExists() {
    RegistrationRequest request = new RegistrationRequest();
    request.setGmcNumber(EXISTING_GMC_NUMBER);

    List<String> gmcNumbers = newArrayList(EXISTING_GMC_NUMBER, NEW_GMC_NUMBER);
    TraineeProfile existingTraineeProfile = new TraineeProfile(1L, EXISTING_GMC_NUMBER);

    TraineeProfile newTraineeProfile = new TraineeProfile(null, NEW_GMC_NUMBER);
    // given
    given(traineeProfileRepository.findByGmcNumberIn(gmcNumbers))
        .willReturn(newArrayList(existingTraineeProfile));
    given(traineeProfileRepository.save(anyListOf(TraineeProfile.class))).willReturn(newArrayList
        (existingTraineeProfile, newTraineeProfile));

    // when
    List<TraineeProfile> traineeProfiles = service.findOrCreate(DBC, newArrayList(request));

    // then
    assertThat(traineeProfiles).isEqualTo(newArrayList(existingTraineeProfile, newTraineeProfile));
  }

  @Test
  public void shouldReturnMappings() {
    Pageable pageable = new PageRequest(0, 100);
    TraineeProfile existingTraineeProfile = new TraineeProfile(1L, EXISTING_GMC_NUMBER);
    Page<TraineeProfile> page = new PageImpl<>(newArrayList(existingTraineeProfile));
    // given
    given(traineeProfileRepository.findByDesignatedBodyCode(DBC, pageable)).willReturn(page);

    // when
    Page<TraineeProfile> actualPage = service.findAll(DBC, pageable);

    // then
    assertThat(actualPage).isSameAs(page);
  }

}

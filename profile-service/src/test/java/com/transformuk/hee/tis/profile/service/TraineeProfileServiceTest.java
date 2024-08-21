package com.transformuk.hee.tis.profile.service;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.transformuk.hee.tis.profile.domain.TraineeProfile;
import com.transformuk.hee.tis.profile.dto.RegistrationRequest;
import com.transformuk.hee.tis.profile.repository.TraineeProfileRepository;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
    given(traineeProfileRepository.findByGmcNumberIn(gmcNumbers))
        .willReturn(newArrayList(existingTraineeProfile));

    // when
    List<TraineeProfile> traineeProfiles = service.findOrCreate(DBC, newArrayList(request));

    // then
    assertThat(traineeProfiles).isEqualTo(newArrayList(existingTraineeProfile));
    verify(traineeProfileRepository, times(1)).findByGmcNumberIn(gmcNumbers);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void shouldCreateIfNotExists() {
    RegistrationRequest moved = new RegistrationRequest();
    moved.setGmcNumber(EXISTING_GMC_NUMBER);
    RegistrationRequest brandNew = new RegistrationRequest();
    brandNew.setGmcNumber(NEW_GMC_NUMBER);

    TraineeProfile existingTraineeProfile = new TraineeProfile(1L, EXISTING_GMC_NUMBER);

    TraineeProfile newTraineeProfile = new TraineeProfile(null, NEW_GMC_NUMBER);
    // given
    ArgumentCaptor<Collection<String>> stringsCaptor = ArgumentCaptor.forClass(Collection.class);
    given(traineeProfileRepository.findByGmcNumberIn(stringsCaptor.capture()))
        .willReturn(newArrayList(existingTraineeProfile));
    ArgumentCaptor<Iterable<TraineeProfile>> actualProfileCaptor =
        ArgumentCaptor.forClass(Iterable.class);
    given(traineeProfileRepository.saveAll(actualProfileCaptor.capture()))
        .willReturn(newArrayList(newTraineeProfile))
        .willReturn(newArrayList(existingTraineeProfile));

    // when
    List<TraineeProfile> actualProfiles = service.findOrCreate(DBC, newArrayList(moved, brandNew));

    // then
    assertThat(actualProfiles).containsExactlyInAnyOrder(existingTraineeProfile, newTraineeProfile);
    assertThat(stringsCaptor.getValue())
        .containsExactlyInAnyOrder(EXISTING_GMC_NUMBER, NEW_GMC_NUMBER);
    assertThat(actualProfileCaptor.getAllValues().get(0)).containsOnlyOnce(newTraineeProfile);
  }

  @Test
  public void shouldReturnMappings() {
    Pageable pageable = PageRequest.of(0, 100);
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

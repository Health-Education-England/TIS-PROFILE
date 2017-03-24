package com.transformuk.hee.tis.profile.service;

import com.transformuk.hee.tis.profile.model.RegistrationRequest;
import com.transformuk.hee.tis.profile.model.TraineeProfile;
import com.transformuk.hee.tis.profile.repository.TraineeIdRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.util.collections.Sets.newSet;

@RunWith(MockitoJUnitRunner.class)
public class TraineeIdServiceTest {

    private static final String EXISTING_GMC_NUMBER = "gmcOld";
    private static final String NEW_GMC_NUMBER = "gmcNew";
    private static final String DBC = "adbc";
    
    @Mock
    private TraineeIdRepository traineeIdRepository;
    @InjectMocks
    private TraineeIdService service;

    @Test
    public void shouldReturnExistingIds() {
        RegistrationRequest request = new RegistrationRequest();
        request.setGmcNumber(EXISTING_GMC_NUMBER);
        
        Set<String> gmcNumbers = newSet(EXISTING_GMC_NUMBER);
        TraineeProfile existingTraineeProfile = new TraineeProfile(1L, EXISTING_GMC_NUMBER);
        existingTraineeProfile.setActive(true);
        existingTraineeProfile.setDesignatedBodyCode(DBC);

        // given
        given(traineeIdRepository.findByDesignatedBodyCode(DBC)).willReturn(newArrayList(existingTraineeProfile));
        given(traineeIdRepository.findByGmcNumberIn(gmcNumbers)).willReturn(newArrayList(existingTraineeProfile));

        // when
        List<TraineeProfile> traineeProfiles = service.findOrCreate(DBC, newArrayList(request));

        // then
        assertThat(traineeProfiles).isEqualTo(newArrayList(existingTraineeProfile));
        verify(traineeIdRepository, times(1)).findByGmcNumberIn(gmcNumbers);
    }

    @Test
    public void shouldCreateIfNotExists() {
        RegistrationRequest request = new RegistrationRequest();
        request.setGmcNumber(EXISTING_GMC_NUMBER);
        
        List<String> gmcNumbers = newArrayList(EXISTING_GMC_NUMBER, NEW_GMC_NUMBER);
        TraineeProfile existingTraineeProfile = new TraineeProfile(1L, EXISTING_GMC_NUMBER);

        TraineeProfile newTraineeProfile = new TraineeProfile(null, NEW_GMC_NUMBER);
        // given
        given(traineeIdRepository.findByGmcNumberIn(gmcNumbers)).willReturn(newArrayList(existingTraineeProfile));
        given(traineeIdRepository.save(anyListOf(TraineeProfile.class))).willReturn(newArrayList
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
        given(traineeIdRepository.findByDesignatedBodyCode(DBC, pageable)).willReturn(page);

        // when
        Page<TraineeProfile> actualPage = service.findAll(DBC, pageable);

        // then
        assertThat(actualPage).isSameAs(page);
    }
    
}

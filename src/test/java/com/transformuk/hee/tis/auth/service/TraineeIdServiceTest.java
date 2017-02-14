package com.transformuk.hee.tis.auth.service;

import com.transformuk.hee.tis.auth.model.TraineeId;
import com.transformuk.hee.tis.auth.repository.TraineeIdRepository;
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

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TraineeIdServiceTest {

    private static final String EXISTING_GMC_NUMBER = "gmcOld";
    private static final String NEW_GMC_NUMBER = "gmcNew";
    
    @Mock
    private TraineeIdRepository traineeIdRepository;
    @InjectMocks private TraineeIdService service;

    @Test
    public void shouldReturnExistingIds() {
        List<String> gmcNumbers = newArrayList(EXISTING_GMC_NUMBER);
        TraineeId existingTraineeId = new TraineeId(1L, EXISTING_GMC_NUMBER);

        // given
        given(traineeIdRepository.findByGmcNumberIn(gmcNumbers)).willReturn(newArrayList(existingTraineeId));

        // when
        List<TraineeId> traineeIds = service.findOrCreate(gmcNumbers);

        // then
        assertThat(traineeIds).isEqualTo(newArrayList(existingTraineeId));
        verify(traineeIdRepository, times(1)).findByGmcNumberIn(gmcNumbers);
        verify(traineeIdRepository).save(newArrayList());
    }

    @Test
    public void shouldCreateIfNotExists() {
        List<String> gmcNumbers = newArrayList(EXISTING_GMC_NUMBER, NEW_GMC_NUMBER);
        TraineeId existingTraineeId = new TraineeId(1L, EXISTING_GMC_NUMBER);

        TraineeId newTraineeId = new TraineeId(null, NEW_GMC_NUMBER);
        // given
        given(traineeIdRepository.findByGmcNumberIn(gmcNumbers)).willReturn(newArrayList(existingTraineeId));
        given(traineeIdRepository.save(newArrayList(newTraineeId))).willReturn(newArrayList(newTraineeId));

        // when
        List<TraineeId> traineeIds = service.findOrCreate(gmcNumbers);

        // then
        verify(traineeIdRepository).save(newArrayList(newTraineeId));
        assertThat(traineeIds).isEqualTo(newArrayList(existingTraineeId, newTraineeId));
    }

    @Test
    public void shouldReturnMappings() {
        Pageable pageable = new PageRequest(0, 100);
        TraineeId existingTraineeId = new TraineeId(1L, EXISTING_GMC_NUMBER);
        Page<TraineeId> page = new PageImpl<>(newArrayList(existingTraineeId));
        // given
        given(traineeIdRepository.findAll(pageable)).willReturn(page);

        // when
        Page<TraineeId> actualPage = service.findAll(pageable);

        // then
        assertThat(actualPage).isSameAs(page);
    }
    
}

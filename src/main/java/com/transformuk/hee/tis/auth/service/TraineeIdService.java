package com.transformuk.hee.tis.auth.service;

import com.transformuk.hee.tis.auth.model.TraineeId;
import com.transformuk.hee.tis.auth.repository.TraineeIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service to operate on TraineeId domain. 
 */
@Service
public class TraineeIdService {
    
    private TraineeIdRepository traineeIdRepository;

    @Autowired
    public TraineeIdService(TraineeIdRepository traineeIdRepository) {
        this.traineeIdRepository = traineeIdRepository;
    }

    /**
     * Get or create a traineeId which maps to given gmcNumber
     * @param gmcNumbers
     * @return list of {@link TraineeId}
     */
    @Transactional
    public List<TraineeId> findOrCreate(List<String> gmcNumbers) {
        List<TraineeId> existingTraineeIds = traineeIdRepository.findByGmcNumberIn(gmcNumbers);
        List<String> existingGmcNumbers = existingTraineeIds.stream()
                .map(TraineeId::getGmcNumber)
                .collect(Collectors.toList());
        List<String> newGmcNumbers = gmcNumbers.stream()
                .filter(e -> !existingGmcNumbers.contains(e))
                .collect(Collectors.toList());
        List<TraineeId> newTraineeIds = create(newGmcNumbers);
        
        existingTraineeIds.addAll(newTraineeIds);
        return existingTraineeIds;
    }

    /**
     * Gets paged traineeIds which maps to given gmcNumber
     * @return list of {@link TraineeId}
     * @param pageable
     */
    public Page<TraineeId> findAll(Pageable pageable) {
        return traineeIdRepository.findAll(pageable);
    }

    private List<TraineeId> create(List<String> gmcNumbers) {
        List<TraineeId> traineeIds = gmcNumbers.stream()
                .map(this::traineeId)
                .collect(Collectors.toList());
        return traineeIdRepository.save(traineeIds);
    }

    private TraineeId traineeId(String gmcNumber) {
        TraineeId traineeId = new TraineeId();
        traineeId.setGmcNumber(gmcNumber);
        return traineeId;
    }
}

package com.transformuk.hee.tis.auth.service;

import com.transformuk.hee.tis.auth.model.RegistrationRequest;
import com.transformuk.hee.tis.auth.model.TraineeProfile;
import com.transformuk.hee.tis.auth.repository.TraineeIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

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
     * @param dbc designatedBodyCode of a trainee
     * @param requests registration requests
     * @return list of {@link TraineeProfile}
     */
    @Transactional
    public List<TraineeProfile> findOrCreate(String dbc, List<RegistrationRequest> requests) {
        Map<String, RegistrationRequest> requestMap = requests.stream().collect(toMap(t -> t.getGmcNumber(), v -> v));
        List<TraineeProfile> dbProfiles = traineeIdRepository.findByGmcNumberIn(requestMap.keySet());
        
        List<String> existingGmcNumbers = dbProfiles.stream()
                .map(TraineeProfile::getGmcNumber)
                .collect(Collectors.toList());
        
        List<TraineeProfile> newTraineeProfiles = requests.stream()
                .filter(e -> !existingGmcNumbers.contains(e.getGmcNumber()))
                .map(e -> newProfile(dbc, e))
                .collect(Collectors.toList());
        
        // update existing profiles to add extra info - dbc, active flag, dateAdded
        dbProfiles.stream().forEach(e -> update(dbc, e, requestMap));

        dbProfiles.addAll(newTraineeProfiles);
        return traineeIdRepository.save(dbProfiles);
    }

    private void update(String dbc,TraineeProfile traineeProfile, Map<String, RegistrationRequest> requestMap ) {
        RegistrationRequest request = requestMap.get(traineeProfile.getGmcNumber());
        traineeProfile.setDesignatedBodyCode(dbc);
        traineeProfile.setDateAdded(request.getDateAdded());
        traineeProfile.setActive(true);
    }

    /**
     * Gets paged traineeIds which maps to given gmcNumber
     * @return list of {@link TraineeProfile}
     * @param pageable
     */
    public Page<TraineeProfile> findAll(Pageable pageable) {
        return traineeIdRepository.findAll(pageable);
    }

    private TraineeProfile newProfile(String dbc, RegistrationRequest request) {
        TraineeProfile traineeProfile = new TraineeProfile();
        traineeProfile.setGmcNumber(request.getGmcNumber());
        traineeProfile.setDateAdded(request.getDateAdded());
        traineeProfile.setDesignatedBodyCode(dbc);
        traineeProfile.setActive(true);
        return traineeProfile;
    }

}

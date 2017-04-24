package com.transformuk.hee.tis.profile.service;

import com.transformuk.hee.tis.profile.dto.RegistrationRequest;
import com.transformuk.hee.tis.profile.domain.TraineeProfile;
import com.transformuk.hee.tis.profile.repository.TraineeProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * Service to operate on TraineeId domain.
 */
@Service
public class TraineeProfileService {

	private TraineeProfileRepository traineeProfileRepository;

	@Autowired
	public TraineeProfileService(TraineeProfileRepository traineeProfileRepository) {
		this.traineeProfileRepository = traineeProfileRepository;
	}

	/**
	 * Get or create a traineeId which maps to given gmcNumber
	 *
	 * @param dbc      designatedBodyCode of a trainee
	 * @param requests registration requests
	 * @return list of {@link TraineeProfile}
	 */
	@Transactional
	public List<TraineeProfile> findOrCreate(String dbc, List<RegistrationRequest> requests) {
		Map<String, RegistrationRequest> requestMap = requests.stream().collect(toMap(t -> t.getGmcNumber(), v -> v));
		List<TraineeProfile> dbProfiles = traineeProfileRepository.findByGmcNumberIn(requestMap.keySet());

		Set<String> dbGmcNumbers = dbProfiles.stream().map(TraineeProfile::getGmcNumber).collect(Collectors.toSet());
		Set<String> inputGmcNumbers = requestMap.keySet();

		// 1. Brand new profiles - not even associate to any DBC earlier
		List<TraineeProfile> brandNewProfiles = requests.stream()
				.filter(e -> !dbGmcNumbers.contains(e.getGmcNumber()))
				.map(e -> newProfile(dbc, e))
				.collect(Collectors.toList());
		List<TraineeProfile> savedBrandNewProfiles = traineeProfileRepository.save(brandNewProfiles);

		// 2. Matched profiles - associated same DBC in past and now
		List<TraineeProfile> matchedProfiles = dbProfiles.stream()
				.filter(e -> dbc.equalsIgnoreCase(e.getDesignatedBodyCode()))
				.collect(Collectors.toList());

		// 3. MovedIn profiles - associated to another DBC in the past but now appeared in this DBC extract
		List<TraineeProfile> movedInProfiles = dbProfiles.stream()
				.filter(e -> !dbc.equalsIgnoreCase(e.getDesignatedBodyCode()))
				.map(e -> {
					RegistrationRequest request = requestMap.get(e.getGmcNumber());
					e.setDesignatedBodyCode(dbc);
					e.setDateAdded(request.getDateAdded());
					e.setActive(true);
					return e;
				})
				.collect(Collectors.toList());
		traineeProfileRepository.save(movedInProfiles);

		// 4. Completely moved out from system - de-activate
		// TODO : since we process per DBC don't know whether trainees moved or completely gone.

		// union brandNew, unChanged and recently movedIn profiles
		List<TraineeProfile> profilesToBeReturned = new ArrayList<>(savedBrandNewProfiles);
		profilesToBeReturned.addAll(matchedProfiles);
		profilesToBeReturned.addAll(movedInProfiles);
		return profilesToBeReturned;
	}

	/**
	 * Gets paged traineeIds which maps to given gmcNumber
	 *
	 * @param designatedBodyCode
	 * @param pageable
	 * @return list of {@link TraineeProfile}
	 */
	public Page<TraineeProfile> findAll(String designatedBodyCode, Pageable pageable) {
		return traineeProfileRepository.findByDesignatedBodyCode(designatedBodyCode, pageable);
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

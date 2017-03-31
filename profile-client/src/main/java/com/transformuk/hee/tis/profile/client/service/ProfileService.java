package com.transformuk.hee.tis.profile.client.service;

import com.transformuk.hee.tis.profile.dto.TraineeId;
import com.transformuk.hee.tis.profile.dto.UserProfile;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileService {

	Page<TraineeId> getPagedTraineeIds(String dbc, Pageable pageable);

	UserProfile getProfile();

	UserProfile getRODetails(String designatedBodyCode);

	JSONObject getAllUsers(String permissions, String... designatedBodyCodes);

	void setServiceUrl(String serviceUrl);
}

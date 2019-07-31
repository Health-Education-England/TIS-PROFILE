package com.transformuk.hee.tis.profile.client.service;

import com.transformuk.hee.tis.client.ClientService;
import com.transformuk.hee.tis.profile.dto.RegistrationRequest;
import com.transformuk.hee.tis.profile.dto.TraineeId;
import com.transformuk.hee.tis.profile.dto.TraineeProfileDto;
import com.transformuk.hee.tis.security.model.UserProfile;
import java.util.List;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileService extends ClientService {

  Page<TraineeId> getPagedTraineeIds(String dbc, Pageable pageable);

  UserProfile getRODetails(String designatedBodyCode);

  JSONObject getAllUsers(String permissions, String... designatedBodyCodes);

  void setServiceUrl(String serviceUrl);

  List<TraineeProfileDto> getTraineeIdsForGmcNumbers(String designatedBodyCode,
      List<RegistrationRequest> registrationRequests);

  boolean deleteUser(String username);
}

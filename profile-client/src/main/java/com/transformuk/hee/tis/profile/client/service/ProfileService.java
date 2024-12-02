package com.transformuk.hee.tis.profile.client.service;

import com.transformuk.hee.tis.client.ClientService;
import com.transformuk.hee.tis.profile.dto.RegistrationRequest;
import com.transformuk.hee.tis.profile.dto.TraineeId;
import com.transformuk.hee.tis.profile.dto.TraineeProfileDto;
import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import com.transformuk.hee.tis.security.model.UserProfile;
import java.util.List;
import java.util.Set;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileService extends ClientService {

  Page<TraineeId> getPagedTraineeIds(String dbc, Pageable pageable);

  UserProfile getRODetails(String designatedBodyCode);

  HeeUserDTO getSingleAdminUser(String username);

  Page<HeeUserDTO> getAllAdminUsers(Pageable pageable, String username);

  JSONObject getAllUsers(String permissions, String... designatedBodyCodes);

  List<HeeUserDTO> getUsersByNameIgnoreCase(String username);

  void setServiceUrl(String serviceUrl);

  List<TraineeProfileDto> getTraineeIdsForGmcNumbers(String designatedBodyCode,
      List<RegistrationRequest> registrationRequests);

  boolean deleteUser(String username);

  Set<String> getRestrictedRoles();
}

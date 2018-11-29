package com.transformuk.hee.tis.profile.service;

import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.UserTrust;
import com.transformuk.hee.tis.profile.repository.UserTrustRepository;
import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import com.transformuk.hee.tis.profile.service.mapper.HeeUserMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserTrustService {

  @Autowired
  private UserTrustRepository userTrustRepository;
  @Autowired
  private HeeUserMapper heeUserMapper;

  public void assignTrustsToUser(HeeUserDTO heeUserDTO) {
    HeeUser heeUser = heeUserMapper.heeUserDTOToHeeUser(heeUserDTO);

    Set<UserTrust> associatedTrusts = heeUser.getAssociatedTrusts();
    if (CollectionUtils.isNotEmpty(associatedTrusts)) {
      for (UserTrust userTrust : associatedTrusts) {
        heeUser.addAssociatedTrust(userTrust);
      }
    }
    List<UserTrust> userTrusts = userTrustRepository.findByHeeUser(heeUser);
    if (CollectionUtils.isNotEmpty(userTrusts)) {
      userTrustRepository.deleteAll(userTrusts);
    }

    if (CollectionUtils.isNotEmpty(associatedTrusts)) {
      userTrustRepository.saveAll(associatedTrusts);
    }
  }
}

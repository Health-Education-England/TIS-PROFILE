package com.transformuk.hee.tis.profile.service;

import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.UserProgramme;
import com.transformuk.hee.tis.profile.repository.UserProgrammeRepository;
import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import com.transformuk.hee.tis.profile.service.mapper.HeeUserMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserProgrammeService {

  @Autowired
  private UserProgrammeRepository userProgrammeRepository;
  @Autowired
  private HeeUserMapper heeUserMapper;

  public void assignProgrammesToUser(HeeUserDTO heeUserDTO) {
    HeeUser heeUser = heeUserMapper.heeUserDTOToHeeUser(heeUserDTO);

    Set<UserProgramme> associatedProgrammes = heeUser.getAssociatedProgrammes();
    if (CollectionUtils.isNotEmpty(associatedProgrammes)) {
      for (UserProgramme userProgramme : associatedProgrammes) {
        heeUser.addAssociatedProgramme(userProgramme);
      }
    }
    List<UserProgramme> userProgrammes = userProgrammeRepository.findByHeeUser(heeUser);
    if (CollectionUtils.isNotEmpty(userProgrammes)) {
      userProgrammeRepository.deleteAll(userProgrammes);
    }

    if (CollectionUtils.isNotEmpty(associatedProgrammes)) {
      userProgrammeRepository.saveAll(associatedProgrammes);
    }
  }
}

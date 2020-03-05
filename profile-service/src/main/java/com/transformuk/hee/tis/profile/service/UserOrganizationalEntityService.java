package com.transformuk.hee.tis.profile.service;

import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.UserOrganisationalEntity;
import com.transformuk.hee.tis.profile.repository.UserOrganisationalEntityRepository;
import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import com.transformuk.hee.tis.profile.service.mapper.HeeUserMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserOrganizationalEntityService {

  @Autowired
  private UserOrganisationalEntityRepository userOrganisationalEntityRepository;
  @Autowired
  private HeeUserMapper heeUserMapper;

  public void assignOrganisationalEntitiesToUser(HeeUserDTO heeUserDTO) {
    HeeUser heeUser = heeUserMapper.heeUserDTOToHeeUser(heeUserDTO);

    Set<UserOrganisationalEntity> organisationalEntities = heeUser.getAssociatedOrganisationalEntities();
    if (CollectionUtils.isNotEmpty(organisationalEntities)) {
      for (UserOrganisationalEntity organisationalEntity : organisationalEntities) {
        heeUser.addUserOrganisationalEntity(organisationalEntity);
      }
    }
    List<UserOrganisationalEntity> userTrusts = userOrganisationalEntityRepository.findByHeeUser(heeUser);
    if (CollectionUtils.isNotEmpty(userTrusts)) {
      userOrganisationalEntityRepository.delete(userTrusts);
    }

    if (CollectionUtils.isNotEmpty(organisationalEntities)) {
      userOrganisationalEntityRepository.save(organisationalEntities);
    }
  }
}

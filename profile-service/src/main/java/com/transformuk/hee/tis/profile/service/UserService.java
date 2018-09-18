package com.transformuk.hee.tis.profile.service;

import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import com.transformuk.hee.tis.profile.service.mapper.HeeUserMapper;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

  @Autowired
  HeeUserRepository heeUserRepository;

  @Autowired
  HeeUserMapper heeUserMapper;

  @Transactional(readOnly = true)
  public Page<HeeUserDTO> findAllUsersWithTrust(Pageable pageable, String search) {
    Page<HeeUser> heeUsers;
    if(StringUtils.isNotEmpty(search)) {
      heeUsers = heeUserRepository.findByNameIgnoreCaseContaining(pageable, search);
    } else {
    heeUsers = heeUserRepository.findAll(pageable);
    }
    List<HeeUser> heeUserList = heeUsers.getContent();
    List<HeeUserDTO> heeUserDTOS = heeUserMapper.heeUsersToHeeUserDTOs(heeUserList);
    Page<HeeUserDTO> pagedHeeUserDTOS = new PageImpl<>(heeUserDTOS, pageable, heeUsers.getTotalElements());
    return pagedHeeUserDTOS;
  }

  @Transactional
  public HeeUserDTO findSingleUserWithTrust (String username) {
    Optional<HeeUser> heeUser = heeUserRepository.findByNameWithTrusts(username);
    HeeUserDTO heeUserDTO = heeUserMapper.heeUserToHeeUserDTO(heeUser.orElse(null));
    return heeUserDTO;
  }
}

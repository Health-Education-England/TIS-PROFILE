package com.transformuk.hee.tis.profile.service;

import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import com.transformuk.hee.tis.profile.service.mapper.HeeUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

  @Autowired
  HeeUserRepository heeUserRepository;

  @Autowired
  HeeUserMapper heeUserMapper;

  @Transactional
  public List<HeeUserDTO> findAllUsersWithTrust (Pageable pageable) {
    Page<HeeUser> heeUsers = heeUserRepository.findAll(pageable);
    List<HeeUser> heeUserList = heeUsers.getContent();
    List<HeeUserDTO> heeUserDTOS = heeUserMapper.heeUsersToHeeUserDTOs(heeUserList);
    return heeUserDTOS;
  }
}
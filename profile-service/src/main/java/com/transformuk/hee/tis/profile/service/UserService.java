package com.transformuk.hee.tis.profile.service;

import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
  public List<HeeUserDTO> findAllUsersWithTrust(Pageable pageable) {
    return null;
  }
}

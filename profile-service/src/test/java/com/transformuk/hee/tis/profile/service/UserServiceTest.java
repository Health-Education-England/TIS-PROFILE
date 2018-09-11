package com.transformuk.hee.tis.profile.service;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.UserTrust;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import com.transformuk.hee.tis.profile.service.dto.UserTrustDTO;
import com.transformuk.hee.tis.profile.service.mapper.HeeUserMapper;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.once;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  private static final String FIRST_NAME_1 = "first name 1";
  private static final String FIRST_NAME_2 = "first name 2";
  private static final String FIRST_NAME_3 = "first name 3";
  private static final boolean USER_ACTIVE = true;
  private static final boolean USER_NOT_ACTIVE = false;

  @InjectMocks
  private UserService testObj;

  @Mock
  private HeeUserRepository heeUserRepositoryMock;
  @Mock
  private HeeUserMapper heeUserMapperMock;
  @Mock
  private Pageable pageMock;

  private HeeUser heeUser1WithTrusts = new HeeUser(),
      heeUser2EmptyTrusts = new HeeUser(),
      heeUser3NullTrusts = new HeeUser();

  private HeeUserDTO heeUser1WithTrustsDTO = new HeeUserDTO(),
      heeUser2EmptyTrustsDTO = new HeeUserDTO(),
      heeUser3NullTrustsDTO = new HeeUserDTO();

  private Set<UserTrust> userTrusts1, userTrusts2, userTrusts3;
  private Set<UserTrustDTO> userTrusts1DTO, userTrusts2DTO, userTrusts3DTO;

  @Before
  public void setup() {
    heeUser1WithTrusts.firstName(FIRST_NAME_1).active(USER_ACTIVE);
    heeUser2EmptyTrusts.firstName(FIRST_NAME_2).active(USER_NOT_ACTIVE);
    heeUser3NullTrusts.firstName(FIRST_NAME_3).active(USER_ACTIVE);

    UserTrust userTrust1 = new UserTrust(), userTrust2 = new UserTrust();
    userTrust1.setHeeUser(heeUser1WithTrusts);
    userTrust1.setTrustCode("RJ7");
    userTrust1.setTrustName("St Georges");

    userTrust2.setHeeUser(heeUser1WithTrusts);
    userTrust2.setTrustCode("RA1");
    userTrust2.setTrustName("St Pauls");
    userTrusts1 = Sets.newHashSet(userTrust1, userTrust2);
    userTrusts2 = Sets.newHashSet();
    userTrusts3 = null;

    heeUser1WithTrusts.setAssociatedTrusts(userTrusts1);
    heeUser2EmptyTrusts.setAssociatedTrusts(userTrusts2);
    heeUser3NullTrusts.setAssociatedTrusts(userTrusts3);

    //dtos
    heeUser1WithTrustsDTO.setFirstName(FIRST_NAME_1);
    heeUser1WithTrustsDTO.setActive(USER_ACTIVE);

    heeUser2EmptyTrustsDTO.setFirstName(FIRST_NAME_2);
    heeUser2EmptyTrustsDTO.setActive(USER_NOT_ACTIVE);

    heeUser3NullTrustsDTO.setFirstName(FIRST_NAME_3);
    heeUser3NullTrustsDTO.setActive(USER_ACTIVE);

    UserTrustDTO userTrust1DTO = new UserTrustDTO(), userTrust2DTO = new UserTrustDTO();
    userTrust1DTO.setHeeUserDTO(heeUser1WithTrustsDTO);
    userTrust1DTO.setTrustCode("RJ7");
    userTrust1DTO.setTrustName("St Georges");

    userTrust2DTO.setHeeUserDTO(heeUser1WithTrustsDTO);
    userTrust2DTO.setTrustCode("RA1");
    userTrust2DTO.setTrustName("St Pauls");
    userTrusts1DTO = Sets.newHashSet(userTrust1DTO, userTrust2DTO);
    userTrusts2DTO = Sets.newHashSet();
    userTrusts3DTO = null;

    heeUser1WithTrustsDTO.setAssociatedTrusts(userTrusts1DTO);
    heeUser2EmptyTrustsDTO.setAssociatedTrusts(userTrusts2DTO);
    heeUser3NullTrustsDTO.setAssociatedTrusts(userTrusts3DTO);


  }

  @Test
  private void findAllUsersWithTrustShouldReturnHeeUserDTOWithTrustData() {
    Page<HeeUser> foundUsersMock = mock(Page.class);


    List<HeeUser> usersFromPage = Lists.newArrayList(heeUser1WithTrusts, heeUser2EmptyTrusts, heeUser3NullTrusts);
    List<HeeUserDTO> mappedUsers = Lists.newArrayList();
    when(heeUserRepositoryMock.findAll(pageMock)).thenReturn(foundUsersMock);
    when(foundUsersMock.getContent()).thenReturn(usersFromPage);
    when(heeUserMapperMock.heeUsersToHeeUserDTOs(usersFromPage)).thenReturn(mappedUsers);

    List<HeeUserDTO> result = testObj.findAllUsersWithTrust(pageMock);

    //asserts
    Assert.assertEquals(3, result.size());
    Assert.assertEquals(FIRST_NAME_1, result.get(0).getFirstName());
    Assert.assertEquals(FIRST_NAME_2, result.get(1).getFirstName());
    Assert.assertEquals(FIRST_NAME_3, result.get(2).getFirstName());

    Assert.assertSame(userTrusts1DTO, result.get(0).getAssociatedTrusts());
    Assert.assertSame(userTrusts2DTO, result.get(1).getAssociatedTrusts());
    Assert.assertSame(userTrusts3DTO, result.get(2).getAssociatedTrusts());

    //verify
    verify(heeUserRepositoryMock).findAll(pageMock);
    verify(foundUsersMock).getContent();
    verify(heeUserMapperMock.heeUsersToHeeUserDTOs(usersFromPage));
  }


}
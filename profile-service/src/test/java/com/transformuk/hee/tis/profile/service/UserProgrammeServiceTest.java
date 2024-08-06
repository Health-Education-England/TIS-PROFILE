package com.transformuk.hee.tis.profile.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.UserProgramme;
import com.transformuk.hee.tis.profile.repository.UserProgrammeRepository;
import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import com.transformuk.hee.tis.profile.service.mapper.HeeUserMapper;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserProgrammeServiceTest {

  @InjectMocks
  private UserProgrammeService testObj;
  @Mock
  private UserProgrammeRepository userProgrammeRepositoryMock;
  @Mock
  private HeeUserMapper heeUserMapperMock;
  @Mock
  private HeeUserDTO heeUserDTOMock;
  @Mock
  private HeeUser heeUserMock;
  @Mock
  private UserProgramme userProgrammeMock;
  @Mock
  private UserProgramme existingUserProgrammeMock;

  @Test
  public void assignProgrammesToUserShouldClearExistingProgrammeAndReAssignProgrammes() {
    when(heeUserMapperMock.heeUserDTOToHeeUser(heeUserDTOMock)).thenReturn(heeUserMock);
    Set<UserProgramme> payloadUserProgrammes = Sets.newHashSet(userProgrammeMock);
    when(heeUserMock.getAssociatedProgrammes()).thenReturn(payloadUserProgrammes);
    List<UserProgramme> existingUserProgrammes = Lists.newArrayList(existingUserProgrammeMock);
    when(userProgrammeRepositoryMock.findByHeeUser(heeUserMock)).thenReturn(existingUserProgrammes);

    testObj.assignProgrammesToUser(heeUserDTOMock);

    verify(userProgrammeRepositoryMock).delete(existingUserProgrammes);
    verify(heeUserMock).addAssociatedProgramme(userProgrammeMock);
    verify(userProgrammeRepositoryMock).save(payloadUserProgrammes);
  }

}
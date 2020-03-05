package com.transformuk.hee.tis.profile.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.UserOrganisationalEntity;
import com.transformuk.hee.tis.profile.repository.UserOrganisationalEntityRepository;
import com.transformuk.hee.tis.profile.service.dto.HeeUserDTO;
import com.transformuk.hee.tis.profile.service.mapper.HeeUserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserOrganisationalEntityServiceTest {

  @InjectMocks
  private UserOrganizationalEntityService testObj;
  @Mock
  private UserOrganisationalEntityRepository userOrganisationalEntityRepositoryMock;
  @Mock
  private HeeUserMapper heeUserMapperMock;
  @Mock
  private HeeUserDTO heeUserDTOMock;
  @Mock
  private HeeUser heeUserMock;
  @Mock
  private UserOrganisationalEntity userOrganisationalEntityMock;
  @Mock
  private UserOrganisationalEntity existingUserOrganisationalEntityMock;

  @Test
  public void assignOrganisationalEntitiesToUserShouldClearExistingProgrammeAndReAssignOrganisationalEntities() {
    when(heeUserMapperMock.heeUserDTOToHeeUser(heeUserDTOMock)).thenReturn(heeUserMock);
    Set<UserOrganisationalEntity> payloadUserOrganisationalEntities = Sets.newHashSet(userOrganisationalEntityMock);
    when(heeUserMock.getAssociatedOrganisationalEntities()).thenReturn(payloadUserOrganisationalEntities);
    List<UserOrganisationalEntity> existingUserProgrammes = Lists.newArrayList(existingUserOrganisationalEntityMock);
    when(userOrganisationalEntityRepositoryMock.findByHeeUser(heeUserMock)).thenReturn(existingUserProgrammes);

    testObj.assignOrganisationalEntitiesToUser(heeUserDTOMock);

    verify(userOrganisationalEntityRepositoryMock).delete(existingUserProgrammes);
    verify(heeUserMock).addUserOrganisationalEntity(userOrganisationalEntityMock);
    verify(userOrganisationalEntityRepositoryMock).save(payloadUserOrganisationalEntities);
  }

}
package com.transformuk.hee.tis.profile.validators;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.Permission;
import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.repository.RoleRepository;
import com.transformuk.hee.tis.profile.web.rest.errors.CustomParameterizedException;
import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import java.util.Set;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class HeeUserValidatorTest {

  private static final String DBC_ABBR = "DBC_ABBR";
  private static final String DBC = "DBC";
  private static final String INVALID_DBC = "INVALID";
  private static final long ID = 1L;
  private static final long ID_INVALID = 2L;
  private static final String DBC_NAME = "DBC_NAME";
  private static final String ROLENAME = "ROLENAME";
  private static final String PERMISSION_NAME = "PERMISSION_NAME";
  private static final String OTHER_PERMISSION = "OTHER_PERMISSION";
  private static final String GMC_ID = "1234567";
  private static final String GMC_ID_TOO_LONG = "12345678";
  @Mock
  RoleRepository roleRepositoryMock;
  private final Set<String> dbcCodes = Sets.newLinkedHashSet(DBC);
  private final Set<String> dbcCodesInvalid = Sets.newLinkedHashSet(INVALID_DBC);
  private final Set<String> dbcCodesNone = Sets.newLinkedHashSet(HeeUser.NONE);
  private final DBCDTO dbcDto = new DBCDTO();
  private final DBCDTO dbcDtoInvalid = new DBCDTO();
  private final Role role = new Role();
  private final Permission permission = new Permission();
  private final Permission permission1 = new Permission();
  private final Set<Role> roles = Sets.newLinkedHashSet(role);
  @Mock
  private ReferenceService referenceServiceMock;
  @InjectMocks
  private HeeUserValidator testObj;

  @Before
  public void setup() {
    dbcDto.setAbbr(DBC_ABBR);
    dbcDto.setDbc(DBC);
    dbcDto.setId(ID);
    dbcDto.setName(DBC_NAME);
    dbcDto.setStatus(Status.CURRENT);

    dbcDtoInvalid.setDbc(INVALID_DBC);
    dbcDtoInvalid.setId(ID_INVALID);
    dbcDtoInvalid.setStatus(Status.CURRENT);

    permission.setName(PERMISSION_NAME);
    permission1.setName(OTHER_PERMISSION);
    Set<Permission> permissions = Sets.newLinkedHashSet(permission, permission1);
    role.setName(ROLENAME);
    role.setPermissions(permissions);
  }

  @Test
  public void shouldValidateDbcIds() {
    // Given
    when(referenceServiceMock.getDBCByCode(DBC))
        .thenReturn(new ResponseEntity<>(dbcDto, HttpStatus.OK));

    // When
    testObj.validateDbcIds(dbcCodes);

    // Then
    verify(referenceServiceMock).getDBCByCode(DBC);
  }

  @Test
  public void shouldValidateInvalidDbcAsEmptyReponse() {
    // Given
    when(referenceServiceMock.getDBCByCode(INVALID_DBC))
        .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    // When
    testObj.validateDbcIds(dbcCodesInvalid);

    // Then
    verify(referenceServiceMock).getDBCByCode(INVALID_DBC);
  }

  @Test
  public void shouldValidateIfCodeIsNone() {
    // When
    testObj.validateDbcIds(dbcCodesNone);

    // Then
    verify(referenceServiceMock, never()).getDBCByCode(any(String.class));
  }

  @Test
  public void shouldValidateIfSetOfDbcCodesIsNull() {
    // When
    testObj.validateDbcIds(null);

    // Then
    verify(referenceServiceMock, never()).getDBCByCode(any(String.class));
  }

  @Test
  public void shouldValidateRoles() {
    // Given
    when(roleRepositoryMock.findByName(role.getName())).thenReturn(role);

    // When
    testObj.validateRoles(roles);

    // Then
    verify(roleRepositoryMock).findByName(role.getName());
  }

  @Test(expected = CustomParameterizedException.class)
  public void shouldThrowExceptionIfRoleNotFoundInRepository() {
    // Given
    try {
      when(roleRepositoryMock.findByName(role.getName())).thenReturn(null);

      // When
      testObj.validateRoles(roles);
    } finally {
      // Then
      verify(roleRepositoryMock).findByName(role.getName());
    }
  }

  @Test
  public void shouldDealWithNullSetOfRoles() {
    // When
    testObj.validateRoles(null);
    // Then
    verify(roleRepositoryMock, never()).findByName(any(String.class));
  }

  @Test
  public void shouldValidateGmcId() {
    testObj.validateGmcId(GMC_ID);
  }

  @Test
  public void shouldValidateGmcIdIfNull() {
    testObj.validateGmcId(null);
  }

  @Test(expected = CustomParameterizedException.class)
  public void shouldThrowExceptionIfGmcIdIsMoreThanSevenChars() {
    testObj.validateGmcId(GMC_ID_TOO_LONG);
  }
}
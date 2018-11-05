package com.transformuk.hee.tis.profile.validators;

import com.transformuk.hee.tis.profile.domain.Permission;
import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.profile.repository.RoleRepository;
import com.transformuk.hee.tis.profile.web.rest.errors.CustomParameterizedException;
import com.transformuk.hee.tis.profile.web.rest.errors.ErrorConstants;
import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import jdk.nashorn.internal.ir.SetSplitState;
import net.logstash.logback.encoder.org.apache.commons.lang.ObjectUtils;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HeeUserValidatorTest {

  private static final String DBC_ABBR = "DBC_ABBR";
  private static final String DBC = "DBC";
  private static final String INVAlID_DBC = "INVALID";
  private static final long ID = 1L;
  private static final long ID_INVALID = 2L;
  private static final String DBC_NAME = "DBC_NAME";
  private static final String ROLENAME = "ROLENAME";
  private static final String PERMISSION_NAME = "PERMISSION_NAME";
  private static final String OTHER_PERMISSION = "OTHER_PERMISSION";
  private static final String ACCEPTABLEPASSWORD = "ACCEPTABLEPASSWORD";
  private static final String SHORTPW = "SHORTPW";
  private static final String GMC_ID = "1234567";
  private static final String GMC_ID_TOO_LONG = "12345678";

  private Set<String> dbcCodes = Sets.newLinkedHashSet(DBC);
  private Set<String> dbcCodes_invalid = Sets.newLinkedHashSet(INVAlID_DBC);

  private DBCDTO dbcdto = new DBCDTO();
  private DBCDTO dbcdto_invalid = new DBCDTO();

  private Role role = new Role();
  private Permission permission = new Permission();
  private Permission permission1 = new Permission();
  private Set<Role> roles = Sets.newLinkedHashSet(role);

    CustomParameterizedException customParameterizedException = new CustomParameterizedException("Invalid Designated Body Code: "
      + INVAlID_DBC, ErrorConstants.ERR_VALIDATION);

  @Mock
  private HeeUserRepository heeUserRepositoryMock;

  @Mock
  private ReferenceService referenceServiceMock;

  @Mock
  RoleRepository roleRepositoryMock;

  @InjectMocks
  private HeeUserValidator testObj;

  @Before
  public void setup() {
    dbcdto.setAbbr(DBC_ABBR);
    dbcdto.setDbc(DBC);
    dbcdto.setId(ID);
    dbcdto.setName(DBC_NAME);
    dbcdto.setStatus(Status.CURRENT);

    dbcdto_invalid.setDbc(INVAlID_DBC);
    dbcdto_invalid.setId(ID_INVALID);
    dbcdto_invalid.setStatus(Status.CURRENT);


    permission.setName(PERMISSION_NAME);
    permission1.setName(OTHER_PERMISSION);
    Set<Permission> permissions = Sets.newLinkedHashSet(permission, permission1);
    role.setName(ROLENAME);
    role.setPermissions(permissions);
  }

  @Test
  public void shouldValidateDBCIds () {
    // Given
    when(referenceServiceMock.getDBCByCode(DBC)).thenReturn(new ResponseEntity<>(dbcdto, HttpStatus.OK));

    // When
    testObj.validateDBCIds(dbcCodes);

    // Then
    verify(referenceServiceMock).getDBCByCode(DBC);
  }

  @Test(expected = CustomParameterizedException.class)
  public void shouldThrowExceptionifInvalidDBC() {
    // Given
    when(referenceServiceMock.getDBCByCode(INVAlID_DBC)).thenThrow(customParameterizedException);

    // When
    testObj.validateDBCIds(dbcCodes_invalid);

    // Then
    verify(referenceServiceMock).getDBCByCode(INVAlID_DBC);
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
  public void shouldThrowExceptionIfNullRole() {
    // Given
    when(roleRepositoryMock.findByName(role.getName())).thenReturn(null);

    // When
    testObj.validateRoles(roles);

    // Then
    verify(roleRepositoryMock).findByName(role.getName());
  }

  @Test
  public void shouldValidateValidPassword() {
    testObj.validatePassword(ACCEPTABLEPASSWORD);
  }

  @Test(expected = CustomParameterizedException.class)
  public void shouldThrowExceptionIfPasswordTooShort() {
    testObj.validatePassword(SHORTPW);
  }

  @Test(expected = CustomParameterizedException.class)
  public void shouldThrowExceptionIfPasswordEmpty() {
    testObj.validatePassword(StringUtils.EMPTY);
  }

  @Test
  public void shouldValidateNonNullTemporaryPassword() {
    testObj.validateIsTemporary(true);
  }

  @Test(expected = CustomParameterizedException.class)
  public void shouldThrowExceptionWithNullTemporaryPassword() {
    testObj.validateIsTemporary(null);
  }

  @Test
  public void shouldValidateGmcId() {
    testObj.validateGmcId(GMC_ID);
  }

  @Test(expected = CustomParameterizedException.class)
  public void shouldThrowExceptionIfGmcIdIsMoreThanSevenChars() {
    testObj.validateGmcId(GMC_ID_TOO_LONG);
  }
}
package com.transformuk.hee.tis.profile.assembler;

import static com.transformuk.hee.tis.profile.assembler.UserProfileAssembler.PRINCIPLE_SEPARATOR;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.Permission;
import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.domain.UserTrust;
import com.transformuk.hee.tis.profile.repository.PermissionRepository;
import com.transformuk.hee.tis.security.model.Trust;
import com.transformuk.hee.tis.security.model.UserProfile;
import java.util.Set;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserProfileAssemblerTest {

  private static final Set<String> USER_DESIGNATED_BODY_CODES = Sets.newHashSet("DBC1", "DBC2");
  private static final String USER_EMAIL = "email@email.com";
  private static final String USER_FIRST_NAME = "first";
  private static final String USER_LAST_NAME = "last";
  private static final String USER_GMC_ID = "123GMC";
  private static final String USER_PHONE_NUMBER = "0208 112 3325";
  private static final String USER_NAME = "first-last-name";
  private static final String TRUST_CODE = "TRUST CODE";
  private static final Long TRUST_ID = 1L;
  private static final String TRUST_NAME = "TRUST NAME";

  @InjectMocks
  private UserProfileAssembler testObj;

  @Mock
  private PermissionRepository permissionRepositoryMock;
  @Mock
  private HeeUser heeUserMock;
  @Mock
  private Role role1Mock, role2Mock;
  @Mock
  private Permission permission1Mock, permission2Mock, permissionPolicyMock;
  @Mock
  private UserTrust userTrustMock;

  @Before
  public void before() {
    when(heeUserMock.getDesignatedBodyCodes()).thenReturn(USER_DESIGNATED_BODY_CODES);
    when(heeUserMock.getEmailAddress()).thenReturn(USER_EMAIL);
    when(heeUserMock.getFirstName()).thenReturn(USER_FIRST_NAME);
    when(heeUserMock.getLastName()).thenReturn(USER_LAST_NAME);
    when(heeUserMock.getGmcId()).thenReturn(USER_GMC_ID);
    when(heeUserMock.getPhoneNumber()).thenReturn(USER_PHONE_NUMBER);
    when(heeUserMock.getName()).thenReturn(USER_NAME);

    Set<Role> roles = Sets.newHashSet(role1Mock, role2Mock);
    when(heeUserMock.getRoles()).thenReturn(roles);

    when(role1Mock.getName()).thenReturn("Role1");
    when(role1Mock.getPermissions()).thenReturn(Sets.newHashSet(permission1Mock));
    when(role2Mock.getName()).thenReturn("Role2");
    when(role2Mock.getPermissions()).thenReturn(Sets.newHashSet(permission2Mock));
    when(permissionPolicyMock.getPrincipal()).thenReturn("xxxx" + PRINCIPLE_SEPARATOR + USER_NAME);
  }

  @Test
  public void toUserProfileReturnsUserProfileWithPopulatedHeeUserData() {

    when(permissionRepositoryMock.findByPrincipalEndsWith(PRINCIPLE_SEPARATOR + USER_NAME))
        .thenReturn(Lists.newArrayList(permissionPolicyMock));

    UserProfile result = testObj.toUserProfile(heeUserMock);
    Assert.assertEquals(USER_EMAIL, result.getEmailAddress());
    Assert.assertEquals(USER_FIRST_NAME, result.getFirstName());
    Assert.assertEquals(USER_LAST_NAME, result.getLastName());
    Assert.assertEquals(USER_GMC_ID, result.getGmcId());
    Assert.assertEquals(USER_PHONE_NUMBER, result.getPhoneNumber());
    Assert.assertEquals(USER_NAME, result.getUserName());
  }

  @Test
  public void toUserProfileReturnsUserProfileWithTrusts() {

    when(heeUserMock.getAssociatedTrusts()).thenReturn(Sets.newHashSet(userTrustMock));
    when(userTrustMock.getTrustCode()).thenReturn(TRUST_CODE);
    when(userTrustMock.getTrustId()).thenReturn(TRUST_ID);
    when(userTrustMock.getTrustName()).thenReturn(TRUST_NAME);

    UserProfile result = testObj.toUserProfile(heeUserMock);
    Set<Trust> trusts = result.getAssignedTrusts();
    Trust first = trusts.iterator().next();
    Assert.assertEquals(TRUST_CODE, first.getTrustCode());
    Assert.assertEquals(TRUST_NAME, first.getTrustName());
    Assert.assertEquals(TRUST_ID, first.getId());

  }

}
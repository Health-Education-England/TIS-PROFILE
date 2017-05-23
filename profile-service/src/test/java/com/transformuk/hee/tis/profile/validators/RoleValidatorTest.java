package com.transformuk.hee.tis.profile.validators;

import com.transformuk.hee.tis.profile.domain.Permission;
import com.transformuk.hee.tis.profile.repository.HeeUserRepository;
import com.transformuk.hee.tis.profile.repository.PermissionRepository;
import com.transformuk.hee.tis.profile.web.rest.TestUtil;
import com.transformuk.hee.tis.profile.web.rest.errors.CustomParameterizedException;
import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import java.util.Arrays;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleValidatorTest {

	@Mock
	private PermissionRepository permissionRepository;

	@Mock
	private HeeUserRepository heeUserRepository;

	private EntityManager em;

	@InjectMocks
	private RoleValidator roleValidator;


	@Test
	public void validatePermissions() {
		//Given
		Permission permission = new Permission();
		permission.setName(TestUtil.DEFAULT_NAME);

		//When
		when(permissionRepository.findByName(TestUtil.DEFAULT_NAME)).thenReturn(permission);
		roleValidator.validatePermissions(Sets.newHashSet(Arrays.asList(permission)));

		//then
		verify(permissionRepository).findByName(TestUtil.DEFAULT_NAME);

	}

	@Test(expected = CustomParameterizedException.class)
	public void shouldThrowErrorForInvalidPermissions() throws Exception {
		//Given
		Permission permission = new Permission();
		permission.setName(TestUtil.DEFAULT_NAME);

		//When
		when(permissionRepository.findByName(TestUtil.DEFAULT_NAME)).thenReturn(null);
		roleValidator.validatePermissions(Sets.newHashSet(Arrays.asList(permission)));

		//then
		verify(permissionRepository).findByName(TestUtil.DEFAULT_NAME);

	}

	@Test
	public void validateBeforeDelete() {
		//Given
		String roleName = TestUtil.DEFAULT_NAME;
		long expectedCount = 0;

		//When
		when(heeUserRepository.countByRolesNameAndActive(roleName, true)).thenReturn(expectedCount);
		roleValidator.validateBeforeDelete(roleName);

		//then
		verify(heeUserRepository).countByRolesNameAndActive(roleName, true);

	}

	@Test(expected = CustomParameterizedException.class)
	public void shouldThrowCannotDeleteUserAssociated() throws Exception {
		//Given
		String roleName = TestUtil.DEFAULT_NAME;
		long expectedCount = 2L;

		//When
		when(heeUserRepository.countByRolesNameAndActive(roleName, true)).thenReturn(expectedCount);
		roleValidator.validateBeforeDelete(roleName);

		//then
		verify(heeUserRepository).countByRolesNameAndActive(roleName, true);

	}

}

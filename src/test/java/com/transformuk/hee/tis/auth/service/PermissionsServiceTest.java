package com.transformuk.hee.tis.auth.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.auth.model.Permission;
import com.transformuk.hee.tis.auth.model.Role;
import com.transformuk.hee.tis.auth.repository.RoleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PermissionsServiceTest {

	public static final String ROLE_NAME = "admin";
	public static final Set<String> ROLE_NAMES = Sets.newHashSet("RVAdmin", "RVOfficer");
	public static final Permission PERM1 = new Permission("Perm1");
	public static final Permission PERM2 = new Permission("Perm2");
	public static final Permission PERM3 = new Permission("Perm3");
	public static final Role RV_ADMIN = new Role("RVAdmin", Sets.newHashSet(PERM1, PERM2));
	public static final Role RV_OFFICER = new Role("RVOfficer", Sets.newHashSet(PERM2, PERM3));

	@Mock
	private RoleRepository repository;

	@InjectMocks
	private PermissionsService service;

	@Test
	public void shouldAggregatePermissionsForRoles() {
		// given
		given(repository.findByNameIn(ROLE_NAMES)).willReturn(Lists.newArrayList(RV_ADMIN, RV_OFFICER));

		//when
		Set<String> permSet = service.getPermissions(ROLE_NAMES);

		//then
		assertEquals(Sets.newHashSet("Perm1", "Perm2", "Perm3"), permSet);
	}
}

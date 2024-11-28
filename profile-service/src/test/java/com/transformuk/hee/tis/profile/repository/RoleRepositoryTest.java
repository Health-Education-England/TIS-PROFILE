package com.transformuk.hee.tis.profile.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.Role;
import java.util.Collection;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
class RoleRepositoryTest {

  private static final String ROLE_1 = "role1";
  private static final String RESTRICTED_ROLE_1 = "RVOfficer";
  private static final String RESTRICTED_ROLE_2 = "Machine User";
  private static final String RESTRICTED_ROLE_3 = "HEE";

  private static final Collection<String> restrictedRoles = Set.of(RESTRICTED_ROLE_1,
      RESTRICTED_ROLE_2, RESTRICTED_ROLE_3);

  @Autowired
  RoleRepository roleRepository;

  private Role restrictedRole1, restrictedRole2, restrictedRole3, role1;

  private void createRoles() {
    role1 = new Role(ROLE_1, null);
    restrictedRole1 = new Role(RESTRICTED_ROLE_1, null);
    restrictedRole2 = new Role(RESTRICTED_ROLE_2, null);
    restrictedRole3 = new Role(RESTRICTED_ROLE_3, null);
  }

  @BeforeEach
  public void setup() {
    roleRepository.deleteAll();
    createRoles();
  }

  @Test
  void shouldFindByNameNotIn() {
    roleRepository.saveAll(Set.of(role1, restrictedRole1, restrictedRole2, restrictedRole3));
    Pageable pageable = PageRequest.of(0, 50);
    Page<Role> result = roleRepository.findByNameNotIn(restrictedRoles, pageable);

    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    assertEquals(ROLE_1, result.getContent().get(0).getName());
  }

  @AfterEach
  public void teardown() {
    roleRepository.deleteAll();
  }
}

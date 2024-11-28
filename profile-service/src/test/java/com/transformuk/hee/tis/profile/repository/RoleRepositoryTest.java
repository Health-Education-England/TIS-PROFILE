package com.transformuk.hee.tis.profile.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.Role;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
  private static final String RESTRICTED_ROLE_1 = "Restricted role";

  private static final Set<String> restrictedRoleSet = Set.of(RESTRICTED_ROLE_1);

  @Autowired
  RoleRepository roleRepository;

  private Role restrictedRole1, role1;

  private void createRoles() {
    role1 = new Role(ROLE_1, null);
    restrictedRole1 = new Role(RESTRICTED_ROLE_1, null);
  }

  @BeforeEach
  public void setup() {
    createRoles();
  }

  @Test
  void shouldFindByNameNotIn() {
    roleRepository.saveAll(Set.of(role1, restrictedRole1));
    int allRoleSize = roleRepository.findAll().size();
    Pageable pageable = PageRequest.of(0, 50);
    Page<Role> result = roleRepository.findByNameNotIn(restrictedRoleSet, pageable);

    assertNotNull(result);
    assertEquals(allRoleSize - restrictedRoleSet.size(), result.getContent().size());
    List<String> roleNameList = result.getContent().stream().map(role -> role.getName())
        .collect(Collectors.toList());
    assertThat(roleNameList).contains(ROLE_1);
    assertThat(roleNameList).doesNotContain(RESTRICTED_ROLE_1);
  }
}

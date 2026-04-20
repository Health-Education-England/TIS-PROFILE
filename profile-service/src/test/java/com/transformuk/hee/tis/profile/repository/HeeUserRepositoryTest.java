package com.transformuk.hee.tis.profile.repository;

import static com.google.common.collect.Sets.newHashSet;

import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.Role;
import com.transformuk.hee.tis.profile.domain.UserTrust;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
public class HeeUserRepositoryTest {

  public static final String TRUST_NAME_1 = "St Georges";
  public static final String TRUST_CODE_1 = "RJ7";
  public static final String TRUST_CODE_2 = "RA1";
  public static final long TRUST_ID_1 = 12345L;
  public static final long TRUST_ID_2 = 6789L;
  public static final String TRUST_NAME_2 = "St Pauls";
  private static final String GMC_ID_1 = "GMC ID1";
  private static final String EMAIL_1 = "EMAIL 1";
  private static final String FIRST_NAME_1 = "FIRST NAME 1";
  private static final String LAST_NAME_1 = "LAST NAME 1";
  private static final String NAME_1 = "NAME 1";
  private static final String PHONE_NUMBER_1 = "020202020020";
  private static final String GMC_ID_2 = "GMC ID2";
  private static final String EMAIL_2 = "EMAIL 2";
  private static final String FIRST_NAME_2 = "FIRST NAME 2";
  private static final String LAST_NAME_2 = "LAST NAME 2";
  private static final String NAME_2 = "NAME 2";
  private static final String PHONE_NUMBER_2 = "0101010101010";
  private static final String GMC_ID_3 = "GMC ID3";
  private static final String EMAIL_3 = "EMAIL 3";
  private static final String FIRST_NAME_3 = "FIRST NAME 3";
  private static final String LAST_NAME_3 = "LAST NAME 3";
  private static final String NAME_3 = "NAME 3";
  private static final String PHONE_NUMBER_3 = "03030303030";
  private static final String NAME_SEARCH_STRING = "Bo";
  private static final String LTFT_ADMIN_ROLE = "NHSE LTFT Admin";
  private static final String DBC_1 = "1-AIIDWX";
  private static final String DBC_2 = "1-OTHER";

  @Autowired
  private HeeUserRepository heeUserRepository;
  @Autowired
  private UserTrustRepository userTrustRepository;
  @Autowired
  private RoleRepository roleRepository;

  private UserTrust userTrust1, userTrust2;

  @Before
  public void setup() {
    // clear the existing users that's brought in via the DML
    heeUserRepository.deleteAll();
    userTrustRepository.deleteAll();

    createUserTrusts();
    createHeeUsers();
  }

  private void createHeeUsers() {
    HeeUser heeUserWithTrust = new HeeUser();
    heeUserWithTrust.setActive(true);
    heeUserWithTrust.setGmcId(GMC_ID_1);
    heeUserWithTrust.setEmailAddress(EMAIL_1);
    heeUserWithTrust.setFirstName(FIRST_NAME_1);
    heeUserWithTrust.setLastName(LAST_NAME_1);
    heeUserWithTrust.setName(NAME_1);
    heeUserWithTrust.setPhoneNumber(PHONE_NUMBER_1);
    heeUserWithTrust.addAssociatedTrust(userTrust1);
    heeUserWithTrust.addAssociatedTrust(userTrust2);

    HeeUser heeUserWithEmptyTrust = new HeeUser();
    heeUserWithEmptyTrust.setActive(true);
    heeUserWithEmptyTrust.setGmcId(GMC_ID_2);
    heeUserWithEmptyTrust.setEmailAddress(EMAIL_2);
    heeUserWithEmptyTrust.setFirstName(FIRST_NAME_2);
    heeUserWithEmptyTrust.setLastName(LAST_NAME_2);
    heeUserWithEmptyTrust.setName(NAME_2);
    heeUserWithEmptyTrust.setPhoneNumber(PHONE_NUMBER_2);

    HeeUser heeUserWithNullTrust = new HeeUser();
    heeUserWithNullTrust.setActive(true);
    heeUserWithNullTrust.setGmcId(GMC_ID_3);
    heeUserWithNullTrust.setEmailAddress(EMAIL_3);
    heeUserWithNullTrust.setFirstName(FIRST_NAME_3);
    heeUserWithNullTrust.setLastName(LAST_NAME_3);
    heeUserWithNullTrust.setName(NAME_3);
    heeUserWithNullTrust.setPhoneNumber(PHONE_NUMBER_3);
    heeUserWithNullTrust.setAssociatedTrusts(null);

    heeUserRepository.saveAndFlush(heeUserWithTrust);
    heeUserRepository.saveAndFlush(heeUserWithEmptyTrust);
    heeUserRepository.saveAndFlush(heeUserWithNullTrust);

    userTrustRepository.saveAndFlush(userTrust1);
    userTrustRepository.saveAndFlush(userTrust2);
  }

  private void createUserTrusts() {
    userTrust1 = new UserTrust();
    userTrust1.setTrustCode(TRUST_CODE_1);
    userTrust1.setTrustId(TRUST_ID_1);
    userTrust1.setTrustName(TRUST_NAME_1);

    userTrust2 = new UserTrust();
    userTrust2.setTrustCode(TRUST_CODE_2);
    userTrust2.setTrustId(TRUST_ID_2);
    userTrust2.setTrustName(TRUST_NAME_2);

  }

  @After
  public void teardown() {
    heeUserRepository.deleteAll();
    userTrustRepository.deleteAll();
  }


  @Transactional
  @Test
  public void getAllUsersShouldAllowForLazyFetchOfAssociatedTrusts() {
    Pageable pageable = PageRequest.of(0, 100);
    Page<HeeUser> result = heeUserRepository.findAll(pageable);

    Assert.assertNotNull(result);
    Assert.assertEquals(3, result.getContent().size());

    Optional<HeeUser> optionalUserWithTrusts = findUserWithGMCId(GMC_ID_1, result.getContent());
    Assert.assertTrue(optionalUserWithTrusts.isPresent());

    HeeUser heeUserWithTrusts = optionalUserWithTrusts.get();
    Set<UserTrust> associatedTrusts = heeUserWithTrusts.getAssociatedTrusts();
    Assert.assertNotNull(associatedTrusts);
    Assert.assertEquals(2, associatedTrusts.size());

    Optional<UserTrust> optionalUserTrust1 = findTrust(TRUST_CODE_1, associatedTrusts);
    Assert.assertTrue(optionalUserTrust1.isPresent());
    Assert.assertEquals(TRUST_NAME_1, optionalUserTrust1.get().getTrustName());

    Optional<UserTrust> optionalUserTrust2 = findTrust(TRUST_CODE_2, associatedTrusts);
    Assert.assertTrue(optionalUserTrust2.isPresent());
    Assert.assertEquals(TRUST_NAME_2, optionalUserTrust2.get().getTrustName());

    Optional<HeeUser> optionalUserWithTrusts2 = findUserWithGMCId(GMC_ID_2, result.getContent());
    Assert.assertTrue(optionalUserWithTrusts2.isPresent());
    Assert.assertEquals(0, optionalUserWithTrusts2.get().getAssociatedTrusts().size());

    Optional<HeeUser> optionalUserWithTrusts3 = findUserWithGMCId(GMC_ID_3, result.getContent());
    Assert.assertTrue(optionalUserWithTrusts3.isPresent());
    Assert.assertNull(optionalUserWithTrusts3.get().getAssociatedTrusts());
  }

  @Transactional
  @Test
  public void findByNameLikeShouldReturnAllUsersWhereNameIsLike() {

    HeeUser user1 = new HeeUser(), user2 = new HeeUser(), user3 = new HeeUser();
    user1.setName("Bob");
    user1.setLastName("");
    user1.emailAddress("");
    user2.setName("James");
    user2.setLastName("");
    user2.emailAddress("");
    user3.setName("aBo");
    user3.setLastName("");
    user3.emailAddress("");
    heeUserRepository.saveAll(Lists.newArrayList(user1, user2, user3));
    heeUserRepository.flush();

    Pageable page = PageRequest.of(0, 100);

    Page<HeeUser> results = heeUserRepository
        .findByNameIgnoreCaseContaining(page, NAME_SEARCH_STRING);

    Assert.assertEquals(2, results.getTotalElements());
    Optional<HeeUser> foundBobOptional = results.getContent().stream()
        .filter(user -> StringUtils.equals("Bob", user.getName())).findAny();
    Assert.assertTrue(foundBobOptional.isPresent());
    Optional<HeeUser> foundBoOptional = results.getContent().stream()
        .filter(user -> StringUtils.equals("aBo", user.getName())).findAny();
    Assert.assertTrue(foundBoOptional.isPresent());
  }


  private Optional<HeeUser> findUserWithGMCId(String gmcId1, List<HeeUser> result) {
    return result.stream()
        .filter(heeUser -> StringUtils.equals(gmcId1, heeUser.getGmcId()))
        .findAny();
  }

  private Optional<UserTrust> findTrust(String code, Set<UserTrust> userTrusts) {
    return userTrusts.stream()
        .filter(ut -> StringUtils.equals(code, ut.getTrustCode()))
        .findAny();
  }

  @Test
  public void findByRoleAndDbcShouldReturnActiveUsersMatchingRoleAndDbc() {
    Role ltftAdminRole = new Role();
    ltftAdminRole.setName(LTFT_ADMIN_ROLE);
    roleRepository.saveAndFlush(ltftAdminRole);

    HeeUser matchingUser = new HeeUser();
    matchingUser.setName("admin.user");
    matchingUser.setFirstName("Alice");
    matchingUser.setLastName("Admin");
    matchingUser.emailAddress("alice@hee.nhs.uk");
    matchingUser.setActive(true);
    matchingUser.setRoles(newHashSet(ltftAdminRole));
    matchingUser.setDesignatedBodyCodes(newHashSet(DBC_1));
    heeUserRepository.saveAndFlush(matchingUser);

    List<HeeUser> results = heeUserRepository.findByRoleAndDbc(LTFT_ADMIN_ROLE, DBC_1);

    Assert.assertNotNull(results);
    Assert.assertEquals(1, results.size());
    Assert.assertEquals("admin.user", results.get(0).getName());

    heeUserRepository.deleteById("admin.user");
    roleRepository.deleteById(LTFT_ADMIN_ROLE);
  }

  @Test
  public void findByRoleAndDbcShouldNotReturnUsersWithDifferentDbc() {
    Role ltftAdminRole = new Role();
    ltftAdminRole.setName(LTFT_ADMIN_ROLE);
    roleRepository.saveAndFlush(ltftAdminRole);

    HeeUser userWithDifferentDbc = new HeeUser();
    userWithDifferentDbc.setName("admin.other.dbc");
    userWithDifferentDbc.setFirstName("Bob");
    userWithDifferentDbc.setLastName("Admin");
    userWithDifferentDbc.emailAddress("bob@hee.nhs.uk");
    userWithDifferentDbc.setActive(true);
    userWithDifferentDbc.setRoles(newHashSet(ltftAdminRole));
    userWithDifferentDbc.setDesignatedBodyCodes(newHashSet(DBC_2));
    heeUserRepository.saveAndFlush(userWithDifferentDbc);

    List<HeeUser> results = heeUserRepository.findByRoleAndDbc(LTFT_ADMIN_ROLE, DBC_1);

    Assert.assertTrue(results.stream()
        .noneMatch(u -> u.getName().equals("admin.other.dbc")));

    heeUserRepository.deleteById("admin.other.dbc");
    roleRepository.deleteById(LTFT_ADMIN_ROLE);
  }

  @Test
  public void findByRoleAndDbcShouldNotReturnInactiveUsers() {
    Role ltftAdminRole = new Role();
    ltftAdminRole.setName(LTFT_ADMIN_ROLE);
    roleRepository.saveAndFlush(ltftAdminRole);

    HeeUser inactiveUser = new HeeUser();
    inactiveUser.setName("admin.inactive");
    inactiveUser.setFirstName("Carol");
    inactiveUser.setLastName("Admin");
    inactiveUser.emailAddress("carol@hee.nhs.uk");
    inactiveUser.setActive(false);
    inactiveUser.setRoles(newHashSet(ltftAdminRole));
    inactiveUser.setDesignatedBodyCodes(newHashSet(DBC_1));
    heeUserRepository.saveAndFlush(inactiveUser);

    List<HeeUser> results = heeUserRepository.findByRoleAndDbc(LTFT_ADMIN_ROLE, DBC_1);

    Assert.assertTrue(results.stream()
        .noneMatch(u -> u.getName().equals("admin.inactive")));

    heeUserRepository.deleteById("admin.inactive");
    roleRepository.deleteById(LTFT_ADMIN_ROLE);
  }

  @Test
  public void findByRoleAndDbcShouldNotReturnUsersWithDifferentRole() {
    Role ltftAdminRole = new Role();
    ltftAdminRole.setName(LTFT_ADMIN_ROLE);
    Role otherRole = new Role();
    otherRole.setName("Other Role");
    roleRepository.saveAndFlush(ltftAdminRole);
    roleRepository.saveAndFlush(otherRole);

    HeeUser userWithOtherRole = new HeeUser();
    userWithOtherRole.setName("admin.other.role");
    userWithOtherRole.setFirstName("Dave");
    userWithOtherRole.setLastName("Admin");
    userWithOtherRole.emailAddress("dave@hee.nhs.uk");
    userWithOtherRole.setActive(true);
    userWithOtherRole.setRoles(newHashSet(otherRole));
    userWithOtherRole.setDesignatedBodyCodes(newHashSet(DBC_1));
    heeUserRepository.saveAndFlush(userWithOtherRole);

    List<HeeUser> results = heeUserRepository.findByRoleAndDbc(LTFT_ADMIN_ROLE, DBC_1);

    Assert.assertTrue(results.stream()
        .noneMatch(u -> u.getName().equals("admin.other.role")));

    heeUserRepository.deleteById("admin.other.role");
    roleRepository.deleteById(LTFT_ADMIN_ROLE);
    roleRepository.deleteById("Other Role");
  }

  @Test
  public void findByRoleAndDbcShouldReturnResultsOrderedByFirstNameThenLastName() {
    Role ltftAdminRole = new Role();
    ltftAdminRole.setName(LTFT_ADMIN_ROLE);
    roleRepository.saveAndFlush(ltftAdminRole);

    HeeUser userZara = new HeeUser();
    userZara.setName("admin.zara");
    userZara.setFirstName("Zara");
    userZara.setLastName("Admin");
    userZara.emailAddress("zara@hee.nhs.uk");
    userZara.setActive(true);
    userZara.setRoles(newHashSet(ltftAdminRole));
    userZara.setDesignatedBodyCodes(newHashSet(DBC_1));

    HeeUser userAnna = new HeeUser();
    userAnna.setName("admin.anna");
    userAnna.setFirstName("Anna");
    userAnna.setLastName("Admin");
    userAnna.emailAddress("anna@hee.nhs.uk");
    userAnna.setActive(true);
    userAnna.setRoles(newHashSet(ltftAdminRole));
    userAnna.setDesignatedBodyCodes(newHashSet(DBC_1));

    heeUserRepository.saveAndFlush(userZara);
    heeUserRepository.saveAndFlush(userAnna);

    List<HeeUser> results = heeUserRepository.findByRoleAndDbc(LTFT_ADMIN_ROLE, DBC_1);

    Assert.assertTrue(results.size() >= 2);
    int annaIndex = -1, zaraIndex = -1;
    for (int i = 0; i < results.size(); i++) {
      if ("admin.anna".equals(results.get(i).getName())) {
        annaIndex = i;
      }
      if ("admin.zara".equals(results.get(i).getName())) {
        zaraIndex = i;
      }
    }
    Assert.assertTrue("Anna should be present in the results", annaIndex != -1);
    Assert.assertTrue("Zara should be present in the results", zaraIndex != -1);
    Assert.assertTrue("Anna should appear before Zara", annaIndex < zaraIndex);

    heeUserRepository.deleteById("admin.zara");
    heeUserRepository.deleteById("admin.anna");
    roleRepository.deleteById(LTFT_ADMIN_ROLE);
  }
}
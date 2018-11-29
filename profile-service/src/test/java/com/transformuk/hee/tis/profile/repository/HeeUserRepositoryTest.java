package com.transformuk.hee.tis.profile.repository;

import com.transformuk.hee.tis.profile.ProfileApp;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.UserTrust;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApp.class)
public class HeeUserRepositoryTest {

  public static final String TRUST_NAME_1 = "St Georges";
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
  public static final String TRUST_CODE_1 = "RJ7";
  public static final String TRUST_CODE_2 = "RA1";
  public static final long TRUST_ID_1 = 12345L;
  public static final long TRUST_ID_2 = 6789L;
  public static final String TRUST_NAME_2 = "St Pauls";
  private static final String NAME_SEARCH_STRING = "Bo";

  @Autowired
  private HeeUserRepository heeUserRepository;
  @Autowired
  private UserTrustRepository userTrustRepository;

  private HeeUser heeUserWithTrust, heeUserWithEmptyTrust, heeUserWithNullTrust;
  private UserTrust userTrust1, userTrust2;

  @Before
  public void setup() {
    // clear the existing users thats brought in via the DML
    heeUserRepository.deleteAll();
    userTrustRepository.deleteAll();

    createUserTrusts();
    createHeeUsers();
  }

  private void createHeeUsers() {
    heeUserWithTrust = new HeeUser();
    heeUserWithTrust.setActive(true);
    heeUserWithTrust.setGmcId(GMC_ID_1);
    heeUserWithTrust.setEmailAddress(EMAIL_1);
    heeUserWithTrust.setFirstName(FIRST_NAME_1);
    heeUserWithTrust.setLastName(LAST_NAME_1);
    heeUserWithTrust.setName(NAME_1);
    heeUserWithTrust.setPhoneNumber(PHONE_NUMBER_1);
    heeUserWithTrust.addAssociatedTrust(userTrust1);
    heeUserWithTrust.addAssociatedTrust(userTrust2);

    heeUserWithEmptyTrust = new HeeUser();
    heeUserWithEmptyTrust.setActive(true);
    heeUserWithEmptyTrust.setGmcId(GMC_ID_2);
    heeUserWithEmptyTrust.setEmailAddress(EMAIL_2);
    heeUserWithEmptyTrust.setFirstName(FIRST_NAME_2);
    heeUserWithEmptyTrust.setLastName(LAST_NAME_2);
    heeUserWithEmptyTrust.setName(NAME_2);
    heeUserWithEmptyTrust.setPhoneNumber(PHONE_NUMBER_2);

    heeUserWithNullTrust = new HeeUser();
    heeUserWithNullTrust.setActive(true);
    heeUserWithNullTrust.setGmcId(GMC_ID_3);
    heeUserWithNullTrust.setEmailAddress(EMAIL_3);
    heeUserWithNullTrust.setFirstName(FIRST_NAME_3);
    heeUserWithNullTrust.setLastName(LAST_NAME_3);
    heeUserWithNullTrust.setName(NAME_3);
    heeUserWithNullTrust.setPhoneNumber(PHONE_NUMBER_3);
    heeUserWithNullTrust.setAssociatedTrusts(null);

    heeUserWithTrust = heeUserRepository.saveAndFlush(heeUserWithTrust);
    heeUserWithEmptyTrust = heeUserRepository.saveAndFlush(heeUserWithEmptyTrust);
    heeUserWithNullTrust = heeUserRepository.saveAndFlush(heeUserWithNullTrust);

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
    Pageable pageable = new PageRequest(0, 100);
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

    Optional<UserTrust> optionalUserTrust_2 = findTrust(TRUST_CODE_2, associatedTrusts);
    Assert.assertTrue(optionalUserTrust_2.isPresent());
    Assert.assertEquals(TRUST_NAME_2, optionalUserTrust_2.get().getTrustName());

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
    user2.setName("James");
    user3.setName("aBo");
    heeUserRepository.saveAll(Lists.newArrayList(user1, user2, user3));
    heeUserRepository.flush();

    Pageable page = new PageRequest(0, 100);

    Page<HeeUser> results = heeUserRepository.findByNameIgnoreCaseContaining(page, NAME_SEARCH_STRING);

    Assert.assertEquals(2, results.getTotalElements());
    Optional<HeeUser> foundBobOptional = results.getContent().stream().filter(user -> StringUtils.equals("Bob", user.getName())).findAny();
    Assert.assertTrue(foundBobOptional.isPresent());
    Optional<HeeUser> foundBoOptional = results.getContent().stream().filter(user -> StringUtils.equals("aBo", user.getName())).findAny();
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
}

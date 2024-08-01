package com.transformuk.hee.tis.profile.web.rest;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.profile.domain.HeeUser;
import com.transformuk.hee.tis.profile.domain.Role;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.springframework.http.MediaType;

/**
 * Utility class for testing REST controllers.
 */
public class TestUtil {

  public static final String DEFAULT_NAME = "AAAAAAAAAA";
  public static final String UPDATED_NAME = "BBBBBBBBBB";

  public static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
  public static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

  public static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
  public static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

  public static final String DEFAULT_GMC_ID = "1234567";
  public static final String UPDATED_GMC_ID = "7654321";

  public static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
  public static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

  public static final String DEFAULT_EMAIL_ADDRESS = "AAAAAAAAAA";
  public static final String UPDATED_EMAIL_ADDRESS = "BBBBBBBBBB";

  public static final Boolean DEFAULT_ACTIVE = false;
  public static final Boolean UPDATED_ACTIVE = true;

  public static final String DEFAULT_ROLE = "Hee Admin Revalidation";
  public static final String DEFAULT_DESIGNATED_CODE = "1-AIIDH1";

  /**
   * MediaType for JSON UTF8
   */
  public static final MediaType JSON = MediaType.APPLICATION_JSON;

  /**
   * Convert an object to JSON byte array.
   *
   * @param object the object to convert
   * @return the JSON byte array
   * @throws IOException Any Exception creating or using the mapper, including
   *                     {@link com.fasterxml.jackson.core.JsonProcessingException }.
   */
  public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    JavaTimeModule module = new JavaTimeModule();
    mapper.registerModule(module);

    return mapper.writeValueAsBytes(object);
  }

  /**
   * Create a byte array with a specific size filled with specified data.
   *
   * @param size the size of the byte array
   * @param data the data to put in the byte array
   * @return the JSON byte array
   */
  public static byte[] createByteArray(int size, String data) {
    byte[] byteArray = new byte[size];
    for (int i = 0; i < size; i++) {
      byteArray[i] = Byte.parseByte(data, 2);
    }
    return byteArray;
  }

  /**
   * Creates a matcher that matches when the examined string reprensents the same instant as the
   * profile datetime
   *
   * @param date the profile datetime against which the examined string is checked
   */
  public static ZonedDateTimeMatcher sameInstant(ZonedDateTime date) {
    return new ZonedDateTimeMatcher(date);
  }

  /**
   * Verifies the equals/hashcode contract on the domain object.
   */
  public static void equalsVerifier(Class<?> clazz) throws Exception {
    Object domainObject1 = clazz.getConstructor().newInstance();
    assertThat(domainObject1.toString()).isNotNull();
    // Test with an instance of another class
    Object testOtherObject = new Object();
    assertThat(domainObject1).isNotEqualTo(testOtherObject);
    // Test with an instance of the same class
    Object domainObject2 = clazz.getConstructor().newInstance();
    assertThat(domainObject1).isNotEqualTo(domainObject2);
    // HashCodes are equals because the objects are not persisted yet
    assertThat(domainObject1.hashCode()).isEqualTo(domainObject2.hashCode());
  }

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static HeeUser createEntityHeeUser() {
    Role role = new Role();
    role.setName(DEFAULT_ROLE);

    HeeUser heeUser = new HeeUser()
        .name(DEFAULT_NAME)
        .firstName(DEFAULT_FIRST_NAME)
        .lastName(DEFAULT_LAST_NAME)
        .gmcId(DEFAULT_GMC_ID)
        .phoneNumber(DEFAULT_PHONE_NUMBER)
        .emailAddress(DEFAULT_EMAIL_ADDRESS)
        .active(DEFAULT_ACTIVE);

    heeUser.setRoles(Sets.newHashSet(role));
    heeUser.setDesignatedBodyCodes(Sets.newHashSet(DEFAULT_DESIGNATED_CODE));
    return heeUser;
  }

  /**
   * A matcher that tests that the examined string represents the same instant as the profile
   * datetime.
   */
  public static class ZonedDateTimeMatcher extends TypeSafeDiagnosingMatcher<String> {

    private final ZonedDateTime date;

    public ZonedDateTimeMatcher(ZonedDateTime date) {
      this.date = date;
    }

    @Override
    protected boolean matchesSafely(String item, Description mismatchDescription) {
      try {
        if (!date.isEqual(ZonedDateTime.parse(item))) {
          mismatchDescription.appendText("was ").appendValue(item);
          return false;
        }
        return true;
      } catch (DateTimeParseException e) {
        mismatchDescription.appendText("was ").appendValue(item)
            .appendText(", which could not be parsed as a ZonedDateTime");
        return false;
      }

    }

    @Override
    public void describeTo(Description description) {
      description.appendText("a String representing the same Instant as ").appendValue(date);
    }
  }
}

package com.transformuk.hee.tis.profile.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Set;
import org.junit.Test;

public class LocalOfficeToDbcMapperTest {

  private static final String NE_LO = "North East";
  private static final String NW_LO = "North West";
  private static final String YH_LO = "Yorkshire and the Humber";
  private static final String LONDON_LO = "LaSE";

  @Test
  public void shouldMapLocalOffices() {
    //Given
    Set<String> localOffices = Sets.newHashSet(NE_LO, NW_LO, YH_LO, LONDON_LO);
    //When
    Set<String> response = LocalOfficeToDbcMapper.map(localOffices);

    //Then
    assertThat(response)
        .hasSize(8)
        .contains("1-AIIDSI", "1-AIIDNQ", "1-AIIDQQ", "1-AIIDR8", "1-AIIDWA", "1-AIIDVS",
            "1-AIIDWI", "LDN-MOCK-DBC");
  }

  @Test
  public void shouldMapNimdtaLocalOffice() {
    Set<String> map = LocalOfficeToDbcMapper.map(Collections.singleton("1-25U-830"));

    assertThat(map)
        .hasSize(1)
        .contains("Northern Ireland Medical and Dental Training Agency");
  }

  @Test
  public void shouldHandleUnknownLocalOffices() {
    //Given
    Set<String> localOffices = Sets.newHashSet("unknown");
    //When
    Set<String> response = LocalOfficeToDbcMapper.map(localOffices);

    //Then
    assertThat(response).hasSize(0);
  }

  @Test(expected = NullPointerException.class)
  public void shouldHandleNull() {
    //When
    LocalOfficeToDbcMapper.map(null);
  }
}

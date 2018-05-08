package com.transformuk.hee.tis.profile.service.mapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class LocalOfficeToDbcMapper {

  /** Maps between a local office and designated body code
   *
   */
  private static final Map<String, List<String>> localOfficeToDbcMap = ImmutableMap.<String, List<String>>builder()
      .put("LaSE", Lists.newArrayList("1-AIIDR8","1-AIIDWA","1-AIIDVS","1-AIIDWI","LDN-MOCK-DBC"))
      .put("London LETBs", Lists.newArrayList("1-AIIDR8","1-AIIDWA","1-AIIDVS","1-AIIDWI","LDN-MOCK-DBC"))
      .put("London (NWL, NCEL, SL)", Lists.newArrayList("1-AIIDWA","1-AIIDVS","1-AIIDWI","LDN-MOCK-DBC"))
      .put("Kent Surrey and Sussex",Lists.newArrayList("1-AIIDR8"))
      .put("North West London",Lists.newArrayList("1-AIIDWA"))
      .put("North Central and East London",Lists.newArrayList("1-AIIDVS"))
      .put("South London",Lists.newArrayList("1-AIIDWI"))
      .put("East Midlands",Lists.newArrayList("1-AIIDSA"))
      .put("East of England",Lists.newArrayList("1-AIIDWT"))
      .put("North East",Lists.newArrayList("1-AIIDSI"))
      .put("Thames Valley",Lists.newArrayList("1-AIIDH1"))
      .put("Yorkshire and the Humber",Lists.newArrayList("1-AIIDQQ"))
      .put("West Midlands",Lists.newArrayList("1-AIIDMY"))
      .put("South West",Lists.newArrayList("1-AIIDMQ"))
      .put("Wessex",Lists.newArrayList("1-AIIDHJ"))
      .put("North West",Lists.newArrayList("1-AIIDNQ"))
      .build();

  public static Set<String> map(Set<String> localOffices) {
    Preconditions.checkNotNull(localOffices);
    Set<String> dbcs = Sets.newHashSet();
    localOffices.forEach(localOffice -> {
      if(localOfficeToDbcMap.containsKey(localOffice)) {
        dbcs.addAll(localOfficeToDbcMap.get(localOffice));
      }
    });
    return dbcs;
  }
}

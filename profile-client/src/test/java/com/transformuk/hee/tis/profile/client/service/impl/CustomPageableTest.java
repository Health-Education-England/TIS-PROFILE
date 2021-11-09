package com.transformuk.hee.tis.profile.client.service.impl;

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CustomPageableTest {

  private CustomPageable customPageable;

  @Before
  public void setup() {
    List<Object> objectsList = Arrays.asList(new Object(), new Object(), new Object());
    customPageable = new CustomPageable(objectsList, 2, 1, 1);
  }

  @Test
  public void shouldGetNextPageRequestParameters() {
    Assert.assertEquals("page=3&size=1", customPageable.getNextPageRequestParameters());
  }

  @Test
  public void shouldGetPreviousPageRequestParameters() {
    Assert.assertEquals("page=1&size=1", customPageable.getPreviousPageRequestParameters());
  }
}

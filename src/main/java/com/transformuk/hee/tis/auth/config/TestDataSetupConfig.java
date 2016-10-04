package com.transformuk.hee.tis.auth.config;

import com.transformuk.hee.tis.auth.service.TestDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;


@Configuration
@Profile("testData")
public class TestDataSetupConfig {

	@Autowired private TestDataService testDataService;

	@PostConstruct
	public void setupTestData() {
		testDataService.setupTestData();
	}
}

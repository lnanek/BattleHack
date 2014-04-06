package com.paypal.core;

import java.net.MalformedURLException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GoogleAppEngineHttpConnectionTest {

	GoogleAppEngineHttpConnection googleAppEngineHttpConnection;
	HttpConfiguration httpConfiguration;

	@BeforeClass
	public void beforeClass() {
		googleAppEngineHttpConnection = new GoogleAppEngineHttpConnection();
		httpConfiguration = new HttpConfiguration();
	}

	@AfterClass
	public void afterClass() {
		googleAppEngineHttpConnection = null;
		httpConfiguration = null;
	}

	@Test(expectedExceptions = MalformedURLException.class)
	public void checkMalformedURLExceptionTest() throws Exception {
		httpConfiguration.setEndPointUrl("ww.paypal.in");
		googleAppEngineHttpConnection
				.createAndconfigureHttpConnection(httpConfiguration);
	}

}

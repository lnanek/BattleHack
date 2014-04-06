package com.paypal.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;

import junit.framework.Assert;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidResponseDataException;

public class HttpConnectionTest {
	HttpConnection connection;
	HttpConfiguration httpConfiguration;

	@BeforeClass
	public void beforeClass() throws MalformedURLException, IOException {
		ConnectionManager connectionMgr = ConnectionManager.getInstance();
		connection = connectionMgr.getConnection();
		httpConfiguration = new HttpConfiguration();

	}

	@Test(expectedExceptions = MalformedURLException.class)
	public void checkMalformedURLExceptionTest() throws Exception {
		httpConfiguration.setEndPointUrl("ww.paypal.in");
		connection.createAndconfigureHttpConnection(httpConfiguration);
	}

	@Test(expectedExceptions = InvocationTargetException.class)
	public void readMethodTest() throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, IOException {
		Method readMethod = HttpConnection.class.getDeclaredMethod("read",
				BufferedReader.class);
		readMethod.setAccessible(true);
		BufferedReader br = null;
		readMethod.invoke(connection, br);
	}

	@Test
	public void executeTest() throws InvalidResponseDataException,
			HttpErrorException, ClientActionRequiredException, IOException,
			InterruptedException {
		httpConfiguration
				.setEndPointUrl("https://svcs.sandbox.paypal.com/AdaptivePayments/ConvertCurrency");
		connection.createAndconfigureHttpConnection(httpConfiguration);
		String response = connection.execute("url", "payload", null);
		Assert.assertTrue(response.contains("<ack>Failure</ack>"));
	}

	@AfterClass
	public void afterClass() {
		connection = null;
		httpConfiguration = null;
	}

}

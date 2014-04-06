package com.paypal.core;

import java.io.IOException;
import java.net.MalformedURLException;

import junit.framework.Assert;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.paypal.exception.SSLConfigurationException;

public class DefaultHttpConnectionTest {

	DefaultHttpConnection defaultHttpConnection;
	HttpConfiguration httpConfiguration;

	@BeforeClass
	public void beforeClass() {
		defaultHttpConnection = new DefaultHttpConnection();
		httpConfiguration = new HttpConfiguration();
	}

	@AfterClass
	public void afterClass() {
		defaultHttpConnection = null;
		httpConfiguration = null;
	}

	@Test(expectedExceptions = MalformedURLException.class)
	public void checkMalformedURLExceptionTest() throws Exception {
		httpConfiguration.setEndPointUrl("ww.paypal.in");
		defaultHttpConnection
				.createAndconfigureHttpConnection(httpConfiguration);
	}

	@Test(expectedExceptions = SSLConfigurationException.class)
	public void checkSSLConfigurationExceptionTest()
			throws SSLConfigurationException {
		defaultHttpConnection.setupClientSSL("certPath", "certKey");
	}

	@Test(dataProvider = "configParamsForProxy", dataProviderClass = DataProviderClass.class)
	public void createAndConfigureHttpConnectionForProxyTest(
			ConfigManager config) throws IOException {

		httpConfiguration.setGoogleAppEngine(Boolean.parseBoolean(config.getConfigurationMap().get(Constants.GOOGLE_APP_ENGINE)));
		if (Boolean.parseBoolean(config.getConfigurationMap().get(Constants.USE_HTTP_PROXY))) {
			httpConfiguration.setProxyPort(Integer.parseInt(config.getConfigurationMap().get(Constants.HTTP_PROXY_PORT)));
			httpConfiguration.setProxyHost(config.getConfigurationMap().get(Constants.HTTP_PROXY_HOST));
			httpConfiguration.setProxyUserName(config.getConfigurationMap().get(Constants.HTTP_PROXY_USERNAME));
			httpConfiguration.setProxyPassword(config.getConfigurationMap().get(Constants.HTTP_PROXY_PASSWORD));
		}
		httpConfiguration.setConnectionTimeout(Integer.parseInt(config.getConfigurationMap().get(Constants.HTTP_CONNECTION_TIMEOUT)));
		httpConfiguration.setMaxRetry(Integer.parseInt(config.getConfigurationMap().get(Constants.HTTP_CONNECTION_RETRY)));
		httpConfiguration.setReadTimeout(Integer.parseInt(config.getConfigurationMap().get(Constants.HTTP_CONNECTION_READ_TIMEOUT)));
		httpConfiguration.setMaxHttpConnection(Integer.parseInt(config.getConfigurationMap().get(Constants.HTTP_CONNECTION_MAX_CONNECTION)));
		httpConfiguration
				.setEndPointUrl("https://svcs.sandbox.paypal.com/AdaptivePayments/ConvertCurrency");
		defaultHttpConnection
				.createAndconfigureHttpConnection(httpConfiguration);
		Assert.assertEquals(
				Integer.parseInt(System.getProperty("http.maxConnections")),
				httpConfiguration.getMaxHttpConnection());
	}

}

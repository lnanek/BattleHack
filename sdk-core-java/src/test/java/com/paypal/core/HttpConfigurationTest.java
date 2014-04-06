package com.paypal.core;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

import com.paypal.core.HttpConfiguration;

public class HttpConfigurationTest {
	HttpConfiguration httpConf;

	@BeforeClass
	public void beforeClass() {
		httpConf = new HttpConfiguration();
	}

	@AfterClass
	public void afterClass() {
		httpConf = null;
	}

	@Test(priority = 0)
	public void getConnectionTimeoutTest() {
		Assert.assertEquals(httpConf.getConnectionTimeout(), 0);
	}

	@Test(priority = 1)
	public void getEndPointUrlTest() {
		Assert.assertEquals(httpConf.getEndPointUrl(), null);
	}

	@Test(priority = 2)
	public void getMaxHttpConnectionTest() {
		Assert.assertEquals(httpConf.getMaxHttpConnection(), 10);
	}

	@Test(priority = 3)
	public void getMaxRetryTest() {
		Assert.assertEquals(httpConf.getMaxRetry(), 2);
	}

	@Test(priority = 4)
	public void getProxyHostTest() {
		Assert.assertEquals(httpConf.getProxyHost(), null);
	}

	@Test(priority = 5)
	public void getProxyPortTest() {
		Assert.assertEquals(httpConf.getProxyPort(), -1);
	}

	@Test(priority = 6)
	public void getReadTimeoutTest() {
		Assert.assertEquals(httpConf.getReadTimeout(), 0);
	}

	@Test(priority = 7)
	public void getRetryDelayTest() {
		Assert.assertEquals(httpConf.getRetryDelay(), 1000);
	}

	@Test(priority = 9)
	public void isProxySetTest() {
		Assert.assertEquals(httpConf.isProxySet(), false);
	}

	@Test(priority = 11)
	public void setAndGetConnectionTimeoutTest() {
		httpConf.setConnectionTimeout(5000);
		Assert.assertEquals(httpConf.getConnectionTimeout(), 5000);
	}

	@Test(priority = 12)
	public void setAndGetEndPointUrlTest() {
		httpConf.setEndPointUrl("https://svcs.sandbox.paypal.com/Invoice/CreateInvoice");
		Assert.assertEquals(httpConf.getEndPointUrl(),
				"https://svcs.sandbox.paypal.com/Invoice/CreateInvoice");
	}

	@Test(priority = 14)
	public void setAndGetMaxHttpConnectionTest() {
		httpConf.setMaxHttpConnection(3);
		Assert.assertEquals(httpConf.getMaxHttpConnection(), 3);
	}

	@Test(priority = 15)
	public void setAndGetMaxRetryTest() {
		httpConf.setMaxRetry(2);
		Assert.assertEquals(httpConf.getMaxRetry(), 2);
	}

	@Test(priority = 16)
	public void setAndGetProxyHostTest() {
		httpConf.setProxyHost(null);
		Assert.assertEquals(httpConf.getProxyHost(), null);
	}

	@Test(priority = 17)
	public void setAndGetProxyPortTest() {
		httpConf.setProxyPort(8080);
		Assert.assertEquals(httpConf.getProxyPort(), 8080);
	}

	@Test(priority = 18)
	public void setAndIsProxySetTest() {
		httpConf.setProxySet(true);
		Assert.assertEquals(httpConf.isProxySet(), true);
	}

	@Test(priority = 19)
	public void setAndGetReadTimeoutTest() {
		httpConf.setReadTimeout(10);
		Assert.assertEquals(httpConf.getReadTimeout(), 10);
	}

	@Test(priority = 20)
	public void setAndGetRetryDelayTest() {
		httpConf.setRetryDelay(30);
		Assert.assertEquals(httpConf.getRetryDelay(), 30);
	}


	@Test(priority = 21)
	public void setAndGetProxyUserName() {
		httpConf.setProxyUserName("proxyUser");
		Assert.assertEquals(httpConf.getProxyUserName(), "proxyUser");
	}

	@Test(priority = 22)
	public void setAndGetProxyPassword() {
		httpConf.setProxyPassword("proxyPassword");
		Assert.assertEquals(httpConf.getProxyPassword(), "proxyPassword");
	}

}

package com.paypal.core.nvp;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.paypal.core.ConfigManager;
import com.paypal.core.CredentialManager;
import com.paypal.core.ConfigurationUtil;
import com.paypal.core.SDKUtil;
import com.paypal.core.credential.ICredential;
import com.paypal.core.credential.SignatureCredential;
import com.paypal.core.credential.TokenAuthorization;
import com.paypal.core.soap.MerchantAPICallPreHandler;

public class PlatformAPICallPreHandlerTest {

	@BeforeClass
	public void setUp() {
	}

	@Test()
	public void getHeaderMapSignatureWithoutTokenTest()
			throws Exception {
		Map<String, String> initMap = ConfigurationUtil.getSignatureConfiguration();
		CredentialManager credentialmgr = new CredentialManager(initMap);
		ICredential signatureCredential = credentialmgr
				.getCredentialObject("jb-us-seller_api1.paypal.com");
		PlatformAPICallPreHandler platformApiCaller = new PlatformAPICallPreHandler(
				"payload", "servicename", "method", signatureCredential,
				"sdkname", "1.0.0", "portName", initMap);
		Map<String, String> header = platformApiCaller.getHeaderMap();
		Assert.assertEquals("jb-us-seller_api1.paypal.com",
				header.get("X-PAYPAL-SECURITY-USERID"));
		Assert.assertEquals("WX4WTU3S8MY44S7F",
				header.get("X-PAYPAL-SECURITY-PASSWORD"));
		Assert.assertEquals(
				"AFcWxV21C7fd0v3bYYYRCpSSRl31A7yDhhsPUU2XhtMoZXsWHFxu-RWy",
				header.get("X-PAYPAL-SECURITY-SIGNATURE"));
		Assert.assertEquals("APP-80W284485P519543T",
				header.get("X-PAYPAL-APPLICATION-ID"));
		Assert.assertEquals("NV", header.get("X-PAYPAL-REQUEST-DATA-FORMAT"));
		Assert.assertEquals("NV", header.get("X-PAYPAL-RESPONSE-DATA-FORMAT"));
	}

	@Test()
	public void getHeaderMapCertificateWithoutTokenTest() throws Exception {
		Map<String, String> initMap = ConfigurationUtil
				.getCertificateConfiguration();
		CredentialManager credentialmgr = new CredentialManager(initMap);
		ICredential certificateCredential = credentialmgr
				.getCredentialObject("certuser_biz_api1.paypal.com");
		PlatformAPICallPreHandler platformApiCaller = new PlatformAPICallPreHandler(
				"payload", "servicename", "method", certificateCredential,
				"sdkname", "1.0.0", "portName", initMap);
		Map<String, String> header = platformApiCaller.getHeaderMap();
		Assert.assertEquals("certuser_biz_api1.paypal.com",
				header.get("X-PAYPAL-SECURITY-USERID"));
		Assert.assertEquals("D6JNKKULHN3G5B8A",
				header.get("X-PAYPAL-SECURITY-PASSWORD"));
		Assert.assertNull(header.get("X-PAYPAL-SECURITY-SIGNATURE"));
		Assert.assertEquals("APP-80W284485P519543T",
				header.get("X-PAYPAL-APPLICATION-ID"));
		Assert.assertEquals("NV", header.get("X-PAYPAL-REQUEST-DATA-FORMAT"));
		Assert.assertEquals("NV", header.get("X-PAYPAL-RESPONSE-DATA-FORMAT"));
	}

	@Test
	public void getHeaderMapWithSignatureUserTokenTest() throws Exception {
		Map<String, String> initMap = ConfigurationUtil
				.getSignatureConfiguration();
		PlatformAPICallPreHandler platformApiCaller = new PlatformAPICallPreHandler(
				"payload", "servicename", "method",
				"jb-us-seller_api1.paypal.com", "accessToken", "tokenSecret",
				"sdkname", "1.0.0", "portName", initMap);
		Map<String, String> header = platformApiCaller.getHeaderMap();
		String authHeader = (String) header.get("X-PAYPAL-AUTHORIZATION");
		String[] headers = authHeader.split(",");
		Assert.assertEquals("token=accessToken", headers[0]);
	}

	@Test()
	public void getHeaderMapWithCertificateUserTokenTest() throws Exception {
		Map<String, String> initMap = ConfigurationUtil
				.getCertificateConfiguration();
		PlatformAPICallPreHandler platformApiCaller = new PlatformAPICallPreHandler(
				"payload", "servicename", "method",
				"certuser_biz_api1.paypal.com", "accessToken", "tokenSecret",
				"sdkname", "1.0.0", "portName", initMap);
		Map<String, String> header = platformApiCaller.getHeaderMap();
		String authHeader = (String) header.get("X-PAYPAL-AUTHORIZATION");
		String[] headers = authHeader.split(",");
		Assert.assertEquals("token=accessToken", headers[0]);
	}

	@Test
	public void payloadEndpointCredentialTest() throws Exception {
		Map<String, String> initMap = ConfigurationUtil
				.getSignatureConfiguration();
		PlatformAPICallPreHandler platformApiCaller = new PlatformAPICallPreHandler(
				"payload", "servicename", "method",
				"jb-us-seller_api1.paypal.com", "accessToken", "tokenSecret",
				"sdkname", "1.0.0", "portName", initMap);
		Assert.assertEquals(
				"https://svcs.sandbox.paypal.com/servicename/method",
				platformApiCaller.getEndPoint());
		Assert.assertEquals("payload", platformApiCaller.getPayLoad());
		SignatureCredential signatureCredential = (SignatureCredential) platformApiCaller
				.getCredential();
		TokenAuthorization thirdAuth = (TokenAuthorization) signatureCredential
				.getThirdPartyAuthorization();
		Assert.assertEquals("accessToken", thirdAuth.getAccessToken());
		Assert.assertEquals("tokenSecret", thirdAuth.getTokenSecret());
	}

	@Test()
	public void setGetSDKVersionAndNameTest()
			throws Exception {
		Map<String, String> initMap = ConfigurationUtil
				.getSignatureConfiguration();
		PlatformAPICallPreHandler platformApiCaller = new PlatformAPICallPreHandler(
				"payload", "servicename", "method",
				"jb-us-seller_api1.paypal.com", "accessToken", "tokenSecret",
				"testSDK", "1.0.0", "portName", initMap);
		Assert.assertEquals("1.0.0", platformApiCaller.getSdkVersion());
		Assert.assertEquals("testSDK", platformApiCaller.getSdkName());
	}
}

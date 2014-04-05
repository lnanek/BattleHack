package com.paypal.core.soap;

import java.io.ByteArrayInputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.paypal.core.ConfigManager;
import com.paypal.core.Constants;
import com.paypal.core.CredentialManager;
import com.paypal.core.ConfigurationUtil;
import com.paypal.core.DefaultSOAPAPICallHandler;
import com.paypal.core.credential.ICredential;

public class MerchantAPICallPreHandlerTest {

	@BeforeClass
	public void setUp() {
	}

	@Test()
	public void getHeaderMapSignatureTest() throws Exception {
		Map<String, String> initMap = ConfigurationUtil
				.getSignatureConfiguration();
		CredentialManager credentialmgr = new CredentialManager(initMap);
		ICredential signatureCredential = credentialmgr
				.getCredentialObject("jb-us-seller_api1.paypal.com");
		DefaultSOAPAPICallHandler defaultSoaphandler = new DefaultSOAPAPICallHandler(
				"<Request>test</Request>", null, null, initMap);
		MerchantAPICallPreHandler soapHandler = new MerchantAPICallPreHandler(
				defaultSoaphandler, signatureCredential, "sdkName", "1.0.0",
				"portName", initMap);
		Map<String, String> headers = soapHandler.getHeaderMap();
		Assert.assertNotNull(headers);
		assert (headers.size() > 0);
		Assert.assertEquals("jb-us-seller_api1.paypal.com",
				headers.get(Constants.PAYPAL_SECURITY_USERID_HEADER));
		Assert.assertEquals("WX4WTU3S8MY44S7F",
				headers.get(Constants.PAYPAL_SECURITY_PASSWORD_HEADER));
		Assert.assertEquals(
				"AFcWxV21C7fd0v3bYYYRCpSSRl31A7yDhhsPUU2XhtMoZXsWHFxu-RWy",
				headers.get(Constants.PAYPAL_SECURITY_SIGNATURE_HEADER));
		Assert.assertEquals(Constants.PAYLOAD_FORMAT_SOAP,
				headers.get(Constants.PAYPAL_REQUEST_DATA_FORMAT_HEADER));
		Assert.assertEquals(Constants.PAYLOAD_FORMAT_SOAP,
				headers.get(Constants.PAYPAL_RESPONSE_DATA_FORMAT_HEADER));
	}

	@Test()
	public void getHeaderMapCertificateTest() throws Exception {
		Map<String, String> initMap = ConfigurationUtil
				.getCertificateConfiguration();
		CredentialManager credentialmgr = new CredentialManager(initMap);
		DefaultSOAPAPICallHandler defaultSoaphandler = new DefaultSOAPAPICallHandler(
				"<Request>test</Request>", null, null, initMap);
		ICredential certificateCredential = credentialmgr
				.getCredentialObject("certuser_biz_api1.paypal.com");
		MerchantAPICallPreHandler soapHandler = new MerchantAPICallPreHandler(
				defaultSoaphandler, certificateCredential, "sdkName", "1.0.0",
				"portName", initMap);
		Map<String, String> headers = soapHandler.getHeaderMap();
		Assert.assertNotNull(headers);
		assert (headers.size() > 0);
		Assert.assertEquals("certuser_biz_api1.paypal.com",
				headers.get(Constants.PAYPAL_SECURITY_USERID_HEADER));
		Assert.assertEquals("D6JNKKULHN3G5B8A",
				headers.get(Constants.PAYPAL_SECURITY_PASSWORD_HEADER));
		Assert.assertEquals(Constants.PAYLOAD_FORMAT_SOAP,
				headers.get(Constants.PAYPAL_REQUEST_DATA_FORMAT_HEADER));
		Assert.assertEquals(Constants.PAYLOAD_FORMAT_SOAP,
				headers.get(Constants.PAYPAL_RESPONSE_DATA_FORMAT_HEADER));
		Assert.assertEquals(
				soapHandler.getSdkName() + "-" + soapHandler.getSdkVersion(),
				headers.get(Constants.PAYPAL_REQUEST_SOURCE_HEADER));
	}

	@Test()
	public void getPayLoadForSignature() throws Exception {
		Map<String, String> initMap = ConfigurationUtil
				.getSignatureConfiguration();
		CredentialManager credentialmgr = new CredentialManager(initMap);
		ICredential signatureCredential = credentialmgr
				.getCredentialObject("jb-us-seller_api1.paypal.com");
		DefaultSOAPAPICallHandler defaultSoaphandler = new DefaultSOAPAPICallHandler(
				"<Request>test</Request>", null, null, initMap);
		MerchantAPICallPreHandler soapHandler = new MerchantAPICallPreHandler(
				defaultSoaphandler, signatureCredential, "sdkName", "1.0.0",
				"portName", initMap);
		String payload = soapHandler.getPayLoad();

		Document dom = loadXMLFromString(payload);
		Element docEle = dom.getDocumentElement();
		NodeList header = docEle.getElementsByTagName("soapenv:Header");
		NodeList requestCredential = ((Element) header.item(0))
				.getElementsByTagName("ns:RequesterCredentials");
		NodeList credential = ((Element) requestCredential.item(0))
				.getElementsByTagName("ebl:Credentials");
		NodeList user = ((Element) credential.item(0))
				.getElementsByTagName("ebl:Username");
		NodeList psw = ((Element) credential.item(0))
				.getElementsByTagName("ebl:Password");
		NodeList sign = ((Element) credential.item(0))
				.getElementsByTagName("ebl:Signature");

		String username = user.item(0).getTextContent();
		String password = psw.item(0).getTextContent();
		String signature = sign.item(0).getTextContent();

		Assert.assertEquals("jb-us-seller_api1.paypal.com", username);
		Assert.assertEquals("WX4WTU3S8MY44S7F", password);
		Assert.assertEquals(
				"AFcWxV21C7fd0v3bYYYRCpSSRl31A7yDhhsPUU2XhtMoZXsWHFxu-RWy",
				signature);

		NodeList requestBody = docEle.getElementsByTagName("Request");
		Node bodyContent = requestBody.item(0);
		String bodyText = bodyContent.getTextContent();
		Assert.assertEquals("test", bodyText);

	}

	@Test()
	public void getPayLoadForCertificate(ConfigManager conf) throws Exception {
		Map<String, String> initMap = ConfigurationUtil
				.getCertificateConfiguration();
		CredentialManager credentialmgr = new CredentialManager(initMap);
		DefaultSOAPAPICallHandler defaultSoaphandler = new DefaultSOAPAPICallHandler(
				"<Request>test</Request>", null, null, initMap);
		ICredential certificateCredential = credentialmgr
				.getCredentialObject("certuser_biz_api1.paypal.com");
		MerchantAPICallPreHandler soapHandler = new MerchantAPICallPreHandler(
				defaultSoaphandler, certificateCredential, "sdkName", "1.0.0",
				"portName", initMap);
		String payload = soapHandler.getPayLoad();

		Document dom = loadXMLFromString(payload);
		Element docEle = dom.getDocumentElement();
		NodeList header = docEle.getElementsByTagName("soapenv:Header");
		NodeList requestCredential = ((Element) header.item(0))
				.getElementsByTagName("ns:RequesterCredentials");
		NodeList credential = ((Element) requestCredential.item(0))
				.getElementsByTagName("ebl:Credentials");
		NodeList user = ((Element) credential.item(0))
				.getElementsByTagName("ebl:Username");
		NodeList psw = ((Element) credential.item(0))
				.getElementsByTagName("ebl:Password");

		String username = user.item(0).getTextContent();
		String password = psw.item(0).getTextContent();

		Assert.assertEquals("certuser_biz_api1.paypal.com", username);
		Assert.assertEquals("D6JNKKULHN3G5B8A", password);

		NodeList requestBody = docEle.getElementsByTagName("Request");
		Node bodyContent = requestBody.item(0);
		String bodyText = bodyContent.getTextContent();
		Assert.assertEquals("test", bodyText);
	}

	@Test()
	public void setGetSDKNameTest() throws Exception {
		Map<String, String> initMap = ConfigurationUtil
				.getCertificateConfiguration();
		CredentialManager credentialmgr = new CredentialManager(initMap);
		DefaultSOAPAPICallHandler defaultSoaphandler = new DefaultSOAPAPICallHandler(
				"<Request>test</Request>", null, null, initMap);
		ICredential certificateCredential = credentialmgr
				.getCredentialObject("certuser_biz_api1.paypal.com");
		MerchantAPICallPreHandler soapHandler = new MerchantAPICallPreHandler(
				defaultSoaphandler, certificateCredential, "testsdk", "1.0.0",
				"testsdkPortName", null);
		Assert.assertEquals("testsdk", soapHandler.getSdkName());
	}

	@Test()
	public void setGetSDKVersionTest(ConfigManager conf) throws Exception {
		Map<String, String> initMap = ConfigurationUtil
				.getCertificateConfiguration();
		CredentialManager credentialmgr = new CredentialManager(initMap);
		DefaultSOAPAPICallHandler defaultSoaphandler = new DefaultSOAPAPICallHandler(
				"<Request>test</Request>", null, null, initMap);
		ICredential certificateCredential = credentialmgr
				.getCredentialObject("certuser_biz_api1.paypal.com");
		MerchantAPICallPreHandler soapHandler = new MerchantAPICallPreHandler(
				defaultSoaphandler, certificateCredential, "testsdk", "1.0.0",
				"testsdkPortName", null);
		Assert.assertEquals("1.0.0", soapHandler.getSdkVersion());
	}

	@Test()
	public void getEndPointTest() throws Exception {
		Map<String, String> initMap = ConfigurationUtil
				.getCertificateConfiguration();
		CredentialManager credentialmgr = new CredentialManager(initMap);
		DefaultSOAPAPICallHandler defaultSoaphandler = new DefaultSOAPAPICallHandler(
				"<Request>test</Request>", null, null, initMap);
		ICredential certificateCredential = credentialmgr
				.getCredentialObject("certuser_biz_api1.paypal.com");
		MerchantAPICallPreHandler soapHandler = new MerchantAPICallPreHandler(
				defaultSoaphandler, certificateCredential, "testsdk", "1.0.0",
				"testsdkPortName", initMap);
		String endpoint = soapHandler.getEndPoint();
		Assert.assertEquals("https://api.sandbox.paypal.com/2.0", endpoint);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void MerchantAPICallPreHandlerConstructorTest() {
		Map<String, String> initMap = ConfigurationUtil
				.getCertificateConfiguration();
		DefaultSOAPAPICallHandler defaultSoaphandler = new DefaultSOAPAPICallHandler(
				"<Request>test</Request>", null, null, initMap);
		new MerchantAPICallPreHandler(defaultSoaphandler, null);
	}

	private Document loadXMLFromString(String xml) throws Exception {
		ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		return builder.parse(stream);
	}

	@AfterClass
	public void tearDown() {
	}

}

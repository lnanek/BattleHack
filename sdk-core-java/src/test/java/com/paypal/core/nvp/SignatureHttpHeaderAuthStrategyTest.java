package com.paypal.core.nvp;

import java.util.Map;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.paypal.core.credential.SignatureCredential;
import com.paypal.core.credential.TokenAuthorization;
import com.paypal.core.nvp.SignatureHttpHeaderAuthStrategy;

public class SignatureHttpHeaderAuthStrategyTest {

	@Test
	public void processTokenAuthorizationTest() throws Exception{
		SignatureHttpHeaderAuthStrategy signatureHttpHeaderAuthStrategy = new SignatureHttpHeaderAuthStrategy("https://svcs.sandbox.paypal.com/");
		TokenAuthorization tokenAuthorization = new TokenAuthorization("accessToken","tokenSecret");
		SignatureCredential signatureCredential = new SignatureCredential("testusername","testpassword","testsignature");
		Map header = signatureHttpHeaderAuthStrategy.processTokenAuthorization(signatureCredential, tokenAuthorization);
		String authHeader = (String)header.get("X-PAYPAL-AUTHORIZATION");
		String[] headers=authHeader.split(",");
		
		Assert.assertEquals("token=accessToken", headers[0]);
	}
	
	@Test
	public void generateHeaderStrategyWithoutTokenTest() throws Exception{
		SignatureHttpHeaderAuthStrategy signatureHttpHeaderAuthStrategy= new SignatureHttpHeaderAuthStrategy("https://svcs.sandbox.paypal.com/");
		SignatureCredential signatureCredential = new SignatureCredential("testusername","testpassword","testsignature");
		Map<String,String> header = signatureHttpHeaderAuthStrategy.generateHeaderStrategy(signatureCredential);
		String username = (String)header.get("X-PAYPAL-SECURITY-USERID");
		String psw = (String)header.get("X-PAYPAL-SECURITY-PASSWORD");
		String sign = (String)header.get("X-PAYPAL-SECURITY-SIGNATURE");
		Assert.assertEquals("testusername", username);
		Assert.assertEquals("testpassword", psw);
		Assert.assertEquals("testsignature", sign);
	}
}

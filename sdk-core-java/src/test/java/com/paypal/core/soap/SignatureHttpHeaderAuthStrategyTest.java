package com.paypal.core.soap;

import java.util.Map;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.paypal.core.credential.SignatureCredential;
import com.paypal.core.credential.TokenAuthorization;

public class SignatureHttpHeaderAuthStrategyTest {

	@Test
	public void processTokenAuthorizationTest() throws Exception{
		SignatureHttpHeaderAuthStrategy signatureHttpHeaderAuthStrategy = new SignatureHttpHeaderAuthStrategy("https://api-3t.sandbox.paypal.com/2.0");
		TokenAuthorization tokenAuthorization = new TokenAuthorization("accessToken","tokenSecret");
		SignatureCredential signatureCredential = new SignatureCredential("testusername","testpassword","testsignature");
		Map header = signatureHttpHeaderAuthStrategy.processTokenAuthorization(signatureCredential, tokenAuthorization);
		String authHeader = (String)header.get("X-PP-AUTHORIZATION");
		String[] headers=authHeader.split(",");
		
		Assert.assertEquals("token=accessToken", headers[0]);
	}
	
	@Test
	public void generateHeaderStrategyWithoutTokenTest() throws Exception{
		SignatureHttpHeaderAuthStrategy signatureHttpHeaderAuthStrategy= new SignatureHttpHeaderAuthStrategy("https://api-3t.sandbox.paypal.com/2.0");
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

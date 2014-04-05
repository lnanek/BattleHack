package com.paypal.core.credential;

import junit.framework.Assert;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.paypal.core.UnitTestConstants;
import com.paypal.exception.MissingCredentialException;

public class SignatureCredentialTest {

	SignatureCredential cred;

	@BeforeClass
	public void beforeClass() throws MissingCredentialException {
		cred = new SignatureCredential("platfo_1255077030_biz_api1.gmail.com",
				"1255077037",
				"Abg0gYcQyxQvnf2HDJkKtA-p6pqhA1k-KTYE0Gcy1diujFio4io5Vqjf");
	}

	@Test(priority = 0)
	public void getSignatureTest() {
		Assert.assertEquals(
				"Abg0gYcQyxQvnf2HDJkKtA-p6pqhA1k-KTYE0Gcy1diujFio4io5Vqjf",
				((SignatureCredential) cred).getSignature());
	}

	@Test(priority = 1)
	public void getPasswordTest() {
		Assert.assertEquals("1255077037", cred.getPassword());
	}

	@Test(priority = 2)
	public void getUserNameTest() {
		Assert.assertEquals("platfo_1255077030_biz_api1.gmail.com",
				cred.getUserName());
	}

	@Test(priority = 3)
	public void setAndGetAppId() {
		cred.setApplicationId("APP-80W284485P519543T");
		Assert.assertEquals("APP-80W284485P519543T", cred.getApplicationId());
	}

	@Test(priority = 4)
	public void setAndGetThirdPartyAuthorizationForSubjectAuthorization() {
		ThirdPartyAuthorization thirdPartyAuthorization = new SubjectAuthorization(
				"Subject");
		cred.setThirdPartyAuthorization(thirdPartyAuthorization);
		thirdPartyAuthorization = cred.getThirdPartyAuthorization();
		Assert.assertEquals(
				((SubjectAuthorization) thirdPartyAuthorization).getSubject(),
				"Subject");

	}

	@Test(priority = 5)
	public void setAndGetThirdPartyAuthorizationForTokenAuthorization() {
		ThirdPartyAuthorization thirdPartyAuthorization = new TokenAuthorization(
				UnitTestConstants.ACCESS_TOKEN, UnitTestConstants.TOKEN_SECRET);
		cred.setThirdPartyAuthorization(thirdPartyAuthorization);
		thirdPartyAuthorization = cred.getThirdPartyAuthorization();
		Assert.assertEquals(
				((TokenAuthorization) thirdPartyAuthorization).getAccessToken(),
				UnitTestConstants.ACCESS_TOKEN);
		Assert.assertEquals(
				((TokenAuthorization) thirdPartyAuthorization).getTokenSecret(),
				UnitTestConstants.TOKEN_SECRET);

	}

	@Test(priority = 6, expectedExceptions = IllegalArgumentException.class)
	public void illegalArgumentExceptionTest() {
		cred = new SignatureCredential(null, null, null);

	}

	@AfterClass
	public void afterClass() {
		cred = null;
	}
}

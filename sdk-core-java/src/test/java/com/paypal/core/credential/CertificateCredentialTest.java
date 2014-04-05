package com.paypal.core.credential;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.paypal.core.UnitTestConstants;
import com.paypal.exception.MissingCredentialException;

public class CertificateCredentialTest {
	CertificateCredential cred;

	@BeforeClass
	public void beforeClass() throws MissingCredentialException {
		cred = new CertificateCredential(
				"platfo_1255077030_biz_api1.gmail.com", "1255077037",
				"sdk-cert.p12", "KJAERUGBLVF6Y");
	}

	@Test(priority = 0)
	public void getCertificatePathTest() {
		Assert.assertEquals("sdk-cert.p12", cred.getCertificatePath());
	}

	@Test(priority = 1)
	public void getCertificateKeyTest() {
		Assert.assertEquals("KJAERUGBLVF6Y", cred.getCertificateKey());
	}

	@Test(priority = 2)
	public void getPasswordTest() {
		Assert.assertEquals("1255077037", cred.getPassword());
	}

	@Test(priority = 3)
	public void getUserNameTest() {
		Assert.assertEquals("platfo_1255077030_biz_api1.gmail.com",
				cred.getUserName());
	}

	@Test(priority = 4)
	public void setAndGetAppId() {
		cred.setApplicationId("APP-80W284485P519543T");
		Assert.assertEquals("APP-80W284485P519543T", cred.getApplicationId());
	}

	@Test(priority = 5)
	public void setAndGetThirdPartyAuthorization() {
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

	@Test(priority = 6)
	public void setAndGetThirdPartyAuthorizationForSubjectAuthorization() {
		ThirdPartyAuthorization thirdPartyAuthorization = new SubjectAuthorization(
				"Subject");
		cred.setThirdPartyAuthorization(thirdPartyAuthorization);
		thirdPartyAuthorization = cred.getThirdPartyAuthorization();
		Assert.assertEquals(
				((SubjectAuthorization) thirdPartyAuthorization).getSubject(),
				"Subject");

	}

	@Test(priority = 7, expectedExceptions = IllegalArgumentException.class)
	public void illegalArgumentExceptionTest() {
		cred = new CertificateCredential(null, null, null, null);

	}

	@AfterClass
	public void afterClass() {
		cred = null;

	}
}

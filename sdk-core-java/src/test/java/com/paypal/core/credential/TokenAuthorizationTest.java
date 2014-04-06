package com.paypal.core.credential;

import org.testng.annotations.Test;

public class TokenAuthorizationTest {

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void illegalArgumentExceptionTest() {
		TokenAuthorization tokenAuth = new TokenAuthorization(null, null);
	}
}

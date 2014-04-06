package com.paypal.core;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SDKUtilTest {

	@Test
	public void escapeInvalidXmlChars() {
		Assert.assertEquals(
				SDKUtil.escapeInvalidXmlChars("<ns:GetBalanceReq>&\";</ns:GetBalanceReq>"),
				"&lt;ns:GetBalanceReq&gt;&amp;&quot;;&lt;/ns:GetBalanceReq&gt;");
		Assert.assertEquals(SDKUtil.escapeInvalidXmlChars("&"), "&amp;");
		Assert.assertEquals(SDKUtil.escapeInvalidXmlChars("&amp;<"),
				"&amp;&lt;");
		Assert.assertEquals(SDKUtil.escapeInvalidXmlChars("&lt;&"), "&lt;&amp;");
		Assert.assertEquals(SDKUtil.escapeInvalidXmlChars("abc\"xyz"),
				"abc&quot;xyz");
	}

	@Test
	public void escapeInvalidXmlCharsRegexString() {
		Assert.assertEquals(
				SDKUtil.escapeInvalidXmlCharsRegex("<ns:GetBalanceReq>&\";</ns:GetBalanceReq>"),
				"&lt;ns:GetBalanceReq&gt;&amp;&quot;;&lt;/ns:GetBalanceReq&gt;");
		Assert.assertEquals(SDKUtil.escapeInvalidXmlCharsRegex("&"), "&amp;");
		Assert.assertEquals(SDKUtil.escapeInvalidXmlCharsRegex("&amp;<"),
				"&amp;&lt;");
		Assert.assertEquals(SDKUtil.escapeInvalidXmlCharsRegex("&lt;&"),
				"&lt;&amp;");
		Assert.assertEquals(SDKUtil.escapeInvalidXmlCharsRegex("abc\"xyz"),
				"abc&quot;xyz");
	}
	
}

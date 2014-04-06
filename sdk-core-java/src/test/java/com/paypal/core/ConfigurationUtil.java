package com.paypal.core;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationUtil {
	
	public static Map<String, String> getSignatureConfiguration() {
		Map<String, String> initMap = new HashMap<String, String>();
		initMap.put("acct1.UserName", "jb-us-seller_api1.paypal.com");
		initMap.put("acct1.Password", "WX4WTU3S8MY44S7F");
		initMap.put("acct1.Signature",
				"AFcWxV21C7fd0v3bYYYRCpSSRl31A7yDhhsPUU2XhtMoZXsWHFxu-RWy");
		initMap.put("acct1.AppId", "APP-80W284485P519543T");
		initMap.put("mode", "sandbox");
		return initMap;
	}

	public static Map<String, String> getCertificateConfiguration() {
		Map<String, String> initMap = new HashMap<String, String>();
		initMap.put("acct2.UserName", "certuser_biz_api1.paypal.com");
		initMap.put("acct2.Password", "D6JNKKULHN3G5B8A");
		initMap.put("acct2.CertKey", "password");
		initMap.put("acct2.CertPath", "src/test/resources/sdk-cert.p12");
		initMap.put("acct2.AppId", "APP-80W284485P519543T");
		initMap.put("mode", "sandbox");
		return initMap;
	}

}

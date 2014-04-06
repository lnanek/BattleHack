package com.paypal.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.DataProvider;

public class DataProviderClass {
	static ConfigManager conf;

	@DataProvider(name = "configParams")
	public static Object[][] configParams() throws FileNotFoundException,
			IOException {
		conf = ConfigManager.getInstance();
		InputStream in = DataProviderClass.class
				.getResourceAsStream("/sdk_config.properties");
		conf.load(in);
		return new Object[][] { new Object[] { conf } };
	}

	@DataProvider(name = "configParamsForSoap")
	public static Object[][] configParamsForSoap()
			throws FileNotFoundException, IOException {
		conf = ConfigManager.getInstance();
		InputStream in = DataProviderClass.class
				.getResourceAsStream("/sdk_config_soap.properties");
		conf.load(in);
		return new Object[][] { new Object[] { conf } };
	}

	@DataProvider(name = "configParamsForProxy")
	public static Object[][] configParamsForProxy()
			throws FileNotFoundException, IOException {
		conf = ConfigManager.getInstance();
		InputStream in = DataProviderClass.class
				.getResourceAsStream("/sdk_config_proxy.properties");
		conf.load(in);
		return new Object[][] { new Object[] { conf } };
	}
}

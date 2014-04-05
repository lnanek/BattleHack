package com.paypal.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.testng.annotations.Test;

public class BaseServiceTest {
	BaseService service;
	String incorrectFilePath = "src/test/resources/config.properties";
	String correctFilePath = "src/test/resources/sdk_config.properties";

	@Test
	public void initConfigTestUsingFilePathTest() throws IOException {
		BaseService.initConfig(correctFilePath);
	}

	@Test
	public void initConfigTestUsingFileTest() throws IOException {
		File file = new File(correctFilePath);
		BaseService.initConfig(file);
	}

	@Test
	public void initConfigTestUsingInputStreamTest() throws IOException {
		InputStream is = new FileInputStream(new File(correctFilePath));
		BaseService.initConfig(is);
	}

	@Test(expectedExceptions = FileNotFoundException.class)
	public void initConfigTestUsingFilePathForExceptionTest() throws Exception {
		BaseService.initConfig(incorrectFilePath);
	}

	@Test(expectedExceptions = FileNotFoundException.class)
	public void initConfigTestUsingFileForExceptionTest() throws Exception {
		File file = new File(incorrectFilePath);
		BaseService.initConfig(file);
	}

	@Test(expectedExceptions = FileNotFoundException.class)
	public void initConfigTestUsingInputStreamForExceptionTest()
			throws Exception {
		InputStream is = new FileInputStream(new File(incorrectFilePath));
		BaseService.initConfig(is);
	}

}

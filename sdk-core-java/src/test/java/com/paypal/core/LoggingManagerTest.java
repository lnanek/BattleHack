package com.paypal.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.paypal.core.APIService;
import com.paypal.core.LoggingManager;

public class LoggingManagerTest {

	@Test
	public void getLoggerTest() throws NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Method method = LoggingManager.class.getDeclaredMethod("getLogger",
				Class.class);
		method.setAccessible(true);
		Assert.assertNotNull(method.invoke(LoggingManager.class,
				APIService.class));
	}
}

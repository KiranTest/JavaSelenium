package com.hrsoft.test.setuphelpers;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
	private int retryCount = 0;
	private static final int MAX_RETRY_COUNT = 1;

	@Override
	public boolean retry(ITestResult result) {
		if (retryCount < MAX_RETRY_COUNT) {
			retryCount++;
			return true; 
		}
		return false; 
	}
}



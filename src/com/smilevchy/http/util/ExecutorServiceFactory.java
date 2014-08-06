package com.smilevchy.http.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceFactory {
	private static ExecutorService executorService = null;
	
	
	public static ExecutorService getExecutorService() {
		if (null == executorService) {
			executorService = Executors.newCachedThreadPool();
		}
		
		return executorService;
	}
}

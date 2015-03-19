package com.smilevchy.http.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceFactory {
	private static volatile ExecutorService executorService = null;
	
	
	public static ExecutorService getExecutorService() {
		if (executorService == null) {
			synchronized (ExecutorServiceFactory.class) {
				if (executorService == null) {
					executorService = Executors.newCachedThreadPool();		
				}
			}
		}
		
		return executorService;
	}
}

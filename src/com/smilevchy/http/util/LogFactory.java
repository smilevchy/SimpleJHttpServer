package com.smilevchy.http.util;

import java.util.logging.Logger;

public class LogFactory {
	private static Logger logger = null;
	
	
	public static Logger getLogger() {
		if (null == logger) {
			logger = Logger.getLogger("JHttpServer");
		}
		
		return logger;
	}
}

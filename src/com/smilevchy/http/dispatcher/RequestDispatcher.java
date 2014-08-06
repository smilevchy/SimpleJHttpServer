package com.smilevchy.http.dispatcher;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.util.logging.Level;

import com.smilevchy.http.processor.GetRequestProcessor;
import com.smilevchy.http.processor.PostRequestProcessor;
import com.smilevchy.http.processor.UnknownRequestProcessor;
import com.smilevchy.http.protocol.HttpMethod;
import com.smilevchy.http.util.ExecutorServiceFactory;
import com.smilevchy.http.util.LogFactory;


public class RequestDispatcher {
	
	public void dispatchRequest(Socket connection) {
		try {
			Reader in = new InputStreamReader(new BufferedInputStream(connection.getInputStream()), "utf-8");
			
			StringBuilder requestLine = new StringBuilder();
			while (true) {
				int c = in.read();
				
				if (c == '\r' || c == '\n' || c == -1)
					break;
			
				requestLine.append((char) c);
			}
			
			String requestLineStr = requestLine.toString();
			String[] requestLineTokens = requestLine.toString().split("\\s+");
			
			LogFactory.getLogger().info(connection.getRemoteSocketAddress() + " " + requestLineStr);
			
			if (isGet(requestLineStr)) {
				ExecutorServiceFactory.getExecutorService().execute(new GetRequestProcessor(connection, requestLineTokens));
			} else if (isPost(requestLineStr)) {
				ExecutorServiceFactory.getExecutorService().execute(new PostRequestProcessor(connection, requestLineTokens));
			} else {
				ExecutorServiceFactory.getExecutorService().execute(new UnknownRequestProcessor(connection, requestLineTokens));
			}
		} catch (IOException e) {
			LogFactory.getLogger().log(Level.WARNING, "Error talking to " + connection.getRemoteSocketAddress(), e);
		}
	}
	
	private boolean isGet(String header) {
		return whichMethod(header, HttpMethod.GET);
	}
	
	private boolean isPost(String header) {
		return whichMethod(header, HttpMethod.POST);
	}
	
	private boolean whichMethod(String header, HttpMethod httpMethod) {
		boolean result = false;
		
		String[] slices = header.split("\\s+");
		
		if (httpMethod.toString().equals(slices[0].toUpperCase())) {
			result = true;
		}
		
		return result;
	}
}

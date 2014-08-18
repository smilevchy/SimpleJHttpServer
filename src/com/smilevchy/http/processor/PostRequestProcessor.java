package com.smilevchy.http.processor;

import java.net.Socket;

import com.smilevchy.http.util.LogFactory;

public class PostRequestProcessor extends RequestProcessor {

	public PostRequestProcessor(Socket connection, String[] requestLineTokens) {
		super(connection, requestLineTokens);
	}

	@Override
	public void process(Socket connection) {
		LogFactory.getLogger().info("Handle a post request");
	}
}

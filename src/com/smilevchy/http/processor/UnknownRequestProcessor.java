package com.smilevchy.http.processor;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

import com.smilevchy.http.protocol.HttpResponseStatusCode;
import com.smilevchy.http.util.LogFactory;

public class UnknownRequestProcessor extends RequestProcessor {
	
	public UnknownRequestProcessor(Socket connection, String[] requestLineTokens) {
		super(connection, requestLineTokens);
	}

	@Override
	public void process(Socket connection) {
		try {
			sendError(HttpResponseStatusCode.STATUS_CODE_501);
		} finally {
			try {
				connection.close();
			} catch (IOException e) {
				LogFactory.getLogger().log(Level.WARNING, "Error closing connection to " + connection.getRemoteSocketAddress(), e);
			}
		}
	}
}

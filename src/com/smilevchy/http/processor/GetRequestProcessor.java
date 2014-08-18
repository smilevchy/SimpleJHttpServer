package com.smilevchy.http.processor;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

import com.smilevchy.http.protocol.HttpResponseStatusCode;
import com.smilevchy.http.server.Config;
import com.smilevchy.http.util.LogFactory;

public class GetRequestProcessor extends RequestProcessor {

	public GetRequestProcessor(Socket connection, String[] requestLineTokens) {
		super(connection, requestLineTokens);
	}

	@Override
	public void process(Socket connection) {
		LogFactory.getLogger().info("Handle a get request from " + connection.getRemoteSocketAddress());
		
		try {
			String fileName = "";
			
			if (requestLineTokens.length > 1) {
				fileName = requestLineTokens[1];
			}
			
			if (fileName.endsWith("/")){
				fileName += Config.indexFilename;
			}
			
			File theFile = new File(Config.docRootPath, fileName.substring(1, fileName.length()));
			
			// Don't let clients outside the document root
			if (theFile.canRead() && theFile.getCanonicalPath().startsWith(Config.docRootPath)) {
				sendSuccess(theFile);
			} else { // can't find the file
				sendError(HttpResponseStatusCode.STATUS_CODE_404);
			}
		} catch (IOException e) {
			LogFactory.getLogger().log(Level.WARNING, "Error talking to " + connection.getRemoteSocketAddress(), e);
		} finally {
			try {
				connection.close();
			} catch (IOException e) {
				LogFactory.getLogger().log(Level.WARNING, "Error closing connection to " + connection.getRemoteSocketAddress(), e);
			}
		}
	}
}

package com.smilevchy.http.processor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Date;
import java.util.logging.Level;

import com.smilevchy.http.protocol.HttpResponseStatusCode;
import com.smilevchy.http.util.LogFactory;

public abstract class RequestProcessor implements Runnable {
	public static final String RESPONSE_501 = "<HTML>\r\n" +
			"<HEAD><TITLE>Not Implemented</TITLE>\r\n" +
			"</HEAD>\r\n" + 
			"<BODY>" + 
			"<H1>HTTP Error 501: Not Implemented</H1>\r\n" +
			"</BODY></HTML>\r\n";
	public static final String RESPONSE_404 = "<HTML>\r\n" +
			"<HEAD><TITLE>File Not Found</TITLE>\r\n" +
			"</HEAD>\r\n" +
			"<BODY>" +
			"<H1>HTTP Error 404: File Not Found</H1>\r\n" +
			"</BODY></HTML>\r\n";
	
	protected String[] requestLineTokens = null;

	private Socket connection = null;
	
	
	public RequestProcessor(Socket connection, String[] requestLineTokens) {
		this.connection = connection;
		this.requestLineTokens = requestLineTokens;
	}
	
	@Override
	public void run() {
		process(connection);
	}
	
	protected void sendHeader(Writer out, String responseCode, String contentType, int length) throws IOException {
		out.write(responseCode + "\r\n");
		Date now = new Date();
		out.write("Date: " + now + "\r\n");
		out.write("Server: JHTTP 1.0\r\n");
		out.write("Content-length: " + length + "\r\n");
		out.write("Content-type: " + contentType + "\r\n\r\n");
		out.flush();
	}
	
	protected void sendSuccess(File file) {
		OutputStream raw = null;
		Writer out = null;
		
		try {
			raw = new BufferedOutputStream(connection.getOutputStream());
			out = new OutputStreamWriter(raw);
			
			String version = "";
			
			if (requestLineTokens.length > 2) {
				version = requestLineTokens[2];
			}
			
			byte[] theData = Files.readAllBytes(file.toPath());
			String contentType = URLConnection.getFileNameMap().getContentTypeFor(file.getName());
			
			if (version.startsWith("HTTP/")) { // send a MIME header
				sendHeader(out, "HTTP/1.0 200 OK", contentType, theData.length);
			}
			
			raw.write(theData);
			raw.flush();
		} catch (IOException e) {
			LogFactory.getLogger().log(Level.WARNING, "Error talking to " + connection.getRemoteSocketAddress(), e);
		} finally {
			if (null != raw) {
				try {
					raw.close();
				} catch (IOException e) {
					LogFactory.getLogger().log(Level.WARNING, "Error closing writer to " + connection.getRemoteSocketAddress(), e);
				}
			}
		}
	}
	
	protected void sendError(HttpResponseStatusCode code) {
		switch (code) {
			case STATUS_CODE_404:
				sendError404();
				break;
			case STATUS_CODE_501:
				sendError501();
				break;
			default:
				sendErrorDefault();
				break;
		}
	}
	
	private void sendError404() {
		Writer writer = null;
		
		try {
			String version = "";
			if (requestLineTokens.length > 2) {
				version = requestLineTokens[2];
			}
			
			writer = new PrintWriter(new BufferedOutputStream(connection.getOutputStream()));
			
			if (version.startsWith("HTTP/")) { // send a MIME header
				sendHeader(writer, "HTTP/1.0 404 File Not Found", "text/html; charset=utf-8", RESPONSE_404.length());
			}
			writer.write(RESPONSE_404);
			writer.flush();
		} catch (IOException e) {
			LogFactory.getLogger().log(Level.WARNING, "Error responding to " + connection.getRemoteSocketAddress(), e);
		} finally {
			if (null != writer) {
				try {
					writer.close();
				} catch (IOException e) {
					LogFactory.getLogger().log(Level.WARNING, "Error closing writer to " + connection.getRemoteSocketAddress(), e);
				}
			}
		}
	}
	
	private void sendError501() {
		Writer writer = null;
		
		try {
			String version = "";
			if (requestLineTokens.length > 2) {
				version = requestLineTokens[2];
			}
			
			writer = new PrintWriter(new BufferedOutputStream(connection.getOutputStream()));
			
			if (version.startsWith("HTTP/")) { // send a MIME header
				sendHeader(writer, "HTTP/1.0 501 Not Implemented", "text/html; charset=utf-8", RESPONSE_501.length());
			}
			writer.write(RESPONSE_501);
			writer.flush();
		} catch (IOException e) {
			LogFactory.getLogger().log(Level.WARNING, "Error responding to " + connection.getRemoteSocketAddress(), e);
		} finally {
			if (null != writer) {
				try {
					writer.close();
				} catch (IOException e) {
					LogFactory.getLogger().log(Level.WARNING, "Error closing writer to " + connection.getRemoteSocketAddress(), e);
				}
			}
		}
	}
	
	private void sendErrorDefault() {
		
	}
	
	public abstract void process(Socket connection);		
}

package com.smilevchy.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

import com.smilevchy.http.dispatcher.RequestDispatcher;
import com.smilevchy.http.util.LogFactory;


public class JHttpServer {
	private RequestDispatcher requestDispatcher = null;

	
	public JHttpServer() {
		requestDispatcher = new RequestDispatcher();
	}
	
	public static void main(String[] args) {
		init(args);
		
		boot();
	}
	
	private static void init(String[] args) {
		if (null != args && args.length >= 1) {
			int port;
			
			try {
				port = Integer.parseInt(args[0]);
				
				if (port > 0 && port < 65535) {
					Config.port = port;
				}
			} catch (RuntimeException ex) {
				LogFactory.getLogger().warning("Get port error, so server will be started in default port 8080");
			}	
		}
		
		LogFactory.getLogger().info("Initiation is done. Server is ready to be started in port " + Config.port);
	}
	
	private static void boot() {
		(new JHttpServer()).start();
	}
	
	private void start() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(Config.port);
			
			LogFactory.getLogger().info("Server is started on port " + Config.port + " and document root \"" + Config.docRootPath + "\"");
			
			while (true) {
				try {
					Socket request = serverSocket.accept();
					requestDispatcher.dispatchRequest(request);
				} catch (IOException e) {
					LogFactory.getLogger().log(Level.WARNING, "Error accepting connection", e);
				}
			}
		} catch (IOException e) {
			LogFactory.getLogger().log(Level.SEVERE, "Failure in server's starting ......", e);
		} finally {
			if (null != serverSocket) {
				try {
					serverSocket.close();
					LogFactory.getLogger().info("Server is terminated ......");
				} catch (IOException e) {
					LogFactory.getLogger().log(Level.SEVERE, "Error closing server", e);
				}
			}
		}
	}
}

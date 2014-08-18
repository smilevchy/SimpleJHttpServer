package com.smilevchy.http.server;

import java.io.File;

public class Config {
	/** 默认监听端口 */
	public static int port = 8080;
	/** 默认根目录路径 (当前目录) */
	public static final String docRootPath = System.getProperty("user.dir");
	/** 默认根目录 */
	public static final File docRoot = new File(docRootPath);
	/** 默认首页文件名称 */
	public static String indexFilename = "index.html";
}

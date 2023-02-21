package com.jubotech.framework.util;

import org.springframework.core.env.Environment;

public class PropertyUtils {
	private static final String HTTP_PORT = "server.port";
	private static final String SOCKET_PORT = "com.jubotech.socket.port";
	private static final String WEBSOCKET_PORT = "com.jubotech.websocket.port";
	private static final String UPLOAD_URL = "com.jubotech.upload.url";
	private static final String SERVER_IP = "com.jubotech.server.ip";
	private static final String FILE_PATH = "com.jubotech.server.file.path";  
	
	// http端口
	public static Integer getHttpPort(Environment env) {
		return Integer.valueOf(env.getProperty(HTTP_PORT));
	}

	// socket端口
	public static Integer getNettySocketPort(Environment env) {
		return Integer.valueOf(env.getProperty(SOCKET_PORT));
	}

	// websocket端口
	public static Integer getNettyWebsocketPort(Environment env) {
		return Integer.valueOf(env.getProperty(WEBSOCKET_PORT));
	}

	// 全局上传网络地址
	public static String getUploadUrl(Environment env) {
		return env.getProperty(UPLOAD_URL);
	}
	
	// 服务器ip
	public static String getServerIp(Environment env) {
		return env.getProperty(SERVER_IP);
	}

	// 服务器文件存储地址
	public static String getServerFilePath(Environment env) {
		return env.getProperty(FILE_PATH);
	}
}

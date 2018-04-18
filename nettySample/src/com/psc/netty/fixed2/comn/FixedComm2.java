package com.psc.netty.fixed2.comn;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixedComm2 {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FixedComm2.class);
	public static String delimeter ="#";
	public static String defalutCharacterSet = "UTF-8";
	public static Properties prop;
	public static Properties getProperties() throws Exception{
		
		if (prop == null) {
			prop = new Properties();
			File file = new File("./config/env.properties");
			prop.load(new FileInputStream(file));	
		}
		return prop;
	}

	
	public static String getServerIp() throws Exception{	
		String ip = getProperties().getProperty("SERVER_IP");
		if (ip == null || ip.equals("")) {
			throw new Exception("server ip is not setting");
		}
		return ip;
	}
	
	public static int getPort() throws Exception{	
		String port = getProperties().getProperty("PORT");
		if (port == null || port.equals("")) {
			throw new Exception("port is not setting");
		}
		int porti = Integer.parseInt(port);
		return porti;
	}

	public static int getServerSize() throws Exception{	
		String size = getProperties().getProperty("SERVER_SIZE");
		if (size == null || size.equals("")) {
			throw new Exception("server size is not setting");
		}
		int sizei = Integer.parseInt(size);
		return sizei;
	}
	
	public static int getClientSize() throws Exception{	
		String size = getProperties().getProperty("CLIENT_SIZE");
		if (size == null || size.equals("")) {
			throw new Exception("client size is not setting");
		}
		int sizei = Integer.parseInt(size);
		return sizei;
	}
	
	public static String getCharacterSet() throws Exception{	
		String characterSet = getProperties().getProperty("CHARACTER_SET");
		if (characterSet == null || characterSet.equals("")) {
			return defalutCharacterSet;
		}
		return characterSet;
	}
	
}

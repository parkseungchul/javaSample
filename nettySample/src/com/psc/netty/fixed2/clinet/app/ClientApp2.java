package com.psc.netty.fixed2.clinet.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientApp2 {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientApp2.class);
	private byte[] data;
	
	public void setData(byte[] data) {
		this.data = data;
	}

	public void run() throws Exception{
		if(data == null) {
			return;
		}
		String result = new String(data);
		System.out.println(result);
	}
}

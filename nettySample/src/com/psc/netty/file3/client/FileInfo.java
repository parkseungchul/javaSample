package com.psc.netty.file3.client;

import java.io.Serializable;

public final class FileInfo implements Serializable{

	 public static int MAX_BUF_BYTE = 65536;
	 public static final int headerLength = 512;


	 public static int typeLen = 1;
	 public static int posLen = 1;
	 public static int totLen = 10;
	 public static int fileLen = 500;
	 
	 private final String fileName;
	 private final byte[] msg;
	 private final String msgLength;
	 private final String cnt;
	 private final String maxCnt;
	 
	 public FileInfo(String fileName, byte[] msg, String msgLength, String cnt, String maxCnt) {
		 this.fileName = fileName;
		 this.msg = msg;
		 this.msgLength = msgLength;
		 this.cnt = cnt;
		 this.maxCnt = maxCnt;
	 }

	public String getFileName() {
		return fileName;
	}

	public byte[] getMsg() {
		return msg;
	}
	
	public String getMsgLength() {
		return msgLength;
	}

	public String getCnt() {
		return cnt;
	}

	public String getMaxCnt() {
		return maxCnt;
	}
	
	
	 
	 
	 
}

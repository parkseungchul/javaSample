package com.psc.netty.file3.server;

import java.io.File;
import java.io.FileOutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class File3ServerHandler extends ChannelInboundHandlerAdapter {

	private String targetPath = "C://GIT/github.com/parkseungchul/javaSample/nettySample/files/target";
    private File file;
    
    private int size;
    
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		ByteBuf buf = (ByteBuf) msg;
		
		size = (buf.readableBytes() -512) + size;
		
		System.out.println("==================================>"+size);
		
		/**
		boolean isFirst = false;
		byte[] bytes     = (byte[])msg;
		byte[] fileName  = getByteCut(0, 512, bytes);
		byte[] content   = null;

		String sfileName = new String(fileName).trim();
		
		
		
		if(sfileName.startsWith(FileInfo.START_SEPARATOR) && sfileName.endsWith(FileInfo.END_SEPARATOR)) {
			file = new File(sfileName.substring(1,sfileName.length()-1));
			isFirst = true;
			sfileName = sfileName.substring(1, sfileName.length() - 1);
			if(file.exists()) {
				file.delete();
			}
			content = getByteCut(512, -1, bytes);
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file,true);
			
			if(isFirst) {
				fos.write(content);
			}else {
				fos.write(bytes);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if (fos != null) {
				fos.close();
			}
		}
		**/
		

//
//		String filePath = targetPath +"/"+"test.txt";
//		File file = new File(filePath);
//
//		FileOutputStream fos = null;
//		try {
//			fos = new FileOutputStream(file,true);
//			fos.write(bytes);
//			
//		}catch(Exception e) {
//			e.printStackTrace();
//		}finally {
//			if (fos != null) {
//				fos.close();
//			}
//		}
	}
	
	public byte[] getByteCut(int startIndex, int length, byte[] sourceBytes) {
		
		if (length == -1) {
			length = sourceBytes.length - startIndex;
		}	
		byte[] targetBytes = new byte[length];
		int index = 0;
		for(int i= startIndex; i<startIndex + length; i++) {
			targetBytes[index] = sourceBytes[i];
			index++;
		}
		return targetBytes;
	}
}

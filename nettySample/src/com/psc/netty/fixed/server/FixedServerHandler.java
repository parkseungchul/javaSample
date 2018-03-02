package com.psc.netty.fixed.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.psc.netty.fixed.client.FixedLengthVO;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class FixedServerHandler extends ChannelInboundHandlerAdapter {

	private String targetPath = "C://GIT/github.com/parkseungchul/javaSample/nettySample/files/target";
 
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;

		FixedLengthVO fv = new FixedLengthVO(buf.array());
		
		//fv.print();

		int     cnt        = fv.getCNT();
		int     thisLength = fv.getTHIS_LENGTH();
		String  fileName   = fv.getFILENAME();
		boolean isStart    = fv.getSTART();
		boolean isEnd      = fv.getEND();
		long    fileSize   = fv.getTOT_LENGTH();
		

		File file = new File(targetPath +"/" + fileName);
		
		if(isStart) {
			if (file.exists()) {
				file.delete();
			}
		}
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		
		try {
			fos = new FileOutputStream(file , true);
			bos = new BufferedOutputStream(fos);
			bos.write(fv.getDataB());
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			if (bos != null) {bos.close();}
			if (fos != null) {fos.close();}
		}
		
		
		if(isEnd) {
			String sendMessage;
			if ( file.length() != fileSize) {
				sendMessage = "FAIL";
			}else {
				sendMessage = "SUCCESS";
			}
			ByteBuf messageBuffer = Unpooled.buffer();
			messageBuffer.writeBytes(sendMessage.getBytes());
			ctx.writeAndFlush(messageBuffer);
		}

		
		
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
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}

package com.psc.netty.fixed.client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.Scanner;

import com.psc.netty.file3.client.FileInfo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class FixedClinetHandler extends ChannelInboundHandlerAdapter  {

	private String sourcePath = "C://GIT/github.com/parkseungchul/javaSample/nettySample/files/source/";

	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		System.out.println("select upload files");
		File fileList = new File(sourcePath);
		File files[] = fileList.listFiles();
		
		for(int i=0; i<files.length; i++) {
			System.out.println(i +" "+files[i]);
		}
		Scanner sc = new Scanner(System.in);
		int input = sc.nextInt();
		File file = files[input];


		FileInputStream in = null;
		BufferedInputStream bis = null;
		
		
	    try {
		     
	         

	    	int bufferSize = Common.MAX_SIZE - FixedLengthVO.HEADER_INT;
	    	byte[] buffer = new byte[bufferSize];
	    	
	    	
	    	long fileLength = file.length();

	    	int max = 0;
	    	if(fileLength < (long)bufferSize) {
	    		max = 1;
	    	}else {
	    		max = (int)(fileLength/bufferSize);
	    		max = max + 1;
	    	}
	    	
	    	System.out.println("[FILE LENGTH:"+fileLength+"][BUFFER SIZE: "+bufferSize+"][SEND CNT:"+max+"]");
	        
	        in = new FileInputStream(file);
	        bis = new BufferedInputStream(in);
	     
	        int len = 0;
            int cnt = 0;
	        while ((len = bis.read(buffer)) >= 0) {
	        	cnt++;
	        	ctx.writeAndFlush(new FixedLengthVO(cnt, fileLength, cnt==1?true:false, max==cnt?true:false, file.getName(), getBytes(buffer, len)));
	        }
	        System.out.println("[CNT:"+cnt+"]");
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            bis.close();
	            in.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	    }
		
		
		
		
		
//		ChannelFuture cf = null;
//		for(int i=0; i<input; i++) {
//			if(i == 0) {
//				isfirst = true;
//			}else {
//				isfirst = false;
//			}
//			if(i == input - 1) {
//				isLast = true;
//			}else {
//				isLast = false;
//			}
//			cf = ctx.writeAndFlush(new FixedLengthVO(i, 9999, isfirst, isLast, "TEST.txt", "PSC".getBytes()));
//		}

	}
	
	
	public byte[] getBytes(byte[] sourceBytes, int len) {
		//System.out.println("CREATE BYTE: "+ len);
		byte[] targetByte = new byte[len];
		for(int i=0; i< len; i++) {
			targetByte[i] = sourceBytes[i];
		}
		return targetByte;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String readMessage = ((ByteBuf)msg).toString(Charset.defaultCharset());
		System.out.println(readMessage);
		ctx.channel().close();
	}

}

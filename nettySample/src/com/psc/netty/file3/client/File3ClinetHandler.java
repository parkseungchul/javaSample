package com.psc.netty.file3.client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class File3ClinetHandler extends ChannelInboundHandlerAdapter {
	
	public byte[] getBytes(byte[] sourceBytes, int len) {
		System.out.println("create byte: "+ len);
		byte[] targetByte = new byte[len];
		for(int i=0; i< len; i++) {
			targetByte[i] = sourceBytes[i];
		}
		return targetByte;
	}
	
	
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
		String input = sc.nextLine();
		File file = files[Integer.parseInt(input)];

//		FileInputStream in = new FileInputStream(file);
//		FileRegion region = new DefaultFileRegion(in.getChannel(), 0, file.length());
//		ChannelFuture cf = ctx.writeAndFlush(region);
//		cf.addListener(new ChannelFutureListener() {
//			
//			@Override
//			public void operationComplete(ChannelFuture future) throws Exception {
//				System.out.println("finish");
//				future.channel().close();
//				
//			}
//		});
		
		
	    // -Dio.netty.threadLocalDirectBufferSize: 65536
		int MAX_BUF_BYTE = FileInfo.MAX_BUF_BYTE - FileInfo.headerLength;
	    
		int maxCnt = (int) (MAX_BUF_BYTE/file.length());
		
		
	    FileInputStream in = null;
	    BufferedInputStream bis = null;

	     
	    try {
	     
	         
	        byte[] buffer = new byte[MAX_BUF_BYTE];
	     
	        in = new FileInputStream(file);
	        bis = new BufferedInputStream(in);
	     
	        int len = 0;
            int cnt = 0;
	        while ((len = bis.read(buffer)) >= 0) {
            	cnt++;
            	ctx.writeAndFlush(new FileInfo(file.getName(), getBytes(buffer, len), String.valueOf(len), String.valueOf(cnt), String.valueOf(maxCnt)));
	        }
	     
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


		/**
		long pointer = 0;
        for (;;) {
            long len = file.length();
            if (len < pointer) {
                // file was reset
                pointer = len;
            } else if (len > pointer) {
                // Content was added
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                raf.seek(pointer);
                String line;
                int cnt = 0;
                while ((line = raf.readLine()) != null) {
                	
                	cnt = cnt + 1;
                	ctx.writeAndFlush(new FileInfo(file.getName(), line, String.valueOf(cnt)));
                }
                pointer = raf.getFilePointer();
                raf.close();
            }
        }
        **/
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	

}

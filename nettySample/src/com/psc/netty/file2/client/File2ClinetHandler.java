package com.psc.netty.file2.client;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Scanner;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class File2ClinetHandler extends ChannelInboundHandlerAdapter {
	
	
	private String targetPath = "C://GIT/github.com/parkseungchul/javaSample/nettySample/files/target/";
	private String[] fileList = new String[]{"guhala0.jpg","guhala1.jpg", "guhala2.jpg","1GB.zip"};
	private int inputNum;
	private static int cnt = 0;
	private static float totSize = 0L;
	
	private long start;
	private long end;

	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		System.out.println("select download files");
		for(int i=0; i<fileList.length; i++) {
			System.out.println( i + " " + fileList[i] );
		}
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();
		inputNum = 0;
		try 
		{
			inputNum = Integer.parseInt(input);	
		}catch(Exception e) {
			inputNum = 0;	
		}
				
		input = String.valueOf(inputNum);
		
		
		ByteBuf messageBuffer = Unpooled.buffer();
		messageBuffer.writeBytes(input.getBytes());
		ctx.writeAndFlush(messageBuffer);
		
		
		start = System.currentTimeMillis();
		


		
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		cnt = cnt + 1;
		byte[] bytes = (byte[])msg;
		
		totSize = totSize + bytes.length;
		
		float size = totSize/(1024*1024);
		
		System.out.println(cnt+"["+ bytes.length  +"] " + size + " MB");
		String filePath = targetPath + fileList[inputNum];
		
		File file = new File(filePath);
		if (cnt == 1) {
			if (file.isFile()) {
				file.delete();
			}
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file,true);
			fos.write(bytes);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if (fos != null) {
				fos.close();
			}
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
	@Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		end = System.currentTimeMillis();
		
		long runtime = ( end - start )/1000;
		float totalSize = (totSize/(1024*1024));
		System.out.println("total size     : " + totalSize + "MB");
		System.out.println("excute time    : " + runtime + " sec");
		System.out.println("speed download : " + totalSize/runtime +" MB/s");
        System.out.println("finish!");
    }
}

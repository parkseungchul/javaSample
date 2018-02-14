package com.psc.netty.file.client;

import java.io.File;
import java.io.FileOutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class FileClinetHandler extends ChannelInboundHandlerAdapter {
	

	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		System.out.println("Client Channel Active!");
		byte[] bytes = new byte[10];
		ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
		ctx.writeAndFlush(byteBuf);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		byte[] bytes = (byte[])msg;
		String filePath = "C://GIT/github.com/parkseungchul/javaSample/nettySample/files/target/guhala.jpg";
		
		File file = new File(filePath);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file,true);
			fos.write(bytes);
			
		}catch(Exception e) {
			
		}finally {
			if (fos != null) {
				fos.close();
			}
		}
		
		
		
//		FileOutputStream fos = null;
//		FileChannel out = null;
//		
//		byte[] bytes = (byte[])msg;
//		ByteBuf messageBuffer = Unpooled.buffer();
//		ByteBuffer buf = messageBuffer.nioBuffer();
//		
//		try {
//			fos = new FileOutputStream(new File(filePath), true);
//			out = fos.getChannel();
//			out.write(buf);
//			buf.flip();
//			buf.clear();
//		}catch(Exception e) {
//			e.printStackTrace();
//		}finally {
//			if (out != null) {out.close();}
//			if (fos != null) {fos.close();}
//		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}

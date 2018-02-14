package com.psc.netty.file.server;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;

public class FileServerHandler extends ChannelInboundHandlerAdapter {

	private static String FileDir = "./";
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		final String readMessage = ((ByteBuf)msg).toString(Charset.forName("UTF-8"));
		System.out.println("서버에서 받은 메시지["+readMessage+"]");
	
		File file = new File(FileDir +readMessage);

		if(!file.isFile()) {
			ctx.channel().close();
		}else {
			FileInputStream in = new FileInputStream(file);
			FileRegion region = new DefaultFileRegion(in.getChannel(), 0, file.length());
			ChannelFuture cf = ctx.writeAndFlush(region);
			cf.addListener(new ChannelFutureListener() {
				
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					// TODO Auto-generated method stub
					future.channel().close();
					
				}
			});
		}
		
	}
}

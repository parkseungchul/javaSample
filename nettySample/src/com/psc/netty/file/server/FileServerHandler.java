package com.psc.netty.file.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;

public class FileServerHandler extends ChannelInboundHandlerAdapter {


	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String filePath = "C://GIT/github.com/parkseungchul/javaSample/nettySample/files/source/guhala.jpg";
		File file = new File(filePath);
		
		FileInputStream in = new FileInputStream(file);
		FileRegion region = new DefaultFileRegion(in.getChannel(), 0, file.length());
		ChannelFuture cf = ctx.writeAndFlush(region);
		cf.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				future.channel().close();
				
			}
		});
	}
}

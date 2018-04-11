package com.psc.netty.echo.client;

import java.nio.charset.Charset;
import java.util.Scanner;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClinetHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		System.out.println("에코 서버/클라이언트 예제 (클라이언트 종료 END)");
		Scanner sc = new Scanner(System.in);
		final String sendMessage = sc.nextLine();
		
		ByteBuf messageBuffer = Unpooled.buffer();
		messageBuffer.writeBytes(sendMessage.getBytes());
		
		if (sendMessage.toUpperCase().equals("END")) {
			ctx.channel().close();
		}else {
			StringBuilder builder = new StringBuilder();
			builder.append("===>[");
			builder.append(sendMessage);
			builder.append("]");

			System.out.println(builder.toString());
			ChannelFuture cf = ctx.writeAndFlush(messageBuffer);
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String readMessage = ((ByteBuf)msg).toString(Charset.defaultCharset());
		System.out.println("["+readMessage+"]<===");
	}
	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}

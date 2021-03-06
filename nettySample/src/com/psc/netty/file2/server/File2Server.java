package com.psc.netty.file2.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

public class File2Server {

	public static void main(String[] args) throws Exception {
			EventLoopGroup bossGroup = new NioEventLoopGroup(1);
			EventLoopGroup workerGroup = new NioEventLoopGroup();
			
			try{
				ServerBootstrap b = new ServerBootstrap();
				b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();
						p.addLast(new ByteArrayDecoder());
						p.addLast(new ByteArrayEncoder());
						p.addLast(new File2ServerHandler());
						
					}
				}); 
				
				ChannelFuture f = b.bind(19999).sync();
				f.channel().closeFuture().sync();
				
				
			}
			finally{
				workerGroup.shutdownGracefully();
				bossGroup.shutdownGracefully();
			}

		}
}

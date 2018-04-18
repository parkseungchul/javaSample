package com.psc.netty.fixed2.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.psc.netty.fixed2.comn.FixedComm2;
import com.psc.netty.fixed2.comn.FixedDecoder2;
import com.psc.netty.fixed2.comn.FixedEncoder2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class FixedServer2 {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FixedServer2.class);
	
	public static void main(String[] args) throws Exception {
		EventLoopGroup bossGroup = null;
		EventLoopGroup workerGroup =null;
		LOGGER.info("SERVER START! PORT["+FixedComm2.getPort()+"]");
		
		try{
			bossGroup = new NioEventLoopGroup(1);
			workerGroup = new NioEventLoopGroup();
			
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new FixedDecoder2());
					p.addLast(new FixedEncoder2());
					p.addLast(new FixedServerHandler2());
				}
			}); 
			ChannelFuture f = b.bind(FixedComm2.getPort()).sync();
			f.channel().closeFuture().sync();
		}
		finally{
			if(workerGroup != null) {
				workerGroup.shutdownGracefully();
			}
			if(bossGroup != null) {
				bossGroup.shutdownGracefully();
			}
		}

	}
}

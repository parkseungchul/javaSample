package com.psc.netty.fixed2.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.psc.netty.fixed2.comn.FixedComm2;
import com.psc.netty.fixed2.comn.FixedDecoder2;
import com.psc.netty.fixed2.comn.FixedEncoder2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class FixedClient2 {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FixedClient2.class);
	
	public static void main(final String[] args) throws Exception {

		LOGGER.info("CLIENT START! IP["+FixedComm2.getServerIp()+"] PORT["+FixedComm2.getPort()+"]");
		
		EventLoopGroup group = null;
		
		try{
			group = new NioEventLoopGroup();
			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new FixedDecoder2());
					p.addLast(new FixedEncoder2());
					p.addLast(new FixedClinetHandler2(args));
				}
			});
			
			ChannelFuture f = b.connect(FixedComm2.getServerIp(), FixedComm2.getPort()).sync();
			f.channel().closeFuture().sync();
		}
		finally{
			if (group != null) {
				group.shutdownGracefully();
			}
			
		}

	}
}

package com.psc.netty.udp.server;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.util.Scanner;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;



public class LogEventBroadcaster {
    private final Bootstrap bootstrap;
    private final EventLoopGroup group;
    private File file;

    private final String filePath = "C://GIT/github.com/parkseungchul/javaSample/nettySample/files/source/";
    
    public LogEventBroadcaster(InetSocketAddress address) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new LogEventEncoder(address));
    }
    
    public void run() throws Exception {
        Channel ch = bootstrap.bind(0).syncUninterruptibly().channel();
        System.out.println("LogEventBroadcaster running");
        
        while(true) {

	        File fileList = new File(filePath);
	        File []files = fileList.listFiles();
	        for(int i=0; i<files.length;i++) {
	        	System.out.println(i +" "+files[i].getName());
	        }
	        System.out.println("choose transpfer file number ");
	        Scanner sc = new Scanner(System.in);
	        int choose = sc.nextInt();
	        file = files[choose];
	        
	        
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
	                    //ch.writeAndFlush(new LogEvent(null, -1, file.getAbsolutePath(), line, String.valueOf(cnt)));
	                	ch.writeAndFlush(new LogEvent(null, -1, file.getName(), line, String.valueOf(cnt)));
	                }
	                pointer = raf.getFilePointer();
	                raf.close();
	            }
	            
	            
	            if ( len == pointer) {
	            	System.out.println("File transfer complete");
	            	break;
	            }
	            
	            try {
	                Thread.sleep(1000);
	            } catch (InterruptedException e) {
	                Thread.interrupted();
	                break;
	            }
	        }
	        
	        
            
            
        }
    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {

    	String input1 = "19999";

        LogEventBroadcaster broadcaster = new LogEventBroadcaster(new InetSocketAddress("255.255.255.255",Integer.parseInt(input1)));

        
        try {
            broadcaster.run();
        } finally {
            broadcaster.stop();
        }
    }
}

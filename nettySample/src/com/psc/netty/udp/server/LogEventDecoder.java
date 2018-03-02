package com.psc.netty.udp.server;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

/**
 * @author <a href="mailto:norman.maurer@googlemail.com">Norman Maurer</a>
 */
public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {
	
	private String filePath = "C://GIT/github.com/parkseungchul/javaSample/nettySample/files/target/";
	
	
    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket datagramPacket, List<Object> out) throws Exception {
        ByteBuf data = datagramPacket.content();
        int i = data.indexOf(0, data.readableBytes(), LogEvent.SEPARATOR);
        String filename = data.slice(0, i).toString(CharsetUtil.UTF_8);

        int j = data.indexOf(i+1, data.readableBytes(), LogEvent.SEPARATOR);
        String cnt =  data.slice(i+1, j-i-1).toString(CharsetUtil.UTF_8);
        
        String logMsg =  data.slice(j + 1,  data.readableBytes()).toString(CharsetUtil.UTF_8);
        
        
        ByteBuf data2 = data.slice(j + 1,  data.readableBytes());
        byte[] bytes = data2.array();
        
        System.out.println("==>"+filename +"|###|"+cnt +"<==");
        

		File file = new File(filePath +"/"+filename);
		if (cnt.equals("1")) {
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
        
        
        
        LogEvent event = new LogEvent(datagramPacket.recipient(), System.currentTimeMillis(), filename,logMsg);
        out.add(event);
        
        
        
        
        
    }
}

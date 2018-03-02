package com.psc.netty.file3.server;

import java.nio.charset.Charset;
import java.util.List;

import com.psc.netty.file3.client.FileInfo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class ByteArrayDecoder2  extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
   
    	//System.out.println("########################################################"+msg.readableBytes());
    	if (msg.readableBytes() < FileInfo.headerLength) {
            return; 
        }

    	ByteBuf buf = Unpooled.buffer(10);
    	buf.writeByte(msg.getByte(2));
    	buf.writeByte(msg.getByte(3));
    	buf.writeByte(msg.getByte(4));
    	buf.writeByte(msg.getByte(5));
    	buf.writeByte(msg.getByte(6));
    	buf.writeByte(msg.getByte(7));
    	buf.writeByte(msg.getByte(8));
    	buf.writeByte(msg.getByte(9));
    	buf.writeByte(msg.getByte(10));
    	buf.writeByte(msg.getByte(11));

    	String lenStr = buf.toString(Charset.defaultCharset());
    	lenStr = lenStr.trim();

    	int lenInt = Integer.parseInt(lenStr);
    	size = size + lenInt;


    	if (msg.readableBytes() < lenInt) {
    		return;
    	}
    	System.out.println(lenInt+"@@@===>"+size+"<===");
        out.add(msg.readBytes(lenInt));
  

        
 
    }
    
    
    int size;
    
	public byte[] getByteCut(int startIndex, int length, byte[] sourceBytes) {
		
		if (length == -1) {
			length = sourceBytes.length - startIndex;
		}	
		byte[] targetBytes = new byte[length];
		int index = 0;
		for(int i= startIndex; i<startIndex + length; i++) {
			targetBytes[index] = sourceBytes[i];
			index++;
		}
		return targetBytes;
	}
}
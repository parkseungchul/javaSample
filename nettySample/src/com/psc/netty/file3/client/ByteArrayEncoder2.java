package com.psc.netty.file3.client;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

@Sharable
public class ByteArrayEncoder2 extends MessageToMessageEncoder<FileInfo> {
    @Override
    protected void encode(ChannelHandlerContext ctx, FileInfo fileInfo, List<Object> out) throws Exception {
        
    	String type = "";
    	String postion ="";
    	String msgLength = fileInfo.getMsgLength();
    	String fileName = fileInfo.getFileName();
    	
    	type = "F";
    	String cnt = fileInfo.getCnt();
 
    	if(cnt.equals("1")) {
    		postion = "1";
    	}else {
    		postion = "0";
    	}
    	
    	int totalLen = Integer.parseInt(msgLength) + FileInfo.headerLength;
    	String totalLength = String.valueOf(totalLen);
    	totalLength = totalLength + addBlank(FileInfo.totLen - totalLength.length());
    	
    	
    	
    	fileName = fileName + addBlank(FileInfo.fileLen - fileName.getBytes().length);

    	
    	String header = type + postion + totalLength + fileName; //512
    	byte [] headerBytes = header.getBytes();
    			
    	//int totLen = headerBytes.length + fileInfo.getMsg().length;
    	System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+totalLen);
    	ByteBuf by = Unpooled.directBuffer(totalLen);
    	by.writeBytes(headerBytes);
    	by.writeBytes(fileInfo.getMsg());
        out.add(by);
    }
    
    
    public String addBlank(int addCnt) {
    	StringBuilder sb = new StringBuilder();
    	for(int i=0; i<addCnt; i++) {
    		sb.append(" ");
    	}
    	return sb.toString();
    }
    
}
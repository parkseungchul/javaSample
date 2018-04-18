package com.psc.netty.fixed.server;

import java.nio.charset.Charset;
import java.util.List;

import com.psc.netty.fixed.client.FixedLengthVO;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class FixedDecoder extends ByteToMessageDecoder {

	@Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        System.out.println(">" + msg.readableBytes() +"<-" + FixedLengthVO.HEADER_INT);
		if (msg.readableBytes() < FixedLengthVO.HEADER_INT) {
            return; 
        }

		String thisLengthS = msg.toString(Charset.defaultCharset()).substring(0, FixedLengthVO.THIS_LENGTH_INT);

		int cntInt = Integer.parseInt(thisLengthS);

		if (msg.readableBytes() < cntInt) {
            return; 
        }
		System.out.println(msg.readableBytes()+" "+cntInt);
        out.add(msg.readBytes(cntInt));
	}

}

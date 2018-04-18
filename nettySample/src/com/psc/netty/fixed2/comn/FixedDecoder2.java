package com.psc.netty.fixed2.comn;

import java.nio.charset.Charset;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class FixedDecoder2 extends ByteToMessageDecoder {

	private static final Logger LOGGER = LoggerFactory.getLogger(FixedDecoder2.class);
	
	@Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        
		// Fixed 정보를 정상적으로 읽기 위해서는 적어도 header 길이 이상은 가지고 있어야 하므로 적을 경우 다시 리턴하여 ByteBuf에 더 적재
		LOGGER.debug("decoding bytebuf readable length["+msg.readableBytes()+"] vo header length["+FixedLengthVO2.HEADER_INT+"]");
       
		if (msg.readableBytes() < FixedLengthVO2.HEADER_INT) {
            return; 
        }

		// header 정보를 확보한 뒤에 전체 길이 정보를 가져와서 byteBuf에서 맞게 잘라서 넣음
		String thisLengthS = msg.toString(Charset.defaultCharset()).substring(0, FixedLengthVO2.LENGTH_INT);

		int thisLength = Integer.parseInt(thisLengthS);

		if (msg.readableBytes() < thisLength) {
            return; 
        }
        out.add(msg.readBytes(thisLength));
	}

}

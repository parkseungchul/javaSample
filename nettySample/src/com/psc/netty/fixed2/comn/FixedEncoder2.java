package com.psc.netty.fixed2.comn;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class FixedEncoder2 extends MessageToMessageEncoder<FixedLengthVO2>{

	private static final Logger LOGGER = LoggerFactory.getLogger(FixedEncoder2.class);
	
	@Override
	protected void encode(ChannelHandlerContext ctx, FixedLengthVO2 fixedLengthVO2, List<Object> out) throws Exception {

		LOGGER.debug(
		"Encoding CNT["+ fixedLengthVO2.getCnt()+"] "
			    + "START["+fixedLengthVO2.getStart()+"] "
			    + "END["+fixedLengthVO2.getEnd()+"]"
	    );
		
		// This is the encoding area
		ByteBuf by = Unpooled.directBuffer(fixedLengthVO2.getLength());	
		by.writeBytes(fixedLengthVO2.getHeadByte());
		by.writeBytes(fixedLengthVO2.getDataByte());
        out.add(by);
	}
}

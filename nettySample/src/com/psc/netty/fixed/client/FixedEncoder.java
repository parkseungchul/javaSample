package com.psc.netty.fixed.client;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class FixedEncoder extends MessageToMessageEncoder<FixedLengthVO>{

	@Override
	protected void encode(ChannelHandlerContext ctx, FixedLengthVO fixedLengthVO, List<Object> out) throws Exception {

		ByteBuf by = Unpooled.directBuffer(fixedLengthVO.getTHIS_LENGTH());	
		by.writeBytes(fixedLengthVO.getHeadB());
		by.writeBytes(fixedLengthVO.getDataB());
        out.add(by);
		
	}
}

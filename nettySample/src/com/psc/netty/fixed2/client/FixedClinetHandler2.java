package com.psc.netty.fixed2.client;

import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.psc.netty.fixed2.clinet.app.ClientApp2;
import com.psc.netty.fixed2.comn.FixedComm2;
import com.psc.netty.fixed2.comn.FixedLengthVO2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class FixedClinetHandler2 extends ChannelInboundHandlerAdapter  {

	private static final Logger LOGGER = LoggerFactory.getLogger(FixedClinetHandler2.class);
	
	public HashMap<String, byte[]> hm;
	public static String[] args;
	public ClientApp2 ca2;
	public FixedClinetHandler2(String[] args) {
		this.args = args;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		for(String str: args) {
			sb.append(new String(str.getBytes(), FixedComm2.getCharacterSet()) + FixedComm2.delimeter);
		}

		LOGGER.info("SEND DATA[" +sb.toString()+"]");
	
		makeDate(ctx, sb.toString());
		
		LOGGER.info("SEND COMPLETE!");
	}
	
	
	public void makeDate(ChannelHandlerContext ctx, String result) throws Exception {
		
		String uuid = UUID.randomUUID().toString();
		hm = new HashMap<String, byte[]>();
		
		byte[] resultByte = result.getBytes();
		int bufferSize = FixedComm2.getClientSize()- FixedLengthVO2.HEADER_INT;
		int totLength = resultByte.length;

    	int max = 0;
    	int lastBufferSize = 0;
    	if(totLength < bufferSize) {
    		max = 1;
    		lastBufferSize = totLength;
    	}else {
    		max = (int)(totLength/bufferSize);
    		lastBufferSize = (totLength%bufferSize);
    		max = max + 1;
    	}
    	
    	int curCnt = 1;
    	int curIndex = 0;
    	while(true) {
    		// 마지막 버퍼일 경우
	    	if(curCnt == max) {
	    		byte[] targetByte = new byte[lastBufferSize];
	    		copyBytes(resultByte, targetByte, curIndex, curIndex + lastBufferSize);
	    		FixedLengthVO2 fv2 = new FixedLengthVO2(curCnt, totLength, curCnt==1?true:false, max==curCnt?true:false, uuid, targetByte);
	    		ctx.writeAndFlush(fv2);
	    		LOGGER.debug(fv2.print().toString());
	    		break;
	    	// 중간 버퍼일경우
	    	}else {
	    		byte[] targetByte = new byte[bufferSize];
	    		copyBytes(resultByte, targetByte, curIndex, curIndex + bufferSize);
	    		
	    		FixedLengthVO2 fv2 =  new FixedLengthVO2(curCnt, totLength, curCnt==1?true:false, max==curCnt?true:false, uuid, targetByte);
	    		ctx.writeAndFlush(fv2);
	    		
	    		LOGGER.debug(fv2.print().toString());
	    	}
	    	curIndex =  curIndex + bufferSize;
	    	curCnt   = curCnt + 1;
    	}
	}
	
	public byte[] copyBytes(byte[] source, byte[] target, int sourceStartIndex, int sourceEndIndex) {
		int cnt = 0;
		for(int i = sourceStartIndex; i<sourceEndIndex; i++) {
			target[cnt] = source[i];
			cnt++;
		}
		return target;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		ByteBuf buf = (ByteBuf) msg;
		FixedLengthVO2 fv2 = new FixedLengthVO2(buf.array());
		
		LOGGER.debug("RECEIVE DATA UUID[" + fv2.getFiller()+"]");
		
		
		setHm(fv2.getFiller(),fv2.getDataByte());
		if(fv2.getEnd()) {
			
			byte[] data = getHm(fv2.getFiller());
			ClientApp2 ca2 = new ClientApp2();
			ca2.setData(data);
			this.ca2 = ca2;
			if(hm != null) {
				hm.remove(fv2.getFiller());
			}
			ctx.close();
		}
	}
	
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	ca2.run();
    }
	
	public void setHm(String key, byte[] value) {
		byte[] result = hm.get(key);
		
		if (result == null) {
			hm.put(key, value);
		}else {
			/*
			 * result 는 원래 있던 값
			 * value 는 현재 값
			 * target = result + vale
			 */
			byte[] target = new byte[result.length + value.length];
			System.arraycopy(result, 0, target, 0            , result.length);
			System.arraycopy(value , 0, target, result.length, value.length);
			hm.put(key, target);
		}
	}
	
	public byte[] getHm(String key) {
		byte[] result = hm.get(key);
		if(result == null) {
			return "값이 없습니다!".getBytes();
		}else {
			return result;
		}
	}
}

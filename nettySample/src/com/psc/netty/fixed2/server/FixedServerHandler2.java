package com.psc.netty.fixed2.server;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.psc.netty.fixed2.comn.FixedComm2;
import com.psc.netty.fixed2.comn.FixedLengthVO2;
import com.psc.netty.fixed2.server.app.ServerApp2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class FixedServerHandler2 extends ChannelInboundHandlerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(FixedServerHandler2.class);
	private HashMap<String, byte[]> hm;
	
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	
    	LOGGER.info("CHANNEL ACTIVE!");
    	if (hm == null) {
    		hm = new HashMap<String, byte[]>();
    	}
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
			return "not found data!".getBytes();
		}else {
			return result;
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		ByteBuf buf = (ByteBuf) msg;
		FixedLengthVO2 fv2 = new FixedLengthVO2(buf.array());
		
//		int     length     = fv2.getLength();
//		int     cnt        = fv2.getCnt();
//      int     totLength  = fv2.getTotLength();
//		boolean isStart    = fv2.getStrat();
//		boolean isEnd      = fv2.getEnd();
//		String  filler     = fv2.getFiller();
//		byte    dataByte[] = fv2.getDataByte();
//		String data = new String(dataByte);
		
		LOGGER.debug(fv2.print().toString());
		String uuid = fv2.getFiller();
		setHm(uuid, fv2.getDataByte());
		
		LOGGER.debug("===============================>"+uuid);
		// 마지막까지 데이터를 읽고 나서 실행합니다.
		if(fv2.getEnd()) {
	    	LOGGER.info("READ COMPLETE! uuid:" + uuid);
			makeDate(ctx, fv2);	
		}else {
			return;
		}
	}
	
	
	public void makeDate(ChannelHandlerContext ctx, FixedLengthVO2 fv2) throws Exception{
		
		byte[] data = getHm(fv2.getFiller());
		String dataS = new String(data);
		LOGGER.debug("★★★★★★★★★★");
		LOGGER.debug(dataS);
		LOGGER.debug("★★★★★★★★★★");
		
		// TODO 업무 로직  START
		// 만약에 byte[]로 리턴할 수 있다면 하단의 resultByte 에 맵핑한다면 굳
		ServerApp2 fsApp2 = new ServerApp2();
		String result = fsApp2.run(dataS);
		// TODO 업무 로직  END
		
		// 받은 데이터를 다 사용했어 반드시 삭제해야 합니다.
		String uuid = fv2.getFiller();
		hm.remove(uuid);
		
		byte[] resultByte = result.getBytes();
		int bufferSize = FixedComm2.getServerSize() - FixedLengthVO2.HEADER_INT;
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
	    		ctx.writeAndFlush(new FixedLengthVO2(curCnt, totLength, curCnt==1?true:false, max==curCnt?true:false, uuid, targetByte));
	    		break;
	    	// 중간 버퍼일경우
	    	}else {
	    		byte[] targetByte = new byte[bufferSize];
	    		copyBytes(resultByte, targetByte, curIndex, curIndex + bufferSize);
	    		ctx.writeAndFlush(new FixedLengthVO2(curCnt, totLength, curCnt==1?true:false, max==curCnt?true:false, uuid, targetByte));
	    	}
	    	curIndex =  curIndex + bufferSize;
	    	curCnt   = curCnt + 1;
    	}
    	LOGGER.info("WRITE COMPLETE! uuid:" + uuid);
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
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}

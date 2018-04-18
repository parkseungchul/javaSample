package com.psc.netty.fixed2.server.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.psc.netty.fixed2.comn.FixedComm2;
import com.psc.netty.fixed2.server.FixedServerHandler2;

public class ServerApp2 {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerApp2.class);
	
	public String run(String para) throws Exception{
		
		LOGGER.info("input para characterSet Before:"+ para );
		para = new String (para.getBytes(), FixedComm2.getCharacterSet());
		LOGGER.info("input para characterSet After:"+ para );
		
		StringBuilder sb = new StringBuilder();
		
		// TODO SERVER APP START

		String paras[] = para.split(FixedComm2.delimeter);
		String type = paras[0];
		String addStr = "";
		int cnt = Integer.parseInt(paras[1]);
		
		if(type.equals("A")) {
			addStr = "ABCDE";
		}else if(type.equals("a")) {
			addStr = "abcde";
		}else if(type.equals("1")) {
			addStr = "12345";
		}else if(type.equals("가")) {
			addStr = "가나다라마";
		}else {
			addStr = "(<@>)";
		}
		
		for(int i = 0; i< cnt; i++) {
			sb.append(addStr);
			if(i%100 == 0) {
				sb.append("\n");
			}
		}
		
		// TODO SERVER APP END
		return sb.toString();
	}
	

}

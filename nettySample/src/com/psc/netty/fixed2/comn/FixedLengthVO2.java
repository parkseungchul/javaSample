package com.psc.netty.fixed2.comn;

import java.io.Serializable;

public class FixedLengthVO2 implements Serializable {

	private static final long serialVersionUID = 7725638519638532357L;
	
	private static String LENGTH;        // Current VO Length 
	private static String CNT;           // Count Number
	private static String TOT_LENGTH;    // Total Send Data Length
	private static String START;         // is Start VO? true 0 false 1
	private static String END;           // is End VO? true 0 false 1
	private static String FILLER;        // extra field
	private byte[] DATA;     
	
	public static int LENGTH_INT = 8;    
	public static int CNT_INT         = 8;    
	public static int TOT_LENGTH_INT  = 12;   
	public static int START_INT       = 1;
	public static int END_INT         = 1;
	public static int FILLER_INT      = 128;
	public static int HEADER_INT      = LENGTH_INT + CNT_INT + TOT_LENGTH_INT +START_INT + END_INT + FILLER_INT;
	public int DATA_INT;
	
	public FixedLengthVO2(byte [] fixed) {
		int INDEX = 0;
		LENGTH = new String(getBytes(fixed, INDEX, LENGTH_INT));
		INDEX       = INDEX + LENGTH_INT;
		CNT         = new String(getBytes(fixed, INDEX, CNT_INT));
		INDEX       = INDEX + CNT_INT;
		TOT_LENGTH  = new String(getBytes(fixed, INDEX, TOT_LENGTH_INT));
		INDEX       = INDEX + TOT_LENGTH_INT;
		START       = new String(getBytes(fixed, INDEX, START_INT));
		INDEX       = INDEX + START_INT;
		END         = new String(getBytes(fixed, INDEX, END_INT));
		INDEX       = INDEX + END_INT;
		FILLER      = new String(getBytes(fixed, INDEX, FILLER_INT));
		INDEX       = INDEX + FILLER_INT;  
		DATA        = getBytes(fixed, INDEX, -1);
		
	}
	
	
	public FixedLengthVO2(int cnt, int totSize, boolean isStart, boolean isEnd, String filler, byte[] data) {
		if (data == null) {
			DATA_INT = 0;
		}else {
			DATA_INT = data.length;
		}

		int thisLen = HEADER_INT + DATA_INT;
		LENGTH         = String.format("%0"+String.valueOf(LENGTH_INT)+"d", thisLen);
		CNT            = String.format("%0"+String.valueOf(CNT_INT)+"d"        , cnt);
		TOT_LENGTH     = String.format("%0"+String.valueOf(TOT_LENGTH_INT)+"d" , totSize);
		START          = isStart?"1":"0";
		END            = isEnd?"1":"0";
		int formatSize = FILLER_INT - (filler.getBytes().length -filler.length());
		FILLER         = String.format("%"+formatSize+"s", filler);
		DATA           = data;
	}
	

	public StringBuilder print() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼\n");
		sb.append("THIS_LENGTH : [" + LENGTH.trim()     +"]\n");
		sb.append("CNT         : [" + CNT.trim()        +"]\n");
		sb.append("TOT_LENGTH  : [" + TOT_LENGTH.trim() +"]\n");
		sb.append("START       : [" + getStart()        +"]\n");
		sb.append("END         : [" + getEnd()          +"]\n");
		sb.append("FILLER      : [" + FILLER.trim()     +"]\n");
		sb.append("DATA        : [" + getData()         +"]\n");
		sb.append("▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲\n");
		return sb;
	}
	
	public byte[] getBytes(byte[] fixed, int startIndex, int size) {
		byte[] target;
		
		if (size == -1) {
			size = fixed.length - startIndex;
		}

		target = new byte[size];
		int cnt = 0;
		for(int i=startIndex; i<startIndex +  size; i++) {
			target[cnt] = fixed[i];
			cnt++;
		}

		return target;
	}
	
	public byte[] getHeadByte() {
		return (LENGTH + CNT + TOT_LENGTH + START + END + FILLER).getBytes();
	}
	
	public byte[] getDataByte() {
		return DATA;
	}
	
	public int getLength() {
		return Integer.parseInt(LENGTH);
	}
	
	public int getCnt() {
		return Integer.parseInt(CNT);
	}
	
	public int getTotLength() {
		return Integer.parseInt(TOT_LENGTH);
	}
	
	public boolean getStart() {
		return START.equals("1")?true:false;
	}
	
	public boolean getEnd() {
		return END.equals("1")?true:false;
	}
	
	public String getFiller() {
		return FILLER.trim();
	}
	
	public String getData() {
		return new String(DATA);
	}
}

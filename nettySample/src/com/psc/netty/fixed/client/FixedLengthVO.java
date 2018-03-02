package com.psc.netty.fixed.client;

import java.io.Serializable;

public class FixedLengthVO implements Serializable{

	private static final long serialVersionUID = 4899735984175427256L;
	
	private static String THIS_LENGTH;   // 
	private static String CNT;           //
	private static String TOT_LENGTH;    //
	private static String START;         // true 0 false 1
	private static String END;           // true 0 false 1
	private static String FILENAME;      // TEST.txt
	private byte[] DATA;     
	
	public static int THIS_LENGTH_INT = 8;    
	public static int CNT_INT         = 8;    
	public static int TOT_LENGTH_INT  = 12;   
	public static int START_INT       = 1;
	public static int END_INT         = 1;
	public static int FILENAME_INT    = 128;
	public static int HEADER_INT      = THIS_LENGTH_INT + CNT_INT + TOT_LENGTH_INT +START_INT + END_INT + FILENAME_INT;
	public int DATA_INT;


	public FixedLengthVO(int cnt, long fileSize, boolean isStart, boolean isEnd, String fileName, byte[] data) {
		if (data == null) {
			DATA_INT = 0;
		}else {
			DATA_INT = data.length;
		}


		int thisLen = HEADER_INT + DATA_INT;
		
		
		THIS_LENGTH = String.format("%0"+String.valueOf(THIS_LENGTH_INT)+"d", thisLen);
		CNT         = String.format("%0"+String.valueOf(CNT_INT)+"d"        , cnt);
		TOT_LENGTH  = String.format("%0"+String.valueOf(TOT_LENGTH_INT)+"d" , fileSize);
		START       = isStart?"1":"0";
		END         = isEnd?"1":"0";
		int formatSize = FILENAME_INT - (fileName.getBytes().length -fileName.length());
		FILENAME    = String.format("%"+formatSize+"s", fileName);
		DATA        = data;
		
		if(isEnd || isStart) {
			System.out.println("end cnt is:" +cnt +"thisLen:" + thisLen);
		}
	}
	
	
	public FixedLengthVO(byte [] fixed) {
		int INDEX = 0;
		THIS_LENGTH = new String(getBytes(fixed, INDEX, THIS_LENGTH_INT));
		INDEX       = INDEX + THIS_LENGTH_INT;
		CNT         = new String(getBytes(fixed, INDEX, CNT_INT));
		INDEX       = INDEX + CNT_INT;
		TOT_LENGTH  = new String(getBytes(fixed, INDEX, TOT_LENGTH_INT));
		INDEX       = INDEX + TOT_LENGTH_INT;
		START       = new String(getBytes(fixed, INDEX, START_INT));
		INDEX       = INDEX + START_INT;
		END         = new String(getBytes(fixed, INDEX, END_INT));
		INDEX       = INDEX + END_INT;
		FILENAME    = new String(getBytes(fixed, INDEX, FILENAME_INT));
		INDEX       = INDEX + FILENAME_INT;  
		DATA        = getBytes(fixed, INDEX, -1);
		
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
	
	public int getTHIS_LENGTH() {
		return Integer.parseInt(THIS_LENGTH);
	}
	
	public int getCNT() {
		return Integer.parseInt(CNT);
	}
	
	public long getTOT_LENGTH() {
		return Long.parseLong(TOT_LENGTH);
	}
	
	public boolean getSTART() {
		return START.equals("1")?true:false;
	}
	
	public boolean getEND() {
		return END.equals("1")?true:false;
	}
	
	public String getFILENAME() {
		return FILENAME.trim();
	}
	
	public byte[] getHeadB() {
		return (THIS_LENGTH + CNT + TOT_LENGTH + START + END + FILENAME).getBytes();
	}
	
	
	public byte[] getDataB() {
		return DATA;
	}
	
	public String getHeadS() {
		return THIS_LENGTH + CNT + TOT_LENGTH + START + END + FILENAME;
	}
	
	public String getDataS() {
		return new String(DATA);
	}
	
	public void print() {
		System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
		System.out.println("THIS_LENGTH:" + THIS_LENGTH.trim());
		System.out.println("CNT        :" + CNT.trim());
		System.out.println("TOT_LENGTH :" + TOT_LENGTH.trim());
		System.out.println("START      :" + START.trim());
		System.out.println("END        :" + END.trim());
		System.out.println("FILENAME   :" + FILENAME.trim());
		System.out.println("▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲");
		
	}
	
	public static void main(String[] args) {
		
		FixedLengthVO fv = new FixedLengthVO(1, 1, true, true, "TEST.txt", new byte[10]);
		String a = fv.getHeadS()+ fv.getDataS();
		System.out.println("["+a+"]");
		
		FixedLengthVO fv2 =  new FixedLengthVO(a.getBytes());
		String a2 = fv.getHeadS()+ fv.getDataS();
		System.out.println("["+a2+"]");
	}
	
}

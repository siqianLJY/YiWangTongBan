package com.synjones.idcard;

public class IDCardReaderRetInfo {
	public int errCode;//-10 error
	public IDCard card;
	public byte sw1;
	public byte sw2;
	public byte sw3;
	public static final int ERROR_RECV_FINDCARD=-1;
	public static final int ERROR_RECV_READBASE=-2;
	public static final int ERROR_OK=0;
	public static final int ERROR_SAME_CARD=1;
	public static final int ERROR_READ_CARD=2;
	public static final int ERROR_UNKNOWN=-10;
	public IDCardReaderRetInfo(int errCode){//errcode=-1findcard err; errcode=-2 read base msg recv err
		this.errCode=errCode;
	}
	public IDCardReaderRetInfo(int errCode,IDCard card){//errcode=0 ok
		this.errCode=errCode;
		this.card=card;
	}
	public IDCardReaderRetInfo(int errCode,byte sw[]){//errcode=1 find recv ok,but idcard not get;errcode=2 read recv OK, but idcard not get
		this.errCode=errCode;
		this.sw1=sw[0];
		this.sw2=sw[1];
		this.sw3=sw[2];
	}
	public boolean isRecvError(){
		return errCode<0;
	}
}

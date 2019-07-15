package com.synjones.multireaderlib;

import com.synjones.cardutil.util;
import com.synjones.multireaderlib.IOProtocol.ProtocolType;

import android.util.Log;

public class TestInstance implements DataTransInterface{

	public static final String TAG="TestInstance";
	@Override
	public void sendData(byte[] data, int datalen) {
		// TODO Auto-generated method stub
		plog(util.bytesToHexString(data, datalen));
	}
	private  int recvIndex=0;
	byte buf[];
	@Override
	public int recvData(byte[] recvbuf, int offset) {
		//二代证返回
		byte sw1[]={0,0,(byte) 0x90};
		byte sw2[]={0,0,(byte) 0x80};
		byte errdata[]={1,2,3,4,5,6,7,8,9,0};
		byte res1[]=packRecvData(ProtocolType.IDCARD, sw1, 0, 0, errdata);
		
		if(recvIndex==0){
			buf=new byte[errdata.length+res1.length];
			System.arraycopy(errdata, 0, buf, 0, errdata.length);
			System.arraycopy(res1, 0, buf, errdata.length, res1.length);
		}
		int onceLen=(int) (Math.random()*(buf.length));
		int copyLen=(buf.length-recvIndex)>onceLen?onceLen:(buf.length-recvIndex);
		if(copyLen<=0) return -1;
		System.arraycopy(buf, recvIndex, recvbuf, offset, copyLen);
		recvIndex+=copyLen;
		plog("recv len="+copyLen);
/*		int copyPos=0;		
		System.arraycopy(data, 0, recvbuf, offset, data.length);
		copyPos+=data.length;
		System.arraycopy(res1, 0, recvbuf, offset+data.length, res1.length-1);
		copyPos+=res1.length;*/
		

		// TODO Auto-generated method stub
		return copyLen;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		recvIndex=0;
	}
	
	//返回数据格式 head+2len+3sw+data+1xor
	private byte[] packRecvData(IOProtocol.ProtocolType type,byte sw[],int cmd, int para,byte data[]){
		int validLen=IOProtocol.SW_OCCUPYBYTES+IOProtocol.XOR_OCCUPYBYTES;
		byte Head[];
		if(type==ProtocolType.IDCARD){
			Head=IOProtocol.IDCARD_HEAD;
		}	
		else{
			Head=IOProtocol.COMMON_HEAD;
			validLen+=IOProtocol.CMD_OCCUPYBYTES+IOProtocol.PARA_OCCUPYBYTES;
		}
		
		if(data!=null)
			validLen+=data.length;
		byte packedBuf[]=new byte[validLen+Head.length+IOProtocol.VALIDLEN_OCCUPYBYTES];
		int copyPos=0;
		System.arraycopy(Head, 0, packedBuf, copyPos, Head.length);
		copyPos+= Head.length;
		packedBuf[copyPos++]=(byte) (validLen>>8);
		packedBuf[copyPos++]=(byte) validLen;
		System.arraycopy(sw, 0, packedBuf, copyPos, IOProtocol.SW_OCCUPYBYTES);
		copyPos+=IOProtocol.SW_OCCUPYBYTES;
		if(type==ProtocolType.COMMON){
			packedBuf[copyPos++]=(byte) cmd;
			packedBuf[copyPos++]=(byte) para;			
		}	
		if(data!=null){
			System.arraycopy(data, 0, packedBuf, copyPos, data.length);
			copyPos+=data.length;
		}
		packedBuf[copyPos]=IOProtocol.xorchk(packedBuf, Head.length, packedBuf.length-Head.length-1);
		return packedBuf;

	}
	
	public void plog(String msg){
		Log.e(TAG, msg);
	}

}

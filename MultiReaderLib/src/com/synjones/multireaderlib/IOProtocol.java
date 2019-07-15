package com.synjones.multireaderlib;

import java.io.IOException;
import java.util.ArrayList;

import android.os.Environment;
import android.util.Log;

import com.synjones.cardutil.util;

/**
 * 使用多合一读卡器1.9版本协议
 * 读卡器底层IO协议
* @ClassName IOProtocol 
* @author zhaodianbo@Synjones
* @date 2014-10-20 上午9:45:37 
*
 */
public class IOProtocol {
	 //数据传输接口
	public DataTransInterface dti;
	//二代证操作头
	public static final byte IDCARD_HEAD[]={(byte) 0xAA,(byte) 0xAA,(byte) 0xAA,(byte) 0x96,0x69};
	//其他操作头
	public static final byte COMMON_HEAD[]={(byte) 0xAA,(byte) 0xAA,(byte) 0xAA,0x69,(byte) 0x96};
	//有效数据长度所占字节数
	public static final int VALIDLEN_OCCUPYBYTES=2;
	//xor校验所占字节数
	public static final int XOR_OCCUPYBYTES=1;
	//返回状态占字节数
	public static final int SW_OCCUPYBYTES=3;
	public static final int CMD_OCCUPYBYTES=1;
	public static final int PARA_OCCUPYBYTES=1;

	public byte findHeadBuf[]=new byte[1024];
	public byte recvBuf[]=new byte[8096];
	public byte emptyBuf[]=new byte[8096];
	/**
	 * 读卡器协议基本分为两类  二代证的和非二代证的
	* @ClassName ProtocolType 
	* @author zhaodianbo@Synjones
	* @date 2014-10-21 下午1:15:50 
	*
	 */
	public enum ProtocolType{IDCARD,COMMON};
	
	public IOProtocol(DataTransInterface ioInterface){
		this.dti=ioInterface;
	}
	/**
	 * 执行读二代证命令
	* @Title doIDcardCmd 
	* @param cmd 命令值
	* @param para 参数值
	* @param data 命令数据
	* @param timeout 命令返回超时时间
	* @return
	 */
	public CmdResponse doIDcardCmd(int cmd,int para,byte data[],long timeout){
		send(packCmd(ProtocolType.IDCARD, cmd, para, data));
		byte recvBuf[]=recv(ProtocolType.IDCARD, timeout);

		return Parse(recvBuf);
	}
	

	public void sendIDcardCmd(int cmd,int para,byte data[],long timeout){
		send(packCmd(ProtocolType.IDCARD, cmd, para, data));
	}
	
	public ArrayList<CmdResponse> recvIDCardCmd(byte datain[]){
		return recv(datain);
	}
	/**
	 * 
	* @Title doCmd 
	* @param cmd 命令值
	* @param para 参数值
	* @param data 命令数据
	* @param timeout 命令返回超时时间
	* @return
	 */
	public CmdResponse doCmd(int cmd,int para,byte data[],long timeout){
		send(packCmd(ProtocolType.COMMON, cmd, para, data));
		if(timeout==0) return null;
		byte recvBuf[]=recv(ProtocolType.COMMON, timeout);
		return Parse(recvBuf);
	}
	
	public CmdResponse recvResponse(ProtocolType type,long timeout){
		if(timeout==0) return null;
		byte recvBuf[]=recv(type, timeout);
		return Parse(recvBuf);
	}
	
	private void send(byte data[]){
		dti.clear();
		dti.sendData(data, data.length);
	}
	//唤醒单片机
	public void sendFF(){
		byte data[]={(byte) 0xff,(byte) 0xff};
		dti.sendData(data, data.length);
	}
	public byte[] recv(ProtocolType Type,long timeout){
		byte Head[];
		if(Type==ProtocolType.COMMON)
			Head=COMMON_HEAD;
		else
			Head=IDCARD_HEAD;

		int copyPos=0;
		int recvLen=0;
        System.arraycopy(emptyBuf,0,findHeadBuf,0,findHeadBuf.length);
        System.arraycopy(emptyBuf,0,recvBuf,0,recvBuf.length);
		boolean findHead=false;
		//先把head和len接收到 
		long cur_time=System.currentTimeMillis();
		//Log.e("IO lib", "recv data start!");
		boolean haveDate=false;
		long recvTimeout=0;
		while(System.currentTimeMillis()-cur_time<timeout){
			recvLen=dti.recvData(findHeadBuf,copyPos);
			if(recvLen<=0){
				util.mSleep(5);
				if(haveDate){
					recvTimeout+=5;
					if(recvTimeout>800){
						Log.e("IO lib", "recvHeadTimeout,recv data len="+copyPos);
						return null;
					}
				}
				continue;
			}
			haveDate=true;
			recvTimeout=0;
			copyPos+=recvLen;
			//Log.e("IO lib", "recv data len="+recvLen);
			//Log.e("IO lib", "recv data="+util.bytesToHexString(findHeadBuf, recvLen));
			if(!findHead && copyPos>5){
				
				int headIndex=util.IndexOf(findHeadBuf, Head);
				if(headIndex<0){
					System.arraycopy(findHeadBuf, 1, findHeadBuf, 0, copyPos-1);
					copyPos--;
				}
				else{
					findHead=true;
					System.arraycopy(findHeadBuf, headIndex, recvBuf, 0, copyPos-headIndex);
					copyPos=copyPos-headIndex;
					findHeadBuf=recvBuf;
				}
			}
			
			if(findHead && copyPos>7){
				break;
			}
		}
		if(copyPos<7) {
			Log.e("IO lib", "接收数据头超时,recvLen="+copyPos);
			return null;//接收数据头超时
		}
		//根据len长度接收剩余的数据
		//计算全部数据长度
		int totalDataLength=getLenFromTwoBytes(recvBuf[5],recvBuf[6])+Head.length+2;
		if(totalDataLength>recvBuf.length) {
			Log.e("IO lib", "超过最大长度 err");
			return null;//超过最大长度 err
		}
		//剩余数据长度
		int leftLen=totalDataLength-copyPos;
		haveDate=false;
		recvTimeout=0;
		while(System.currentTimeMillis()-cur_time<timeout && leftLen>0){
			recvLen=dti.recvData(recvBuf,copyPos);
			if(recvLen<=0){
				util.mSleep(5);
				if(haveDate){
					recvTimeout+=5;
					if(recvTimeout>800){
						Log.e("IO lib", "recvBodyTimeout,recv data len="+copyPos);
						return null;
					}
				}
				continue;
			}
			//cur_time=System.currentTimeMillis();
			haveDate=true;
			recvTimeout=0;
			copyPos+=recvLen;
			leftLen-=recvLen;
		}
		if(leftLen>0) {
			Log.e("IO lib", "接收超时,totalDataLength="+totalDataLength+",recvLen="+copyPos);
			return null;//接收超时
		}
		//接收完成 计算xor
		if(xorchk(recvBuf, Head.length, totalDataLength-Head.length)!=0){
			Log.e("IO lib", "计算xor err");
			return null;
		}
/*		byte t[]=new byte[totalDataLength];
		System.arraycopy(recvBuf, 0, t, 0, totalDataLength);
		String path= Environment.getExternalStorageDirectory()+"/synjones/id.data";
		try {
			util.write2SdcardFile(path,t);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//Log.e("IO lib", "recv data end!");
		return recvBuf;
	}
	
	
	private int recv_status=0;
	int copyPos=0;
	int headPos=0;
	int headIndex=0;
	ArrayList<CmdResponse> gResponseList=new ArrayList<CmdResponse>();
	private ArrayList<CmdResponse>  recv(byte datain[]){
		byte Head[]=IDCARD_HEAD;
		int recvLen=datain.length;		
		//先把head和len接收到 
		Log.e("IO lib", "recv data start!");
		System.arraycopy(datain, 0, recvBuf, copyPos, recvLen);
		recvLen+=copyPos;
		copyPos=recvLen;
		while((headIndex=util.IndexOf(recvBuf, Head, headPos))>=0){
			try {
				if((recvLen-headIndex)<10) {
					break;
				}
				int totalDataLength=getLenFromTwoBytes(recvBuf[headIndex+5],recvBuf[headIndex+6])+Head.length+2;
				headPos+=totalDataLength;
				if(recvLen-headPos<0){//需要更多数据，保存剩余数据
					headPos-=totalDataLength;
					break;
				}
				if(xorchk(recvBuf, Head.length+headIndex, totalDataLength-Head.length)==0){
					byte tmp[]=new byte[totalDataLength];
					System.arraycopy(recvBuf, headIndex, tmp, 0, totalDataLength);
					CmdResponse resp=Parse(tmp);
					if(resp!=null)
						gResponseList.add(resp);
				}				
			} catch (Exception e) {
				headPos=0;
				return null;
			}	
		}

		ArrayList<CmdResponse> retList=new ArrayList<CmdResponse>(gResponseList);
		gResponseList.clear();
		return retList;
		
		
		//Log.e("IO lib", "recv data end!");
	}
	
	//head+2len+cmd+para+data+xor
	private byte[] packCmd(ProtocolType Type,int cmd,int para,byte data[]){

		int validDataLen=CMD_OCCUPYBYTES+PARA_OCCUPYBYTES+XOR_OCCUPYBYTES;
		if(data!=null) validDataLen+=data.length;
		int copyPos=0;
		byte[] ret=new byte[COMMON_HEAD.length+VALIDLEN_OCCUPYBYTES+validDataLen];
		if(Type==ProtocolType.IDCARD){
			System.arraycopy(IDCARD_HEAD, 0, ret, 0, IDCARD_HEAD.length);
			copyPos+=IDCARD_HEAD.length;
		}
		else{
			System.arraycopy(COMMON_HEAD, 0, ret, 0, COMMON_HEAD.length);
			copyPos+=COMMON_HEAD.length;
		}		
		ret[copyPos++]=(byte) ((validDataLen>>8)&0xff);
		ret[copyPos++]=(byte) (validDataLen&0xff);
		ret[copyPos++]=(byte) cmd;
		ret[copyPos++]=(byte) para;
		if(data!=null && data.length>0)
		{
			System.arraycopy(data, 0, ret, copyPos, data.length);
			copyPos+=data.length;
		}
		ret[copyPos]=xorchk(ret, COMMON_HEAD.length, ret.length-COMMON_HEAD.length-1);
		return ret;
	}
	
	//head+2len+3sw+data+1xor
	private CmdResponse Parse(byte recv[]){
		if(recv==null) return null;
		int validDatalen =getLenFromTwoBytes(recv[5],recv[6]);
		if(validDatalen<4) return null;//至少包含3个字节的读卡器状态，1字节校验
		CmdResponse cmdResponse=new CmdResponse();
		cmdResponse.data=new byte[validDatalen-SW_OCCUPYBYTES-XOR_OCCUPYBYTES];
		int dataStartPos=COMMON_HEAD.length+VALIDLEN_OCCUPYBYTES+SW_OCCUPYBYTES;
		System.arraycopy(recv,dataStartPos, cmdResponse.data, 0,  cmdResponse.data.length);
		System.arraycopy(recv,COMMON_HEAD.length+VALIDLEN_OCCUPYBYTES , cmdResponse.sw, 0, SW_OCCUPYBYTES);
		return cmdResponse;
	}
	
	public static byte xorchk(byte[] b, int offset, int length) {
		byte chk = 0;
		int i;
		for (i = 0; i < length; i++) {
			chk ^= b[offset + i];
		}
		return chk;
	}

	private int getLenFromTwoBytes(byte B1,byte B2){
		return (((B1&0xff)<<8)|(B2&0xff))&0xffff;
	}
}

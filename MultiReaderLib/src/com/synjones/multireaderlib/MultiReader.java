package com.synjones.multireaderlib;

import java.util.ArrayList;

import com.synjones.multireaderlib.IOProtocol.ProtocolType;

/**
 * 多合一读卡器
* @ClassName MultiReader 
* @author zhaodianbo@Synjones
* @date 2014-8-26 下午4:00:30 
*
 */
public class MultiReader{
	
	 //数据传输接口	 
	//private DataTransInterface dti;
	private IOProtocol iop;
	
	private MultiReader(){
	}	
	//单例模式
	private static MultiReader readerInstance=new MultiReader();
	
	public static MultiReader getReader(){
		return readerInstance;
	}
	
	/**
	 * 设置数据传输接口
	* @Title setDataTransInterface 
	* @param dti
	 */
	public void setDataTransInterface(DataTransInterface dti){
		iop=new IOProtocol(dti);
	}
	public void setIOProtocol(IOProtocol ioProtocol){
		iop=ioProtocol;
	}
	
	public CmdResponse doCmd(int cmd,int para,byte data[],long timeout){
		return iop.doCmd(cmd, para, data, timeout);
	}
	
	public CmdResponse doIDcardCmd(int cmd,int para,long timeout){
		return iop.doIDcardCmd(cmd, para, null, timeout);
	}
	
	public CmdResponse doIDcardCmd(int cmd,int para,byte data[],long timeout){
		return iop.doIDcardCmd(cmd, para, data, timeout);
	}
	
	public void sendIDcardCmd(int cmd,int para,byte data[],long timeout){
		iop.sendIDcardCmd(cmd, para, data, timeout);
	}
	public ArrayList<CmdResponse>  recvIDcardCmd(byte data[]){
		return iop.recvIDCardCmd(data);
	}
	
	
	public boolean openReader(){
		return EnterWorkMode();
	}
	
	public void closeReader(){		
		EnterSavePowerMode();
	}
	
	public boolean EnterSavePowerMode(){
		return EnterSavePowerMode(1000);
	}
	
	public boolean EnterSavePowerMode(long timeout){
		CmdResponse response=doReaderCmd(1,null, timeout);
		if(response!=null && response.SwIs9000())
			return true;
		return false;
	}
	
	public boolean EnterWorkMode(){
		return EnterWorkMode(1000);
	}
	
	public boolean EnterWorkMode(long timeout){
		iop.sendFF();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		CmdResponse response=doReaderCmd(2,null, timeout);
		if(response!=null&& response.SwIs9000())
			return true;
		return false;
	}
	
	public byte findCard(){
		CmdResponse response=doReaderCmd(5,null, 500);
		if(response==null) return -1;
		return response.getCommonByte();
	}
	
	public byte[] getBatteryInfo(){
		CmdResponse response=doReaderCmd(3,null, 500);
		if(response==null) return null;
		return response.getCommonData();
	}
	
	public void beep(int flag){
		doReaderCmd(4,flag, 0);
	}
	public void beepTime(int time){
		byte data[]=new byte[3];
		data[0]=1;
		data[1]=(byte)(time>>8);
		data[2]=(byte)time;
		doReaderCmd(4,data, 0);
	}
	
	public void greenLedControl(int onOffType,int flashingInterval){
		if(onOffType==2|| onOffType==3)
			doReaderCmd(6,onOffType, 0);		
	}
	
	public void flashOn(){
		doReaderCmd(7,null, 0);
	}
	
	public CmdResponse getLastCommondResponse(ProtocolType cmdType, long timeout){
		doReaderCmd(0x0A,null, 0);
		return iop.recvResponse(cmdType, timeout);
	}
	public static final int SET_MODULE_STATUS=1;
	public static final int MODULE_STATUS_ON=1;
	public static final int MODULE_STATUS_OFF=0;
	public static final int GET_MODULE_STATUS=2;
	public static final int SET_PACKET_DELAY=1;
	public static final int GET_PACKET_DELAY=2;
	public static final int SET_PACKET_LENGTH=3;
	public static final int GET_PACKET_LENGTH=4;
	public CmdResponse setIdModulePara(int cmd,int para){
		byte datas[]=new byte[2];
		datas[0]=(byte) cmd;
		datas[1]=(byte) para;
		return doReaderCmd(8,datas,500);
	}
	public CmdResponse getIdModulePara(int paraNo){
		return doReaderCmd(8,paraNo,500);
	}
	
	public CmdResponse setBtTransPara(int cmd,int para){
		byte datas[]=new byte[2];
		datas[0]=(byte) cmd;
		datas[1]=(byte) para;
		return doReaderCmd(0x0b,datas,500);
	}
	public CmdResponse getBtTransPara(int paraNo){
		return doReaderCmd(0x0b,paraNo,500);
	}


	public void updateBegin(){
		updateControl(1);
	}


	public void updateSetPacketInfo(int packetLen,int packetNums){
		byte data[]=new byte[3];
		data[0]= (byte) packetLen;
		data[1]=(byte)(packetNums>>8);
		data[2]=(byte)packetNums;
		updateControl(4,data);
	}
	public int getUpdateResponse(){
		return GetUpdateResponse(0);
	}

	public void updateEnd(){
		updateControl(3);
	}

	public void updateConfirm(){
		updateControl(1);
	}
	
	public void updateControl(int flag){
		iop.doCmd(0x0c, flag, null, 0);
	}

	public void updateControl(int flag,byte data[]){
		iop.doCmd(0x0c, flag, data, 0);
	}
	
	public void updateProgram(byte data[]){
		iop.doCmd(0x0c, 2, data, 0);
	}
	
	public int GetUpdateResponse(int flag){
		CmdResponse cr=iop.recvResponse(ProtocolType.COMMON,500);
		if(cr!=null &&cr.SwIs9000())
		{
			return 0;
			/*if(cr.getCmd()!=(byte)0x0c || cr.getPara()!=(byte)flag)
				return -1;
			else
				return cr.getCommonByte();*/
		}
		return -1;
	}

	public CmdResponse getVersionInfo(){
		CmdResponse response=iop.doCmd(0x14, 0xff, null, 500);
		if(response!=null && response.reponseOK(0x14, 0xff))
			return response;
		return null;
	}

	private CmdResponse doReaderCmd(int para,byte data[],long timeout){		
		CmdResponse response=iop.doCmd(0x18, para, data, timeout);
		if(response!=null && response.reponseOK(0x18, para))
			return response;
		return null;
	}
	
	private  CmdResponse doReaderCmd(int para,int data,long timeout){		
		byte data_array[]=new byte[1];
		data_array[0]=(byte) data;
		return doReaderCmd(para, data_array, timeout);
	}
}
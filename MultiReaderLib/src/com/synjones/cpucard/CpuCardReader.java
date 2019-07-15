package com.synjones.cpucard;

import android.util.Log;

import com.synjones.cardutil.util;
import com.synjones.multireaderlib.CmdResponse;
import com.synjones.multireaderlib.MultiReader;

/**
 * cpu读卡器
* @ClassName CpuCardReader 
* @author zhaodianbo@Synjones
* @date 2014-10-20 下午3:31:35 
*
 */
public class CpuCardReader {
	MultiReader reader;
	CpuCardCmd cpuCmd=new CpuCardCmd();
	public CpuCardReader(MultiReader reader){
		this.reader=reader;
	}
	
	public enum CpuType{CPU,PSAM};
	public static final int PSAM_1=1;
	public static final int PSAM_2=2;
	
	public class ApduRet{
		public byte sw[]=new byte[2];
		public byte data[]=null;
		public boolean SwIs9000(){
			if(sw[0]==(byte)0x90 && sw[1]==0)
				return true;
			else
				return false;
		}
	}
	
	/**
	 * 寻卡函数  
	* @Title findCard 
	* @return 卡号
	 */
	public byte[] findCpuCard(){
		CmdResponse response=doCpuCmd(1, null, 2000);
		if(response==null) return null;
		int cardNoLen=response.getCommonByte();
		if(cardNoLen<=0) return null;
		byte cardNo[]=new byte[response.getCommonByte()];
		try {			
			System.arraycopy(response.getData(), 3, cardNo, 0, cardNo.length);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(getClass().getName(), ""+e.getMessage());
			return null;
		}
		return cardNo;
	}
	/**
	 * 外部认证
	* @Title cpuExternalAuth 
	* @param keyID 外部认证密钥标识号
	* @param encryptedRn 8字节加密后的随机数
	* @return
	 */
	public ApduRet cpuExternalAuth(int keyID,byte encryptedRn[]){
		return exeCpuApdu(cpuCmd.CmdExAuth((byte)keyID, encryptedRn));
	}
	/**
	 * 获取随机数
	* @Title cpuGetRandomNum 
	* @return
	 */
	public ApduRet cpuGetRandomNum(){
		return exeCpuApdu(cpuCmd.CmdGetChallenge(8));
	}
	
	
	
	/**
	 * 寻找psam卡
	* @Title findPSAMCard 
	* @param whichPsam psam卡1或psam卡2
	* @return ATR，见不同卡片COS
	 */
	public byte[] findPSAMCard(int whichPsam){
		int whichOperate=whichPsam==PSAM_1?1:4;
		CmdResponse response=doPSAMCmd(whichOperate, null, 2000);
		if(response==null) return null;
		return response.getCommonData();
	}
	
	/**
	 * 卸载Psam卡
	* @Title unloadPasm 
	* @param whichPsam
	* @return
	 */
	public int unloadPasm(int whichPsam){
		int whichOperate=whichPsam==PSAM_1?3:6;
		CmdResponse response=doPSAMCmd(whichOperate, null, 500);
		if(response==null) return -1;
		return 0;
	}
	
	public ApduRet exeCpuApdu(byte cmd[]){
		return exeApdu(CpuType.CPU, 0, cmd, false);
	}
	public ApduRet exePSAMApdu(byte cmd[]){
		return exeApdu(CpuType.PSAM,PSAM_1,cmd,true);
	}
	
	public ApduRet exeApdu(CpuType CardType,int whichPsam,byte cmd[],boolean psamNeedInfo){
		ApduRet Ret=null;
		switch (CardType) {
		case CPU:
			CmdResponse response=reader.doCmd(0x22, 2, cmd, 1000);
			Ret=getApduRet(response);
			break;
		case PSAM:
			for(int i=0;i<2;i++){
				int whichOperate=whichPsam==PSAM_1?2:5;
				CmdResponse response2=reader.doCmd(0x17,whichOperate, cmd, 1500);
				if(response2==null) return null;
				Ret=getApduRet(response2);
				if(Ret==null) return null;
				if(Ret.sw[0]==(byte)0x61 && psamNeedInfo)
					cmd=cpuCmd.PsamGetResponse(Ret.sw[1]);					
				else
					break;
			};
			break;
		default:
			break;
		}
		return Ret;
	}
	

	
	public ApduRet getApduRet(CmdResponse response){
		if(response==null) return null;
		ApduRet apduret=new ApduRet();
		byte apduRetData[]=response.getCommonData();
		if(apduRetData==null||apduRetData.length<2) return null;
		int datalen=apduRetData.length-2;
		if(datalen>0) {
			apduret.data = new byte[datalen];
			System.arraycopy(apduRetData, 0, apduret.data, 0, datalen);
		}
		System.arraycopy(apduRetData,datalen , apduret.sw, 0, 2);
		return apduret;
	}
	
	
	private CmdResponse doCpuCmd(int para,byte data[],long timeout){
		CmdResponse response=reader.doCmd(0x22, para, data, timeout);
		if(response!=null && response.reponseOK(0x22, para))
			return response;
		return null;
	}
	
	
	private CmdResponse doPSAMCmd(int para,byte data[],long timeout){
		CmdResponse response=reader.doCmd(0x17, para, data, timeout);
		if(response!=null && response.reponseOK(0x17, para))
			return response;
		return null;
	}
}

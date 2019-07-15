package com.synjones.multireaderlib;
/**
 * 读卡器返回数据解包后的数据
* @ClassName CmdResponse 
* @author zhaodianbo@Synjones
* @date 2014-8-26 下午3:37:18 
*
 */
public class CmdResponse{
	public byte sw[]=new byte[3];
	byte data[]=null;
	public boolean SwIs9000(){
		if(sw[0]==0 && sw[1]==0 && sw[2]==(byte)0x90)
			return true;
		else
			return false;
	}
	
	public byte getCmd(){
		return data[0];
	}
	public byte getPara(){
		return data[1];
	}
	public byte[] getData(){
		return data;
	}
	/**
	 * 非二代证操作时，返回数据的第三位通常是命令执行的结果
	* @Title getCommonByte 
	* @return
	 */
	public byte getCommonByte(){
		try {
			return data[2];
		} catch (Exception e) {
			// TODO: handle exception
		}
		return -1;
		
	}
	/**
	 * data是读卡器返回的所有有效数据，commonData是去除命令和参数的有效数据。
	* @Title getCommonData 
	* @return
	 */
	public byte[] getCommonData(){
		try{
			byte commonData[]=new byte[data.length-2];
			System.arraycopy(data, 2, commonData, 0, commonData.length);
			return commonData;
		}catch (Exception e){}
		return null;
	}
	public  boolean reponseOK(int cmd,int para){
		if( !SwIs9000()) return false;
		//if(getCmd()!=(byte)cmd || getPara()!=(byte)para)
		//	return false;
		return true;
		
	}
}
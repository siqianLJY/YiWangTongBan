package com.synjones.m1card;

import android.util.SparseArray;

import com.synjones.multireaderlib.CmdResponse;
import com.synjones.multireaderlib.MultiReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 实现M1卡相关操作
* @ClassName MifareCard 
* @author zhaodianbo@Synjones
* @date 2014-8-26 下午4:03:54 
*
 */
public class M1CardReader{
	MultiReader reader;
	public enum KeyType{KeyA,KeyB};
	public M1CardReader(MultiReader reader){
		this.reader=reader;
	}
	int timeout=800;
	/**
	 * 寻找M1卡
	* @Title findM1Card 
	* @return 4字节卡序列号+ 1字节（保留）+1字节卡类型+2字节ATQ字节
	 */
	public byte[] findM1Card(){
		CmdResponse resp= doM1ReaderCmd(0, null, timeout);
		if(resp==null) return null;
		return resp.getData();
	}
	
	/**
	 * 验证密钥
	* @Title verifyKey 
	* @param type 密钥类型 KeyType.KeyA | KeyType.KeyB
	* @param blockNo 验证块号
	* @param key 6字节密钥
	* @return true 成功
	 */
	public boolean verifyKey(KeyType type,int blockNo,byte key[]){
		
		
		byte data[]=new byte[8];
		data[0]=(byte) ((KeyType.KeyA ==type)?0:1);
		data[1]=(byte) blockNo;
		System.arraycopy(key, 0, data, 2, 6);
		
		if(isTrans){
			baos.write(9);
			baos.write(1);			
			try {
				baos.write(data);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		CmdResponse resp= doM1ReaderCmd(0x1, data, timeout);
		if(resp!=null)
			return true;
		/*else{
			pend();
			byte ret[]=findM1Card();
			if(ret==null || doM1ReaderCmd(0x1, data, 200)==null)
				return false;
			else
				return true;
		}*/
		return false;
	}
	
	/**
	 * 读块值
	* @Title readBlock 
	* @param blockNo 块号
	* @return 16字节数据
	 */
	public byte[] readBlock(int blockNo){
		byte data[]=new byte[1];
		data[0]=(byte) blockNo;
		
		if(isTrans){
			baos.write(2);
			baos.write(2);
			try {
				baos.write(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		CmdResponse resp= doM1ReaderCmd(0x2, data, timeout);
		//if(resp==null) 
		//	resp= doM1ReaderCmd(0x2, data, 200);
		if(resp==null) 	return null;
		
		return resp.getData();
	}
	
	/**
	 * 写块
	* @Title writeBlock 
	* @param blockNo 块号
	* @param writeData 16字节块数据
	* @return true 成功
	 */
	public boolean writeBlock(int blockNo,byte writeData[]){
		byte data[]=new byte[17];
		data[0]=(byte) blockNo;
		System.arraycopy(writeData, 0, data, 1, 16);
		
		if(isTrans){
			baos.write(18);
			baos.write(3);
			try {
				baos.write(data);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		CmdResponse resp= doM1ReaderCmd(0x3, data, timeout);
		if(resp!=null)
			return true;
		//else
		//	resp= doM1ReaderCmd(0x3, data, 200);
	//	if(resp!=null)
	//		return true;
		return false;
	}
	
	/**
	 * 挂起M1卡
	* @Title pend 
	* @return ture 成功
	 */
	public boolean pend(){
		CmdResponse resp= doM1ReaderCmd(0x6, null, timeout);
		if(resp!=null)
			return true;
		return false;
	}
	
	private boolean isTrans=false;
	ByteArrayOutputStream baos;
	/**
	 * 开始事务处理，在write、read或verifyKey之前调用
	 * 如果不使用事务，则调用write、read或verifyKey时直接执行
	* @Title beginTrans
	 */
	public void beginTrans(){
		isTrans=true;	
		try {
			if(baos!=null)
				baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		baos=new ByteArrayOutputStream();
	}
	/**
	 * 提交事务，在write、read或verifyKey之后调用
	* @Title endTrans 
	* @param timeout  等待事务处理超时时间，以 ms为单位
	* @return
	 */
	public CmdResponse endTrans(long timeout){
		isTrans=false;
		byte array[]=baos.toByteArray();
		try {
			baos.close();
			baos=null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(array==null || array.length<=0)
			return null;
		CmdResponse resp= doM1ReaderCmd(0x9, array, timeout);
		//if(resp==null) 
		//	resp= doM1ReaderCmd(0x2, data, 200);
		return resp;	
	}
	
	public SparseArray<byte[]> getReadData(CmdResponse resp){
		if(resp==null|| !resp.SwIs9000() || resp.getData()==null) return null;
		int blockNums=resp.getData().length/17;
		SparseArray< byte[]> dataArray=new SparseArray< byte[]>(blockNums);
		for(int i=0;i<blockNums;i++){
			byte data[]=new byte[16];
			System.arraycopy(resp.getData(), i*17+1, data, 0, 16);
			int key=(resp.getData()[i*17])&0xff;
			dataArray.put(key, data);
		}
		return dataArray;
	}
	
	private CmdResponse doM1ReaderCmd(int para,byte data[],long timeout){
		CmdResponse response=reader.doCmd(0x20, para, data, timeout);
		if(response!=null && response.reponseOK(0x20, para))
			return response;
		return null;
	}
}
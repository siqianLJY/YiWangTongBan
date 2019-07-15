package com.synjones.idcard;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import com.synjones.cardutil.util;
import com.synjones.multireaderlib.CmdResponse;
import com.synjones.multireaderlib.IOProtocol.ProtocolType;
import com.synjones.multireaderlib.MultiReader;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * 身份证操作
* @ClassName IdentityCard 
* @author zhaodianbo@Synjones
* @date 2014-8-26 下午4:07:47 
*
 */
public class IDcardReader{
	MultiReader reader;
	public IDcardReader(MultiReader reader){
		this.reader=reader;
	}
	
	private DecodeAIDLService myAIDLService;
	private volatile boolean mIsBound=false;
	private Context mContext;
	private Bitmap bmp;
	private ServiceConnection Remoteconnection = new ServiceConnection() {  
		  
        @Override  
        public void onServiceDisconnected(ComponentName name) {  
        }  
  
        @Override  
        public void onServiceConnected(ComponentName name, IBinder service) {  
            myAIDLService = DecodeAIDLService.Stub.asInterface(service);
			mIsBound=true;
        }  
    };  
	
    /**
     * 打开二代证读卡
    * @Title open 
    * @param mContext
     */
	public void open(Context mContext){

		//setMaxRFByte((byte) 0x50);
		this.mContext=mContext.getApplicationContext();
		if(!mIsBound || myAIDLService==null){
			Intent bindIntent = new Intent(this.mContext, RemoteDecodeService.class);
			this.mContext.bindService(bindIntent, Remoteconnection, Context.BIND_AUTO_CREATE);
			long t=System.currentTimeMillis();
			while(System.currentTimeMillis()-t<1000 && !mIsBound){
				util.mSleep(50);
			}
		}


	}
		

	
	
	/** 结束读卡  关闭读卡器
	* @Title: CloseSerialPort    
	* @return void 
	*/
	public void close(){		

		
		try {
    	   	if(mIsBound){
    	    	mContext.unbindService(Remoteconnection);
    			mIsBound=false;
        	}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	
	
	/**  获取身份证信息，此函数将阻塞直到返回身份证或读卡超时
	* @Title: getIDcardBlocking   
	* @return IDCard 
	*/
	public IDCard getIDcardBlocking(){
		IDCard card=null;
		try {
			card= getIDCardFp();
			//card.decodeResult=decodeWltStep(card,wltPath, bmpPath);
			if(card==null) return null;
			decodePhoto2Card(card);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return card;
	}
	
	public IDCard getIDcardBlockingNoFp(boolean readOnce){
		IDCard card=null;
		try {
			card= getIDCardNoFp(readOnce);
			//card.decodeResult=decodeWltStep(card,wltPath, bmpPath);
			if(card==null) return null;
			decodePhoto2Card(card);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return card;
	}
	
	public IDCardReaderRetInfo getIDcardBlockingNoFpRetInfo(boolean readOnce){
		IDCardReaderRetInfo info = null;
		try {
			info = getIDCardNoFpRetInfo(readOnce);
			if(info.errCode!=0) return info;
			decodePhoto2Card(info.card);
		} catch (Exception e) {
			e.printStackTrace();
			info=new IDCardReaderRetInfo(IDCardReaderRetInfo.ERROR_UNKNOWN);
		}		
		return info;
	}
	
	
	
	public IDCardReaderRetInfo getIDcardBlockingNoFpRetInfo(boolean readOnce,boolean needDecodePhoto){
		IDCardReaderRetInfo info = null;
		try {
			if(needDecodePhoto) 
				return getIDcardBlockingNoFpRetInfo(readOnce);
			info = getIDCardNoFpRetInfo(readOnce);
			if(info.errCode!=0) return info;

		} catch (Exception e) {
			e.printStackTrace();
			info=new IDCardReaderRetInfo(IDCardReaderRetInfo.ERROR_UNKNOWN);
		}		
		return info;
	}
	
	/**  获取身份证信息，此函数将阻塞直到返回身份证或读卡超时
	* @Title: getIDcardBlocking   
	* @return IDCard 
	*/
	public IDCard getIDcardBlocking(int findtimeout,int selecttimout,int readtimeout){
		IDCard card=null;
		try {
			//IDCard idcard=new IDCard();
			findIDCard(findtimeout);
			selectIDCard(selecttimout);
	/*		if(cr==null || cr.sw[0]!=0 ||cr.sw[1]!=0 || (cr.sw[2]!=(byte)0x90 && cr.sw[2]!=(byte)0x81))
				resp=readBaseMsgFp(200);
			else*/
			card=getIdcardInfo(readBaseMsgFp(readtimeout),true);
			//card.decodeResult=decodeWltStep(card,wltPath, bmpPath);
			if(card==null) return null;
			decodePhoto2Card(card);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return card;
	}
	
	 public byte[] decodeWlt(byte wlt[])
    {
		//byte []wlt={87, 76, 102, 0, 126, 0, 50, 0, 0, -1, -123, 23, 81, 81, 81, 62, 113, 13, -43, 100, -13, 7, -54, -105, -125, -100, -117, -22, -111, 95, 86, -95, -108, -2, -39, 27, -97, 101, 62, -92, 113, 119, -89, 42, -100, -117, 32, 23, 60, 124, -118, 114, 110, -94, -46, -43, -56, 76, -101, 74, 117, -128, -107, 76, -49, 26, -80, 40, 125, -101, -78, 70, -125, 120, -3, -64, -55, -23, -76, -78, 119, 105, -53, -111, -82, -36, 82, 81, 81, 90, 62, -110, 69, 59, -104, 10, 22, -16, -74, 48, 42, 85, 64, 119, 96, -56, -114, -71, 93, -15, 8, 61, 35, 93, -69, 58, 97, -72, -128, -18, 43, 14, -43, -36, 55, 112, 30, 29, 97, 20, -36, -98, -54, -22, -91, 40, 13, -98, 28, 52, -120, -28, -51, 104, -81, -35, 122, -42, -118, -105, 93, -14, 90, 125, 35, 56, 51, -84, 118, 76, 42, -99, -66, -41, -96, 86, 114, -32, 37, 57, -114, 101, 0, -107, -67, -108, -78, 46, -88, 100, -104, -85, -109, -116, 115, 75, -50, 3, 39, -81, 78, -104, 13, -107, -31, 35, 37, 107, 56, 68, -17, -90, -20, 91, 114, -15, 35, -84, 88, 73, -91, -108, 51, 44, 63, 40, 75, 2, -91, -43, 93, -46, -53, -52, -120, 26, -67, -105, 86, -91, 38, -121, -83, -24, -114, -73, -68, -123, 37, -9, 91, -127, -2, -104, -93, 103, -41, -126, -5, 111, -7, -30, 31, 116, 95, 9, -73, 23, -112, 123, -93, 1, 81, 32, 51, 20, 48, 12, 56, 56, 100, 26, 47, 100, 77, -47, 104, -128, -40, -74, -105, 114, -64, -65, -106, -10, -83, 50, 107, -77, -123, -115, -83, 3, -51, 45, 14, 76, 40, -82, 81, 105, 43, 38, -47, -8, -10, -24, -30, 18, -28, 72, 38, 89, 102, 54, 63, 34, 68, 64, -115, -33, -10, 124, 56, 38, 84, -36, -101, -7, 48, -15, -26, 10, -50, 23, -53, 34, -73, 64, 79, -91, -80, 4, 27, 76, 54, 10, -78, 75, -110, 59, 125, -49, 113, -36, 74, -113, 99, -11, 29, 59, 67, 38, -75, -52, -74, 76, -100, 12, 56, -49, -6, 17, -46, -45, -109, 123, -50, 77, -46, 113, -46, -119, 22, 53, 11, -59, -23, 10, -37, -24, 37, -106, 125, -112, 87, -54, 74, 117, 102, -85, 58, -26, 121, 62, -118, -100, -71, 44, 34, -47, 40, 55, -75, -47, -46, 23, -55, -24, -34, -91, -8, -128, -105, 25, -95, 59, -47, 60, 125, -35, 126, 23, 33, -127, 46, -26, -104, -34, 0, 42, 8, 22, 52, -1, -97, -51, 53, 88, -4, -98, 49, 61, -16, 73, 80, -49, 16, -55, 69, 21, 29, -82, 81, -26, 88, -80, 88, 41, -116, -10, -62, 50, -86, 91, -62, -9, 105, -122, -116, -93, -12, -18, -66, 34, -104, 88, -91, -18, -61, -33, 125, -31, 65, 98, 54, -55, -115, 97, 13, -45, -17, 73, 11, 22, -63, -114, -12, -43, -83, 54, 76, -125, -78, 116, 87, 8, 31, -25, -84, 66, 61, -21, 27, 100, 99, 60, 55, -72, 22, -41, 95, 87, 106, -105, -86, -36, -80, 15, 92, -108, 76, 99, -107, 47, -115, 102, 29, 79, -58, -55, -21, -20, -91, -86, -102, -32, 82, 69, -38, -83, 111, -44, 35, 55, 33, 42, -50, -103, 12, 7, 97, 45, -7, -128, -25, 49, 27, -70, 95, 106, -76, -80, 30, -77, -108, 43, 5, -90, 89, 40, -33, -57, 34, -110, -3, 92, 101, -51, 13, -67, 61, 115, -124, 93, 72, -31, -102, 56, 66, -108, 51, -57, -101, 9, -31, -98, 25, -70, -16, -95, 117, 91, 74, 66, 37, -51, 31, 121, 104, -119, -120, 104, 33, 35, 32, 26, -109, -105, 49, -36, 51, 8, -118, -33, 99, 127, 55, -102, -123, -89, -73, 20, 38, 4, 120, -12, 119, -79, 49, 41, 10, -100, -117, 23, 3, 44, -45, 38, 118, -83, -23, 107, -39, -45, -93, 16, -73, -9, 102, 10, 64, -35, 73, 89, 122, -122, 10, -40, 86, -18, 11, -112, -32, -36, -7, -102, -11, -89, 52, -69, 66, -82, 81, 94, 91, -54, 92, 44, -53, -125, -112, 31, 25, -62, 8, 12, 15, 48, -77, -76, -121, 90, -110, 0, 26, 103, -37, -124, -75, 74, -67, 114, 9, 90, 66, 63, 114, -91, 30, -81, 71, -115, 41, 121, 54, -5, 34, 12, -85, 47, -79, 4, -87, -19, -75, -63, 119, -74, -35, -30, 9, 53, 34, -71, -110, -76, 19, -125, 11, -49, 93, -124, 49, -97, 11, -113, -33, 2, 18, -100, 89, 59, 72, 27, 38, -43, 76, -79, -19, -15, -65, 78, 105, 60, -27, -36, -72, -65, 39, -107, -78, -121, -124, 99, -37, 12, 87, 2, -44, -68, -52, 104, -56, 110, -9, -68, 17, 88, 66, 31, -36, -26, -18, 117, -111, 100, -2, 108, 20, -83, -41, 61, -44, -115, -70, 10, -84, 75, 69, -9, -31, -116, 80, -36, 46, 126, 57, -9, 108, -28, -3, 69, 41, 92, 94, 115, -34, 111, 43, -81, 80, -53, 113, -37, -70, 85, 90, 62, 9, 105, -65, -52, -76, 47, -15, -102, 58, -81, 30, 46, 85, -51, -60, -80, -66, -109, 78, 93, 29, 62, 69, 78, -118, 123, -54, 28, 120, -110, 122, 115, -50, -76, 72, 86, -7, 23, -46, 22, -82, 81, 111, 110, -47, -25, 36, -76, -34, -16, -79, -64, -90, 95, -12, -117, 12, 46, -81, 106, 55, 76, -109, 88, -124, -103, 1, -127, 114, 4, 24, 95, 39, -40, 40, -93, -38, 18, 43, -65, 90, 62, 2, 124, -127, 63, 30, -112, -111, 22, 29, 1, 36, 107, 86, -86, 36, 20, 106, 115, 115, -114, -77, -2, -47, 97, -67, -82, 81, 94, -119, -110, 71, -34, -108, 38, -109, -65, -38, -85, 34, -91, 59, 29, -22, -68, 50, -24, 26, -99, -91, 41, 12, 77, -70, -44, -22, -113, -87, 16, 59, 1, 94, 98, -63, -62, -38, 93, -73, 23, -100, -100, 38, -22};
	 	if(wlt==null) return null;
        //利用IBinder对象间接调用服务里面的方法
    	try {
			return   myAIDLService.decode(wlt);
		} catch (Exception e) {
			e.printStackTrace();
			return reopenAndDecodeWlt2(wlt);
		}

    }

	private byte[] reopenAndDecodeWlt2(byte wlt[]){
		close();
		myAIDLService=null;
		open(mContext);
		long t=System.currentTimeMillis();
		while (System.currentTimeMillis()-t<600){
			if(myAIDLService!=null){
				try {
					return myAIDLService.decode2(wlt);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			util.mSleep(50);
		}
		return null;
	}
	 
	 private void decodePhoto2Card(IDCard card){
		byte bmpBytes[]=decodeWlt(card.getWlt());
		if(bmpBytes!=null)
			bmp=BitmapFactory.decodeByteArray(bmpBytes, 0, bmpBytes.length);
		else
			bmp=null;
		card.setPhoto(bmp);
	 }
 
	
	
	/**
	 * 复位SAM_V
	* @Title resetSAM 
	* @return
	 */
	public CmdResponse resetSAM(){
		return reader.doIDcardCmd(0x10,0xff,300);
	}
	
	/**
	 * 设置最大通信字节数
	* @Title setMaxRFByte 
	* @param max
	* @return
	 */
	public CmdResponse setMaxRFByte(int max){
		byte data[]=new byte[1];
		data[0]=(byte) max;
		return reader.doIDcardCmd(0x61, 0xff, data, 300);
	}
	/**
	 * SAM_V状态检测
	* @Title getSAMStatus 
	* @return
	 */
	public CmdResponse getSAMStatus(){
		return reader.doIDcardCmd(0x11, 0xff, 300);
	}
	
	/**
	 * 获取samvID (读SAM_V管理信息)
	* @Title getSAMID 
	* @return
	 */
	public CmdResponse getSAMID() {
		return reader.doIDcardCmd(0x12, 0xff, 300);
	}
	/**
	 * 寻卡
	* @Title findIdcard 
	* @return
	 */
	public CmdResponse findIDCard(){
		return reader.doIDcardCmd(0x20, 0x01, 500);
	}
	public CmdResponse findIDCard(int timeout){
		return reader.doIDcardCmd(0x20, 0x01, timeout);
	}
	
	public void findIDCardNoRep(){
		 reader.sendIDcardCmd(0x20, 0x01,null, 500);
	}

	/**
	 * 选卡
	* @Title selectIdcard 
	* @return
	 */
	public CmdResponse selectIDCard(){
		return reader.doIDcardCmd(0x20, 0x02, 500);
	}
	
	public CmdResponse selectIDCard(int t){
		return reader.doIDcardCmd(0x20, 0x02, t);
	}
	
	public void selectIDCardNoRep(){
		reader.sendIDcardCmd(0x20, 0x02, null,500);
	}
	/**
	 * 读固定信息
	* @Title readBaseMsg 
	* @return
	 */
	public CmdResponse readBaseMsg(){
		return reader.doIDcardCmd(0x30, 0x01, 3500);
	}
	
	/**
	 * 读固定信息和指纹信息
	* @Title readBaseMsgFp 
	* @return
	 */
	public CmdResponse readBaseMsgFp(){
		return reader.doIDcardCmd(0x30, 0x10, 3500);
	}
	
	public CmdResponse readBaseMsgFp(long timeout){
		return reader.doIDcardCmd(0x30, 0x10, timeout);
	}
	public void readBaseMsgFpNoRep(){
		 reader.sendIDcardCmd(0x30, 0x10,null,0);
	}
	
	/**
	 * 读追加地址信息
	* @Title readAppendAdd 
	* @return
	 */
	public CmdResponse readAppendAdd(){
		return reader.doIDcardCmd(0x30, 0x03, 1000);
	}
	
	/**
	 * 获取身份证对象
	* @Title getIDCard 
	* @return
	 */
	public IDCard getIDCard() {
		short textlen = 0, wltlen;
		String dbgmsg = "";
		IDCard idcard=new IDCard();
		byte[] basemsg = null;
		findIDCard();
		selectIDCard();
		CmdResponse resp=readBaseMsg();
		if(resp==null) return null;
		if(!resp.SwIs9000()){
			IDCard.SW1=resp.sw[0];
			IDCard.SW2=resp.sw[1];
			IDCard.SW3=resp.sw[2];
			return null;
		}
		if(resp.getData().length<1284) return null;
		else
		{
			try {
				basemsg = new byte[4+256+1024];
				System.arraycopy(resp.getData(), 0, basemsg, 0, basemsg.length);
			
				textlen = (short) (basemsg[0] * 256 + basemsg[1]);
				wltlen = (short) (basemsg[2] * 256 + basemsg[3]);
				byte[] name = new byte[30];
				System.arraycopy(basemsg, 4, name, 0, name.length);
				idcard.setName(new String(name, "UTF-16LE").trim());
				byte[] sex = new byte[2];
				System.arraycopy(basemsg, 34, sex, 0, sex.length);
				idcard.setSex(new String(sex, "UTF-16LE"));
				if (idcard.getSex().equalsIgnoreCase("1"))
					idcard.setSex("男");
				else
					idcard.setSex("女");
				byte[] nation = new byte[4];
				System.arraycopy(basemsg, 36, nation, 0, nation.length);
				idcard.setNation(idcard.getNationName(new String(nation, "UTF-16LE")));
				byte[] birthday = new byte[16];
				System.arraycopy(basemsg, 40, birthday, 0, birthday.length);
				idcard.setBirthday(new String(birthday, "UTF-16LE"));
				byte[] address = new byte[70];
				System.arraycopy(basemsg, 56, address, 0, address.length);
				idcard.setAddress(new String(address, "UTF-16LE").trim());
				byte[] idcardno = new byte[36];
				System.arraycopy(basemsg, 126, idcardno, 0, idcardno.length);
				idcard.setIDCardNo(new String(idcardno, "UTF-16LE"));
				byte[] grantdept = new byte[30];
				System.arraycopy(basemsg, 162, grantdept, 0, grantdept.length);
				idcard.setGrantDept(new String(grantdept, "UTF-16LE").trim());
				byte[] userlifebegin = new byte[16];
				System.arraycopy(basemsg, 192, userlifebegin, 0, userlifebegin.length);
				idcard.setUserLifeBegin(new String(userlifebegin, "UTF-16LE"));
				byte[] userlifeend = new byte[16];
				System.arraycopy(basemsg, 208, userlifeend, 0, userlifeend.length);
				idcard.setUserLifeEnd(new String(userlifeend, "UTF-16LE").trim());
				byte[] wlt = new byte[1024];
				System.arraycopy(basemsg, textlen+4, wlt, 0, wlt.length);
				idcard.setWlt(wlt);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		return idcard;
	}
	
	/**
	 * 获取包含指纹的二代证信息
	* @Title getIDCardFp 
	* @return
	 */
	public IDCard getIDCardFp() {		
		//IDCard idcard=new IDCard();
		findIDCard();
		CmdResponse cr=selectIDCard();
		CmdResponse resp=null;
/*		if(cr==null || cr.sw[0]!=0 ||cr.sw[1]!=0 || (cr.sw[2]!=(byte)0x90 && cr.sw[2]!=(byte)0x81))
			resp=readBaseMsgFp(200);
		else*/
			resp=readBaseMsgFp();

		
		return getIdcardInfo(resp,true);
	}
	
	public IDCard getIDCardNoFp(boolean readOnce) {		
		//IDCard idcard=new IDCard();
		CmdResponse cr=findIDCard();
		if(cr==null) return null;
		if(readOnce && cr.sw[2]!=(byte)0x9f)
			return null;
		selectIDCard();		
		CmdResponse resp=null;
/*		if(cr==null || cr.sw[0]!=0 ||cr.sw[1]!=0 || (cr.sw[2]!=(byte)0x90 && cr.sw[2]!=(byte)0x81))
			resp=readBaseMsgFp(200);
		else*/
		resp=readBaseMsg();

		
		return getIdcardInfo(resp,false);
	}
	
	/**
	 * read id card
	* @Title getIDCard 
	* @param sameCardReadOnce return error msg if card has been read
	* @param needPhoto need decode photo
	* @param needFp need fingerprint info
	* @return
	 */
	public IDCardReaderRetInfo getIDCardInfo(boolean sameCardReadOnce,boolean needPhoto,boolean needFp){
		if(!needFp) return getIDcardBlockingNoFpRetInfo(sameCardReadOnce, needPhoto);
		IDCardReaderRetInfo ret = getIDCardRetInfo(sameCardReadOnce);
		if(!needPhoto || ret.errCode!=0) return ret;
		decodePhoto2Card(ret.card);
		return ret;
	}
	
	public IDCardReaderRetInfo getIDCardNoFpRetInfo(boolean sameCardReadOnce) {		
		//IDCard idcard=new IDCard();
		Log.e("test","recv findcard");
		CmdResponse cr=findIDCard();
		if(cr==null) return new IDCardReaderRetInfo(IDCardReaderRetInfo.ERROR_RECV_FINDCARD);
		if(sameCardReadOnce && cr.sw[2]!=(byte)0x9f)
			return new IDCardReaderRetInfo(IDCardReaderRetInfo.ERROR_SAME_CARD, cr.sw);
		Log.e("test","recv selectIDCard");
		selectIDCard();		
		CmdResponse resp=null;
/*		if(cr==null || cr.sw[0]!=0 ||cr.sw[1]!=0 || (cr.sw[2]!=(byte)0x90 && cr.sw[2]!=(byte)0x81))
			resp=readBaseMsgFp(200);
		else*/
		Log.e("test","recv readBaseMsg");
		resp=readBaseMsg();
		if(resp==null )return new IDCardReaderRetInfo(IDCardReaderRetInfo.ERROR_RECV_READBASE);
		if(!resp.SwIs9000() || (resp.SwIs9000() && resp.getData().length<1284) )
			return new IDCardReaderRetInfo(IDCardReaderRetInfo.ERROR_READ_CARD, resp.sw);
		
		return new IDCardReaderRetInfo(IDCardReaderRetInfo.ERROR_OK, getIdcardInfo(resp,false));
	}
	
	public IDCardReaderRetInfo getIDCardRetInfo(boolean sameCardReadOnce) {		
		//IDCard idcard=new IDCard();
		CmdResponse cr=findIDCard();
		if(cr==null) return new IDCardReaderRetInfo(IDCardReaderRetInfo.ERROR_RECV_FINDCARD);
		if(sameCardReadOnce && cr.sw[2]!=(byte)0x9f)
			return new IDCardReaderRetInfo(IDCardReaderRetInfo.ERROR_SAME_CARD, cr.sw);
		selectIDCard();		
		CmdResponse resp=null;
		resp=readBaseMsgFp();
		if(resp==null )return new IDCardReaderRetInfo(IDCardReaderRetInfo.ERROR_RECV_READBASE);
		if(!resp.SwIs9000() || (resp.SwIs9000() && resp.getData().length<1284) )
			return new IDCardReaderRetInfo(IDCardReaderRetInfo.ERROR_READ_CARD, resp.sw);
		
		return new IDCardReaderRetInfo(IDCardReaderRetInfo.ERROR_OK, getIdcardInfo(resp,true));
	}
	
	public IDCardReaderRetInfo getLastBaseMsg(boolean isFpBasemsg){
		CmdResponse resp=reader.getLastCommondResponse(ProtocolType.IDCARD,3000);
		if(resp==null )return new IDCardReaderRetInfo(IDCardReaderRetInfo.ERROR_RECV_READBASE);
		if(!resp.SwIs9000() || (resp.SwIs9000() && resp.getData().length<1284) )
			return new IDCardReaderRetInfo(IDCardReaderRetInfo.ERROR_READ_CARD, resp.sw);
		IDCardReaderRetInfo ret = new IDCardReaderRetInfo(IDCardReaderRetInfo.ERROR_OK, getIdcardInfo(resp,isFpBasemsg));
		decodePhoto2Card(ret.card);
		return ret;
	}
	
	
	
	public void getIDCardFpNoRep() {		
		//IDCard idcard=new IDCard();
		try {
			findIDCardNoRep();
			Thread.sleep(100);
			selectIDCardNoRep();
			Thread.sleep(100);
			readBaseMsgFpNoRep();	
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public ArrayList<CmdResponse>  getResponse(byte data[]){
		return reader.recvIDcardCmd(data);
	}
	
	
	public IDCard getIdcardInfo(CmdResponse resp,boolean isFpBasemsg){
		short textlen=0, wltlen=0,fplen = 0;
		//boolean isFpBasemsg=false;
		String dbgmsg = "";
		byte basemsg[]=null;
		IDCard idcard=new IDCard();
		if(resp==null || resp.getData().length<1284) return null;
		if(!resp.SwIs9000()){
			IDCard.SW1=resp.sw[0];
			IDCard.SW2=resp.sw[1];
			IDCard.SW3=resp.sw[2];
			return null;
		}
		else {
			try {
				basemsg=resp.getData();
				textlen = (short) (basemsg[0] * 256 + basemsg[1]);
				wltlen = (short) (basemsg[2] * 256 + basemsg[3]);
				if(isFpBasemsg)
					fplen= (short) (basemsg[4] * 256 + basemsg[5]);
				int varLen=isFpBasemsg?6:4;
				//System.arraycopy(resp.getData(), 0, basemsg, 0, textlen+wltlen+fplen+varLen);
				if(basemsg[varLen+248]==(byte)0x49)//外国人
				{
					ForeignerIDCard foreignerIDCard=new ForeignerIDCard();
					foreignerIDCard.parse(basemsg,varLen);
					idcard.setForeignerIDCard(foreignerIDCard);
					idcard.setCardType(IDCard.CARDTYPE_FOREIGNER);
					idcard.setSex(foreignerIDCard.getSex());
					idcard.setNation(foreignerIDCard.getNation());
					idcard.setUserLifeBegin(foreignerIDCard.getUserLifeBegin());
					idcard.setUserLifeEnd(foreignerIDCard.getUserLifeEnd());
					idcard.setBirthday(foreignerIDCard.getBorn());
					idcard.setName(foreignerIDCard.getCHNName());
					idcard.setGrantDept(foreignerIDCard.getGrantDep());
					idcard.setIDCardNo(foreignerIDCard.getIDCardNo());
				}else{
					//身份证
					//System.arraycopy(resp.getData(), 0, basemsg, 0, textlen+wltlen+fplen+varLen);
					try {
						byte[] name = new byte[30];
						System.arraycopy(basemsg, varLen, name, 0, name.length);
						idcard.setName(new String(name, "UTF-16LE").trim());
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						byte[] sex = new byte[2];
						System.arraycopy(basemsg, varLen+30, sex, 0, sex.length);
						idcard.setSex(new String(sex, "UTF-16LE"));
						if (idcard.getSex().equalsIgnoreCase("1"))
							idcard.setSex("男");
						else
							idcard.setSex("女");
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						byte[] nation = new byte[4];
						System.arraycopy(basemsg, varLen+32, nation, 0, nation.length);
						idcard.setNation(idcard.getNationName(new String(nation, "UTF-16LE")));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						byte[] birthday = new byte[16];
						System.arraycopy(basemsg, varLen+36, birthday, 0, birthday.length);
						idcard.setBirthday(new String(birthday, "UTF-16LE"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						byte[] address = new byte[70];
						System.arraycopy(basemsg, varLen+52, address, 0, address.length);
						idcard.setAddress(new String(address, "UTF-16LE").trim());
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						byte[] idcardno = new byte[36];
						System.arraycopy(basemsg, varLen+122, idcardno, 0, idcardno.length);
						idcard.setIDCardNo(new String(idcardno, "UTF-16LE"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						byte[] grantdept = new byte[30];
						System.arraycopy(basemsg,varLen+ 158, grantdept, 0, grantdept.length);
						idcard.setGrantDept(new String(grantdept, "UTF-16LE").trim());
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						byte[] userlifebegin = new byte[16];
						System.arraycopy(basemsg, varLen+188, userlifebegin, 0, userlifebegin.length);
						idcard.setUserLifeBegin(new String(userlifebegin, "UTF-16LE"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						byte[] userlifeend = new byte[16];
						System.arraycopy(basemsg, varLen+204, userlifeend, 0, userlifeend.length);
						idcard.setUserLifeEnd(new String(userlifeend, "UTF-16LE").trim());
					} catch (Exception e) {
						e.printStackTrace();
					}

					if(basemsg[varLen+248]=='J'){//港澳台
						idcard.setCardType(IDCard.CARDTYPE_HMT);
						try {
							byte[] passID = new byte[18];
							System.arraycopy(basemsg, varLen+220, passID, 0, passID.length);
							idcard.setPassID(new String(passID, "UTF-16LE").trim());
						} catch (Exception e) {
							e.printStackTrace();
						}

						try {
							byte[] issuesTimes = new byte[4];
							System.arraycopy(basemsg, varLen+238, issuesTimes, 0, issuesTimes.length);
							idcard.setIssuesTimes(new String(issuesTimes, "UTF-16LE").trim());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}



					idcard.setPhoto(bmp);

				}
				try {
					byte[] wlt = new byte[wltlen];
					System.arraycopy(basemsg, textlen+varLen, wlt, 0, wltlen);
					idcard.setWlt(wlt);
				} catch (Exception e) {
					e.printStackTrace();
				}
				byte fp[]=null;
				if(fplen>0){
					fp=new byte[fplen];//指纹信息
					System.arraycopy(basemsg, textlen+wltlen+varLen, fp, 0,fplen );
				}
				idcard.setFp(fp);
				Log.d("Sysjones","id card get");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		return idcard;
	}
	
	/**
	 * 获取追加住址信息
	* @Title readAppendAddress 
	* @return
	 */
	public String readAppendAddress(){
		CmdResponse resp=readAppendAdd();
		if(resp==null ||resp.getData().length<70) return "";
		byte AppendAdd[]=new byte[70];
		System.arraycopy(resp.getData(), 0, AppendAdd, 0, AppendAdd.length);
		try {
			if(resp.SwIs9000())
				return new String(AppendAdd, "UTF-16LE");	
			else if(resp.sw[2]==(byte)0x91){
				return "无追加住址";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 获取Samv模块编号
	* @Title getSamvIDStr 
	* @return
	 */
	public String getSamvIDStr(){
		CmdResponse resp=getSAMID();
		if(resp==null || !resp.SwIs9000()|| resp.getData().length<16) return "";
		return handleSamv(resp.getData());
	}
	
	public  String handleSamv(byte bsSamId[]){
		byte[] bs1 = new byte[4];
		byte[] bs2 = new byte[5];
		byte[] bs3 = new byte[5];
		byte[] bs4 = new byte[5];
		bs2[4] = 0;
		bs3[4] = 0;
		bs4[4] = 0;
		System.arraycopy(bsSamId, 0, bs1, 0, bs1.length);
		System.arraycopy(bsSamId, 4, bs2, 0, 4);
		System.arraycopy(bsSamId, 8, bs3, 0, 4);
		System.arraycopy(bsSamId, 12, bs4, 0, 4);
		//log.debug("last===========" + new String(bs4));
		BigInteger bi1 = new BigInteger(reverse(bs1));
		BigInteger bi2 = new BigInteger(reverse(bs2));
		BigInteger bi3 = new BigInteger(reverse(bs3));
		BigInteger bi4 = new BigInteger(reverse(bs4));

		String puzzBytes=String.format( "%1d%1d",bs1[3], bs1[2]);
		int puzzInt=Integer.parseInt(puzzBytes);

		String strSamId = String.format(
				"%1d%1d.%02d-%8d-%010d-%010d", bs1[1],
				bs1[0], puzzInt, bi2, bi3, bi4);


		/*String strSamId = String.format(
				"%1d%1d.%1d%1d-%8d-%010d-%010d", bs1[1],
				bs1[0], bs1[3], bs1[2], bi2, bi3, bi4);*/
		return strSamId;
		
	}
	
	private   byte[] reverse(byte[] b) {

		byte[] temp = new byte[b.length];
		for (int i = 0; i < b.length; i++) {
			temp[i] = b[b.length - 1 - i];
		}
		return temp;
	}
	
}
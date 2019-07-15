package com.synjones.cpucard;

import android.text.format.Time;

import com.synjones.cardutil.util;

/**
 * cpu卡操作APDU指令
* @ClassName CpuCard 
* @author zhaodianbo@Synjones
* @date 2014-8-26 下午4:08:56 
*
 */
public class CpuCardCmd{

    public  byte[] genEncryptedRandNum(byte[] key16, byte[] randomNum)
    {
    	try{
    		 if (key16 == null || randomNum == null || key16.length != 16 || randomNum.length != 4) return null;

    	        byte[] data2Encrypt = new byte[8];
    	       // randomNum.CopyTo(data2Encrypt, 0);
    	        System.arraycopy(randomNum, 0, data2Encrypt, 0, randomNum.length);


    	            //使用3des加密
    	        return DesECBUtil.encryptTripleDES(key16,data2Encrypt);//mDES.Encrypt3DES(key16, data2Encrypt);
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
       
      
    }
    
    

    public  boolean isSimpleKey(byte[] key16)
    {
        byte samekey = key16[0];
        for (int i = 1; i < 16; i++)
            if (samekey != key16[i])
                return false;
        return true;
    }
    //EXTERNAL AUTHENTICATE
    public  byte[] CmdExAuthEncrypted(byte keyID,byte[] key, byte[] randomNum)
    {
        byte[] EncryptedDatabuf = genEncryptedRandNum(key, randomNum);
        return CmdExAuth(keyID, EncryptedDatabuf);

/*        byte[] cmdhead={0x00,(byte) 0x82,0x00 ,0x00 ,0x08};
        byte[] buf = new byte[cmdhead.length + EncryptedDatabuf.length];
       // cmdhead.CopyTo(buf, 0);
        System.arraycopy(cmdhead, 0, buf, 0, cmdhead.length);
        buf[3] = keyID;        
        System.arraycopy(EncryptedDatabuf, 0, buf, cmdhead.length, EncryptedDatabuf.length);
         return buf;
        */     
    }
    
    public  byte[] CmdExAuth(byte keyID, byte[] encryptedRn)
    {

        byte[] cmdhead={0x00,(byte) 0x82,0x00 ,0x00 ,0x08};
        byte[] buf = new byte[cmdhead.length + encryptedRn.length];
       // cmdhead.CopyTo(buf, 0);
        System.arraycopy(cmdhead, 0, buf, 0, cmdhead.length);
        buf[3] = keyID;        
        System.arraycopy(encryptedRn, 0, buf, cmdhead.length, encryptedRn.length);

        return buf;
    }

    public  boolean IsCmdResponseOK(byte[] inData)
    {
        if (inData==null||inData.length < 3) return false;
        int len=inData.length;
        if (inData[len - 1] == 0x00 && inData[len - 2] == (byte)0x90)
            return true;
        return false;
    }
    //通过文件标示符或文件名称选择文件或目录//SELECT
    public  byte[] CmdSelect(byte way,byte[] data)
    {
        if (data == null || data.length == 0) return null;
        byte[] head = { 0x00, (byte) 0xa4, 0x00, 0x00, 0x00 };
        if (way == 0x0)//通过文件标示符选择
        {
            head[4] = 2;//lc=2, 文件标示符长度
            if (data.length != 2) return null;//data是文件标示符
        }
        else if (way == 0x04)//通过文件名称选择
        {
            head[2] = way;
            head[4] = (byte) data.length;//data是文件名
        }
        else
            return null;

        byte[] buf = new byte[head.length + data.length];
        //head.CopyTo(buf, 0);
        System.arraycopy(head,0,buf,0,head.length);
        System.arraycopy(data, 0, buf, head.length, data.length);
        return buf;
    }

    public  byte[] CmdCd2Root()
    {
        byte[] buf={0x00,(byte) 0xA4 ,0x00,0x00 ,0x02 ,0x3F ,0x00,0x00};
        return buf;
    }
    //GET CHALLENGE
    public  byte[] CmdGetChallenge(int le)
    {
        byte[] buf = { 0x00, (byte) 0x84, 0x00, 0x00, 0x04 };
        if(le==8)
        	buf[4]=8;
        return buf;
    }

    //READ BINARY
    /************************************************************************/
    /* 1.明文读
        CLA + INS + P1 + P2+要读的长度（00表示全读）
       2.带MAC读
        CLA + INS + P1 + P2+要读的长度+MAC       
     * 如果偏移量超过oxff  则需要先选择文件再读，否则直接用短文件标示符可读*/
    /************************************************************************/
    public  byte[] CmdReadBinary(byte fileID,int offset,byte wantedLen)
    {
        byte[] head = null;
        int headLen=0;
        byte[] P1P2=new byte[2];
        if(offset>0xff){//参数p1 p2代表偏移量
            P1P2[0]=(byte) (offset>>8);
            P1P2[1]=(byte) offset;
        }
        else
        {
            P1P2[0]=(byte)( (fileID & 0x1F) | 0x80 );
            P1P2[1]=(byte)offset;
        }

        headLen = 5;
        head = new byte[headLen];
        head[1] = (byte) 0xB0;
        //P1P2.CopyTo(head, 2);
        System.arraycopy(P1P2,0,head,2,P1P2.length);
        head[4] = wantedLen;            
        return head;
      
    }

    public  byte[] CmdReadBinaryMAC(byte fileID, int offset, byte wantedLen, byte[] RandomNum, byte[] key)
    {

        byte[] head = CmdReadBinary(fileID, offset, wantedLen);
        head[0] = 0x04;
        byte[] mac = genMAC(key, RandomNum, head);
        byte[] retData = new byte[head.length + mac.length];

        //head.CopyTo(retData, 0);
        System.arraycopy(head,0,retData,0,head.length);
        //mac.CopyTo(retData, head.length);
        System.arraycopy(mac,0,retData,head.length,mac.length);
        return retData;

    }

    //写二进制文件UPDATE BINARY
    /************************************************************************/
    /*         CLA+INS+P1+P2+Lc+Data
     *  00 D6 文件标示|偏移量+写长度+数据                                                                     */
    /************************************************************************/
    public  byte[] CmdWriteBinary(byte fileID, byte[]data,int offset)
    {
        if(data==null||data.length==0||offset>=0xff) return null;
        int MaxWriteLen=0xff-offset;
        int WriteLen=data.length>MaxWriteLen?MaxWriteLen:data.length;
        byte[] head = new byte[5 + WriteLen];
        head[1] = (byte) 0xd6;

        byte[] P1P2 = new byte[2];
        if (offset > 0xff)
        {//参数p1 p2代表偏移量
            P1P2[0] = (byte)(offset >> 8);
            P1P2[1] = (byte)offset;
        }
        else
        {
            P1P2[0] = (byte)((fileID & 0x1F) | 0x80);
            P1P2[1] = (byte)offset;
        }
        //P1P2.CopyTo(head, 2);
        System.arraycopy(P1P2,0,head,2,P1P2.length);
        head[4] =(byte) WriteLen;
        System.arraycopy(data, 0, head, 5, WriteLen);
        return head;
    }

    public  byte[] CmdWriteBinaryMAC(byte fileID, byte[] data, int offset, byte[] RandomNum, byte[] key)
    {
        byte[] head = CmdWriteBinary(fileID, data, offset);
        head[0] = 0x04;
        head[4] = (byte)(head[4] + 4);
        byte[] mac = genMAC(key, RandomNum, head);

        byte[] retData = new byte[head.length + mac.length];

        //head.CopyTo(retData, 0);
        System.arraycopy(head,0,retData,0,head.length);
       // mac.CopyTo(retData, head.length);
        System.arraycopy(mac,0,retData,head.length,mac.length);


        return retData;
    }

    public  byte[] genMAC(byte[] key16, byte[] randomNum, byte[] data)
    {
    	try{
    		if (key16 == null || key16.length != 16 || randomNum == null || randomNum.length == 0)
                return null;
            if (data == null || data.length == 0) return null;
            int datalen = data.length;
            int len = 0;
            byte[] buf = null;
            if (datalen % 8 == 0)
            {
                len = datalen + 8;
                buf = new byte[len];
                //data.CopyTo(buf, 0);
                System.arraycopy(data,0,buf,0,data.length);
                byte[] tail = { (byte) 0x80, 0, 0, 0, 0, 0, 0 ,0};
                //tail.CopyTo(buf, datalen);
                System.arraycopy(tail,0,buf,datalen,tail.length);
            }
            else
            {
                len = (datalen / 8 + 1) * 8;
                buf = new byte[len];
                //data.CopyTo(buf, 0);
                System.arraycopy(data,0,buf,0,data.length);
                buf[datalen] = (byte) 0x80;
            }

            byte[] leftKey = new byte[8];
            byte[] rightKey = new byte[8];
            System.arraycopy(key16, 0, leftKey, 0, 8);
            System.arraycopy(key16, 8, rightKey, 0, 8);
            byte[] initData = new byte[8];
          //  randomNum.CopyTo(initData, 0);
            System.arraycopy(randomNum,0,initData,0,randomNum.length);
            byte[] tmpdata;
            int index=0;
            tmpdata = xor8bytes(initData, buf,index);
            index+=8;
            while (index<len)
            {
               byte[] tmp= DesECBUtil.encryptDES(leftKey, tmpdata);
               tmpdata = xor8bytes(tmp, buf, index);
               index += 8;
            }
            byte[] tmp2 = DesECBUtil.encryptDES(leftKey, tmpdata);
            tmpdata = DesECBUtil.decryptDES(rightKey, tmp2);
            byte[] MAC = DesECBUtil.encryptDES(leftKey, tmpdata);
            byte[] Mac4Byte = new byte[4];
            System.arraycopy(MAC, 0, Mac4Byte, 0, 4);
            return Mac4Byte;
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
        
      
    }

    public  byte[] xor8bytes(byte[] ByteArray1, byte[] ByteArray2,int ByteArray2Offset)
    {
        byte[] ret = new byte[8];
        for (int i = 0; i < 8; i++)
        {
            ret[i]=(byte) (ByteArray1[i]^ByteArray2[i+ByteArray2Offset]);
        }
        return ret;
    }

    //用自身产生的8字节随机数进行内部认证
    //INTERNAL AUTHENTICATE
    public byte[] CmdInternalAuth(byte KeyId,byte[] data)
    {
        byte[] buf = new byte[13];
        try
        {           
            buf[1] = (byte) 0x88;
            buf[3] = KeyId;
            buf[4] = 8;
           // data.CopyTo(buf, 5);
            System.arraycopy(data,0,buf,5,data.length);
        }
        catch (Exception ex)
        {
            return null;
        }
        return buf;
       
    }

    public boolean handleInternalAuth(byte[] buf, byte[] num,byte[]key)
    {
    	boolean ret = false;
        try
        {
            if (!IsCmdResponseOK(buf) || buf.length != 10 || num.length != 8)
                return ret;
            byte[] EncryptedNum = new byte[8];
            System.arraycopy(buf, 0, EncryptedNum, 0, 8);
            byte[] DecryptedNum = DesECBUtil.decryptTripleDES(key, EncryptedNum);
            for (int i = 0; i < 8; i++)
                if (num[i] != DecryptedNum[i])
                    return ret;
            ret = true;
        }
        catch (Exception ex)
        {
        	
        }

        return ret;
    }
    
    
    
    public byte[] ReadRecord(byte fileId, byte RecordId, byte wantedLen)
    {
        byte[] buf = new byte[5];
        try
        {
            buf[1] = (byte) 0xB2;
            buf[2] = RecordId;//p1
            buf[3] =(byte) ((fileId<<3)|4);//p2
            buf[4] = wantedLen;

        }
        catch (Exception ex)
        {
            return null;
        }
        return buf;
    }

    public byte[] UpdateRecord(byte fileId, byte RecordId, byte[] data)
    {
        byte[] buf = new byte[5 + data.length];
        try
        {
            buf[1] = (byte) 0xDC;
            buf[2] = RecordId;//p1
            buf[3] = (byte)((fileId << 3) | 4);//p2
            buf[4] = (byte)data.length;
           // data.CopyTo(buf, 5);
            System.arraycopy(data, 0, buf, 0, 5);

        }
        catch (Exception ex)
        {
            return null;
        }
        return buf;
    }


    public byte[] AppendRecord(byte fileId, byte[] data)
    {
        byte[] buf = new byte[5 + data.length];
        try
        {
            buf[1] = (byte) 0xE2;
            buf[2] = 0;//p1
            buf[3] = (byte)((fileId << 3) | 4);//p2
            buf[4] = (byte)data.length;
            //data.CopyTo(buf, 5);
            System.arraycopy(data, 0, buf, 0, 5);
        }
        catch (Exception ex)
        {
            return null;
        }
        return buf;
    }
    
    
    /***********************************PBOC2.0钱包**************************************/
    
    public class Init4PurchaseResponse{
    	byte Balance[]=new byte[4];		//4
    	byte offlineTradeSn[]=new byte[2];		//2
    	byte Overdraft[]=new byte[3];	//3
    	byte keyVersionId;
    	byte ArithmeticID;
    	byte randomNum[]=new byte[4];	//4
    	
    	@Override
    	public String toString(){
    		return "Balance="+util.bytesToHexString(Balance)+"offlineTradeSn="+util.bytesToHexString(offlineTradeSn)+
    				"Overdraft="+util.bytesToHexString(Overdraft)+"keyVersionId="+keyVersionId+"ArithmeticID="+ArithmeticID+
    				"randomNum="+util.bytesToHexString(randomNum);
    	}
    }
    //initialize for purchase 
    public  byte[] Initialize4Purchase(byte Type,byte keyID,byte money[],byte TerminalSn[]){
    	byte buf[]=new byte[17];
    	buf[0]=(byte) 0x80;
    	buf[1]=0x50;
    	buf[2]=1;
    	buf[3]=Type;
    	buf[4]=0x0b;
    	buf[5]=keyID;
    	try {
    		System.arraycopy(money, 0, buf, 6, 4);
    		System.arraycopy(TerminalSn, 0, buf, 10, 6);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
    	buf[16]=0x0f;
    	return buf;  	
    }
    public class Data2GenMAC1{
		public byte[] randomNum=new byte[4];		//4 bytes
		public byte[] offlineTradeSn=new byte[2];	//2
		public byte[] money=new byte[4];			//4
		public byte tradeType;						//1
		public byte[] tradeDate=new byte[4];		//4
		public byte[] tradeTime=new byte[3];		//3
		public byte keyVersionId;		//1
		public byte ArithmeticID;		//1
		public byte[] appSn	=new byte[8];			//8
		public byte[] bankId=new byte[8];			//8
		public byte[] cityId=new byte[8];			//8
    	
    }
    
    //计算MAC1
    public  byte[] Init_SAM_For_Purchase(Data2GenMAC1 data){
    	int N=1;
    	if(data.cityId!=null)
    		N=3;
    	else if(data.bankId!=null)
    		N=2;   	
    	byte buf[]=new byte[5+0x14+8*N];
    	buf[0]=(byte) 0x80;
    	buf[1]=0x70;
    	buf[2]=0;
    	buf[3]=0;
    	buf[4]=(byte) (0x14+8*N);
    	try {  		
    		System.arraycopy(data.randomNum, 0, buf, 5, 4);
    		System.arraycopy(data.offlineTradeSn, 0, buf, 9, 2);   		
    		System.arraycopy(data.money, 0, buf, 11, 4);
    		buf[15]=data.tradeType;
    		System.arraycopy(data.tradeDate, 0, buf, 16, 4);
    		System.arraycopy(data.tradeTime, 0, buf, 20, 3);
    		buf[23]=data.keyVersionId;
    		buf[24]=data.ArithmeticID;
    		System.arraycopy(data.appSn, 0, buf, 25, 8);
    		if(data.cityId!=null){
    			System.arraycopy(data.bankId, 0, buf, 33, 8);
    			if(data.bankId!=null)
    				System.arraycopy(data.bankId, 0, buf, 41, 8);  	
    		}
        	
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
    	buf[5+0x14*N-1]=0x08;
    	return buf;
    }
    
    //DEBIT FOR CAPP PURCHASE 
    public  byte[] debit(byte TerminalTradeSn[],byte TradeDate[],byte TradeTime[],byte MAC1[]){
    	byte buf[]=new byte[21];
    	buf[0]=(byte) 0x80;
    	buf[1]=0x54;
    	buf[2]=1;
    	buf[3]=0;
    	buf[4]=0x0f;
    	try {  		
    		System.arraycopy(TerminalTradeSn, 0, buf, 5, 4);
    		System.arraycopy(TradeDate, 0, buf, 9, 4);
    		System.arraycopy(TradeTime, 0, buf, 13, 3);
    		System.arraycopy(MAC1, 0, buf, 16, 4);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
    	buf[20]=0x08;
    	return buf;
    }
    
    //校验MAC2
    public  byte[] CreditSAM4Purchase(byte MAC2[]){
    	byte buf[]=new byte[9];
    	buf[0]=(byte) 0x80;
    	buf[1]=0x72;
    	buf[2]=0;
    	buf[3]=0;
    	buf[4]=0x04;
    	try {  		
    		System.arraycopy(MAC2, 0, buf, 5, 4);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
    	return buf;
    }
    
    public  byte[] PsamGetResponse(byte len){
    	byte buf[]=new byte[5];
    	buf[1]=(byte) 0xc0;
    	buf[4]=len;
    	return buf;
    }
    
    public byte[] VerifyPin(String pin,int pinID,byte pucSource[]){
    	for(int i=pin.length();i<6;i++){
    		pin+="F";
    	}
    	byte pinbytes[]=null;
    	try {
    		pinbytes=util.HexString2Bytes(pin);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
    	
    	byte buf[]=new byte[8];
    	buf[1]=0x20;
    	buf[3]=(byte) pinID;
    	buf[4]=3;
    	ConvertPIN(pucSource, pinbytes);
    	System.arraycopy(pinbytes, 0, buf, 5, 3);

    	return buf;
    	
    }
    
    public void ConvertPIN(byte[] pucSource, byte[] pucPIN)
    {
    	pucPIN[0] = (byte) ((pucSource[0] ^ pucSource[3])&0x77);
    	pucPIN[1] = (byte) ((pucSource[1] ^ pucSource[4])&0x77);
    	pucPIN[2] = (byte) ((pucSource[2] ^ pucSource[5])&0x77);
    }

    
    
    public class InitLoadRet{
    	byte Money[]=new byte[4];
    	byte OnlineIndex[]=new byte[2];
    	byte KeyVer;
    	byte AlgrmID;
    	byte Random[]=new byte[4];
    	byte MAC1[]=new byte[4];
    	@Override
    	public String toString(){
    		return "Balance="+util.bytesToHexString(Money)+"OnlineIndex="+util.bytesToHexString(OnlineIndex)
    				+"keyVersionId="+KeyVer+"ArithmeticID="+AlgrmID+
    				"randomNum="+util.bytesToHexString(Random)+"MAC1="+util.bytesToHexString(MAC1);
    	}
    }
    
    public byte[]InitializeForLoad(int type,int keyId,byte money[],byte termianlSn[]){
    	byte buf[]=new byte[17];
    	buf[0]=(byte) 0x80;
    	buf[1]=0x50;
    	buf[2]=0 ;
    	buf[3]=(byte)type ;
    	buf[4]=0xb;
    	buf[5]=(byte) keyId;
    	System.arraycopy(money, 0, buf, 6, 4);
    	System.arraycopy(termianlSn, 0, buf, 10, 6);
    	return buf;
    }
    
    public byte[] load(byte date[],byte time[],byte MAC2[]){
    	byte buf[]=new byte[17];
    	buf[0]=(byte) 0x80;
    	buf[1]=0x52;
    	buf[2]=0 ;
    	buf[3]=0 ;
    	buf[4]=0xb;
    	System.arraycopy(date, 0, buf, 5, 4);
    	System.arraycopy(time, 0, buf, 9, 3);
    	System.arraycopy(MAC2, 0, buf, 12, 4);
    	return buf;
    }
    
    public byte[] InitForDescrypt(int keyType,int keyVer,byte data[]){
    	byte buf[]=new byte[5+data.length];
    	buf[0]=(byte) 0x80;
    	buf[1]=0x1a;
    	buf[2]=(byte) keyType;
    	buf[3]=(byte)keyVer ;
    	buf[4]=(byte) data.length;
    	System.arraycopy(data, 0, buf, 5, data.length);

    	return buf;
    }
    
    public byte[]Descrypt(int mode,byte[]data){
    	byte buf[]=new byte[5+data.length];
    	buf[0]=(byte) 0x80;
    	buf[1]=(byte) 0xfa;
    	buf[2]=(byte) mode;
    	buf[3]=0 ;
    	buf[4]=(byte) data.length;
    	System.arraycopy(data, 0, buf, 5, data.length);
    	return buf;
    }
    
    public byte[]getTransactionProve(int type,byte offlineTradeSn[]){
      	byte buf[]=new byte[8];
    	buf[0]=(byte) 0x80;
    	buf[1]=(byte) 0x5a;
    	buf[3]=(byte) type;
    	buf[4]=2;
    	buf[5]=offlineTradeSn[0];
    	buf[6]=offlineTradeSn[1];
    	buf[7]=0x08;
    	return buf;
    }
    
    public byte[]getBalance(int type){
      	byte buf[]=new byte[5];
    	buf[0]=(byte) 0x80;
    	buf[1]=(byte) 0x5c;
    	buf[3]=(byte) type;
    	buf[4]=0x04;
    	return buf;
    }
    
    
    public static byte[] getDate() {
		Time mytoday = new Time();
		mytoday.setToNow();				
		return util.HexString2Bytes(mytoday.format("%Y%m%d"));
	}
    
    public static byte[] getTime() {		
    	Time mytoday = new Time();
		mytoday.setToNow();				
		return util.HexString2Bytes(mytoday.format("%H%M%S"));	
	}
    
}
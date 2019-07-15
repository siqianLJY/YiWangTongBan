package com.example.litianci.yiwangtongban.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class util {

	
	private static final String TAG="SynjonesHandset";
	public static boolean DebugSwitch=false;
	public static  void DebugLog(String err){
		if(!DebugSwitch) return;
		Log.e(TAG, err);
	}
	public static String trimSpaces(String IP){//去掉IP字符串前后所有的空格 
        while(IP.startsWith(" ")){ 
               IP= IP.substring(1,IP.length()).trim(); 
            } 
        while(IP.endsWith(" ")){ 
               IP= IP.substring(0,IP.length()-1).trim(); 
            } 
        return IP; 
    } 
	public static boolean isIp(String IP){//判断是否是一个IP 
        boolean b = false; 
        IP = trimSpaces(IP); 
        if(IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){ 
            String s[] = IP.split("\\."); 
            if(Integer.parseInt(s[0])<255) 
                if(Integer.parseInt(s[1])<255) 
                    if(Integer.parseInt(s[2])<255) 
                        if(Integer.parseInt(s[3])<255) 
                            b = true; 
        } 
        return b; 
    }


	public static byte[] hexString2bytes(String src){
		try{
			if(src==null||src.length()==0) return null;
			int srcLen=src.length();
			if(srcLen%2!=0) {
				src += "0";
				srcLen++;
			}
			byte ret[]=new byte[srcLen/2];
			for(int i=0,j=0;i<=srcLen-2;i+=2,j++){
				String hex=src.substring(i,i+2);
				ret[j]=(byte)Integer.parseInt(hex,16);
			}
			return ret;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	//byte数据转换成string显示出来 比如byte数据12，转换为"12"
		public static String bytesToHexString(byte[] src){  
		    StringBuilder stringBuilder = new StringBuilder("");  
		    if (src == null || src.length <= 0) {  
		        return null;  
		    }  
		    for (int i = 0; i < src.length; i++) {  
		        int v = src[i] & 0xFF;  
		        String hv = Integer.toHexString(v);  
		        stringBuilder.append("0x");
		        if (hv.length() < 2) {  
		            stringBuilder.append(0);  
		        }  
		        
		        stringBuilder.append(hv.toUpperCase());  
		        stringBuilder.append(",");
		    }  
		    return stringBuilder.toString();  
		}
		
		public static String bytesToHexString(byte[] src,int len){  
		    StringBuilder stringBuilder = new StringBuilder("");  
		    if (src == null || src.length <= 0||src.length<len) {  
		        return null;  
		    }  
		    for (int i = 0; i < len; i++) {  
		        int v = src[i] & 0xFF;  
		        String hv = Integer.toHexString(v);  
		        stringBuilder.append("0x");
		        if (hv.length() < 2) {  
		            stringBuilder.append(0);  
		        }  
		        
		        stringBuilder.append(hv.toUpperCase());  
		        stringBuilder.append(",");
		    }  
		    return stringBuilder.toString();  
		}
		
		public static String bytesToHexStringNo0x(byte[] src){  
		    StringBuilder stringBuilder = new StringBuilder("");  
		    if (src == null || src.length <= 0) {  
		        return null;  
		    }  
		    for (int i = 0; i < src.length; i++) {  
		        int v = src[i] & 0xFF;  
		        String hv = Integer.toHexString(v);  
		        
		        if (hv.length() < 2) {  
		            stringBuilder.append(0);  
		        }  
		        
		        stringBuilder.append(hv.toUpperCase());  
		       // stringBuilder.append(" ");
		    }  
		    return stringBuilder.toString();  
		}
		
		public static String getToday() {
			try {
				Time mytoday = new Time();
				mytoday.setToNow();
				return mytoday.format("%Y-%m-%d");
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}

		public static String getNow() {
			try {
				Time mytoday = new Time();
				mytoday.setToNow();
				return mytoday.format("%H:%M:%S");
			} catch (Exception e) {
				e.printStackTrace();

				return "";
			}
		}

		public static String DatetimeString2DateString(String _strDatetime) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = sdf.parse(_strDatetime);
				SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
				String strDate = sdfDate.format(date);
				return strDate;
			} catch (Exception e) {
				e.printStackTrace();

				return "";
			}
		}

		public static String DatetimeString2TimeString(String _strDatetime) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = sdf.parse(_strDatetime);
				SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
				String strTime = sdfTime.format(date);
				return strTime;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}

		public static void viewDialog(Context context, String _strMsg) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("提示信息");
			builder.setMessage(_strMsg);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			builder.create().show();
		}

		public static void writeStr2SdcardFile(String fileName,String write_str) throws IOException{ 
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED))
			{
				File sdDir = Environment.getExternalStorageDirectory();
				//System.out.println(sdDir);
				try{ 
					   String fullname=sdDir.getCanonicalPath()+fileName;
				       FileOutputStream fout = new FileOutputStream(fullname); 
				       byte [] bytes = write_str.getBytes(); 
				
				       fout.write(bytes); 
				       fout.close(); 
				     }
				
				      catch(Exception e){ 
				        e.printStackTrace(); 
				       } 
			}
			else
			{
				//没有sd卡，需要使用内部空间
			}
			
		
		 }
		
		public static void appendGB2SdcardFile(String fileName,String write_str) { 
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED))
			{
				File sdDir = Environment.getExternalStorageDirectory();
				//System.out.println(sdDir);
				try{ 
					   String fullname=sdDir.getCanonicalPath()+fileName;
				       FileOutputStream fout = new FileOutputStream(fullname,true); 
				       OutputStreamWriter osw = new OutputStreamWriter(fout,"GB2312");
				       //byte [] bytes = write_str.getBytes(); 
				
				       osw.write(write_str); 
				       osw.close(); 
				       fout.close(); 
				     }
				
				      catch(Exception e){ 
				        e.printStackTrace(); 
				       } 
			}
			else
			{
				//没有sd卡，需要使用内部空间
			}	
		}
		
		
		
		public static void Beep(MediaPlayer mediaPlayer, Context context) {
			try {
				//
				if(mediaPlayer.isPlaying())
					mediaPlayer.stop();
				mediaPlayer.start();
			} catch (Exception e) {
				//log.debug("syn.Beep error");
				//log.debug(e.getMessage());
				e.printStackTrace();
			}

		}
}
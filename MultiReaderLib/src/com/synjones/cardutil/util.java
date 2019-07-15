package com.synjones.cardutil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;



public class util{
	
	private static  boolean LOG_SWTICH=true;
	
	public static void mSleep(long time){
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	 public static void closeBoard(Context mcontext) {
		  InputMethodManager imm = (InputMethodManager) mcontext
		    .getSystemService(Context.INPUT_METHOD_SERVICE);
		  // imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
		  if (imm.isActive())  //
		   imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
		     InputMethodManager.HIDE_NOT_ALWAYS);
		 }
	 
	 public static void closeBoard(Context mcontext,View view){
		 InputMethodManager imm = (InputMethodManager) mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
		 imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
		 imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
	 }
	
	public static void mPrintLog(Context context,String err){
		if(LOG_SWTICH)
			Log.e(context.getClass().getName(), err);
	}
	
	public static void mPrintLog(Class cla,String err){
		if(LOG_SWTICH)
			Log.e(cla.getName(), err);
	}
	public static void write2SdcardFile(String fileName,byte src[]) throws IOException { 
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			//File sdDir = Environment.getExternalStorageDirectory();
			//System.out.println(sdDir);
			try{ 
				  // String fullname=sdDir.getCanonicalPath()+fileName;
			       FileOutputStream fout = new FileOutputStream(fileName,false); 
			       //byte [] bytes = write_str.getBytes(); 
			
			       fout.write(src); 
			       fout.close(); 
			     }
			
			      catch(Exception e){ 
			        e.printStackTrace(); 
			       } 
		}
		else
		{
			throw new IOException("need SD card");
			//没有sd卡使用内部空间
		}
		
	
	 }
	
	
	 public static int saveData2File(Context mContext,String filename,byte in[]){	    	
	    	try {
				FileOutputStream outfile = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
				outfile.write(in);
				outfile.close();
				
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
	    	return 0;
	    }
	    
	    public static byte[] getDataInFile(Context mContext,String filename){
	    	try {
				FileInputStream showfile = mContext.openFileInput(filename);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] data = new byte[1024];
				int len = 0;
				
				while( (len=showfile.read(data))  != -1){
					out.write(data, 0, len);
				}
				
				return out.toByteArray();				
			} catch (Exception e) {				
				e.printStackTrace();
				return null;
				
			}
	    }
	
	
	
	public static void setLogSwtich(boolean flag){
		LOG_SWTICH=flag;
	}

	public static String trimSpaces(String IP){//
        while(IP.startsWith(" ")){ 
               IP= IP.substring(1,IP.length()).trim(); 
            } 
        while(IP.endsWith(" ")){ 
               IP= IP.substring(0,IP.length()-1).trim(); 
            } 
        return IP; 
    } 
	public static boolean isIp(String IP){//
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
	
	public static  byte uniteBytes(byte src0, byte src1) {
	    byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
	    _b0 = (byte)(_b0 << 4);
	    byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
	    byte ret = (byte)(_b0 ^ _b1);
	    return ret;
	  }
	
	public static  byte[] HexString2Bytes(String src){
	    byte[] ret = new byte[src.length()/2];
	    byte[] tmp = src.getBytes();
	    for(int i=0; i<src.length()/2; i++){
	      ret[i] = uniteBytes(tmp[i*2], tmp[i*2+1]);
	    }
	    return ret;
	  }
	
	//
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
		
		public static String bytesToHexStringNo0x(byte[] src,int len){  
		    StringBuilder stringBuilder = new StringBuilder("");  
		    if (src == null || src.length <len) {  
		        return null;  
		    }  
		    for (int i = 0; i < len; i++) {  
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
		
		
		public static String bytesToHexString(byte[] src,int len){  
		    StringBuilder stringBuilder = new StringBuilder("");  
		    if (src == null || src.length <= 0) {  
		        return null;  
		    }  
		    if(src.length<len) return null;
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
		public static String getBackupDate() {
			
			try {
				Time mytoday = new Time();
				mytoday.setToNow();				
				return  mytoday.format("%Y%m%d%H%M%S");
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
		
		/**
		 * 
		 * @param ?
		 * @return 该毫秒数转换?* days * hours * minutes * seconds 后的格式
		 * @author fy.zhang
		 */
		public static String formatDuring(long mss) {
			long days = mss / (1000 * 60 * 60 * 24);
			long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
			long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
			long seconds = (mss % (1000 * 60)) / 1000;
			return String.format("%02d:%02d:%02d",hours,minutes,seconds );
			/*return days + " days " + hours + ":" + minutes + ":"
					+ seconds + "";*/
		}
		
		public static String formatDuringm(long mss) {
			long days = mss / (1000 * 60 * 60 * 24);
			long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
			long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
			long seconds = (mss % (1000 * 60)) / 1000;
			return String.format("%02d:%02d:%02d.",hours,minutes,seconds)+(mss%1000);
			/*return days + " days " + hours + ":" + minutes + ":"
					+ seconds + "";*/
		}
		/**
		 * 
		 * @param begin 时间段的
		 * @param end	时间段的结束
		 * @return	输入的两个Date类型数据之间的时间间格用* days * hours * minutes * seconds的格式展?
		 * @author fy.zhang
		 */
		public static String formatDuring(Date begin, Date end) {
			return formatDuring(end.getTime() - begin.getTime());
		}


		/*public static void viewDialog(Context context, String _strMsg) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("提示");
			builder.setMessage(_strMsg);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			builder.create().show();
		}*/
		

	    
		 public static String getVersionName(Context ctx) 
		    {
		            // 获取packagemanager的实?
		            PackageManager packageManager = ctx.getPackageManager();
		            // getPackageName()
		            PackageInfo packInfo;
		            String version="未知";
					try {
						packInfo = packageManager.getPackageInfo(ctx.getPackageName(),0);
						 version = packInfo.versionName;
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            
		            return version;
		    }
		 
		 
		 /**
		     * 获取手机内部剩余存储空间
		     * @return
		     */
		    public static long getAvailableInternalMemorySize() {
		        File path = Environment.getDataDirectory();
		        StatFs stat = new StatFs(path.getPath());
		        long blockSize = stat.getBlockSize();
		        long availableBlocks = stat.getAvailableBlocks();
		        return availableBlocks * blockSize/1024/1024;
		    }
		    
		    /**
		     * SDCARD是否
		     */
		    public static boolean externalMemoryAvailable() {
		        return android.os.Environment.getExternalStorageState().equals(
		                android.os.Environment.MEDIA_MOUNTED);
		    }
		    /**
		     * 获取SDCARD剩余存储空间
		     * @return
		     */
		    public static long getAvailableExternalMemorySize() {
		        if (externalMemoryAvailable()) {
		            File path = Environment.getExternalStorageDirectory();
		            StatFs stat = new StatFs(path.getPath());
		            long blockSize = stat.getBlockSize();
		            long availableBlocks = stat.getAvailableBlocks();
		            return availableBlocks * blockSize/1024/1024;
		        } else {
		            return -1;
		        }
		    }
		   
		    public static  AlertDialog SetParaDialog(Context mContext,String title,View v,DialogInterface.OnClickListener  di,DialogInterface.OnClickListener cancel) {

		        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		        builder.setTitle(title).setView(v).setNegativeButton("取消", cancel).setCancelable(false);;		        
		        builder.setPositiveButton("确定",di);
		    	return  builder.show();		    
		    }
		    

		
	
		public static String getApplicationName(Context ctx) { 
			PackageManager packageManager = null; 
			ApplicationInfo applicationInfo = null; 
			String applicationName=null;
			try { 
				packageManager = ctx.getApplicationContext().getPackageManager(); 
				applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), 0); 
				applicationName=(String) packageManager.getApplicationLabel(applicationInfo); 
			} catch (Exception e) { 
				e.printStackTrace();	
			} 
					 
			return applicationName; 
		} 
		
		public static int byteArray2int(byte array[],int offset)
		{
			return array[offset]&0xff|(array[offset+1]<<8)&0xff00|(array[offset+2]<<16)&0xff0000|(array[offset+3]<<24)&0xff000000;
		}
		
		public static int byteArray2Bigint(byte array[],int offset)
		{
			return array[offset+3]&0xff|(array[offset+2]<<8)&0xff00|(array[offset+1]<<16)&0xff0000|(array[offset]<<24)&0xff000000;
		}
		
		public static void int2byteArray(int num,byte array[],int offset)
		{
			array[offset]  =(byte) (num&0xff);
			array[offset+1]=(byte) ((num>>8)&0xff);
			array[offset+2]=(byte) ((num>>16)&0xff);
			array[offset+3]=(byte) ((num>>24)&0xff);
		}
		
		public static void int2BigbyteArray(int num,byte array[],int offset)
		{
			array[offset+3]  =(byte) (num&0xff);
			array[offset+2]=(byte) ((num>>8)&0xff);
			array[offset+1]=(byte) ((num>>16)&0xff);
			array[offset]=(byte) ((num>>24)&0xff);
		}
		

		
		public static int IndexOf(byte[] s, byte[] pattern)
		    {
			try {
				  int slen = s.length;
			        int plen = pattern.length;
			        boolean researchFlag=false;
			        for (int i = 0; i <= slen - plen; i++)
			        {
			        	researchFlag=false;
			            for (int j = 0; j < plen; j++)
			            {
							if (s[i + j] != pattern[j]) {
								researchFlag=true;
								break;
							}
			            }
			            if(researchFlag==false)
			            	return i;
			        }
			} catch (Exception e) {
				// TODO: handle exception
			}
		      
		        return -1;
		    }
		
		public static int IndexOf(byte[] s, byte[] pattern,int from)
	    {
	        int slen = s.length;
	        int plen = pattern.length;
	        boolean researchFlag=false;
	        for (int i =from; i <= slen - plen; i++)
	        {
	        	researchFlag=false;
	            for (int j = 0; j < plen; j++)
	            {
					if (s[i + j] != pattern[j]) {
						researchFlag=true;
						break;
					}
	            }
	            if(researchFlag==false)
	            	return i;
	        }
	        return -1;
	    }

	public static byte[] bitmap2ByteArray(Bitmap photo) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try{
			photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			return baos.toByteArray();
		}finally {
			try {
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
package com.synjones.idcard;

import android.content.Context;

public interface IDecodeWlt {
	byte[] decode(Context ctx, byte[] wlt);
}


/*

package com.synjones.idcard;

import java.io.File;
import java.io.FileInputStream;

import android.content.Context;

import com.cvr.cvr100u_demo.BaseData;
import com.ivsign.android.IDCReader.IDCReaderSDK;

public class DecodeWlt implements IDecodeWlt{
	private static final byte[] byLicData = { 5, 0, 1, 0, 91,
			3, 51, 1, 90, -77, 30 };
	@Override
	public byte[] decode(Context ctx,byte[] wlt) {
		// TODO Auto-generated method stub
		BaseData.load(ctx);

		String bmpPath = ctx.getFileStreamPath("zp.bmp").getAbsolutePath();
		DeleteFile(bmpPath);
		String wltPath = ctx.getFileStreamPath("zp.wlt").getAbsolutePath();
		DeleteFile(wltPath);

		////byte[] wlt_ok=FileOperate.readAllSdcardFile("/wlt.wlt");
		//log.error("wlt");
		//fos.write(wlt.img);
		IDCReaderSDK.wltInit(ctx.getFilesDir().getAbsolutePath());
		IDCReaderSDK.wltGetBMP(wlt,byLicData);

		//int result = dw.Wlt2Bmp(this,wlt, bmpPath);
		//int result = dw.Wlt2BmpWrap(wltPath, bmpPath);
		byte [] buffer =null;
		FileInputStream fin;
		try {
			File bmpFile = new File(bmpPath);
			fin = new FileInputStream(bmpFile);
			int length = fin.available();
			buffer=new byte[length];
			fin.read(buffer);
			fin.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer;
	}

	private void DeleteFile(String filename) {
		File file = new File(filename);
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			}
		}
	}

}*/


/*

package com.cvr.cvr100u_demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class BaseData {
	public static final byte data[]={31, -117, 8, 0, 0, 0, 0, 0, 0, 0, -67, -107, 75, 75, -43, 81, 20, -59, -9, 85, 51, -45, -20, -86, -103, -102, 105, -102, 122, -77, -121, 105, 62, -14, -3, -86, -52, 124, 63, 42, 45, -51, 76, -77, -89, 86, 58, 8, 34, 8, -126, -96, 6, 13, 106, 24, 68, -109, -96, -127, 18, 4, 65, 20, 125, -122, -120, 38, 13, 44, -88, 104, 32, 5, 65, 81, 80, 4, 77, 90, -5, -82, 117, 73, -5, 0, 45, -40, 108, 126, -9, -20, -1, 57, 103, -81, 125, -48, -20, 97, -77, -128, -3, -115, -21, 33, -77, 85, -56, 81, 70, -91, 38, -101, 37, 33, 71, -117, 19, -30, -52, 82, -112, 99, -60, -13, 88, 72, 69, 94, 38, 126, -125, 77, -46, -111, 99, -59, 11, -120, -75, -120, -27, -30, 105, 68, 22, 34, 78, 92, -119, 88, -113, 88, 33, -50, 68, 108, 64, -60, -117, 19, 17, -7, 126, -82, 56, 86, -75, 43, -59, -47, -86, 77, 20, 7, -76, 22, -31, 43, -24, 47, 104, 12, 95, 11, 53, -103, 21, 24, 123, 114, 125, 47, 50, -37, -120, -100, 44, -66, -100, 107, 86, 104, -20, -47, -11, 12, -51, 108, 66, 94, 45, -66, -118, -62, 45, -58, -98, 93, 95, 113, -40, 86, -28, 53, -30, 123, -72, -36, 54, -28, 52, 113, 9, 26, 47, 54, 122, -30, 26, -127, 81, -37, -111, 51, 34, -3, -64, -56, 82, 121, 20, -18, 7, 13, -107, -53, 7, 87, 51, 6, 81, -127, -68, 78, -4, 40, 64, -49, -78, -60, 45, -32, 106, -28, 108, -15, 15, 68, -115, -47, 83, -41, 83, 68, 29, 34, 71, 124, 3, 81, -113, -56, 21, -97, 70, 52, 26, 61, 119, -11, 32, 96, -111, -27, -119, -21, 84, -101, 47, 46, 82, 109, 100, 94, 31, -32, 47, -98, 76, 56, -36, -33, -82, 54, -36, -39, -24, -87, 43, -79, -42, 108, -89, -47, 83, -41, 55, -104, -79, -37, -24, -87, 43, 7, -123, 45, -56, -101, -59, -27, -72, -24, 30, -93, -57, -82, 89, 24, -45, 106, -12, -40, -107, 1, -93, -37, 116, 15, -41, 69, -52, -93, -35, -24, -71, -21, 2, 30, 111, -121, -47, 115, -41, 111, 60, -100, 78, -28, 18, -15, 47, 92, -68, -37, 56, 3, -41, 120, 28, 123, 46, 21, -57, 98, 94, -67, -56, 101, -30, 23, -102, 87, -71, -8, 93, 12, -41, 42, -60, 65, -15, 14, -15, -124, -26, 87, 41, 126, -91, -7, 85, -119, 71, -94, 88, 91, 29, -71, -113, -26, 89, 35, -66, 31, 96, 109, -83, 120, 76, -13, -83, 19, -121, 2, -84, -83, 23, -1, 84, 109, -125, -8, -71, 106, 27, -59, 15, -76, -42, 36, -66, 37, -50, 21, -89, 28, -26, 124, 60, 124, 126, 49, 48, -89, 15, 121, -105, -42, -117, -80, -48, 111, -100, -103, 107, 30, -105, -37, 103, -100, -103, -21, 19, -116, -35, 111, -100, -103, -85, 30, -125, 60, 96, -100, -103, -21, 18, 30, -50, 32, -14, 94, -15, 123, 60, -52, -125, -58, 25, -70, -26, 50, -55, -19, -30, -98, 116, -82, 117, -120, 11, 82, -55, -99, -30, -69, -55, 92, -21, 18, 79, 5, -55, -35, -30, -2, 68, -42, -10, -120, -97, 36, 112, -83, 87, -36, 28, -65, 116, -2, 101, -121, -40, 111, -65, -6, -49, -62, 34, 126, 10, -9, -24, -70, -119, -58, -121, -116, 61, -70, -34, -62, 92, 60, -7, 112, -113, -82, 47, 24, 46, 44, -76, 1, -15, 67, 60, -68, 17, 99, -49, -82, 59, -16, -29, -120, -79, 71, 87, 90, 104, -87, 31, 51, 3, 60, 111, 72, -25, -49, -95, -79, -93, -58, 51, 92, -81, 97, -12, -104, -15, 12, 87, 30, 6, 59, 110, 60, 35, -20, 15, 30, -57, -124, -15, 12, -41, -29, 50, -14, -88, 56, -67, -104, 107, -111, -5, -52, 14, 114, -1, 49, -99, -41, 10, 99, 78, 24, -9, 116, 45, -32, 98, 39, -111, -113, -119, 95, 98, -2, -89, -116, 123, -70, 26, -22, -7, 55, -29, -72, 120, -76, 106, -23, -7, -91, 48, -18, -116, 113, 15, 87, 16, -3, 76, 26, -9, 112, -43, -32, 97, 76, 25, -9, 8, 127, -1, -49, -2, -123, -125, -4, 126, 82, -9, -5, -120, -31, -100, 53, 126, -29, -70, -122, -31, -98, 91, -76, -33, 109, -8, 119, -34, -8, -101, -85, -73, -113, -1, 99, 34, -11, 73, -61, 92, -97, -42, 126, 121, -88, -97, 89, 84, -1, 121, -120, 28, 89, -1, -33, -6, 3, 17, 122, -77, -120, -128, 7, 0, 0};
	public static final byte li[]={0x76 ,(byte) 0xBB ,(byte) 0xDE ,0x36 ,0x33 ,(byte) 0xF5 ,(byte) 0xAE ,0x47 ,0x35 ,(byte) 0x9E ,0x2B ,0x3C ,0x7E ,(byte) 0xFB ,(byte) 0xBC ,0x3A ,(byte) 0xA1 ,(byte) 0xAF ,0x4D ,0x71 ,0x2E ,0x7C ,0x61 ,(byte) 0xD3 ,(byte) 0xDB ,(byte) 0xA0 ,0x03 ,(byte) 0xC6 ,(byte) 0x90 ,0x40 ,(byte) 0xC0 ,(byte) 0xAB};
	public static int  load(Context context){
		try {
			String dirPath=context.getFilesDir().getAbsolutePath();
			String baseFilePath=dirPath+"/base.dat";
			String liFilePath=dirPath+"/license.lic";
			String libPath=dirPath+"/libjm.so";
			File f=new File(baseFilePath);
			if(!f.exists()){
				byte uncompressedBase[]=uncompress(data);
				writeByte2SdcardFile(baseFilePath, uncompressedBase);
			}
			f=new File(liFilePath);
			if(!f.exists()){
				writeByte2SdcardFile(liFilePath, li);
			}

			f=new File(libPath);
			if(!f.exists()){
				copyFromAssert(context,"face1.dat",libPath);
			}
			System.load(libPath);
			Log.e("TEST", "load data");
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}

	 public static boolean writeByte2SdcardFile(String fileName,byte write_byte[]){
	        boolean ret=false;
	        try{
	            FileOutputStream fout = new FileOutputStream(fileName);
	            fout.write(write_byte);
	            fout.flush();
	            fout.close();
	            ret=true;
	        }
	        catch(Exception e){
	            e.printStackTrace();
	        }
	        return ret;
	    }


	   private static boolean copyFromAssert(Context context,String Srcpath,String destPath)
	    {
	        InputStream inputStream = null;
	        AssetManager assetManager = context.getAssets();
	        try {
	            inputStream = assetManager.open(Srcpath);
	            unzipLib(inputStream,destPath);
	            inputStream.close();
	        } catch (Exception e) {
	            return false;
	        }
	        return true;
	    }


	    public static void unzipLib(InputStream zipFileName, String destPath) {
	        try {
	            ZipInputStream in = new ZipInputStream(zipFileName);
	            // 获取ZipInputStream中的ZipEntry条目，一个zip文件中可能包含多个ZipEntry，
	            // 当getNextEntry方法的返回值为null，则代表ZipInputStream中没有下一个ZipEntry，
	            // 输入流读取完成；
	            ZipEntry entry = in.getNextEntry();
	            if (entry != null) {
	                // 创建以zip包文件名为目录名的根目录
	             File file = new File(destPath);
                 file.createNewFile();
                 FileOutputStream out = new FileOutputStream(file);
                 int b;
                 while ((b = in.read()) != -1) {
                     out.write(b);
                 }
                 out.close();
	                // 读取下一个ZipEntry
	               // entry = in.getNextEntry();
	            }
	            in.close();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO 自动生成 catch 块
	            e.printStackTrace();
	        }
	    }

	    public static byte [] readAllSdcardFile(String fileName){
	        // String res="";
	        byte [] buffer = null;

	        try{
	            FileInputStream fin = new FileInputStream(fileName);
	            int length = fin.available();
	            buffer = new byte[length];
	            fin.read(buffer);
	            fin.close();
	        }
	        catch(Exception e){
	            e.printStackTrace();
	            return null;
	        }
	        return buffer;
	    }

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

	    public static byte[] compress(byte bytesIn[]) throws IOException {
	        if (bytesIn == null || bytesIn.length == 0) {
	          return null;
	        }
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        GZIPOutputStream gzip = new GZIPOutputStream(out);
	        gzip.write(bytesIn);
	        gzip.close();
	        return out.toByteArray();
	      }

	      // 解压缩
	      public static byte[] uncompress(byte bytesIn[]) throws IOException {
	    	  if (bytesIn == null || bytesIn.length == 0) {
		          return null;
		        }
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        ByteArrayInputStream in = new ByteArrayInputStream(bytesIn);
	        GZIPInputStream gunzip = new GZIPInputStream(in);
	        byte[] buffer = new byte[256];
	        int n;
	        while ((n = gunzip.read(buffer)) > 0) {
	          out.write(buffer, 0, n);
	        }
	        return out.toByteArray();
	      }

}



*/


/*

package com.ivsign.android.IDCReader;

public class IDCReaderSDK {

	 public static native int wltInit(String paramString);
	 public static native int wltGetBMP(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
}


*/

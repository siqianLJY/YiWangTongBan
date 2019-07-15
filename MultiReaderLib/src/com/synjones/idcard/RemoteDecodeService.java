package com.synjones.idcard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.synjones.bluetooth.DecodeWlt;
import com.synjones.cardutil.util;
import com.synjones.idcard.DecodeAIDLService.Stub;

import dalvik.system.DexClassLoader;


public class RemoteDecodeService extends Service {
		public static final String TAG = "PhotoDecodeService";
		Class clz;
		private DecodeAIDLService.Stub mBinder = new Stub() {
			@Override
			public byte[] decode(byte[] wlt) throws RemoteException {
				String bmpPath = getFileStreamPath("photo.bmp").getAbsolutePath();
				String wltPath = getFileStreamPath("photo.wlt").getAbsolutePath();
				File wltFile = new File(wltPath);
				DeleteFile(bmpPath);
				DeleteFile(wltPath);
				try {
					FileOutputStream fos = new FileOutputStream(wltFile);
					fos.write(wlt);
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				////byte[] wlt_ok=FileOperate.readAllSdcardFile("/wlt.wlt");
				//log.error("wlt");
				//fos.write(wlt.img);
				
				DecodeWlt dw = new DecodeWlt();
				
				int result = dw.Wlt2Bmp(wltPath, bmpPath);
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

			@Override
			public byte[] decode2(byte[] wlt) throws RemoteException {

				copyJmTools(getApplicationContext());
				File dexOutputDir = getDir("dex1", 0);
				String dexPath = Environment.getExternalStorageDirectory().toString() + File.separator + "/synjones/idcard/tools/jm.jar";
				DexClassLoader loader = new DexClassLoader(dexPath,
						dexOutputDir.getAbsolutePath(),
						null, getClassLoader());

				byte img[]=null;
				try {
					if(clz==null){
						clz = loader.loadClass("com.synjones.idcard.DecodeWlt");
						IDecodeWlt impl = (IDecodeWlt) clz.newInstance();
						// BaseData.load(MainActivity.this);
						img=impl.decode(getApplicationContext(),wlt);
					}
				} catch (Exception e) {
					Log.d(TAG, "decode2 error,"+e.getMessage());
				}

				//byte img[]=decode(wlt);

				return img;
			}
		};
/*
	public static byte[] jm(Context ctx,byte in[]){
		copyJmTools(ctx);
		File dexOutputDir = ctx.getDir("dex1", 0);
		String dexPath = Environment.getExternalStorageDirectory().toString() + File.separator + "/synjones/idcard/tools/jm.jar";
		DexClassLoader loader = new DexClassLoader(dexPath,
				dexOutputDir.getAbsolutePath(),
				null, ctx.getClassLoader());

		byte img[]=null;
		try {
			if(clz==null){
				clz = loader.loadClass("com.synjones.idcard.DecodeWlt");
				IDecodeWlt impl = (IDecodeWlt) clz.newInstance();
				// BaseData.load(MainActivity.this);
				img=impl.decode(ctx,in);
			}
		} catch (Exception e) {
			Log.d("TEST111", "error happened", e);
		}

		//byte img[]=decode(wlt);

		return img;
	}*/

	private  static  boolean copyJmTools(Context ctx)
	{
		String ex= Environment.getExternalStorageDirectory().getPath();
		String jmLibPath=ex+"/synjones/idcard/tools/jm.jar";
		InputStream inputStream = null;
		AssetManager assetManager = ctx.getAssets();
		byte inDb[]=null;
		try {
			inputStream = assetManager.open("base1.dat");
			inDb=new byte[inputStream.available()];
			inputStream.read(inDb);
			inputStream.close();
			util.write2SdcardFile(jmLibPath,inDb);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private void DeleteFile(String filename) {
		File file = new File(filename);
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			}
		}
	}
		
		@Override
		public void onCreate() {
			super.onCreate();
			Log.d(TAG, "onCreate() executed");
		}
		
		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			Log.d(TAG, "onStartCommand() executed");
			return super.onStartCommand(intent, flags, startId);
		}
		
		@Override
		public void onDestroy() {
			super.onDestroy();
			Log.d(TAG, "onDestroy() executed");
		}
		
		@Override
		public IBinder onBind(Intent intent) {
			return mBinder;
		}
		


}
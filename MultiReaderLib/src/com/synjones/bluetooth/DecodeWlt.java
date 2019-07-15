package com.synjones.bluetooth;
/**
 *  照片解码库
* @ClassName DecodeWlt 
* @author zhaodianbo@Synjones
* @date 2013-12-23 下午12:39:26 
*
 */
public class DecodeWlt {
	public native int Wlt2Bmp(String wltPath, String bmpPath);
	//private static final Logger log = LoggerFactory.getLogger();
	static {
		try {
			System.loadLibrary("DecodeWlt");
		} catch (UnsatisfiedLinkError e) {
			e.printStackTrace();
			//util.DebugLog(e.getMessage().toString());
		}
	}
}

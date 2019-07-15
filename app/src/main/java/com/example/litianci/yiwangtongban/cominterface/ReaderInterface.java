package com.example.litianci.yiwangtongban.cominterface;

import android.app.Activity;
import android.os.Handler;

/**
 * 读卡器通用接口，读卡器和主程序之间使用handler通信
* @ClassName ReaderInterface 
* @author zhaodianbo@Synjones
* @date 2015-7-16 下午3:41:21 
*
 */
public  interface ReaderInterface {
	/**
	 * 初始化读卡器
	* @Title setContextAndHandler
	* @param act
	 */
     void setActivityAndHandler(Activity act, Handler mHandler);
	
	/**
	 * 释放读卡器
	* @Title release
	 */
     void release();
	
	/**
	 * 打开读卡器，请求对应的硬件资源，并开启读卡线程
	* @Title open
	 */
     boolean open();

	/**
	 * 关闭读卡线程，释放资源
	* @Title close
	 */
     void close();

     boolean isOpen();

     DataTransChannel getDataTransChannel();

	 String  getDevName();

}

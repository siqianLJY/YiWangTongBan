package com.synjones.multireaderlib;
/**
 * android设备和读卡器数据通信接口  根据android设备的接口替换，如USB、串口等
* @ClassName DataTransInterface 
* @author zhaodianbo@Synjones
* @date 2014-5-16 下午3:31:14 
*
 */
public interface DataTransInterface{
	/**
	 * 发送数据接口
	* @Title sendData 
	* @param data 待发送的数据
	* @param datalen 数据长度
	 */
	void sendData(byte[]data, int datalen);
	/**
	 * 接收数据接口
	* @Title recvData 
	* @param recvbuf 接收buf
	* @param offset buf内的偏移量
	* @return 接收到的长度
	 */
	int recvData(byte recvbuf[],int offset);
	/**
	 * 清空接收缓存
	* @Title clear
	 */
	void clear();
}
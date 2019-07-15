package com.example.litianci.yiwangtongban.cominterface.otg;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Environment;
import android.util.Log;

import com.synjones.multireaderlib.DataTransInterface;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Created by zhaodianbo on 1/15.
 */

public class UsbInterfaceAdapter implements DataTransInterface {
    /**
     * 版本标志
     */
    public static String VERSION = "1.1";

    /**
     * 调试标志
     */
    public static String TAG = "XZXUsbReader";
    /**
     * 权限标志
     */
    public static final String ACTION_SYNJONES_USB_PERMISSION = "com.synjones.hid.USB_PERMISSION";

    /**
     * 卸载USB设备
     */
    public static final String ACTION_USB_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";

    // 超时
    private int TimeOut = 100;
    private int transferTimeOut = TimeOut;
    private int readTimeOut = TimeOut;
    private int writeTimeOut = TimeOut;

    private Context context;

    private UsbManager usbManager;
    private UsbDevice usbDevice;
    private UsbDeviceConnection usbDeviceConnection;
    private UsbInterface usbInterface;
    private UsbEndpoint uein;
    private UsbEndpoint ueout;
    private boolean opened = false;

    // 收发器属性
    private static final int MAX_SENDLEN = 1;
    private static final int SECVBUF_LEN = 4096;
    private static final int SENDBUF_LEN = MAX_SENDLEN;

    // 缓冲区
    private byte[] recv_buf = new byte[SECVBUF_LEN];
    private byte[] send_buf = new byte[SENDBUF_LEN];
    private long receivecount = 0, sendcount = 0;

    private ArrayList<String> Supported_VID_PID = new ArrayList();

    private Logger gLogger;


    public UsbInterfaceAdapter(Context context) {
        this.context = context;
        this.usbManager = (UsbManager) this.context.getSystemService(Context.USB_SERVICE);
        Supported_VID_PID.add("0316:5020");
        Supported_VID_PID.add("0400:C35A");
        configLog();
    }

    public UsbInterfaceAdapter(Context context, UsbDevice device) {
        this(context);
        if (!isSupportedDevice(device)) {
            log(getDeviceString(device) + "may is not a supported device");
        }
        this.usbDevice = device;
    }

    public String getDevName(){
        if(usbDevice==null) return "";
        return usbDevice.getDeviceName();
    }


    public void configLog()
    {
        LogConfigurator logConfigurator = new LogConfigurator();
        logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + "usbOtg.txt");
        // Set the root log level
        logConfigurator.setRootLevel(Level.DEBUG);
        // Set log level of a specific logger
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.configure();

        //gLogger = Logger.getLogger(this.getClass());
        gLogger = Logger.getLogger("OtgTest");
    }
    private void log(String log){
        Log.e(TAG, log);
        //gLogger.error(log);
    }


    /**
     * 判断是否是支持的USB设备类型，即是否是我司设备
     *
     * @param device
     *            USB设备
     * @return true表示支持,false表示不支持
     */
    public  boolean isSupportedDevice(UsbDevice device) {
        //return device.getProductId() == PL2303HXA_PRODUCT_ID;
        Iterator<String> iterator=Supported_VID_PID.iterator();
        while(iterator.hasNext()){
            if(getDeviceVIDPID(device).equals(iterator.next()))
                return true;
        }
        return false;
    }

    /**
     * 获取所有支持的设备，即获取所有的usb设备
     *
     * @return 设备列表
     */
    public  List<UsbDevice> getAllSupportedDevices() {
        List<UsbDevice> res = new ArrayList<UsbDevice>();
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            boolean flag = isSupportedDevice(device);
            log(getDeviceString(device) + (flag ? "" : " not support!"));
            if (flag)
                res.add(device);
        }
        return res;
    }

    /**
     * 获取设备描述字符串
     *
     * @param device
     *            设备
     * @return 描述字符串
     */
    public static String getDeviceString(UsbDevice device) {
        return String.format("DID:%d VID:%04X PID:%04X",
                            new Object[] {  Integer.valueOf(device.getDeviceId()),
                                            Integer.valueOf(device.getVendorId()),
                                            Integer.valueOf(device.getProductId()) });
    }
    private  String getDeviceVIDPID(UsbDevice d) {
        return String.format("%04X:%04X", new Object[] {Integer.valueOf(d.getVendorId()),
                                                        Integer.valueOf(d.getProductId()) });
    }
    /**
     * 获取USB设备
     *
     * @return USB设备
     */
    public UsbDevice getUsbDevice() {
        return usbDevice;
    }



    /**
     * 获取设备ID
     *
     * @return 设备ID
     */
    public int getDeviceID() {
        return this.usbDevice.getDeviceId();
    }

    /**
     * 转换为字符串描述
     *
     * @return 描述字符串
     */
    public String toString() {
        return String.format("DeviceID:%d VendorID:%04X ProductID:%04X R:%d T:%d", new Object[] { Integer.valueOf(usbDevice.getDeviceId()), Integer.valueOf(usbDevice.getVendorId()), Integer.valueOf(usbDevice.getProductId()), receivecount, sendcount });
    }

    /**
     * 获取设备名称
     *
     * @return 设备名词
     */
    public String getName() {
        return String.format("usbDeviceID_%d", usbDevice.getDeviceId());
    }

    /**
     * 检查权限，如果没有权限则请求授权
     *
     * @return true 有权限, false 无权限，并开始请求授权
     */
    public boolean checkPermission() {
        return checkPermission(true);
    }

    /**
     * 检查权限
     *
     * @param autoRequest
     *            没有权限的时候是否自动请求权限,true自动请求,false不自动请求
     * @return true 有权限, false 无权限，并开始请求授权
     */
    public boolean checkPermission(boolean autoRequest) {
        if (!usbManager.hasPermission(usbDevice)) {
            log( "no permission");
            if (autoRequest) {
                log( "requesting permission");
                PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_SYNJONES_USB_PERMISSION), 0);
                usbManager.requestPermission(usbDevice, mPermissionIntent);
            }
            return false;
        }
        log( "permission true");
        return true;
    }



    /**
     * 打开设备
     *打开失败异常，如果未授权或不支持的设置参数
     */
    public boolean open() {

        close();
        List<UsbDevice> devList=getAllSupportedDevices();
        if(devList.size()<=0){
            log("usb device not find");
            return false;
        }
        usbDevice=devList.get(0);

        if (usbDevice == null ||usbManager ==null) {
            log( "open failed,usbDevice|usbManager = null");
            return false;
        }

        log( "find usb device="+usbDevice);

        if(!checkPermission())
            return false;

        int UARTintf = 0;
        for (int index = 0; index < usbDevice.getInterfaceCount(); ++index) {
            UsbInterface intf = usbDevice.getInterface(index);
            if ((255 != intf.getInterfaceClass()) || (intf.getInterfaceProtocol() != 0) ||
                    (intf.getInterfaceSubclass() != 0)) continue;
            UARTintf = index;
            break;
        }
        log( "UARTintf="+UARTintf);

       UsbDeviceConnection usbDeviceConnection = usbManager.openDevice(usbDevice);
        if (usbDeviceConnection != null) {
            log( "openDevice()=>ok!");
          //  Log.i(TAG, "getInterfaceCount()=>" + device.getInterfaceCount());
            this.usbDeviceConnection=usbDeviceConnection;
            this.usbInterface = usbDevice.getInterface(UARTintf);
            //this.uein= usbInterface.getEndpoint(0);
            //this.ueout = usbInterface.getEndpoint(1);
            log( "usbInterface="+usbInterface);

           for (int i = 0; i < usbInterface.getEndpointCount(); ++i) {
                UsbEndpoint ue = usbInterface.getEndpoint(i);
                if ((ue.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK  || ue.getType()==UsbConstants.USB_ENDPOINT_XFER_INT) && ue.getDirection() == UsbConstants.USB_DIR_IN) {
                    uein = ue;
                } else if ((ue.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK || ue.getType()==UsbConstants.USB_ENDPOINT_XFER_INT) && ue.getDirection() == UsbConstants.USB_DIR_OUT) {
                    ueout = ue;
                }
            }
            if (uein != null && ueout != null) {
                log("uein="+uein);
                log("ueout="+ueout);
                if(usbDeviceConnection.claimInterface(usbInterface, true)){
                    log("get Endpoint ok!");
                    opened = true;
                }else{
                    usbDeviceConnection.close();
                }

            }
        } else {
            log( "openDevice()=>fail!");
           // throw new Exception("usbManager.openDevice failed!");
        }
        return opened;
    }

    /**
     * 重置
     */
    public void reset() throws Exception {
        /*
        byte[] mPortSetting = new byte[7];
        controlTransfer(161, 33, 0, 0, mPortSetting, 7, transferTimeOut);
        mPortSetting[0] = (byte) (baudRate & 0xff);
        mPortSetting[1] = (byte) (baudRate >> 8 & 0xff);
        mPortSetting[2] = (byte) (baudRate >> 16 & 0xff);
        mPortSetting[3] = (byte) (baudRate >> 24 & 0xff);
        mPortSetting[4] = 0;
        mPortSetting[5] = 0;
        mPortSetting[6] = 8;
        controlTransfer(33, 32, 0, 0, mPortSetting, 7, transferTimeOut);
        controlTransfer(161, 33, 0, 0, mPortSetting, 7, transferTimeOut);
        */
    }

    private int controlTransfer(int requestType, int request, int value, int index, byte[] buffer, int length, int timeout) {
        int res = this.usbDeviceConnection.controlTransfer(requestType, request, value, index, buffer, length, timeout);
        if (res < 0) {
            String err = String.format("controlTransfer fail when: %d %d %d %d buffer %d %d", requestType, request, value, index, length, timeout);
            log( err);
        }
        return res;
    }

    /**
     * 设备是否已经打开
     *
     * @return true已经打开，false未打开
     */
    public boolean isOpened() {
        return opened;
    }

    /**
     * 向串口发一字节
     *
     * @param data
     *            要发送的数据
     * @return 发送了的字节数
     */
    public int write(byte data) {
        send_buf[0] = data;
        int ret = usbDeviceConnection.bulkTransfer(ueout, send_buf, 1, writeTimeOut);
        ++sendcount;
        return ret;
    }

    /**
     * 发送一串数据
     *
     * @param datas
     *            要发送的数据数组
     * @return 发送了的字节数
     */
    public int write(byte[] datas) {
        int ret = usbDeviceConnection.bulkTransfer(ueout, datas, datas.length, writeTimeOut);
        sendcount += ret;
        return ret;
    }

    private int readix = 0;
    private int readlen = 0;
    private boolean readSuccess = false;

    /**
     * 读取一个字节，调用该函数之后需要调用isReadSuccess()进行判断返回的数据是否是真正读取的数据
     *
     * @return 被读取的数据
     */
    public byte read() {
        byte ret = 0;
        if (readix >= readlen) {
            readlen = usbDeviceConnection.bulkTransfer(uein, recv_buf, SECVBUF_LEN, readTimeOut);
            readix = 0;
        }
        if (readix < readlen) {
            ret = recv_buf[readix];
            readSuccess = true;
            ++receivecount;
            ++readix;
        } else {
            readSuccess = false;
        }
        return ret;
    }

    /**
     * 判断上一个读操作是否成功
     *
     * @return
     */
    public boolean isReadSuccess() {
        return readSuccess;
    }

    /**
     * 关闭串口
     */
    public void close() {
        this.opened = false;
        if (this.usbDeviceConnection != null) {
            /*if (this.usbInterface != null) {
                this.usbDeviceConnection.releaseInterface(this.usbInterface);
                this.usbInterface = null;
            }*/
            this.usbDeviceConnection.close();
            this.usbDevice = null;
            this.usbDeviceConnection = null;
        }
    }

    public void release(){
        if (this.usbInterface != null) {
            this.usbDeviceConnection.releaseInterface(this.usbInterface);
            this.usbInterface = null;
        }
    }

    /**
     * 清空读缓冲区
     */
    public void cleanRead() {
        while ((readlen = usbDeviceConnection.bulkTransfer(uein, recv_buf, recv_buf.length, readTimeOut)) > 0) {

        }
        readlen = 0;
        readix = 0;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public int getWriteTimeOut() {
        return writeTimeOut;
    }

    public void setWriteTimeOut(int writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
    }

    @Override
    public void sendData(byte[] data, int datalen) {
        int offset=0;
        while(datalen>0){
            byte sendBuf[]=new byte[Math.min(64,datalen)];
            System.arraycopy(data,offset,sendBuf,0,sendBuf.length);
            int ret = usbDeviceConnection.bulkTransfer(ueout, sendBuf, sendBuf.length, writeTimeOut);
            sendcount+=ret;
            offset+=sendBuf.length;
            datalen-=sendBuf.length;
        }
       // sendcount += ret;
    }

    @Override
    public int recvData(byte[] recvbuf, int offset) {
        byte buffer[]=new byte[64];
        readlen = usbDeviceConnection.bulkTransfer(uein, buffer, 64, readTimeOut);
        if(readlen<=0) return readlen;
        receivecount+=readlen;
        int recvbufLen=recvbuf.length;
        int remainSpace=recvbufLen-offset;
        int copyLen=Math.min(remainSpace,readlen);
        System.arraycopy(buffer,0,recvbuf,offset,copyLen);
        return copyLen;
    }

    @Override
    public void clear() {
        cleanRead();
    }
}
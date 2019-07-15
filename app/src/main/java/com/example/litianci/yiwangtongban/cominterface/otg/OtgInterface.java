package com.example.litianci.yiwangtongban.cominterface.otg;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;

import com.example.litianci.yiwangtongban.cominterface.DataTransChannel;
import com.example.litianci.yiwangtongban.cominterface.DeviceConnectManager;
import com.example.litianci.yiwangtongban.cominterface.ReaderInterface;


/**
 * Created by zhaodianbo on 2016-4-11.
 */
public class OtgInterface implements ReaderInterface {


    UsbInterfaceAdapter usbInterfaceAdapter;
    Activity activity;
    Handler mHandler;
    DataTransChannel dataTransChannel;
    boolean isOpen=false;
    private  BroadcastReceiver mUsbReceiver;
    public OtgInterface(){
        dataTransChannel=new DataTransChannel();
    }

    @Override
    public void setActivityAndHandler(Activity act, Handler mHandler) {
        this.activity=act;
        this.mHandler=mHandler;
    }

    @Override
    public void release() {
        close();
        usbInterfaceAdapter=null;
    }

    @Override
    public boolean open() {
        return openDev();
    }

    @Override
    public void close() {
        closeDev();
        try{
            if(mUsbReceiver!=null)
                activity.unregisterReceiver(mUsbReceiver);
        }catch (Exception e){}
        mUsbReceiver=null;
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }


    @Override
    public DataTransChannel getDataTransChannel() {
        return dataTransChannel;
    }

    @Override
    public String getDevName() {
        return "OTG";
    }


    private boolean openDev(){
        try{
            close();
            registerDeviceConnectResultReceiver();
            usbInterfaceAdapter=new UsbInterfaceAdapter(activity);
            if(usbInterfaceAdapter.open()){
                dataTransChannel.setDataTransInterface(usbInterfaceAdapter);
                isOpen=true;//dataTransChannel.canReadSamv();
            }
            return isOpen;
        }catch (Exception e){
            isOpen=false;
            return false;
        }
    }




    private void closeDev(){
        if(usbInterfaceAdapter!=null){
            try{
                usbInterfaceAdapter.close();
            }catch (Exception e){}
        }
        isOpen=false;
    }


    private void registerDeviceConnectResultReceiver() {
        if(mUsbReceiver==null){
            mUsbReceiver=new UsbPermissionReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(UsbInterfaceAdapter.ACTION_SYNJONES_USB_PERMISSION);
            intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
            activity.registerReceiver(mUsbReceiver, intentFilter);
        }
    }

    class UsbPermissionReceiver extends  BroadcastReceiver{

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                Log.i("USBOTG", "lib:ACTION_USB_DEVICE_ATTACHED");
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                String deviceName = device.getDeviceName();
                if ((usbInterfaceAdapter != null) && (usbInterfaceAdapter.getDevName().equals(deviceName))) {
                    close();
                    mHandler.obtainMessage(DeviceConnectManager.MESSAGE_DISCONNECTED).sendToTarget();
                }
                Log.i("USBOTG", "ACTION_USB_DEVICE_DETACHED");
            } else if (action.equals(UsbInterfaceAdapter.ACTION_SYNJONES_USB_PERMISSION)) {
                synchronized (this) {
                    if (!intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        Log.i("USBOTG", "Permission not granted :(");
                    } else {
                        mHandler.obtainMessage(DeviceConnectManager.MESSAGE_RECONNECT_DEVICE).sendToTarget();
                        Log.i("USBOTG", "ACTION_USB_PERMISSION: Permission granted");
                    }
                }
            }
            Log.i("USBOTG", "Leave BroadcastReceiver");
        }
    }



}

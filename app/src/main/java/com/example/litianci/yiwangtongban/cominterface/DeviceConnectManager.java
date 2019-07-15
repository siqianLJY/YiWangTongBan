package com.example.litianci.yiwangtongban.cominterface;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.litianci.yiwangtongban.util.MyProgressDialog;
import com.synjones.multireaderlib.DataTransInterface;

public class DeviceConnectManager {

    private static ReaderInterface readerface;
    public static final String ACTION_DEVICE_CONNECT_RESULT="action.device.connect.result";

    public static final int MESSAGE_CONNECTED = 0x20;
    public static final int MESSAGE_DISCONNECTED = 0x21;
    public static final int MESSAGE_CONNECTFAILED = 0x22;
    public static final int MESSAGE_CONNECTING_DEVICE = 0x23;
    public static final int MESSAGE_RECONNECT_DEVICE = 0x24;
    public static final int MESSAGE_STOP_READCARD = 0x25;
    Activity activity;
    boolean isOpening=false;
    public static volatile DeviceConnectManager deviceConnectManager;

    private DeviceConnectManager() {
        readerface = ReaderInterfaceFactory.create();
    }

    public static DeviceConnectManager getInstance(){
        if(deviceConnectManager==null){
            synchronized (DeviceConnectManager.class){
                if(deviceConnectManager==null)
                    deviceConnectManager=new DeviceConnectManager();
            }
        }
        return deviceConnectManager;
    }


    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mSendBroadcast(msg.what);
        }
    };

    private void mSendBroadcast(int what){
         mSendBroadcast(what, null);
    }
    private void mSendBroadcast(int what,Parcelable msg){
        if(activity==null) return;
        Intent intent = new Intent(ACTION_DEVICE_CONNECT_RESULT);
        intent.putExtra("what", what);
        if(msg!=null)
            intent.putExtra("msg",  msg);
        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
    }


    public synchronized void setReaderface(ReaderInterface readerface){
        DeviceConnectManager.readerface.release();
        DeviceConnectManager.readerface=readerface;
    }

    public synchronized String getDevName(){
        if(readerface==null) return "";
        return readerface.getDevName();
    }

    public synchronized void openAsync(OpenHandler openHandler){
        if(isOpening) return;
        new OpenDeviceAsync(openHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public synchronized void setActivity(Activity act){
        this.activity=act;
        readerface.setActivityAndHandler(act, mHandler);
    }



    public synchronized void release() {
        try{
            readerface.release();
        }catch (Exception e){}
    }

    public synchronized boolean open() {
        if(isOpen()) return true;
        return readerface.open();
    }


    public synchronized void close() {
        try{
            readerface.close();
            mSendBroadcast(MESSAGE_DISCONNECTED);
        }catch (Exception e){}
    }

    public synchronized void resetReader(int whichReader){
        close();
        release();
        ReaderInterfaceFactory.setWhichReader(whichReader);
        readerface= ReaderInterfaceFactory.create();
        readerface.setActivityAndHandler(activity, mHandler);
    }

    public synchronized boolean isOpen() {
        return readerface.isOpen();
    }

    public  synchronized DataTransChannel getDataTransChannel() {
        return readerface.getDataTransChannel();
    }

    public static synchronized DataTransInterface getDataTransInterface(){
        return readerface.getDataTransChannel();
    }

    class OpenDeviceAsync extends AsyncTask<Void,Void,Void>{
        MyProgressDialog progressBar;
        OpenHandler openHandler;
        boolean isOpen=false;
        boolean isCancel=false;
        public OpenDeviceAsync(OpenHandler openHandler){
            this.openHandler=openHandler;
        }
        @Override
        protected void onPreExecute() {
            isOpening=true;
            progressBar= new MyProgressDialog(activity, "正在连接设备", new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    isCancel=true;
                    readerface.close();
                }
            });
            mSendBroadcast(MESSAGE_CONNECTING_DEVICE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                isOpen=readerface.open();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(isCancel){
                readerface.close();
                isOpen=false;
            }
            if(isOpen)
                mSendBroadcast(MESSAGE_CONNECTED);
            else
                mSendBroadcast(MESSAGE_CONNECTFAILED);
            progressBar.dismiss();
            if(isOpen && openHandler!=null){
                openHandler.onDeviceOpend();
            }
            isOpening=false;
        }
    }

    public interface OpenHandler{
        void onDeviceOpend();
    }


}

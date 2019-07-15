package com.example.litianci.yiwangtongban.idcardusb_reader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.litianci.yiwangtongban.R;
import com.example.litianci.yiwangtongban.cominterface.DeviceConnectManager;
import com.synjones.cardutil.util;
import com.synjones.idcard.IDCard;
import com.synjones.idcard.IDCardReaderRetInfo;
import com.synjones.idcard.IDcardReader;
import com.synjones.multireaderlib.DataTransInterface;
import com.synjones.multireaderlib.MultiReader;

/**
 * Created by zhaodianbo on 2016-3-23.
 */
public class IDCardReaderThread {
    MultiReader reader;//底层协议封装
    IDcardReader idreader;//身份证协议封装
    ReadIDCardThread readIDCardThread;
    boolean isReading;
    public volatile boolean stopRead = false;
    boolean isConnectErr=false;

    private MediaPlayer mediaPlayer;

    Handler handler;
    Context context;
    boolean hasConnect=false;
    DeviceConnectManager deviceConnectManager= DeviceConnectManager.getInstance();
    final String TAG="idcardReaderThread";
    public static final int MESSAGE_DISPLAY_IDCARD =0x30;
    public static final int MESSAGE_STOP_READ =0x31;
    public static final int MESSAGE_START_READ=0x32;

    int readTimes=0;
    int successTimes=0;
    long startReadTime=0;

    public IDCardReaderThread(){
        reader= MultiReader.getReader();
    }


    public void setDataTransInterface(DataTransInterface dataTransInterface){
        reader.setDataTransInterface(dataTransInterface);
        hasConnect= (dataTransInterface!=null);
    }

    public void setActivityAndHandler(Activity activity, Handler handler){
        this.handler=handler;
        this.context=activity;
        deviceConnectManager.setActivity(activity);
    }

    public  void  open(DeviceConnectManager.OpenHandler openHandler){
        registerDeviceConnectResultReceiver();
        deviceConnectManager.openAsync(openHandler);
    }

    public boolean isOpen(){
        return deviceConnectManager.isOpen();
    }

    public void close(){
        stop();
        deviceConnectManager.close();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(deviceConnectResultReceiver);
    }

    private void registerDeviceConnectResultReceiver() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DeviceConnectManager.ACTION_DEVICE_CONNECT_RESULT);
        broadcastManager.registerReceiver(deviceConnectResultReceiver, intentFilter);
    }


    BroadcastReceiver deviceConnectResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!intent.getAction().equals(DeviceConnectManager.ACTION_DEVICE_CONNECT_RESULT))
                return;
            int what = intent.getIntExtra("what", 0);
            switch (what){
                case DeviceConnectManager.MESSAGE_CONNECTED:
                    setDataTransInterface(DeviceConnectManager.getDataTransInterface());
                    break;
                case DeviceConnectManager.MESSAGE_CONNECTING_DEVICE:
                    break;
                case DeviceConnectManager.MESSAGE_CONNECTFAILED:
                    setDataTransInterface(null);
                    break;
                case DeviceConnectManager.MESSAGE_DISCONNECTED:
                    setDataTransInterface(null);
                    break;
                case DeviceConnectManager.MESSAGE_RECONNECT_DEVICE:
                    setDataTransInterface(null);
                    deviceConnectManager.openAsync(onOpen());
                    break;
                case DeviceConnectManager.MESSAGE_STOP_READCARD:
                    if(isReading()){
                        stop();
                    }
                    break;
            }
            handler.obtainMessage(what).sendToTarget();
        }
    };


    public void read(){
        stop();
        stopRead=false;
        if(deviceConnectManager.isOpen()) {
            readIDCardThread = new ReadIDCardThread();
            readIDCardThread.start();
        }
        else{
            deviceConnectManager.openAsync(onOpen());
        }
    }


    DeviceConnectManager.OpenHandler onOpen(){
        return new DeviceConnectManager.OpenHandler() {
            @Override
            public void onDeviceOpend() {
                setDataTransInterface(DeviceConnectManager.getDataTransInterface());
                readIDCardThread = new ReadIDCardThread();
                readIDCardThread.start();
            }
        };
    }

    public void stop(){
        if(readIDCardThread!=null) {
            readIDCardThread.cancel();
        }
        readIDCardThread=null;
    }


    public boolean isReading(){
        if(readIDCardThread==null) return false;
        return  readIDCardThread.isAlive();
    }



    public String getReadStatus(){
        String t= util.formatDuring(System.currentTimeMillis()-startReadTime);
        return String.format("时间：%s,次数：%d,成功：%d",t,readTimes,successTimes);
    }


    class ReadIDCardThread extends Thread{
        int pauseTimes=0;
        int recvErrTimes=0;

        public void run() {
            Looper.prepare();
            readTimes=0;
            successTimes=0;
            startReadTime=System.currentTimeMillis();

            if(hasConnect) {
                idreader=new IDcardReader(reader);
                idreader.open(context);
                idreader.findIDCard();//为了唤醒模块 用电池的蓝牙设备等需要唤醒 其他设备可不执行此处
                //idreader.getSamvIDStr(); //获取公安部安全模块的编号
                mediaPlayer = MediaPlayer.create(context, R.raw.b);//读卡声音
            }
            else
            {
                handler.obtainMessage(DeviceConnectManager.MESSAGE_CONNECTFAILED).sendToTarget();
                stopRead=true;
                return;
            }

            handler.obtainMessage(MESSAGE_START_READ).sendToTarget();
            while (!stopRead) {
                if(!hasConnect){
                    stopRead=true;
                    break;
                }
                pauseTimes=0;

                try {
                    IDCardReaderRetInfo info;
                    long ct=System.currentTimeMillis();
                    if(true) {
                        //获取身份证指纹信息、文字和照片  同一张卡只读一次
                        info = idreader.getIDCardInfo(false, true, false);
                    }
                    else {
                        //只获取身份证文字和照片 同一张卡只读一次
                        info = idreader.getIDcardBlockingNoFpRetInfo(true);
                    }
                    recvErrTimes=info.isRecvError()?recvErrTimes+1:0;
                    if(recvErrTimes>3){
                        isConnectErr=true;
                        deviceConnectManager.close();
                        handler.obtainMessage(DeviceConnectManager.MESSAGE_CONNECTFAILED).sendToTarget();
                        Log.d(TAG, "recvErrTimes>3");
                        break;
                    }
                    readTimes++;
                    if(info.errCode==0){
                        successTimes++;
                        IDCard idcard=info.card;
                        idcard.setReadTime(""+(System.currentTimeMillis()-ct));
                        handler.obtainMessage(MESSAGE_DISPLAY_IDCARD, idcard).sendToTarget();
                        beep();
                        mySleep(800);
                    } else{
                        mySleep(300);
                    }

                } catch (Exception ee) {
                    Log.e(TAG,"run exception:" + ee.getMessage());
                    break;
                }
            }//while
            try{
                idreader.close();
                //reader.closeReader();
            }catch (Exception e){
            }
            handler.sendEmptyMessage(MESSAGE_STOP_READ);
            mediaPlayer.release();
            stopRead=true;
            isReading=false;
        }

        public void cancel(){
            stopRead=true;
            interrupt();
            try {
                join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    public  void beep() {
        try {
            if(mediaPlayer==null)   return;
            if(mediaPlayer.isPlaying())
                mediaPlayer.stop();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void mySleep(long t){
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public  String formatDuringm(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        if(hours>0)
            return String.format("%d:%02d:%02d.%03d",hours,minutes,seconds,mss%1000);
        if(minutes>0)
            return String.format("%d:%02d.%03d",minutes,seconds,mss%1000);
        return String.format("%d.%03d",seconds,mss%1000);
			/*return days + " days " + hours + ":" + minutes + ":"
					+ seconds + "";*/
    }



}

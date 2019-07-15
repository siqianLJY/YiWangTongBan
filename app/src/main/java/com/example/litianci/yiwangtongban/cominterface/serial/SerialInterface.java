package com.example.litianci.yiwangtongban.cominterface.serial;

import android.app.Activity;
import android.os.Handler;

import com.example.litianci.yiwangtongban.cominterface.DataTransChannel;
import com.example.litianci.yiwangtongban.cominterface.ReaderInterface;
import com.example.litianci.yiwangtongban.util.LocalSharedPreferences;
import com.example.litianci.yiwangtongban.util.SPNameInterface;

import java.io.File;

import android_serialport_api.SerialPort;

/**
 * 使用了手持机718D中所带的SerialPort jar和so库
 */

/**
 * Created by zhaodianbo on 2016-4-12.
 */
public class SerialInterface  implements ReaderInterface {
    DataTransChannel dataTransChannel;
    SerialPort serialPort;
    Activity activity;
    boolean isOpen=false;
    String path="串口";
    public static final String[] Baudrate=new String[]{"9600","38400","57600","115200"};
    public SerialInterface(){
        dataTransChannel=new DataTransChannel();
    }
    @Override
    public void setActivityAndHandler(Activity act, Handler mHandler) {
        this.activity=act;
    }

    @Override
    public void release() {
        close();
        serialPort=null;
    }

    @Override
    public boolean open() {
        if(serialPort!=null)
            serialPort.close();
        int baudrate=-1;
        String path = LocalSharedPreferences.getSp(activity).getString(SPNameInterface.SP_SERIAL_NAME, "");

        try{
            String baudrateStr =LocalSharedPreferences.getSp(activity).getString(SPNameInterface.SP_SERIAL_BAUDRATE, "115200");
            if(baudrateStr.isEmpty()) return false;
            baudrate=Integer.decode(baudrateStr);
        }catch (Exception e){
            return false;
        }


        /* Check parameters */
        if ( (path == null) ||path.isEmpty()|| (baudrate == -1)) {
            return false;
        }


        /* Open the serial port */
        try {
            serialPort = new SerialPort(new File(path), baudrate, 0);
            dataTransChannel.setInOutStream(serialPort.getInputStream(),serialPort.getOutputStream());
            dataTransChannel.open();
            isOpen=dataTransChannel.canReadSamv();;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return isOpen;
    }


    @Override
    public void close() {
       if(serialPort!=null) {
           try {
               serialPort.close();
           }catch (Exception e){}
       }
        isOpen = false;
        if(dataTransChannel!=null){
            try{
                dataTransChannel.close();
            }catch (Exception e){}
        }
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
        return path;
    }
}

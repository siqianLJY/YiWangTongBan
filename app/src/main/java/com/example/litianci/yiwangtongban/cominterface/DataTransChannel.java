package com.example.litianci.yiwangtongban.cominterface;

import android.util.Log;

import com.synjones.idcard.IDcardReader;
import com.synjones.multireaderlib.CmdResponse;
import com.synjones.multireaderlib.DataTransInterface;
import com.synjones.multireaderlib.MultiReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by zhaodianbo on 2016-3-24.
 */
public class DataTransChannel implements DataTransInterface
{
    private DataTransInterface dataTransInterface;
    private InputStream inputStream;
    private OutputStream outputStream;

    public DataTransChannel(){}
    
    public void setDataTransInterface(DataTransInterface dataTransInterface){
        this.dataTransInterface=dataTransInterface;
    }
    public void setInOutStream(InputStream inputStream, OutputStream outputStream){
        this.inputStream=inputStream;
        this.outputStream=outputStream;
    }

    public void open(){
        if(dataTransInterface!=null) return;
        new CheckBlockReading().start();
        new RecvThread().start();
    }


    public boolean canReadSamv(){
        MultiReader multiReader=MultiReader.getReader();
        multiReader.setDataTransInterface(this);
        IDcardReader iDcardReader=new IDcardReader(multiReader);
        CmdResponse cmdResponse = iDcardReader.getSAMID();
        if (cmdResponse == null || !cmdResponse.SwIs9000()) {
            try {Thread.sleep(300);} catch (InterruptedException e) {}
            cmdResponse = iDcardReader.getSAMID();
            if (cmdResponse == null || !cmdResponse.SwIs9000())
                return false;
        }
        return true;
    }
    

    public void close(){
        recvRunFlag=false;
        if(inputStream!=null){
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        inputStream=null;
        if(outputStream!=null){
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        outputStream=null;
    }


    @Override
    public void sendData(byte[] data, int datalen) {
        if(dataTransInterface!=null){
            dataTransInterface.sendData(data,datalen);
            return;
        }
        try {
            outputStream.write(data, 0, datalen);
            outputStream.flush();
            //	Log.e("sendData", "write len="+datalen);
        }catch (IOException ioe) {
            ioe.printStackTrace();
        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public int recvData(byte[] recvbuf, int offset) {
        if(dataTransInterface!=null){
            return dataTransInterface.recvData(recvbuf,offset);
        }
        try {
            if(recvWay==RecvWay.DirectRead){//非阻塞 直接读
                if(inputStream.available()>0){
                    int len=inputStream.read(recvbuf, offset, recvbuf.length-offset);
                    //Log.e("btRecv", util.bytesToHexString(recvbuf, offset+len));
                    return len;
                }
            }
            else{
                if(!recvRunFlag) return -1;
                int size=iReadQueueArray.size();
                if(size<=0) return 0;
                Thread.sleep(20);
                //	synchronized (ReadQueueLock) {
                int retLen=(recvbuf.length-offset)>iReadQueueArray.size()?iReadQueueArray.size():(recvbuf.length-offset);
                for(int i=0;i<retLen;i++)
                    recvbuf[i+offset]=iReadQueueArray.poll();
                return retLen;
                //	}
            }

            //}catch (IOException ioe) {
            //	ioe.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    byte buffer[]=new byte[1024];
    @Override
    public void clear() {
        if(dataTransInterface!=null){
            dataTransInterface.clear();
            return;
        }
        try {
            if(recvWay==RecvWay.ThreadRead){
                //synchronized (ReadQueueLock) {
                iReadQueueArray.clear();
                //}
            }
            else{
                while(inputStream.available()>0){

                    inputStream.read(buffer);
                }
            }
            //}catch (IOException ioe) {
            //	ioe.printStackTrace();
        }catch (Exception e) {
            // TODO: handle exception
        }
    }

    private  boolean recvRunFlag=false;
    private ArrayBlockingQueue<Byte> iReadQueueArray = new ArrayBlockingQueue<Byte>(10240, true);
    public enum RecvWay{DirectRead,ThreadRead};
    private RecvWay recvWay=RecvWay.ThreadRead;
    public void setRecvWay(RecvWay way){this.recvWay=way;}
    volatile boolean block=true;
    private class CheckBlockReading extends Thread{
        @Override
        public void run() {
            try {
                inputStream.available();
                block=false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class RecvThread extends Thread{
        public void run(){
            recvRunFlag=true;
            Log.e("btRecv", "recv Thread start!");
            long cut=System.currentTimeMillis();
            if(recvWay==RecvWay.DirectRead){
                recvRunFlag=false;
            }

            while(recvRunFlag && System.currentTimeMillis()-cut<200){
                if(block==false) {
                    recvRunFlag=false;
                    recvWay=RecvWay.DirectRead;
                    break;
                }
            }

            Log.e("btRecv", "RecvWay:"+recvWay);

            byte buffer[]=new byte[1024];
            while(recvRunFlag){
                //isSocketBlockMode=true;
                int readlen=0;
                try {
                    readlen=inputStream.read(buffer);
                } catch (Exception e) {
                    //readlen=-1;
                    e.printStackTrace();
                    //if(recvRunFlag)
                    //	handler.obtainMessage(ReadIDCardFragment.MESSAGE_CONNECTFAILED).sendToTarget();
                    break;
                }
                if(readlen>0){
                    //synchronized (ReadQueueLock) {
                    int i=0;
                    while(iReadQueueArray.size()<10240 && i<readlen){
                        try {
                            iReadQueueArray.offer(buffer[i++]);
                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                    //}
                    //Log.e("recv len=" + readlen);
                    //MyLog.e(MyUtils.bytesToHexString(buffer, readlen));
                }
            }
            recvRunFlag=false;
            Log.e("btRecv", "recv Thread quit!!");
        }
    }
}

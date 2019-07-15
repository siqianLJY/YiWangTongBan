package com.example.litianci.yiwangtongban.banshi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.litianci.yiwangtongban.MainActivity;
import com.example.litianci.yiwangtongban.R;
import com.example.litianci.yiwangtongban.cominterface.DeviceConnectManager;
import com.example.litianci.yiwangtongban.cominterface.ReaderInterfaceFactory;
import com.example.litianci.yiwangtongban.idcardusb_reader.IDCardReaderThread;
import com.synjones.idcard.IDCard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class JixuBanli1Activity extends Activity implements View.OnClickListener {

    @Bind(R.id.btn_return)
    Button btnReturn;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.iv_home)
    ImageView ivHome;
    @Bind(R.id.iv_last)
    ImageView ivLast;

    IDCardReaderThread idCardReaderThread;
    final int UPDATE_READER_STATUS = 0x40;
    @Bind(R.id.iv_photo)
    ImageView ivPhoto;
    private int times = 0;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String devName = DeviceConnectManager.getInstance().getDevName();
            switch (msg.what) {

                case DeviceConnectManager.MESSAGE_CONNECTED:
                    Toast.makeText(JixuBanli1Activity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    idCardReaderThread.read();
                    break;
                case DeviceConnectManager.MESSAGE_CONNECTING_DEVICE:
                    Toast.makeText(JixuBanli1Activity.this, "正在连接", Toast.LENGTH_SHORT).show();
                    break;
                case DeviceConnectManager.MESSAGE_CONNECTFAILED:
                    Toast.makeText(JixuBanli1Activity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case DeviceConnectManager.MESSAGE_DISCONNECTED:
                    Toast.makeText(JixuBanli1Activity.this, "连接关闭", Toast.LENGTH_SHORT).show();
                    break;
                case DeviceConnectManager.MESSAGE_RECONNECT_DEVICE:
                    Toast.makeText(JixuBanli1Activity.this, "重新连接", Toast.LENGTH_SHORT).show();
                    break;
                case DeviceConnectManager.MESSAGE_STOP_READCARD:
                    break;
                case IDCardReaderThread.MESSAGE_START_READ:
                    new JixuBanli1Activity.MyTimer().start();
                    break;
                case IDCardReaderThread.MESSAGE_DISPLAY_IDCARD:
                    IDCard idCard = (IDCard) msg.obj;
                    ivPhoto.setImageBitmap(idCard.getPhoto());
                    if (times == 0) {
//                        saveBitmap(idCard.getPhoto());
                        idCardReaderThread.stop();

                        Intent intent = new Intent(JixuBanli1Activity.this, JixuBanli2Activity.class);
                        intent.putExtra("name", ((IDCard) msg.obj).getName());
                        intent.putExtra("num", ((IDCard) msg.obj).getIDCardNo());
                        Bundle b = new Bundle();
                        b.putParcelable("bitmap", ((IDCard) msg.obj).getPhoto());
                        intent.putExtras(b);
                        startActivity(intent);
                        idCardReaderThread.close();
                    }

                    times++;
                    break;
                case IDCardReaderThread.MESSAGE_STOP_READ:
//                    btnRead.setText("读卡");
                    break;
                case UPDATE_READER_STATUS:
//                    tvReaderStatus.setText(idCardReaderThread.getReadStatus());
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void saveBitmap(Bitmap bitmap) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "LOL");
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        File pictureFile = new File(appDir, "shenfenzheng");
        if (pictureFile.exists()) {
            pictureFile.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(pictureFile);
            Bitmap bm = bitmap;
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Snackbar.make(findViewById(android.R.id.content), "保存图片成功" + pictureFile.getAbsolutePath(), Snackbar.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    pictureFile.getAbsolutePath(), "shenfenzheng", null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(pictureFile);
        intent.setData(uri);
        sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jixu_banli1);
        ButterKnife.bind(this);
        setOnClick();
        DeviceConnectManager.getInstance().resetReader(ReaderInterfaceFactory.READER_USB);
        idCardReaderThread = new IDCardReaderThread();
        idCardReaderThread.setActivityAndHandler(this, mHandler);
        idCardReaderThread.open(null);
        idCardReaderThread.read();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void setOnClick() {
        btnReturn.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        ivHome.setOnClickListener(this);
        ivLast.setOnClickListener(this);
    }

    /**
     * 身份证识别相关
     */
    class MyTimer extends Thread {
        @Override
        public void run() {
            while (idCardReaderThread.isReading()) {
                mHandler.obtainMessage(UPDATE_READER_STATUS).sendToTarget();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_home: {
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("确认前往首页？");
                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(JixuBanli1Activity.this, MainActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                });
                adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                adb.create().show();


            }
            break;
            case R.id.iv_last: {
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("确认返回上一页？");
                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                adb.create().show();


            }
            break;
            default:
                break;
        }
    }
}

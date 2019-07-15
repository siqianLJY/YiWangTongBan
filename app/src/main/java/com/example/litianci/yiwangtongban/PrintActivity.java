package com.example.litianci.yiwangtongban;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.litianci.yiwangtongban.cominterface.DeviceConnectManager;
import com.example.litianci.yiwangtongban.cominterface.ReaderInterfaceFactory;
import com.example.litianci.yiwangtongban.dayin.Print2ShibieActivity;
import com.example.litianci.yiwangtongban.idcardusb_reader.IDCardReaderThread;
import com.synjones.idcard.IDCard;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PrintActivity extends Activity implements View.OnClickListener {

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
                    Toast.makeText(PrintActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    idCardReaderThread.read();
                    break;
                case DeviceConnectManager.MESSAGE_CONNECTING_DEVICE:
                    Toast.makeText(PrintActivity.this, "正在连接", Toast.LENGTH_SHORT).show();
                    break;
                case DeviceConnectManager.MESSAGE_CONNECTFAILED:
                    Toast.makeText(PrintActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case DeviceConnectManager.MESSAGE_DISCONNECTED:
                    Toast.makeText(PrintActivity.this, "连接关闭", Toast.LENGTH_SHORT).show();
                    break;
                case DeviceConnectManager.MESSAGE_RECONNECT_DEVICE:
                    Toast.makeText(PrintActivity.this, "重新连接", Toast.LENGTH_SHORT).show();
                    break;
                case DeviceConnectManager.MESSAGE_STOP_READCARD:
                    break;
                case IDCardReaderThread.MESSAGE_START_READ:
                    new PrintActivity.MyTimer().start();
                    break;
                case IDCardReaderThread.MESSAGE_DISPLAY_IDCARD:
                    IDCard idCard = (IDCard) msg.obj;
                    ivPhoto.setImageBitmap(idCard.getPhoto());
                    if (times == 0) {
                        idCardReaderThread.stop();
                        Intent intent = new Intent(PrintActivity.this, Print2ShibieActivity.class);

                        intent.putExtra("name", ((IDCard) msg.obj).getName());
                        intent.putExtra("num", ((IDCard) msg.obj).getIDCardNo());
                        Bundle b = new Bundle();
                        b.putParcelable("bitmap", ((IDCard) msg.obj).getPhoto());
                        intent.putExtras(b);
                        startActivity(intent);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
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
        idCardReaderThread.close();
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
            case R.id.btn_return: {

            }
            break;
            case R.id.btn_next: {
//                Intent intent = new Intent(PrintActivity.this, Print2ShibieActivity.class);
//                startActivity(intent);
            }
            break;
            case R.id.iv_home: {
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("确认前往首页？");
                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(PrintActivity.this, MainActivity.class);
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

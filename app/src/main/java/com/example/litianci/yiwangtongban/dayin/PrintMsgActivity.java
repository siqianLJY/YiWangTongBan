package com.example.litianci.yiwangtongban.dayin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.litianci.yiwangtongban.Globals;
import com.example.litianci.yiwangtongban.MainActivity;
import com.example.litianci.yiwangtongban.R;
import com.example.litianci.yiwangtongban.been.NormalResult;
import com.example.litianci.yiwangtongban.utils.GsonUtils;
import com.example.litianci.yiwangtongban.utils.VolleyUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PrintMsgActivity extends Activity implements View.OnClickListener {


    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.et_phonenum)
    EditText etPhonenum;
    @Bind(R.id.et_yanzhengma)
    EditText etYanzhengma;
    @Bind(R.id.btn_sendmsg)
    Button btnSendmsg;
    @Bind(R.id.btn_zcbksbl)
    Button btnZcbksbl;
    @Bind(R.id.iv_home)
    ImageView ivHome;
    @Bind(R.id.iv_last)
    ImageView ivLast;
    private String yanzhengma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_msg);
        ButterKnife.bind(this);
        btnSendmsg.setOnClickListener(this);
        btnZcbksbl.setOnClickListener(this);
        ivHome.setOnClickListener(this);
        ivLast.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sendmsg: {
                if ("".equals(etPhonenum.getText().toString())) {
                    Toast.makeText(PrintMsgActivity.this, "手机号不能为空",
                            Toast.LENGTH_SHORT).show();
                } else {
                    getTime();
                    getCodeRequest(this, etPhonenum.getText().toString());
                }
            }
            break;
            case R.id.btn_zcbksbl: {
                if (yanzhengma.equals(etYanzhengma.getText().toString())) {
                    Zhuce(PrintMsgActivity.this, etPhonenum.getText().toString());
                }
            }
            break;

            case R.id.iv_home: {
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("确认前往首页？");
                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(PrintMsgActivity.this, MainActivity.class);
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

    public void getCodeRequest(final Context context, String phone) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);

        new VolleyUtil() {

            @Override
            public <T> boolean analysisData(String response) {
                NormalResult s3 = GsonUtils.json2bean(response, NormalResult.class);
                Log.i("MainActivity", response);

                if (s3 == null) {
                    Toast.makeText(context, "数据异常",
                            Toast.LENGTH_SHORT).show();


                } else if (s3.getCode() == 1) {
                    Toast.makeText(PrintMsgActivity.this, "该手机号已注册", Toast.LENGTH_SHORT).show();

                } else if (s3.getCode() == 0) {
                    yanzhengma = s3.getMsg() + "";
                    Toast.makeText(PrintMsgActivity.this, "验证码已发送！", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        }.volleyStringRequestPost(context, params, "sendcode", null);
    }

    public void Zhuce(final Context context, String phone) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        params.put("realname", getIntent().getStringExtra("name"));
        params.put("idcard", getIntent().getStringExtra("num"));
        new VolleyUtil() {

            @Override
            public <T> boolean analysisData(String response) {
                NormalResult s3 = GsonUtils.json2bean(response, NormalResult.class);
                Log.i("MainActivity", response);

                if (s3 == null) {
                    Toast.makeText(context, "数据异常",
                            Toast.LENGTH_SHORT).show();


                } else if (s3.getCode() == 1) {
                    Toast.makeText(PrintMsgActivity.this, "该手机号已注册", Toast.LENGTH_SHORT).show();

                } else if (s3.getCode() == 0) {
                    webview.loadUrl(Globals.path + "beflogin?id=" + s3.getMsg());
                    Intent intent = new Intent(PrintMsgActivity.this, PrintResultActivity.class);
                    intent.putExtra("id", getIntent().getStringExtra("id"));
                    intent.putExtra("name", getIntent().getStringExtra("name"));
                    intent.putExtra("num", getIntent().getStringExtra("num"));
                    startActivity(intent);
                    Toast.makeText(PrintMsgActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        }.volleyStringRequestPost(context, params, "doreg", null);
    }

    //倒计时的设置
    private CountDownTimer mCountDownTimer = null;

    //创建验证码倒计时的功能
    public void getTime() {
        mCountDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                btnSendmsg.setClickable(false);
                btnSendmsg.setText("重新获取" + millisUntilFinished / 1000 + "");
                btnSendmsg.setBackgroundResource(R.drawable.button_back_h);
            }

            public void onFinish() {
                btnSendmsg.setClickable(true);
                btnSendmsg.setText("重新获取");
                btnSendmsg.setBackgroundResource(R.color.colorsendmsg);
            }
        };
        if (mCountDownTimer != null) {
            mCountDownTimer.start();
        }

    }
}

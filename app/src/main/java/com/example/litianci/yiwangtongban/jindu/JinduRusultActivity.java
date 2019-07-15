package com.example.litianci.yiwangtongban.jindu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
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

public class JinduRusultActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.iv_home)
    ImageView ivHome;
    @Bind(R.id.iv_last)
    ImageView ivLast;
    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jindu_rusult);
        ButterKnife.bind(this);
        WebSettings settings = webview.getSettings();
        //启用数据库
        settings.setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
//启用地理定位
        settings.setGeolocationEnabled(true);
//设置定位的数据库路径
        settings.setGeolocationDatabasePath(dir);
//最重要的方法，一定要设置，这就是出不来的主要原因
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        settings.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        settings.setAllowFileAccessFromFileURLs(false);
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        settings.setAllowUniversalAccessFromFileURLs(false);
        //开启JavaScript支持
        settings.setJavaScriptEnabled(true);
        // 支持缩放
        settings.setSupportZoom(true);

//        JinduSearch(this, "");
        image.setVisibility(View.GONE);
        text.setVisibility(View.GONE);
        webview.setVisibility(View.VISIBLE);
        webview.loadUrl(Globals.path+"matview?num="+getIntent().getStringExtra("num")+"&pass="+getIntent().getStringExtra("pass"));
        setOnClick();
    }

    public void setOnClick() {
        ivHome.setOnClickListener(this);
        ivLast.setOnClickListener(this);
    }

    public void JinduSearch(final Context context, String phone) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("", "");

        new VolleyUtil() {

            @Override
            public <T> boolean analysisData(String response) {
                NormalResult s3 = GsonUtils.json2bean(response, NormalResult.class);
                Log.i("MainActivity", response);

                if (s3 == null) {
                    Toast.makeText(context, "数据异常",
                            Toast.LENGTH_SHORT).show();


                } else if (s3.getCode() !=0) {
                    Toast.makeText(JinduRusultActivity.this, "申报号或密码不对，请核对后输入", Toast.LENGTH_LONG).show();
                    finish();

                } else if (s3.getCode() == 0) {


                }
                return false;
            }
        }.volleyStringRequestPost(context, params, "matstatus?num=" + getIntent().getStringExtra("num")+"&pass="+getIntent().getStringExtra("pass"), null);
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
                        Intent intent = new Intent(JinduRusultActivity.this, MainActivity.class);
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

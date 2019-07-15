package com.example.litianci.yiwangtongban.zhinan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import com.example.litianci.yiwangtongban.Globals;
import com.example.litianci.yiwangtongban.MainActivity;
import com.example.litianci.yiwangtongban.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BanshiZhinanActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.iv_home)
    ImageView ivHome;
    @Bind(R.id.iv_last)
    ImageView ivLast;
    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.btn_theme)
    Button btnTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banshi_zhinan);
        ButterKnife.bind(this);
        ivHome.setOnClickListener(this);
        ivLast.setOnClickListener(this);
        btnTheme.setText(getIntent().getStringExtra("theme"));
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


        webview.setWebChromeClient(new WebChromeClient());
        //֧��javascript
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setUseWideViewPort(true);    //设置webview推荐使用的窗口，使html界面自适应屏幕setUseWideViewPort(true);

        webview.setHorizontalScrollBarEnabled(false);//ˮƽ����ʾ
        webview.setVerticalScrollBarEnabled(false); //��ֱ����ʾ
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("zhang", "点击链接" + url);


                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i("onPageFinished", url);
                if (url.contains("=")) {
//                    Widnew = url.split("=")[1];
                }

            }
        });
        webview.loadUrl(Globals.path + "guide?id=" + getIntent().getStringExtra("id"));
//        webview.loadUrl(" http://www.sysqwx.bjshy.com.cn/sywwg/image/check/pat.jsp?appid=117&path=http://www.sysqwx.bjshy.com.cn/sywwg");

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
                        Intent intent = new Intent(BanshiZhinanActivity.this, MainActivity.class);
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

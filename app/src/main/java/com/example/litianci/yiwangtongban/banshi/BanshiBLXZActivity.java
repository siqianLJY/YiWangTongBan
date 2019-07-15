package com.example.litianci.yiwangtongban.banshi;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.litianci.yiwangtongban.Globals;
import com.example.litianci.yiwangtongban.MainActivity;
import com.example.litianci.yiwangtongban.R;
import com.example.litianci.yiwangtongban.utils.PhotoUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BanshiBLXZActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.tv_theme)
    TextView tvTheme;
    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.webview)
    WebView webView;
    @Bind(R.id.iv_home)
    ImageView ivHome;
    @Bind(R.id.iv_last)
    ImageView ivLast;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    String URL = "0";
    String ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banshi_blxz);
        ButterKnife.bind(this);
        ivHome.setOnClickListener(this);
        ivLast.setOnClickListener(this);
        tvTheme.setText(getIntent().getStringExtra("theme"));
        WebSettings settings = webView.getSettings();
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


        webView.setWebChromeClient(new WebChromeClient());
        //֧��javascript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setUseWideViewPort(true);    //设置webview推荐使用的窗口，使html界面自适应屏幕setUseWideViewPort(true);

        webView.setHorizontalScrollBarEnabled(false);//ˮƽ����ʾ
        webView.setVerticalScrollBarEnabled(false); //��ֱ����ʾ
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        Log.i("安卓版本", Build.VERSION.SDK_INT + "");

        if (Build.VERSION.SDK_INT < 26) {
            webView.setWebViewClient(new myWebClient());
        } else {
            webView.setWebViewClient(new myWebClient2());
        }

        webView.setWebChromeClient(new WebChromeClient() {
            //The undocumented magic method override
            //Eclipse will swear at you if you try to put @Override here
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                BanshiBLXZActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);

            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg);
            }

            // For Lollipop 5.0+ Devices
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView, final ValueCallback<Uri[]> filePathCallback, final FileChooserParams fileChooserParams) {


                URL ="0";
                Log.d("标记", "onShowFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
                mUploadCallbackAboveL = filePathCallback;

                takeCamera();

                return true;
            }

            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }


        });
        webView.loadUrl(Globals.path + "condition?id=" + getIntent().getStringExtra("id"));
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
                        Intent intent = new Intent(BanshiBLXZActivity.this, MainActivity.class);
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

    //图片
    private final static int FILE_CHOOSER_RESULT_CODE = 128;
    //拍照
    //拍照图片路径
    private String cameraFielPath;

    private void openImageChooserActivity() {
        takeCamera();
    }

    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/" + SystemClock.currentThreadTimeMillis() + ".jpg");
    private Uri imageUri;

    //拍照
    private void takeCamera() {


        imageUri = Uri.fromFile(fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            imageUri = FileProvider.getUriForFile(BanshiBLXZActivity.this, "guangming.org.diql.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri

        }
        PhotoUtils.takePicture(BanshiBLXZActivity.this, imageUri, PHOTO_REQUEST);
    }

    private final static int PHOTO_REQUEST = 100;


    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub


            if (!url.contains("ok")) {
                view.loadUrl(url);
                if (url.contains("village")) {
                    ID = url.split("=")[1];
                    Log.i("链接地址", url + "ID值" + ID);
                }
            } else {
                Intent intent = new Intent(BanshiBLXZActivity.this, BanshiChooseTypeActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("theme", getIntent().getStringExtra("theme"));
                Log.i("跳转时候ID的值", ID);
                startActivity(intent);
            }


            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            Log.i("链接", url);
            super.onPageFinished(view, url);


        }
    }

    public class myWebClient2 extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            Log.i("方法2", url);
            if (url.contains("tel")) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                try {

                    Uri data = Uri.parse(url);
                    intent.setData(data);

                } catch (NullPointerException e) {

                }


                startActivity(intent);
                return true;
            }


            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            Log.i("链接", url);
            super.onPageFinished(view, url);


        }
    }

    //flipscreen not loading again
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (URL.contains("0")) {
            if (requestCode == PHOTO_REQUEST) {
                Log.i("标记", "近来没");
                if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                if (mUploadCallbackAboveL != null) {
                    onActivityResultAboveL(requestCode, resultCode, intent);
                } else if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (requestCode == REQUEST_SELECT_FILE) {
                    if (uploadMessage == null)
                        return;
                    uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                    uploadMessage = null;
                }

            } else if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == mUploadMessage)
                    return;
                // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
                // Use RESULT_OK only if you're implementing WebView inside an Activity
                Uri result = intent == null || resultCode != BanshiBLXZActivity.RESULT_OK ? null : intent.getData();
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != PHOTO_REQUEST || mUploadCallbackAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
    }

    private ValueCallback<Uri[]> mUploadCallbackAboveL;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //����һ�������õİ����ķ�����keyCode �����û��Ķ���������ǰ��˷��ؼ���ͬʱWebviewҪ���صĻ���WebViewִ�л��˲�������ΪmWebView.canGoBack()���ص���һ��Boolean���ͣ��������ǰ�������Ϊtrue
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.setWebViewClient(null);
            webView.setWebChromeClient(null);
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
//            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
    }
}

package com.example.litianci.yiwangtongban.banshi;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.litianci.yiwangtongban.Globals;
import com.example.litianci.yiwangtongban.MainActivity;
import com.example.litianci.yiwangtongban.R;
import com.example.litianci.yiwangtongban.been.PrinPZResult;
import com.example.litianci.yiwangtongban.utils.GsonUtils;
import com.example.litianci.yiwangtongban.utils.PhotoUtils;
import com.example.litianci.yiwangtongban.utils.VolleyUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import android_serialport_api.SerialPort;
import butterknife.Bind;
import butterknife.ButterKnife;

public class BanshiTXBDActivity extends Activity implements View.OnClickListener {


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
    @Bind(R.id.tv_wcbl)
    TextView tvWcbl;
    @Bind(R.id.btn_dypz)
    Button btnDypz;
    private static final String TAG = "MainActivity";

    protected SerialPort mSerialPort;
    protected InputStream mInputStream;
    protected OutputStream mOutputStream;

    private String prot = "/dev/ttyS1";
    private int baudrate = 38400;
    private static long i = 0;

    private Toast mToast;


    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {

            }
        }
    };
    private Thread receiveThread;
    private Thread sendThread;
    byte[] hexKnife = {(byte) 0x1B, (byte) 0x69};
    byte[] big = {(byte) 0x1B, (byte) 0x56, (byte) 0x03};
    byte[] line = {(byte) 0x0A};//换行

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banshi_txbd);
        ButterKnife.bind(this);
        ivHome.setOnClickListener(this);
        ivLast.setOnClickListener(this);
        btnDypz.setOnClickListener(this);
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

//                mUploadMessage = uploadMsg;
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("image/*");
//                BanshiTXBDActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);

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
                AlertDialog.Builder adb = new AlertDialog.Builder(BanshiTXBDActivity.this);
                adb.setItems(new String[]{"相册", "拍照"}, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        URL = i + "";
//                        if (URL.contains("0")) {
//                            if (uploadMessage != null) {
//                                uploadMessage.onReceiveValue(null);
//                                uploadMessage = null;
//                            }
//
//                            uploadMessage = filePathCallback;
//
//                            Intent intent = fileChooserParams.createIntent();
//                            try {
//                                startActivityForResult(intent, REQUEST_SELECT_FILE);
//                            } catch (ActivityNotFoundException e) {
//                                uploadMessage = null;
//                            }
//                        } else {
//                            Log.d("标记", "onShowFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
//                            mUploadCallbackAboveL = filePathCallback;
//
//                            takeCamera();
//                        }
//                        dialogInterface.dismiss();
                    }
                });
                adb.create().show();


                return true;
            }

            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }


        });
//        btnDypz.setVisibility(View.VISIBLE);
        webView.loadUrl(Globals.path + "form?id=" + getIntent().getStringExtra("ID"));
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
            imageUri = FileProvider.getUriForFile(BanshiTXBDActivity.this, "guangming.org.diql.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri

        }
        PhotoUtils.takePicture(BanshiTXBDActivity.this, imageUri, PHOTO_REQUEST);
    }

    private final static int PHOTO_REQUEST = 100;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_home: {
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("确认前往首页？");
                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(BanshiTXBDActivity.this, MainActivity.class);
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
            case R.id.btn_dypz: {
                printPZ(this,"");

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


    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            Log.i("链接地址", url);
            if (url.contains("matterresult")) {
                tvWcbl.setBackgroundResource(R.drawable.buzhou_bg);
                btnDypz.setVisibility(View.VISIBLE);
                view.loadUrl(url);
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
        closeSerialPort();
    }

    private void receiveThread() {
        // 接收
        receiveThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    int size;
                    try {
                        byte[] buffer = new byte[1024];
                        if (mInputStream == null)
                            return;
                        size = mInputStream.read(buffer);

                        if (size > 0) {
                            String recinfo = new String(buffer, 0,
                                    size);
                            Log.i("test", "接收到串口信息:" + recinfo);
//                            sb = recinfo;
//                            handler.sendEmptyMessage(1);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        receiveThread.start();
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {

        if (mSerialPort != null) {
            mSerialPort.close();
        }
        if (mInputStream != null) {
            try {
                mInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mOutputStream != null) {
            try {
                mOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public void printPZ(final Context context, String phone) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("", "");

        new VolleyUtil() {

            @Override
            public <T> boolean analysisData(String response) {
                PrinPZResult s3 = GsonUtils.json2bean(response, PrinPZResult.class);
                Log.i("MainActivity", response);

                if (s3 == null) {
                    Toast.makeText(context, "数据异常",
                            Toast.LENGTH_SHORT).show();


                } else if (s3.getCode() != 0) {
                    Toast.makeText(BanshiTXBDActivity.this, "暂无结果", Toast.LENGTH_SHORT).show();

                } else if (s3.getCode() == 0) {

                    final String num = "                "+s3.getData().getNumber();
                    final String pass = "\n                " +s3.getData().getPassword()+
                            "                        " +
                            "                         " +
                            "                      " +
                            "                    " +
                            "                   ";

//                Toast.makeText(BanshiTXBDActivity.this,"连接失败，请检查打印机连接",Toast.LENGTH_LONG).show();
                    // 打开
                    try {
                        mSerialPort = new SerialPort(new File(prot), baudrate, 0);
                        mInputStream = mSerialPort.getInputStream();
                        mOutputStream = mSerialPort.getOutputStream();
                        receiveThread();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.e(TAG, "打开失败");
                        e.printStackTrace();
                    }
                    // 发送
                    sendThread = new Thread() {
                        @Override
                        public void run() {
                            while (i < 1) {
                                try {


                                    byte[] numtemp = (num).getBytes();
                                    byte[] passtemp = (pass).getBytes();

                                    mOutputStream.write(line);
                                    mOutputStream.write(line);
                                    mOutputStream.write(big);
                                    mOutputStream.write(numtemp);
                                    mOutputStream.write(passtemp);
                                    mOutputStream.write(line);
                                    mOutputStream.write(line);
                                    mOutputStream.write(line);
                                    mOutputStream.write(line);


                                    Log.i("test", "发送成功:1" + i);
                                    Thread.sleep(1);
                                    i++;
                                } catch (Exception e) {
                                    Log.i("test", "发送失败");
                                    e.printStackTrace();
                                }
                            }
                            try {
                                mOutputStream.write(hexKnife);


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    sendThread.start();
                    Toast.makeText(BanshiTXBDActivity.this, "请收好打印凭证", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        }.volleyStringRequestPost(context, params, "getresult?id=" + getIntent().getStringExtra("ID"), null);
    }

}

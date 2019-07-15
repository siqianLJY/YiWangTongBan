package com.example.litianci.yiwangtongban.banshi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facetracking.AFT_FSDKFace;
import com.arcsoft.idcardveri.CompareResult;
import com.arcsoft.idcardveri.DetectFaceResult;
import com.arcsoft.idcardveri.IdCardVerifyError;
import com.arcsoft.idcardveri.IdCardVerifyManager;
import com.arcsoft.liveness.FaceInfo;
import com.example.litianci.yiwangtongban.Constants;
import com.example.litianci.yiwangtongban.Globals;
import com.example.litianci.yiwangtongban.MainActivity;
import com.example.litianci.yiwangtongban.R;
import com.example.litianci.yiwangtongban.been.Face;
import com.example.litianci.yiwangtongban.been.NormalResult;
import com.example.litianci.yiwangtongban.camera.ArcFaceCamera;
import com.example.litianci.yiwangtongban.camera.CameraPreviewListener;
import com.example.litianci.yiwangtongban.facerecognition.FaceRecognitionService;
import com.example.litianci.yiwangtongban.facerecognition.FaceSerchListener;
import com.example.litianci.yiwangtongban.liveness.LivenessActiveListener;
import com.example.litianci.yiwangtongban.liveness.LivenessCheckListener;
import com.example.litianci.yiwangtongban.liveness.LivenessService;
import com.example.litianci.yiwangtongban.util.ImageUtils;
import com.example.litianci.yiwangtongban.utils.GsonUtils;
import com.example.litianci.yiwangtongban.utils.VolleyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BanshiShenfen2Activity extends Activity implements View.OnClickListener, CameraPreviewListener {

    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.btn_return)
    Button btnReturn;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.iv_home)
    ImageView ivHome;
    @Bind(R.id.iv_last)
    ImageView ivLast; 
    @Bind(R.id.tv_theme)
    TextView tvTheme;
    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.surfce_preview)
    SurfaceView surfcePreview;
    @Bind(R.id.surfce_rect)
    SurfaceView surfceRect;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.iv_face)
    ImageView ivFace;
    public static int flag = 3;
    boolean isIdCardReady;
    FaceRecognitionService faceRecognitionService;
    LivenessService livenessService;
    //相机的位置
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    //相机的方向
    private int cameraOri = 90;
    public static List<Face> faces = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banshi_shenfen2);
        ButterKnife.bind(this);
        setOnClick();
        //激活活体检测
        LivenessService.activeEngine(new LivenessActiveListener() {
            @Override
            public void activeSucceed() {
                toast("激活成功");
            }

            @Override
            public void activeFail(String massage) {
                Log.d("激活活体检测失败", massage);
                toast("激活失败：" + massage);
            }
        });
        tvTheme.setText(getIntent().getStringExtra("theme"));
        ArcFaceCamera.getInstance().openCamera(BanshiShenfen2Activity.this, surfcePreview, surfceRect);
        int initResult = IdCardVerifyManager.getInstance().init(Constants.IDCARDAPPID, Constants.FRSDKKEY);
        Log.e("LivenessActivity", "init result: " + initResult);
        inputIdCard();

        faceRecognitionService = new FaceRecognitionService();
        livenessService = new LivenessService();

        ArcFaceCamera.getInstance().setCameraPreviewListener(this);
        ArcFaceCamera.getInstance().init(cameraId);

    }

    public void toast(final String test) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BanshiShenfen2Activity.this, test, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void inputIdCard() {
        Bundle b = getIntent().getExtras();
        Bitmap bitmap = (Bitmap) b.getParcelable("bitmap");
        if (flag != -1) {
            ivFace.setImageBitmap(bitmap);
        }

        byte[] nv21Data = ImageUtils.getNV21(bitmap);

        DetectFaceResult result = IdCardVerifyManager.getInstance().inputIdCardData(nv21Data, bitmap.getWidth(), bitmap.getHeight());
        Log.e("LivenessActivity", "inputIdCardData result: " + result.getErrCode());
        if (result.getErrCode() == IdCardVerifyError.OK) {
            isIdCardReady = true;
            Log.i("是否进入方法", "是");
        }
    }

    public void setOnClick() {
        btnReturn.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        ivHome.setOnClickListener(this);
        ivLast.setOnClickListener(this);
    }

    public void getList(final Context context, String type) {
        Map<String, String> params = new HashMap<String, String>();
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
                    Intent intent = new Intent(BanshiShenfen2Activity.this, BanshiShenfen3Activity.class);
                    intent.putExtra("theme", getIntent().getStringExtra("theme"));
                    intent.putExtra("id", getIntent().getStringExtra("id"));
                    intent.putExtra("name", getIntent().getStringExtra("name"));
                    intent.putExtra("num", getIntent().getStringExtra("num"));
                    startActivity(intent);

                } else if (s3.getCode() == 0) {
                    webview.loadUrl(Globals.path + "beflogin?id="+s3.getMsg());
                    Intent intent = new Intent(BanshiShenfen2Activity.this, BanshiBLXZActivity.class);
                    intent.putExtra("theme", getIntent().getStringExtra("theme"));
                    intent.putExtra("id", getIntent().getStringExtra("id"));
                    startActivity(intent);
                    Toast.makeText(BanshiShenfen2Activity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        }.volleyStringRequestPost(context, params, "checkidcard", null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_return: {

            }
            break;
            case R.id.btn_next: {
//                getList(this, "");

            }
            break;
            case R.id.iv_home: {
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("确认前往首页？");
                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(BanshiShenfen2Activity.this, MainActivity.class);
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

    //开始检测
    public synchronized void detect(final byte[] data, final List<AFT_FSDKFace> fsdkFaces) {


        if (fsdkFaces.size() > 0) {//如果有人脸进行注册、识别
            final AFT_FSDKFace aft_fsdkFace = fsdkFaces.get(0).clone();
            //人脸注册-----------------------------------------------------------------------------------------------------------
            if (flag == 1) {
                flag = -1;
                AFR_FSDKFace afr_fsdkFace = faceRecognitionService.faceData(data, aft_fsdkFace.getRect(), aft_fsdkFace.getDegree());
                faces.add(new Face("用户" + (faces.size() + 1), afr_fsdkFace.getFeatureData()));
                toast("注册成功，姓名为：" + "用户" + faces.size());
                finish();
                //人脸对比----------------------------------------------------------------------------------------------
            } else if (flag == 2) {
                AFR_FSDKFace afr_fsdkFace = faceRecognitionService.faceData(data, aft_fsdkFace.getRect(), aft_fsdkFace.getDegree());

                List<byte[]> faceList = new ArrayList<>();
                for (Face face : faces) {
                    faceList.add(face.getData());
                }

                faceRecognitionService.faceSerch(afr_fsdkFace.getFeatureData(), faceList, new FaceSerchListener() {
                    @Override
                    public void serchFinish(float sorce, int position) {
                        Log.e("LivenessActivity", "sorce：" + sorce + "，position：" + position);
                        if (sorce > 0.7) {
                            tvName.setText(faces.get(position).getName() + "：相似度：" + sorce);
//                            iv_face.setImageBitmap(ImageUtils.cropFace(data, aft_fsdkFace.getRect(), mWidth, mHeight, cameraOri));
                        } else {
//                            tv_name.setText("");
                        }
                    }
                });
                flag = -1;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            flag = 2;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                //人证识别----------------------------------------------------------------------------------------------
            } else if (flag == 3) {//人证识别
                if (isIdCardReady) {
                    DetectFaceResult result = IdCardVerifyManager.getInstance().onPreviewData(data, mWidth, mHeight, true);
                    if (result.getErrCode() == IdCardVerifyError.OK) {
                        Log.e("LivenessActivity", "onPreviewData video result: " + result);
                        compare();
                        flag = -1;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    inputIdCard();
                                    Thread.sleep(500);
                                    flag = 3;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            }
        }


        //活体检测------------------------------------------------------------------------------------------------------------------------------
        List<FaceInfo> faceInfos = new ArrayList<>();
        if (fsdkFaces.size() > 0) {
            faceInfos.add(new FaceInfo(fsdkFaces.get(0).getRect(), fsdkFaces.get(0).getDegree()));
        }

        livenessService.isLive(faceInfos, data.clone(), livenessCheckListener);


    }

    LivenessCheckListener livenessCheckListener = new LivenessCheckListener() {
        @Override
        public void noFace() {
            tvStatus.setText("没有人脸");
        }

        @Override
        public void notSignleFace() {
            //tv_status.setText("人脸太多");
        }

        @Override
        public void liveness() {
            tvStatus.setText("活体通过");
        }

        @Override
        public void livenessNot() {
            tvStatus.setText("非活体");
        }

        @Override
        public void unknownEorr() {
            tvStatus.setText("未知错误");
        }
    };
    //比对阈值，建议为0.82
    private static final double THRESHOLD = 0.82d;

    private void compare() {

        CompareResult compareResult = IdCardVerifyManager.getInstance().compareFeature(THRESHOLD);
        Log.e("LivenessActivity", "compareFeature: result " + compareResult.getResult() + ", isSuccess "
                + compareResult.isSuccess() + ", errCode " + compareResult.getErrCode());

        if (compareResult.isSuccess()) {
            tvName.setText("相似度为：" + compareResult.getResult());
            if (compareResult.getResult() > THRESHOLD) {
                getList(this, "");
            }

//            else {
//                AlertDialog.Builder adb = new AlertDialog.Builder(this);
//                adb.setTitle("请携带本人身份证前来办理");
//                adb.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                adb.create().show();
//            }
        }
        else {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("请携带本人身份证前来办理");
            adb.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            adb.create().show();
        }
    }

    @Override
    public void onPreviewData(byte[] data, List<AFT_FSDKFace> fsdkFaces) {
        detect(data, fsdkFaces);
    }

    int mWidth, mHeight;

    @Override
    public void onPreviewSize(int width, int height) {
        mHeight = height;
        mWidth = width;
        livenessService.setSize(width, height);
        faceRecognitionService.setSize(width, height);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        livenessService.destoryEngine();
        faceRecognitionService.destroyEngine();
        IdCardVerifyManager.getInstance().unInit();
    }
}

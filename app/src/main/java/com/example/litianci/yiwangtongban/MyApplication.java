package com.example.litianci.yiwangtongban;

import android.app.Application;

import com.example.litianci.yiwangtongban.builder.AcrFaceManagerBuilder;


public class MyApplication extends Application {

    public static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        initArcFace();
//        PicturePickUtil.init("com.yorhp.arcface.fileProvider");
    }

    private void initArcFace() {

        new AcrFaceManagerBuilder().setContext(this)
                .setFreeSdkAppId(Constants.FREESDKAPPID)
                .setFdSdkKey(Constants.FDSDKKEY)
                .setFtSdkKey(Constants.FTSDKKEY)
                .setFrSdkKey(Constants.FRSDKKEY)
                .setLivenessAppId(Constants.LIVENESSAPPID)
                .setLivenessSdkKey(Constants.LIVENESSSDKKEY)
                .create();
    }

}




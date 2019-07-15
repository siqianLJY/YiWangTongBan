package com.example.litianci.yiwangtongban.builder;

import android.content.Context;

import com.example.litianci.yiwangtongban.manager.ArcFaceManager;


public class AcrFaceManagerBuilder {
    ArcFaceManager arcFaceManager = new ArcFaceManager();


    public AcrFaceManagerBuilder setContext(Context context) {
        arcFaceManager.setContext(context);
        return this;
    }


    public AcrFaceManagerBuilder setFreeSdkAppId(String freeSdkAppId) {
        arcFaceManager.setFreeSdkAppId(freeSdkAppId);
        return this;
    }

    public AcrFaceManagerBuilder setFdSdkKey(String fdSdkKey) {
        arcFaceManager.setFdSdkKey(fdSdkKey);
        return this;
    }

    public AcrFaceManagerBuilder setFrSdkKey(String frSdkKey) {
        arcFaceManager.setFrSdkKey(frSdkKey);
        return this;
    }


    public AcrFaceManagerBuilder setFtSdkKey(String ftSdkKey) {
        arcFaceManager.setFtSdkKey(ftSdkKey);
        return this;
    }


    public AcrFaceManagerBuilder setLivenessAppId(String livenessAppId) {
        arcFaceManager.setLivenessAppId(livenessAppId);
        return this;
    }

    public AcrFaceManagerBuilder setLivenessSdkKey(String livenessSdkKey) {
        arcFaceManager.setLivenessSdkKey(livenessSdkKey);
        return this;
    }

    public ArcFaceManager create() {
        return arcFaceManager;
    }

}

package com.example.litianci.yiwangtongban.manager;

import android.content.Context;

import com.example.litianci.yiwangtongban.facefind.FaceFindService;
import com.example.litianci.yiwangtongban.facerecognition.FaceRecognitionService;
import com.example.litianci.yiwangtongban.facetrack.FaceTrackService;
import com.example.litianci.yiwangtongban.liveness.LivenessService;


/**
 * 初始化sdkKey和appId
 */
public class ArcFaceManager {

    public static Context context;

    public static void setFreeSdkAppId(String freeSdkAppId) {
        FaceFindService.setAPPID(freeSdkAppId);
        FaceTrackService.setAPPID(freeSdkAppId);
        FaceRecognitionService.setAPPID(freeSdkAppId);
    }

    public static void setFdSdkKey(String fdSdkKey) {
        FaceFindService.setFdSdkkey(fdSdkKey);
    }

    public static void setFtSdkKey(String ftSdkKey) {
        FaceTrackService.setFtSdkkey(ftSdkKey);
    }

    public static void setLivenessAppId(String livenessAppId) {
        LivenessService.setAPPID(livenessAppId);
    }

    public static void setLivenessSdkKey(String livenessSdkKey) {
        LivenessService.setLivenessSdkkey(livenessSdkKey);
    }

    public static void setFrSdkKey(String frSdkKey) {
        FaceRecognitionService.setFrSdkkey(frSdkKey);
    }

    public static void setContext(Context context) {
        ArcFaceManager.context = context;
    }

    public static Context getContext() {
        return context;
    }

}

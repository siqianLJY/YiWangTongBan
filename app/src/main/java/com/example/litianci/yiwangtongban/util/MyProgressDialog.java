package com.example.litianci.yiwangtongban.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by zhaodianbo on 2016-11-25.
 */
public class MyProgressDialog {
    ProgressDialog m_pDialog;

    public MyProgressDialog(Context context, String msg, DialogInterface.OnCancelListener onCancelListener){
        m_pDialog = new ProgressDialog(context);
        m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_pDialog.setMessage(msg);
        m_pDialog.setCancelable(false);
        m_pDialog.setOnCancelListener(onCancelListener);
        m_pDialog.show();
    }

    public void dismiss(){
        try{
            m_pDialog.dismiss();
        }catch (Exception e){
        }
    }
}

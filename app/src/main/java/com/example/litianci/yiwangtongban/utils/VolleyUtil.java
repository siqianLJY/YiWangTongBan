package com.example.litianci.yiwangtongban.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.litianci.yiwangtongban.Globals;

import java.util.Map;

/**
 * 2015年5月5日17:03:46
 */
public abstract class VolleyUtil {
    private final int SPLASH_DISPLAY_LENGHT = 1500;

    // RetryPolicy retryPolicy =
    // post请求封装
    public <T> void volleyStringRequestPost(final Context context,
                                            final Map<String, String> paramsss, String servlet, final Object ob) {

        Log.i("请求值", "paramsss=" + paramsss);
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        if (ob == null) {
            if (context != null) {
            }
        }

        StringRequest sr = new StringRequest(Request.Method.POST,
                Globals.path + servlet, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (ob != null) {
//                    ((PullToRefreshListView)ob).onRefreshComplete();
                }

                if (response.contains("\"Stu\":0")) {
                    Log.i("数据", "res:" + response);
                    Toast.makeText(context, "数据异常",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response != null) {
                    Log.i("数据", "res:" + response);
                    analysisData(response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                if (ob != null) {
//                    ((PullToRefreshListView)ob).onRefreshComplete();
                }
                Log.i("Response", "error" + error.getMessage());


                if (AppUtil.networkCheck() == false) {
                    Toast.makeText(Globals.context, "没有网络", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "数据异常",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return paramsss;
            }
        };


        // sr.setRetryPolicy(new DefaultRetryPolicy(15 * 1000,
        // DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        sr.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        Log.i("asd123456", sr.toString());
        mRequestQueue.add(sr);
        // new Handler().postDelayed(new Runnable() {
        // public void run() {
        // if (au != null) {
        // au.closeDialog();
        // }
        // }
        // }, SPLASH_DISPLAY_LENGHT);

    }

    public abstract <T> boolean analysisData(String response);

}

package com.example.litianci.yiwangtongban.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhaodianbo on 2016-3-16.
 */
public class LocalSharedPreferences {
    private static String SP_KEY="info";

    public static SharedPreferences getSp(Context mContext){
        return mContext.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE);
    }
/*    public static String getString(Context context,String preferencesName,String defValue){
        return getSp(context).getString(preferencesName, defValue);
    }
    public static boolean setString(Context context,String preferencesName,String value ){
        return getSp(context).edit().putString(preferencesName,value).commit();
    }

    public static Boolean getBoolean(Context context,String preferencesName,boolean defValue){
        return getSp(context).getBoolean(preferencesName, defValue);
    }
    public static boolean setBoolean(Context context,String preferencesName,boolean value ){
        return getSp(context).edit().putBoolean(preferencesName,value).commit();
    }*/

}


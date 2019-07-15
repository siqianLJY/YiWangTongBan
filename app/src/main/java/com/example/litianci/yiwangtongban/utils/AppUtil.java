package com.example.litianci.yiwangtongban.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.litianci.yiwangtongban.Globals;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import org.apache.log4j.Logger;

//import me.huixin.tryst.ui.LoginActivity;

/**
 * Created by Administrator on 13-12-16.
 */
public class AppUtil {
    // private static Logger logger = LogHelper.getLogger(AppUtil.class);
    private static String DeviceId;

    // /**
    // * 安装快捷图标
    // */
    // public static void installShortCut(Context activity, int shortCutIcon) {
    //
    // Intent shortcutIntent = new Intent();
    // Intent intent;
    //
    // //创建快捷方式的Intent
    // shortcutIntent = new Intent(
    // "com.android.launcher.action.INSTALL_SHORTCUT");
    // //不允许重复创建
    // shortcutIntent.putExtra("duplicate", false);
    // //需要现实的名称
    // shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "面具");
    // Parcelable shortIcon = Intent.ShortcutIconResource.fromContext(
    // activity, shortCutIcon);
    //
    // shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, shortIcon);
    //
    // intent = new Intent();
    // intent.setClass(activity.getApplicationContext(), LoginActivity.class);
    // intent.setAction(Intent.ACTION_MAIN);
    // intent.addCategory("android.intent.category.LAUNCHER");
    //
    // //点击快捷图片，运行的程序主入口
    // shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
    // //发送广播。OK
    // activity.sendBroadcast(shortcutIntent);
    // }


    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    /**
     * 获取应用程序的版本号
     *
     * @return
     */

    public static String getVersion() {
        try {
            return Globals.context.getPackageManager().getPackageInfo(
                    Globals.context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 调起系统发短信功能
     *
     * @param phoneNumber
     * @param message
     */
    public void doSendSMSTo(String phoneNumber, String message) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            intent.putExtra("sms_body", message);
//			startActivity(intent);
        }
    }

    /**
     * 安装应用程序
     *
     * @param t
     */
    public static void installApk(File t, Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(t),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);

        // try {
        // Runtime.getRuntime().exec("pm install " + t.getAbsolutePath());
        // Log.i(TAG,"install success");
        // } catch (IOException e) {
        //
        // Log.e(TAG,e.toString());
        // }

    }

    /**
     * 获取sim序列号
     *
     * @return 返回sim序列号
     */

    public static String getSimIMSI() {
        TelephonyManager telManager = (TelephonyManager) Globals.context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telManager.getSubscriberId();
        return imsi;
    }

    public static String getMobile() {
        TelephonyManager telephonyManager = (TelephonyManager) Globals.context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String mobile = telephonyManager.getLine1Number();
        return mobile;
    }

    /**
     * 获取手机设备号
     *
     * @return 手机设备号
     */

    public static String getDeviceId() {
        if (DeviceId == null) {
            final TelephonyManager tm = (TelephonyManager) Globals.context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = ""
                    + android.provider.Settings.Secure.getString(
                    Globals.context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(),
                    ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            //获得是手机uuid
//			DeviceId = deviceUuid.toString();
            //获得是手机的imei码
            DeviceId = "" + tm.getDeviceId();
        }
        return DeviceId;
    }



    /**
     * 获取手机设备号（uuid）
     *
     * @return 手机设备号
     */

    public static String getDeviceId2() {
        if (DeviceId == null) {
            final TelephonyManager tm = (TelephonyManager) Globals.context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = ""
                    + android.provider.Settings.Secure.getString(
                    Globals.context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(),
                    ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            //获得是手机uuid
            DeviceId = deviceUuid.toString();
        }
        return DeviceId;
    }



    public static boolean isTopActivty(String packageNameFlg) {
        ActivityManager activityManager = (ActivityManager) Globals.context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager
                .getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            ComponentName cn = tasksInfo.get(0).topActivity;
            if (cn.getClassName().indexOf(packageNameFlg) >= 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTopActivty(Class<?> cls) {
        ActivityManager activityManager = (ActivityManager) Globals.context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager
                .getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            // 应用程序位于堆栈的顶层
            ComponentName cn = tasksInfo.get(0).topActivity;
            Class<?> clsName = cls;
            int i = 0;
            while (clsName != Object.class && i < 20) {
                i++;
                boolean f = cn.getClassName().equals(clsName.getName());
                if (f) {
                    return true;
                }
                clsName = cls.getSuperclass();
            }
        }
        return false;
    }

    /**
     * 判断网络状态
     *
     * @return
     */
    public static boolean networkCheck() {
        // WIFI处于连接
        boolean isWIFI = isWIFIConnectivity();
        // Mobile连接
        boolean isMobile = isMobileConnectivity(Globals.context);
        return isWIFI || isMobile;
    }

    /**
     * @param context
     * @return
     */
    public static boolean isMobileConnectivity(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null) {
            return networkInfo.isConnected();
        }
        return false;
    }

    /**
     * WIFI是否处于连接
     *
     * @param
     * @return
     */
    public static boolean isWIFIConnectivity() {
        ConnectivityManager manager = (ConnectivityManager) Globals.context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null) {
            return networkInfo.isConnected();
        }
        return false;
    }

    public static String getTopActivityName() {
        ActivityManager activityManager = (ActivityManager) Globals.context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager
                .getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            // 应用程序位于堆栈的顶层
            return tasksInfo.get(0).topActivity.getClassName();
        }
        return "";
    }

    public static boolean isTopActivity() {
        ActivityManager activityManager = (ActivityManager) Globals.context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager
                .getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            // 应用程序位于堆栈的顶层
            if (Globals.context.getPackageName().equals(
                    tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static ComponentName getTopActivity() {
        ActivityManager activityManager = (ActivityManager) Globals.context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager
                .getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            // 应用程序位于堆栈的顶层
            return tasksInfo.get(0).topActivity;
        }
        return null;
    }

    /**
     * 收集设备信息.
     *
     * @return
     */


    // public static boolean isWorked(Class<?> cls) {
    // ActivityManager myManager = (ActivityManager) BaseApplication.context
    // .getSystemService(Context.ACTIVITY_SERVICE);
    // ArrayList<RunningServiceInfo> runningService =
    // (ArrayList<RunningServiceInfo>) myManager.getRunningServices(30);
    // for (int i = 0; i < runningService.size(); i++) {
    // if (runningService.get(i).service.getClass().equals(cls)) {
    // return true;
    // }
    // }
    // return false;
    // }

    /**
     * 把uri地址转化为文件路径
     *
     * @param
     * @return
     */
    public static String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        if (contentUri.getScheme() == null
                || !contentUri.getScheme().equals("content")) {
            return contentUri.getPath();
        }
        ContentResolver contentResolver = Globals.context.getContentResolver();
        Cursor cursor = contentResolver.query(contentUri, proj, null, null,
                null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String res = cursor.getString(index);
        cursor.close();
        return res;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = Globals.context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = Globals.context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 验证手机号：
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$"
                );
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();
    }

    /**
     * 验证邮箱：
     */
    public static boolean isEmail(String strEmail) {
//		String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        String strPattern = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    /**
     * 身份证验证：
     */
    public static boolean isID(String s) {
        String strPattern = "^\\d{15}|^\\d{17}([0-9]|X|x)$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(s);
        return m.matches();
    }

    //判断文件是否存在
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * <pre>
     * 根据指定的日期字符串获取星期几
     * </pre>
     *
     * @param strDate 指定的日期字符串(yyyy-MM-dd 或 yyyy/MM/dd)
     * @return week
     * 星期几(MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY)
     */
    public static String getWeek(String strDate) {
        int year = Integer.parseInt(strDate.substring(0, 4));
        int month = Integer.parseInt(strDate.substring(5, 7));
        int day = Integer.parseInt(strDate.substring(8, 10));

        Calendar c = Calendar.getInstance();
        Log.i("zhang", year + "-" + month + "-" + day);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);

        String week = "";
        int weekIndex = c.get(Calendar.DAY_OF_WEEK);

        switch (weekIndex) {
            case 1:
                week = "日";
                break;
            case 2:
                week = "一";
                break;
            case 3:
                week = "二";
                break;
            case 4:
                week = "三";
                break;
            case 5:
                week = "四";
                break;
            case 6:
                week = "五";
                break;
            case 7:
                week = "六";
                break;
        }
        return week;
    }

    /**
     * 是不是网址：
     */
    public static boolean isWeb(String s) {
        String strPattern = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 下载文件的分割方法
     */
    public static String wenjianDivision(String pk, String name, String url) {
        Log.i("zhang", "我的pk" + pk);
        String filenames = url.substring(url.lastIndexOf('/') + 1);

        String[] filename2s = name.split("\\.");
        Log.i("zhang______", "分割个数" + filename2s.length);
        String myname = filenames.substring(2, 14);
        String filename = filename2s[0] + " " + myname + "." + filename2s[filename2s.length - 1];
        Log.i("zhang______", "我的文件名字" + filename);
        return filename;
    }

    /**
     * 获取文件格式 返回的使用打开文件的intent
     */
    public static Intent getFileIntent(File file) {
        //Uri uri = Uri.parse("http://m.ql18.com.cn/hpf10/1.pdf");
        Uri uri = Uri.fromFile(file);
        String type = getMIMEType(file);
        Log.i("tag", "type=" + type);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, type);
        return intent;
    }

    /**
     * 判断文件的格式使用不同的action打开文件
     */
    public static String getMIMEType(File f) {
        //如果没有匹配的打开文件的情况现在所有的程序打开文件
        String type = "*/*";
        String fName = f.getName();
        /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;

    }

    public static final String[][] MIME_MapTable = {
            //{后缀名， MIME类型}
            {"3gp", "video/3gpp"},
            {"apk", "application/vnd.android.package-archive"},
            {"asf", "video/x-ms-asf"},
            {"avi", "video/x-msvideo"},
            {"bin", "application/octet-stream"},
            {"bmp", "image/bmp"},
            {"c", "text/plain"},
            {"class", "application/octet-stream"},
            {"conf", "text/plain"},
            {"cpp", "text/plain"},
            {"doc", "application/msword"},
            {"docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {"xls", "application/vnd.ms-excel"},
            {"xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {"exe", "application/octet-stream"},
            {"gif", "image/gif"},
            {"gtar", "application/x-gtar"},
            {"gz", "application/x-gzip"},
            {"h", "text/plain"},
            {"htm", "text/html"},
            {"html", "text/html"},
            {"jar", "application/java-archive"},
            {"java", "text/plain"},
            {"jpeg", "image/jpeg"},
            {"jpg", "image/jpeg"},
            {"js", "application/x-javascript"},
            {"log", "text/plain"},
            {"m3u", "audio/x-mpegurl"},
            {"m4a", "audio/mp4a-latm"},
            {"m4b", "audio/mp4a-latm"},
            {"m4p", "audio/mp4a-latm"},
            {"m4u", "video/vnd.mpegurl"},
            {"m4v", "video/x-m4v"},
            {"mov", "video/quicktime"},
            {"mp2", "audio/x-mpeg"},
            {"mp3", "audio/x-mpeg"},
            {"mp4", "video/mp4"},
            {"mpc", "application/vnd.mpohun.certificate"},
            {"mpe", "video/mpeg"},
            {"mpeg", "video/mpeg"},
            {"mpg", "video/mpeg"},
            {"mpg4", "video/mp4"},
            {"mpga", "audio/mpeg"},
            {"msg", "application/vnd.ms-outlook"},
            {"ogg", "audio/ogg"},
            {"pdf", "application/pdf"},
            {"png", "image/png"},
            {"pps", "application/vnd.ms-powerpoint"},
            {"ppt", "application/vnd.ms-powerpoint"},
            {"pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {"prop", "text/plain"},
            {"rc", "text/plain"},
            {"rmvb", "audio/x-pn-realaudio"},
            {"rtf", "application/rtf"},
            {"sh", "text/plain"},
            {"tar", "application/x-tar"},
            {"tgz", "application/x-compressed"},
            {"txt", "text/plain"},
            {"wav", "audio/x-wav"},
            {"wma", "audio/x-ms-wma"},
            {"wmv", "audio/x-ms-wmv"},
            {"wps", "application/vnd.ms-works"},
            {"xml", "text/plain"},
            {"z", "application/x-compress"},
            {"zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    /**
     * 设置状态栏背景状态
     */
    public static void setTranslucentStatus(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemStatusManager tintManager = new SystemStatusManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(0);//状态栏无背景
    }


    //将uri转换成String形式
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}

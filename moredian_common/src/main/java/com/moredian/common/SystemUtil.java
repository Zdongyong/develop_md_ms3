package com.moredian.common;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * 创建日期：2018/11/19 on 14:43
 * 描述：
 * 作者：zhudongyong
 */
public class SystemUtil {

    public final static String TAG = "SystemUtil";

    public static String getProcessName( Context ctx ){
        int pid = android.os.Process.myPid();
        String processName;
        ActivityManager am = (ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : am.getRunningAppProcesses()){
            if(appProcess.pid == pid){
                processName = appProcess.processName;
                return processName;
            }
        }

        return "";
    }



    public static String getAppVersion( Context context ) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        }catch (Exception e) {
            Log.e(TAG, "getPackageInfo exception: " + e.toString() );
            e.printStackTrace();
        }
        return "null";
    }

    public static String getSystemVersion(){
        return "Model=" + android.os.Build.MODEL + " ,Android="
                + android.os.Build.VERSION.RELEASE +  " ,Disp=" + android.os.Build.DISPLAY;
    }

}

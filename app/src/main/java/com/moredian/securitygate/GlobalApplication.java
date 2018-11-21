package com.moredian.securitygate;

import android.app.Application;
import android.content.Context;
import com.moredian.common.SystemUtil;
import com.moredian.common.arouter.ArouterConfig;

/**
 */
public class GlobalApplication extends Application {

    private static final String TAG = GlobalApplication.class.getSimpleName();
    public final static String MAIN_PROCESS_NAME = "com.moredian.securitygate";
    private static Context appConext;

    @Override
    public void onCreate() {
        super.onCreate();
        String processName = SystemUtil.getProcessName(getApplicationContext());
        if (MAIN_PROCESS_NAME.equals(processName)) {
            appConext = getApplicationContext();
            //初始化Arouter
            ArouterConfig.init(this,BuildConfig.DEBUG);
        }

    }

    public static Context getContext() {
        return appConext;
    }

}
package com.moredian.common.arouter;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * 创建日期：2018/11/19 on 15:44
 * 描述：
 * 作者：zhudongyong
 */
public class ArouterConfig {

    public static void init(Application app, boolean isDebug) {
        if (true) {
            ARouter.openLog();  // 打印日志
            ARouter.openDebug();    // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(app);      // 尽可能早，推荐在Application中初始化
    }
}

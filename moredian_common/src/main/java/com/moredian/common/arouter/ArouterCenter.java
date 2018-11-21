package com.moredian.common.arouter;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * 创建日期：2018/11/19 on 15:44
 * 描述：
 * 作者：zhudongyong
 */
public class ArouterCenter {

    /**
     * 测试首页
     */
    public static void toMain() {
        ARouter.getInstance().build(RouterURLS.BASE_MAIN).navigation();
    }


}

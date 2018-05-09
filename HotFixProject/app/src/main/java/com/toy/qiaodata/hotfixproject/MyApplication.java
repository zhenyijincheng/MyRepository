package com.toy.qiaodata.hotfixproject;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by YANG on 2018/5/9.
 */

public class MyApplication extends TinkerApplication {
    public MyApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL,
                "com.toy.qiaodata.hotfixproject.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}

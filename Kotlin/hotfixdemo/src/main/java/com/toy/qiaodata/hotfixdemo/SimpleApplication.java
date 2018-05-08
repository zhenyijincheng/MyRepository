package com.toy.qiaodata.hotfixdemo;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by YANG on 2018/5/7.
 */

public class SimpleApplication extends TinkerApplication {
//public class SimpleApplication extends Application {

    public SimpleApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL,
                "com.toy.qiaodata.hotfixdemo.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}

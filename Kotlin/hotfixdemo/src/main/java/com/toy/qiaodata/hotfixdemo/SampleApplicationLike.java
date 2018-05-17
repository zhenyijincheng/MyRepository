package com.toy.qiaodata.hotfixdemo;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.tinker.lib.listener.DefaultPatchListener;
import com.tencent.tinker.lib.listener.PatchListener;
import com.tencent.tinker.lib.patch.AbstractPatch;
import com.tencent.tinker.lib.patch.UpgradePatch;
import com.tencent.tinker.lib.reporter.DefaultLoadReporter;
import com.tencent.tinker.lib.reporter.DefaultPatchReporter;
import com.tencent.tinker.lib.reporter.LoadReporter;
import com.tencent.tinker.lib.reporter.PatchReporter;
import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.SharePatchInfo;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * Created by YANG on 2018/5/7.
 */

public class SampleApplicationLike extends DefaultApplicationLike {
    public static final String TAG = "HOTFIX__123";

    public SampleApplicationLike(Application application, int tinkerFlags,
                                 boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                                 long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        Bugly.init(getApplication(), "c955598d4e", false);
        //是测试机
        Bugly.setIsDevelopmentDevice(getApplication(),true);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        installTinker();


        Beta.canNotifyUserRestart = true;

        Beta.autoCheckUpgrade = false;
//        Beta.canAutoDownloadPatch = false;
//        Beta.autoDownloadOnWifi = false;
//        Beta.canAutoPatch = false;


    }

    private void installTinker() {
//        // 安装tinker
//        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
//        Beta.installTinker(this);

        // or you can just use DefaultLoadReporter
        LoadReporter loadReporter = new SampleLoadReporter(getApplication());
        // or you can just use DefaultPatchReporter
        PatchReporter patchReporter = new SamplePatchReporter(getApplication());
        // or you can just use DefaultPatchListener
        PatchListener patchListener = new SamplePatchListener(getApplication());
        // you can set your own upgrade patch if you need
        AbstractPatch upgradePatchProcessor = new SimpleUpgradePatch();
        // you can set your own repair patch if you need
        AbstractPatch repairPatchProcessor = new RepairPatch();
        TinkerManager.TinkerPatchResultListener patchResultListener = new TinkerManager.TinkerPatchResultListener() {
            @Override
            public void onPatchResult(PatchResult result) {
                // you can get the patch result
            }
        };
        Beta.installTinker(this,loadReporter,patchReporter,patchListener,patchResultListener,upgradePatchProcessor);

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    private class SampleLoadReporter extends DefaultLoadReporter {

        public SampleLoadReporter(Context context) {
            super(context);
        }

        @Override
        public void onLoadPatchListenerReceiveFail(File file, int i) {
            super.onLoadPatchListenerReceiveFail(file,i);
            Log.d(TAG,"onLoadPatchListenerReceiveFail:" + file.getName() + "  i:" + i);
        }

        @Override
        public void onLoadPatchVersionChanged(String s, String s1, File file, String s2) {
            super.onLoadPatchVersionChanged(s, s1, file, s2);
            Log.d(TAG,"onLoadPatchVersionChanged:" + file.getName() + "  s:" + s + "  s1:" + s1 + "  s2:" + s2);
        }

        @Override
        public void onLoadInterpret(int i, Throwable throwable) {
            super.onLoadInterpret(i, throwable);
            Log.d(TAG,"onLoadInterpret:" + throwable.getMessage() + "  i:" + i);

        }

        @Override
        public void onLoadResult(File file, int i, long l) {
            super.onLoadResult(file, i, l);
            Log.d(TAG,"onLoadResult:" + file.getName() + "  i:" + i + "  l:" + l);

        }

        @Override
        public void onLoadException(Throwable throwable, int i) {
            super.onLoadException(throwable, i);
            Log.d(TAG,"onLoadException:" + throwable.getMessage() + "  i:" + i);

        }

        @Override
        public void onLoadFileNotFound(File file, int i, boolean b) {
            super.onLoadFileNotFound(file, i, b);
            Log.d(TAG,"onLoadFileNotFound:" + file.getName() + "  i:" + i + " b:" + b);
        }

        @Override
        public void onLoadFileMd5Mismatch(File file, int i) {
            super.onLoadFileMd5Mismatch(file,i);
            Log.d(TAG,"onLoadFileMd5Mismatch:" + file.getName() + "  i:" + i);

        }

        @Override
        public void onLoadPatchInfoCorrupted(String s, String s1, File file) {
            onLoadPatchInfoCorrupted(s, s1, file);
            Log.d(TAG,"onLoadPatchInfoCorrupted:" + file.getName() + "  s:" + s + "  s1:" + s1);

        }

        @Override
        public void onLoadPackageCheckFail(File file, int i) {
            super.onLoadPackageCheckFail(file, i);
            Log.d(TAG,"onLoadPackageCheckFail:" + file.getName() + "  i:" + i);

        }
    }

    private class SamplePatchReporter extends DefaultPatchReporter {

        public SamplePatchReporter(Context context) {
            super(context);
        }

        @Override
        public void onPatchServiceStart(Intent intent) {
            super.onPatchServiceStart(intent);
            Log.d(TAG,"SamplePatchReporter:" + intent.getAction());
        }

        @Override
        public void onPatchPackageCheckFail(File file, int i) {
            super.onPatchPackageCheckFail(file, i);
            Log.d(TAG,"onPatchPackageCheckFail:" + file.getName() + "  i:" + i);

        }

        @Override
        public void onPatchVersionCheckFail(File file, SharePatchInfo sharePatchInfo, String s) {
            super.onPatchVersionCheckFail(file, sharePatchInfo, s);
            Log.d(TAG,"onPatchVersionCheckFail:" + file.getName() + "  s:" + s + " sharePatchInfo:" + sharePatchInfo.fingerPrint);
        }

        @Override
        public void onPatchTypeExtractFail(File file, File file1, String s, int i) {
            super.onPatchTypeExtractFail(file, file1, s, i);
            Log.d(TAG,"onPatchTypeExtractFail:" + file.getName() + "  file1:" + file1.getName() + " i:" + i);

        }

        @Override
        public void onPatchDexOptFail(File file, List<File> list, Throwable throwable) {
            super.onPatchDexOptFail(file, list, throwable);
            Log.d(TAG,"onPatchDexOptFail:" + file.getName() + "  throwable:" + throwable.getMessage());

        }

        @Override
        public void onPatchResult(File file, boolean b, long l) {
            super.onPatchResult(file, b, l);
            Log.d(TAG,"onPatchResult:" + file.getName());

        }

        @Override
        public void onPatchException(File file, Throwable throwable) {
            super.onPatchException(file, throwable);
            Log.d(TAG,"onPatchException:" + file.getName());

        }

        @Override
        public void onPatchInfoCorrupted(File file, String s, String s1) {
            super.onPatchInfoCorrupted(file, s, s1);
            Log.d(TAG,"onPatchInfoCorrupted:" + file.getName());

        }
    }

    private class SamplePatchListener extends DefaultPatchListener {

        public SamplePatchListener(Context context) {
            super(context);
        }

        @Override
        public int onPatchReceived(String s) {
            Log.d(TAG,"onPatchReceived:" + s);
            return super.onPatchReceived(s);
        }
    }

    private class RepairPatch extends AbstractPatch {
        @Override
        public boolean tryPatch(Context context, String s, PatchResult patchResult) {
            Log.d(TAG,"tryPatch:" + s);
            return false;
        }
    }

    private class SimpleUpgradePatch extends UpgradePatch {
        @Override
        public boolean tryPatch(Context context, String tempPatchPath, PatchResult patchResult) {
            Log.d(TAG,"tryPatch:" + tempPatchPath);
            return super.tryPatch(context, tempPatchPath, patchResult);
        }
    }
}

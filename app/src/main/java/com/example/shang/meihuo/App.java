package com.example.shang.meihuo;

import android.app.Application;
import android.util.Log;

import com.example.shang.meihuo.utils.Utils;
import com.squareup.leakcanary.LeakCanary;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;


public class App extends Application {
    private static App INSTANCE;

    public static App getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化 LeakCanary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        BGASwipeBackManager.getInstance().init(this);
        ConfigManage.INSTANCE.initConfig(this);
        INSTANCE = this;
        Utils.init(this);
        Log.i("xyz","执行了app");
    }
}

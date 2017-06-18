package com.example.shang.meihuo.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 包相关工具类
 *
 */

public class PackageUtil {

    /**
     * 获取当前应用的版本号
     */
    public static String getVersionName() {
        // 获取packagemanager的实例
        PackageManager packageManager = Utils.getContext().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(Utils.getContext().getPackageName(), 0);
//            Log.i("packageName","         "+Utils.getContext().getPackageName()); //packageName:    com.nanchen.aiyagirl
//            Log.i("packinfo","    "+packInfo); //packinfo:     PackageInfo{46554de8 com.nanchen.aiyagirl
//            Log.i("version"," "+packInfo.versionName); //version:  1.0.2
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "1.0";
        }
    }
}

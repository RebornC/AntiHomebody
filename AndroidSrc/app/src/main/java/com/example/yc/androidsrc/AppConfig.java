package com.example.yc.androidsrc;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * 初始化全局配置
 *
 * @author RebornC
 * Created on 2018/12/12.
 */

public class AppConfig extends Application {

    private static final String applicationID = "62dddc420e225515924df91ad0cd2823";

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化Bmob
        Bmob.initialize(this, applicationID);
    }
}

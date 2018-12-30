package com.example.yc.androidsrc.app;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * 初始化全局配置
 *
 * @author RebornC
 * Created on 2018/12/12.
 */

public class AppConfig extends Application {

    /**
     * 后端密钥
     */
    private static final String applicationID = "62dddc420e225515924df91ad0cd2823";


    /**
     * 非宅APP的等级数据设计
     * 用户初创建后等级为1，通过获取能量并浇灌后升级，最高等级为8
     * 每次升级所需能量 = 350 * (当前等级)^2 + 200
     * levelEnergy[n]：从n级升至n+1级所需要的能量
     */
    private static final Integer[] levelEnergy = {0, 550, 1600, 3350, 5600, 8750, 12600, 17500};

    public Integer getLevelEnergy(Integer curLevel) {
        return levelEnergy[curLevel];
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化Bmob
        Bmob.initialize(this, applicationID);
    }
}

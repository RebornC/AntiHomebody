package com.example.yc.androidsrc.common;

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

    /**
     * 目标步数
     */
    private static final int targetStepNumber = 8000;

    /**
     * 系数，当步数低于目标步数时，获得的能量=步数*系数
     */
    private static final double coefficient = 0.04;

    /**
     * 当步数不低于目标步数时，获得的能量上限（额外奖励30）
     */
    private static final int maxStepEnergy = 350;

    /**
     * 每天通过计划打卡获得的能量值=maxPlanEnergy*完成度
     */
    private static final int maxPlanEnergy = 300;

    /**
     * 用static修饰类方法，这样可直接通过类调用而不用创建对象
     */

    public static Integer getLevelEnergy(Integer curLevel) {
        return levelEnergy[curLevel];
    }

    public static int getTargetStepNumber() {
        return targetStepNumber;
    }

    public static double getCoefficient() {
        return coefficient;
    }

    public static int getMaxStepEnergy() {
        return maxStepEnergy;
    }

    public static int getMaxPlanEnergy() {
        return maxPlanEnergy;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化Bmob
        Bmob.initialize(this, applicationID);
    }
}

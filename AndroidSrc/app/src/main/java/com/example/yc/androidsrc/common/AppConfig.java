package com.example.yc.androidsrc.common;

import android.app.Application;
import android.util.Log;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import cn.bmob.v3.Bmob;

/**
 * 初始化全局配置
 *
 * @author RebornC
 * Created on 2018/12/12.
 */

public class AppConfig extends Application {

    private static final String TAG = "AppConfig";

    /**
     * 后端密钥
     */
    private static final String applicationID = "62dddc420e225515924df91ad0cd2823";

    /**
     * 微信开发平台所申请到的AppID
     */
    private static final String APP_ID = "wx01f46947ce1abda7";

    /**
     * IWXAPI是第三方app和微信通信的openApi接口
     */
    private static IWXAPI api;

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

        // 初始化友盟
        // 在此处调用基础组件包提供的初始化函数
        // 相应信息可在应用管理 -> 应用信息 中找到 http://message.umeng.com/list/apps
        // 参数一：当前上下文context
        // 参数二：应用申请的Appkey
        // 参数三：渠道名称
        // 参数四：设备类型，必须参数，传参数为UMConfigure.DEVICE_TYPE_PHONE则表示手机；传参数为UMConfigure.DEVICE_TYPE_BOX则表示盒子；默认为手机
        // 参数五：Push推送业务的secret 填充Umeng Message Secret对应信息（需替换）
        UMConfigure.init(this, "5c6cc832f1f556461c0000e8", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "baf3e837727551d94471f63837a6a1d7");
        // 获取消息推送代理示例
        PushAgent mPushAgent = PushAgent.getInstance(this);
        // 注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.i(TAG,"注册成功：deviceToken：-------->  " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e(TAG,"注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });

        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        // 将应用的appId注册到微信
        api.registerApp(APP_ID);

    }

    public IWXAPI getWechatApi() {
        return api;
    }
}

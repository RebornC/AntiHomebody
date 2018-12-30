package com.example.yc.androidsrc.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2018/12/30.
 */

public class CheckUtil {

    /**
     * 检测service是否在运行
     *
     * @param context
     * @param serviceName
     * @return
     */
    public static boolean isServiceWorked(Context context, String serviceName) {
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测activity是否存在于栈顶
     *
     * @param context
     * @param PackageName
     * @return
     */
    public static boolean isForeground(Context context, String PackageName) {
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> task = myManager.getRunningTasks(1);
        ComponentName componentInfo = task.get(0).topActivity;
        if (componentInfo.getPackageName().equals(PackageName))
            return true;
        return false;
    }

    /**
     * 判断某个app进程是否在运行
     *
     * @param context
     * @param appInfo
     * @return
     */
    public static boolean isRunningProcess(Context context, String appInfo) {
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppPs = myManager.getRunningAppProcesses();
        if (runningAppPs != null && runningAppPs.size() > 0) {
            if (runningAppPs.contains(appInfo)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断一个Activity是否正在运行(非前台)
     * 由于MainActivity设置为singleTask，所以在切换用户重新登录后，应该判断MainActivity是否存在于栈中
     * 若存在，则销毁，重新实例化
     *
     * @param pkg  pkg为应用包名
     * @param cls  cls为类名eg
     * @param context
     * @return
     */
    public static boolean isActivityRunning(Context context, String cls) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1000);
        for (ActivityManager.RunningTaskInfo taskInfo : list) {
            if (taskInfo.baseActivity.getShortClassName().contains(cls)) {
                return true;
            }
        }
        return false;
    }
}

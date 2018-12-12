package com.example.yc.androidsrc.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.ui.activity.MainActivity;

public class StepCounterService extends Service implements SensorEventListener {

    // IBinder对象，向Activity传递数据的桥梁
    private StepBinder stepBinder = new StepBinder();
    // UI监听对象
    private UpdateUIStepCallBack mCallback;
    // 传感器管理对象
    private SensorManager sensorManager;
    // 计步传感器类型  Sensor.TYPE_STEP_COUNTER或者Sensor.TYPE_STEP_DETECTOR
    private static int stepSensorType = -1;
    // 默认1秒进行一次存储
    private static int duration = 1000 * 1;
    // 计时器
    private TimeCount time;
    // 当前所走的步数
    private int CURRENT_STEP;
    // 每次第一次启动计步功能时是否从系统中获取了已有的步数记录
    private boolean hasRecord = false;
    // 系统中获取到的已有的步数
    private int hasStepCount = 0;
    // 上一次的步数
    private int previousStepCount = 0;

    private Notification.Builder builder;
    private NotificationManager notificationManager;
    private Intent nfIntent;


    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                startStepDetector();
            }
        }).start();
        startTimeCount();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /**
         * 此处设将Service为前台，不然当APP结束以后很容易被GC给干掉，这也就是大多数音乐播放器会在状态栏设置一个
         * 原理大都是相通的
         */
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //获取一个Notification构造器
        builder = new Notification.Builder(this.getApplicationContext());
        /**
         * 设置点击通知栏打开的界面，此处需要注意了，如果你的计步界面不在主界面，则需要判断app是否已经启动，
         * 再来确定跳转页面，这里面太多坑，（别问我为什么知道 - -）
         * 总之有需要的可以和我交流
         */
        nfIntent = new Intent(this, MainActivity.class);
        nfIntent.putExtra("toValue","switchFragment1"); // 点击通知栏跳转到计步fragment界面
        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.logo_round)) // 设置下拉列表中的图标(大图标)
                .setContentTitle("已累计"+CURRENT_STEP+"步") // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.logo_round) // 设置状态栏内的小图标
                .setContentText("加油，要记得勤加运动"); // 设置上下文内容
        // 获取构建好的Notification
        Notification stepNotification = builder.build();
        notificationManager.notify(110, stepNotification);
        // 参数一：唯一的通知标识；参数二：通知消息。
        startForeground(110, stepNotification);// 开始前台服务

        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return stepBinder;
    }

    /**
     * 向Activity传递数据的纽带
     */
    public class StepBinder extends Binder {

        /**
         * 获取当前service对象
         * @return StepService
         */
        public StepCounterService getService() {
            return StepCounterService.this;
        }
    }

    /**
     * 注册UI更新监听
     * @param paramICallback
     */
    public void registerCallback(UpdateUIStepCallBack paramICallback) {
        this.mCallback = paramICallback;
    }

    /**
     * 开始保存计步数据
     */
    private void startTimeCount() {
        if (time == null) {
            time = new TimeCount(duration, 1000);
        }
        time.start();
    }

    /**
     * 获取传感器实例
     */
    private void startStepDetector() {
        if (sensorManager != null) {
            sensorManager = null;
        }
        // 获取传感器管理器的实例
        sensorManager = (SensorManager) this
                .getSystemService(SENSOR_SERVICE);
        // android4.4以后可以使用计步传感器
        addStepCountListener();
    }

    /**
     * 添加传感器监听
     *
     * 1. TYPE_STEP_COUNTER 是指从开机被激活后统计的步数，当重启手机后该数据归零
     * 该传感器是一个硬件传感器所以功耗较低
     * 为了能持续的计步，请不要反注册事件，就算手机处于休眠状态它依然会计步。
     * 当激活的时候依然会上报步数。该sensor适合在长时间的计步需求。
     *
     * 2.TYPE_STEP_DETECTOR是指走路检测器，即单次有效计步
     * 根据API文档，该sensor用来监测走步，每次返回数字1.0（浮点数）
     * 如果需要长事件的计步请使用TYPE_STEP_COUNTER
     */
    private void addStepCountListener() {
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (countSensor != null) {
            stepSensorType = Sensor.TYPE_STEP_COUNTER;
            Log.i("StepCounterService", "Sensor.TYPE_STEP_COUNTER");
            sensorManager.registerListener(StepCounterService.this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else if (detectorSensor != null) {
            stepSensorType = Sensor.TYPE_STEP_DETECTOR;
            Log.i("StepCounterService", "Sensor.TYPE_STEP_DETECTOR");
            sensorManager.registerListener(StepCounterService.this, detectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    /**
     * 传感器监听回调
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (stepSensorType == Sensor.TYPE_STEP_COUNTER) {
            // 获取当前传感器返回的临时步数
            int tempStep = (int) event.values[0];
            // 首次如果没有获取手机系统中已有的步数则获取一次系统中APP还未开始记步的步数
            if (!hasRecord) {
                hasRecord = true;
                hasStepCount = tempStep;
            } else {
                // APP打开并启动计步功能后到现在的总步数 = 本次系统回调的总步数 - APP打开之前已有的步数
                int thisStepCount = tempStep - hasStepCount;
                // 本次有效步数 = APP打开后所记录的总步数 - 上一次所记录的总步数
                int thisStep = thisStepCount - previousStepCount;
                // 总步数 = 现有的步数 + 本次有效步数
                CURRENT_STEP += (thisStep);
                // 动态规划记录方式
                previousStepCount = thisStepCount;
            }
        } else if (stepSensorType == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0) {
                CURRENT_STEP++;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 获取当前步数
     * @return
     */
    public int getStepCount() {
        return CURRENT_STEP;
    }

    /**
     * 保存计步数据并更新UI
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            // 如果计时器正常结束，则开始计步
            time.cancel();
            mCallback.updateUIStep(CURRENT_STEP);

            nfIntent = new Intent(StepCounterService.this, MainActivity.class);
            nfIntent.putExtra("toValue","switchFragment1"); // 点击通知栏跳转到计步fragment界面
            builder.setContentIntent(PendingIntent.getActivity(StepCounterService.this, 0, nfIntent, 0)) // 设置PendingIntent
                    .setLargeIcon(BitmapFactory.decodeResource(StepCounterService.this.getResources(), R.mipmap.logo_round)) // 设置下拉列表中的图标(大图标)
                    .setContentTitle("已累计"+CURRENT_STEP+"步") // 设置下拉列表里的标题
                    .setSmallIcon(R.mipmap.logo_round) // 设置状态栏内的小图标
                    .setContentText("加油，要记得勤加运动"); // 设置上下文内容
            // 获取构建好的Notification
            Notification stepNotification = builder.build();
            notificationManager.notify(110, stepNotification);

            startTimeCount();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消前台进程
        stopForeground(true);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}

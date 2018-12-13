package com.example.yc.androidsrc.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.service.StepCounterService;
import com.example.yc.androidsrc.service.UpdateUIStepCallBack;
import com.example.yc.androidsrc.ui.activity.StepChartActivity;
import com.example.yc.androidsrc.views.ImageTextButton;
import com.example.yc.androidsrc.views.StepProgressView;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author RebornC
 * Created on 2018/11/28.
 */

public class TabFragment2 extends Fragment {

    private boolean isBind = false;

    private SensorManager mSensorManager;
    private Sensor mStepCount;
    private Sensor mStepDetector;
    private static int stepSensor = -1;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

//    private TextView currentTime;
//    private TextView stepCounter;
//    private TextView delector;
    private StepProgressView stepProgressView;
    // 目标步数
    private int targetStepNumber = 8000;
    // 当前步数
    private int currentCounts = 0;

    private ImageTextButton startCount;
    private ImageTextButton stopCount;

    private Handler mHandler;
    private static final int UPDATE_TEXT = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);

//        // 通过系统服务获取SensorManager
//        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
//        // 系统计步累加值
//        mStepCount = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//        // 单次有效计步
//        mStepDetector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
//
//        if (mStepCount != null) {
//            stepSensor = 0;
//            mSensorManager.registerListener(mSensorEventListener, mStepCount, SensorManager.SENSOR_DELAY_FASTEST);
//        } else if (mStepDetector != null) {
//            stepSensor = 1;
//            mSensorManager.registerListener(mSensorEventListener, mStepDetector, SensorManager.SENSOR_DELAY_FASTEST);
//        }

//        currentTime = (TextView) view.findViewById(R.id.current_time);
//        stepCounter = (TextView) view.findViewById(R.id.step_counter);
//        delector  = (TextView) view.findViewById(R.id.delector);
        stepProgressView = (StepProgressView) view.findViewById(R.id.step_walk_arv);
        stepProgressView.setTargetStepNumber(targetStepNumber);
        stepProgressView.setCurrentCount(targetStepNumber, currentCounts);

        startCount = (ImageTextButton) view.findViewById(R.id.start_count);
        startCount.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        startCount.setText("开始计步");
        startCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(), "开始计步", Toast.LENGTH_SHORT).show();
            }
        });

        stopCount = (ImageTextButton) view.findViewById(R.id.stop_count);
        stopCount.setImageResource(R.drawable.ic_stop_white_24dp);
        stopCount.setText("领取能量");
        stopCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(), "领取能量", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), StepChartActivity.class);
                startActivity(intent);
            }
        });

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDATE_TEXT:
                        // 刷新数据
                        updateText();
                        break;
                    default:
                        break;
                }
            }
        };

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 不能直接在UI线程中进行操作，所以通过handler进行消息传递
                Message msg = new Message();
                msg.what = UPDATE_TEXT;
                mHandler.sendMessage(msg);
            }
        }, 0, 1000); // 0秒之后，每隔1秒进行更新

        begin();

        return view;
    }

    public void updateText() {
//        Date curDate = new Date(System.currentTimeMillis());
//        int stepCounterValue = Sensor.TYPE_STEP_COUNTER;
//        int delectorValue = Sensor.TYPE_STEP_DETECTOR;
//        currentTime.setText(formatter.format(curDate));
//        stepCounter.setText(String.valueOf(stepCounterValue));
//        delector.setText(String.valueOf(delectorValue));
    }



    private void begin() {
        if (isBind == false){
            // 开启计步
            Intent intent = new Intent(getActivity(), StepCounterService.class);
            isBind = getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
            getActivity().startService(intent);
        }
    }

    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepCounterService stepService = ((StepCounterService.StepBinder) service).getService();
            // 设置初始化数据
            stepProgressView.setCurrentCount(targetStepNumber, stepService.getStepCount());

            // 设置步数监听回调
            stepService.registerCallback(new UpdateUIStepCallBack() {
                @Override
                public void updateUIStep(int stepCount) {
                    stepProgressView.setCurrentCount(targetStepNumber, stepCount);
                }
            });
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    //服务解绑
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            getActivity().unbindService(conn);
            isBind = false;
        }
    }


}

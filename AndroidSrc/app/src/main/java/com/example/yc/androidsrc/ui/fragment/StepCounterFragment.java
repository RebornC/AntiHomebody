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
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.db.StepDataDao;
import com.example.yc.androidsrc.model.StepEntity;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.StepCounterPresenterCompl;
import com.example.yc.androidsrc.presenter.impl.IStepCounterPresenter;
import com.example.yc.androidsrc.service.StepCounterService;
import com.example.yc.androidsrc.service.UpdateUIStepCallBack;
import com.example.yc.androidsrc.ui.activity.StepChartActivity;
import com.example.yc.androidsrc.ui.impl.IStepCounterView;
import com.example.yc.androidsrc.utils.ToastUtil;
import com.example.yc.androidsrc.views.ImageTextButton;
import com.example.yc.androidsrc.views.StepProgressView;
import com.example.yc.androidsrc.views.TextButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobUser;

/**
 * @author RebornC
 * Created on 2018/11/28.
 */

public class StepCounterFragment extends Fragment implements IStepCounterView, View.OnClickListener {

    private View view;
    private IStepCounterPresenter stepCounterPresenter;

    private String objectId;
    private _User curUser;

    private Toolbar toolbar;
    private TextView title;
    private ImageTextButton startCount;
    private TextButton stopCount;

    private Intent mIntent;
    private StepCounterService stepService;
    private boolean isBind = false; // 是否绑定service
    private boolean isCounting = false; // 是否点击开始计步
    private StepProgressView stepProgressView;
    private int targetStepNumber = 8000; // 目标步数
    private int currentCounts = 0; // 当前步数
    private int curEnergyValue = 0; // 当前通过计步获得的能量
    private int maxEnergyValue = 350; // 当步数不低于8000时，获得的能量上限为350
    private double coefficient = 0.04; // 系数，当步数低于8000时，获得的能量=步数*系数

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_step_counter, container, false);
        initView();
        return view;
    }

    /**
     * find view and init view
     */
    public void initView() {
        // 先通过Bmob本地缓存获取用户Id，在通过Id获取数据库中的本地记录
        stepCounterPresenter = new StepCounterPresenterCompl(this);
        objectId = BmobUser.getCurrentUser(_User.class).getObjectId();
        curUser = stepCounterPresenter.getUserDate(getActivity(), objectId);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);

        title = (TextView) getActivity().findViewById(R.id.title);
        title.setText("查看记录");
        title.setTextColor(getResources().getColor(R.color.offWhite));
        title.setOnClickListener(this);

        stepProgressView = (StepProgressView) view.findViewById(R.id.step_walk_arv);
        stepProgressView.setTargetStepNumber(targetStepNumber);
        stepProgressView.setCurrentCount(targetStepNumber, currentCounts);

        startCount = (ImageTextButton) view.findViewById(R.id.start_count);
        startCount.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        startCount.setText("开始计步");
        startCount.setOnClickListener(this);

        stopCount = (TextButton) view.findViewById(R.id.stop_count);
        stopCount.setText("领取能量");
        setStepAndEnergyValue(currentCounts);
        stopCount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title:
                Intent intent = new Intent(getActivity(), StepChartActivity.class);
                startActivity(intent);
                break;
            case R.id.start_count:
                if (!isCounting) {
                    startCount.setText("计步中");
                    startCount.setImageResource(R.drawable.ic_graphic_eq_white_24dp);
                    isCounting = true;
                    // 绑定服务，开始计步
                    beginBindService();
                } else {
                    ToastUtil.showShort(getActivity(), "你已经开始计步了喔");
                }
                break;
            case R.id.stop_count:
                if (isCounting && curEnergyValue != 0) {
                    startCount.setText("开始计步");
                    startCount.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                    int flag_1 = currentCounts;
                    int flag_2 = curEnergyValue;
                    // 停止服务，当前步数恢复为0
                    isCounting = false;
                    getActivity().unbindService(conn);
                    isBind = false;
                    getActivity().stopService(mIntent);
                    setStepAndEnergyValue(0);
                    // 调用presenter层，将数据记录到数据库中，同时更新到后台
                    stepCounterPresenter.updateSqlData(getActivity(), curUser, flag_1, flag_2);
                    stepCounterPresenter.updateBackendData(flag_2);
                } else if (curEnergyValue == 0) {
                    ToastUtil.showShort(getActivity(), "抱歉，此时没有可领取的能量");
                } else {
                    ToastUtil.showShort(getActivity(), "还未开始计步，暂无能量可领取");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置界面中步数和能量的数据
     */
    public void setStepAndEnergyValue(int stepCount) {
        currentCounts = stepCount;
        stepProgressView.setCurrentCount(targetStepNumber, stepCount);
        if (currentCounts < targetStepNumber) {
            curEnergyValue = (int) (currentCounts * coefficient);
            stopCount.setEnergyValue(curEnergyValue);
        } else {
            stopCount.setEnergyValue(maxEnergyValue);
        }
    }

    /**
     * 开启计步后台服务
     */
    private void beginBindService() {
        if (isBind == false){
            mIntent = new Intent(getActivity(), StepCounterService.class);
            isBind = getActivity().bindService(mIntent, conn, Context.BIND_AUTO_CREATE);
            getActivity().startService(mIntent);
        }
    }

    /**
     * 用于查询应用服务状态
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的
     */
    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起与Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 获取服务对象
            stepService = ((StepCounterService.StepBinder) service).getService();
            // 设置初始化数据
            setStepAndEnergyValue(stepService.getStepCount());
            // 设置步数监听回调
            stepService.registerCallback(new UpdateUIStepCallBack() {
                @Override
                public void updateUIStep(int stepCount) {
                    setStepAndEnergyValue(stepCount);
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

    /**
     * 服务解绑
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            getActivity().unbindService(conn);
            isBind = false;
        }
    }

    /**
     * 当该fragment已被实例化但不可视时，重新切换到此fragment时会需要加载这个方法
     * 或者是由可视变为不可视
     * 进行界面的一些改动
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // onPause()
        } else {
            // onResume()
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            title.setText("查看记录");
            title.setOnClickListener(this);
        }
    }

    /**
     * 数据库更新后的结果反馈
     * @param result
     * @param message
     */
    @Override
    public void onUpdateData(boolean result, String message) {
        if (result) {
            ToastUtil.showShort(getActivity(), message);
        }
    }
}

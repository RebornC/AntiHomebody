package com.example.yc.androidsrc.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.common.DataMonitor;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.GrowUpPresenterCompl;
import com.example.yc.androidsrc.presenter.impl.IGrowUpPresenter;
import com.example.yc.androidsrc.ui.impl.IGrowUpView;
import com.example.yc.androidsrc.utils.ToastUtil;
import com.example.yc.androidsrc.views.GifView;
import com.github.mikephil.charting.data.Entry;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * 成长：能量驿站
 *
 * @author RebornC
 * Created on 2019/1/5.
 */

public class EnergyHouseFragment extends Fragment implements IGrowUpView, View.OnClickListener {

    private String objectId;
    private _User curUser;

    private View view;
    private IGrowUpPresenter growUpPresenter;

    private GifView gifV;
    private ImageView changeStateBtn;
    private ProgressBar progressBar;
    private TextView degree;
    private TextView exp;
    private TextView energyValue;
    private Button receiveEnergyBtn;
    private AlertDialog dialog;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    private TextView title;

    private int userLevel;
    private int denominator;
    private int numerator;
    private int curEnergy;
    private int activeLevel;

    private static final int LEVEL_CHANGE_CODE_0 = 0; // 用户等级不变
    private static final int LEVEL_CHANGE_CODE_1 = 1; // 用户等级发生变化，升级
    private static final int UPDATE_BACKEND_CODE = 2; // 更新至后端
    private static final int REWARD_ENERGY = 3; // 奖励能量值
    private static final int PUNISH_ENERGY = 4; // 扣罚能量值
    private static final int LEVEL_CHANGE_CODE_2 = 5; // 用户等级发生变化，降级

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_energy_house, container, false);
        initView();
        initValue();
        getUserEnergyData();
        return view;
    }


    public void initView() {
        growUpPresenter = new GrowUpPresenterCompl(this);
        objectId = BmobUser.getCurrentUser(_User.class).getObjectId();
        gifV = (GifView) view.findViewById(R.id.gifview);
        avLoadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.loading_icon);
        changeStateBtn = (ImageView) view.findViewById(R.id.change_state_btn);
        changeStateBtn.setTag('1');
        changeStateBtn.setOnClickListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        degree = (TextView) view.findViewById(R.id.degree);
        exp = (TextView) view.findViewById(R.id.exp);
        energyValue = (TextView) view.findViewById(R.id.energy_value);
        receiveEnergyBtn = (Button) view.findViewById(R.id.receive_energy);
        receiveEnergyBtn.setOnClickListener(this);
    }


    public void initValue() {
        // 获取/刷新用户数据
        curUser = growUpPresenter.getUserDate(getActivity(), objectId);
        userLevel = curUser.getCurLevel();
        denominator = curUser.getDenominator();
        numerator = curUser.getNumerator();
        curEnergy = curUser.getCurEnergy();
        // 刷新界面
        setGifView(userLevel);
        degree.setText("Lv." + String.valueOf(userLevel));
        progressBar.setMax(denominator);
        progressBar.setProgress(numerator);
        exp.setText(String.valueOf(numerator) + "/" + String.valueOf(denominator));
        energyValue.setText(String.valueOf(curEnergy));
    }


    /**
     * 在用户创建账号满7天的情况下
     * (由于User.getCreatedAt()返回空值？所以先用数据库中本地登录数据代替
     * 每天首日登录会核算过去7天的活跃度
     * 根据评测分数进行奖罚
     */
    public void getUserEnergyData() {
        // 最近七天（包含当天）
        List<String> dateList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for (int i = 0 ; i < 7; i++) {
            Date date = cal.getTime();
            // 日期格式化 yyyy-MM-dd
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(date);
            dateList.add(dateString);
            // 将日历日期往前推1天
            cal.add(cal.DATE, -1);
        }
        // 翻转数组
        Collections.reverse(dateList);
        // 判断当天首次登录信息是否写进数据库，没有的话则写入，同时弹出活跃度检测结果
        if (!growUpPresenter.loginToday(getActivity(), curUser.getObjectId())) {
            growUpPresenter.addLoginData(getActivity(), curUser.getObjectId());
            if (dateList.contains(growUpPresenter.queryFirstLoginDate(getActivity(), curUser.getObjectId()))) {
                // 此时本地登录未满7天，不应该进行活跃度检测
            } else {
                // 此时本地登录已满7天，进行活跃度检测，弹出对话框
                List<Integer> dailyEnergy = new ArrayList<>();
                for (int i = 0; i < dateList.size(); i++) {
                    int energyValue = growUpPresenter.getUserDailyEnergyData(getActivity(), curUser.getObjectId(), dateList.get(i));
                    dailyEnergy.add(energyValue);
                }
                // 进行数据评估，返回相关结果
                activeLevel = DataMonitor.estimate(dailyEnergy).get(0);
                // 当天初次登录弹出提示活跃度的对话框
                popupActiveDialog(getActivity());
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_state_btn:
                if (v.getTag().equals('1')) {
                    v.setTag('0');
                    gifV.pause();
                    changeStateBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                } else if (v.getTag().equals('0')) {
                    v.setTag('1');
                    gifV.play();
                    changeStateBtn.setImageResource(R.drawable.ic_stop_black_24dp);
                }
                break;
            case R.id.receive_energy:
                if (curEnergy == 0) {
                    ToastUtil.showShort(getActivity(), "当前储存能量为零，不可浇灌");
                } else {
                    popupReceiveEnergyDialog(getActivity());
                }
                break;
            case R.id.btn_cancel_add:
                dialog.dismiss();
                break;
            case R.id.btn_comfirm_add:
                // 浇灌能量
                dialog.dismiss();
                growUpPresenter.addEnergy(getActivity(), curUser);
                break;
        }
    }

    public void setGifView(int userLevel) {
        switch (userLevel) {
            case 1:
                gifV.setGifResource(R.mipmap.level_1);
                break;
            case 2:
                gifV.setGifResource(R.mipmap.level_2);
                break;
            case 3:
                gifV.setGifResource(R.mipmap.level_3);
                break;
            case 4:
                gifV.setGifResource(R.mipmap.level_4);
                break;
            case 5:
                gifV.setGifResource(R.mipmap.level_5);
                break;
            case 6:
                gifV.setGifResource(R.mipmap.level_6);
                break;
            case 7:
                gifV.setGifResource(R.mipmap.level_7);
                break;
            case 8:
                gifV.setGifResource(R.mipmap.level_8);
                break;
        }
    }


    /**
     * 弹出领取能量值的对话框
     *
     * @param context
     */
    public void popupReceiveEnergyDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(getActivity(), R.layout.add_energy_custom_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView energy_value = (TextView) view.findViewById(R.id.energy_value);
        energy_value.setText(String.valueOf(curEnergy));
        Button btn_cancel_add = (Button) view.findViewById(R.id.btn_cancel_add);
        Button btn_comfirm_add = (Button) view.findViewById(R.id.btn_comfirm_add);
        btn_cancel_add.setOnClickListener(this);
        btn_comfirm_add.setOnClickListener(this);
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.getWindow().setLayout(800, 800);
    }


    /**
     * 弹出恭喜升级的对话框
     *
     * @param context
     */
    public void popupUpgradeDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(getActivity(), R.layout.upgrade_custom_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView level = (TextView) view.findViewById(R.id.level);
        level.setText(String.valueOf(userLevel));
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.getWindow().setLayout(800, 800);
    }


    /**
     * 弹出提示活跃度的对话框
     *
     * @param context
     */
    public void popupActiveDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(getActivity(), R.layout.user_state_custom_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView level = (TextView) view.findViewById(R.id.active_level);
        level.setText(String.valueOf(activeLevel));
        TextView evaluation = (TextView) view.findViewById(R.id.evaluation);
        evaluation.setText(DataMonitor.getEvaluationByScore(getActivity(), activeLevel));
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.getWindow().setLayout(800, 800);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                int result = DataMonitor.getResultByScore(activeLevel);
                if (result > 0) growUpPresenter.rewardEnergy(getActivity(), curUser, result);
                else if (result < 0) growUpPresenter.punishEnergy(getActivity(), curUser, result);
            }
        });
    }


    /**
     * 当ViewPager中的fragment切换为可见时
     * PS: setUserVisibleHint先于onCreateView进行
     * 所以要注意空指针现象
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (view != null) {
                title = (TextView) getActivity().findViewById(R.id.title);
                title.setText("");
            }
        }
    }

    @Override
    public void onUpdateData(boolean result, int resultCode, String message) {
        initValue();
        if (result && resultCode == LEVEL_CHANGE_CODE_1) {
            // 进行升级
            // 先让界面出现2秒钟的缓冲图形，然后再出现对应的形象动图，弹出提示框
            gifV.setVisibility(View.GONE);
            avLoadingIndicatorView.setVisibility(View.VISIBLE);
            Handler handle = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    avLoadingIndicatorView.setVisibility(View.GONE);
                    gifV.setVisibility(View.VISIBLE);
                    popupUpgradeDialog(getActivity());
                }
            };
            handle.postDelayed(runnable, 2000);
        } else if (result && resultCode == LEVEL_CHANGE_CODE_2) {
            // 进行降级
            gifV.setVisibility(View.GONE);
            avLoadingIndicatorView.setVisibility(View.VISIBLE);
            Handler handle = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    avLoadingIndicatorView.setVisibility(View.GONE);
                    gifV.setVisibility(View.VISIBLE);
                }
            };
            handle.postDelayed(runnable, 2000);
        }
    }
}

package com.example.yc.androidsrc.ui.fragment;

import android.content.Context;
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
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.GrowUpPresenterCompl;
import com.example.yc.androidsrc.presenter.impl.IGrowUpPresenter;
import com.example.yc.androidsrc.ui.impl.IGrowUpView;
import com.example.yc.androidsrc.utils.ToastUtil;
import com.example.yc.androidsrc.views.GifView;
import com.wang.avi.AVLoadingIndicatorView;

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

    private int userLevel;
    private int denominator;
    private int numerator;
    private int curEnergy;

    private static final int LEVAL_CHANGE_CODE_0 = 0; // 用户等级不变
    private static final int LEVAL_CHANGE_CODE_1 = 1; // 用户等级发生变化
    private static final int UPDATE_BACKEND_CODE = 2; // 更新至后端

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_energy_house, container, false);
        initView();
        initValue();
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


    @Override
    public void onUpdateData(boolean result, int resultCode, String message) {
        initValue();
        if (result && resultCode == LEVAL_CHANGE_CODE_1) {
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
        } else if (result && resultCode == UPDATE_BACKEND_CODE) {
            // ToastUtil.showShort(getActivity(), message);
        }
    }
}

package com.example.yc.androidsrc.presenter.impl;

import android.content.Context;

import com.example.yc.androidsrc.model._User;

import java.util.List;

/**
 * 逻辑层接口
 * 访问用户动画形象的等级与能量值等相关数据
 *
 * @author RebornC
 * Created on 2018/12/31.
 */

public interface IGrowUpPresenter {
    _User getUserDate(Context context, String objectId);
    void addEnergy(Context context, _User user);
    int getUserDailyEnergyData(Context context, String userId, String curDate);
    int getTotalStepByDateList(Context context, String userId, List<String> dateList);
    int getTotalPlanByDateList(Context context, String userId, List<String> dateList);
}

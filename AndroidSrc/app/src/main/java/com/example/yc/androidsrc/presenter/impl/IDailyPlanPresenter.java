package com.example.yc.androidsrc.presenter.impl;

import android.content.Context;

import com.example.yc.androidsrc.model.PlanEntity;
import com.example.yc.androidsrc.model._User;

import java.util.List;

/**
 * 逻辑层接口
 * 访问计划清单数据表
 * 将计划打卡数据更新到后台和本地SQLite数据库
 *
 * @author RebornC
 * Created on 2018/12/31.
 */

public interface IDailyPlanPresenter {
    _User getUserDate(Context context, String objectId);
    List<PlanEntity> getTodoPlan(Context context, String userId, String curDate);
    List<PlanEntity> getCompletedPlan(Context context, String userId, String curDate);
    void addNewPlan(Context context, PlanEntity planEntity);
    void modifyPlan(Context context, PlanEntity oldPlanEntity, PlanEntity newPlanEntity);
    void deletePlan(Context context, PlanEntity planEntity);
    boolean hasReceivedEnergy(Context context, String userId, String selectedDate);
    void receiveEnergy(Context context, _User curUser, String selectedDate, Integer energy);
    void updateBackendData(int energy);
}

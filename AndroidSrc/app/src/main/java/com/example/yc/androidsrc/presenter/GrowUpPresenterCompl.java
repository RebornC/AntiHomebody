package com.example.yc.androidsrc.presenter;

import android.content.Context;

import com.example.yc.androidsrc.common.AppConfig;
import com.example.yc.androidsrc.db.DailyEnergyDao;
import com.example.yc.androidsrc.db.EnergyOfPlanDao;
import com.example.yc.androidsrc.db.PlanDataDao;
import com.example.yc.androidsrc.db.StepDataDao;
import com.example.yc.androidsrc.db.UserDataDao;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.impl.IGrowUpPresenter;
import com.example.yc.androidsrc.ui.impl.IGrowUpView;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 访问用户动画形象的等级与能量值等相关数据
 * 同时进行数据的更新
 *
 * @author RebornC
 * Created on 2018/12/31.
 */

public class GrowUpPresenterCompl implements IGrowUpPresenter {

    private IGrowUpView iGrowUpView;
    private static final int LEVAL_CHANGE_CODE_0 = 0; // 用户等级不变
    private static final int LEVAL_CHANGE_CODE_1 = 1; // 用户等级发生变化
    private static final int UPDATE_BACKEND_CODE = 2; // 更新至后端

    public GrowUpPresenterCompl(IGrowUpView iGrowUpView) {
        this.iGrowUpView = iGrowUpView;
    }

    @Override
    public _User getUserDate(Context context, String objectId) {
        UserDataDao userDataDao = new UserDataDao(context);
        _User user = userDataDao.getUserDataById(objectId);
        return user;
    }


    /**
     * 浇灌能量
     *
     * @param context
     * @param user
     */
    @Override
    public void addEnergy(Context context, _User user) {
        boolean isChange = false;
        try {
            int curLevel = user.getCurLevel(); // 当前等级
            int numerator = user.getNumerator(); // 当前进度条上的分子
            int denominator = user.getDenominator(); // 当前进度条的分母，即从当前等级升到下一级所需要的总能量
            int curEnergy = user.getCurEnergy(); // 当前用户所积攒的、还未浇灌的能量
            // 浇灌能量后数据发生变化
            int newCurLevel = curLevel;
            int newNumerator = numerator + curEnergy;
            int newDenominator = denominator;
            int newCurEnergy = 0; // 当前用户所积攒的能量归零
            // 判断是否升级
            while (newNumerator >= newDenominator && curLevel < 8) {
                isChange = true;
                newCurLevel++;
                newNumerator = newNumerator - newDenominator;
                newDenominator = AppConfig.getLevelEnergy(newCurLevel);
            }
            // 更新用户数据表
            UserDataDao userDataDao = new UserDataDao(context);
            user.setCurLevel(newCurLevel);
            user.setCurEnergy(newCurEnergy);
            user.setNumerator(newNumerator);
            user.setDenominator(newDenominator);
            userDataDao.updateUserData(user);
        } finally {
            // 同时更新至后端
            updateBackendData(user);
            if (isChange) {
                iGrowUpView.onUpdateData(true, LEVAL_CHANGE_CODE_1, "浇灌成功");
            }
            else {
                iGrowUpView.onUpdateData(true, LEVAL_CHANGE_CODE_0, "浇灌成功");
            }
        }
    }

    public void updateBackendData(_User user) {
        _User curUser = BmobUser.getCurrentUser(_User.class);
        String objectId = curUser.getObjectId();
        curUser.setCurLevel(user.getCurLevel());
        curUser.setNumerator(user.getNumerator());
        curUser.setDenominator(user.getDenominator());
        curUser.setCurEnergy(user.getCurEnergy());
        curUser.setTotalEnergy(user.getTotalEnergy());
        curUser.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    iGrowUpView.onUpdateData(true, UPDATE_BACKEND_CODE, "该数据已更新至云端");
                } else {
                    iGrowUpView.onUpdateData(true, UPDATE_BACKEND_CODE, "该数据暂时无法更新至云端");
                }
            }
        });
    }

    /**
     * 获取指定用户在指定日期当天所获得的总能量
     * 用于监测用户活跃度
     *
     * @param context
     * @param userId
     * @param curDate
     * @return
     */
    @Override
    public int getUserDailyEnergyData(Context context, String userId, String curDate) {
        DailyEnergyDao dailyEnergyDao = new DailyEnergyDao(context);
        return dailyEnergyDao.getEnergyByIdAndDate(userId, curDate);
    }

    /**
     * 返回用户在这些日期里的步行总数
     *
     * @param context
     * @param userId
     * @param dateList
     * @return
     */
    @Override
    public int getTotalStepByDateList(Context context, String userId, List<String> dateList) {
        int stepSum = 0;
        StepDataDao stepDataDao = new StepDataDao(context);
        for (int i = 0; i < dateList.size(); i++) {
            int stepCount = stepDataDao.getStepDataInChart(userId, dateList.get(i));
            stepSum += stepCount;
        }
        return stepSum;
    }

    /**
     * 返回用户这些天通过打卡完成的计划条目
     *
     * @param context
     * @param userId
     * @param dateList
     * @return
     */
    @Override
    public int getTotalPlanByDateList(Context context, String userId, List<String> dateList) {
        int planSum = 0;
        PlanDataDao planDataDao = new PlanDataDao(context);
        for (int i = 0; i < dateList.size(); i++) {
            int planCount = planDataDao.getCompletedPlanSum(userId, dateList.get(i));
            planSum += planCount;
        }
        return planSum;
    }
}

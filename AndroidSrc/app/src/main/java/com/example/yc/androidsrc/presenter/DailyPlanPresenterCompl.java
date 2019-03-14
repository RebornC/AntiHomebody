package com.example.yc.androidsrc.presenter;

import android.content.Context;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.db.DailyEnergyDao;
import com.example.yc.androidsrc.db.EnergyOfPlanDao;
import com.example.yc.androidsrc.db.EnergySourceDao;
import com.example.yc.androidsrc.db.PlanDataDao;
import com.example.yc.androidsrc.db.UserDataDao;
import com.example.yc.androidsrc.model.DailyEnergyEntity;
import com.example.yc.androidsrc.model.EnergySourceEntity;
import com.example.yc.androidsrc.model.PlanEntity;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.impl.IDailyPlanPresenter;
import com.example.yc.androidsrc.presenter.impl.IGrowUpPresenter;
import com.example.yc.androidsrc.ui.impl.IDailyPlanView;
import com.example.yc.androidsrc.ui.impl.IGrowUpView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 访问计划清单数据表
 * 将计划打卡数据更新到后台和本地SQLite数据库
 *
 * @author RebornC
 * Created on 2018/12/31.
 */

public class DailyPlanPresenterCompl implements IDailyPlanPresenter {

    private IDailyPlanView iDailyPlanView;

    public DailyPlanPresenterCompl(IDailyPlanView iDailyPlanView) {
        this.iDailyPlanView = iDailyPlanView;
    }

    @Override
    public _User getUserData(Context context, String objectId) {
        UserDataDao userDataDao = new UserDataDao(context);
        _User user = userDataDao.getUserDataById(objectId);
        return user;
    }

    @Override
    public List<PlanEntity> getTodoPlan(Context context, String userId, String curDate) {
        PlanDataDao planDataDao = new PlanDataDao(context);
        List<PlanEntity> todoList = planDataDao.getTodoPlan(userId, curDate);
        return todoList;
    }

    @Override
    public List<PlanEntity> getCompletedPlan(Context context, String userId, String curDate) {
        PlanDataDao planDataDao = new PlanDataDao(context);
        List<PlanEntity> completedList = planDataDao.getCompletedPlan(userId, curDate);
        return completedList;
    }

    @Override
    public void addNewPlan(Context context, PlanEntity planEntity) {
        try {
            PlanDataDao planDataDao = new PlanDataDao(context);
            planDataDao.addNewPlanData(planEntity);
        } finally {
            iDailyPlanView.onUpdateData(true, "增加成功");
        }
    }

    @Override
    public void modifyPlan(Context context, PlanEntity oldPlanEntity, PlanEntity newPlanEntity) {
        try {
            PlanDataDao planDataDao = new PlanDataDao(context);
            planDataDao.updatePlanData(oldPlanEntity, newPlanEntity);
        } finally {
            iDailyPlanView.onUpdateData(true, "修改成功");
        }
    }

    @Override
    public void deletePlan(Context context, PlanEntity planEntity) {
        try {
            PlanDataDao planDataDao = new PlanDataDao(context);
            planDataDao.deletePlanData(planEntity);
        } finally {
            iDailyPlanView.onUpdateData(true, "删除成功");
        }
    }

    @Override
    public boolean hasReceivedEnergy(Context context, String userId, String selectedDate) {
        EnergyOfPlanDao energyOfPlanDao = new EnergyOfPlanDao(context);
        return energyOfPlanDao.queryData(userId, selectedDate);
    }

    @Override
    public void receiveEnergy(Context context, _User curUser, String selectedDate, Integer energy) {
        try {
            // 更新该天领取状态
            EnergyOfPlanDao energyOfPlanDao = new EnergyOfPlanDao(context);
            energyOfPlanDao.addNewData(curUser.getObjectId(), selectedDate, energy);
            // 更新用户数据表
            UserDataDao userDataDao = new UserDataDao(context);
            Integer newCurEnergy = curUser.getCurEnergy() + energy;
            Integer newTotalEnergy = curUser.getTotalEnergy() + energy;
            curUser.setCurEnergy(newCurEnergy);
            curUser.setTotalEnergy(newTotalEnergy);
            userDataDao.updateUserData(curUser);
            // 更新动态记录
            SimpleDateFormat df_1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat df_2 = new SimpleDateFormat("HH:mm");
            String curDay = df_1.format(new Date());
            String curTime = df_2.format(new Date());
            String source = context.getResources().getStringArray(R.array.source_of_energy)[1];
            EnergySourceEntity energySourceEntity = new EnergySourceEntity
                    (curUser.getObjectId(), curDay, curTime, energy, source);
            EnergySourceDao energySourceDao = new EnergySourceDao(context);
            energySourceDao.addNewData(energySourceEntity);
            // 更新每日总能量记录
            DailyEnergyEntity dailyEnergyEntity = new DailyEnergyEntity(curUser.getObjectId(), curDay, energy);
            DailyEnergyDao dailyEnergyDao = new DailyEnergyDao(context);
            dailyEnergyDao.addDataByUserIdAndDate(dailyEnergyEntity);
        } finally {
            iDailyPlanView.onUpdateData(true, "领取成功");
        }
    }

    @Override
    public void updateBackendData(int energy) {
        _User curUser = BmobUser.getCurrentUser(_User.class);
        String objectId = curUser.getObjectId();
        Integer newCurEnergy = curUser.getCurEnergy() + energy;
        Integer newTotalEnergy = curUser.getTotalEnergy() + energy;
        curUser.setCurEnergy(newCurEnergy);
        curUser.setTotalEnergy(newTotalEnergy);
        curUser.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    iDailyPlanView.onUpdateData(true, "该数据已更新至云端");
                } else {
                    iDailyPlanView.onUpdateData(true, "该数据暂时无法更新至云端");
                }
            }
        });
    }
}

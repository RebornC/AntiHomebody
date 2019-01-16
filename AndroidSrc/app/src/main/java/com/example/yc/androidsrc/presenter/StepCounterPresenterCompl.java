package com.example.yc.androidsrc.presenter;

import android.content.Context;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.common.AppConfig;
import com.example.yc.androidsrc.db.DailyEnergyDao;
import com.example.yc.androidsrc.db.EnergySourceDao;
import com.example.yc.androidsrc.db.StepDataDao;
import com.example.yc.androidsrc.db.UserDataDao;
import com.example.yc.androidsrc.model.DailyEnergyEntity;
import com.example.yc.androidsrc.model.EnergySourceEntity;
import com.example.yc.androidsrc.model.StepEntity;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.impl.IStepCounterPresenter;
import com.example.yc.androidsrc.ui.impl.IStepCounterView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 将计步数据更新到后台和本地SQLite数据库
 *
 * @author RebornC
 * Created on 2018/12/30.
 */

public class StepCounterPresenterCompl implements IStepCounterPresenter {

    private IStepCounterView iStepCounterView;
    private int targetStepNumber = AppConfig.getTargetStepNumber();

    public StepCounterPresenterCompl(IStepCounterView iStepCounterView) {
        this.iStepCounterView = iStepCounterView;
    }

    @Override
    public _User getUserDate(Context context, String objectId) {
        UserDataDao userDataDao = new UserDataDao(context);
        _User user = userDataDao.getUserDataById(objectId);
        return user;
    }

    @Override
    public void updateSqlData(Context context, _User curUser, int stepCounts, int energy) {
        try {
            SimpleDateFormat df_1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat df_2 = new SimpleDateFormat("HH:mm");
            String curDay = df_1.format(new Date());
            String curTime = df_2.format(new Date());
            // 更新计步数据表
            StepEntity stepEntity = new StepEntity(curUser.getObjectId(), curDay, Integer.toString(stepCounts));
            StepDataDao stepDataDao = new StepDataDao(context);
            stepDataDao.addStepDataByUserIdAndDate(stepEntity);
            // 更新用户数据表
            UserDataDao userDataDao = new UserDataDao(context);
            Integer newCurEnergy = curUser.getCurEnergy() + energy;
            Integer newTotalEnergy = curUser.getTotalEnergy() + energy;
            curUser.setCurEnergy(newCurEnergy);
            curUser.setTotalEnergy(newTotalEnergy);
            userDataDao.updateUserData(curUser);
            // 更新动态记录
            String source = context.getResources().getStringArray(R.array.source_of_energy)[0];
            EnergySourceEntity energySourceEntity = new EnergySourceEntity
                    (curUser.getObjectId(), curDay, curTime, energy, source);
            EnergySourceDao energySourceDao = new EnergySourceDao(context);
            energySourceDao.addNewData(energySourceEntity);
            // 更新每日总能量记录
            DailyEnergyEntity dailyEnergyEntity = new DailyEnergyEntity(curUser.getObjectId(), curDay, energy);
            DailyEnergyDao dailyEnergyDao = new DailyEnergyDao(context);
            dailyEnergyDao.addDataByUserIdAndDate(dailyEnergyEntity);
        } finally {
            if (stepCounts > targetStepNumber) {
                iStepCounterView.onUpdateData(true, "恭喜你达到目标步数，成功领取最大能量值~");
            } else if (energy != 0){
                iStepCounterView.onUpdateData(true, "成功领取能量~");
            }
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
                    // iStepCounterView.onUpdateData(true, "该数据已更新至云端");
                } else {
                    // iStepCounterView.onUpdateData(true, "该数据暂时无法更新至云端");
                }
            }
        });
    }
}

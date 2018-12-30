package com.example.yc.androidsrc.presenter;

import android.content.Context;

import com.example.yc.androidsrc.db.StepDataDao;
import com.example.yc.androidsrc.db.UserDataDao;
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
    private int targetStepNumber = 8000;

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
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String curDate = df.format(new Date());
            StepEntity stepEntity = new StepEntity(curUser.getObjectId(), curDate, Integer.toString(stepCounts));
            // 更新计步数据表
            StepDataDao stepDataDao = new StepDataDao(context);
            stepDataDao.addStepDataByUserIdAndDate(stepEntity);
            // 更新用户数据表
            UserDataDao userDataDao = new UserDataDao(context);
            Integer newCurEnergy = curUser.getCurEnergy() + energy;
            Integer newTotalEnergy = curUser.getTotalEnergy() + energy;
            curUser.setCurEnergy(newCurEnergy);
            curUser.setTotalEnergy(newTotalEnergy);
            userDataDao.updateUserData(curUser);
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
                    // 更新成功
                    // iStepCounterView.onUpdateData(true, "该数据已更新至云端");
                } else {
                    // iStepCounterView.onUpdateData(true, "该数据暂时无法更新至云端");
                }
            }
        });
    }
}

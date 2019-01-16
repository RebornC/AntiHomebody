package com.example.yc.androidsrc.presenter;

import android.content.Context;

import com.example.yc.androidsrc.db.EnergySourceDao;
import com.example.yc.androidsrc.model.EnergySourceEntity;
import com.example.yc.androidsrc.presenter.impl.IDynamicPresenter;

import java.util.Collections;
import java.util.List;


/**
 * “用户能量来源动态”界面的数据访问
 *
 * @author RebornC
 * Created on 2019/1/15.
 */

public class DynamicPresenterCompl implements IDynamicPresenter {

    @Override
    public List<EnergySourceEntity> getSqlData(Context context, String objectId) {
        EnergySourceDao energySourceDao = new EnergySourceDao(context);
        List<EnergySourceEntity> list = energySourceDao.getEnergySourceById(objectId);
        // 翻转数组，时间近的排在前面
        Collections.reverse(list);
        return list;
    }
}

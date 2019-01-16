package com.example.yc.androidsrc.presenter.impl;

import android.content.Context;

import com.example.yc.androidsrc.model.EnergySourceEntity;
import java.util.List;

/**
 * 逻辑层接口
 * 对“用户能量来源动态”模块进行数据操作
 *
 * @author RebornC
 * Created on 2019/1/15.
 */

public interface IDynamicPresenter {
    List<EnergySourceEntity> getSqlData(Context context, String objectId);
}

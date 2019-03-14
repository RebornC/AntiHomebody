package com.example.yc.androidsrc.presenter.impl;

import android.content.Context;

import com.example.yc.androidsrc.model._User;

/**
 * 逻辑层接口
 * 将计步数据更新到后台和本地SQLite数据库
 *
 * @author RebornC
 * Created on 2018/12/30.
 */

public interface IStepCounterPresenter {
    _User getUserData(Context context, String objectId);
    void updateSqlData(Context context, _User curUser, int stepCounts, int energy);
    void updateBackendData(int energy);
}

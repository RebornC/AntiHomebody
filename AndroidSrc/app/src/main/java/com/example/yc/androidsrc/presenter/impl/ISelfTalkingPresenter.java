package com.example.yc.androidsrc.presenter.impl;

import android.content.Context;

import com.example.yc.androidsrc.model.SelfTalkingEntity;
import com.example.yc.androidsrc.model._User;

import java.util.List;

/**
 * 逻辑层接口
 * 对“碎碎念”模块进行数据操作
 *
 * @author RebornC
 * Created on 2019/1/15.
 */

public interface ISelfTalkingPresenter {
    List<SelfTalkingEntity> getSqlData(Context context, String objectId);
    void deleteData(Context context, SelfTalkingEntity selfTalkingEntity);
    void updateData(Context context, SelfTalkingEntity oldEntity, SelfTalkingEntity newEntity);
}

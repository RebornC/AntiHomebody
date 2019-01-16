package com.example.yc.androidsrc.presenter;

import android.content.Context;

import com.example.yc.androidsrc.db.SelfTalkingDao;
import com.example.yc.androidsrc.db.UserDataDao;
import com.example.yc.androidsrc.model.SelfTalkingEntity;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.impl.IPersonalMsgPresenter;
import com.example.yc.androidsrc.presenter.impl.ISelfTalkingPresenter;
import com.example.yc.androidsrc.ui.impl.IPersonalMsgView;

import java.io.File;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


/**
 * “碎碎念”界面的数据访问
 *
 * @author RebornC
 * Created on 2019/1/15.
 */

public class SelfTalkingPresenterCompl implements ISelfTalkingPresenter {

    @Override
    public List<SelfTalkingEntity> getSqlData(Context context, String objectId) {
        SelfTalkingDao selfTalkingDao = new SelfTalkingDao(context);
        List<SelfTalkingEntity> list = selfTalkingDao.getDataById(objectId);
        // 翻转数组，时间近的排在前面
        Collections.reverse(list);
        return list;
    }

    @Override
    public void deleteData(Context context, SelfTalkingEntity selfTalkingEntity) {
        SelfTalkingDao selfTalkingDao = new SelfTalkingDao(context);
        selfTalkingDao.deleteData(selfTalkingEntity);
    }

    @Override
    public void updateData(Context context, SelfTalkingEntity oldEntity, SelfTalkingEntity newEntity) {
        SelfTalkingDao selfTalkingDao = new SelfTalkingDao(context);
        selfTalkingDao.updateData(oldEntity, newEntity);
    }
}

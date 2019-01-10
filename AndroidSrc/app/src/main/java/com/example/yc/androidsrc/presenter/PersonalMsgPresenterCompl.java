package com.example.yc.androidsrc.presenter;

import android.content.Context;

import com.example.yc.androidsrc.db.UserDataDao;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.impl.IPersonalMsgPresenter;
import com.example.yc.androidsrc.ui.impl.IGrowUpView;
import com.example.yc.androidsrc.ui.impl.IPersonalMsgView;

import cn.bmob.v3.BmobUser;


/**
 * 用户个人界面的数据访问
 *
 * @author RebornC
 * Created on 2019/1/10.
 */

public class PersonalMsgPresenterCompl implements IPersonalMsgPresenter {

    private IPersonalMsgView iPersonalMsgView;

    public PersonalMsgPresenterCompl(IPersonalMsgView iPersonalMsgView) {
        this.iPersonalMsgView = iPersonalMsgView;
    }

    @Override
    public void logOut() {
        BmobUser.logOut();
    }
}

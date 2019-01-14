package com.example.yc.androidsrc.presenter.impl;

import android.content.Context;

import com.example.yc.androidsrc.model._User;


/**
 * 用户个人界面的数据访问
 *
 * @author RebornC
 * Created on 2019/1/10.
 */

public interface IPersonalMsgPresenter {
    void logOut();
    void uploadHeadPortrait(String uri);
    void modifyUserNickname(String newName);
    void modifyUserSignature(String newSignature);
    void syncBackend(Context context);
}

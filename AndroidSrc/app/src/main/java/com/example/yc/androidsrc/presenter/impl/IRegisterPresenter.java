package com.example.yc.androidsrc.presenter.impl;

import android.content.Context;

/**
 * 用户注册模块的逻辑层接口
 *
 * @author RebornC
 * Created on 2018/12/14.
 */

public interface IRegisterPresenter {
    void doRegister(String username, String phone, String psd, String confirmPsd);
    void setProgressDialogVisibility(boolean visibility);
    void showLoginDialog(Context context);
    void doLogin(String username, String psd, Context context);
}

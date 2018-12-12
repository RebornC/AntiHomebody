package com.example.yc.androidsrc.presenter.impl;

/**
 * 用户登录模块的逻辑层接口
 *
 * @author RebornC
 * Created on 2018/12/13.
 */

public interface ILoginPresenter {
    void doLogin(String phone, String psd);
    void setProgressDialogVisibility(boolean visibility);
}

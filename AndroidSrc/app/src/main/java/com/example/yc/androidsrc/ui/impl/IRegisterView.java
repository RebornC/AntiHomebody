package com.example.yc.androidsrc.ui.impl;

/**
 * 用户注册模块的接口
 *
 * @author RebornC
 * Created on 2018/12/14.
 */

public interface IRegisterView {
    void onRegisterResult(boolean result, int code, String message);
    void onSetProgressDialogVisibility(boolean visibility);
    void onShowLoginDialog(boolean isLogin);
    void onLoginResult(boolean result, int code, String message);
}

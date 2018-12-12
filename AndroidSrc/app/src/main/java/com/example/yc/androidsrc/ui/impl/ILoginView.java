package com.example.yc.androidsrc.ui.impl;

/**
 * 用户登录模块的接口
 *
 * @author RebornC
 * Created on 2018/12/13.
 */

public interface ILoginView {
    void onLoginResult(Boolean result, int code, String message);
    void onSetProgressDialogVisibility(boolean visibility);
}

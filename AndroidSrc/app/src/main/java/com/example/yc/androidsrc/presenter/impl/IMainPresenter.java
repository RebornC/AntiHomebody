package com.example.yc.androidsrc.presenter.impl;

import android.app.Activity;

/**
 * 主界面的逻辑层接口
 *
 * @author RebornC
 * Created on 2018/12/14.
 */

public interface IMainPresenter {
    void verifyPermissions(Activity activity);
    void showMissingPermissionDialog(Activity activity);
}

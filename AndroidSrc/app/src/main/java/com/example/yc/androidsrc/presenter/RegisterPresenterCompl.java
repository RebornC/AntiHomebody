package com.example.yc.androidsrc.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.impl.IRegisterPresenter;
import com.example.yc.androidsrc.ui.activity.MainActivity;
import com.example.yc.androidsrc.ui.activity.RegisterActivity;
import com.example.yc.androidsrc.ui.impl.IRegisterView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 用户注册模块的逻辑层
 *
 * @author RebornC
 * Created on 2018/12/14.
 */

public class RegisterPresenterCompl implements IRegisterPresenter {

    private IRegisterView iRegisterView;

    private String msg = "";
    private static final int REGISTER_FAIL_CODE_1 = 1; // 填写不完整
    private static final int REGISTER_FAIL_CODE_2 = 2; // 密码填写不一致
    private static final int REGISTER_FAIL_CODE_3 = 3; // Bmob error
    private static final int REGISTER_SUCCESS_CODE = 4; // 注册成功
    private static final int LOGIN_FAIL_CODE = 1; // Bmob error
    private static final int LOGIN_SUCCESS_CODE = 2; // 登录成功

    public RegisterPresenterCompl(IRegisterView iRegisterView) {
        this.iRegisterView = iRegisterView;
    }

    @Override
    public void doRegister(String username, String phone, String psd, String confirmPsd) {
        if (checkInput(username, phone, psd, confirmPsd)) {
            // 填写正确，进行用户注册，同时显示缓冲界面
            signUp(username, phone, psd);
            iRegisterView.onSetProgressDialogVisibility(true);
        }
    }

    @Override
    public void setProgressDialogVisibility(boolean visibility) {
        iRegisterView.onSetProgressDialogVisibility(visibility);
    }

    @Override
    public void showLoginDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("注册成功！请开始你的成长之旅吧");
        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface log, int which) {

                    }
                });
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface log, int which) {
                        iRegisterView.onSetProgressDialogVisibility(true);
                        iRegisterView.onShowLoginDialog(true);
                    }
                });

        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public void doLogin(String username, String psd) {
        // 登录
        BmobUser.loginByAccount(username, psd, new LogInListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                iRegisterView.onSetProgressDialogVisibility(false);
                if (e == null) {
                    iRegisterView.onLoginResult(true, LOGIN_SUCCESS_CODE, msg);
                } else {
                    iRegisterView.onLoginResult(false, LOGIN_FAIL_CODE, e.getMessage());
                }
            }
        });
    }

    /**
     * 检查输入是否符合要求
     */
    public boolean checkInput(String username, String phone, String psd, String confirmPsd) {
        if (!username.equals("") && !phone.equals("") && !psd.equals("") && !confirmPsd.equals("")) {
            if (!psd.equals(confirmPsd)) {
                iRegisterView.onRegisterResult(false, REGISTER_FAIL_CODE_2, msg);
                return false;
            } else
                return true;
        } else {
            iRegisterView.onRegisterResult(false, REGISTER_FAIL_CODE_1, msg);
            return false;
        }
    }

    /**
     * 账号密码注册
     */
    private void signUp(String username, String phone, String psd) {
        final _User user = new _User();
        user.setUsername(username);
        user.setMobilePhoneNumber(phone);
        user.setPassword(psd);
        user.signUp(new SaveListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                iRegisterView.onSetProgressDialogVisibility(false);
                if (e == null) {
                    iRegisterView.onRegisterResult(true, REGISTER_SUCCESS_CODE, msg);
                } else {
                    iRegisterView.onRegisterResult(false, REGISTER_FAIL_CODE_3, e.getMessage());
                }
            }
        });
    }

}

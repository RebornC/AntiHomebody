package com.example.yc.androidsrc.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.common.AppConfig;
import com.example.yc.androidsrc.db.UserDataDao;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.impl.IRegisterPresenter;
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

    private Context context;
    private IRegisterView iRegisterView;
    private AlertDialog dialog;

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
    public void doRegister(String nickname, String phone, String psd, String confirmPsd) {
        if (checkInput(nickname, phone, psd, confirmPsd)) {
            // 填写正确，进行用户注册，同时显示缓冲界面
            signUp(nickname, phone, psd);
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
        View view = View.inflate(context, R.layout.register_success_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        Button cancelSyncBtn = (Button)view.findViewById(R.id.btn_cancel);
        Button confirmSyncBtn = (Button)view.findViewById(R.id.btn_confirm);
        // 取消/确定按钮监听事件处理
        cancelSyncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        confirmSyncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iRegisterView.onSetProgressDialogVisibility(true);
                iRegisterView.onShowLoginDialog(true);
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.getWindow().setLayout(900, 400);
    }

    @Override
    public void doLogin(String username, String psd, Context mContext) {
        // 登录
        this.context = mContext;
        BmobUser.loginByAccount(username, psd, new LogInListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                iRegisterView.onSetProgressDialogVisibility(false);
                if (e == null) {
                    iRegisterView.onLoginResult(true, LOGIN_SUCCESS_CODE, msg);
                    // 注册并登录成功后，在数据库中添加该用户的信息
                    UserDataDao dao = new UserDataDao(context);
                    dao.addNewUserData(user);
                } else {
                    iRegisterView.onLoginResult(false, LOGIN_FAIL_CODE, e.getMessage());
                }
            }
        });
    }

    /**
     * 检查输入是否符合要求
     */
    public boolean checkInput(String nickname, String phone, String psd, String confirmPsd) {
        if (!nickname.equals("") && !phone.equals("") && !psd.equals("") && !confirmPsd.equals("")) {
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
    private void signUp(String nickname, String phone, String psd) {
        final _User user = new _User();
        user.setUsername(phone); // 使用phone作为username唯一标识
        user.setNickName(nickname);
        user.setMobilePhoneNumber(phone);
        user.setPassword(psd);
        // 设置初始的等级与能量值
        user.setCurLevel(1);
        user.setNumerator(0);
        user.setDenominator(AppConfig.getLevelEnergy(1));
        user.setCurEnergy(0);
        user.setTotalEnergy(0);
        user.signUp(new SaveListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                iRegisterView.onSetProgressDialogVisibility(false);
                if (e == null) {
                    iRegisterView.onRegisterResult(true, REGISTER_SUCCESS_CODE, msg);
                } else {
                    if (e.getErrorCode() == 202)
                        iRegisterView.onRegisterResult(false, REGISTER_FAIL_CODE_3, "该手机号码已被注册");
                    else
                        iRegisterView.onRegisterResult(false, REGISTER_FAIL_CODE_3, e.getMessage());
                }
            }
        });
    }

}

package com.example.yc.androidsrc.presenter;

import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.impl.ILoginPresenter;
import com.example.yc.androidsrc.ui.impl.ILoginView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;

/**
 * 用户登录模块的逻辑层
 *
 * @author RebornC
 * Created on 2018/12/13.
 */

public class LoginPresenterCompl implements ILoginPresenter {

    private ILoginView iLoginView;
    private String phone;
    private String psd;
    private String msg = "";
    private static final int LOGIN_FAIL_CODE_1 = 1; // 手机号码为空
    private static final int LOGIN_FAIL_CODE_2 = 2; // 密码为空
    private static final int LOGIN_FAIL_CODE_3 = 3; // 该手机号码未被注册
    private static final int LOGIN_FAIL_CODE_4 = 4; // 密码错误
    private static final int LOGIN_FAIL_CODE_5 = 5; // Bmob error
    private static final int LOGIN_SUCCESS_CODE = 6; // 登录成功

    public LoginPresenterCompl(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
    }

    @Override
    public void doLogin(String phone, String psd) {
        if (checkInput(phone, psd)) {
            this.phone = phone;
            this.psd = psd;
            queryUserIsExist(phone);
        }
    }

    @Override
    public void setProgressDialogVisibility(boolean visibility) {
        iLoginView.onSetProgressDialogVisibility(visibility);
    }

    /**
     * 检查输入是否符合要求
     */
    public boolean checkInput(String phone, String psd) {
        if (phone.equals("")) {
            iLoginView.onLoginResult(false, LOGIN_FAIL_CODE_1, msg);
            return false;
        } else if (psd.equals("")) {
            iLoginView.onLoginResult(false, LOGIN_FAIL_CODE_2, msg);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 查询该手机号码是否存在已注册用户
     * @param phone
     */
    private void queryUserIsExist(String phone) {
        iLoginView.onSetProgressDialogVisibility(true);
        BmobQuery<_User> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("mobilePhoneNumber", phone);
        bmobQuery.findObjects(new FindListener<_User>() {
            @Override
            public void done(List<_User> object, BmobException e) {
                if (e == null) {
                    if (object.size() != 0) {
                        // 存在已注册用户，获取该用户名进行登录
                        String username = object.get(0).getUsername();
                        signIn(username);
                    } else {
                        iLoginView.onSetProgressDialogVisibility(false);
                        iLoginView.onLoginResult(false, LOGIN_FAIL_CODE_3, msg);
                    }
                } else {
                    iLoginView.onSetProgressDialogVisibility(false);
                    iLoginView.onLoginResult(false, LOGIN_FAIL_CODE_5, e.getMessage());
                }
            }
        });
    }

    /**
     * 登录
     */
    private void signIn(String username) {
        BmobUser.loginByAccount(username, psd, new LogInListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                iLoginView.onSetProgressDialogVisibility(false);
                if (e == null) {
                    iLoginView.onLoginResult(true, LOGIN_SUCCESS_CODE, msg);
                } else {
                    if (e.getErrorCode() == 101)
                        iLoginView.onLoginResult(false, LOGIN_FAIL_CODE_4, msg);
                    else
                        iLoginView.onLoginResult(false, LOGIN_FAIL_CODE_5, e.getMessage());
                }
            }
        });
    }
}

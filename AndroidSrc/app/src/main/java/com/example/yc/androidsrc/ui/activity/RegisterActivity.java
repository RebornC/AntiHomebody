package com.example.yc.androidsrc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.presenter.RegisterPresenterCompl;
import com.example.yc.androidsrc.presenter.impl.IRegisterPresenter;
import com.example.yc.androidsrc.ui.impl.IRegisterView;
import com.example.yc.androidsrc.utils.ToastUtil;
import com.example.yc.androidsrc.views.CustomDialog;


/**
 * 用户注册模块
 *
 * @author RebornC
 * Created on 2018/12/12.
 */

public class RegisterActivity extends AppCompatActivity implements IRegisterView, View.OnClickListener {

    private IRegisterPresenter registerPresenter;

    private EditText userName;
    private EditText phoneNumber;
    private EditText password;
    private EditText confirmPassword;
    private Button registerBtn;
    private CustomDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏

        initView();
    }

    public void initView() {
        // find view
        dialog = new CustomDialog(this, R.style.CustomDialog);
        userName = (EditText) findViewById(R.id.user_name);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        registerBtn = (Button) findViewById(R.id.register);
        // set listener
        registerBtn.setOnClickListener(this);
        // init
        registerPresenter = new RegisterPresenterCompl(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                registerPresenter.doRegister(userName.getText().toString(), phoneNumber.getText().toString(),
                        password.getText().toString(), confirmPassword.getText().toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void onRegisterResult(boolean result, int code, String message) {
        if (result) {
            // 注册成功，出现弹框，选择是否登录进入主界面
            registerPresenter.showLoginDialog(this);
        } else {
            switch (code) {
                case 1:
                    ToastUtil.showShort(this, "请填写完整");
                    break;
                case 2:
                    ToastUtil.showShort(this, "密码填写不一致，请仔细检查");
                    break;
                case 3:
                    ToastUtil.showShort(this, message);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onSetProgressDialogVisibility(boolean visibility) {
        if (visibility)
            dialog.show();
        else
            dialog.dismiss();
    }

    @Override
    public void onShowLoginDialog(boolean isLogin) {
        // 选择登录
        if (isLogin)
            registerPresenter.doLogin(userName.getText().toString(), password.getText().toString());
    }

    @Override
    public void onLoginResult(boolean result, int code, String message) {
        if (result) {
            // 登录成功，进入主界面
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        } else {
            switch (code) {
                case 1:
                    ToastUtil.showShort(this, message);
                    break;
                default:
                    break;
            }
        }
    }
}

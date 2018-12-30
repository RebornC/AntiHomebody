package com.example.yc.androidsrc.ui.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.presenter.LoginPresenterCompl;
import com.example.yc.androidsrc.presenter.impl.ILoginPresenter;
import com.example.yc.androidsrc.ui.impl.ILoginView;
import com.example.yc.androidsrc.utils.CheckUtil;
import com.example.yc.androidsrc.utils.ToastUtil;
import com.example.yc.androidsrc.views.CustomDialog;
import com.example.yc.androidsrc.views.GifView;

import java.util.List;

/**
 * 用户登录模块
 *
 * @author RebornC
 * Created on 2018/12/5.
 */

public class LoginActivity extends AppCompatActivity implements ILoginView, View.OnClickListener {

    private ILoginPresenter loginPresenter;

    private CustomDialog dialog;
    private GifView progressIcon;
    private EditText phoneNumber;
    private EditText password;
    private Button loginBtn;
    private TextView register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏

        initView();
    }

    public void initView() {
        // find view
        dialog = new CustomDialog(this, R.style.CustomDialog);
        progressIcon = (GifView) findViewById(R.id.progress_icon);
        progressIcon.setGifPlayCounts(1); // 让动图只播放一次即停止
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        password = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);
        // set listener
        loginBtn.setOnClickListener(this);
        register.setOnClickListener(this);
        // init
        loginPresenter = new LoginPresenterCompl(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login:
                loginPresenter.doLogin(phoneNumber.getText().toString(), password.getText().toString(), LoginActivity.this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoginResult(boolean result, int code, String message) {
        if (result) {
            // 登录成功
            // 假若MainActivity已经存在于栈中，则需要销毁再重新实例化，这样用户界面得以更新
            if (CheckUtil.isActivityRunning(this, "MainActivity")) {
                MainActivity.instance.finish();
            }
            // 进入主界面
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            switch (code) {
                case 1:
                    ToastUtil.showShort(this, "请输入手机号码");
                    break;
                case 2:
                    ToastUtil.showShort(this, "请输入密码");
                    break;
                case 3:
                    ToastUtil.showShort(this, "登录失败，该手机号码未被注册");
                    break;
                case 4:
                    ToastUtil.showShort(this, "密码输入错误");
                    break;
                case 5:
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

}

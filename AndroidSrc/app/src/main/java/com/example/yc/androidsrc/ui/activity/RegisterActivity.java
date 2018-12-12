package com.example.yc.androidsrc.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.views.CustomDialog;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;


/**
 * 用户注册模块
 *
 * @author RebornC
 * Created on 2018/12/12.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText userName;
    private EditText phoneNumber;
    private EditText password;
    private EditText confirmPassword;
    private String userNameText;
    private String phoneNumberText;
    private String passwordText;
    private String confirmPasswordText;
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
        dialog = new CustomDialog(this, R.style.CustomDialog);
        userName = (EditText) findViewById(R.id.user_name);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        registerBtn = (Button) findViewById(R.id.register);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                if (checkInput()) {
                    // 填写正确，进行用户注册，同时显示缓冲界面
                    signUp(v);
                    dialog.show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 检查输入是否符合要求
     */
    public boolean checkInput() {
        userNameText = userName.getText().toString();
        phoneNumberText = phoneNumber.getText().toString();
        passwordText = password.getText().toString();
        confirmPasswordText = confirmPassword.getText().toString();
        if (!userNameText.equals("") && !phoneNumberText.equals("") && !passwordText.equals("") && !confirmPasswordText.equals("")) {
            if (!passwordText.equals(confirmPasswordText)) {
                Toast.makeText(this, "密码填写不一致，请仔细检查", Toast.LENGTH_SHORT).show();
                return false;
            } else
                return true;
        } else {
            Toast.makeText(this, "请填写完整", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 账号密码注册
     */
    private void signUp(final View view) {
        final _User user = new _User();
        user.setUsername(userNameText);
        user.setMobilePhoneNumber(phoneNumberText);
        user.setPassword(passwordText);
        user.signUp(new SaveListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                dialog.dismiss();
                if (e == null) {
                    showDialog();
                } else {
                    Snackbar.make(view, "注册失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 登录
     */
    private void signIn() {
        BmobUser.loginByAccount(userNameText, passwordText, new LogInListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                dialog.dismiss();
                if (e == null) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                } else {
                    Toast.makeText(RegisterActivity.this, "登录失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                        dialog.show();
                        signIn();
                    }
                });

        builder.setCancelable(false);
        builder.show();
    }

}

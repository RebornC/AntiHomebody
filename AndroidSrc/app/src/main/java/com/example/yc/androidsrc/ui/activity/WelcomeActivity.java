package com.example.yc.androidsrc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.model._User;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobUser;

/**
 * 欢迎界面
 *
 * @author RebornC
 * Created on 2018/12/12.
 */

public class WelcomeActivity extends AppCompatActivity {

    private Intent it;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏

        // 判断是否存在当前用户，如果存在，则直接进入主界面，不用再次登录
        _User currentUser = BmobUser.getCurrentUser(_User.class);
        if (currentUser != null) {
            it = new Intent(this, MainActivity.class);
            // 假若用户通过桌面 widget 启动 App，则带上指定的跳转信息
            if (getIntent() != null) {
                it.putExtra("key", getIntent().getStringExtra("key"));
            }
        } else {
            // 不存在当前用户，转向登录界面
            it = new Intent(this, LoginActivity.class);
        }

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(it); // 执行意图
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        };
        timer.schedule(task, 1000 * 2); // 2秒后跳转
    }

}
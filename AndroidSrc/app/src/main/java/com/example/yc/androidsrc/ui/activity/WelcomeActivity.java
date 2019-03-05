package com.example.yc.androidsrc.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.model.Advertisement;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.utils.SharedPreferencesUtil;
import com.example.yc.androidsrc.utils.ToastUtil;
import com.umeng.message.PushAgent;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobUser;

/**
 * 欢迎界面
 *
 * @author RebornC
 * Created on 2018/12/12.
 */

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent mIntent;
    private ImageView advertisementImage;
    private Button timerBtn;
    private CountDownTimer advertisementTimer;
    private Integer ms = 4;
    private String httpUrl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏

        // 应用数据统计接口
        PushAgent.getInstance(this).onAppStart();

        initView();
        adCountDown();
        verifyNetwork();
    }

    public void initView() {
        advertisementImage = (ImageView) findViewById(R.id.ad_image);
        advertisementImage.setOnClickListener(this);
        timerBtn = (Button) findViewById(R.id.ad_timer);
        timerBtn.setOnClickListener(this);
        httpUrl = SharedPreferencesUtil.get(this, "httpUrl", "").toString();
        // 检查当前是否存在广告图，如果存在，则直接从本地文件中读取，否则加载默认图片
        File PicFile = new File(Environment.getExternalStorageDirectory(), "advertisementImage.jpg");
        if (PicFile.exists()) {
            // 加载本地图片的两种方式
            advertisementImage.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/advertisementImage.jpg"));
            // ad_image.setImageURI(Uri.fromFile(PicFile));
        } else {
            // 加载默认图片
            // advertisementImage.setImageResource(R.mipmap.coopen);
            SharedPreferencesUtil.put(WelcomeActivity.this, "version", "0");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ad_image:
                // 如果存在广告链接，则跳转到相应的webview界面
                if (!httpUrl.equals("")) {
                    advertisementTimer.cancel();
                    mIntent = new Intent(WelcomeActivity.this, WebViewActivity.class);
                    mIntent.putExtra("httpUrl", httpUrl);
                    mIntent.putExtra("isAdvertisement", true);
                    startActivity(mIntent);
                }
                break;
            case R.id.ad_timer:
                advertisementTimer.cancel();
                goNextActivity();
                break;
        }
    }

    /**
     * 广告倒计时
     */
    public void adCountDown() {
        advertisementTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) { // 第一个参数为总计时，第二个参数为计时速度
                ms -= 1;
                timerBtn.setText("跳过 " + ms + " s");
            }

            @Override
            public void onFinish() {
                goNextActivity();
            }
        }.start();
    }

    /**
     * 跳转下一界面
     */
    public void goNextActivity() {
        // 判断是否存在当前用户，如果存在，则直接进入主界面，不用再次登录
        _User currentUser = BmobUser.getCurrentUser(_User.class);
        if (currentUser != null) {
            mIntent = new Intent(this, MainActivity.class);
            // 假若用户通过桌面 widget 启动 App，则带上指定的跳转信息
            if (getIntent() != null) {
                mIntent.putExtra("key", getIntent().getStringExtra("key"));
            }
        } else {
            // 不存在当前用户，转向登录界面
            mIntent = new Intent(this, LoginActivity.class);
        }
        startActivity(mIntent); // 执行意图
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    /**
     * 先判断是否有可用网络
     * 使用ConnectivityManager获取手机所有连接管理对象，manager获取NetworkInfo对象
     * 最后判断当前网络状态是否为连接状态即可，如果有网，则获取最新的广告数据
     */
    public void verifyNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if ((networkInfo == null) || !networkInfo.isConnected()) {
            ToastUtil.showShort(WelcomeActivity.this, "当前网络不可用");
            return;
        } else {
            // 网络获取广告资源
            Advertisement advertisement = new Advertisement();
            advertisement.getAdvertisementMsg(this);
        }
    }

}
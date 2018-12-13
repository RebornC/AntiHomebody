package com.example.yc.androidsrc.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.example.yc.androidsrc.presenter.MainPresenterCompl;
import com.example.yc.androidsrc.presenter.impl.IMainPresenter;
import com.example.yc.androidsrc.ui.fragment.MenuFragment;
import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.ui.fragment.TabFragment1;
import com.example.yc.androidsrc.ui.fragment.TabFragment2;
import com.example.yc.androidsrc.ui.fragment.TabFragment3;
import com.example.yc.androidsrc.ui.fragment.TabFragment4;
import com.example.yc.androidsrc.ui.fragment.TabFragment5;
import com.example.yc.androidsrc.ui.impl.IMainView;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 *
 * @author RebornC
 * Created on 2018/11/26.
 */

public class MainActivity extends AppCompatActivity implements IMainView {

    private IMainPresenter mainPresenter;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private CardView cardView;
    private FrameLayout contentFrameLayout;
    private Fragment currentFragment;
    private List<Fragment> tabFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // 设置组件属性并加载
        initView();
        // 动态权限申请
        applyPermissions(MainActivity.this);
    }

    public void initView() {
        mainPresenter = new MainPresenterCompl(this);

        cardView = (CardView) findViewById(R.id.card_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(""); // 不显示程序应用名
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        tabFragments.add(new TabFragment1());
        tabFragments.add(new TabFragment2());
        tabFragments.add(new TabFragment3());
        tabFragments.add(new TabFragment4());
        tabFragments.add(new TabFragment5());

        contentFrameLayout = (FrameLayout) findViewById(R.id.content_view);
        currentFragment = tabFragments.get(0);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_view, currentFragment).commit();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT); // 菜单滑动时content不被阴影覆盖

        /**
         * 监听抽屉的滑动事件
         */
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = mDrawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;
                float leftScale = 0.5f + slideOffset * 0.5f;
                mMenu.setAlpha(leftScale);
                mMenu.setScaleX(leftScale);
                mMenu.setScaleY(leftScale);
                mContent.setPivotX(0);
                mContent.setPivotY(mContent.getHeight() * 2/3);
                mContent.setScaleX(rightScale);
                mContent.setScaleY(rightScale);
                mContent.setTranslationX(mMenu.getWidth() * slideOffset * 0.9f);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                cardView.setRadius(20);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                cardView.setRadius(0);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    public void applyPermissions(Activity activity) {
        // 交给presenter逻辑层处理
        mainPresenter.verifyPermissions(activity);
    }


    /**
     * 切换主视图的fragment，避免重复实例化加载
     * @param position
     */
    public void switchFragment(int position) {
        Fragment fragment = tabFragments.get(position);
        if (currentFragment != fragment) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (fragment.isAdded()) {
                transaction.hide(currentFragment)
                        .show(fragment)
                        .commit();
            } else {
                transaction.hide(currentFragment)
                        .add(R.id.content_view, fragment)
                        .commit();
            }
            currentFragment = fragment;
        }
    }

    /**
     * onNewIntent方法的使用在于如果activity已经开启了，并设置了启动模式为：android:launchMode="singleTask"的时候
     * 当再次使用intent来启动这个activtiy的时候就会先进入onNewIntent()这个方法，接着再onResart()-->onStart()-->onResume()
     * 在接受通知栏的函数里面可以传递参数，在该activity的onNewIntent进行判断处理，指定需要显示的fragment界面
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getNotify(intent);
    }

    /**
     * 如果系统由于内存不足把该Activity释放掉了，那么点击通知栏再次调用的时候会重新启动Activity
     * 即执行onCreate()-->onStart()-->onResume()等，所以在onResume()里也进行参数传递
     */
    @Override
    protected void onResume() {
        super.onResume();
        getNotify(getIntent());
    }

    private void getNotify(Intent intent){
        String value = intent.getStringExtra("toValue");
        if(!TextUtils.isEmpty(value)) {
            switch (value) {
                case "switchFragment1":
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    MenuFragment menuFragment = (MenuFragment) fragmentManager.findFragmentById(R.id.nav_view); // 强制转换
                    menuFragment.setItemChecked(1); // 菜单选项切换到对应一栏
                    switchFragment(1); // 切换到对应的fragment界面
                    break;
            }
        }
        super.onNewIntent(intent);
    }

    /**
     * 权限申请的结果回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        // 用户拒绝权限，判断是否勾选禁止后不再询问
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[i]);
                        if (showRequestPermission) {
                            // 重新申请权限
                            mainPresenter.verifyPermissions(MainActivity.this);
                            return;
                        } else {
                            // 已禁止再次询问，提示用户手动设置权限
                            mainPresenter.showMissingPermissionDialog(MainActivity.this);
                        }
                    }
                }
            default:
                break;
        }
    }

    /**
     * 用户是否同意手动设置权限的结果回调
     * @param isChecked
     */
    @Override
    public void onShowMissingPermissionDialog(boolean isChecked) {
        if (isChecked) {
            // 前往设置界面授予权限
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } else
            // 退出应用
            finish();
    }
}

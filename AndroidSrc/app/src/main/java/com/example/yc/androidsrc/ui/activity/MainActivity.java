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

import com.example.yc.androidsrc.ui.fragment.MenuFragment;
import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.ui.fragment.TabFragment1;
import com.example.yc.androidsrc.ui.fragment.TabFragment2;
import com.example.yc.androidsrc.ui.fragment.TabFragment3;
import com.example.yc.androidsrc.ui.fragment.TabFragment4;
import com.example.yc.androidsrc.ui.fragment.TabFragment5;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // 存储所需要申请的动态权限
    private static String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.BODY_SENSORS};

    private DrawerLayout mDrawerLayout;
    private FrameLayout contentFrameLayout;
    private Fragment currentFragment;
    private List<Fragment> tabFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // 动态权限申请
        verifyPermissions(MainActivity.this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT); // 菜单滑动时content不被阴影覆盖

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(""); // 不显示程序应用名
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        final CardView cardView = (CardView) findViewById(R.id.card_view);

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

//    public void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment current = fragmentManager.findFragmentById(R.id.content_view);
//        if (current != null) {
//            if (current.getClass().equals(fragment.getClass())) {
//                // 如果是同个fragment，则维持原样
//                return;
//            }
//        }
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.content_view, fragment);
//        transaction.addToBackStack(null); // 模拟返回栈
//        transaction.commit();
//    }

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
     * 针对 Android 6.0 以上机型进行动态权限申请
     * @param activity
     */
    public static void verifyPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT > 23) {
            List<String> mPermissionList = new ArrayList<>();
            // 检查是否已经授予了权限
            for (int i = 0; i < permissions.length; i++) {
                if (ActivityCompat.checkSelfPermission(activity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);
                }
            }
            if (!mPermissionList.isEmpty()) {
                String[] mPermissions = mPermissionList.toArray(new String[mPermissionList.size()]); // 将List转为数组
                ActivityCompat.requestPermissions(activity, mPermissions, 1);
            }
        }
    }

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
                            verifyPermissions(MainActivity.this);
                            return;
                        } else {
                            // 已禁止再次询问，提示用户手动设置权限
                            showMissingPermissionDialog();
                        }
                    }
                }
            default:
                break;
        }
    }

    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("请前往设置界面授予权限，否则将退出应用");
        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);
        builder.show();
    }

    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

}

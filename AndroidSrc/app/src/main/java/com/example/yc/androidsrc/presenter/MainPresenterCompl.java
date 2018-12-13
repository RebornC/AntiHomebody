package com.example.yc.androidsrc.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.example.yc.androidsrc.presenter.impl.IMainPresenter;
import com.example.yc.androidsrc.ui.impl.IMainView;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面的逻辑层
 *
 * @author RebornC
 * Created on 2018/12/14.
 */

public class MainPresenterCompl implements IMainPresenter {

    private IMainView iMainView;

    // 存储所需要申请的动态权限
    private static String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.BODY_SENSORS};

    public MainPresenterCompl(IMainView iMainView) {
        this.iMainView = iMainView;
    }

    /**
     * 针对 Android 6.0 以上机型进行动态权限申请
     * @param activity
     */
    @Override
    public void verifyPermissions(Activity activity) {
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
    public void showMissingPermissionDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("提示");
        builder.setMessage("请前往设置界面授予权限，否则将退出应用");
        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        iMainView.onShowMissingPermissionDialog(false);
                    }
                });
        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        iMainView.onShowMissingPermissionDialog(true);
                    }
                });

        builder.setCancelable(false);
        builder.show();
    }

}

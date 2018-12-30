package com.example.yc.androidsrc.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.db.UserDataDao;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.ui.activity.LoginActivity;
import com.example.yc.androidsrc.ui.activity.SQLdataActivity;
import com.example.yc.androidsrc.utils.ToastUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author RebornC
 * Created on 2018/11/28.
 */

public class TabFragment5 extends Fragment implements View.OnClickListener{

    private String objectId = (String) BmobUser.getObjectByKey("objectId");
    private EditText name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_5, container, false);

        //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        TextView title = (TextView) getActivity().findViewById(R.id.title);
        title.setText("");
        title.setTextColor(getResources().getColor(R.color.white));

//        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
//        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        name = (EditText) view.findViewById(R.id.name);
        
        Button btn1 = (Button) view.findViewById(R.id.btn1);
        btn1.setOnClickListener(this);

        Button btn2 = (Button) view.findViewById(R.id.btn2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) view.findViewById(R.id.btn3);
        btn3.setOnClickListener(this);

        Button btn4 = (Button) view.findViewById(R.id.btn4);
        btn4.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                // 改名
                final _User p2 = new _User();
                p2.setUsername(name.getText().toString());
                p2.update(objectId, new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            ToastUtil.showShort(getActivity(), "改名成功:"+p2.getUpdatedAt());
                        }else{
                            ToastUtil.showShort(getActivity(), "改名失败：" + e.getMessage());
                        }
                    }

                });
                break;
            case R.id.btn2:
                // 退出登录，同时清除缓存用户对象
                BmobUser.logOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn3:
                Intent it = new Intent(getActivity(), SQLdataActivity.class);
                startActivity(it);
                break;
            case R.id.btn4:
                /**
                 * 有时候会因为网络问题导致云端数据与本地数据不一致
                 * 比如本地数据更新了，云端数据却因为没网而无法更新
                 * 所以增加这个功能，防止这个情况
                 */
                _User curUser = BmobUser.getCurrentUser(_User.class);
                String objectId = curUser.getObjectId();
                UserDataDao userDataDao = new UserDataDao(getActivity());
                _User sqlUser = userDataDao.getUserDataById(objectId);
                curUser.setCurLevel(sqlUser.getCurLevel());
                curUser.setNumerator(sqlUser.getNumerator());
                curUser.setDenominator(sqlUser.getDenominator());
                curUser.setCurEnergy(sqlUser.getCurEnergy());
                curUser.setTotalEnergy(sqlUser.getTotalEnergy());
                curUser.update(objectId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            ToastUtil.showShort(getActivity(), "同步成功");
                        } else {
                            ToastUtil.showShort(getActivity(), "同步失败");
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // onPause()
        } else {
            // onResume()
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
            TextView title = (TextView) getActivity().findViewById(R.id.title);
            title.setText("");
            title.setTextColor(getResources().getColor(R.color.white));
        }
    }

}

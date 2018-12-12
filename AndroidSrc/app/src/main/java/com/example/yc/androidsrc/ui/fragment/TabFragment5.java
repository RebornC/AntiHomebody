package com.example.yc.androidsrc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.ui.activity.LoginActivity;

import cn.bmob.v3.BmobUser;

/**
 * Created by yc on 2018/11/28.
 */

public class TabFragment5 extends Fragment implements View.OnClickListener{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_5, container, false);

        Button btn = (Button) view.findViewById(R.id.btn);
        btn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                // 退出登录，同时清除缓存用户对象
                BmobUser.logOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}

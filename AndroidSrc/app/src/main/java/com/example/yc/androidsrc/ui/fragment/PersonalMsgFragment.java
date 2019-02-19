package com.example.yc.androidsrc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.PersonalMsgPresenterCompl;
import com.example.yc.androidsrc.presenter.impl.IPersonalMsgPresenter;
import com.example.yc.androidsrc.ui.activity.DynamicActivity;
import com.example.yc.androidsrc.ui.activity.LoginActivity;
import com.example.yc.androidsrc.ui.activity.SelfTalkingActivity;
import com.example.yc.androidsrc.ui.activity.UserSettingsActivity;
import com.example.yc.androidsrc.ui.activity.WebViewActivity;
import com.example.yc.androidsrc.ui.impl.IPersonalMsgView;
import com.example.yc.androidsrc.utils.ToastUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;

/**
 * @author RebornC
 * Created on 2018/11/28.
 */

public class PersonalMsgFragment extends Fragment implements IPersonalMsgView, View.OnClickListener {

    private IPersonalMsgPresenter personalMsgPresenter;
    private _User curUser;

    private View view;
    private Toolbar toolbar;
    private TextView title;
    private ImageView userHead;
    private TextView userName;
    private TextView userSignature;
    private AlertDialog dialog;

    private LinearLayout dynamic;
    private LinearLayout wormhole;

    private ListView introListView;
    private SimpleAdapter introAdapter;
    private List<Map<String,Object>> introlistData;

    private ListView settingListView;
    private SimpleAdapter settingAdapter;
    private List<Map<String,Object>> settinglistData;

    private static final String key = "CODE";
    private static final int AccseeUserSettingsActivity = 0;
    private static final int UPLOAD_HEADPORTRAIT_CODE = 1; // 上传头像
    private static final int UPLOAD_MESSAGE_CODE = 2; // 上传其他用户信息

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_msg, container, false);
        initView();
        return view;
    }

    public void initView() {
        personalMsgPresenter = new PersonalMsgPresenterCompl(this);
        curUser = BmobUser.getCurrentUser(_User.class);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        title = (TextView) getActivity().findViewById(R.id.title);
        title.setText("");
        userHead = (ImageView) view.findViewById(R.id.user_head);
        if (curUser.getHeadPortrait() != null) {
            Glide.with(getActivity()).load(curUser.getHeadPortrait().getFileUrl()).into(userHead);
        }
        userName = (TextView) view.findViewById(R.id.user_name);
        userName.setText(curUser.getNickName());
        userSignature = (TextView) view.findViewById(R.id.user_signature);
        if (curUser.getSignature() != null) {
            userSignature.setText(curUser.getSignature());
        }

        dynamic = (LinearLayout) view.findViewById(R.id.dynamic);
        dynamic.setOnClickListener(this);
        wormhole = (LinearLayout) view.findViewById(R.id.wormhole);
        wormhole.setOnClickListener(this);

        // 初始化列表1
        introListView = (ListView) view.findViewById(R.id.intro_list_view);
        introlistData = new ArrayList<>();
        Integer[] intro_list_icon_id = {R.mipmap.book_icon, R.mipmap.compass_icon};
        String[] intro_list_text = {"关于非宅", "用户指南"};
        for (int i = 0; i < 2; i++) {
            Map<String,Object> temp = new LinkedHashMap<>();
            temp.put("icon", intro_list_icon_id[i]);
            temp.put("text", intro_list_text[i]);
            introlistData.add(temp);
        }
        introAdapter = new SimpleAdapter(getActivity(), introlistData, R.layout.list_view_item_1, new String[] {"icon","text"}, new int[] {R.id.icon, R.id.text});
        introListView.setAdapter(introAdapter);
        setListViewHeightBasedOnChildren(introListView, introAdapter);
        introListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intentToIntro = new Intent(getActivity(), WebViewActivity.class);
                        intentToIntro.putExtra("httpUrl", "https://github.com/RebornC/AntiHomebody/blob/master/docs/%E5%85%B3%E4%BA%8E%E9%9D%9E%E5%AE%85.md");
                        startActivity(intentToIntro);
                        break;
                    case 1:
                        Intent intentToCompass = new Intent(getActivity(), WebViewActivity.class);
                        intentToCompass.putExtra("httpUrl", "https://github.com/RebornC/AntiHomebody/blob/master/docs/%E7%94%A8%E6%88%B7%E6%8C%87%E5%8D%97.md");
                        startActivity(intentToCompass);
                        break;
                }
            }
        });

        // 初始化列表2
        settingListView = (ListView) view.findViewById(R.id.setting_list_view);
        settinglistData = new ArrayList<>();
        Integer[] settings_list_icon_id = {R.drawable.ic_settings_24dp, R.drawable.ic_cloud_queue_24dp, R.drawable.ic_power_settings_new_24dp};
        String[] settings_list_text = {"编辑资料", "同步云端", "退出登录"};
        for (int i = 0; i < 3; i++) {
            Map<String,Object> temp = new LinkedHashMap<>();
            temp.put("icon", settings_list_icon_id[i]);
            temp.put("text", settings_list_text[i]);
            settinglistData.add(temp);
        }
        settingAdapter = new SimpleAdapter(getActivity(), settinglistData, R.layout.list_view_item_1, new String[] {"icon","text"}, new int[] {R.id.icon, R.id.text});
        settingListView.setAdapter(settingAdapter);
        setListViewHeightBasedOnChildren(settingListView, settingAdapter);
        settingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(getActivity(), UserSettingsActivity.class);
                        startActivityForResult(intent, AccseeUserSettingsActivity);
                        break;
                    case 1:
                        popupSyncDialog();
                        break;
                    case 2:
                        popupLogoutDialog();
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dynamic:
                startActivity(new Intent(getActivity(), DynamicActivity.class));
                break;
            case R.id.wormhole:
                startActivity(new Intent(getActivity(), SelfTalkingActivity.class));
                break;
            case R.id.btn_cancel_sync:
                dialog.dismiss();
                break;
            case R.id.btn_comfirm_sync:
                personalMsgPresenter.syncBackend(getActivity());
                dialog.dismiss();
                break;
            case R.id.btn_cancel_logout:
                dialog.dismiss();
                break;
            case R.id.btn_comfirm_logout:
                personalMsgPresenter.logOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                dialog.dismiss();
                break;
        }
    }

    /**
     * 动态修改listview高度，使得listview能完全展开
     * @param listView
     */
    private void setListViewHeightBasedOnChildren(ListView listView, SimpleAdapter simpleAdapter) {
        if (listView == null) {
            return;
        }
        if (simpleAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < simpleAdapter.getCount(); i++) {
            View listItem = simpleAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (simpleAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public void popupSyncDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.sync_custom_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        Button cancelSyncBtn = (Button)view.findViewById(R.id.btn_cancel_sync);
        Button comfirmSyncBtn = (Button)view.findViewById(R.id.btn_comfirm_sync);
        // 取消/确定按钮监听事件处理
        cancelSyncBtn.setOnClickListener(this);
        comfirmSyncBtn.setOnClickListener(this);
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.getWindow().setLayout(800, 400);
    }

    public void popupLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.logout_custom_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        Button cancelLogoutBtn = (Button)view.findViewById(R.id.btn_cancel_logout);
        Button comfirmLogoutBtn = (Button)view.findViewById(R.id.btn_comfirm_logout);
        // 取消/确定按钮监听事件处理
        cancelLogoutBtn.setOnClickListener(this);
        comfirmLogoutBtn.setOnClickListener(this);
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.getWindow().setLayout(800, 400);
    }

    @Override
    public void onUpdateData(boolean result, int resultCode, String message) {
        ToastUtil.showShort(getActivity(), message);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // onPause()
        } else {
            // onResume()
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            title.setText("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 判断用户数据是否变化，若变化则刷新界面
        if (requestCode == AccseeUserSettingsActivity && resultCode == getActivity().RESULT_OK) {
            Bundle bundle = data.getBundleExtra(key);
            List<Integer> codeList = bundle.getIntegerArrayList(key);
            if (codeList.contains(UPLOAD_HEADPORTRAIT_CODE)) {
                curUser = BmobUser.getCurrentUser(_User.class);
                if (curUser.getHeadPortrait() != null) {
                    Glide.with(getActivity()).load(curUser.getHeadPortrait().getFileUrl()).into(userHead);
                }
            }
            if (codeList.contains(UPLOAD_MESSAGE_CODE)) {
                curUser = BmobUser.getCurrentUser(_User.class);
                userName.setText(curUser.getNickName());
                if (curUser.getSignature() != null) {
                    userSignature.setText(curUser.getSignature());
                }
            }
        }
    }
}

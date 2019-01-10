package com.example.yc.androidsrc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.db.UserDataDao;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.PersonalMsgPresenterCompl;
import com.example.yc.androidsrc.presenter.impl.IPersonalMsgPresenter;
import com.example.yc.androidsrc.ui.activity.LoginActivity;
import com.example.yc.androidsrc.ui.activity.SQLdataActivity;
import com.example.yc.androidsrc.ui.activity.UserSettingsActivity;
import com.example.yc.androidsrc.ui.impl.IPersonalMsgView;
import com.example.yc.androidsrc.utils.ToastUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

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

    private ListView settingListView;
    private SimpleAdapter settingAdapter;
    private List<Map<String,Object>> settinglistData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_msg, container, false);

        initView();

        Button btn1 = (Button) view.findViewById(R.id.btn1);
        btn1.setOnClickListener(this);

        Button btn2 = (Button) view.findViewById(R.id.btn2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) view.findViewById(R.id.btn3);
        btn3.setOnClickListener(this);

        Button btn4 = (Button) view.findViewById(R.id.btn4);
        btn4.setOnClickListener(this);

        Button btn5 = (Button) view.findViewById(R.id.btn5);
        btn4.setOnClickListener(this);

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
        userName.setText(curUser.getUsername());
        userSignature = (TextView) view.findViewById(R.id.user_signature);
        if (curUser.getSignature() != null) {
            userSignature.setText(curUser.getSignature());
        }

        // 初始化列表
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
                        startActivity(new Intent(getActivity(), UserSettingsActivity.class));
                        break;
                    case 1:
                        break;
                    case 2:
                        personalMsgPresenter.logOut();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn1:
                // 改名
//                final _User p2 = new _User();
//                p2.setUsername(name.getText().toString());
//                p2.update(objectId, new UpdateListener() {
//
//                    @Override
//                    public void done(BmobException e) {
//                        if(e==null){
//                            ToastUtil.showShort(getActivity(), "改名成功:"+p2.getUpdatedAt());
//                        }else{
//                            ToastUtil.showShort(getActivity(), "改名失败：" + e.getMessage());
//                        }
//                    }
//
//                });
//                break;
//            case R.id.btn2:
//                // 退出登录，同时清除缓存用户对象
//                BmobUser.logOut();
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.btn3:
//                Intent it = new Intent(getActivity(), SQLdataActivity.class);
//                startActivity(it);
//                break;
//            case R.id.btn4:
//                /**
//                 * 有时候会因为网络问题导致云端数据与本地数据不一致
//                 * 比如本地数据更新了，云端数据却因为没网而无法更新
//                 * 所以增加这个功能，防止这个情况
//                 */
//                _User curUser = BmobUser.getCurrentUser(_User.class);
//                String objectId = curUser.getObjectId();
//                UserDataDao userDataDao = new UserDataDao(getActivity());
//                _User sqlUser = userDataDao.getUserDataById(objectId);
//                curUser.setCurLevel(sqlUser.getCurLevel());
//                curUser.setNumerator(sqlUser.getNumerator());
//                curUser.setDenominator(sqlUser.getDenominator());
//                curUser.setCurEnergy(sqlUser.getCurEnergy());
//                curUser.setTotalEnergy(sqlUser.getTotalEnergy());
//                curUser.update(objectId, new UpdateListener() {
//                    @Override
//                    public void done(BmobException e) {
//                        if (e == null) {
//                            ToastUtil.showShort(getActivity(), "同步成功");
//                        } else {
//                            ToastUtil.showShort(getActivity(), "同步失败");
//                        }
//                    }
//                });
//                break;
//            case R.id.btn5:
//                break;
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // onPause()
        } else {
            // onResume()
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
            title.setText("");
        }
    }

}

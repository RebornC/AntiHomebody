package com.example.yc.androidsrc.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yc.androidsrc.model.MenuItem;
import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.adapter.MenuItemAdapter;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * 主界面左侧菜单
 *
 * @author RebornC
 * Created on 2018/11/26.
 */

public class MenuFragment extends Fragment {

    private _User curUser;

    private View navView;
    private ImageView userHead;
    private TextView userNameTv;
    private ListView mListView;
    private List<MenuItem> menuItemList = new ArrayList<>();
    private MenuItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        navView = inflater.inflate(R.layout.fragment_menu, container, false);

        initView();
        clickEvents();

        return navView;
    }

    public void initView() {
        // find view
        curUser = BmobUser.getCurrentUser(_User.class);
        userHead = (ImageView) navView.findViewById(R.id.user_head);
        if (curUser.getHeadPortrait() != null) {
            Glide.with(getActivity()).load(curUser.getHeadPortrait().getFileUrl()).into(userHead);
        }
        userNameTv = (TextView) navView.findViewById(R.id.user_name);
        userNameTv.setText(curUser.getUsername());
        mListView = (ListView) navView.findViewById(R.id.menu_list_view);
        mListView.setDivider(null);
        // init ListView
        String[] data_zh = getResources().getStringArray(R.array.menu_zh);
        String[] data_en = getResources().getStringArray(R.array.menu_en);
        for (int i = 0; i < data_zh.length; i++) {
            MenuItem menuItem = new MenuItem(data_zh[i], data_en[i]);
            menuItemList.add(menuItem);
        }
        adapter = new MenuItemAdapter(getActivity(), R.layout.menu_list_item, menuItemList);
        mListView.setAdapter(adapter);
    }

    public void clickEvents() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.changeSelected(position);
                MainActivity activity = (MainActivity) getActivity();
                DrawerLayout mDrawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
                mDrawerLayout.closeDrawer(Gravity.START);
                activity.switchFragment(position);
            }
        });

    }

    /**
     * 默认第几项被选中
     * @param position
     */
    public void setItemChecked(int position) {
        adapter.changeSelected(position);
    }

}

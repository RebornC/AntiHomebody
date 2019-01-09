package com.example.yc.androidsrc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.adapter.ViewPagerAdapter;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.GrowUpPresenterCompl;
import com.example.yc.androidsrc.presenter.StepCounterPresenterCompl;
import com.example.yc.androidsrc.presenter.impl.IGrowUpPresenter;
import com.example.yc.androidsrc.ui.impl.IGrowUpView;
import com.example.yc.androidsrc.utils.ToastUtil;
import com.example.yc.androidsrc.views.GifView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * @author RebornC
 * Created on 2018/11/28.
 */

public class GrowUpFragment extends Fragment implements IGrowUpView, View.OnClickListener {

    private View view;
    private IGrowUpPresenter growUpPresenter;
    private String objectId;
    private _User curUser;

    private Toolbar toolbar;
    private TextView title;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private ImageView tab_0;
    private ImageView tab_1;
    private ImageView tab_2;
    private ImageView rocket;
    private TextView tabTitle;
    private String[] tabName;
    private EnergyHouseFragment energyHouseFragment;
    private ActiveDegreeFragment activeDegreeFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_grow_up, container, false);
        // getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        initView();
        return view;
    }

    public void initView() {
        // 先通过Bmob本地缓存获取用户Id，在通过Id获取数据库中的本地记录
        growUpPresenter = new GrowUpPresenterCompl(this);
        objectId = BmobUser.getCurrentUser(_User.class).getObjectId();
        curUser = growUpPresenter.getUserDate(getActivity(), objectId);

        // toolbar设置
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        title = (TextView) getActivity().findViewById(R.id.title);
        title.setText("");

        // ViewPager设置
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        fragmentList = new ArrayList<>();
        energyHouseFragment = new EnergyHouseFragment();
        activeDegreeFragment = new ActiveDegreeFragment();
        fragmentList.add(new EnergyHouseFragment());
        fragmentList.add(energyHouseFragment);
        fragmentList.add(activeDegreeFragment);
        titleList = new ArrayList<>();
        tabName = getResources().getStringArray(R.array.tab_name);
        titleList.add(tabName[0]);
        titleList.add(tabName[1]);
        titleList.add(tabName[2]);
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchViewPager(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 底部导航设置
        tab_0 = (ImageView) view.findViewById(R.id.tab_0);
        tab_1 = (ImageView) view.findViewById(R.id.tab_1);
        tab_2 = (ImageView) view.findViewById(R.id.tab_2);
        tab_0.setOnClickListener(this);
        tab_1.setOnClickListener(this);
        tab_2.setOnClickListener(this);
        tab_0.setTag('0');
        tab_1.setTag('1');
        tab_2.setTag('0');
        rocket = (ImageView) view.findViewById(R.id.rocket);
        rocket.bringToFront(); // 显示在最顶层
        tabTitle = (TextView) view.findViewById(R.id.tab_title);
        tabTitle.setText(tabName[1]);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_0:
                viewPager.setCurrentItem(0);
                switchViewPager(0);
                break;
            case R.id.tab_1:
                viewPager.setCurrentItem(1);
                switchViewPager(1);
                break;
            case R.id.tab_2:
                viewPager.setCurrentItem(2);
                switchViewPager(2);
                break;
        }
    }


    /**
     * viewpager切换后，bottomNavigation发生相应的变化
     *
     * @param position
     */
    public void switchViewPager(int position) {
        if (position == 0) {
            if (tab_0.getTag().equals('0')) {
                tab_0.setTag('1');
                tab_1.setTag('0');
                tab_2.setTag('0');
                tab_0.setImageResource(R.mipmap.circle_blue_icon);
                tab_1.setImageResource(R.mipmap.circle_gray_icon);
                tab_2.setImageResource(R.mipmap.circle_gray_icon);
                tabTitle.setText(tabName[0]);
            }
        } else if (position == 1) {
            if (tab_1.getTag().equals('0')) {
                tab_1.setTag('1');
                tab_0.setTag('0');
                tab_2.setTag('0');
                tab_1.setImageResource(R.mipmap.circle_blue_icon);
                tab_0.setImageResource(R.mipmap.circle_gray_icon);
                tab_2.setImageResource(R.mipmap.circle_gray_icon);
                tabTitle.setText(tabName[1]);
            }
        } else if (position == 2) {
            if (tab_2.getTag().equals('0')) {
                tab_2.setTag('1');
                tab_0.setTag('0');
                tab_1.setTag('0');
                tab_2.setImageResource(R.mipmap.circle_blue_icon);
                tab_0.setImageResource(R.mipmap.circle_gray_icon);
                tab_1.setImageResource(R.mipmap.circle_gray_icon);
                tabTitle.setText(tabName[2]);
            }
        }
    }


    /**
     * 当该fragment已被实例化但不可视时，重新切换到此fragment时会需要加载这个方法
     * 或者是由可视变为不可视
     * 在这里进行界面的一些改动
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // onPause()
        } else {
            // onResume()
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            title.setText("");
            // 因为此时用户的能量值可能发生了变化，所以要刷新energyHouseFragment中的数据
            energyHouseFragment.initValue();
        }
    }

    @Override
    public void onUpdateData(boolean result, int resultCode, String message) {

    }

}

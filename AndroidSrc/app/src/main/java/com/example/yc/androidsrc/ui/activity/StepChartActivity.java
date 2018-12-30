package com.example.yc.androidsrc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.adapter.ViewPagerAdapter;
import com.example.yc.androidsrc.ui.fragment.MonthStepFragment;
import com.example.yc.androidsrc.ui.fragment.WeekStepFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户步数历史记录统计
 *
 * @author RebornC
 * Created on 2018/12/10.
 */

public class StepChartActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    private List<Fragment> fragmentList;
    private List<String> titleList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_chart);
        initView();
    }

    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("步行数据");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        fragmentList = new ArrayList<>();
        fragmentList.add(new WeekStepFragment());
        fragmentList.add(new MonthStepFragment());
        titleList = new ArrayList<>();
        titleList.add("最近7天");
        titleList.add("最近30天");

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}

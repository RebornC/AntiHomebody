package com.example.yc.androidsrc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.adapter.EnergySourceAdapter;
import com.example.yc.androidsrc.adapter.TimeLineAdapter;
import com.example.yc.androidsrc.model.EnergySourceEntity;
import com.example.yc.androidsrc.model.SelfTalkingEntity;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.DynamicPresenterCompl;
import com.example.yc.androidsrc.presenter.SelfTalkingPresenterCompl;
import com.example.yc.androidsrc.presenter.impl.IDynamicPresenter;
import com.example.yc.androidsrc.presenter.impl.ISelfTalkingPresenter;
import com.example.yc.androidsrc.utils.ToastUtil;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * 用户能量动态界面
 *
 * @author RebornC
 * Created on 2019/1/15.
 */

public class DynamicActivity extends AppCompatActivity implements View.OnClickListener {

    private IDynamicPresenter dynamicPresenter;
    private String userId;
    private ImageView backIcon;
    private ListView mListView;
    private EnergySourceAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        initView();
        initList();
    }

    public void initView() {
        userId = BmobUser.getCurrentUser(_User.class).getObjectId();
        dynamicPresenter = new DynamicPresenterCompl();
        backIcon = (ImageView) findViewById(R.id.back_icon);
        backIcon.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.list_view);
    }

    public void initList() {
        final List<EnergySourceEntity> listData = dynamicPresenter.getSqlData(DynamicActivity.this, userId);
        adapter = new EnergySourceAdapter(DynamicActivity.this, R.layout.dynamic_timeline_list_item, listData);
        mListView.setAdapter(adapter);
        setListViewHeightBasedOnChildren();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_icon:
                finish();
                break;
        }
    }

    /**
     * 动态修改listview高度，使得listview能完全展开
     */
    private void setListViewHeightBasedOnChildren() {
        if (mListView == null) {
            return;
        }
        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, mListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = totalHeight;
        mListView.setLayoutParams(params);
    }

}
